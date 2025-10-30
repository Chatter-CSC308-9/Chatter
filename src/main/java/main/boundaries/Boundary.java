package main.boundaries;

import java.util.ArrayList;
import java.util.List;

public abstract class Boundary {
    private final List<Object> controllers = new ArrayList<>();
    public void addController(Object controller) {
        controllers.add(controller);
    }
    public List<Object> getControllers() {
        return controllers;
    }
}
