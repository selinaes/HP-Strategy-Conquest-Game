package edu.duke.ece651.team16.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private List<Player> players;

    
    public Server() {
        this.players = new ArrayList<Player>();
    }


    /* 
    * Add a player to the server
    */
    public void addPlayer(Player p) {
        players.add(p);
    }

    /* Form a hashmap playerName: player's territoriesName for client to use
    * @return HashMap<String, ArrayList<String>>
    */
    public HashMap<String, ArrayList<String>> formMap() {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        for (Player p : players) {
            ArrayList<String> list = new ArrayList<String>();
            for (Territory t : p.getTerritories()) {
                list.add(t.getName());
            }
            map.put(p.getName(), list);
        }
        return map;
    }


}
