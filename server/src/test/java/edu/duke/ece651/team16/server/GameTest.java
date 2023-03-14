package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import static org.mockito.Mockito.*;
import org.mockito.Mockito;

import org.junit.jupiter.api.Test;

public class GameTest {
  // @Test
  // public void test_game() {

  // }

  // @Test
  // public void testCreatePlayer() throws IOException {
  // // Mock objects
  // Socket mockSocket = mock(Socket.class);
  // Connection mockConnection = mock(Connection.class);
  // ArrayList<Connection> mockConnections = mock(ArrayList.class);
  // Player mockPlayer = mock(Player.class);
  // HashMap<String, ArrayList<HashMap<String, String>>> mockMap =
  // mock(HashMap.class);
  // Game mockGame = spy(new Game());

  // // Set up mock objects
  // when(mockGame.getAllConnections()).thenReturn(mockConnections);
  // when(mockGame.getDefaultMap()).thenReturn(mockMap);
  // when(mockConnection.getSocket()).thenReturn(mockSocket);
  // when(mockGame.chooseColor(mockConnection)).thenReturn("red");
  // doNothing().when(mockGame).sendMap(mockPlayer, mockMap);

  // // Call the method being tested
  // mockGame.createPlayer(mockSocket, 1);

  // // Verify that the expected methods were called
  // verify(mockConnections).add(any(Connection.class));
  // verify(mockGame).chooseNumOfPlayers(any(Connection.class), eq(1));
  // verify(mockGame).chooseColor(any(Connection.class));
  // verify(mockGame).addPlayer(any(Player.class));
  // verify(mockGame).sendMap(any(Player.class), any(HashMap.class));
  // }

  @Test
  public void test_chooseColor() throws IOException {
    Connection connectionMock = mock(Connection.class);
    Game game = new Game();
    when(connectionMock.recv()).thenReturn("red");
    String color = game.chooseColor(connectionMock);
    assertEquals(color, "red");
    verify(connectionMock)
        .send(
            "Please enter a color you want to choose. Current available colors are: red blue ");
    verify(connectionMock).recv();
    verify(connectionMock).send("Valid");
    verifyNoMoreInteractions(connectionMock);
  }

  @Test
  public void testChooseColorInvalid() throws IOException {
    Connection connectionMock = mock(Connection.class);
    Game game = new Game();
    when(connectionMock.recv()).thenReturn("0", "red");
    String color = game.chooseColor(connectionMock);
    verify(connectionMock, times(2))
        .send("Please enter a color you want to choose. Current available colors are: red blue ");
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
    Game game = new Game();
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
    verify(connectionMock).send("Stage Complete");
  }

  @Test
  public void testChooseNumOfPlayers2() throws IOException {
    // create mock connection and set behavior
    Connection connectionMock = mock(Connection.class);
    // create a game and call the method
    Game game = new Game();
    game.chooseNumOfPlayers(connectionMock, 2);
    // check the numPlayer and sent message
    verify(connectionMock).send("Not the first player. Waiting for others to set player number.");
  }

  @Test
  public void testChooseNumOfPlayersInvalid() throws IOException {
    Game game = new Game();
    Connection connectionMock = mock(Connection.class);
    when(connectionMock.recv()).thenReturn("1", "3");
    game.chooseNumOfPlayers(connectionMock, 1);

    // HashMap<String, ArrayList<HashMap<String, String>>> actualMap = game.formMap();
    verify(connectionMock, times(2))
        .send("You are the first player! Please set the number of players in this game(Valid player number: 2-4): ");
    verify(connectionMock).send("Invalid number of players");
    verify(connectionMock, times(2)).recv(); // ensure recv method is called 2 times
    verify(connectionMock, times(2)).send("Valid");
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
    player1List.add(t1Map);
    HashMap<String, String> t2Map = new HashMap<>();
    t2Map.put("TerritoryName", "Territory2");
    t2Map.put("Neighbors", "(next to: Territory1, Territory3)");
    player1List.add(t2Map);
    expectedMap.put("red", player1List);

    ArrayList<HashMap<String, String>> player2List = new ArrayList<>();
    HashMap<String, String> t3Map = new HashMap<>();
    t3Map.put("TerritoryName", "Territory3");
    t3Map.put("Neighbors", "(next to: Territory1, Territory2)");
    player2List.add(t3Map);
    expectedMap.put("blue", player2List);

    // call the method with mock players
    Game game = new Game();
    // ServerSocket serverSocketMock = mock(ServerSocket.class);
    // Server server = new Server(serverSocketMock);
    game.addPlayer(player1);
    game.addPlayer(player2);
    
    HashMap<String, ArrayList<HashMap<String, String>>> actualMap = game.formMap();
    // compare the expected and actual results
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void test_sendMap() throws Exception{
    Player player1 = mock(Player.class);
    Connection connectionMock = mock(Connection.class);
    Game game = new Game();
    
    //create hashmap
    ObjectMapper mapperMock = mock(ObjectMapper.class);
    HashMap<String, ArrayList<HashMap<String, String>>> map = new HashMap<>();
    when(mapperMock.writeValueAsString(map)).thenThrow(new JsonProcessingException("error"));
    assertThrows(JsonProcessingException.class, ()->game.sendMap(player1, map));
  }

}
