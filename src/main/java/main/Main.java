package main;

import javafx.application.Application;
import main.boundaries.screens.Current;
import main.controllers.EditWorkController;

public class Main {
    public static void main(String[] args) {
        //EditWorkController ewc = new EditWorkController();
        //Current currentBoundary = new Current(ewc);
        Application.launch(WindowLauncher.class, "Shell");
    }
}