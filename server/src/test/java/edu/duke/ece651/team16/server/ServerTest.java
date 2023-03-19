package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.beans.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class ServerTest {

  @Test
  public void testRun() throws IOException, JsonProcessingException, Exception {

    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);
    Socket socket = mock(Socket.class);
    Game mockGame = mock(Game.class);

    Field gameField = server.getClass().getDeclaredField("game");
    gameField.setAccessible(true);
    gameField.set(server, mockGame);

    when(serverSocket.accept()).thenReturn(socket).thenAnswer(invocation -> {
      throw new IOException("Socket closed");
    });
    doNothing().when(mockGame).gameFlow(any(Socket.class), anyInt());

    // run the server in a seperate thread, and interrupt it
    Thread serverThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          server.run(); // call the run() method of the Server instance
        } catch (IOException e) {
          // System.out.println(e.getMessage());
        }
      }
    });
    serverThread.start();

    // Wait for the server to start
    Thread.sleep(100);

    // Interrupt the server after a short delay
    serverThread.interrupt();
    // Thread.sleep(100);

    verify(serverSocket, atLeast(1)).accept();
    verify(socket).close();
    verify(mockGame).gameFlow(eq(socket), eq(1));
  }

  @Test
  public void testAcceptOrNull() throws IOException {

    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);
    Socket socket = mock(Socket.class);

    when(serverSocket.accept()).thenReturn(socket);

    Socket result = server.acceptOrNull();

    verify(serverSocket).accept();
    assertEquals(socket, result);
  }

}
