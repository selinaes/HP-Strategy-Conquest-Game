package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.beans.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class ServerTest {

  // @Test
  // public void testRun() throws IOException, JsonProcessingException, Exception {

  //   ServerSocket serverSocket = mock(ServerSocket.class);
  //   Server server = new Server(serverSocket);
  //   Socket socket = mock(Socket.class);
  //   Game mockGame = mock(Game.class);

  //   Field gameField = server.getClass().getDeclaredField("game");
  //   gameField.setAccessible(true);
  //   gameField.set(server, mockGame);

  //   when(serverSocket.accept()).thenReturn(socket).thenAnswer(invocation -> {
  //     throw new IOException("Socket closed");
  //   });
  //   doNothing().when(mockGame).gameFlow(any(Conn.class), anyInt());

  //   // run the server in a seperate thread, and interrupt it
  //   Thread serverThread = new Thread(new Runnable() {
  //     @Override
  //     public void run() {
  //       try {
  //         server.run(); // call the run() method of the Server instance
  //       } catch (IOException e) {
  //         // System.out.println(e.getMessage());
  //       }
  //     }
  //   });
  //   serverThread.start();

  //   // Wait for the server to start
  //   Thread.sleep(100);

  //   // Interrupt the server after a short delay
  //   serverThread.interrupt();
  //   // Thread.sleep(100);

  //   verify(serverSocket, atLeast(1)).accept();
  //   verify(socket).close();
  //   // verify(mockGame).gameFlow(eq(socket), eq(1));
  // }

  // @Test
  // public void testRunExceptio2() throws IOException, JsonProcessingException,
  //     Exception {

  //   ServerSocket serverSocket = mock(ServerSocket.class);
  //   Server server = new Server(serverSocket);
  //   Socket socket = mock(Socket.class);
  //   Game mockGame = mock(Game.class);

  //   Field gameField = server.getClass().getDeclaredField("game");
  //   gameField.setAccessible(true);
  //   gameField.set(server, mockGame);

  //   when(serverSocket.accept()).thenReturn(socket).thenAnswer(invocation -> {
  //     throw new IOException("Socket closed");
  //   });
  //   doThrow(new IOException("Socket closed")).when(socket).close();
  //   doNothing().when(mockGame).gameFlow(any(Conn.class), anyInt());

  //   // run the server in a seperate thread, and interrupt it
  //   Thread serverThread = new Thread(new Runnable() {
  //     @Override
  //     public void run() {
  //       try {
  //         server.run(); // call the run() method of the Server instance
  //       } catch (IOException e) {
  //         // System.out.println(e.getMessage());
  //       }
  //     }
  //   });
  //   serverThread.start();

  //   // Wait for the server to start
  //   Thread.sleep(100);

  //   // Interrupt the server after a short delay
  //   serverThread.interrupt();
  //   // Thread.sleep(100);

  //   verify(serverSocket, atLeast(1)).accept();
  //   verify(socket).close();
  //   // verify(mockGame).gameFlow(eq(socket), eq(1));
  // }

  @Test
  public void testRunException() throws IOException, JsonProcessingException,
      Exception {

    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);

    when(serverSocket.accept()).thenAnswer(invocation -> {
      throw new IOException("Socket closed");
    });

    // run the server in a seperate thread, and interrupt it
    Thread serverThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          server.run(); // call the run() method of the Server instance
        } catch (IOException e) {
          // System.out.println(e.getMessage());
        }
      }
    });
    serverThread.start();

    // Wait for the server to start
    Thread.sleep(100);

    // Interrupt the server after a short delay
    serverThread.interrupt();

    verify(serverSocket, atLeast(1)).accept();
  }

  @Test
  public void testLoginSuccess() throws Exception {
    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);

    HashMap<String, String> accounts = new HashMap<>();
    accounts.put("user1", "password1");

    Field accountsField = server.getClass().getDeclaredField("accounts");
    accountsField.setAccessible(true);
    accountsField.set(server, accounts);

    Conn conn = mock(Conn.class);
    when(conn.recv()).thenReturn("user1, password1");
    server.login(conn);
    verify(conn).send("Login successful.");
  }

  @Test
  public void testLoginWrongPassword() throws Exception {
    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);

    HashMap<String, String> accounts = new HashMap<>();
    accounts.put("user1", "password1");

    Field accountsField = server.getClass().getDeclaredField("accounts");
    accountsField.setAccessible(true);
    accountsField.set(server, accounts);

    Conn conn = mock(Conn.class);
    when(conn.recv()).thenReturn("user1, wrongpassword").thenReturn("user1, password1");
    server.login(conn);
    verify(conn).send("Wrong password, please try again.");
  }

  @Test
  public void testLoginNewAccount() {
    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);

    Conn conn = mock(Conn.class);
    when(conn.recv()).thenReturn("newuser, newpassword");
    server.login(conn);
    verify(conn).send("New account created.");
  }

  @Test
  public void testEnterGameJoinRoom() throws Exception {
    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);

    Game mockGame = mock(Game.class);
    doNothing().when(mockGame).gameFlow(any(Conn.class), anyInt());
    when(mockGame.getGameState()).thenReturn("setNumPlayer");

    HashMap<String, Game> gameList = new HashMap<>();
    gameList.put("room1", mockGame);

    HashMap<String, Integer> gameClients = new HashMap<>();
    gameClients.put("room1", 2);

    Field listField = server.getClass().getDeclaredField("gameList");
    listField.setAccessible(true);
    listField.set(server, gameList);

    Field clientsField = server.getClass().getDeclaredField("gameClients");
    clientsField.setAccessible(true);
    clientsField.set(server, gameClients);

    Conn conn = mock(Conn.class);
    when(conn.recv()).thenReturn("room1");
    server.enterGame(conn);
    verify(conn).send("Room joined.");
  }

  @Test
  public void testEnterGameRoomFull() throws Exception {
    ServerSocket serverSocket = mock(ServerSocket.class);
    Server server = new Server(serverSocket);

    Game mockGame = mock(Game.class);
    when(mockGame.getGameState()).thenReturn("setPlayerColor");

    Game mockGame1 = mock(Game.class);
    doNothing().when(mockGame1).gameFlow(any(Conn.class), anyInt());
    when(mockGame1.getGameState()).thenReturn("setNumPlayer");

    HashMap<String, Game> gameList = new HashMap<>();
    gameList.put("room1", mockGame1);
    gameList.put("room2", mockGame);

    HashMap<String, Integer> gameClients = new HashMap<>();
    gameClients.put("room1", 2);
    gameClients.put("room2", 4);

    Field listField = server.getClass().getDeclaredField("gameList");
    listField.setAccessible(true);
    listField.set(server, gameList);

    Field clientsField = server.getClass().getDeclaredField("gameClients");
    clientsField.setAccessible(true);
    clientsField.set(server, gameClients);

    Conn conn = mock(Conn.class);
    when(conn.recv()).thenReturn("room2").thenReturn("room1");
    server.enterGame(conn);
    verify(conn).send("Room exceeded player number, game already started.");
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
