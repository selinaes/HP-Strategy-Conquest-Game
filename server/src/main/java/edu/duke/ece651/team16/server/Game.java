package edu.duke.ece651.team16.server;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;

public class Game {
    protected List<Player> players;
    private int numPlayer;
    private List<String> colors;
    private Map defaultMap;
    private List<Connection> allConnections;
    private String gameState; // setNumPlayer, setPlayerColor, setUnits, placementEnd, worldWar, warEnd
    private int readyPlayer;
    private int unitsPerPlayer;
    private int playerLowBound;
    private int playerHighBound;
    private int gameRound;

    /**
     * Constructor for Server class that takes in a serverSocket
     * 
     * @param ServerSocket serverSocket
     */
    public Game(int unitsPerPlayer) {
        this.numPlayer = 4;
        this.players = new ArrayList<Player>();
        this.defaultMap = new Map(numPlayer);
        defaultMap.createBasicMap();
        this.colors = defaultMap.getColorList();
        this.allConnections = new ArrayList<Connection>();
        this.gameState = "setNumPlayer";
        this.unitsPerPlayer = unitsPerPlayer;
        this.readyPlayer = 0;
        this.playerLowBound = 2;
        this.playerHighBound = 4;
        this.gameRound = 0;
    }

    // /**
    // * Used to debug players inside the game
    // */
    // public void printPlayer() {
    // System.out.println("===Update Player List====");
    // for (Player p : players) {
    // System.out.println("Player is " + p.getColor());
    // }
    // System.out.println("========End=======\n");
    // }

    /**
     * Multithreaded main flow of the game, with different phases
     * 
     * @param ServerSocket serverSocket: the server socket
     * @param int          numClients: the number of clients
     */
    public void gameFlow(Socket client_socket, int numClients) {
        Player p = doPlacementPhase(client_socket, numClients);
        synchronized (this) {
            this.readyPlayer = 0;
        }
        while (findWinner() == null) {
            if (!players.contains(p)) {
                return; // player exit
            }
            p.getConnection().send("Game continues");
            doActionPhase(p);

        }
        Player winner = findWinner();
        // notify this player of winner!
        p.getConnection().send(winner.getColor());
    }

    /**
     * Check if player wants to watch after lose
     * 
     * @return Player: the player who wants to watch
     */
    public void issueOrderOrWatch(Player p) {
        if (p.getisWatch()) {
            // send done
            p.getConnection().send("watching");
            notifyAllPlayers(p.getConnection(), "worldWar");
        } else if (p.checkLose()) {
            p.getConnection().send("Choose watch");
            if (ifChooseWatch(p.getConnection()).equals("e")) {
                synchronized (this) {
                    players.remove(p);
                    --numPlayer;
                }
                return;
            } else {
                p.setWatch();
                notifyAllPlayers(p.getConnection(), "worldWar");
            }
        } else {
            p.getConnection().send("do nothing");
            doAction(p);
        }
    }

    // synchronize check state
    public void checkState(String state) {
        while (true) {
            synchronized (this) {
                if (gameState.equals(state)) {
                    break;
                }
            }
        }
    }

    /**
     * 
     * the action phase of the game
     *
     * @param Player p: the player
     */
    public void doActionPhase(Player p) {
        HashMap<String, String> to_send_log = new HashMap<>();
        issueOrderOrWatch(p);
        while (true) {
            synchronized (this) {
                if (gameState.equals("worldWar")) {
                    ++readyPlayer; // so cannot use checkState()
                    break;
                }
            }
        }

        // if all players are ready, then execute world war (the last player who reached
        // here)
        synchronized (this) {
            if (readyPlayer == numPlayer) {
                to_send_log = worldwar();
                for (Player player : players) {
                    sendLog(player, to_send_log);
                }
                this.gameState = "warEnd";
                this.gameRound++;
                this.readyPlayer = 0;
            }
        }

        checkState("warEnd");
        HashMap<String, ArrayList<HashMap<String, String>>> to_send1 = formMap();
        sendMap(p, to_send1);
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
        chooseNumOfPlayers(connection, numClients);// gameState = setNumPlayer

        checkState("setPlayerColor");

        HashMap<String, ArrayList<HashMap<String, String>>> to_send_initial = formInitialMap();
        sendInitialMap(connection, to_send_initial);

        String color = chooseColor(connection);
        Player p = new Player(color, connection, defaultMap.getMap().get(color), this.unitsPerPlayer);
        addPlayer(p);

        assignUnits(p, connection);

        checkState("placementEnd");

        HashMap<String, ArrayList<HashMap<String, String>>> to_send = formMap();
        sendMap(p, to_send);
        return p;
    }

