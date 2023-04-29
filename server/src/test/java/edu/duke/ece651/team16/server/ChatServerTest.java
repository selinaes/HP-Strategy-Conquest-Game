package edu.duke.ece651.team16.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatServerTest {

  private ChatServer chatServer;
  private ServerSocket chatServerSock;
  private Socket socket;
  private Conn conn;
  private Player player;

  @BeforeEach
  public void setUp() throws IOException {
    chatServerSock = mock(ServerSocket.class);
    socket = mock(Socket.class);
    conn = mock(Conn.class);
    player = mock(Player.class);

    when(chatServerSock.accept()).thenReturn(socket);

    chatServer = new ChatServer();
  }

  @Test
  public void testAddPlayer() {
    int initialPlayerNum = chatServer.getPlayerListSize();
    chatServer.addPlayer(player);
    int updatedPlayerNum = chatServer.getPlayerListSize();
    assertEquals(initialPlayerNum + 1, updatedPlayerNum);
  }

  // @Test
  // public void testSetUp() throws IOException {
  // chatServer.setUp();
  // verify(chatServerSock, times(1)).accept();
  // }

  @Test
  public void testSendWelcome() throws NoSuchFieldException, IllegalAccessException {
    Conn conn1 = mock(Conn.class);
    Conn conn2 = mock(Conn.class);

    // Create the ChatServer object
    ChatServer chatServer = new ChatServer();

    // Get the private field communicators using reflection
    Field communicatorsField = ChatServer.class.getDeclaredField("communicators");
    communicatorsField.setAccessible(true);

    // Set the communicators field with a new ArrayList containing the mock Conn
    // objects
    ArrayList<Conn> communicators = new ArrayList<>();
    communicators.add(conn1);
    communicators.add(conn2);
    communicatorsField.set(chatServer, communicators);

    // Call the sendWelcome method
    chatServer.sendWelcome();
  }

  @Test
  public void testSendToOne() throws NoSuchFieldException, IllegalAccessException {
    // Create mock Conn and Player objects
    Conn conn1 = mock(Conn.class);
    Conn conn2 = mock(Conn.class);
    Player player1 = mock(Player.class);
    Player player2 = mock(Player.class);

    // Set up player colors
    when(player1.getColor()).thenReturn("Red");
    when(player2.getColor()).thenReturn("Blue");

    // Create the ChatServer object
    ChatServer chatServer = new ChatServer();

    // Get and set the private fields using reflection
    Field communicatorsField = ChatServer.class.getDeclaredField("communicators");
    communicatorsField.setAccessible(true);
    ArrayList<Conn> communicators = new ArrayList<>();
    communicators.add(conn1);
    communicators.add(conn2);
    communicatorsField.set(chatServer, communicators);

    Field playersField = ChatServer.class.getDeclaredField("players");
    playersField.setAccessible(true);
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    playersField.set(chatServer, players);

    // Call the sendToOne() method
    String message = "Hello, Red!";
    String name = "Red";
    String from = "Blue";
    chatServer.sendToOne(message, name, from);

    String from1 = "map";
    chatServer.sendToOne(message, name, from1);

    String from2 = "Blue1";
    chatServer.sendToOne(message, name, from2);

    // Verify that the send() method was called on the correct Conn object with the
    // expected message
    // verify(conn1).send(from + ": " + message + "(Private message)");
    // verify(conn2, never()).send(from + ": " + message + "(Private message)");
  }

  @Test
  public void testSendToAll1() throws NoSuchFieldException, IllegalAccessException {
    // Create mock Conn and Player objects
    Conn conn1 = mock(Conn.class);
    Conn conn2 = mock(Conn.class);
    Player player1 = mock(Player.class);
    Player player2 = mock(Player.class);

    // Set up player colors
    when(player1.getColor()).thenReturn("Red");
    when(player2.getColor()).thenReturn("Blue");

    // Create the ChatServer object
    ChatServer chatServer = new ChatServer();

    // Get and set the private fields using reflection
    Field communicatorsField = ChatServer.class.getDeclaredField("communicators");
    communicatorsField.setAccessible(true);
    ArrayList<Conn> communicators = new ArrayList<>();
    communicators.add(conn1);
    communicators.add(conn2);
    communicatorsField.set(chatServer, communicators);

    Field playersField = ChatServer.class.getDeclaredField("players");
    playersField.setAccessible(true);
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    playersField.set(chatServer, players);

    // Call the sendToOne() method
    chatServer.sendWelcome();

    // Verify that the send() method was called on the correct Conn object with the
    // expected message
    // verify(conn1).send(from + ": " + message + "(Private message)");
    // verify(conn2, never()).send(from + ": " + message + "(Private message)");
  }
}
