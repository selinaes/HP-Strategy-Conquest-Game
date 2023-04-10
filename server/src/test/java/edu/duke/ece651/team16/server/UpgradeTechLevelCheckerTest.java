package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;
import static org.mockito.Mockito.*;

public class UpgradeTechLevelCheckerTest {
    @Test
    public void test_invalid() {
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
        p1.newResourcePerTurn();
        UpgradeOrder uo = new UpgradeOrder(p1, t1, 1, 0, 2);
        assertEquals("You do not have enough tech level to upgrade desired level amount for these units",
                uo.tryAction());
    }

}
