package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(MockitoJUnitRunner.class)
@PrepareForTest(ObjectMapper.class)
public class GameTest {
  // @Test
  // public void testSendInitialMapJsonProcessingException() throws Exception {
  // // Create a mock Connection & HashMap object
  // Connection connMock = mock(Connection.class);
  // HashMap<String, ArrayList<HashMap<String, String>>> mockMap = new
  // HashMap<>();

  // // Mock the ObjectMapper class
  // ObjectMapper objectMapperMock = PowerMockito.mock(ObjectMapper.class);
  // PowerMockito.mock(ObjectMapper.class);
  // PowerMockito.whenNew(ObjectMapper.class).withNoArguments().thenReturn(objectMapperMock);
  // PowerMockito.when(objectMapperMock.writeValueAsString(mockMap)).thenThrow(new
  // JsonProcessingException("test", null, null));

  // Game game = new Game(2);

  // try {
  // game.sendInitialMap(connMock, mockMap);
  // } catch (JsonProcessingException e) {
  // // Verify that the JsonProcessingException was thrown
  // assertEquals(JsonProcessingException.class, e.getClass());
  // throw e;
  // }
  // }

  @Test
  public void testGameFlow() throws Exception {
    String[] inputs = new String[] { "1", "red", "Duke Garden", "2", "m", "Duke Garden, Duke Chapel, 10",
        "Duke Garden, Duke Chapel, 2", "d" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());

    // Mock objects
    Socket mockSocket = mock(Socket.class);
    OutputStream mockOutputStream = mock(OutputStream.class);
    when(mockSocket.getInputStream()).thenReturn(sysIn);
    when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    Game Game = new Game(2);

    // Modify the playerLowBound field of game to 1 player
    Field readyPlayerField = Game.getClass().getDeclaredField("playerLowBound");
    readyPlayerField.setAccessible(true);
    readyPlayerField.set(Game, 1);

    // Call the method
    Game.gameFlow(mockSocket, 1);
  }

  @Test
  public void testdoPlacementPhase() throws IOException, Exception {
    String[] inputs = new String[] { "2", "red", "Duke Garden", "2" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());

    // Mock objects
    Socket mockSocket = mock(Socket.class);
    OutputStream mockOutputStream = mock(OutputStream.class);
    when(mockSocket.getInputStream()).thenReturn(sysIn);
    when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    Game Game = new Game(2);

    // Get the numPlayer field of game
    Field readyPlayerField = Game.getClass().getDeclaredField("readyPlayer");
    readyPlayerField.setAccessible(true);
    readyPlayerField.set(Game, 1);

    // Call the method being tested
    Game.doPlacementPhase(mockSocket, 1);
  }

  @Test
  public void testRun2() throws Exception {
    String[] inputs = new String[] { "1" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());

    // Mock objects
    Socket mockSocket = mock(Socket.class);
    OutputStream mockOutputStream = mock(OutputStream.class);
    when(mockSocket.getInputStream()).thenReturn(sysIn);
    when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    Game Game = new Game(2);

    Thread testThread = new Thread(new Runnable() {
      @Override
      public void run() {
        Game.doPlacementPhase(mockSocket, 2); // call the run() method of the Server instance
      }
    });
    testThread.start();

    // Wait for the server to start
    Thread.sleep(100);

    // Interrupt the server after a short delay
    testThread.interrupt();
  }

  @Test
  public void testRun3() throws Exception {
    String[] inputs = new String[] { "2", "red", "Duke Garden", "2" };
    String inputString = String.join("\n", inputs) + "\n";
    InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());

    // Mock objects
    Socket mockSocket = mock(Socket.class);
    OutputStream mockOutputStream = mock(OutputStream.class);
    when(mockSocket.getInputStream()).thenReturn(sysIn);
    when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    Game Game = new Game(2);

    Thread testThread = new Thread(new Runnable() {
      @Override
      public void run() {
        Game.doPlacementPhase(mockSocket, 1); // call the run() method of the Server instance
      }
    });
    testThread.start();

    // Wait for the server to start
    Thread.sleep(100);

