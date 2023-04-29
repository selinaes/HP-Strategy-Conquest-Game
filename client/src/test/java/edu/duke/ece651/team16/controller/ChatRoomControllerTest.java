package edu.duke.ece651.team16.controller;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.ServerSocket;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ChatRoomControllerTest extends ApplicationTest {
    private ChatRoomController controller;
    ConnClient connClient;

    @Override
    public void start(Stage stage) throws Exception {
        new Thread(() -> {
            try {
                ServerSocket socket = new ServerSocket(4321);
                Socket clientSocket = socket.accept();
                connClient = new ConnClient(clientSocket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ChatRoom.fxml"));
        loader.setControllerFactory(c -> {
            return new ChatRoomController("red");
        });
        AnchorPane chatRoomPane = loader.load();
        controller = loader.getController();
        controller.setGamePlayController(mock(GamePlayController.class));
        Scene scene = new Scene(chatRoomPane);
        stage.setScene(scene);
        stage.show();

    }

    @Test
    public void test_() throws IOException {
        connClient.send("server:hello");
        connClient.send("red:hello");
        connClient.send("playerlist:red blue");
        connClient.send("map:hello");
        connClient.send("blue:hello");
        clickOn("#input").write("hello");
        clickOn("#toWho").clickOn("blue");
        clickOn("#send");
        controller.sendMsg("hello");
    }

}
