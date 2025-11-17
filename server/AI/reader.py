import argparse
import json
import os
import sys
import time
import shutil
import subprocess
subprocess.run([sys.executable, "-m", "pip", "install", "ollama"])
subprocess.run([sys.executable, "-m", "ollama", "pull", "llama3.1"])
subprocess.check_call([sys.executable, "-m", "pip", "install", "requests"])
subprocess.check_call([sys.executable, "-m", "pip", "install", "dotenv"])

import requests
from pathlib import Path
from dotenv import load_dotenv
load_dotenv()

def positive_int(v):
    v = int(v)
    if v <= 0: raise argparse.ArgumentTypeError("must be > 0")
    return v

def build_args():
    p = argparse.ArgumentParser(
        description="Run a local chat using Ollama. Auto-starts server and auto-pulls model if needed."
    )
    p.add_argument("--model", default=os.getenv("MODEL", "llama3.1"),
                   help="Ollama model tag (e.g., llama3.1, mistral:7b-instruct)")
    src = p.add_mutually_exclusive_group(required=True)
    src.add_argument("--prompt_file", help="Path to a text file with your prompt")
    src.add_argument("--prompt", help="Inline prompt text")
    p.add_argument("--output", default="feedback.txt", help="Where to write the model output")
    p.add_argument("--host", default=os.getenv("OLLAMA_HOST", "http://localhost:11434"),
                   help="Ollama host URL")
    p.add_argument("--max_new_tokens", type=positive_int, default=int(os.getenv("MAX_NEW_TOKENS", "512")))
    p.add_argument("--temperature", type=float, default=float(os.getenv("TEMPERATURE", "0.7")))
    p.add_argument("--system", default=os.getenv("SYSTEM", "I will provide a message. Please evaluate the phrase with the following priority order: grammatically, intention, and stylistically. Please attempt to keep the same intention throughout. any new phrase provided should keep the rules defined but not take into account the previous prompt. Finally, provide the intitial sentence and the final modified message."),
                   help="System message (chat behavior)")
    p.add_argument("--timeout", type=positive_int, default=int(os.getenv("STARTUP_TIMEOUT", "30")),
                   help="Seconds to wait for server to become ready")
    p.add_argument("--no_autostart", action="store_true",
                   help="Do not try to start `ollama serve` automatically")
    p.add_argument("--verbose", action="store_true", help="More logs")
    return p.parse_args()


def ollama_binary_present():
    return shutil.which("ollama") is not None

def ensure_server_up(host, timeout=30, autostart=True, verbose=False):
    """
    Ensure an Ollama server is responding at host (default http://localhost:11434).
    If not up and autostart=True and `ollama` is on PATH, we try to start it.
    """
    def ready():
        try:
            r = requests.get(f"{host}/api/version", timeout=2)
            return r.status_code == 200
        except Exception:
            return False

    if ready():
        if verbose: print("Ollama server is already running.")
        return

    if not autostart:
        raise RuntimeError("Ollama server is not reachable")

    if not ollama_binary_present():
        raise RuntimeError("`ollama` not found on PATH. Install llama3.1 from https://ollama.com/download ")

    if verbose: print("… starting `ollama serve` in the background")

    proc = subprocess.Popen(
        ["ollama", "serve"],
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL
    )

    start = time.time()
    while time.time() - start < timeout:
        if ready():
            if verbose: print("Ollama server is ready.")
            return
        time.sleep(0.5)

    try:
        proc.terminate()
    except Exception:
        pass
    raise RuntimeError("Failed to start Ollama server within timeout. "
                       "Try running `ollama serve` manually to inspect logs.")

def pull_model_if_needed(host, model, verbose=False):
    if verbose: print(f"… ensuring model '{model}' is present (pulling if needed)")
    url = f"{host}/api/pull"
    body = {"name": model, "stream": True}
    with requests.post(url, json=body, stream=True) as r:
        r.raise_for_status()
        last_status = None
        for line in r.iter_lines():
            if not line:
                continue
            data = json.loads(line.decode("utf-8"))
            status = data.get("status")
            if status and status != last_status and verbose:
                print(f"   - {status}")
            last_status = status
            if data.get("completed"):
                break
    if verbose: print(f"✓ Model '{model}' is ready.")

def run_generate(host, model, out_path, temperature=0.7, max_new_tokens=512):
    url = f"{host}/api/generate"
    prompt_text = f"""
        System: I will provide a message. Please evaluate the phrase with the following priority order:
        1. Grammar
        2. Intention
        3. Style
        Please keep the same intention throughout, and return both the initial and final modified message.
        User message:
        {open(args.prompt_file).read()}
        """
    body = {
        "model": model,
        "stream": True,
        "prompt": prompt_text,
        "options": {
            "temperature": float(temperature),
            "num_predict": int(max_new_tokens)
        }
    }
    os.makedirs(os.path.dirname(out_path) or ".", exist_ok=True)
    with requests.post(url, json=body, stream=True) as r, open(out_path, "w", encoding="utf-8") as out:
        r.raise_for_status()
        for line in r.iter_lines():
            if not line:
                continue
            data = json.loads(line.decode("utf-8"))
            delta = data.get("response", "")
            print(delta, end="", flush=True)
            out.write(delta)
    print(f"\n✓ Wrote response to {out_path}")

if __name__ == "__main__":
    args = build_args()

    run_generate("http://localhost:11434", args.model, out_path=args.output)