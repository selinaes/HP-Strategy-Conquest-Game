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

// @RunWith(PowerMockRunner.class)
// @PowerMockRunnerDelegate(MockitoJUnitRunner.class)
// @PrepareForTest(ObjectMapper.class)
public class GameTest {
    // // @Test
    // // public void testSendInitialMapJsonProcessingException() throws Exception {
    // // // Create a mock Connection & HashMap object
    // // Connection connMock = mock(Connection.class);
    // // HashMap<String, ArrayList<HashMap<String, String>>> mockMap = new
    // // HashMap<>();

    // // // Mock the ObjectMapper class
    // // ObjectMapper objectMapperMock = PowerMockito.mock(ObjectMapper.class);
    // // PowerMockito.mock(ObjectMapper.class);
    // //
    // PowerMockito.whenNew(ObjectMapper.class).withNoArguments().thenReturn(objectMapperMock);
    // //
    // PowerMockito.when(objectMapperMock.writeValueAsString(mockMap)).thenThrow(new
    // // JsonProcessingException("test", null, null));

    // // Game game = new Game(2);

    // // try {
    // // game.sendInitialMap(connMock, mockMap);
    // // } catch (JsonProcessingException e) {
    // // // Verify that the JsonProcessingException was thrown
    // // assertEquals(JsonProcessingException.class, e.getClass());
    // // throw e;
    // // }
    // // }

    // @Test
    // public void testGameFlow() throws Exception {
    // // Player 1's input
    // String[] inputs1 = new String[] { "2", "red", "A", "5", "m", "A, B, 10",
    // "A, A, 0", "a", "A, B, 5", "d" };
    // String inputString1 = String.join("\n", inputs1) + "\n";
    // InputStream sysIn1 = new ByteArrayInputStream(inputString1.getBytes());

    // // Mock objects
    // Socket mockSocket1 = mock(Socket.class);
    // OutputStream mockOutputStream1 = mock(OutputStream.class);
    // when(mockSocket1.getInputStream()).thenReturn(sysIn1);
    // when(mockSocket1.getOutputStream()).thenReturn(mockOutputStream1);

    // // Player 2's input
    // String[] inputs2 = new String[] { "blue", "done", "d" };
    // String inputString2 = String.join("\n", inputs2) + "\n";
    // InputStream sysIn2 = new ByteArrayInputStream(inputString2.getBytes());

    // // Mock objects
    // Socket mockSocket2 = mock(Socket.class);
    // OutputStream mockOutputStream2 = mock(OutputStream.class);
    // when(mockSocket2.getInputStream()).thenReturn(sysIn2);
    // when(mockSocket2.getOutputStream()).thenReturn(mockOutputStream2);

    // Game Game = new Game(5, "Basic");

    // // Call the method
    // Thread redThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // Game.gameFlow(mockSocket1, 1);
    // }
    // });

    // Thread blueThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // Game.gameFlow(mockSocket2, 2);
    // }
    // });
    // redThread.start();
    // blueThread.start();

    // long timeoutMillis = 5000;
    // redThread.join(timeoutMillis);
    // blueThread.join(timeoutMillis);

    // }

    // @Test
    // public void testGameFlow2() throws Exception {
    // // Player 1's input
    // String[] inputs1 = new String[] { "2", "red", "A", "5", "a", "A, B, 5", "d"
    // };
    // String inputString1 = String.join("\n", inputs1) + "\n";
    // InputStream sysIn1 = new ByteArrayInputStream(inputString1.getBytes());

    // // Mock objects
    // Socket mockSocket1 = mock(Socket.class);
    // OutputStream mockOutputStream1 = mock(OutputStream.class);
    // when(mockSocket1.getInputStream()).thenReturn(sysIn1);
    // when(mockSocket1.getOutputStream()).thenReturn(mockOutputStream1);

