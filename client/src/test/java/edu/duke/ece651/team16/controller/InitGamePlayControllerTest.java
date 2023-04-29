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

public class InitGamePlayControllerTest extends ApplicationTest {
    private InitGamePlayController initGamePlayController;
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
        String map = "{\"red\":[{\"TerritoryName\":\"Black Lake\",\"Rate\":\"Food Rate:5, Tech Rate:5\",\"Neighbors\":\"(next to:brodie:5,smith:5,dukeChapel:5)\",\"Resource\":\"(Food:60 Tech:60 Tech Level:1)\",\"Unit\":\"0,0,0,0,0,0,0,\"}]}";

        when(mockReader.readLine()).thenReturn("key").thenReturn("key").thenReturn("value").thenReturn("value")
                .thenReturn("finished stage")
                .thenReturn("stage Complete").thenReturn(map)
                .thenReturn("Game continous").thenReturn("do nothing");
        socketReceiveField.set(client, mockReader);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/InitGamePlay.fxml"));
        AnchorPane root = loader.load();
        initGamePlayController = loader.getController();
        setClient();
        client.setColor("red");
        initGamePlayController.setClient(client);
        String map = "{\"red\":[{\"TerritoryName\":\"Black Lake\",\"Rate\":\"Food Rate:5, Tech Rate:5\",\"Neighbors\":\"(next to:brodie:5,smith:5,dukeChapel:5)\",\"Resource\":\"(Food:60 Tech:60 Tech Level:1)\",\"Unit\":\"0,0,0,0,0,0,0,\"}]}";
        MapParser mapParser = new MapParser(map);
        mapParser.setPlayer("red");
        initGamePlayController.setinitMapParser(mapParser);
        initGamePlayController.setMapParser();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        initGamePlayController.initSpinner();
        waitForFxEvents();
    }

    @Test
    public void testInitGamePlayController() throws IOException, Exception {
        new Thread(() -> {
            try {
                ServerSocket socket = new ServerSocket(4321);
                Socket clientSocket = socket.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        clickOn(610.0, 268.0);
        waitForFxEvents();
        clickOn("#assignUnits").clickOn();
        waitForFxEvents();
    }

}
