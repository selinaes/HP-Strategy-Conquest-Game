package edu.duke.ece651.team16.server;
import edu.duke.ece651.team16.shared.*;
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
    private List<Player> players;
    private ServerSocket listenSocket;
    ThreadPoolExecutor threadPool;
    private int numPlayer;
    private List<String> colors;
    private int port;
    // private List<Connection> connections;
    // private HashMap<String, List<Territory>> defaultMap;
    
    public Server(int port) {
        this.players = new ArrayList<Player>();
        try{
            this.listenSocket = new ServerSocket(port);
        }
        catch(IOException e){
            System.out.println("Failed to initialize Connection.");
        }
        this.port = port;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(2, 16, 5, TimeUnit.SECONDS, workQueue);
        this.colors = new ArrayList<String>();
        colors.add("Red");
        colors.add("Blue");
        // this.defaultMap = createDukeMap();
    }

    
    /* 
    * Add a player to the server
    */
    public void addPlayer(Player p) {
        players.add(p);
    }

    /**
   * This is a helper method to accept a socket from the ServerSocket
   * or return null if it timesout.
   */
    private Socket acceptOrNull() {
        try {
        return listenSocket.accept();
        } catch (IOException ioe) {
        // In real code, we would want to be more discriminating here.
        // Was this a timeout, or some other problem?
        return null;
        }
    }
    
    // run server to receive client's message
    public void run() throws IOException {
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
                    Player p = new Player(name, color, connection);
                    addPlayer(p);
                    
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



    /* Form a hashmap playerName: player's territoriesName and corresponding neighbors
    * @return HashMap<String, ArrayList<String>>
    */
    public HashMap<String, ArrayList<String>> formMap() {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        for (Player p : players) {
            ArrayList<String> list = new ArrayList<String>();
            for (Territory t : p.getTerritories()) {
                // ArrayList<Territory> neighbors = t.getNeighbors();
                // String neighborNames = "";
                // for (Territory neighbor : neighbors) {
                //     neighborNames += neighbor.getName() + " ";
                // }
                list.add(t.getName());
                // list.add(neighborNames);
            }
            map.put(p.getName(), list);
        }
        return map;
    }

    /**
    * Send a hashmap to client
    * @param player
    * @param HashMap<String, ArrayList<String>> to_send
    */
    public void sendMap(Player player, HashMap<String, ArrayList<String>> to_send) throws JsonProcessingException {
        try{
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

    /*
    * Prompt the player to choose a color
    * @param Connection connection
    */
    public String chooseColor(Connection connection) throws IOException {
        // connection.send("Which color do you want to choose? Please enter a number.\n Current available colors are: ");
        String colorList = "";
        for (int i = 0; i < colors.size(); i++) {
            colorList += i + ". " + colors.get(i) + " ";
        }
        connection.send("Which color do you want to choose? Please enter a number. Current available colors are: "+colorList);
        String index = connection.recv();
        System.out.println("Player's color is: " + index);
        int colorindex = Integer.parseInt(index);
        if (colorindex < 0 || colorindex >= colors.size()) {
            connection.send("Invalid color index");
            chooseColor(connection);
        }
        colors.remove(colorindex);
        return colors.get(colorindex);
    }

    /* 
    * Prompt the player to enter his/her name
    * @param Connection connection
    */
    public String enterName(Connection connection) throws IOException {
        connection.send("Please enter your name: ");
        String name = connection.recv();
        return name;
    }

    
}
