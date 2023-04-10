package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

public class BasicUnitTest {
  @Test
  public void testSetWhere() {
    Conn c1 = mock(Conn.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    Territory territory1 = new Territory("Territory 1");
    Territory territory2 = new Territory("Territory 2");
    AdvancedUnit unit = new AdvancedUnit(p, territory1, true, 1);

    unit.setwhere(territory2);
    unit.setisAttacker();

    assertEquals(territory2, unit.getwhere());
  }

  @Test
  public void testGetWhere() {
    Conn c1 = mock(Conn.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    Territory territory1 = new Territory("Territory 1");
    AdvancedUnit unit = new AdvancedUnit(p, territory1, true, 1);

    Territory where = unit.getwhere();

    assertEquals(territory1, where);
  }

  @Test
  public void testGetOwner() {
    Conn c1 = mock(Conn.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    Territory territory1 = new Territory("Territory 1");
    AdvancedUnit unit = new AdvancedUnit(p, territory1, true, 1);

    Player owner = unit.getOwner();

    assertEquals(p, owner);
  }

  @Test
  public void testGetAlive() {
    Conn c1 = mock(Conn.class);
    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    Territory territory1 = new Territory("Territory 1");
    AdvancedUnit unit = new AdvancedUnit(p, territory1, true, 1);
    unit.getId();
    boolean alive = unit.getAlive();
    unit.setBonus(1);
    unit.setLevel(1);
    assertEquals(1, unit.getLevel());
    unit.setName("kate");
    assertEquals("kate", unit.getName());
    assertEquals(true, alive);
  }

  @Test
  public void testBasicUnit() {
    // Conn c1 = mock(Conn.class);
    // // create a list for territories
    // List<Territory> list = new ArrayList<Territory>();
    // Player p = new Player("blue", c1, list, 2);
    // Territory territory1 = new Territory("Territory 1");
    // BasicUnit unit = new AdvancedUnit(p, territory1, true, 1);
    // unit.upgrade(1);
    // unit.getId();
    // boolean alive = unit.getAlive();
    // assertEquals(1, unit.getLevel());
    // assertEquals(1, unit.getBonus());
    // String name = unit.getName();
    // assertEquals(true, alive);
    // List<Territory> list = new ArrayList<Territory>();

    // // create a Player object
    // Player p = new Player("blue", null, list, 2);

    // // create a Territory object
    // Territory territory1 = new Territory("Territory 1");

    // // create a BasicUnitImpl object
    // BasicUnit unit = new AdvancedUnit(p, territory1, true, 1);

    // // test the methods of the BasicUnitImpl object
    // unit.upgrade(1);
    // assertEquals(1, unit.getLevel());
    // assertEquals(0, unit.getBonus());
    // assertEquals("BasicUnit", unit.getName());
    // assertTrue(unit.getAlive());
  }

}
