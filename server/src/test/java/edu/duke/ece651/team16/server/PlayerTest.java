package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.net.Socket;
import java.util.List;
import org.junit.jupiter.api.Test;
import edu.duke.ece651.team16.shared.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {
  @Test
  public void test_addTerritory() throws IOException {
    Connection c1 = mock(Connection.class);
    // when(c1.getSocket()).thenReturn(s1);

    // create a list for territories
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list);

    Territory t1 = new Territory("Narnia");
    Territory t2 = new Territory("Gondor");
    list.add(t1);
    list.add(t2);

    p.addTerritories(t1, t2);

    assertEquals(p.getTerritories(), list);
    assertEquals(t1.getOwner(), p);
  }

  @Test
  public void test_getColor() throws IOException {
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list);
    assertEquals(p.getColor(), "blue");
  }

  @Test
  public void test_getConnection() {
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list);
    assertEquals(p.getConnection(), c1);
  }

}
