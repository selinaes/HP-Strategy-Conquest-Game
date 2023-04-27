package edu.duke.ece651.team16.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    client.ifExit();
    client.getColor();
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
    client.playerChooseColor("red");

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
    when(mockReader.readLine()).thenReturn("Not Valid");
    socketReceiveField.set(client, mockReader);

    client.playerChooseColor("red");

    when(mockReader.readLine()).thenReturn(null).thenThrow(new IOException()).thenReturn("String");
    socketReceiveField.set(client, mockReader);
    client.recvMsg();

    // client.playerChooseColor("red");
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
    client.playerChooseNum("2");
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

    client.playerChooseNum("2");

    when(mockReader.readLine())
        .thenReturn("finished stage");
    socketReceiveField.set(client, mockReader);
    client.playerChooseNum("2");
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
    String s1 = "finished stage";
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

    client.playerAssignUnit("2");

    // clientInput = "unitTest1"
    when(mockReader.readLine()).thenReturn(s4).thenReturn(s1);
    socketReceiveField.set(client, mockReader);
    client.playerAssignUnit("2");

    // clientInput = "unitTest2", "unitTest3"
    when(mockReader.readLine()).thenReturn(s4).thenReturn(s2).thenReturn(s4).thenReturn(s1);
    socketReceiveField.set(client, mockReader);
    client.playerAssignUnit("2");

    // clientInput = "unitTest4"
    when(mockReader.readLine()).thenReturn(s4).thenReturn(s3);
    socketReceiveField.set(client, mockReader);
    client.playerAssignUnit("2");

    // clientInput = "unitTest5", "unitTest6"
    when(mockReader.readLine()).thenReturn(s4).thenReturn(s4).thenReturn(s1);
    socketReceiveField.set(client, mockReader);
    client.playerAssignUnit("2");
  }

  @Test
  public void test_playerAssignUnitsException() throws IOException, Exception {
    String s1 = "finished stage";
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
    client.playerAssignUnit("2");
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
    ArrayList<String> clientInput = new ArrayList<>();
    clientInput.add("d");
    client.playerOneAction(clientInput);
  }

  @Test
  public void test_playerActionTurn() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);
    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);

    when(mockReader.readLine()).thenReturn("finished stage");
    socketReceiveField.set(client, mockReader);
    ArrayList<String> clientInput = new ArrayList<>();
    clientInput.add("d");
    client.playerOneAction(clientInput);

    ArrayList<String> clientInput2 = new ArrayList<>();
    clientInput2.add("a");
    clientInput2.add("T1, t2, 10");
    client.playerOneAction(clientInput2);
    ArrayList<String> clientInput3 = new ArrayList<>();
    clientInput3.add("s");
    clientInput3.add("s");
    client.playerOneAction(clientInput3);
  }

  @Test
  public void test_playerChooseWatch() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Valid");
    socketReceiveField.set(client, mockReader);
    client.playerChooseWatch("e");
    client.playerChooseWatch("w");
    client.getClientSocket();
    client.setClientSocket(mSocket);
  }

  @Test
  public void test_playerChooseRoom() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader inputSource = mock(BufferedReader.class);
    PrintStream out = makeOut();

    Client client = new Client(inputSource, out, socketReceive, socketSend);

    Field socketReceiveField = client.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn("Valid");
    socketReceiveField.set(client, mockReader);
    client.playerChooseRoom("e");
  }

}
