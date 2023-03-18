package edu.duke.ece651.team16.client;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Views {
    PrintStream out;
    private boolean watch;
    private ObjectMapper objectMapper;

    /**
     * Constructor for Views
     */
    public Views(PrintStream out) {
        this.watch = false;
        this.objectMapper = new ObjectMapper();
        this.out = out;
    }

    public PrintStream getInitialMap(String jsonString) throws IOException {
        displayInitialMap(jsonString);
        return out;
    }

    public PrintStream getMap(String jsonString) throws IOException {
        displayMap(jsonString);
        return out;
    }

    public PrintStream getLog(String jsonString) throws IOException {
        displayLog(jsonString);
        return out;
    }

    /**
     * Display the initial map to the player
     * 
     * @throws IOException
     */
    private void displayInitialMap(String jsonString) throws IOException {
        // convert jsonString to jsonobject
        HashMap<String, ArrayList<HashMap<String, String>>> input_map;
        try {
            input_map = objectMapper.readValue(jsonString, HashMap.class);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert json string to json object.");
            return;
        }
        // Player name is the input_map key
        // ArrayList<HashMap<String, String>> is the value of the key
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : input_map.entrySet()) {
            String color = entry.getKey();
            out.println();
            String title = color + " player will have these territories: ";
            out.println(title);
            String seperation = String.join("", Collections.nCopies(title.length(), "-"));
            out.println(seperation);
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            for (HashMap<String, String> asset : playerAsset) {
                String territory = asset.get("TerritoryName");
                String neighbors = asset.get("Neighbors");
                out.println(territory + " " + neighbors);
            }
            out.println();
        }
    }

    /**
     * Display actions for player
     * 
     * @throws IOException
     * @return String of actions
     */
    public String displayEntry(String to_display) throws IOException {
        // convert jsonString to jsonobject
        HashMap<String, String> input_entry;
        try {
            input_entry = objectMapper.readValue(to_display, HashMap.class);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert json string to json object.");
            return "";
        }
        return input_entry.get("Entry");
    }

    /**
     * Display map
     * 
     * @throws IOException
     */
    private void displayMap(String jsonString) throws IOException {
        // convert jsonString to jsonobject
        HashMap<String, ArrayList<HashMap<String, String>>> input_map;
        try {
            input_map = objectMapper.readValue(jsonString, HashMap.class);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert json string to json object.");
            return;
        }
        // Player name is the input_map key
        // ArrayList<HashMap<String, String>> is the value of the key
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : input_map.entrySet()) {
            String playername = entry.getKey();
            String title = playername + " player: ";
            out.println(title);
            String seperation = String.join("", Collections.nCopies(title.length(), "-"));
            out.println(seperation);
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            for (HashMap<String, String> asset : playerAsset) {
                String territory = asset.get("TerritoryName");
                String neighbors = asset.get("Neighbors");
                String units = asset.get("Unit");
                out.println(units + " in " + territory + " " + neighbors);
            }
        }
    }

    /**
     * Display map
     *
     * @throws IOException
     */
    private void displayLog(String jsonString) throws IOException {
        // convert jsonString to jsonobject
        HashMap<String, String> input_Log;
        try {
            input_Log = objectMapper.readValue(jsonString, HashMap.class);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert json string to json object.");
            return;
        }
        // Player name is the input_map key
        // ArrayList<HashMap<String, String>> is the value of the key
        for (Map.Entry<String, String> entry : input_Log.entrySet()) {
            String territoryName = entry.getKey();
            String title = "War Log in " + territoryName + ": ";
            out.println(title);
            String seperation = String.join("", Collections.nCopies(title.length(),
                    "-"));
            out.println(seperation);
            out.println(entry.getValue());
        }
    }

    /**
     * Player watch other players' turn
     */
    public void playerWatchTurn() {
        out.println("Watching other players' turn. You already lost.");
    }

}
