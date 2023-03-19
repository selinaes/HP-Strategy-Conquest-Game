package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MapTest {
    @Test
    public void test_getColorList() {
        GameMap m1 = new GameMap(2);
        ArrayList<String> e1 = new ArrayList<String>();
        e1.add("red");
        e1.add("blue");
        assertEquals(e1, m1.getColorList());
        GameMap m2 = new GameMap(3);
        e1.add("yellow");
        assertEquals(e1, m2.getColorList());
        GameMap m3 = new GameMap(4);
        e1.add("green");
        assertEquals(e1, m3.getColorList());
    }

    @Test
    public void test_createBasic() {
        GameMap m1 = new GameMap(2);
        HashMap<String, List<Territory>> e1 = new HashMap<String, List<Territory>>();
        Territory red = new Territory("A");
        Territory blue = new Territory("B");
        red.setNeighbors(Arrays.asList(blue));
        e1.put("red", Arrays.asList(red));
        e1.put("blue", Arrays.asList(blue));
        e1.get("red").toString();
        assertNotEquals(m1, m1.createBasicMap().get("blue"));
        assertEquals(e1.get("red"), m1.createBasicMap().get("red"));
        assertEquals(e1.get("blue"), m1.createBasicMap().get("blue"));
        m1.createDukeMap();
        GameMap m2 = new GameMap(-1);
        m2.createDukeMap();
        GameMap m3 = new GameMap(3);
        m3.createDukeMap();
        GameMap m4 = new GameMap(4);
        m4.createDukeMap();
    }

    @Test
    public void test_createTest() {
        GameMap m1 = new GameMap(2);
        HashMap<String, List<Territory>> e1 = new HashMap<String, List<Territory>>();
        Territory red = new Territory("A");
        Territory blue = new Territory("B");
        red.setNeighbors(Arrays.asList(blue));
        e1.put("red", Arrays.asList(red));
        e1.put("blue", Arrays.asList(blue));
        e1.get("red").toString();
        assertNotEquals(m1, m1.createBasicMap().get("blue"));
        assertEquals(e1.get("red"), m1.createBasicMap().get("red"));
        assertEquals(e1.get("blue"), m1.createBasicMap().get("blue"));
        m1.createDukeMap();
        GameMap m2 = new GameMap(-1);
        m2.createTestMap();
        GameMap m3 = new GameMap(3);
        m3.createTestMap();
        GameMap m4 = new GameMap(4);
        m4.createTestMap();
    }

}