package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;

public class AttackInputRuleCheckerTest {
  @Test
  public void test_from_not_owner() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    // Territories.add(t1);
    Territories.add(t2);
    Connection connection = mock(Connection.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new BasicUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    Map map = new Map(1);
    AttackInputRuleChecker checker = new AttackInputRuleChecker(null);
    assertEquals("You do not own the from territory", checker.checkMyRule(t1, t2, p1, 1, map));
  }

  @Test
  public void test_to_owner() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Connection connection = mock(Connection.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new BasicUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    Map map = new Map(1);
    AttackInputRuleChecker checker = new AttackInputRuleChecker(null);
    assertEquals("You do own the to territory", checker.checkMyRule(t1, t2, p1, 1, map));
  }

  @Test
  public void test_not_enough_units() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Connection connection = mock(Connection.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new BasicUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    Map map = new Map(1);
    AttackInputRuleChecker checker = new AttackInputRuleChecker(null);
    assertEquals("You do not have enough alive units in the from territory", checker.checkMyRule(t1, t2, p1, 5, map));
  }

  @Test
  public void test_pass() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Connection connection = mock(Connection.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new BasicUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    Map map = new Map(1);
    AttackInputRuleChecker checker = new AttackInputRuleChecker(null);
    assertEquals(null, checker.checkMyRule(t1, t2, p1, 1, map));
  }

}
