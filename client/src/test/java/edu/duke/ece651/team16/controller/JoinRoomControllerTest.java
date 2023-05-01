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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class JoinRoomControllerTest {
    private JoinRoomController joinRoomController;
    private Client client;

    private Socket makeSocket() throws IOException {
        Socket mockSocket = Mockito.mock(Socket.class);
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
        Mockito.when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        Mockito.when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        return mockSocket;
    }

    private BufferedReader makeMockSocketReader(Socket mockSocket) throws IOException {
        return new BufferedReader(new InputStreamReader(mockSocket.getInputStream()));
    }

    private PrintWriter makeMockSocketSend(Socket mockSocket) throws IOException {
        return new PrintWriter(mockSocket.getOutputStream(), true);
    }

    private PrintStream makeOut() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(byteArrayOutputStream);
        return out;
    }

    private void setClient(String ret) throws IOException, Exception {
        Socket mSocket = makeSocket();
        BufferedReader socketReceive = makeMockSocketReader(mSocket);
        PrintWriter socketSend = makeMockSocketSend(mSocket);
        BufferedReader inputSource = mock(BufferedReader.class);
        PrintStream out = makeOut();

        client = new Client(inputSource, out, socketReceive, socketSend);
        Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
        socketReceiveField.setAccessible(true);
        BufferedReader mockReader = mock(BufferedReader.class);

        when(mockReader.readLine()).thenReturn(ret);
        socketReceiveField.set(client, mockReader);
    }

    @Start
    public void start(Stage stage) throws Exception, IOException {
        // Load the FXML file for the JoinRoomController UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/JoinGame.fxml"));
        AnchorPane anchorPane = loader.load();
        joinRoomController = loader.getController();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testSearchRoom(FxRobot robot) throws IOException, Exception {
        setClient("Room created.");
        joinRoomController.setClient(client);
        robot.clickOn("#roomIDField").clickOn().write("123");
        robot.clickOn("#searchRoom");
        robot.clickOn("#joinRoom");
        // robot.clickOn("Close");
        // robot.clickOn("#chooseNum").clickOn("2");
        // robot.clickOn("#joinRoom");
    }

    @Test
    public void testSearchRoom2(FxRobot robot) throws IOException, Exception {
        setClient("Room joined.");
        joinRoomController.setClient(client);
        robot.clickOn("#roomIDField").write("123");
        robot.clickOn("#searchRoom");
        robot.clickOn("#joinRoom");
    }

    @Test
    public void testSearchRoom3(FxRobot robot) throws IOException, Exception {
        setClient("exceed");
        joinRoomController.setClient(client);
        robot.clickOn("#roomIDField").write("123");
        robot.clickOn("#searchRoom");
    }
}