    // // Player 2's input
    // String[] inputs2 = new String[] { "blue", "done", "d" };
    // String inputString2 = String.join("\n", inputs2) + "\n";
    // InputStream sysIn2 = new ByteArrayInputStream(inputString2.getBytes());

    // // Mock objects
    // Socket mockSocket2 = mock(Socket.class);
    // OutputStream mockOutputStream2 = mock(OutputStream.class);
    // when(mockSocket2.getInputStream()).thenReturn(sysIn2);
    // when(mockSocket2.getOutputStream()).thenReturn(mockOutputStream2);

    // Game Game = new Game(5, "Basic");

    // // Call the method
    // Thread redThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // Game.gameFlow(mockSocket1, 1);
    // }
    // });

    // Thread blueThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // Game.gameFlow(mockSocket2, 2);
    // }
    // });
    // redThread.start();
    // blueThread.start();

    // long timeoutMillis = 5000;
    // redThread.join(timeoutMillis);
    // blueThread.join(timeoutMillis);

    // }

    // @Test
    // public void testGameFlow_with_watch() throws Exception {
    // // Player 1's input
    // String[] inputs1 = new String[] { "3", "red", "R", "5", "a", "R, Y, 5",
    // "d", "m", "R, Y, 1", "a", "Y, B, 7", "d" };
    // String inputString1 = String.join("\n", inputs1) + "\n";
    // InputStream sysIn1 = new ByteArrayInputStream(inputString1.getBytes());

    // // Mock objects
    // Socket mockSocket1 = mock(Socket.class);
    // OutputStream mockOutputStream1 = mock(OutputStream.class);
    // when(mockSocket1.getInputStream()).thenReturn(sysIn1);
    // when(mockSocket1.getOutputStream()).thenReturn(mockOutputStream1);

    // // Player 2's input
    // String[] inputs2 = new String[] { "blue", "done", "d", "d" };
    // String inputString2 = String.join("\n", inputs2) + "\n";
    // InputStream sysIn2 = new ByteArrayInputStream(inputString2.getBytes());

    // // Mock objects
    // Socket mockSocket2 = mock(Socket.class);
    // OutputStream mockOutputStream2 = mock(OutputStream.class);
    // when(mockSocket2.getInputStream()).thenReturn(sysIn2);
    // when(mockSocket2.getOutputStream()).thenReturn(mockOutputStream2);

    // // Player 3's input
    // String[] inputs3 = new String[] { "yellow", "done", "d", "w" };
    // String inputString3 = String.join("\n", inputs3) + "\n";
    // InputStream sysIn3 = new ByteArrayInputStream(inputString3.getBytes());

    // // Mock objects
    // Socket mockSocket3 = mock(Socket.class);
    // OutputStream mockOutputStream3 = mock(OutputStream.class);
    // when(mockSocket3.getInputStream()).thenReturn(sysIn3);
    // when(mockSocket3.getOutputStream()).thenReturn(mockOutputStream3);

    // Game Game = new Game(5, "Test3");

    // // Call the method
    // Thread redThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // Game.gameFlow(mockSocket1, 1);
    // }
    // });

    // Thread blueThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // Game.gameFlow(mockSocket2, 2);
    // }
    // });

    // Thread yellowThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // Game.gameFlow(mockSocket2, 3);
    // }
    // });
    // redThread.start();
    // blueThread.start();
    // yellowThread.start();

    // long timeoutMillis = 5000;
    // redThread.join(timeoutMillis);
    // blueThread.join(timeoutMillis);
    // yellowThread.join(timeoutMillis);

    // }

    // @Test
    // public void testDoActionPhase_lose_chooseExit() throws Exception {
    // // Set up mock objects
    // Conn mockConn = mock(Conn.class);
    // Player mockPlayer = mock(Player.class);
    // when(mockPlayer.getConn()).thenReturn(mockConn);
    // when(mockPlayer.getisWatch()).thenReturn(false);
    // when(mockPlayer.checkLose()).thenReturn(true);
    // when(mockConn.recv()).thenReturn("e");

