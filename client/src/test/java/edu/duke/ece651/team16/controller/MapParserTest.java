package edu.duke.ece651.team16.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.IOException;

public class MapParserTest {
    private String jsonString = "{\"name\":[{\"TerritoryName\":\"a\",\"Neighbors\":\"(next to: b)\",\"Unit\":\"1\"}]}";
    private String map = "{\"red\":[{\"TerritoryName\":\"baldwin\",\"Rate\":\"Food Rate:5, Tech Rate:5\",\"Neighbors\":\"(next to:brodie:5,smith:5,dukeChapel:5)\",\"Resource\":\"(Food:60 Tech:60 Tech Level:1)\",\"Unit\":\"0,0,0,0,0,0,0,\"}]}";

    @Test
    public void test_MapParserInit() {
        MapParser m = new MapParser("e");
        m.setMap(jsonString);
        m.setPlayer("red");
    }

    @Test
    public void test_getTerritory() throws IOException {
        MapParser m = new MapParser("e");
        m.setMap(map);
        m.setPlayer("red");
        m.getMyInitTerritory();
        m.getTerritoryInfo("baldwin");
        m.getMyTerritory();
        m.updateUnitsInTerritory("0,1,0,0,0,0", "baldwin");
        m.setPlayer("red2");
        m.getMyInitTerritory();
        m.getTerritoryInfo("baldwins");
        m.getMyTerritory();
        m.updateUnitsInTerritory("0,1,0,0,0,0", "baldwins");
    }

}