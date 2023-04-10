package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResearchOrderTest {
  @Test
  public void test_valid_tryMove() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new AdvancedUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    GameMap map = new GameMap(1);
    ResearchOrder mo = new ResearchOrder(p1);
    assertEquals("Not enough tech resource. Need 20 tech resource, but only have 0.", mo.tryAction());
  }

  @Test
  public void test_valid_tryResearch() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new AdvancedUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    GameMap map = new GameMap(1);
    p1.newResourcePerTurn();
    p1.newResourcePerTurn();
    ResearchOrder mo = new ResearchOrder(p1);
    assertEquals(null, mo.tryAction());
  }

  @Test
  public void test_valid_tryResearch1() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new AdvancedUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    GameMap map = new GameMap(1);
    p1.newResourcePerTurn();
    p1.newResourcePerTurn();
    p1.updateResearchRound(true);

    ResearchOrder mo = new ResearchOrder(p1);
    assertEquals("You have already researched in this turn.", mo.tryAction());
  }

  @Test
  public void test_valid_tryResearch2() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
    List<Territory> Territories = new ArrayList<Territory>();
    Territories.add(t1);
    Territories.add(t2);
    Conn connection = mock(Conn.class);
    Player p1 = new Player("red", connection, Territories, 1);
    Unit u = new AdvancedUnit(p1, t1, false, 1);
    t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
    GameMap map = new GameMap(1);
    p1.newResourcePerTurn();
    p1.newResourcePerTurn();
    p1.updateTechLevel();
    p1.updateTechLevel();
    p1.updateTechLevel();
    p1.updateTechLevel();
    p1.updateTechLevel();
    p1.updateTechLevel();
    p1.updateTechLevel();

    ResearchOrder mo = new ResearchOrder(p1);
    assertEquals("You have already reached the max research level.", mo.tryAction());
  }

}
