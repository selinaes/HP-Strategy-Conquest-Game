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

public class ChatClientTest {
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
    when(mockReader.readLine()).thenReturn(input).thenReturn(null).thenReturn(input);
    return mockReader;
  }

  @Test
  public void test_() throws IOException, Exception {
    Socket mSocket = makeSocket();
    BufferedReader socketReceive = makeMockSocketReader(mSocket);
    PrintWriter socketSend = makeMockSocketSend(mSocket);
    BufferedReader mockReader = makeBufferedReader("test message");
    BufferedReader inputSource = makeInputSource("input");
    PrintStream out = makeOut();

    ChatClient chatClient = new ChatClient(socketReceive, out, mockReader, socketSend);
    Field socketReceiveField = chatClient.getClass().getDeclaredField("socketReceive");
    socketReceiveField.setAccessible(true);
    socketReceiveField.set(chatClient, mockReader);
    chatClient.recvMsg();
    chatClient.recvMsg();
    chatClient.sendResponse("test message");
  }

}
