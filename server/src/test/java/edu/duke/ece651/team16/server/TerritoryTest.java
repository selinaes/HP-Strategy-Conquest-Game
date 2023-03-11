package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team16.server.Territory;
import java.util.ArrayList;

public class TerritoryTest {
  @Test
  public void test_name() {
    Territory t1 = new Territory("Gondor");
    assertEquals("Gondor", t1.getName());
  }

  @Test
  public void test_neighbor(){
    Territory t1 = new Territory("Gondor");
    Territory t2 = new Territory("Mordor");
    t1.setNeighbor(t2);
    ArrayList<Territory> neighbors = new ArrayList<Territory>();
    neighbors.add(t2);
    assertEquals(neighbors, t1.getNeighbors());
  }

}
