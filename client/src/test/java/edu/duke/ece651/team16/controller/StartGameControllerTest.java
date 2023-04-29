package edu.duke.ece651.team16.controller;

import static org.mockito.Mockito.*;
import org.mockito.Mockito;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.application.Platform;

@ExtendWith(ApplicationExtension.class)
public class StartGameControllerTest {

    @Start
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/StartGame.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        StartGameController startGameController = fxmlLoader.getController();
        Client client = mock(Client.class);
        startGameController.setClient(client);
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testNewGameButton(FxRobot robot) throws Exception {
        // Click on the "New Game" button
        // robot.clickOn("#newGame");
    }

}
