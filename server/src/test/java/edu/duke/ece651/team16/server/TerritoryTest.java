package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

public class TerritoryTest {
  @Test
  public void test_name() {
    Territory t1 = new Territory("Gondor");
    assertEquals("Gondor", t1.getName());
  }

  @Test
  public void test_owner() {
    Territory t1 = new Territory("Gondor");
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Player p1 = new Player("blue", c1, list, 2);
    t1.setOwner(p1);
    assertEquals(p1, t1.getOwner());
  }

  @Test
  public void test_setTerritories() {
    Territory t1 = new Territory("Gondor");
    Territory t2 = new Territory("Mordor");
    Territory t3 = new Territory("Narnia");

    ArrayList<Territory> neighbors = new ArrayList<Territory>();
    neighbors.add(t2);
    neighbors.add(t3);

    t1.setNeighbors(neighbors);
    t1.setNeighbors(neighbors); // test no duplicates
    assertEquals(neighbors, t1.getNeighbors());

    String neighName = "(next to: Mordor, Narnia)";
    assertEquals(neighName, t1.getNeighborsNames());
    assertNotEquals(null, t1.getNeighborsNames());

  }

  // add test for equals() return false
  @Test
  public void test_equals() {
    Territory t1 = new Territory("Gondor");
    Territory t2 = new Territory("Mordor");
    Territory t3 = new Territory("Narnia");
    Territory t4 = new Territory("Gondor");
    assertFalse(t1.equals(null));
    assertFalse(t1.equals(t3));
    assertTrue(t1.equals(t4));
  }

}
