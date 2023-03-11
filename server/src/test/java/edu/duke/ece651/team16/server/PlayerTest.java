package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PlayerTest {
  @Test
  public void test_addTerritory() {
    Player p = new Player("testPlayer", "blue");
    Territory t = new Territory("Gondor");
    p.addTerritories(t);
    // create a list and add the territory to it
    List<Territory> list = new ArrayList<Territory>();
    list.add(t);
    assertEquals(p.getTerritories(), list);
  }

  @Test
  public void test_getName() {
    Player p = new Player("testPlayer", "blue");
    assertEquals(p.getName(), "testPlayer");
  }

}
