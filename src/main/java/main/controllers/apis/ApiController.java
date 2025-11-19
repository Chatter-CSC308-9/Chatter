package main.controllers.apis;

import main.controllers.Controller;

public interface ApiController extends Controller {
    void injectControllerAPIs(Controller controller);
}
