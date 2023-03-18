package edu.duke.ece651.team16.client;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import org.junit.Rule;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.io.PrintWriter;

public class ClientTest {
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

  private BufferedReader makeInputSource(String input) throws IOException {
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
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = makeInputSource("test input");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    client.sendResponse("input");

    // test readClientInput if s != null
    String output = client.readClientInput("");
    assertEquals("test input", output);

    // test readClientInput if s == null
    Client client2 = new Client(null, out, socketReceive, socketSend);
    assertThrows(EOFException.class, () -> client.readClientInput(""));
  }

  @Test
  public void test_recvMsg() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader mockReader = makeBufferedReader("test message");
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    socketReceiveField.set(client, mockReader);

    String result = client.recvMsg();
    assertEquals("test message", result);
  }

  @Test
  public void test_playerChooseColor() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = makeBufferedReader("Valid");
    socketReceiveField.set(client, mockReader);
    client.playerChooseColor();
    verify(mockReader, times(2)).readLine();
  }

  @Test
  public void test_playerChooseColorException() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

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
  }

  @Test
  public void test_playerChooseNumber() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = makeBufferedReader("Valid");
    socketReceiveField.set(client, mockReader);
    client.playerChooseNum();
    verify(mockReader, times(2)).readLine();
  }

  @Test
  public void test_playerChooseNumberException() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

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
  }

  @Test
  public void test_waitEveryoneDone() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);

    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("not valid").thenReturn("stage Complete");
    socketReceiveField.set(client, mockReader);
    client.waitEveryoneDone();
  }

  @Test
  public void test_playerAssignUnits() throws IOException, Exception {
    String s1 = "finished placement";
    String s2 = "Valid territory name";
    String s3 = "Valid number of units";
    String s4 = "Other";
    String s5 = "finished stage";

    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    String[] inputs = new String[] { "unitTest1", "unitTest2", "unitTest3",
        "unitTest4", "unitTest5",
        "unitTest6" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn(s5);
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
  }

  @Test
  public void test_playerAssignUnitsException() throws IOException, Exception {
    String s1 = "finished placement";
    String s2 = "Other";
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn(s2).thenReturn(s1);
    socketReceiveField.set(client, mockReader);

    when(inputSource.readLine())
        .thenReturn(null)
        .thenReturn(s1);
    client.playerAssignUnit();
  }

  @Test
  public void test_playerAssignAllUnits() throws IOException, Exception {
    String s1 = "finished placement";
    String s2 = "Valid number of units";
    String s3 = "Other";
    String s4 = "finished stage";
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);

    // playerAssignUnits return True
    when(mockReader.readLine()).thenReturn(s4);
    socketReceiveField.set(client, mockReader);
    client.playerAssignAllUnits();

    // playerAssignUnits return False and then retrn True
    when(mockReader.readLine()).thenReturn(s3).thenReturn(s2).thenReturn(s4);
    socketReceiveField.set(client, mockReader);
    client.playerAssignAllUnits();
  }

  @Test
  public void testCheckMoveInputFormat() throws IOException {
    String input1 = "A, B, 5"; // valid input
    String input2 = "A, B"; // invalid input (not enough arguments)
    String input4 = "A, B, x"; // invalid input (third argument contains letters)

    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    // Test valid input
    assertTrue(client.checkMoveInputFormat(input1));

    // Test invalid input (not enough arguments)
    assertFalse(client.checkMoveInputFormat(input2));

    // Test invalid input (third argument contains letters)
    assertFalse(client.checkMoveInputFormat(input4));
  }

  @Test
  public void test_playerOneAction() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    when(inputSource.readLine())
        .thenReturn(null).thenReturn("t, t", "t, t, 3", "Prompt, prompt, 3");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);
    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Prompt").thenReturn("Not Valid").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);

    client.playerOneAction();
  }

  @Test
  public void test_playerActionTurn() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    when(inputSource.readLine()).thenReturn("s", "m", "Prompt, prompt, 3", "d", "d");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);
    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);

    String jsonString = "{\"Entry\":\"a\"}";
    when(mockReader.readLine()).thenReturn(jsonString).thenReturn("recvMsg").thenReturn("Valid").thenReturn(jsonString)
        .thenReturn("finished stage").thenReturn(jsonString).thenReturn("not finish");
    socketReceiveField.set(client, mockReader);

    client.playerActionTurn();
    client.playerActionTurn();
  }

  @Test
  public void test_playerChooseWatch() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    String[] inputs = new String[] { "P", "w" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("prompt").thenReturn("Not Valid").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);
    client.playerChooseWatch();
    verify(mockReader, times(4)).readLine();
  }

  @Test
  public void test_playerChooseWatchException() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    when(inputSource.readLine()).thenReturn(null).thenReturn("w").thenReturn("e");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("prompt").thenReturn("Valid").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);

    client.playerChooseWatch();

    client.playerChooseWatch();

    assertTrue(client.ifExit());
  }

  @Test
  public void test_runWatchOption() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    when(inputSource.readLine()).thenReturn("w").thenReturn("s", "m", "Prompt, prompt, 3", "d", "d");
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);
    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Choose watch").thenReturn("prompt").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);
    client.runWatchOption();

    Field watch = client.getClass().getDeclaredField("view");
    watch.setAccessible(true);
    Views view = mock(Views.class);
    when(view.isWatch()).thenReturn(false);
    watch.set(client, view);
    String jsonString = "{\"Entry\":\"a\"}";
    when(mockReader.readLine()).thenReturn("not watch").thenReturn(jsonString).thenReturn("recvMsg").thenReturn("Valid")
        .thenReturn(jsonString)
        .thenReturn("finished stage").thenReturn(jsonString).thenReturn("not finish");
    socketReceiveField.set(client, mockReader);
    client.runWatchOption();

  }

  @Test
  public void test_run() throws IOException, Exception {
    String jsonString = "{\"name\":[{\"TerritoryName\":\"a\",\"Neighbors\":\"(next to: b)\",\"Unit\":\"1\"}]}";
    String jsonStringLog = "{\"Entry\":\"You are the blue player, what would you like to do?\"}";

    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    String[] inputs = new String[] { "input1", "input2", "w" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));

    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("playerChooseNum", "Valid")
        .thenReturn("stage Complete").thenReturn(
            jsonString)
        .thenReturn("playerChooseColor", "Valid")
        .thenReturn("finished stage").thenReturn("stage Complete").thenReturn(
            jsonString)
        .thenReturn("Game continues").thenReturn("Choose watch").thenReturn("Input").thenReturn("Valid")
        .thenReturn("stage Complete")
        .thenReturn(jsonStringLog)
        .thenReturn(jsonString).thenReturn("Game over");
    socketReceiveField.set(client, mockReader);

    client.run();
  }

  @Test
  public void test_runExit() throws IOException, Exception {
    String jsonString = "{\"name\":[{\"TerritoryName\":\"a\",\"Neighbors\":\"(next to: b)\",\"Unit\":\"1\"}]}";
    String jsonStringLog = "{\"Entry\":\"You are the blue player, what would you like to do?\"}";

    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    String[] inputs = new String[] { "input1", "input2", "e" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());
    BufferedReader inputSource = new BufferedReader(new InputStreamReader(sysIn));

    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("playerChooseNum", "Valid")
        .thenReturn("stage Complete").thenReturn(
            jsonString)
        .thenReturn("playerChooseColor", "Valid")
        .thenReturn("finished stage").thenReturn("stage Complete").thenReturn(
            jsonString)
        .thenReturn("Game continues").thenReturn("Choose watch").thenReturn("Input").thenReturn("Valid");
    socketReceiveField.set(client, mockReader);

    client.run();
  }
}
