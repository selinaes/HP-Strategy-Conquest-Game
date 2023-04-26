package edu.duke.ece651.team16.controller;

import static org.mockito.Mockito.*;
import org.mockito.Mockito;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.application.Platform;

public class StartGameControllerTest extends ApplicationTest {

    @Override
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
    public void testNewGameButton() throws Exception {
        // Click on the "New Game" button
        clickOn("#newGame");
        Platform.runLater(() -> {
            try {
                start(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
