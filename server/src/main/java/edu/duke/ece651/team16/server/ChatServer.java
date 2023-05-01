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
    private HashMap<Player, Conn> playersCon;

    public ChatServer() {
        try {
            this.chatServerSock = new ServerSocket(4321);
        } catch (IOException e) {
            System.out.println("Failed to create chatServerSocket!");
        }
        this.players = new ArrayList<Player>();
        this.chatHandlers = new ArrayList<ChatHandler>();
        this.communicators = new ArrayList<Conn>();
        this.playersCon = new HashMap<Player, Conn>();
    }

    /**
     * add player to the chat server
     */
    public void addPlayer(Player p) {
        players.add(p);
    }

    public int getPlayerListSize() {
        return players.size();
    }

    /**
     * start the chat server
     */
    public void setUp(String name) {
        try {
            System.out.println("ChatServer: waiting for new player...");
            Conn curCommunicator = new Conn(chatServerSock.accept());
            for (Player p : players) {
                if (p.getColor().equals(name)) {
                    playersCon.put(p, curCommunicator);
                }
            }
            communicators.add(curCommunicator);
            ChatHandler curHandler = new ChatHandler(curCommunicator, this);
            chatHandlers.add(curHandler);
            // for(ChatHandler c: chatHandlers){
            // c.setChatServer(this);
            // }
            curHandler.start();
            System.out.println("ChatServer: new player joined!");
        } catch (IOException e) {
            System.out.println("Failed to accept new player!");
        }
    }

    /**
     * send the first weclome message to all the player
     * alse send the player list to all the player
     */
    public void sendWelcome() {
        String playerListString = "playerlist:";
        for (Player p : players) {
            playerListString += p.getColor() + " ";
        }
        sendToAll(playerListString + ":All");
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

    // /**
    // * send a message to certain player
    // */
    // public synchronized void sendToOne(String message, String name, String from)
    // {
    // System.out.println(from + " " + name);
    // for(Player p: players){
    // if(p.getColor().equals(name)){
    // if (from.equals("map")) {
    // playersCon.get(p).send("map:" + message);
    // }else{
    // playersCon.get(p).send(from + ": " + message + "(Private message)");
    // System.out.println("ChatServer: send message to " + name + ": " + message);
    // }
    // }
    // }
    // }
}