    // Interrupt the server after a short delay
    testThread.interrupt();
  }

  @Test
  public void test_chooseColor() throws IOException {
    Connection connectionMock = mock(Connection.class);
    Game game = new Game(2);
    when(connectionMock.recv()).thenReturn("red");
    String color = game.chooseColor(connectionMock);
    assertEquals(color, "red");
    verify(connectionMock)
        .send(
            "Please enter a color you want to choose. Current available colors are: red blue yellow green ");
    verify(connectionMock).recv();
    verify(connectionMock).send("Valid");
    verifyNoMoreInteractions(connectionMock);
  }

  @Test
  public void testChooseColorInvalid() throws IOException {
    Connection connectionMock = mock(Connection.class);
    Game game = new Game(2);
    when(connectionMock.recv()).thenReturn("0", "red");
    String color = game.chooseColor(connectionMock);
    verify(connectionMock, times(2))
        .send("Please enter a color you want to choose. Current available colors are: red blue yellow green ");
    verify(connectionMock).send("Invalid color");
    verify(connectionMock).send("Valid");
    assertEquals(color, "red");
  }

  @Test
  public void testChooseNumOfPlayers() throws IOException, Exception {
    // create mock connection and set behavior
    Connection connectionMock = mock(Connection.class);
    List<Connection> Connections = new ArrayList<>();
    Connections.add(connectionMock);

    when(connectionMock.recv()).thenReturn("3");

    // create a game and call the method
    Game game = new Game(2);
    Field allConnections = game.getClass().getDeclaredField("allConnections");
    allConnections.setAccessible(true);
    allConnections.set(game, Connections);

    game.chooseNumOfPlayers(connectionMock, 1);

    // check the numPlayer and sent message
    assertEquals(3, game.getNumPlayer());
    verify(connectionMock)
        .send("You are the first player! Please set the number of players in this game(Valid player number: 2-4): ");
    verify(connectionMock).recv();
    verify(connectionMock).send("Valid");
    verify(connectionMock).send("stage Complete");
  }

  @Test
  public void testChooseNumOfPlayers2() throws IOException {
    // create mock connection and set behavior
    Connection connectionMock = mock(Connection.class);
    // create a game and call the method
    Game game = new Game(2);
    game.chooseNumOfPlayers(connectionMock, 2);
    // check the numPlayer and sent message
    verify(connectionMock).send("Not the first player. Please wait for the first player to set player number.");
  }

  @Test
  public void testChooseNumOfPlayersInvalid() throws IOException {
    Game game = new Game(2);
    Connection connectionMock = mock(Connection.class);
    when(connectionMock.recv()).thenReturn("1", "3");
    game.chooseNumOfPlayers(connectionMock, 1);

    verify(connectionMock, times(2))
        .send("You are the first player! Please set the number of players in this game(Valid player number: 2-4): ");
    verify(connectionMock).send("Invalid number of players");
    verify(connectionMock, times(2)).recv(); // ensure recv method is called 2 times
    verify(connectionMock, times(1)).send("Valid");
  }

  @Test
  public void testChooseNumOfPlayersInvalid2() throws IOException {
    Game game = new Game(2);
    Connection connectionMock = mock(Connection.class);
    when(connectionMock.recv()).thenReturn("a", "3");
    game.chooseNumOfPlayers(connectionMock, 1);

    verify(connectionMock, times(2))
        .send("You are the first player! Please set the number of players in this game(Valid player number: 2-4): ");
    verify(connectionMock).send("Invalid number of players");
    verify(connectionMock, times(2)).recv(); // ensure recv method is called 2 times
    verify(connectionMock, times(1)).send("Valid");
  }

  @Test
  public void testFormMap() {
    // create mock players and territories
    Player player1 = mock(Player.class);
    Connection connectionMock = mock(Connection.class);
    when(player1.getColor()).thenReturn("red");
    when(player1.getConnection()).thenReturn(connectionMock);
    List<Territory> territories1 = new ArrayList<>();
    Territory t1 = mock(Territory.class);
    when(t1.getName()).thenReturn("Territory1");
    when(t1.getNeighborsNames()).thenReturn("(next to: Territory2, Territory3)");
    territories1.add(t1);
    Territory t2 = mock(Territory.class);
    when(t2.getName()).thenReturn("Territory2");
    when(t2.getNeighborsNames()).thenReturn("(next to: Territory1, Territory3)");
    territories1.add(t2);
    when(player1.getTerritories()).thenReturn(territories1);

    Player player2 = mock(Player.class);
    when(player2.getColor()).thenReturn("blue");
    List<Territory> territories2 = new ArrayList<>();
    Territory t3 = mock(Territory.class);
    when(t3.getName()).thenReturn("Territory3");
    when(t3.getNeighborsNames()).thenReturn("(next to: Territory1, Territory2)");
    territories2.add(t3);
    when(player2.getTerritories()).thenReturn(territories2);

    // create the expected result
    HashMap<String, ArrayList<HashMap<String, String>>> expectedMap = new HashMap<>();
    ArrayList<HashMap<String, String>> player1List = new ArrayList<>();
    HashMap<String, String> t1Map = new HashMap<>();
    t1Map.put("TerritoryName", "Territory1");
    t1Map.put("Neighbors", "(next to: Territory2, Territory3)");
    t1Map.put("Unit", null);
    player1List.add(t1Map);
    HashMap<String, String> t2Map = new HashMap<>();
    t2Map.put("TerritoryName", "Territory2");
    t2Map.put("Neighbors", "(next to: Territory1, Territory3)");
    t2Map.put("Unit", null);
    player1List.add(t2Map);
    expectedMap.put("red", player1List);

    ArrayList<HashMap<String, String>> player2List = new ArrayList<>();
    HashMap<String, String> t3Map = new HashMap<>();
    t3Map.put("TerritoryName", "Territory3");
    t3Map.put("Neighbors", "(next to: Territory1, Territory2)");
    t3Map.put("Unit", null);
    player2List.add(t3Map);
    expectedMap.put("blue", player2List);

    // call the method with mock players
    Game game = new Game(2);
    game.addPlayer(player1);
    game.addPlayer(player2);

    HashMap<String, ArrayList<HashMap<String, String>>> actualMap = game.formMap();
    HashMap<String, ArrayList<HashMap<String, String>>> initialMap = game.formInitialMap();
    // compare the expected and actual results
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void test_sendMap() throws Exception {
    Player player1 = mock(Player.class);
    Connection mockConnection = mock(Connection.class);
    doReturn(mockConnection).when(player1).getConnection();
    HashMap<String, ArrayList<HashMap<String, String>>> toSend = new HashMap<>();
    Game game = new Game(2);
    game.sendMap(player1, toSend);
  }

  @Test
  public void test_sendInitialMap() throws Exception {
    Connection mockConnection = mock(Connection.class);
    HashMap<String, ArrayList<HashMap<String, String>>> toSend = new HashMap<>();
    Game game = new Game(2);
    game.sendInitialMap(mockConnection, toSend);

    Socket mockSocket = mock(Socket.class);
  }

  @Test
  public void testAssignUnits() throws IOException {
    // Create a mock Connection object
    Connection mockConnection = mock(Connection.class);

    // Create a Player object with some units to assign
    List<Territory> territories = new ArrayList<>();
    Territory t1 = new Territory("Territory 1");
    territories.add(t1);
    Player player = new Player("Player 1", mockConnection, territories, 5);

    // Create a Game object to use with the method
    Game game = new Game(5);
    game.setNumPlayer(1);
    game.addPlayer(player);

    // Set up the Connection mock to return some values when send() and recv() are
    // called
    when(mockConnection.recv()).thenReturn("Territory 1", "3", "done");

    // Call the assignUnits method
    game.assignUnits(player, mockConnection);

    // Verify that the mockConnection.send() method was called with the expected
    // strings
    verify(mockConnection).send(
        "You have 5 units left. If you want to finish placement, enter done. Otherwise, choose a territory to assign units to. Please enter the territory name: ");
    verify(mockConnection).send(
        "You have 5 units left. If you want to finish placement, enter done. Otherwise, how many units do you want to assign to Territory 1? Please enter a number: ");
    verify(mockConnection).send("finished stage");

    // Verify that the Player object has the expected number of units in the
    // Territory object
    // Territory territory = player.getTerritoryByName("Territory 1");
    assertEquals("3 units", t1.getUnitsString());
  }

  @Test
  public void testAssignUnits2() throws IOException, Exception {
    String[] inputs = new String[] { "Territory 1", "5", "done" };
    String inputString = String.join("\n", inputs) + "\n";
    Socket mockSocket = mock(Socket.class);
    InputStream mockInputStream = new ByteArrayInputStream(inputString.getBytes());
    OutputStream mockOutputStream = mock(OutputStream.class);
    when(mockSocket.getInputStream()).thenReturn(mockInputStream);
    when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    Connection mockConnection = new Connection(mockSocket);

    // Create a Player object with some units to assign
    List<Territory> territories = new ArrayList<>();
    Territory t1 = new Territory("Territory 1");
    territories.add(t1);
    Player player = new Player("Player 1", mockConnection, territories, 5);

    // Create a Game object to use with the method
    Game game = new Game(5);
    game.setNumPlayer(1);

    game.addPlayer(player);
  }

  @Test
  public void testAssignUnits3() throws IOException {
    // Create a mock Connection object
    Connection mockConnection = mock(Connection.class);

    // Create a Player object with some units to assign
    List<Territory> territories = new ArrayList<>();
    Territory t1 = new Territory("Territory 1");
    territories.add(t1);
    Player player = new Player("Player 1", mockConnection, territories, 5);

    // Create a Game object to use with the method
    Game game = new Game(5);
    game.setNumPlayer(1);
    game.addPlayer(player);

    // Set up the Connection mock to return some values when send() and recv() are
    // called
    when(mockConnection.recv()).thenReturn("Territory 1", "done");

    // Call the assignUnits method
    game.assignUnits(player, mockConnection);

    // Verify that the mockConnection.send() method was called with the expected
    // strings
    verify(mockConnection).send(
        "You have 5 units left. If you want to finish placement, enter done. Otherwise, choose a territory to assign units to. Please enter the territory name: ");
    verify(mockConnection).send(
        "You have 5 units left. If you want to finish placement, enter done. Otherwise, how many units do you want to assign to Territory 1? Please enter a number: ");
    verify(mockConnection).send("finished stage");
  }

  @Test
  public void testAssignUnitsInvalidTerritory() throws IOException {
    Connection connection = mock(Connection.class);
    Player player = new Player("blue", connection, new ArrayList<Territory>(), 3);
    when(connection.recv()).thenReturn("invalidTerritory", "done");

    Game game = new Game(3);
    game.addPlayer(player);

    game.assignUnits(player, connection);

    verify(connection, times(1)).send("Invalid territory name");
  }

  @Test
  public void testAssignUnitsInvalidNumFormatException() throws IOException {
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Territory territory = new Territory("A");
    list.add(territory);
    Player player = new Player("blue", c1, list, 3);

    when(c1.recv()).thenReturn("A", "invalid", "done");
    Game game = new Game(3);
    game.assignUnits(player, c1);

    verify(c1, times(1)).send("Invalid number of units");
  }

  @Test
  public void testAssignUnits_InvalidNumber() throws IOException {
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Territory territory = new Territory("A");
    list.add(territory);
    Player player = new Player("blue", c1, list, 3);

    when(c1.recv()).thenReturn("A", "-1", "done");
    Game game = new Game(3);
    game.assignUnits(player, c1);

    verify(c1, times(1)).send("Invalid number of units");
  }

  @Test
  public void testCheckNameReturnTerritory() {
    Game game = new Game(2);
    // Create a sample map
    Map map = new Map(2);
    map.createBasicMap();
    // map.addTerritory(new Territory("Territory1", "Player1"));
    // map.addTerritory(new Territory("Territory2", "Player2"));
    // map.addTerritory(new Territory("Territory3", "Player1"));

    // Test for an existing territory name
    Territory t1 = game.checkNameReturnTerritory("A", map);
    assertNotNull(t1);

    // Test for a non-existing territory name
    Territory t2 = game.checkNameReturnTerritory("c", map);
    assertNull(t2);

  }

  @Test
  public void test_formEntry_sendEntry() throws JsonProcessingException, IOException {
    Game game = new Game(2);
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Territory territory = new Territory("A");
    list.add(territory);
    Player player = new Player("blue", c1, list, 2);
    game.sendEntry(player);
    String e1 = "{\"Entry\":\"You are the blue player, what would you like to do?\\n" + "M(ove)\\n" + "A(ttack)\\n"
        + "D(one)\\n\"}";
    verify(c1, times(1)).send(e1);
  }

  @Test
  public void makeActionOrder() throws Exception {
    Game game = new Game(2);
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Territory territory = new Territory("A");
    list.add(territory);
    Player player = new Player("blue", c1, list, 2);
    Map defaultMap = new Map(2);
    defaultMap.createDukeMap();
    game.initializeMap(2);
    when(c1.recv()).thenReturn("A, B, 1", "Perkins Library, Broadhead Center, 1");
    Order actionOrder = game.makeActionOrder(player, "a");
    verify(c1, times(2))
        .send("Please enter in the following format: Territory from, Territory to, number of units(e.g. T1, T2, 2)");
    verify(c1, times(1)).send("Invalid Territory Name");
    Territory fromTerritory = game.checkNameReturnTerritory("Perkins Library", defaultMap);
    Territory toTerritory = game.checkNameReturnTerritory("Broadhead Center", defaultMap);
    assertEquals(player, actionOrder.getPlayer());
  }

}
