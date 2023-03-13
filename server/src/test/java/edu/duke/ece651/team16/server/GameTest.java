package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GameTest {
  @Test
  public void test_game() {

  }


  // @Test
  // public void test_chooseColor() throws IOException {
  //   ServerSocket serverSocketMock = mock(ServerSocket.class);
  //   Connection connectionMock = mock(Connection.class);
  //   Server server = new Server(serverSocketMock);

  //   when(connectionMock.recv()).thenReturn("0");
  //   String color = server.chooseColor(connectionMock);
  //   assertEquals(color, "Red");
  //   verify(connectionMock)
  //       .send(
  //           "Which color do you want to choose? Please enter a number. Current available colors are: 0. Red 1. Blue ");
  //   verify(connectionMock).recv();
  //   verify(connectionMock).send("Valid");
  //   verifyNoMoreInteractions(connectionMock);
  // }

  // @Test
  // public void testChooseColorInvalid() throws IOException {
  //   ServerSocket serverSocketMock = mock(ServerSocket.class);
  //   Connection connection = mock(Connection.class);
  //   Server server = new Server(serverSocketMock);

  //   when(connection.recv()).thenReturn("3", "-1", "0");
  //   String color = server.chooseColor(connection);

  //   verify(connection, times(3)).send(
  //       "Which color do you want to choose? Please enter a number. Current available colors are: 0. Red 1. Blue ");
  //   verify(connection, times(2)).send("Invalid color index");
  //   verify(connection).send("Valid");
  //   assertEquals(color, "Red");
  // }

  // @Test
  // public void testFormMap() {
  //   // create mock players and territories
  //   Player player1 = mock(Player.class);
  //   List<Territory> territories1 = new ArrayList<>();
  //   Territory t1 = mock(Territory.class);
  //   when(t1.getName()).thenReturn("Territory1");
  //   when(t1.getNeighborsNames()).thenReturn("(next to: Territory2, Territory3)");
  //   territories1.add(t1);
  //   Territory t2 = mock(Territory.class);
  //   when(t2.getName()).thenReturn("Territory2");
  //   when(t2.getNeighborsNames()).thenReturn("(next to: Territory1, Territory3)");
  //   territories1.add(t2);
  //   when(player1.getTerritories()).thenReturn(territories1);

  //   Player player2 = mock(Player.class);
  //   List<Territory> territories2 = new ArrayList<>();
  //   Territory t3 = mock(Territory.class);
  //   when(t3.getName()).thenReturn("Territory3");
  //   when(t3.getNeighborsNames()).thenReturn("(next to: Territory1, Territory2)");
  //   territories2.add(t3);
  //   when(player2.getTerritories()).thenReturn(territories2);

  //   // create the expected result
  //   HashMap<String, ArrayList<HashMap<String, String>>> expectedMap = new HashMap<>();
  //   ArrayList<HashMap<String, String>> player1List = new ArrayList<>();
  //   HashMap<String, String> t1Map = new HashMap<>();
  //   t1Map.put("TerritoryName", "Territory1");
  //   t1Map.put("Neighbors", "(next to: Territory2, Territory3)");
  //   player1List.add(t1Map);
  //   HashMap<String, String> t2Map = new HashMap<>();
  //   t2Map.put("TerritoryName", "Territory2");
  //   t2Map.put("Neighbors", "(next to: Territory1, Territory3)");
  //   player1List.add(t2Map);
  //   expectedMap.put("Player1", player1List);

  //   ArrayList<HashMap<String, String>> player2List = new ArrayList<>();
  //   HashMap<String, String> t3Map = new HashMap<>();
  //   t3Map.put("TerritoryName", "Territory3");
  //   t3Map.put("Neighbors", "(next to: Territory1, Territory2)");
  //   player2List.add(t3Map);
  //   expectedMap.put("Player2", player2List);

  //   // call the method with mock players
  //   ServerSocket serverSocketMock = mock(ServerSocket.class);
  //   Server server = new Server(serverSocketMock);
  //   server.addPlayer(player1);
  //   server.addPlayer(player2);
  //   HashMap<String, ArrayList<HashMap<String, String>>> actualMap = server.formMap();

  //   // compare the expected and actual results
  //   assertEquals(expectedMap, actualMap);
  // }

  // @Test
  // public void testEnterName() throws IOException {
  //   // create mock connection and set behavior
  //   Connection connection = mock(Connection.class);
  //   when(connection.recv()).thenReturn("Alice");

  //   // create a game and call the method
  //   ServerSocket serverSocketMock = mock(ServerSocket.class);
  //   Server server = new Server(serverSocketMock);
  //   String name = server.enterName(connection);

  //   // check the return value and sent message
  //   assertEquals("Alice", name);
  //   // assertEquals("Please enter your name: ", connection.getSentMsg());
  // }

  // @Test
  // public void testChooseNumOfPlayers() throws IOException {
  // // create mock connection and set behavior
  // Connection connection = mock(Connection.class);
  // when(connection.recv()).thenReturn("3");

  // // create a game and call the method
  // ServerSocket serverSocketMock = mock(ServerSocket.class);
  // Server server = new Server(serverSocketMock);
  // server.chooseNumOfPlayers(connection);

  // // check the numPlayer and sent message
  // assertEquals(3, server.getNumPlayer());
  // verify(connection).send("How many players does the game have?(Valid player
  // number: 2-4) Please enter a number: ");
  // verify(connection).send("Valid");
  // }

  // @Test
  // public void testChooseNumOfPlayersInvalid() throws IOException {
  // // create mock connection and set behavior
  // Connection connection = mock(Connection.class);
  // when(connection.recv()).thenReturn("1").thenReturn("5").thenReturn("3");
  // ServerSocket serverSocketMock = mock(ServerSocket.class);
  // Server server = new Server(serverSocketMock);
  // // create a game and call the method
  // server.chooseNumOfPlayers(connection);

  // // check the numPlayer and sent message
  // assertEquals(3, server.getNumPlayer());
  // verify(connection).send("How many players does the game have?(Valid player
  // number: 2-4) Please enter a number: ");
  // verify(connection).send("Invalid number of players");
  // verify(connection).send("How many players does the game have?(Valid player
  // number: 2-4) Please enter a number: ");
  // // verify(connection, times(2)).recv(); // ensure recv method is called 2
  // times
  // // before valid input
  // verify(connection).send("Valid");
  // }

}