    // // Call the method under test
    // Game game = new Game(5, "Basic");
    // game.doActionPhase(mockPlayer);

    // // Verify that the appropriate methods were called on the mock objects
    // verify(mockConn).send("Choose watch");
    // verify(mockPlayer).getisWatch();
    // verify(mockPlayer).checkLose();

    // }

    // @Test
    // public void testRun2() throws Exception {
    // String[] inputs = new String[] { "1" };
    // String inputString = String.join("\n", inputs) + "\n";
    // InputStream sysIn = new ByteArrayInputStream(inputString.getBytes());

    // // Mock objects
    // Socket mockSocket = mock(Socket.class);
    // OutputStream mockOutputStream = mock(OutputStream.class);
    // when(mockSocket.getInputStream()).thenReturn(sysIn);
    // when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

    // Game Game = new Game(2, "Duke");

    // Thread testThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // Game.doPlacementPhase(mockSocket, 2); // call the run() method of the Server
    // instance
    // }
    // });
    // testThread.start();

    // // Wait for the server to start
    // Thread.sleep(100);

    // // Interrupt the server after a short delay
    // testThread.interrupt();
    // }

    @Test
    public void testRun3() throws Exception {

        Conn mockConn = mock(Conn.class);
        when(mockConn.recv()).thenReturn("2", "red", "Duke Garden", "2");

        Game Game = new Game(2, "Duke");

        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Game.doPlacementPhase(mockConn, 1); // call the run() method of the Server instance
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
        Conn connectionMock = mock(Conn.class);
        Game game = new Game(2, "Duke");
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
        Conn connectionMock = mock(Conn.class);
        Game game = new Game(2, "Duke");
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
        Conn connectionMock = mock(Conn.class);
        List<Conn> Connections = new ArrayList<>();
        Connections.add(connectionMock);

        when(connectionMock.recv()).thenReturn("3");

        // create a game and call the method
        Game game = new Game(2, "Duke");
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
        verify(connectionMock).send("finished stage");
    }

    @Test
    public void testChooseNumOfPlayers2() throws IOException {
        // create mock connection and set behavior
        Conn connectionMock = mock(Conn.class);
        // create a game and call the method
        Game game = new Game(2, "Duke");
        game.chooseNumOfPlayers(connectionMock, 2);
        // check the numPlayer and sent message
        verify(connectionMock).send("finished stage");
    }

    @Test
    public void testChooseNumOfPlayersInvalid() throws IOException {
        Game game = new Game(2, "Duke");
        Conn connectionMock = mock(Conn.class);
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
        Game game = new Game(2, "Duke");
        Conn connectionMock = mock(Conn.class);
        when(connectionMock.recv()).thenReturn("a", "3");
        game.chooseNumOfPlayers(connectionMock, 1);

        verify(connectionMock, times(2))
                .send("You are the first player! Please set the number of players in this game(Valid player number: 2-4): ");
        verify(connectionMock).send("Invalid number of players");
        verify(connectionMock, times(2)).recv(); // ensure recv method is called 2 times
        verify(connectionMock, times(1)).send("Valid");
    }

    @Test
    public void testAssignUnits() throws IOException {
        // Create a mock Connection object
        Conn mockConnection = mock(Conn.class);

        // Create a Player object with some units to assign
        List<Territory> territories = new ArrayList<>();
        Territory t1 = new Territory("Territory 1");
        territories.add(t1);
        Player player = new Player("Player 1", mockConnection, territories, 5);

        // Create a Game object to use with the method
        Game game = new Game(5, "Duke");
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
        assertEquals("Player 1:3,0,0,0,0,0,0,;", t1.getUnitsString());
    }

