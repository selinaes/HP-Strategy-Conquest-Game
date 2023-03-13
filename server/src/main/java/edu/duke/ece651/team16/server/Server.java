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
    protected List<Player> players;
    private ServerSocket listenSocket;
    ThreadPoolExecutor threadPool;
    private int numPlayer;
    private List<String> colors;
    private HashMap<String, List<Territory>> defaultMap;

    /**
     * Constructor for Server class that takes in a serverSocket
     * 
     * @param ServerSocket serverSocket
     */
    public Server(ServerSocket serverSocket) {
        this.players = new ArrayList<Player>();
        this.listenSocket = serverSocket;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(2, 16, 5, TimeUnit.SECONDS, workQueue);

        Map map = new Map(2);
        this.defaultMap = map.createDukeMap();
        this.colors = map.getColorList();
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
            // This will enqueue the request until
            // a thread in the pool is available, then
            // execute that request on the available thread.
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            // connect to new client, add serverside socket to connection obj
                            Connection connection = new Connection(client_socket);
                            // ask choose color, add new player to server
                            String color = chooseColor(connection);
                            String name = enterName(connection);
                            Player p = new Player(name, color, connection, defaultMap.get(color));
                            addPlayer(p);
                            // send map to client
                            HashMap<String, ArrayList<HashMap<String, String>>> to_send = formMap();
                            try {
                                sendMap(p, to_send);
                            } catch (JsonProcessingException e) {
                                System.out.println("JsonProcessingException");
                            }

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
                list.add(entryMap);
            }
            map.put(p.getName(), list);
        }
        return map;
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
