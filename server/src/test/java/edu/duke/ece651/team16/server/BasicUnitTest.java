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
    Connection c1 = mock(Connection.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    Territory territory1 = new Territory("Territory 1");
    Territory territory2 = new Territory("Territory 2");
    BasicUnit unit = new BasicUnit(p, territory1, true, 1);

    unit.setwhere(territory2);

    assertEquals(territory2, unit.getwhere());
  }

  @Test
  public void testGetWhere() {
    Connection c1 = mock(Connection.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    Territory territory1 = new Territory("Territory 1");
    BasicUnit unit = new BasicUnit(p, territory1, true, 1);

    Territory where = unit.getwhere();

    assertEquals(territory1, where);
  }

  @Test
  public void testGetOwner() {
    Connection c1 = mock(Connection.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    Territory territory1 = new Territory("Territory 1");
    BasicUnit unit = new BasicUnit(p, territory1, true, 1);

    Player owner = unit.getOwner();

    assertEquals(p, owner);
  }

  @Test
  public void testGetAlive() {
    Connection c1 = mock(Connection.class);
    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    Territory territory1 = new Territory("Territory 1");
    BasicUnit unit = new BasicUnit(p, territory1, true, 1);

    boolean alive = unit.getAlive();

    assertEquals(true, alive);
  }

}