    @Test
    public void testAssignUnits_InvalidNumber2() throws IOException {
        Player player = mock(Player.class);
        when(player.unplacedUnits()).thenReturn(0);

        Conn c = mock(Conn.class);

        Game game = new Game(3, "Duke");
        game.assignUnits(player, c);
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

        Conn mockConnection = new Conn(mockSocket);

        // Create a Player object with some units to assign
        List<Territory> territories = new ArrayList<>();
        Territory t1 = new Territory("Territory 1");
        territories.add(t1);
        Player player = new Player("Player 1", mockConnection, territories, 5);

        // Create a Game object to use with the method
        Game game = new Game(5, "Duke");
        game.setNumPlayer(1);

        game.addPlayer(player);
    }

    @Test
    public void testAssignUnits3() throws IOException {
        // Create a mock Connection object
        Conn mockConnection = mock(Conn.class);

        // Create a Player object with some units to assign
        List<Territory> territories = new ArrayList<>();
        Territory t1 = new Territory("Territory 1");
        territories.add(t1);
        Player player = new Player("Player 1", mockConnection, territories, 5);

        // Create a Game object to use with the method
        Game game = new Game(5, "Duke");
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
        Conn connection = mock(Conn.class);
        Player player = new Player("blue", connection, new ArrayList<Territory>(), 3);
        when(connection.recv()).thenReturn("invalidTerritory", "done");

        Game game = new Game(3, "Duke");
        game.addPlayer(player);

        game.assignUnits(player, connection);

        verify(connection, times(1)).send("Invalid territory name");
    }

    @Test
    public void testAssignUnitsInvalidNumFormatException() throws IOException {
        Conn c1 = mock(Conn.class);
        List<Territory> list = new ArrayList<Territory>();
        Territory territory = new Territory("A");
        list.add(territory);
        Player player = new Player("blue", c1, list, 3);

        when(c1.recv()).thenReturn("A", "invalid", "done");
        Game game = new Game(3, "Duke");
        game.assignUnits(player, c1);

        verify(c1, times(1)).send("Invalid number of units");
    }

    @Test
    public void testAssignUnits_InvalidNumber() throws IOException {
        Conn c1 = mock(Conn.class);
        List<Territory> list = new ArrayList<Territory>();
        Territory territory = new Territory("A");
        list.add(territory);
        Player player = new Player("blue", c1, list, 3);

        when(c1.recv()).thenReturn("A", "-1", "done");
        Game game = new Game(3, "Duke");
        game.assignUnits(player, c1);

        verify(c1, times(1)).send("Invalid number of units");
    }

    @Test
    public void testCheckNameReturnTerritory() {
        Game game = new Game(2, "Duke");
        // Create a sample map
        GameMap map = new GameMap(2);
        map.createBasicMap();

        // Test for an existing territory name
        Territory t1 = game.checkNameReturnTerritory("A", map);
        assertNotNull(t1);

        // Test for a non-existing territory name
        Territory t2 = game.checkNameReturnTerritory("c", map);
        assertNull(t2);

    }

    // @Test
    // public void makeMoveAttackOrder() throws Exception {
    //     System.out.println("makeActionOrder start");
    //     Game game = new Game(2, "Duke");
    //     Conn c1 = mock(Conn.class);
    //     List<Territory> list = new ArrayList<Territory>();
    //     Territory territory = new Territory("A");
    //     list.add(territory);
    //     Player player = new Player("blue", c1, list, 2);
    //     GameMap defaultMap = new GameMap(2);
    //     defaultMap.createDukeMap();
    //     game.initializeMap(2);
    //     when(c1.recv()).thenReturn("A, B, 1", "perkins, broadhead, 1");
    //     Order actionOrder = game.makeActionOrder(player, "a");
    //     verify(c1, times(2)).send(
    //             "Please enter in the following format: Territory from, Territory to,number of units(e.g. T1, T2, 2)");
    //     verify(c1, times(1)).send("Invalid Territory Name");
    //     Territory fromTerritory = game.checkNameReturnTerritory("perkins",
    //             defaultMap);
    //     Territory toTerritory = game.checkNameReturnTerritory("broadhead",
    //             defaultMap);
    //     System.out.println("makeActionOrder passed");
    // }

