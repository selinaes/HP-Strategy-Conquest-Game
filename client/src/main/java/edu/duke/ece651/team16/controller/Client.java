package edu.duke.ece651.team16.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;

public class Client {
    private Socket clientSocket;
    private BufferedReader socketReceive;
    private PrintWriter socketSend;
    private Views view;
    private String color;
    private String room;

    // system input and output
    final PrintStream out;
    final BufferedReader inputReader;

    private boolean ifExit;

    /**
     * Constructor for Client
     * 
     * @param clientSocket: client socket
     * @param inputSource:  system input
     * @param out:          system output
     * @throws IOException
     */
    public Client(BufferedReader inputSource, PrintStream out,
            BufferedReader socketReceive, PrintWriter socketSend) throws IOException {
        this.out = out;
        this.inputReader = inputSource;
        this.socketReceive = socketReceive;
        this.socketSend = socketSend;
        this.view = new Views(out);
        this.ifExit = false;
        this.clientSocket = null;
        this.color = null;
        this.room = null;
    }

    /**
     * Get the if player exit
     * 
     * @return boolean ifExit
     */
    public boolean ifExit() {
        return ifExit;
    }

    /**
     * 
     * Receive message
     * from server**@return message*
     * 
     * @throws IOException
     */

    public String recvMsg() throws IOException {
        boolean received = false;
        String msg = null;
        while (!received) {
            try {
                msg = socketReceive.readLine();
                if (msg != null) {
                    received = true;
                }
            } catch (IOException e) {
                System.out.println("Failed to receive message.");
            }
        }
        return msg;
    }

    /**
     * Send response to server
     * 
     * @param response: response message
     * @throws IOException
     */
    public void sendResponse(String response) throws IOException {
        this.socketSend.println(response);
    }

    /**
     * Wait for everyone to finish
     * 
     * @throws IOException
     */
    public void waitEveryoneDone() throws IOException {
        String prompt = recvMsg();
        boolean done = false;
        while (!done) {
            if (prompt.equals("stage Complete")) {
                done = true;
            } else {
                prompt = recvMsg();
            }
        }
    }

    /**
     * Player choose color and send to server
     * 
     * @throws IOException
     */
    public boolean playerChooseColor(String color) throws IOException {
        try {
            sendResponse(color);
            // see if selection is valid
            String prompt = recvMsg();
            if (prompt.equals("Valid")) {
                // successful choose color
                out.println("Successfully set color: " + color);
                this.color = color.toLowerCase();
                return true;
            }
        } catch (EOFException e) {
            out.println(e.getMessage());
        }
        return false;
    }

    // set color
    public void setColor(String color) {
        this.color = color;
    }

    public void setRoomID(String room) {
        this.room = room;
    }

    /**
     * Get player color
     * 
     * @return color
     */
    public String getColor() {
        return this.color;
    }

    public String getRoom() {
        return this.room;
    }

    /**
     * Player choose playerNum and send to server
     * 
     * @param num
     */
    public void playerChooseNum(String num) throws IOException {
        // useless in gui: follow
        String prompt = recvMsg();
        if (prompt.equals("finished stage")) {
            out.println(prompt);
            return;
        }
        // useless in gui: above
        try {
            sendResponse(num);
            prompt = recvMsg();
            if (prompt.equals("Valid")) {
                out.println("Successfully choose number of players: " + num);
            }
        } catch (EOFException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * Player choose territory and set unit number
     * 
     * @throws IOException
     * @return boolean if player finished placement
     */
    public void playerAssignUnit(String input) throws IOException {
        String prompt = recvMsg();
        if (prompt.equals("finished stage")) {
            out.println("Finished Placement. Please wait for other players to place their students.");
            return;
        }
        try {
            sendResponse(input);
            prompt = recvMsg();
        } catch (EOFException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * Player send action input to server
     * clientInput = ["order", "T1, T2, numbers"]
     * 
     * @throws IOException
     */
    public String playerOneAction(ArrayList<String> clientInput) throws IOException {
        recvMsg();// String choices = view.displayEntry(recvMsg()); old version
        System.out.println("after recvMsg");
        sendResponse(clientInput.get(0)); // send order (a or m)
        System.out.println("after sendResponse");
        System.out.println(clientInput);
        if (clientInput.get(0).equals("d")) {
            String msg = recvMsg();
            System.out.println(msg);
            if (msg.equals("finished stage")) {
                out.println("Finished 1 Turn of orders. Please wait for other players to issue orders.");
            }
            System.out.println("Finish finished stage");
            return "Valid";
        }
        String prompt = recvMsg(); // "Please enter <Territor ......"
        try {
            if (clientInput.get(0).equals("a") || clientInput.get(0).equals("m") || clientInput.get(0).equals("u")
                    || clientInput.get(0).equals("l") || clientInput.get(0).equals("s")) {
                sendResponse(clientInput.get(1));
            }
            // research
            prompt = recvMsg(); // Valid, or respective error message, from serverside "tryAction" result
            return prompt;
        } catch (EOFException e) {
            out.println(e.getMessage());
        }

        return "Invalid";
    }

    /**
     * Check if the move/attack input format is correct
     *
     * @param clientInput the input from client
     * @return if move input format is valid
     */
    public boolean checkMoveInputFormat(String clientInput) {
        String[] input = clientInput.split(", ");
        if (input.length != 3) {
            return false;
        }
        String unitNum = input[2];
        if (!unitNum.matches("[0-9]+")) {
            return false;
        }
        return true;
    }

    /**
     * Client receives message from server about if the watch option is valid or
     * not
     * 
     * @throws IOException
     */
    public void playerChooseWatch(String clientInput) throws IOException {
        // receive color choosing prompt from server
        recvMsg(); // prompt
        try {
            sendResponse(clientInput);
            // see if selection is valid
            recvMsg(); // valid
            if (clientInput.toLowerCase().equals("e")) {
                // System.exit(0);
                this.ifExit = true;
                return;
            } else {
                view.setWatch();
                return;
            }
        } catch (EOFException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * Get client socket
     * 
     * @return clientSocket
     */

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    /**
     * Set client socket
     * 
     * @param socket
     */
    public void setClientSocket(Socket socket) {
        this.clientSocket = socket;
    }

    /**
     * Player choose room and send to server
     * 
     * @param roomID
     * @return ifEnter
     * @throws IOException
     */
    public String playerChooseRoom(String roomID) throws IOException {
        try {
            sendResponse(roomID); // send roomId
            String ifEnter = recvMsg(); // Room created, or "Room joined." or "Room exceeded player number, game already
                                        // started"
            out.println(ifEnter);
            return ifEnter;
        } catch (EOFException e) {
            out.println(e.getMessage());
        }
        return "";
    }
}
