package edu.duke.ece651.team16.client;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.net.Socket;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientTest {

  @Test
  public void test_readClientInput() throws IOException {
    // Create mock Socket
    Socket mockSocket = Mockito.mock(Socket.class);
    InputStream mockInputStream = Mockito.mock(InputStream.class);
    OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
    Mockito.when(mockSocket.getInputStream()).thenReturn(mockInputStream);
    Mockito.when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    // Create a ByteArrayInputStream to use for System.in
    String input = "test input";
    InputStream sysIn = new ByteArrayInputStream(input.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));

    // Create a ByteArrayOutputStream to use for System.out
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(baos);

    // Create the Client object and pass in the mock Socket and the input and output
    // streams
    Client client = new Client(mockSocket, inputSource, out);

    client.sendResponse("input");

    // test readClientInput if s != null
    String output = client.readClientInput("");
    assertEquals("test input", output);
    client.close();

    // test readClientInput if s == null
    Client client2 = new Client(mockSocket, null, out);
    assertThrows(EOFException.class, () -> client.readClientInput(""));
  }

  @Test
  public void test_recvMsg() throws IOException, Exception {
    // Create mock Socket
    Socket mockSocket = mock(Socket.class);
    InputStream mockInputStream = Mockito.mock(InputStream.class);
    OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
    Mockito.when(mockSocket.getInputStream()).thenReturn(mockInputStream);
    Mockito.when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockSocket.getInputStream()).thenReturn(mockInputStream);
    when(mockReader.readLine()).thenReturn("test message");

    // Create a ByteArrayInputStream to use for System.in
    String input = "input";
    InputStream sysIn = new ByteArrayInputStream(input.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));

    // Create a ByteArrayOutputStream to use for System.out
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(baos);

    Client client = new Client(mockSocket, inputSource, out);
    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    socketReceiveField.set(client, mockReader);

    String result = client.recvMsg();
    assertEquals("test message", result);
  }
}
