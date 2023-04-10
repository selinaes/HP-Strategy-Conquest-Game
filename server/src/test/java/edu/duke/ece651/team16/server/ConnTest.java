package edu.duke.ece651.team16.server;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnTest {

  private Socket makeMockSocket() throws IOException {
    Socket mockSocket = mock(Socket.class);
    InputStream mockInputStream = mock(InputStream.class);
    OutputStream mockOutputStream = mock(OutputStream.class);
    when(mockSocket.getInputStream()).thenReturn(mockInputStream);
    when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    return mockSocket;
  }

  private BufferedReader makeBufferedReader(String input) throws IOException {
    BufferedReader mockReader = mock(BufferedReader.class);
    when(mockReader.readLine()).thenReturn(input);
    return mockReader;
  }

  // @Test
  // public void testClose() {
  // // Create a mock input stream that will return "close"
  // ByteArrayInputStream inStream = new
  // ByteArrayInputStream("close\n".getBytes());
  // BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

  // // Create a mock instance of the class that uses the recv method
  // MyClass myClass = Mockito.mock(MyClass.class);
  // Mockito.when(myClass.recv()).thenReturn("close");

  // // Call the recv method and verify that the close method is called on the
  // mock
  // // object
  // String ans = myClass.recv();
  // if (ans.equals("close")) {
  // }
  // Mockito.verify(myClass).close();
  // }

  @Test
  public void testSend() throws Exception {
    // Create a mock Socket
    Socket mockSocket = makeMockSocket();

    // Create a mock Connection
    Conn conn = new Conn(mockSocket);

    // Create a mock PrintWriter
    PrintWriter mockPrintWriter = mock(PrintWriter.class);

    // Modify Field of mockConnection
    Field field = conn.getClass().getDeclaredField("out");
    field.setAccessible(true);
    field.set(conn, mockPrintWriter);

    // Define input message
    String msg = "Hello, world!";

    // Call send method with input message
    conn.send(msg);
    conn.close();

    // Verify that the mock PrintWriter was called with the input message
    verify(mockPrintWriter).println(msg);

    Conn Connection2 = new Conn(mockSocket);
    Field field2 = Connection2.getClass().getDeclaredField("out");
    field2.setAccessible(true);
    field2.set(Connection2, null);

    // Set the standard error stream to a PrintStream that writes to a
    // ByteArrayOutputStream
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream errStream = new PrintStream(outContent, true, "UTF-8");
    System.setErr(errStream);

    Connection2.send(msg);
    Connection2.close();

    System.setErr(System.err);

    String expectedOutput = "Failed to send message: out is null.\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void test_recv() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();
    BufferedReader mockReader = makeBufferedReader("test message");

    Conn connection = new Conn(mockSocket);
    Field inField = connection.getClass().getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(connection, mockReader);

    when(mockReader.readLine()).thenReturn("close");

    connection.recv();
  }

  @Test
  public void test_socketClose_IOE() throws IOException, Exception {
    Socket mockSocket = makeMockSocket();

    doThrow(new IOException()).when(mockSocket).close();

    Conn connection = new Conn(mockSocket);
    connection.close();
  }

  @Test
  public void test_connection_fail_construct() throws IOException {
    Socket mockSocket = mock(Socket.class);
    InputStream mockInputStream = mock(InputStream.class);
    OutputStream mockOutputStream = mock(OutputStream.class);
    when(mockSocket.getInputStream()).thenThrow(new IOException()).thenReturn(mockInputStream);
    when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    Conn connection = new Conn(mockSocket);
    connection.close();
  }

}
