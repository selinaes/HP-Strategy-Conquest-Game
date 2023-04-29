package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

public class MoveOrderTest {
  @Test
  public void test_valid_tryMove() {
    GameMap map = new GameMap(1);
    map.setMap(map.createBasicMap());
    Territory t1 = map.getTerritoryList().get(0);
    Territory t2 = map.getTerritoryList().get(1);
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    t1.setOwner(p1);
    t2.setOwner(p1);
    Unit u = new AdvancedUnit(p1, t1, false, 0);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    p1.newResourcePerTurn();
    MoveOrder mo = new MoveOrder(t1, t2, 1, p1, map, 0);
    assertEquals(null, mo.tryAction());
  }

  @Test
  public void test_not_enough_unit() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    GameMap map = new GameMap(1);
    MoveOrder mo = new MoveOrder(t1, t2, 1, p1, map, 0);
    assertEquals("You do not have enough students of this level in the from territory", mo.tryAction());
  }

  @Test
  public void test_not_enough_resource() {
    GameMap map = new GameMap(1);
    map.setMap(map.createBasicMap());
    Territory t1 = map.getTerritoryList().get(0);
    Territory t2 = map.getTerritoryList().get(1);
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    t1.setOwner(p1);
    t2.setOwner(p1);
    Unit u = new AdvancedUnit(p1, t1, false, 0);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    MoveOrder mo = new MoveOrder(t1, t2, 1, p1, map, 0);
    assertEquals("Not enough food resource. Need 5 food resources, but only have 0 food resource.", mo.tryAction());
  }

}
