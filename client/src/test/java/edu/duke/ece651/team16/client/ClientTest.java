package edu.duke.ece651.team16.client;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
import java.beans.Transient;
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

    String jsonString = "{\"name\":[{\"TerritoryName\":\"Baldwin Auditorium\",\"Neighbors\":\"(next to: Brodie Recreational Center, Smith Warehouse, Duke Chapel)\",\"Unit\":\"1\"}]}";
    when(mockReader.readLine()).thenReturn(jsonString);
    socketReceiveField.set(client, mockReader);

    client.displayMap();

    client.close();
  }

  @Test
  public void test_displayEntry() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();
    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);

    String jsonString = "{\"Entry\":\"a\"}";
    when(mockReader.readLine()).thenReturn(jsonString);
    socketReceiveField.set(client, mockReader);

    client.displayEntry();

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

    when(mockReader.readLine())
        .thenReturn("Not the first player. Please wait for the first player to set player number.");
    socketReceiveField.set(client, mockReader);
    client.playerChooseNum();

    client.close();
  }

  @Test
  public void test_waitEveryoneDone() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Not Valid").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);
    client.waitEveryoneDone("Valid");
    client.close();
  }

  @Test
  public void test_displayInitialMap() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Valid");
    socketReceiveField.set(client, mockReader);

    client.displayInitialMap();

    String jsonString = "{\"name\":[{\"TerritoryName\":\"Baldwin Auditorium\",\"Neighbors\":\"(next to: Brodie Recreational Center, Smith Warehouse, Duke Chapel)\"}]}";
    when(mockReader.readLine()).thenReturn(jsonString);
    socketReceiveField.set(client, mockReader);

    client.displayInitialMap();

    client.close();
  }

  @Test
  public void test_playerAssignUnits() throws IOException, Exception {
    String s1 = "finished placement";
    String s2 = "Valid territory name";
    String s3 = "Valid number of units";
    String s4 = "Other";

    Socket mockSocket = makeMockSocket();
    String[] inputs = new String[] { "unitTest1", "unitTest2", "unitTest3", "unitTest4", "unitTest5",
        "unitTest6" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn(s1);
    socketReceiveField.set(client, mockReader);

    client.playerAssignUnit();

    // clientInput = "unitTest1"
    when(mockReader.readLine()).thenReturn(s4).thenReturn(s1);
    socketReceiveField.set(client, mockReader);
    client.playerAssignUnit();

    // clientInput = "unitTest2", "unitTest3"
    when(mockReader.readLine()).thenReturn(s4).thenReturn(s2).thenReturn(s4).thenReturn(s1);
    socketReceiveField.set(client, mockReader);
    client.playerAssignUnit();

    // clientInput = "unitTest4"
    when(mockReader.readLine()).thenReturn(s4).thenReturn(s3);
    socketReceiveField.set(client, mockReader);
    client.playerAssignUnit();

    // clientInput = "unitTest5", "unitTest6"
    when(mockReader.readLine()).thenReturn(s4).thenReturn(s4).thenReturn(s1);
    socketReceiveField.set(client, mockReader);
    client.playerAssignUnit();

    client.close();
  }

  @Test
  public void test_playerAssignUnitsException() throws IOException, Exception {
    String s1 = "finished placement";
    String s2 = "Other";
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn(s2).thenReturn(s1);
    socketReceiveField.set(client, mockReader);

    when(inputSource.readLine())
        .thenReturn(null)
        .thenReturn(s1);
    client.playerAssignUnit();

    client.close();
  }

  @Test
  public void test_playerAssignAllUnits() throws IOException, Exception {
    String s1 = "finished placement";
    String s2 = "Valid number of units";
    String s3 = "Other";
    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);

    // playerAssignUnits return True
    when(mockReader.readLine()).thenReturn(s1);
    socketReceiveField.set(client, mockReader);
    client.playerAssignAllUnits();

    // playerAssignUnits return False and then retrn True
    when(mockReader.readLine()).thenReturn(s3).thenReturn(s2).thenReturn(s1);
    socketReceiveField.set(client, mockReader);
    client.playerAssignAllUnits();

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
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("playerChooseNum", "Valid")
        .thenReturn("setNumPlayer Complete").thenReturn("displayInitialMap").thenReturn("playerChooseColor", "Valid")
        .thenReturn("finished placement").thenReturn("setUnits Complete").thenReturn("displayMap");
    socketReceiveField.set(client, mockReader);

    client.run();

    client.close();
  }

  @Test
  public void testCheckMoveInputFormat() throws IOException {
    String input1 = "A, B, 5"; // valid input
    String input2 = "A, B"; // invalid input (not enough arguments)
    String input4 = "A, B, x"; // invalid input (third argument contains letters)

    Socket mockSocket = makeMockSocket();
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(mockSocket, inputSource, out);

    // Test valid input
    assertTrue(client.checkMoveInputFormat(input1));

    // Test invalid input (not enough arguments)
    assertFalse(client.checkMoveInputFormat(input2));

    // Test invalid input (third argument contains letters)
    assertFalse(client.checkMoveInputFormat(input4));
  }
}
