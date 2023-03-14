package edu.duke.ece651.team16.server;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.ThreadPoolExecutor;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// import com.worklight.server.util.JSONUtils;

public class Server {
    private ServerSocket listenSocket;
    ThreadPoolExecutor threadPool;
    private Game game;
    private int numClients;
    private int unitsPerPlayer;

    /**
     * Constructor for Server class that takes in a serverSocket
     * 
     * @param ServerSocket serverSocket
     */
    public Server(ServerSocket serverSocket, int unitsPerPlayer) {

        this.listenSocket = serverSocket;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(2, 16, 5, TimeUnit.SECONDS, workQueue);

        this.game = new Game();
        this.numClients = 0;
        this.unitsPerPlayer = unitsPerPlayer;
    }

    /**
     * This is a helper method to accept a socket from the ServerSocket
     * or return null if it timesout.
     *
     * @return the socket accepted from the ServerSocket
     */
    public Socket acceptOrNull() {
        try {
            return listenSocket.accept();
        } catch (IOException ioe) {
            // In real code, we would want to be more discriminating here.
            // Was this a timeout, or some other problem?
            return null;
        }
    }

    /**
     * run server to receive client's message
     */
    public void run() throws IOException, JsonProcessingException {
        while (!Thread.currentThread().isInterrupted()) {
            final Socket client_socket = acceptOrNull();
            if (client_socket == null) {
                continue;
            }
            // numClients++;
            synchronized (this) {
                numClients++;
            }
            // This will enqueue the request until
            // a thread in the pool is available, then
            // execute that request on the available thread.
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            game.createPlayer(client_socket, numClients);
                            HashMap<String, ArrayList<HashMap<String, String>>> to_send = formInitialMap();
                            try {
                                sendInitialMap(connection, to_send);
                            } catch (JsonProcessingException e) {
                                System.out.println("JsonProcessingException");
                            }
                            // before chooseColor(connection)

                            // after addPlayer(p)
                            System.out.println("Start placement units");
                            assignUnits(p, connection);

                            // // send map to client
                            // HashMap<String, ArrayList<HashMap<String, String>>> to_send1 = formMap();
                            // try {
                            // sendMap(p, to_send1);
                            // } catch (JsonProcessingException e) {
                            // System.out.println("JsonProcessingException");
                            // }

                        } finally {
                            client_socket.close();
                        }
                    } catch (IOException ioe) {
                        // in something real, we would want to handle
                        // this better... but for this, there isn't much we can or
                        // really want to do.
                    }
                }
            });
        }

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
            map.put(p.getName(), list);
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
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // convert the HashMap to a JSON object
            String jsonString = objectMapper.writeValueAsString(to_send);
            conn.send(jsonString);

        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
    }

    /**
     * Send a hashmap to client
     * 
     * @param player                            to be sent to
     * @param HashMap<String,ArrayList<String>> the map to_send
     */
    public void sendMap(Player player, HashMap<String, ArrayList<HashMap<String, String>>> to_send)
            throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // convert the HashMap to a JSON object
            String jsonString = objectMapper.writeValueAsString(to_send);
            player.getConnection().send(jsonString);

        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
    }

    /*
     * Close all connections to client
     */
    public void close() throws IOException {
        // close all connection by looping players
        for (Player p : players) {
            p.getConnection().close();
        }

    }

    /**
     * Prompt the player to choose a color
     * 
     * @param Connection connection
     * @return string color chosen by player
     **/
    public String chooseColor(Connection connection) throws IOException {
        // connection.send("Which color do you want to choose? Please enter a number.\n
        // Current available colors are: ");
        String colorList = "";
        for (int i = 0; i < colors.size(); i++) {
            colorList += i + ". " + colors.get(i) + " ";
        }
        connection.send(
                "Which color do you want to choose? Please enter a number. Current available colors are: " + colorList);
        String index = connection.recv();
        // System.out.println("Player's color choice is: " + index);
        int colorindex = Integer.parseInt(index);
        // System.out.println(colorindex);
        if (colorindex < 0 || colorindex >= colors.size()) {
            connection.send("Invalid color index");
            return chooseColor(connection);
        } else {
            connection.send("Valid");
            String chosenColor = colors.get(colorindex);
            colors.remove(colorindex);
            return chosenColor;
        }
    }

    // /**
    // * Prompt the player to assign units to territories
    // */
    // public String assignUnitsOneTime(Player p, Connection connection) throws
    // IOException {
    // String msg = "You have " + Integer.toString(p.unplacedUnits()) + " units
    // left. Please assign units to your territories. Please enter a number: ";
    // connection.send(msg);
    // String num = connection.recv();
    // int numOfUnits = Integer.parseInt(num);
    // if (numOfUnits < 0 || numOfUnits > p.unplacedUnits()) {
    // connection.send("Invalid number of units");
    // return assignUnitsOneTime(p, connection);
    // }
    // connection.send("Valid");
    // return num;
    // }

    /**
     * Prompt the player to assign all units to territories
     * 
     */
    public void assignUnits(Player p, Connection connection) throws IOException {
        // send all units to the player
        // breaks when all units are assigned and the connection receives "done"
        System.out.println("p.unplacedUnits(): " + Integer.toString(p.unplacedUnits()));
        while (p.unplacedUnits() > 0) {
            // step 1: get the territory the player want to place unit on
            String msg_territory = "You have " + Integer.toString(p.unplacedUnits())
                    + " units left. If you want to finish placement, enter done. Otherwise, choose a territory to assign units to. Please enter the territory name: ";
            connection.send(msg_territory);
            System.out.println("Waiting for player to choose a territory to assign units to");
            String territoryName = connection.recv();
            System.out.println("TerritoryName: " + territoryName);
            if (territoryName.equals("done")) {
                break;
            }
            if (!p.getTerritoryNames().contains(territoryName)) {
                connection.send("Invalid territory name");
                assignUnits(p, connection);
                return;
            }
            System.out.println("Valid territory name");
            connection.send("Valid territory name");

            // step 2: get the unit number the player want to place
            String msg_amount = "You have " + Integer.toString(p.unplacedUnits())
                    + " units left. If you want to finish placement, enter done. Otherwise, how many units do you want to assign to "
                    + territoryName + "? Please enter a number: ";
            connection.send(msg_amount);
            System.out.println("Waiting for player to choose a number of units to assign to " + territoryName);
            String num = connection.recv();
            int numOfUnits = Integer.parseInt(num);
            if (numOfUnits < 0 || numOfUnits > p.unplacedUnits()) {
                connection.send("Invalid number of units");
                assignUnits(p, connection);
                return;
            }
            System.out.println("Valid number of units");
            connection.send("Valid number of units");
            p.placeUnitsSameTerritory(territoryName, numOfUnits);
        }
        System.out.println("Finished assigning units");
        connection.send("finished placement");

        // String msg = "You have " + Integer.toString(p.unplacedUnits()) + " units
        // left. Please assign units to your territories. Please enter a number: ";
        // connection.send(msg);
        // String num = connection.recv();
        // int numOfUnits = Integer.parseInt(num);
        // if (numOfUnits < 0 || numOfUnits > p.unplacedUnits()) {
        // connection.send("Invalid number of units");
        // return assignUnits(p, connection);
        // }
        // connection.send("Valid");
        // return num;
    }

    /**
     * Prompt the player to enter his/her name
     * 
     * @param Connection connection
     * @return string entered name of the player
     */
    public String enterName(Connection connection) throws IOException {
        connection.send("Please enter your name: ");
        String name = connection.recv();
        return name;
    }

    /**
     * prompt the player to choose number of players
     * 
     * @param Connection connection
     */
    public void chooseNumOfPlayers(Connection connection) throws IOException {
        connection.send("How many players does the game have?(Valid player number: 2-4) Please enter a number: ");
        String num = connection.recv();
        int numOfPlayers = Integer.parseInt(num);
        if (numOfPlayers < 2 || numOfPlayers > 4) {
            connection.send("Invalid number of players");
            chooseNumOfPlayers(connection);
        }
        connection.send("Valid");
        this.numPlayer = numOfPlayers;
    }

    /**
     * Count the number of players
     * 
     * @return int number of players
     */
    public int getNumPlayer() {
        return this.numPlayer;
    }
}
