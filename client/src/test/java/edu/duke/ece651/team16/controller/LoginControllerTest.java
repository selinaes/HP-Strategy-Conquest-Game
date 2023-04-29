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
import javafx.scene.Node;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;
import javafx.scene.control.Button;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerTest {
    ConnClient connClient;

    @Start
    public void start(Stage stage) throws Exception {
        new Thread(() -> {
            try {
                ServerSocket socket = new ServerSocket(1651);
                Socket clientSocket = socket.accept();
                connClient = new ConnClient(clientSocket);
                connClient.send("hello");
                connClient.send("error");
                connClient.send("Login successful.");
                connClient.send("startgame");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        URL xmlResource = getClass().getResource("/ui/login.fxml");
        AnchorPane gp = FXMLLoader.load(xmlResource);
        // LoginController loginGameController = fxmlLoader.getController();
        Scene scene = new Scene(gp);
        stage.setScene(scene);
        stage.show();

    }

    @Test
    public void test_(FxRobot robot) {
        waitForFxEvents();
        Node textField1 = robot.lookup(".text-field").nth(0).query(); // locate the first
        Node textField2 = robot.lookup(".text-field").nth(1).query(); // locate the second
        robot.clickOn(textField1).clickOn().write("test");
        robot.clickOn("#login");
        waitForFxEvents();
    }

}
