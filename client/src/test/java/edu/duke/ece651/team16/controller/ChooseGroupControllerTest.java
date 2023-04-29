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
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChooseGroupControllerTest extends ApplicationTest {
    private ChooseGroupController chooseGroupController;
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

        when(mockReader.readLine()).thenReturn("prompt").thenReturn("unvalid").thenReturn("Valid");
        socketReceiveField.set(client, mockReader);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ChooseGroup.fxml"));
        AnchorPane anchorPane = loader.load();
        chooseGroupController = loader.getController();
        setClient();
        chooseGroupController.setClient(client);
        String map = "{\"red\":[{\"TerritoryName\":\"baldwin\",\"Rate\":\"Food Rate:5, Tech Rate:5\",\"Neighbors\":\"(next to:brodie:5,smith:5,dukeChapel:5)\",\"Resource\":\"(Food:60 Tech:60 Tech Level:1)\",\"Unit\":\"0,0,0,0,0,0,0,\"}]}";
        MapParser mapParser = new MapParser(map);
        chooseGroupController.setMapParser(mapParser);
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test_moveMouse() {
        moveTo(771.0, 400.0);
        waitForFxEvents();
        moveTo(183.0, 400.0);
        waitForFxEvents();
        moveTo(1200.0, 400.0);
        moveTo(240.0, 400.0);
    }

    @Test
    public void test_chooseGroup() {
        clickOn(1200.0, 300.0);
        clickOn("OK");
        clickOn(250.0, 300.0);
        waitForFxEvents();
    }
}
