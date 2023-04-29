package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import java.lang.reflect.Field;
import java.lang.annotation.Target;
import java.util.*;
import java.util.ArrayList;

public class TerritoryTest {
  @Test
  public void test_name() {
    Territory t1 = new Territory("Gondor");
    assertEquals("Gondor", t1.getName());
  }

  @Test
  public void test_owner() {
    Territory t1 = new Territory("Gondor");
    Conn c1 = mock(Conn.class);
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
    // assertEquals(neighName, t1.getNeighborsNames());
    assertNotEquals(null, t1.getNeighborsNames());
  }

  @Test
  public void testSetRates() {
    int expectedFoodRate = 10;
    int expectedTechRate = 20;

    // Create an instance of the class to be tested
    Territory terr = new Territory("a");

    // Set the rates
    terr.setFoodRate(expectedFoodRate);
    terr.setTechRate(expectedTechRate);

    // Verify that the rates have been set correctly
    assertEquals(expectedFoodRate, terr.getFoodRate());
    assertEquals(expectedTechRate, terr.getTechRate());
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
  public void testGetDistance() {
    Territory t1 = new Territory("a");
    Territory t2 = new Territory("b");
    Integer distance = 5;
    t1.setNeighbors(List.of(t2));
    t1.setDistance(List.of(t2), List.of(distance));

    int actualDistance = t1.getDistance(t2);

    assertEquals(distance, actualDistance);
  }

  @Test
  public void testTryRemoveUnits() {
    Territory t = new Territory("test");

    Conn c2 = mock(Conn.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(t);
    Player p = new Player("red", c2, list, 2);

    Unit u1 = new AdvancedUnit(p, t, false, 0);
    Unit u2 = new AdvancedUnit(p, t, false, 0);
    ArrayList l = new ArrayList<>();
    l.add(u1);
    l.add(u2);
    t.tryAddUnits(l);
    t.tryRemoveUnits(2, p, 0);
    assertEquals(0, t.getUnits().size());
  }



  @Test
  public void testGetUnits() {
    Territory territory = new Territory("Gondor");
    ArrayList<Unit> units = territory.getUnits();
    assertEquals(0, units.size());
    Conn c2 = mock(Conn.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    list.add(territory);
    Player player = new Player("red", c2, list, 2);
    // player.addTerritories(list);
    Unit unit = new AdvancedUnit(player, null, true, 1);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit)));
    units = territory.getUnits();
    assertEquals(1, units.size());
    assertEquals(unit, units.get(0));
  }

