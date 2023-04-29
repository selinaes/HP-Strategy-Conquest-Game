package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team16.server.AllianceOrder;
import edu.duke.ece651.team16.server.Player;

import java.util.*;
import static org.mockito.Mockito.*;

public class AllianceOrderTest {
  @Test
  public void test_valid_tryAction() {
    GameMap map = new GameMap(1);
    map.setMap(map.createBasicMap());
    List<Territory> Territories = new ArrayList<Territory>();
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Player p2 = new Player("blue", connection, Territories, 1);
    AllianceOrder mo = new AllianceOrder(p1,p2);
    assertEquals("Waiting for Alliance", mo.tryAction());
    p1.setPendingAlly(p2);
    p2.setPendingAlly(p1);
    AllianceOrder mo1 = new AllianceOrder(p1,p2);
    assertEquals(null,mo1.tryAction());
  }

  @Test
  public void test_invalid_tryAction() {
    GameMap map = new GameMap(1);
    map.setMap(map.createBasicMap());
    List<Territory> Territories = new ArrayList<Territory>();
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Player p2 = new Player("blue", connection, Territories, 1);
    p1.setAlly(p2);
    p2.setAlly(p1);
    AllianceOrder mo = new AllianceOrder(p1,p1);
    assertEquals("You can not choose yourself", mo.tryAction());
    AllianceOrder mo1 = new AllianceOrder(p1,p2);
    assertEquals("You have already formed alliance", mo1.tryAction());
  }
}
