package edu.duke.ece651.team16.server;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.ThreadPoolExecutor;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
// import com.worklight.server.util.JSONUtils;

public class Server {
    private ServerSocket listenSocket;
    ThreadPoolExecutor threadPool;
    // private Game game;
    private int numClients;
    private HashMap<String, Game> gameList; // <gameRoomID, Game>
    private HashMap<String, Integer> gameClients; // <gameRoomID, numClients>
    private HashMap<String, String> accounts;

    /**
     * Constructor for Server class that takes in a serverSocket
     * 
     * @param ServerSocket serverSocket
     */
    public Server(ServerSocket serverSocket) {
        this.listenSocket = serverSocket;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(16, 16, 5, TimeUnit.SECONDS, workQueue);
        // this.game = new Game(24, "Duke");
        this.numClients = 0; // now used to debug, # of clients connected
        this.gameList = new HashMap<>();
        this.gameClients = new HashMap<>();
        this.accounts = new HashMap<>();
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
                            System.out.println("Client connected" + numClients);
                            // check gameRoom ID
                            Conn conn = new Conn(client_socket);
                            login(conn);
                            enterGame(conn);
                        } finally {
                            client_socket.close();
                        }
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                }
            });
        }

    }

    public void login(Conn conn) {
        // login, save password + username
        while (true) {
            conn.send("Please enter your username and password:");
            String userAndPw = conn.recv();
            String[] userAndPwArr = userAndPw.split(", ");
            String username = userAndPwArr[0];
            String password = userAndPwArr[1];

            synchronized (this) {
                if (accounts.containsKey(username)) {
                    if (accounts.get(username).equals(password)) {
                        conn.send("Login successful.");
                        break;
                    } else {
                        conn.send("Wrong password, please try again.");
                        continue;
                    }
                } else {
                    accounts.put(username, password);
                    conn.send("New account created.");
                    break;
                }
            }
        }
    }

    public void enterGame(Conn conn) {
        while (true) {
            conn.send("Welcome to the game! Please enter a room ID:");
            // enter game.gameFlow with specific room ID
            String roomID = conn.recv();
            // new game
            if (!gameList.containsKey(roomID)) {
                gameList.put(roomID, new Game(24, "HP"));
                gameClients.put(roomID, 1);
                conn.send("Room created.");
                gameList.get(roomID).gameFlow(conn, 1);
                break;
            }
            // if game already exists
            else {
                gameClients.put(roomID, gameClients.get(roomID) + 1);
                if (!gameList.get(roomID).getGameState().equals("setNumPlayer")) {
                    conn.send("Room exceeded player number, game already started.");
                    continue;
                }
                conn.send("Room joined.");
                gameList.get(roomID).gameFlow(conn, 2);
                break;
            }
        }
    }

}
