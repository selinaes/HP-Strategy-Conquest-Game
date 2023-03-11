package edu.duke.ece651.team16.server;
import edu.duke.ece651.team16.shared.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private List<Player> players;
    private Connection connection;

    
    public Server(int port) {
        this.players = new ArrayList<Player>();
        this.connection = new Connection(null, port, true);
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
    
    /*
    * Send a hashmap to client
    * @param HashMap<String, ArrayList<String>> to_send
    */
    public void sendMap(HashMap<String, ArrayList<String>> to_send) {
        // convert hashmap to string
        String map = "";
        for (String key : to_send.keySet()) {
            map += key + ":";
            for (String value : to_send.get(key)) {
                map += value + ",";
            }
            map += ";";
        }
        connection.send(map);
    }

    /* 
    * Close the connection
    */
    public void close() throws IOException {
        connection.close();
    }

}
