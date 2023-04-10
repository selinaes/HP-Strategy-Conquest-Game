package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team16.server.UpgradeOrder;

import java.util.*;
import static org.mockito.Mockito.*;

public class UpgradeOrderTest {
    @Test
    public void test_not_enough_resource() {

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
        UpgradeOrder uo = new UpgradeOrder(p1, t1, 1, 0, 1);
        assertEquals("Not enough tech resource. Need 3 tech resource, but only have 0.", uo.tryAction());
    }

}
