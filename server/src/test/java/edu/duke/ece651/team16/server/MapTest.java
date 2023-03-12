package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class MapTest {
    @Test
    public void test_basic_map() {
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
}