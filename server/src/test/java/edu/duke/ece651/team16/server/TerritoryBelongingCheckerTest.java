package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;
import static org.mockito.Mockito.*;

public class TerritoryBelongingCheckerTest {
    @Test
    public void test_valid() {
        UpgradeRuleChecker checker = new TerritoryBelongingChecker(null);
        GameMap map = new GameMap(1);
        map.setMap(map.createBasicMap());
        Territory t1 = map.getTerritoryList().get(0);
        Territory t2 = map.getTerritoryList().get(1);
        t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
        List<Territory> Territories = new ArrayList<Territory>();
        Territories.add(t1);
        Territories.add(t2);
        Conn connection = mock(Conn.class);
        Player p1 = new Player("red", connection, Territories, 1);
        t1.setOwner(p1);
        t2.setOwner(p1);
        Unit u = new AdvancedUnit(p1, t1, false, 0);
        t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
        p1.newResourcePerTurn();
        UpgradeOrder uo = new UpgradeOrder(p1, t1, 1, 0, 1);
        assertEquals(null, uo.tryAction());
    }

    @Test
    public void test_invalid() {
        UpgradeRuleChecker checker = new TerritoryBelongingChecker(null);
        GameMap map = new GameMap(1);
        map.setMap(map.createBasicMap());
        Territory t1 = map.getTerritoryList().get(0);
        Territory t2 = map.getTerritoryList().get(1);
        t1.setNeighbors(new ArrayList<Territory>(Arrays.asList(t2)));
        List<Territory> Territories = new ArrayList<Territory>();
        Territories.add(t2);
        Conn connection = mock(Conn.class);
        Player p1 = new Player("red", connection, Territories, 1);
        t2.setOwner(p1);
        Unit u = new AdvancedUnit(p1, t1, false, 0);
        t1.tryAddUnits(new ArrayList<Unit>(Arrays.asList(u)));
        p1.newResourcePerTurn();
        UpgradeOrder uo = new UpgradeOrder(p1, t1, 1, 0, 1);
        assertEquals("You do not own the territory where you want to upgrade units", uo.tryAction());
    }

}
