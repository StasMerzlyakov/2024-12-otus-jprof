package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.services.*;

// @AppComponentsContainerConfig(order = 2)
public class AppConfig1 {

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(
            IOService ioService, PlayerService playerService, EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}
