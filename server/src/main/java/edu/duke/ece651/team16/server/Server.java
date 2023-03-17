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

    /**
     * Constructor for Server class that takes in a serverSocket
     * 
     * @param ServerSocket serverSocket
     */
    public Server(ServerSocket serverSocket) {

        this.listenSocket = serverSocket;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(2, 16, 5, TimeUnit.SECONDS, workQueue);
        this.game = new Game(5);
        this.numClients = 0;
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
                            game.gameFlow(client_socket, numClients);
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

}
