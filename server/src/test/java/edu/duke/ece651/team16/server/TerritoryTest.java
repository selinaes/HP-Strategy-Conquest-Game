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

  @Test
  public void testGetUnits() {
    Territory territory = new Territory("Gondor");
    ArrayList<Unit> units = territory.getUnits();
    assertEquals(0, units.size());
    Connection c2 = mock(Connection.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player player = new Player("red", c2, list, 2);
    player.addTerritories(territory);
    Unit unit = new BasicUnit(player, null, true, 1);
    territory.tryAddUnits(unit);
    units = territory.getUnits();
    assertEquals(1, units.size());
    assertEquals(unit, units.get(0));
  }

  @Test
  public void testTryAddUnits() {
    // Test adding unit to territory without owner
    Territory territory = new Territory("Gondor");
    Connection c2 = mock(Connection.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player player = new Player("red", c2, list, 2);
    player.addTerritories(territory);
    Unit unit = new BasicUnit(player, null, true, 2);
    territory.tryAddUnits(unit);
    assertEquals(1, territory.getUnits().size());
    assertEquals(territory, unit.getwhere());

    // Test adding unit to territory with different owner
    Connection c1 = mock(Connection.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    Player player2 = new Player("blue", c1, list, 2);
    Unit unit2 = new BasicUnit(player2, null, false, 2);
    territory.tryAddUnits(unit2);
    assertEquals(2, territory.getUnits().size());
    assertEquals(territory, unit2.getwhere());

    // Test adding unit to territory with same owner
    Unit unit3 = new BasicUnit(player, null, false, 3);
    territory.tryAddUnits(unit3);
    assertEquals(3, territory.getUnits().size());
    assertEquals(territory, unit.getwhere());
    assertEquals(territory, unit3.getwhere());
  }

  @Test
  public void testGetUnitsString() {
    Territory territory = new Territory("Gondor");
    assertEquals("0 units", territory.getUnitsString());
    Connection c2 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Player player = new Player("red", c2, list, 2);
    player.addTerritories(territory);
    Unit unit = new BasicUnit(player, null, true, 1);
    territory.tryAddUnits(unit);
    assertEquals("1 units", territory.getUnitsString());

    Unit unit2 = new BasicUnit(player, null, false, 2);
    territory.tryAddUnits(unit2);
    assertEquals("2 units", territory.getUnitsString());

    Unit unit3 = new BasicUnit(player, null, false, 3);
    territory.tryAddUnits(unit3);
    assertEquals("3 units", territory.getUnitsString());
  }

}
