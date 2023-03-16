package edu.duke.ece651.team16.server;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;

// import com.worklight.server.util.JSONUtils;

public class Game {
    protected List<Player> players;
    private int numPlayer;
    private List<String> colors;
    private Map defaultMap;
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
        this.defaultMap = new Map(numPlayer);
        defaultMap.createBasicMap();
        this.colors = defaultMap.getColorList();
        this.allConnections = new ArrayList<Connection>();
        this.gameState = "setNumPlayer";
        this.unitsPerPlayer = unitsPerPlayer;
        this.readyPlayer = 0;
    }

    // gameFlow()
    public void gameFlow(Socket client_socket, int numClients) throws JsonProcessingException, IOException {
        Player p = doPlacementPhase(client_socket, numClients);
        // Action!
        doActionPhase(p);
    }

    public void doActionPhase(Player p) throws JsonProcessingException, IOException{
        doAction(p);
    }

    /**
     * Create a player and add to the server
     * 
     * @param Socket client_socket: client socket
     * @param int    numClients: number of clients
     */
    public Player doPlacementPhase(Socket client_socket, int numClients) {
        Connection connection = new Connection(client_socket);
        allConnections.add(connection);
        try {
            chooseNumOfPlayers(connection, numClients);// gameState = setNumPlayer
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

            Player p = new Player(color, connection, defaultMap.getMap().get(color), this.unitsPerPlayer);

            addPlayer(p);

            assignUnits(p, connection);
            while (true) {
                synchronized (this) {
                    if (gameState.equals("gameStart")) {
                        break;
                    }
                }
            }
            HashMap<String, ArrayList<HashMap<String, String>>> to_send = formMap();
            sendMap(p, to_send);

            return p;

        } catch (IOException ioe) {
            // in something real, we would want to handle
            // this better... but for this, there isn't much we can or
            // really want to do.
        }
        return null;
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
            for (Territory t : defaultMap.getMap().get(color)) {
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
        this.defaultMap = new Map(numOfPlayers);
        defaultMap.createDukeMap();
        this.colors = defaultMap.getColorList();
    }

    // public boolean checkterritoryName()

    // /**
    // * Prompt the player to assign all units to territories
    // *
    // */
    // public void assignUnits(Player p, Connection connection) throws IOException {
    // // send all units to the player
    // // breaks when all units are assigned and the connection receives "done"
    // // System.out.println("p.unplacedUnits(): " +
    // // Integer.toString(p.unplacedUnits()));
    // while (p.unplacedUnits() > 0) {
    // // step 1: get the territory the player want to place unit on
    // String msg_territory = "You have " + Integer.toString(p.unplacedUnits())
    // + " units left. If you want to finish placement, enter done. Otherwise,
    // choose a territory to assign units to. Please enter the territory name: ";
    // connection.send(msg_territory);
    // // System.out.println("Waiting for player to choose a territory to assign
    // // units
    // // to");
    // String territoryName = connection.recv();
    // // System.out.println("TerritoryName: " + territoryName);
    // if (territoryName.equals("done")) {
    // break;
    // }
    // if (!p.getTerritoryNames().contains(territoryName)) {
    // connection.send("Invalid territory name");
    // assignUnits(p, connection);
    // return;
    // }
    // // System.out.println("Valid territory name");
    // connection.send("Valid territory name");

    // // step 2: get the unit number the player want to place
    // String msg_amount = "You have " + Integer.toString(p.unplacedUnits())
    // + " units left. If you want to finish placement, enter done. Otherwise, how
    // many units do you want to assign to "
    // + territoryName + "? Please enter a number: ";
    // connection.send(msg_amount);
    // // System.out.println("Waiting for player to choose a number of units to
    // // assign
    // // to " + territoryName);
    // String num = connection.recv();
    // try {
    // int numOfUnits = Integer.parseInt(num);
    // if (numOfUnits < 0 || numOfUnits > p.unplacedUnits()) {
    // connection.send("Invalid number of units");
    // assignUnits(p, connection);
    // return;
    // }
    // // System.out.println("Valid number of units");
    // connection.send("Valid number of units");
    // p.placeUnitsSameTerritory(territoryName, numOfUnits);
    // } catch (NumberFormatException e) {
    // connection.send("Invalid number of units");
    // assignUnits(p, connection);
    // return;
    // }

    // }
    // // System.out.println("Finished assigning units");
    // connection.send("finished placement");
    // synchronized (this) {
    // ++this.readyPlayer;
    // if (readyPlayer == numPlayer) {
    // System.out.println(allConnections.size());
    // for (Connection c : allConnections) {
    // c.send("setUnits Complete");
    // }
    // this.gameState = "gameStart";
    // }
    // }
    // }

    /**
     * Prompt the player to assign all units to territories
     * 
     */
    public void assignUnits(Player p, Connection connection) throws IOException {
        while (p.unplacedUnits() > 0) {
            promptTerritory(p, connection);
            String territoryName = connection.recv();
            if (territoryName.equals("done")) {
                break;
            }
            if (!isValidTerritory(p, territoryName, connection)) {
                return;
            }

            promptUnitNumber(p, territoryName, connection);
            String num = connection.recv();
            if (!isValidUnitNumber(p, territoryName, num, connection)) {
                return;
            }

            int numOfUnits = Integer.parseInt(num);
            p.placeUnitsSameTerritory(territoryName, numOfUnits);
        }
        notifyAllPlayers(connection);
    }

    private void notifyAllPlayers(Connection connection) throws IOException {
        connection.send("finished placement");
        synchronized (this) {
            ++this.readyPlayer;
            if (readyPlayer == numPlayer) {
                for (Connection c : allConnections) {
                    c.send("setUnits Complete");
                }
                this.gameState = "gameStart";
            }
        }
    }

    private void promptTerritory(Player p, Connection connection) throws IOException {
        String msg_territory = "You have " + Integer.toString(p.unplacedUnits())
                + " units left. If you want to finish placement, enter done. Otherwise, choose a territory to assign units to. Please enter the territory name: ";
        connection.send(msg_territory);
    }

    private boolean isValidTerritory(Player p, String territoryName, Connection connection) throws IOException {
        if (!p.getTerritoryNames().contains(territoryName)) {
            connection.send("Invalid territory name");
            assignUnits(p, connection);
            return false;
        }
        connection.send("Valid territory name");
        return true;
    }

    private void promptUnitNumber(Player p, String territoryName, Connection connection) throws IOException {
        String msg_amount = "You have " + Integer.toString(p.unplacedUnits())
                + " units left. If you want to finish placement, enter done. Otherwise, how many units do you want to assign to "
                + territoryName + "? Please enter a number: ";
        connection.send(msg_amount);
    }

    private boolean isValidUnitNumber(Player p, String territoryName, String num, Connection connection)
            throws IOException {
        try {
            int numOfUnits = Integer.parseInt(num);
            if (numOfUnits < 0 || numOfUnits > p.unplacedUnits()) {
                connection.send("Invalid number of units");
                assignUnits(p, connection);
                return false;
            }
            connection.send("Valid number of units");
            return true;
        } catch (NumberFormatException e) {
            connection.send("Invalid number of units");
            assignUnits(p, connection);
            return false;
        }
    }

    // public void assignUnits(Player p, Connection connection) throws IOException {
    // // send all units to the player
    // // breaks when all units are assigned and the connection receives "done"
    // while (p.unplacedUnits() > 0) {
    // String territoryName = getTerritoryName(connection, p);
    // if (territoryName == null) {
    // return;
    // }
    // if (territoryName.equals("Invalid Territory Name")) {
    // assignUnits(p, connection);
    // return;
    // }

    // int numOfUnits = getNumOfUnits(connection, p, territoryName);
    // if (numOfUnits == -1) {
    // return;
    // }

    // p.placeUnitsSameTerritory(territoryName, numOfUnits);
    // }

    // notifyAllPlayers(connection);
    // }

    // private String getTerritoryName(Connection connection, Player p) throws
    // IOException {
    // String msg_territory = "You have " + Integer.toString(p.unplacedUnits())
    // + " units left. If you want to finish placement, enter done. Otherwise,
    // choose a territory to assign units to. Please enter the territory name: ";
    // connection.send(msg_territory);
    // String territoryName = connection.recv();
    // if (territoryName.equals("done")) {
    // return null;
    // }
    // if (!p.getTerritoryNames().contains(territoryName)) {
    // connection.send("Invalid territory name");
    // // assignUnits(p, connection);
    // return "Invalid Territory Name";
    // }
    // connection.send("Valid territory name");
    // return territoryName;
    // }

    // private int getNumOfUnits(Connection connection, Player p, String
    // territoryName) throws IOException {
    // String msg_amount = "You have " + Integer.toString(p.unplacedUnits())
    // + " units left. If you want to finish placement, enter done. Otherwise, how
    // many units do you want to assign to "
    // + territoryName + "? Please enter a number: ";
    // connection.send(msg_amount);
    // String num = connection.recv();
    // try {
    // int numOfUnits = Integer.parseInt(num);
    // if (numOfUnits < 0 || numOfUnits > p.unplacedUnits()) {
    // connection.send("Invalid number of units");
    // assignUnits(p, connection);
    // return -1;
    // }
    // connection.send("Valid number of units");
    // return numOfUnits;
    // } catch (NumberFormatException e) {
    // connection.send("Invalid number of units");
    // assignUnits(p, connection);
    // return -1;
    // }
    // }

    // private void notifyAllPlayers(Connection connection) throws IOException {
    // connection.send("finished placement");
    // synchronized (this) {
    // ++this.readyPlayer;
    // if (readyPlayer == numPlayer) {
    // for (Connection c : allConnections) {
    // c.send("setUnits Complete");
    // }
    // this.gameState = "gameStart";
    // }
    // }
    // }

    /**
     * set number of players
     * 
     * @param int num
     */
    public void setNumPlayer(int num) {
        this.numPlayer = num;
    }

    /*
     * Form the entry string contains choices of A(ttack), M(ove), D(one)
     * 
     * @param Player p
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
        p.getConnection().send(jsonString);
    }

    /*
     * Do Action phase of the game
     * 
     * @param Player p 
     */
    public void doAction(Player p) throws JsonProcessingException, IOException {
        // choose action step. Client side checked. No reprompt
        sendEntry(p);
        String action = p.getConnection().recv().toLowerCase();
        // perform action, invalid reprompt
        boolean done = false;
        while (!done) {
            if (action.equals("m")) { // move
                doOneMove(p);
            } else if (action.equals("a")) { // attack
                p.getConnection()
                        .send("Please enter <Territory to attack from, Territory toattack, number of units>(e.g. T1, T2, 2)");
            } else { // done
                done = true;
                p.getConnection().send("Finished your turn. Please wait for other players to finish their actions.");
                return;
            }
            doAction(p);
        }
    }

    /*
     * Do one move in the move phase
     * 
     * @param Player p
     */
    public void doOneMove(Player p) throws IOException{
        p.getConnection().send("Please enter in the following format: Territory from, Territory to, number of units(e.g. T1, T2, 2)");
        String actionInput = p.getConnection().recv(); // e.g. T1, T2, 2
        String[] input = actionInput.split(", ");
        // parse actionInput
        String from = input[0];
        String to = input[1];
        int num = Integer.parseInt(input[2]);

        // get Territory
        Territory fromTerritory = checkNameReturnTerritory(from, defaultMap);
        Territory toTerritory = checkNameReturnTerritory(to, defaultMap);

        if (fromTerritory == null || toTerritory == null) {
            p.getConnection().send("Invalid Territory Name");
            doOneMove(p);
        }
        // call moveOrder
        MoveOrder moveOrder = new MoveOrder(fromTerritory, toTerritory, num, p, defaultMap);
        // if valid, send("valid")
        String trymove = moveOrder.tryMove();
        if(trymove == null){
            p.getConnection().send("Valid");
        }
        // if invalid, send("reason"), then recurse doOneMove()
        else{
            p.getConnection().send(trymove);
            doOneMove(p);
        }
    }

    /**
     * check if one placement rule is valid
     * 
     * @param territory_name the territory name
     * @return null if the placement rule is valid, otherwise return the error
     *         message
     */
    public Territory checkNameReturnTerritory(String territory_name, Map map) {
        for (String playercolor: map.getMap().keySet()){
            for (Territory territory: map.getMap().get(playercolor)){
                if (territory_name.equals(territory.getName())){
                    return territory;
                }
            }
        }
        return null;
    }

}
