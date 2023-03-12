package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import java.util.List;

public class ServerTest {
  @Test
  public void test_formMap() {
    int port = 1234;
    Server s = new Server(port);
    Player p1 = new Player("p1", "blue", null);
    Territory t = new Territory("Gondor");
    ArrayList<Territory> neighbors = new ArrayList<Territory>();
    neighbors.add(new Territory("Mordor"));
    t.setNeighbors(neighbors);
    s.addPlayer(p1);
    p1.addTerritories(t);
    HashMap<String, ArrayList<String>> map = new HashMap<String,  ArrayList<String>>();
    ArrayList<String> list = new ArrayList<String>();
    list.add("Gondor");
    list.add("Mordor");
    map.put("p1", list);
    assertEquals(map,  s.formMap());
  }

}
