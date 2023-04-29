package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.junit.jupiter.api.Test;
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

public class MessageGeneratorTest {
  @Test
  public void testFormMap() {
    // create mock players and territories
    Player player1 = mock(Player.class);
    Conn connectionMock = mock(Conn.class);
    when(player1.getColor()).thenReturn("red");
    when(player1.getConn()).thenReturn(connectionMock);
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
    MessageGenerator game = new MessageGenerator();
    ArrayList<Player> players = new ArrayList<>();
    List<String> colors = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    colors.add(player1.getColor());
    colors.add(player2.getColor());

    HashMap<String, ArrayList<HashMap<String, String>>> actualMap = game.formMap(players);
    GameMap defaultMap = new GameMap(2);
    defaultMap.createBasicMap();
    HashMap<String, ArrayList<HashMap<String, String>>> initialMap = game.formInitialMap(defaultMap, colors);
    // game.formInitialMap();
    // compare the expected and actual results
    // assertEquals(expectedMap, actualMap);
    Player player3 = mock(Player.class);
    when(player3.getColor()).thenReturn("blue");
    when(player3.getAlly()).thenReturn(player2);
    players.add(player3);
    HashMap<String, ArrayList<HashMap<String, String>>> allianceMap = game.formMap(players);
  }

  @Test
  public void test_sendMap() throws Exception {
    Player player1 = mock(Player.class);
    Conn mockConnection = mock(Conn.class);
    doReturn(mockConnection).when(player1).getConn();
    HashMap<String, ArrayList<HashMap<String, String>>> toSend = new HashMap<>();
    MessageGenerator game = new MessageGenerator();
    game.sendMap(player1, toSend);

    Field objectMapper = game.getClass().getDeclaredField("objectMapper");
    objectMapper.setAccessible(true);
    ObjectMapper o = mock(ObjectMapper.class);
    when(o.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {
    });
    objectMapper.set(game, o);
    game.sendMap(player1, toSend);
  }

  @Test
  public void test_sendInitialMap() throws Exception {
    Conn mockConnection = mock(Conn.class);
    HashMap<String, ArrayList<HashMap<String, String>>> toSend = new HashMap<>();
    MessageGenerator game = new MessageGenerator();
    game.sendInitialMap(mockConnection, toSend);

    Field objectMapper = game.getClass().getDeclaredField("objectMapper");
    objectMapper.setAccessible(true);
    ObjectMapper o = mock(ObjectMapper.class);
    when(o.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {
    });
    objectMapper.set(game, o);
    game.sendInitialMap(mockConnection, toSend);
  }

  @Test
  public void test_sendLog() throws Exception {
    Player player1 = mock(Player.class);
    Conn mockConnection = mock(Conn.class);
    doReturn(mockConnection).when(player1).getConn();
    HashMap<String, String> toSend = new HashMap<>();
    MessageGenerator game = new MessageGenerator();
    game.sendLog(player1, toSend);
    Field objectMapper = game.getClass().getDeclaredField("objectMapper");
    objectMapper.setAccessible(true);
    ObjectMapper o = mock(ObjectMapper.class);
    when(o.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {
    });
    objectMapper.set(game, o);
    game.sendLog(player1, toSend);
  }

  @Test
  public void test_sendEntry() throws Exception {
    Player player1 = mock(Player.class);
    Conn mockConnection = mock(Conn.class);
    doReturn(mockConnection).when(player1).getConn();
    MessageGenerator game = new MessageGenerator();
    game.sendEntry(player1);
    Field objectMapper = game.getClass().getDeclaredField("objectMapper");
    objectMapper.setAccessible(true);
    ObjectMapper o = mock(ObjectMapper.class);
    when(o.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {
    });
    objectMapper.set(game, o);
    game.sendEntry(player1);
  }

}
