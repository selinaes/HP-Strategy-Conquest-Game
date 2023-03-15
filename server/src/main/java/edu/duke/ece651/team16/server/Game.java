package edu.duke.ece651.team16.server;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.Socket;
import java.net.ServerSocket;

// import com.worklight.server.util.JSONUtils;

public class Game {
    protected List<Player> players;
    private int numPlayer;
    private List<String> colors;
    private HashMap<String, List<Territory>> defaultMap;
    private List<Connection> allConnections;
    private String gameState; // setNumPlayer, setPlayerColor, setUnits, gameStart
    private int readyPlayer;
    private int unitsPerPlayer;

    /**
     * Constructor for Server class that takes in a serverSocket
     * 
     * @param ServerSocket serverSocket
     */
    public Game(int unitsPerPlayer) {
        this.numPlayer = 2;
        this.players = new ArrayList<Player>();
        Map map = new Map(numPlayer);
        this.defaultMap = map.createBasicMap();
        this.colors = map.getColorList();
        this.allConnections = new ArrayList<Connection>();
        this.gameState = "setNumPlayer";
        this.unitsPerPlayer = unitsPerPlayer;
        this.readyPlayer = 0;
    }

    // gameFlow()
    public void gameFlow(Socket client_socket, int numClients) {
        doPlacementPhase(client_socket, numClients);
        // Action!
        doActionPhase();
    }

    public void doActionPhase() {
        return;
    }

    /**
     * Create a player and add to the server
     * 
     * @param Socket client_socket: client socket
     * @param int    numClients: number of clients
     */
    public void doPlacementPhase(Socket client_socket, int numClients) {
        // System.out.println(numClients);
        // connect to new client, add serverside socket to connection obj
        Connection connection = new Connection(client_socket);
        allConnections.add(connection);
        try {
            chooseNumOfPlayers(connection, numClients);// gameState = setNumPlayer
            // System.out.println("Set numplayer to " + numPlayer);
            // ask choose color, add new player to server
            while (true) {
                synchronized (this) {
                    if (gameState.equals("setPlayerColor")) {
                        break;
                    }
                }
            }

            HashMap<String, ArrayList<HashMap<String, String>>> to_send_initial = formInitialMap();
            sendInitialMap(connection, to_send_initial);

            String color = chooseColor(connection);
            Player p = new Player(color, connection, defaultMap.get(color), this.unitsPerPlayer);

            addPlayer(p);

            assignUnits(p, connection);
            // send map to client
            while (true) {
                synchronized (this) {
                    if (gameState.equals("gameStart")) {
                        break;
                    }
                }
            }
            // System.out.println("gamestate is gameStart");
            HashMap<String, ArrayList<HashMap<String, String>>> to_send = formMap();
            sendMap(p, to_send);
            sendEntry(p);

        } catch (IOException ioe) {
            // in something real, we would want to handle
            // this better... but for this, there isn't much we can or
            // really want to do.
        }

    }

    /**
     * Add a player to the server
     * 
     * @param Player p
     */
    public void addPlayer(Player p) {
        players.add(p);
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
    public HashMap<String, ArrayList<HashMap<String, String>>> formMap() {
        HashMap<String, ArrayList<HashMap<String, String>>> map = new HashMap<String, ArrayList<HashMap<String, String>>>();
        for (Player p : players) {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            for (Territory t : p.getTerritories()) {
                HashMap<String, String> entryMap = new HashMap<String, String>();
                // entryMap.put("PlayerName", p.getName());
                entryMap.put("TerritoryName", t.getName());
                entryMap.put("Neighbors", t.getNeighborsNames());
                entryMap.put("Unit", t.getUnitsString());
                list.add(entryMap);
            }
            map.put(p.getColor(), list);
        }
        return map;
    }

    public HashMap<String, ArrayList<HashMap<String, String>>> formInitialMap() {
        HashMap<String, ArrayList<HashMap<String, String>>> map = new HashMap<String, ArrayList<HashMap<String, String>>>();

        for (String color : colors) {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            for (Territory t : defaultMap.get(color)) {
                HashMap<String, String> entryMap = new HashMap<String, String>();
                entryMap.put("TerritoryName", t.getName());
                entryMap.put("Neighbors", t.getNeighborsNames());
                list.add(entryMap);
            }
            map.put(color, list);
        }
        return map;
    }

    /**
     * Send Initial Map to client
     * 
     * @throws JsonProcessingException
     * 
     */
    public void sendInitialMap(Connection conn, HashMap<String, ArrayList<HashMap<String, String>>> to_send)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // convert the HashMap to a JSON object
        String jsonString = objectMapper.writeValueAsString(to_send);
        conn.send(jsonString);
    }

