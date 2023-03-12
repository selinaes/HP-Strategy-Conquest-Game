package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ServerTest {
  @Test
  public void test_formMap() {
    int port = 1234;
    Server s = new Server(port);
    Player p1 = new Player("p1", "blue", null);
    Territory t = new Territory("Gondor");
    t.setNeighbor(new Territory("Mordor"));
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
