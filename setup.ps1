# setup.ps1
# Installs Ollama (winget/choco), starts the server, and pulls llama3.1.
[Diagnostics.CodeAnalysis.SuppressMessageAttribute('PSUseApprovedVerbs', '')]
param()

$ErrorActionPreference = "Stop"

function Have-Cmd($name) {
  try { Get-Command $name -ErrorAction Stop | Out-Null; return $true } catch { return $false }
}

Write-Host "Installing Ollama (if needed)…"
if (Have-Cmd "winget") {
  winget install -e --id Ollama.Ollama --accept-package-agreements --accept-source-agreements
}
elseif (Have-Cmd "choco") {
  choco install -y ollama
}
else {
  Write-Error "Neither winget nor choco found. Install one of them, or install Ollama manually."
}

Write-Host "Starting Ollama server…"
# If 11434 might be busy, change to: "serve --port 11435"
Start-Process -WindowStyle Hidden -FilePath "ollama" -ArgumentList "serve"
Start-Sleep -Seconds 3

Write-Host "Checking server readiness…"
try {
  $v = Invoke-RestMethod -Uri "http://localhost:11434/api/version" -TimeoutSec 5
  Write-Host "Ollama version:" $v.version
} catch {
  Write-Error "Ollama did not respond. Try: 'ollama serve' in a separate window to see logs."
  exit 1
}

Write-Host "Pulling model: llama3.1 (this can take a while)…"
ollama pull llama3.1

Write-Host "Installed models:"
ollama list

Write-Host "Done."

java -jar target/chatter-1.0-SNAPSHOT.jar