    /**
     * Send a hashmap to client
     * 
     * @param player                            to be sent to
     * @param HashMap<String,ArrayList<String>> the map to_send
     */
    public void sendMap(Player player, HashMap<String, ArrayList<HashMap<String, String>>> to_send)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // convert the HashMap to a JSON object
        String jsonString = objectMapper.writeValueAsString(to_send);
        player.getConnection().send(jsonString);
    }

    // /*
    // * Close all connections to client
    // */
    // public void close() throws IOException {
    // // close all connection by looping players
    // for (Player p : players) {
    // p.getConnection().close();
    // }

    // }

    /**
     * Prompt the player to choose a color
     * 
     * @param Connection connection
     * @return string color chosen by player
     **/
    public String chooseColor(Connection connection) throws IOException {
        String colorList = "";
        for (String color : colors) {
            colorList += color + " ";
        }
        connection.send(
                "Please enter a color you want to choose. Current available colors are: " + colorList);
        String chosencolor = connection.recv();
        chosencolor = chosencolor.toLowerCase();
        int colorindex = colors.indexOf(chosencolor);
        if (colorindex == -1) {
            connection.send("Invalid color");
            return chooseColor(connection);
        } else {
            connection.send("Valid");
            colors.remove(colorindex);
            return chosencolor;
        }
    }

    /**
     * prompt the player to choose number of players
     * 
     * @param Connection connection
     */
    public void chooseNumOfPlayers(Connection connection, int numClients) throws IOException {
        synchronized (this) {
            this.gameState = "setNumPlayer";
        }
        // synchronized (this) {
        // this.gameState = "setNumPlayer";
        // }
        if (numClients == 1) {
            connection.send(
                    "You are the first player! Please set the number of players in this game(Valid player number: 2-4): ");
            String num = connection.recv();
            try {
                int numOfPlayers = Integer.parseInt(num);
                if (numOfPlayers < 2 || numOfPlayers > 4) {
                    connection.send("Invalid number of players");
                    chooseNumOfPlayers(connection, numClients);
                    return;
                }
                connection.send("Valid");
                this.numPlayer = numOfPlayers;
            } catch (NumberFormatException e) {
                connection.send("Invalid number of players");
                chooseNumOfPlayers(connection, numClients);
                return;
            }

            for (Connection c : allConnections) {
                c.send("setNumPlayer Complete");
            }
            synchronized (this) {
                this.gameState = "setPlayerColor"; // now out of setNumPlayer stage
                initializeMap(this.numPlayer);
            }
        } else {
            connection.send("Not the first player. Please wait for the first player to set player number.");
        }
    }

    /**
     * Count the number of players
     * 
     * @return int number of players
     */
    public int getNumPlayer() {
        return this.numPlayer;
    }

    /**
     * create new map and color list
     * 
     * @param int numOfPlayers
     */
    public void initializeMap(int numOfPlayers) {
        Map map = new Map(numOfPlayers);
        this.defaultMap = map.createDukeMap();
        this.colors = map.getColorList();
    }

    /**
     * Prompt the player to assign all units to territories
     * 
     */
    public void assignUnits(Player p, Connection connection) throws IOException {
        // send all units to the player
        // breaks when all units are assigned and the connection receives "done"
        // System.out.println("p.unplacedUnits(): " +
        // Integer.toString(p.unplacedUnits()));
        while (p.unplacedUnits() > 0) {
            // step 1: get the territory the player want to place unit on
            String msg_territory = "You have " + Integer.toString(p.unplacedUnits())
                    + " units left. If you want to finish placement, enter done. Otherwise, choose a territory to assign units to. Please enter the territory name: ";
            connection.send(msg_territory);
            // System.out.println("Waiting for player to choose a territory to assign units
            // to");
            String territoryName = connection.recv();
            // System.out.println("TerritoryName: " + territoryName);
            if (territoryName.equals("done")) {
                break;
            }
            if (!p.getTerritoryNames().contains(territoryName)) {
                connection.send("Invalid territory name");
                assignUnits(p, connection);
                return;
            }
            // System.out.println("Valid territory name");
            connection.send("Valid territory name");

            // step 2: get the unit number the player want to place
            String msg_amount = "You have " + Integer.toString(p.unplacedUnits())
                    + " units left. If you want to finish placement, enter done. Otherwise, how many units do you want to assign to "
                    + territoryName + "? Please enter a number: ";
            connection.send(msg_amount);
            // System.out.println("Waiting for player to choose a number of units to assign
            // to " + territoryName);
            String num = connection.recv();
            try {
                int numOfUnits = Integer.parseInt(num);
                if (numOfUnits < 0 || numOfUnits > p.unplacedUnits()) {
                    connection.send("Invalid number of units");
                    assignUnits(p, connection);
                    return;
                }
                // System.out.println("Valid number of units");
                connection.send("Valid number of units");
                p.placeUnitsSameTerritory(territoryName, numOfUnits);
            } catch (NumberFormatException e) {
                connection.send("Invalid number of units");
                assignUnits(p, connection);
                return;
            }

        }
        // System.out.println("Finished assigning units");
        connection.send("finished placement");
        synchronized (this) {
            ++this.readyPlayer;
            if (readyPlayer == numPlayer) {
                System.out.println(allConnections.size());
                for (Connection c : allConnections) {
                    c.send("setUnits Complete");
                }
                this.gameState = "gameStart";
            }
        }
    }

    /**
     * /* set number of players
     * /* @param int num
     */
    public void setNumPlayer(int num) {
        this.numPlayer = num;
    }

    /*
     * Form the entry string contains choices of A(ttack), M(ove), D(one)
     * 
     */
    public HashMap<String, String> formEntry(Player p) {
        StringBuilder entry = new StringBuilder("");
        String header = "You are the " + p.getColor() + " player, what would you like to do?\n";
        entry.append(header);
        String body = "M(ove)\n" + "A(ttack)\n" + "D(one)\n";
        entry.append(body);
        HashMap<String, String> entryMap = new HashMap<String, String>();
        entryMap.put("Entry", entry.toString());
        return entryMap;
    }

    /*
     * Send the entry string to the player
     * 
     * @param Player p
     */
    public void sendEntry(Player p) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> to_send_entry = formEntry(p);

        // convert the HashMap to a JSON object
        String jsonString = objectMapper.writeValueAsString(to_send_entry);
        // System.out.println(jsonString);
        // conn.send(jsonString);

        p.getConnection().send(jsonString);
    }

    // public void doAction(Connection connection){
    // String action = connection.recv();
    // if(action != "M" || action != "A" || action !=)
    // }

}
