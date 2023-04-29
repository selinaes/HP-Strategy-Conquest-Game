package edu.duke.ece651.team16.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

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

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class WaitingRoomControllerTest {
    private WaitingRoomController waitingRoomController;
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

    private void setClient() throws IOException, Exception {
        Socket mSocket = makeSocket();
        BufferedReader socketReceive = makeMockSocketReader(mSocket);
        PrintWriter socketSend = makeMockSocketSend(mSocket);
        BufferedReader inputSource = mock(BufferedReader.class);
        PrintStream out = makeOut();

        client = new Client(inputSource, out, socketReceive, socketSend);
        Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
        socketReceiveField.setAccessible(true);
        BufferedReader mockReader = mock(BufferedReader.class);

        when(mockReader.readLine()).thenReturn("stage Complete").thenReturn("map");
        socketReceiveField.set(client, mockReader);
    }

    @Start
    public void start(Stage stage) throws Exception, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/WaitingRoom.fxml"));
        AnchorPane anchorPane = loader.load();
        waitingRoomController = loader.getController();
        setClient();
        waitingRoomController.setClient(client);
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test_next(FxRobot robot) {
        waitForFxEvents();
        robot.clickOn("#nextWaiting").clickOn();
    }

}
