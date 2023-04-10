// package edu.duke.ece651.team16.controller;

// import javafx.application.Platform;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.layout.AnchorPane;
// import javafx.stage.Stage;
// import org.junit.jupiter.api.Test;

// import org.testfx.framework.junit5.ApplicationTest;
// import java.net.Socket;
// import java.io.IOException;
// import java.io.PrintStream;
// import static org.mockito.Mockito.*;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.testfx.api.FxAssert.verifyThat;
// import static org.testfx.matcher.control.LabeledMatchers.hasText;

// public class StartGameControllerTest extends ApplicationTest {

//     private StartGameController startGameController;
//     private Client client;

//     @Override
//     public void start(Stage stage) throws IOException {
//         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/StartGame.fxml"));
//         AnchorPane anchorPane = fxmlLoader.load();
//         startGameController = fxmlLoader.getController();
//         client = mock(Client.class);
//         startGameController.setClient(client);
//         Scene scene = new Scene(anchorPane);
//         stage.setScene(scene);
//         stage.show();
//     }

//     @Test
//     public void testNewGameButton() {
//         Platform.runLater(() -> {
//             clickOn("#newGame");
//             verifyThat("#newGame", hasText("New Game"));
//         });
//     }

    // @Test
    // public void testExitGameButton() {
    // Platform.runLater(() -> {
    // clickOn("#exitGame");
    // // verifyThat(window("Primary Stage"), WindowMatchers.isClosed());
    // });
    // }

}
