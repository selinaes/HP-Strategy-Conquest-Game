package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;

public class MoveInputRuleCheckerTest {
    @Test
    public void test_from_not_owner() {
        Territory t1 = new Territory("t1");
        Territory t2 = new Territory("t2");
        t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
        List<Territory> Territories = new ArrayList<Territory>();
        // Territories.add(t1);
        Territories.add(t2);
        Conn connection = mock(Conn.class);
        Player p1 = new Player("red", connection, Territories, 1);
        t1.setOwner(new Player("blue", connection, Territories, 1));
        t2.setOwner(p1);
        Unit u = new AdvancedUnit(p1, t1, false, 1);
        t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
        GameMap map = new GameMap(1);
        MoveInputRuleChecker checker = new MoveInputRuleChecker(null);
        assertEquals("You do not own the from territory", checker.checkMyRule(t1, t2, p1, 1, map, 0));
    }

    @Test
    public void test_to_not_owner() {
        Territory t1 = new Territory("t1");
        Territory t2 = new Territory("t2");
        t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
        List<Territory> Territories = new ArrayList<Territory>();
        Territories.add(t1);
        // Territories.add(t2);
        Conn connection = mock(Conn.class);
        Player p1 = new Player("red", connection, Territories, 1);
        t1.setOwner(p1);
        t2.setOwner(new Player("blue", connection, new ArrayList<Territory>(), 1));
        Unit u = new AdvancedUnit(p1, t1, false, 1);
        t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
        GameMap map = new GameMap(1);
        MoveInputRuleChecker checker = new MoveInputRuleChecker(null);
        assertEquals("You do not own the to territory", checker.checkMyRule(t1, t2, p1, 1, map, 0));
    }
}
