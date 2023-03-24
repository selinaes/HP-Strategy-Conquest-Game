package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  public void testRunServer() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    // Create a mock ServerSocket and Server
    ServerSocket mockServerSocket = mock(ServerSocket.class);
    Server mockServer = mock(Server.class);

    // Create a new instance of App and Server
    App app = new App(1234);

    Field listenSocketField = app.getClass().getDeclaredField("listenSocket");
    listenSocketField.setAccessible(true);
    listenSocketField.set(app, mockServerSocket);

    Field ServerField = app.getClass().getDeclaredField("server");
    ServerField.setAccessible(true);
    ServerField.set(app, mockServer);

    // Mock the creation of the ServerSocket and Socket
    doThrow(new IOException("Test Exception")).when(mockServer).run();

    // Run the server
    app.runServer();

    // Verify that the Server was created and run
    verify(mockServer, times(1)).run();
    String output = outputStream.toString();
    assertTrue(output.contains("Server run failed with IOException."));
  }

}
