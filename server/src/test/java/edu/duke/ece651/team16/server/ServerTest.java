package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ServerTest {
  @Test
  public void test_formMap() {
    Server s = new Server();
    Player p1 = new Player("p1", "blue");
    Territory t = new Territory("Gondor");
    s.addPlayer(p1);
    p1.addTerritories(t);
    HashMap<String, ArrayList<String>> map = new HashMap<String,  ArrayList<String>>();
    ArrayList<String> list = new ArrayList<String>();
    list.add("Gondor");
    map.put("p1", list);
    assertEquals(map,  s.formMap());
  }

}
