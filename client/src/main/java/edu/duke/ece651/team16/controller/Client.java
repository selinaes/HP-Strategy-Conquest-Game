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
    }

    public boolean ifExit() {
        return ifExit;
    }

    /**
     * Run watch option in the game for the client
     */
    public void runWatchOption() throws IOException {
        String msg2 = recvMsg();
        if (msg2.equals("Choose watch")) {
            // playerChooseWatch();
            if (ifExit) {
                return;
            }
        }
        if (!view.isWatch()) {
            // playerActionTurn();
        } else {
            view.playerWatchTurn();
        }

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
     * Read client input from system input
     * 
     * @param prompt: prompt message
     * @return client input
     * @throws IOException
     */
    public String readClientInput(String prompt) throws IOException {
        out.println(prompt);
        String s = null;
        s = this.inputReader.readLine();
        if (s == null) {
            throw new EOFException();
        }
        out.println(s);
        return s;
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

    public String getColor() {
        return this.color;
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
            out.println("Finished Placement. Please wait for other players to place units.");
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
        sendResponse(clientInput.get(0)); // send order (a or m)
        if (clientInput.get(0).equals("d")) {
            String msg = recvMsg();
            if (msg.equals("finished stage")) {
                out.println("Finished 1 Turn of orders. Please wait for other players to issue orders.");
            }
            return "Valid";
        }
        String prompt = recvMsg(); // "Please enter <Territor ......"
        try {
            if (clientInput.get(0).equals("a") || clientInput.get(0).equals("m") || clientInput.get(0).equals("u")) {
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

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket socket) {
        this.clientSocket = socket;
    }

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
