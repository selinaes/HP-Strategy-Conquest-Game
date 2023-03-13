package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

public class ServerTest {

  @Test
  public void testRun() throws IOException, JsonProcessingException {

    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);
    Socket socket = mock(Socket.class);

    when(serverSocket.accept()).thenReturn(socket);

    server.run();

    verify(serverSocket).accept();
    verify(socket).close();
    verify(server.game).createPlayer(eq(socket), eq(1));
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
