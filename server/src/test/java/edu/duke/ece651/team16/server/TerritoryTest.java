package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
    t1.addNeighbor(t2);
    ArrayList<Territory> e1 = new ArrayList<Territory>();
    e1.add(t2);
    assertEquals(e1, t1.getNeighbors());
  }

}
