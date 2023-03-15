package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

public class MoveOrderTest {
  @Test
  public void test_valid_tryMove() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Connection connection = mock(Connection.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new BasicUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    Map map = new Map(1);
    MoveOrder mo = new MoveOrder(t1, t2, 1, p1, map);
    assertEquals(null, mo.tryMove());
  }

  @Test
  public void test_not_enough_unit() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Connection connection = mock(Connection.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Map map = new Map(1);
    MoveOrder mo = new MoveOrder(t1, t2, 1, p1, map);
    mo.getPlayer();
    assertEquals("You do not have enough alive units in the from territory", mo.tryMove());
  }

}
