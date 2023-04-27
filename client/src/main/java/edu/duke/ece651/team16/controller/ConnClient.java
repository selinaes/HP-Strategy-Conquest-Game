package edu.duke.ece651.team16.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ConnClient {
    private Socket clientsocket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructor for Server side ConnClient => will send/recv messages to/from
     * client
     * 
     * @param socket: the "accepted" client socket representing the client side
     */
    public ConnClient(Socket clientsocket) {
        try {
            this.clientsocket = clientsocket;
            this.in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
            this.out = new PrintWriter(clientsocket.getOutputStream(), true);

        } catch (IOException e) {
            System.out.println("Failed to initialize Connection.");
        }
    }

    /**
     * send a message to out
     *
     * @param msg: the message to be sent
     */
    public void send(String msg) {
        if (out != null) {
            out.println(msg);
        } else {
            System.err.println("Failed to send message: out is null.");
        }
    }

    /**
     * Receive a message from the other side
     * 
     * @return: the message received
     */
    public String recv() {
        boolean received = false;
        String ans = null;
        while (!received) {
            try {
                ans = in.readLine();
                if (ans != null) {
                    received = true;
                    if (ans.equals("close")) {
                        this.close();
                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to receive message.");
                break;
                // System.exit(1);
            }
        }
        return ans;
    }

    /**
     * Close the connection
     */
    public void close() {
        try {
            clientsocket.close();
        } catch (IOException e) {
            System.out.println("Failed to close socket..");
        }
    }
}
