package edu.duke.ece651.team16.server;

import java.util.ArrayList;
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

public class ChatServer {
    private ServerSocket chatServerSock;
    ThreadPoolExecutor threadPool;
    private ArrayList<Player> players;
    private ArrayList<ChatHandler> chatHandlers;
    private ArrayList<Conn> communicators;
    private int playerNum;

    public ChatServer(int Num) {
        try {
            this.chatServerSock = new ServerSocket(4321);
        } catch (IOException e) {
            System.out.println("Failed to crete chatServerSocket!");
        }
        this.players = new ArrayList<Player>();
        this.chatHandlers = new ArrayList<>(Num);
        this.playerNum = Num;
    }

    /**
     * add player to the chat server
     */
    public void addPlayer(Player p) {
        players.add(p);
    }

    /**
     * start the chat server
     */
    public void setUp() {
        for (int i = 0; i < 2; i++) {
            try {
                Conn curCommunicator = new Conn(chatServerSock.accept());
                communicators.add(curCommunicator);
                ChatHandler curHandler = new ChatHandler(curCommunicator, this);
                chatHandlers.add(curHandler);
                curHandler.start();
            } catch (IOException e) {
                System.out.println("Failed to create chatHandler!");
            }

        }
    }

    /**
     * send a message to all players
     */
    public synchronized void sendToAll(String message) {
        for (Player p : players) {
            p.getConn().send(message);
        }
    }
}