package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MapTest {
    @Test
    public void test_getColorList() {
        Map m1 = new Map(2);
        ArrayList<String> e1 = new ArrayList<String>();
        e1.add("Red");
        e1.add("Blue");
        assertEquals(e1, m1.getColorList());
        Map m2 = new Map(3);
        e1.add("Yellow");
        assertEquals(e1, m2.getColorList());
        Map m3 = new Map(4);
        e1.add("Green");
        assertEquals(e1, m3.getColorList());
    }

    @Test
    public void test_createBasic() {
        Map m1 = new Map(2);
        HashMap<String, List<Territory>> e1 = new HashMap<String, List<Territory>>();
        Territory red = new Territory("Red");
        Territory blue = new Territory("Blue");
        red.setNeighbors(Arrays.asList(blue));
        e1.put("Red", Arrays.asList(red));
        e1.put("Blue", Arrays.asList(blue));
        assertEquals(e1.get("Red"), m1.createBasicMap().get("Red"));
        assertEquals(e1.get("Blue"), m1.createBasicMap().get("Blue"));
    }

}