    /**
     * Add a player to the server
     * 
     * @param Player p
     */
    public void addPlayer(Player p) {
        players.add(p);
        // printPlayer();
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

    /**
     * form the initial map to send to client
     * 
     * @return HashMap<String, ArrayList<HashMap<String, String>>> the map
     */
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
     * @param Connection                        connection
     * @param HashMap<String,ArrayList<String>> the map to_send
     */
    public void sendInitialMap(Connection conn, HashMap<String, ArrayList<HashMap<String, String>>> to_send) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
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
            ObjectMapper objectMapper = new ObjectMapper();
            // convert the HashMap to a JSON object
            String jsonString = objectMapper.writeValueAsString(to_send);
            player.getConnection().send(jsonString);
        } catch (JsonProcessingException e) {
            System.err.println("Error in sending map");
        }

    }

    /**
     * Prompt the player to choose a color
     * 
     * @param Connection connection
     * @return string color chosen by player
     **/
    public String chooseColor(Connection connection) {
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
     * Prompt the lose player to choose watch or exit
     * 
     * @param Connection connection
     * @return string chosen by player
     **/
    public String ifChooseWatch(Connection connection) {
        connection.send("If you want to watch game, Please enter: w; If you want to exit, Please enter: e");
        String chooseWatch = connection.recv();
        chooseWatch = chooseWatch.toLowerCase();
        System.out.println(chooseWatch);
        if (!chooseWatch.equals("w") && !chooseWatch.equals("e")) {
            connection.send("Invalid choice");
            return ifChooseWatch(connection);
        } else {
            connection.send("Valid");
            return chooseWatch;
        }
    }

    /**
     * prompt the player to choose number of players
     * 
     * @param Connection connection
     * @param int        numClients
     */
    public void chooseNumOfPlayers(Connection connection, int numClients) {
        if (numClients == 1) {
            connection.send(
                    "You are the first player! Please set the number of players in this game(Valid player number: 2-4): ");
            String num = connection.recv();
            try {
                int numOfPlayers = Integer.parseInt(num);
                if (numOfPlayers < this.playerLowBound || numOfPlayers > this.playerHighBound) {
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
                c.send("stage Complete");
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
        // defaultMap.createDukeMap();
        defaultMap.createTestMap();
        this.colors = defaultMap.getColorList();
    }

    /**
     * Prompt the player to assign all units to territories
     * 
     * @param Player     p
     * @param Connection connection
     */
    public void assignUnits(Player p, Connection connection) {
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
            if (num.equals("done")) {
                break;
            }
            if (!isValidUnitNumber(p, territoryName, num, connection)) {
                return;
            }
            int numOfUnits = Integer.parseInt(num);
            p.placeUnitsSameTerritory(territoryName, numOfUnits);
        }
        notifyAllPlayers(connection, "placementEnd");
    }

    /**
     * Notify all players that the player has finished assigning units
     * 
     * @param Connection connection
     * @param String     newStage
     */
    public void notifyAllPlayers(Connection connection, String newStage) {
        connection.send("finished stage");
        synchronized (this) {
            ++this.readyPlayer;
            if (readyPlayer == numPlayer) {
                // System.out.println("All players ready, readyPlayer: " + readyPlayer + "
                // Entering: "
                // + newStage);
                for (Connection c : allConnections) {
                    c.send("stage Complete");
                }
                this.gameState = newStage;
                this.readyPlayer = 0;
            }
        }
    }

    /**
     * Prompt the player to choose a territory
     * 
     * @param Player     p
     * @param Connection connection
     */
    private void promptTerritory(Player p, Connection connection) {
        String msg_territory = "You have " + Integer.toString(p.unplacedUnits())
                + " units left. If you want to finish placement, enter done. Otherwise, choose a territory to assign units to. Please enter the territory name: ";
        connection.send(msg_territory);
    }

    /**
     * Check if the territory name is valid
     * 
     * @param Player     p
     * @param String     territoryName
     * @param Connection connection
     * @return boolean
     */
    private boolean isValidTerritory(Player p, String territoryName, Connection connection) {
        if (!p.getTerritoryNames().contains(territoryName)) {
            connection.send("Invalid territory name");
            assignUnits(p, connection);
            return false;
        }
        connection.send("Valid territory name");
        return true;
    }

    /**
     * Prompt the player to choose a number of units
     * 
     * @param Player     p
     * @param String     territoryName
     * @param Connection connection
     */
    private void promptUnitNumber(Player p, String territoryName, Connection connection) {
        String msg_amount = "You have " + Integer.toString(p.unplacedUnits())
                + " units left. If you want to finish placement, enter done. Otherwise, how many units do you want to assign to "
                + territoryName + "? Please enter a number: ";
        connection.send(msg_amount);
    }

    /**
     * Check if the number of units is valid
     * 
     * @param Player     p
     * @param String     territoryName
     * @param String     num
     * @param Connection connection
     * @return boolean
     */
    private boolean isValidUnitNumber(Player p, String territoryName, String num, Connection connection) {
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

    /**
     * set number of players
     * 
     * @param int num
     */
    public void setNumPlayer(int num) {
        this.numPlayer = num;
    }

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
        String body = "M(ove)\n" + "A(ttack)\n" + "D(one)\n";
        entry.append(body);
        HashMap<String, String> entryMap = new HashMap<String, String>();
        entryMap.put("Entry", entry.toString());
        return entryMap;
    }

    /**
     * Form the exit entry containing choices of W(atch), E(xit)
     * 
     * @param Player p
     * @return HashMap<String, String> entryMap
     */
    public HashMap<String, String> formExitWatch(Player p) {
        StringBuilder entry = new StringBuilder("");
        String header = "You are the " + p.getColor() + " player, you have lost the game, what would you like to do?\n";
        entry.append(header);
        String body = "W(atch)\n" + "E(xit )\n";
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
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, String> to_send_entry = formEntry(p);
            // convert the HashMap to a JSON object
            String jsonString = objectMapper.writeValueAsString(to_send_entry);
            p.getConnection().send(jsonString);
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
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(to_send);
            p.getConnection().send(jsonString);
        } catch (JsonProcessingException e) {
            System.err.println("Error in sending entry");
        }

    }

    /**
     * Do Action phase of the game
     * 
     * @param Player p
     * @return boolean
     */
    public boolean doAction(Player p) {
        // choose action step. Client side checked. No reprompt
        sendEntry(p);
        String action = p.getConnection().recv().toLowerCase();
        // perform action, invalid reprompt
        boolean done = false;
        while (!done) {
            if (action.equals("m") || action.equals("a")) { // move or attack
                doOneAction(p, action);
            } else { // done
                done = true;
                notifyAllPlayers(p.getConnection(), "worldWar");
                return done;
            }
            done = doAction(p);
        }
        return done;
    }

    /**
     * ask the player to enter info for action: Territory from, Territory to, number
     * of units(e.g. T1, T2, 2)
     * 
     * @param Player p
     * @param String actionname
     * @return Order
     */
    public Order makeActionOrder(Player p, String actionName) {
        p.getConnection().send(
                "Please enter in the following format: Territory from, Territory to, number of units(e.g. T1, T2, 2)");
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
            return makeActionOrder(p, actionName);
        }
        if (actionName.equals("m")) {
            Order order = new MoveOrder(fromTerritory, toTerritory, num, p, defaultMap);
            return order;
        }
        return (new AttackOrder(fromTerritory, toTerritory, num, p, defaultMap));

    }

    /**
     * Do one move in the move phase
     * 
     * @param Player p
     * @param String actionName
     */
    public void doOneAction(Player p, String actionName) {
        Order order = makeActionOrder(p, actionName);
        String tryAction = order.tryAction();
        if (tryAction == null) { // valid
            p.getConnection().send("Valid");
        }
        // if invalid, send("reason"), then recurse doOneMove()
        else {
            p.getConnection().send(tryAction);
            doOneAction(p, actionName);
            return;
        }
    }

    /**
     * check if one placement rule is valid
     * 
     * @param territory_name the territory name
     * @param map            the map
     * @return null if the placement rule is valid, otherwise return the error
     *         message
     */
    public Territory checkNameReturnTerritory(String territory_name, Map map) {
        for (String playercolor : map.getMap().keySet()) {
            for (Territory territory : map.getMap().get(playercolor)) {
                if (territory_name.equals(territory.getName())) {
                    return territory;
                }
            }
        }
        return null;
    }

    /**
     * After one turn of moving and attacking, resolve battle for each territory
     *
     * @return HashMap<String, String> worldLog the log of the world war
     */
    public HashMap<String, String> worldwar() {
        HashMap<String, String> worldLog = new HashMap<>();
        for (Player p : players) {
            List<Territory> territoriesCopy = List.copyOf(p.getTerritories()); // immutable copy. if don't copy,
                                                                               // ConcurrentModificationException
            for (Territory territory : territoriesCopy) {
                if (territory.existsBattle()) {
                    // System.out.println("Player " + p.getColor() + " turn and " +
                    // territory.getName() + " in battle");
                    String battleLog = territory.doBattle();
                    worldLog.put(territory.getName(), battleLog);
                }
            }
        }
        generateUnit();
        return worldLog;
    }

    /**
     * Generate new unit for each player
     */
    public void generateUnit() {
        for (Player p : players) {
            p.generateNewUnit();
        }
    }

    /**
     * Check if the game is over, and find the winner
     * 
     * @return winner if the game is over, otherwise return null
     */
    public Player findWinner() {
        List<Player> playersCopy = new ArrayList<>(players); // mutable copy
        for (Player p : players) {
            if (p.checkLose()) {
                playersCopy.remove(p);
            }
        }
        if (playersCopy.size() == 1) {
            return playersCopy.get(0);
        }
        return null;
    }
}
