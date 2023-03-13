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
  private Socket makeMockSocket() throws IOException {
    Socket mockSocket = Mockito.mock(Socket.class);
    InputStream mockInputStream = Mockito.mock(InputStream.class);
    OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
    Mockito.when(mockSocket.getInputStream()).thenReturn(mockInputStream);
    Mockito.when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    return mockSocket;
  }

  private BufferedReader makeInputSource(String input) {
    InputStream sysIn = new ByteArrayInputStream(input.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));
    return inputSource;
  }

  private PrintStream makeOut() {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(byteArrayOutputStream);
    return out;
  }

  private BufferedReader makeBufferedReader(String input) throws IOException {
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn(input);
    return mockReader;
  }

  @Test
  public void test_readClientInput() throws IOException {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = makeInputSource("test input");
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    client.sendResponse("input");

    // test readClientInput if s != null
    String output = client.readClientInput("");
    assertEquals("test input", output);
    client.close();

    // test readClientInput if s == null
    Client client2 = new Client(mockSocket, null, out);
    assertThrows(EOFException.class, () -> client.readClientInput(""));
    client2.close();
  }

  @Test
  public void test_recvMsg() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader mockReader = makeBufferedReader("test message");
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);
    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    socketReceiveField.set(client, mockReader);

    String result = client.recvMsg();
    assertEquals("test message", result);
    client.close();
  }

  @Test
  public void test_playerChooseColor() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = makeBufferedReader("Valid");
    socketReceiveField.set(client, mockReader);
    client.playerChooseColor();
    verify(mockReader, times(2)).readLine();
    client.close();
  }

  @Test
  public void test_playerChooseColorException() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Not Valid").thenReturn("Not Valid").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);

    when(inputSource.readLine())
        .thenReturn(null)
        .thenReturn("legal string");
    client.playerChooseColor();

    when(mockReader.readLine()).thenReturn(null).thenThrow(new IOException()).thenReturn("String");
    socketReceiveField.set(client, mockReader);
    client.recvMsg();

    client.close();
  }

  @Test
  public void test_socketClose() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    doThrow(new IOException()).when(mockSocket).close();

    Client client = new Client(mockSocket, inputSource, out);
    client.close();
  }

  @Test
  public void test_displayMap() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();
    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Valid");
    socketReceiveField.set(client, mockReader);

    client.displayMap();

    String jsonString = "{\"name\":[{\"TerritoryName\":\"Baldwin Auditorium\",\"Neighbors\":\"(next to: Brodie Recreational Center, Smith Warehouse, Duke Chapel)\"}]}";
    when(mockReader.readLine()).thenReturn(jsonString);
    socketReceiveField.set(client, mockReader);

    client.displayMap();

    client.close();
  }

  @Test
  public void test_playerChooseNumber() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = makeBufferedReader("Valid");
    socketReceiveField.set(client, mockReader);
    client.playerChooseNum();
    verify(mockReader, times(2)).readLine();
    client.close();
  }

  @Test
  public void test_playerChooseNumberException() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Not Valid").thenReturn("Not Valid").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);

    when(inputSource.readLine())
        .thenReturn(null)
        .thenReturn("legal string");
    client.playerChooseNum();

    client.close();
  }

  @Test
  public void test_playerEnterName() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = makeBufferedReader("Valid");
    socketReceiveField.set(client, mockReader);
    client.playerEnterName();
    verify(mockReader, times(1)).readLine();
    client.close();
  }

  @Test
  public void test_playerEneterNameException() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Not Valid").thenReturn("Not Valid").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);

    when(inputSource.readLine())
        .thenReturn(null)
        .thenReturn("legal string");
    client.playerEnterName();

    client.close();
  }

  @Test
  public void test_run() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    String[] inputs = new String[] { "input1", "input2" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));

    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = makeBufferedReader("Valid");
    socketReceiveField.set(client, mockReader);

    client.run();

    client.close();
  }
}
