package edu.duke.ece651.team16.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;




class AppTest {
    @Test
    public void testMain() throws IOException {
      // Create a mock server socket
      ServerSocket mockServerSocket = mock(ServerSocket.class);

      // Create a mock socket
      Socket mockSocket = mock(Socket.class);

      // Create a mock server instance
      Server mockServer = mock(Server.class);


    }

}
