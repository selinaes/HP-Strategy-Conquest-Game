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

    assertEquals(true, alive);
  }

}