  @Test
  public void testTryAddUnits() {
    // Test adding unit to territory without owner
    Territory territory = new Territory("Gondor");
    Conn c2 = mock(Conn.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player player = new Player("red", c2, list, 2);
    list.add(territory);
    // player.addTerritories(territory);
    Unit unit = new AdvancedUnit(player, null, true, 2);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit)));
    assertEquals(1, territory.getUnits().size());
    assertEquals(territory, unit.getwhere());

    // Test adding unit to territory with different owner
    Conn c1 = mock(Conn.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    Player player2 = new Player("blue", c1, list, 2);
    Unit unit2 = new AdvancedUnit(player2, null, false, 2);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit2)));

    assertEquals(2, territory.getUnits().size());
    assertEquals(territory, unit2.getwhere());

    // Test adding unit to territory with same owner
    Unit unit3 = new AdvancedUnit(player, null, false, 3);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit3)));
    assertEquals(3, territory.getUnits().size());
    assertEquals(territory, unit.getwhere());
    assertEquals(territory, unit3.getwhere());
  }

  @Test
  public void testGetUnitsString() {
    Territory territory = new Territory("Gondor");
    // assertEquals("0 units", territory.getUnitsString());
    Conn c2 = mock(Conn.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(territory);
    Player player = new Player("red", c2, list, 2);
    // player.addTerritories(territory);
    Unit unit = new AdvancedUnit(player, null, true, 1);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit)));
    assertEquals("red:1,0,0,0,0,0,0,;", territory.getUnitsString());

    Unit unit2 = new AdvancedUnit(player, null, false, 2);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit2)));
    assertEquals("red:2,0,0,0,0,0,0,;", territory.getUnitsString());

    Unit unit3 = new AdvancedUnit(player, null, false, 3);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit3)));
    assertEquals("red:3,0,0,0,0,0,0,;", territory.getUnitsString());
  }


  @Test
  public void testGetAliveUnitsFor() {
    Territory territory = new Territory("Gondor");
    Conn c2 = mock(Conn.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(territory);
    Player player = new Player("red", c2, list, 2);

    Unit unit = new AdvancedUnit(player, null, true, 1);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit)));
    assertEquals(1, territory.getAliveUnitsFor(player, 0).size());

    Unit unit2 = new AdvancedUnit(player, null, true, 2);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit2)));
    assertEquals(2, territory.getAliveUnitsFor(player, 0).size());

    // another player
    Conn c1 = mock(Conn.class);
    Player player2 = new Player("blue", c1, list, 2);
    Unit unit3 = new AdvancedUnit(player2, null, false, 3);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit3)));
    assertEquals(2, territory.getAliveUnitsFor(player, 0).size());
  }

  @Test
  public void test_existBattle() {

    Territory territory = new Territory("Gondor");
    assertFalse(territory.existsBattle());

    Conn c2 = mock(Conn.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(territory);
    Player player = new Player("red", c2, list, 2);

    Unit unit = new AdvancedUnit(player, null, true, 1);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit)));
    territory.defendHome();
    assertTrue(territory.existsBattle());
  }

  @Test
  public void test_DefendHome() {
    Territory territory1 = new Territory("Gondor");
    ArrayList<Unit> list = new ArrayList<>();
    territory1.tryAddAttackers(list);
    Territory territory = new Territory("Gondor");
    territory.defendHome();
    assertEquals(0, territory.getUnits().size());
    assertFalse(territory.existsBattle());
  }

  @Test
  public void testDoBattle() {
    Territory territory = new Territory("Gondor");
    assertFalse(territory.existsBattle());

    Conn c2 = mock(Conn.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(territory);
    Player player = new Player("red", c2, list, 2);

    Unit unit = new AdvancedUnit(player, null, true, 1);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit)));
    String winner = territory.doBattle();
    assertEquals("Battle participants: Player red has 1 units. \nBattle Winner: red\n", winner);
  }

  @Test
  public void testDoBattle2() {
    Territory territory = new Territory("Gondor");
    assertFalse(territory.existsBattle());

    Conn c2 = mock(Conn.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(territory);
    Player player = new Player("red", c2, list, 2);

    Unit unit = new AdvancedUnit(player, null, true, 1);
    territory.tryAddUnits(new ArrayList<Unit>(Arrays.asList(unit)));
    String winner = territory.doBattle();
    assertEquals("Battle participants: Player red has 1 units. \nBattle Winner: red\n", winner);
  }

  @Test
  public void testDoBattle1() {
    Territory territory = new Territory("Gondor");
    assertFalse(territory.existsBattle());
    Conn c2 = mock(Conn.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(territory);
    Player player = new Player("red", c2, list, 1);
    territory.setOwner(player);

    Territory territory1 = new Territory("Duke");
    Conn c = mock(Conn.class);
    List<Territory> list0 = new ArrayList<Territory>();
    list0.add(territory1);
    Player player1 = new Player("blue", c, list0, 5);

    Unit unit = new AdvancedUnit(player1, null, true, 1);
    territory.tryAddAttackers(new ArrayList<Unit>(Arrays.asList(unit)));
    territory.tryAddAttackers(new ArrayList<Unit>(Arrays.asList(new AdvancedUnit(player1, null, true, 2))));
    territory.tryAddAttackers(new ArrayList<Unit>(Arrays.asList(new AdvancedUnit(player1, null, true, 3))));
    territory.tryAddAttackers(new ArrayList<Unit>(Arrays.asList(new AdvancedUnit(player1, null, true, 4))));
    territory.tryAddAttackers(new ArrayList<Unit>(Arrays.asList(new AdvancedUnit(player1, null, true, 5))));
    territory.toString();
    String winner = territory.doBattle();
    // assertEquals("Battle participants: Player red has 1 units. \nBattle Winner:
    // red\n", winner);
  }
}
