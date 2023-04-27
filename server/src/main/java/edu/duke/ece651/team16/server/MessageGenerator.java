package edu.duke.ece651.team16.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MessageGenerator {
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Form the entry string contains choices of A(ttack), M(ove), D(one)
     * 
     * @param Player p
     * @return HashMap<String, String>
     */
    public HashMap<String, String> formEntry(Player p) {
        StringBuilder entry = new StringBuilder("");
        String header = "You are the " + p.getColor() + " player, what would you like to do?\n";
        entry.append(header);
        String body = "M(ove)\n" + "A(ttack)\n" + "R(esearch)\n" + "U(pgrade)\n" + "D(one)\n";
        entry.append(body);
        HashMap<String, String> entryMap = new HashMap<String, String>();
        entryMap.put("Entry", entry.toString());
        return entryMap;
    }

    /**
     * Send the entry string to the player
     * 
     * @param Player p
     */
    public void sendEntry(Player p) {
        try {
            HashMap<String, String> to_send_entry = formEntry(p);
            // convert the HashMap to a JSON object
            String jsonString = objectMapper.writeValueAsString(to_send_entry);
            p.getConn().send(jsonString);
        } catch (JsonProcessingException e) {
            System.err.println("Error in sending entry");
        }

    }

    /**
     * Send the entry string to the player
     *
     * @param Player p
     */
    public void sendLog(Player p, HashMap<String, String> to_send) {
        try {
            String jsonString = objectMapper.writeValueAsString(to_send);
            p.getConn().send(jsonString);
        } catch (JsonProcessingException e) {
            System.err.println("Error in sending entry");
        }

    }

    /**
     * Send Initial Map to client
     * 
     * @param Conn                              conn
     * @param HashMap<String,ArrayList<String>> the map to_send
     */
    public void sendInitialMap(Conn conn, HashMap<String, ArrayList<HashMap<String, String>>> to_send) {
        try {
            // convert the HashMap to a JSON object
            String jsonString = objectMapper.writeValueAsString(to_send);
            conn.send(jsonString);
        } catch (JsonProcessingException e) {
            System.err.println("Error in sending map");
        }
    }

    /**
     * Send a hashmap to client
     * 
     * @param player                            to be sent to
     * @param HashMap<String,ArrayList<String>> the map to_send
     */
    public void sendMap(Player player, HashMap<String, ArrayList<HashMap<String, String>>> to_send) {
        try {
            // convert the HashMap to a JSON object
            String jsonString = objectMapper.writeValueAsString(to_send);
            player.getConn().send(jsonString);
        } catch (JsonProcessingException e) {
            System.err.println("Error in sending map");
        }

    }

    /**
     * form the initial map to send to client
     * 
     * @return HashMap<String, ArrayList<HashMap<String, String>>> the map
     */
    public HashMap<String, ArrayList<HashMap<String, String>>> formInitialMap(GameMap currentMap, List<String> colors) {
        HashMap<String, ArrayList<HashMap<String, String>>> map = new HashMap<String, ArrayList<HashMap<String, String>>>();
        for (String color : colors) {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            for (Territory t : currentMap.getMap().get(color)) {
                HashMap<String, String> entryMap = new HashMap<String, String>();
                entryMap.put("TerritoryName", t.getName());
                entryMap.put("Neighbors", t.getNeighborsNames());
                entryMap.put("Rate", t.territoryInfo());
                list.add(entryMap);
            }
            map.put(color, list);
        }
        return map;
    }

    /**
     * Form a hashmap playerName: player's territoriesName and corresponding
     * neighbors
     * HashMap<String, ArrayList<HashMap<String, String>>>
     * [playerName: [{{"TerritoryName1": string},{"Neighbors": [, , , ]}, {"Unit":
     * int}}, {TerritoryName2: {"Neighbors": [, , , ]}, {"Unit": int}}]]
     * 
     * @return HashMap<String, ArrayList<String>> the map
     */
    public HashMap<String, ArrayList<HashMap<String, String>>> formMap(List<Player> players) {
        HashMap<String, ArrayList<HashMap<String, String>>> map = new HashMap<String, ArrayList<HashMap<String, String>>>();
        for (Player p : players) {
            String allyinfo = "";
            if (p.getAlly() != null) {
                allyinfo += p.getAlly().getColor();
            }
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            for (Territory t : p.getTerritories()) {
                HashMap<String, String> entryMap = new HashMap<String, String>();
                // entryMap.put("PlayerName", p.getName());
                entryMap.put("TerritoryName", t.getName());
                entryMap.put("Neighbors", t.getNeighborsNames());
                entryMap.put("Unit", t.getUnitsString());
                entryMap.put("Rate", t.territoryInfo());
                entryMap.put("Resource", p.displayResourceLevel());
                entryMap.put("Ally", allyinfo);
                list.add(entryMap);
            }
            map.put(p.getColor(), list);
        }
        return map;
    }
}
