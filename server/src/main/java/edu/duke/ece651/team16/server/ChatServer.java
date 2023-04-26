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

    public ChatServer() {
        try {
            this.chatServerSock = new ServerSocket(4321);
        } catch (IOException e) {
            System.out.println("Failed to create chatServerSocket!");
        }
        this.players = new ArrayList<Player>();
        this.chatHandlers = new ArrayList<ChatHandler>();
        this.communicators = new ArrayList<Conn>();
        this.playerNum = 0;
    }

    /**
     * add player to the chat server
     */
    public void addPlayer(Player p) {
        players.add(p);
        playerNum++;
    }

    /**
     * start the chat server
     */
    public void setUp() {
        try {
            System.out.println("ChatServer: waiting for new player...");
            Conn curCommunicator = new Conn(chatServerSock.accept());
            communicators.add(curCommunicator);
            ChatHandler curHandler = new ChatHandler(curCommunicator, this);
            chatHandlers.add(curHandler);
            curHandler.start();
            System.out.println("ChatServer: new player joined!");
        } catch (IOException e) {
            System.out.println("Failed to accept new player!");
        }
    }

    /**
     * send a message to all players
     */
    public synchronized void sendToAll(String message) {
        for (Conn c : communicators) {
            c.send(message);
            System.out.println("ChatServer: send message: " + message);
        }
    }

    /**
     * send a message to certain player
     */
    public synchronized void sendToOne(String message, String name) {
        int i = 0;
        for (Conn c : communicators) {
            if (players.get(i).getColor().equals(name)) {
                c.send(message);
                System.out.println("ChatServer: send message: " + message);
                break;
            }
            i++;
        }
    }
}