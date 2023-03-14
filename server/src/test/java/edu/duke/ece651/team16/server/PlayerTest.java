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
    Player p = new Player("blue", c1, list, 2);

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
    Player p = new Player("blue", c1, list, 2);
    assertEquals(p.getColor(), "blue");
  }

  @Test
  public void test_getConnection() {
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    assertEquals(p.getConnection(), c1);
  }

  @Test
  public void test_units(){
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    Player p = new Player("blue", c1, list, 2);
    assertEquals(2, p.unplacedUnits());
  }

  @Test
  public void test_TerritoryNames(){
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(new Territory("A"));
    list.add(new Territory("B"));
    Player p = new Player("blue", c1, list, 2);
    List<String> e1 = new ArrayList<String>();
    e1.add("A");
    e1.add("B");
    assertEquals(e1, p.getTerritoryNames());
  }

  @Test
  public void test_placeUnit(){
    Connection c1 = mock(Connection.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(new Territory("A"));
    Player p = new Player("blue", c1, list, 2);
    p.placeUnitsSameTerritory("A", 2);
    assertEquals(0, p.unplacedUnits());
    // assertEquals(null, p.findNextUnplacedUnit());
  }

}