    @Test
    public void testMakeResearchOrder() {
        // create a mock Player object
        Player mockPlayer = mock(Player.class);
        Conn mockConn = mock(Conn.class);
        when(mockPlayer.getConn()).thenReturn(mockConn);

        // create a Game object
        Game game = new Game(2, "Duke");
        game.addPlayer(mockPlayer);

        // call makeResearchOrder and verify message is sent through connection
        Order order = game.makeResearchOrder(mockPlayer);
        verify(mockConn).send("Please notice you can perform research only once each turn.");

        // verify that the returned Order is a ResearchOrder object
        assertTrue(order instanceof ResearchOrder);
    }

    @Test
    public void test_ifChooseWatch() {
        Conn conn = mock(Conn.class);
        when(conn.recv()).thenReturn("q").thenReturn("w");
        Game game = new Game(2, "Duke");
        game.ifChooseWatch(conn);
    }

    @Test
    public void test_findWinner() throws Exception {
        Game game = new Game(2, "Duke");
        Field gameField = game.getClass().getDeclaredField("players");
        gameField.setAccessible(true);
        Player p1 = mock(Player.class);
        when(p1.checkLose()).thenReturn(true);

        List<Player> p = new ArrayList<Player>();
        p.add(p1);

        gameField.set(game, p);
        game.findWinner();
        when(p1.checkLose()).thenReturn(false);
        gameField.set(game, p);
        game.findWinner();
    }

    @Test
    public void testGetGameState() {
        Game game = new Game(2, "Duke");
        assertEquals("setNumPlayer", game.getGameState());
    }

    @Test
    public void testGenerateUnit() {
        Game game = new Game(2, "Duke");

        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        doNothing().when(p1).generateNewUnit();
        doNothing().when(p2).generateNewUnit();

        game.addPlayer(p1);
        game.addPlayer(p2);

        game.generateUnit();

        verify(p1, times(1)).generateNewUnit();
        verify(p2, times(1)).generateNewUnit();
    }

    @Test
    public void testProduceResources() {
        Game game = new Game(2, "Duke");

        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        doNothing().when(p1).newResourcePerTurn();
        doNothing().when(p2).newResourcePerTurn();

        game.addPlayer(p1);
        game.addPlayer(p2);

        game.produceResources();
        verify(p1, times(1)).newResourcePerTurn();
        verify(p2, times(1)).newResourcePerTurn();
    }

    @Test
    public void test_worldWar() throws Exception {
        Game game = new Game(2, "Duke");
        Field gameField = game.getClass().getDeclaredField("players");
        gameField.setAccessible(true);
        Territory territory1 = mock(Territory.class);
        ArrayList<Territory> list = new ArrayList<>();
        list.add(territory1);
        when(territory1.existsBattle()).thenReturn(true);
        when(territory1.doBattle()).thenReturn("do");

        Player p1 = mock(Player.class);
        when(p1.getTerritories()).thenReturn(list);
        doNothing().when(p1).generateNewUnit();
        doNothing().when(p1).newResourcePerTurn();

        List<Player> p = new ArrayList<Player>();
        p.add(p1);

        gameField.set(game, p);
        game.worldwar();

        when(territory1.existsBattle()).thenReturn(false);
        gameField.set(game, p);
        game.worldwar();
    }

    // @Test
    // public void test_doAction() throws Exception {
    // Game game = new Game(2, "Duke");
    // Field gameField = game.getClass().getDeclaredField("messageGenerator");
    // gameField.setAccessible(true);
    // MessageGenerator m = mock(MessageGenerator.class);
    // doNothing().when(m).sendEntry(any());
    // gameField.set(game, m);

    // Player p = mock(Player.class);
    // Conn c = mock(Conn.class);
    // when(p.getConn()).thenReturn(c);
    // when(c.recv()).thenReturn("x");
    // game.doAction(p);
    // }

}
