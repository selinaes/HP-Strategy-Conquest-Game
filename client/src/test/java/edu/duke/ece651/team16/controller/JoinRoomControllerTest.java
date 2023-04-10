package edu.duke.ece651.team16.controller;

import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Stop;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextMatchers.hasText;
import static org.mockito.Mockito.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

public class JoinRoomControllerTest extends ApplicationTest {
    private JoinRoomController joinRoomController;
    private Client client;

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file for the JoinRoomController UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/JoinGame.fxml"));
        AnchorPane anchorPane = loader.load();
        joinRoomController = loader.getController();
        client = mock(Client.class);
        joinRoomController.setClient(client);
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testSearchRoom() {
        Platform.runLater(() -> {
            clickOn("#roomIDField").clickOn().write("123");
            clickOn("#searchRoom");
        });
    }
}
