package edu.duke.ece651.team16.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapParser {
    private String player;
    private ObjectMapper objectMapper;
    private HashMap<String, ArrayList<HashMap<String, String>>> myMap;

    MapParser(String m) {
        this.player = "";
        this.objectMapper = new ObjectMapper();
        this.myMap = getMapObject(m);
    }

    public void setPlayer(String p) {
        player = p;
    }

    public void setMap(String m) {
        myMap = getMapObject(m);
    }

    public List<String> getMyInitTerritory() throws IOException {
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String color = entry.getKey();
            if (color.equals(player)) {
                ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
                for (HashMap<String, String> asset : playerAsset) {
                    ans.add(asset.get("TerritoryName"));
                }
            }
        }
        return ans;
    }

    public List<String> getTerritoryInfo(String territoryName) {
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String playername = entry.getKey();
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            for (HashMap<String, String> asset : playerAsset) {
                String territory = asset.get("TerritoryName");
                String neighbors = asset.get("Neighbors");
                String units = asset.get("Unit");
                if (territory.equals(territoryName)) {
                    ans.add(playername);
                    ans.add(units);
                    ans.add(neighbors);
                }
            }
        }
        return ans;
    }

    public List<String> getMyTerritory() {
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String playername = entry.getKey();
            if (playername.equals(player)) {
                ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
                for (HashMap<String, String> asset : playerAsset) {
                    String territory = asset.get("TerritoryName");
                    ans.add(territory);
                }
            }
        }
        return ans;
    }

    private HashMap<String, ArrayList<HashMap<String, String>>> getMapObject(String map) {
        try {
            return objectMapper.readValue(map, HashMap.class);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert json string to json object.");
            return new HashMap<>();
        }
    }

    public void updateUnitsInTerritory(int num, String territoryName) {
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String playername = entry.getKey();
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            for (HashMap<String, String> asset : playerAsset) {
                String territory = asset.get("TerritoryName");
                if (territory.equals(territoryName)) {
                    asset.put("Unit", String.valueOf(num) + " units");
                }
            }
        }
    }
}
