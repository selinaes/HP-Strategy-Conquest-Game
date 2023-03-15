package edu.duke.ece651.team16.client;

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

    private BufferedReader socketReceive;
    private PrintWriter socketSend;
    private Socket clientSocket;

    // system input and output
    final PrintStream out;
    final BufferedReader inputReader;

    /**
     * Constructor for Client
     * 
     * @param IP:          server IP
     * @param port:        server port
     * @param inputSource: system input
     * @param out:         system output
     * @throws IOException
     */
    public Client(Socket clientSocket, BufferedReader inputSource, PrintStream out) throws IOException {
        this.clientSocket = clientSocket;
        this.out = out;
        this.inputReader = inputSource;
        socketReceive = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        socketSend = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    /**
     * Run client
     * 
     * @throws IOException
     */
    public void run() throws IOException {
        // step1: Init Game setting: Color, enter name
        playerChooseNum();
        waitEveryoneDone("setNumPlayer Complete");
        // out.println("out of wait");
        displayInitialMap();
        playerChooseColor();
        playerAssignAllUnits();
        waitEveryoneDone("setUnits Complete");
        // out.println("out of wait");
        displayMap();

        // step2: Init Game setting: Territory, Units

        // step3: Do placement

        // step4: Play game
        // this.close();
    }

    /**
     * Receive message from server
     * 
     * @return message
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
        out.println("ReadClientInput: " + prompt);
        String s = null;
        s = this.inputReader.readLine();
        if (s == null) {
            throw new EOFException();
        }
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
     * Close client
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Failed to close socket..");
        }
    }

    /**
     * Wait for everyone to finish
     * 
     * @throws IOException
     */
    public void waitEveryoneDone(String expectPrompt) throws IOException {
        // out.println("wait everyone done");
        String prompt = recvMsg();
        boolean done = false;
        while (!done) {
            if (prompt.equals(expectPrompt)) {
                // out.println(prompt);
                done = true;
            } else {
                // out.println(prompt);
                prompt = recvMsg();
            }
        }
    }

    /**
     * Player choose color and send to server
     * 
     * @throws IOException
     */
    public void playerChooseColor() throws IOException {
        // receive color choosing prompt from server
        String prompt = recvMsg();
        String clientInput = "";
        while (true) {
            try {
                clientInput = readClientInput(prompt);
                sendResponse(clientInput);
                // see if selection is valid
                prompt = recvMsg();
                if (prompt.equals("Valid")) {
                    // successful choose color
                    String color = clientInput;
                    out.println("Successfully set color: " + color);
                    return;
                } else {
                    out.println("Invalid color or color already taken. Please choose again.");
                    playerChooseColor();
                    return;
                }
            } catch (EOFException e) {
                out.println(e.getMessage());
            }
        }
    }

    public void displayInitialMap() throws IOException {
        String jsonString = recvMsg();

        // convert jsonString to jsonobject
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, ArrayList<HashMap<String, String>>> input_map;
        try {
            input_map = objectMapper.readValue(jsonString, HashMap.class);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert json string to json object.");
            return;
        }
        // Player name is the input_map key
        // ArrayList<HashMap<String, String>> is the value of the key
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : input_map.entrySet()) {
            String color = entry.getKey();
            out.println();
            String title = color + " player will have these territories: ";
            out.println(title);
            String seperation = String.join("", Collections.nCopies(title.length(), "-"));
            out.println(seperation);
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            for (HashMap<String, String> asset : playerAsset) {
                String territory = asset.get("TerritoryName");
                String neighbors = asset.get("Neighbors");
                out.println(territory + " " + neighbors);
            }
            out.println();
        }
    }

    /**
     * Display map
     * 
     * @throws IOException
     */
    public void displayMap() throws IOException {
        String jsonString = recvMsg();

        // convert jsonString to jsonobject
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, ArrayList<HashMap<String, String>>> input_map;
        try {
            input_map = objectMapper.readValue(jsonString, HashMap.class);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert json string to json object.");
            return;
        }
        // Player name is the input_map key
        // ArrayList<HashMap<String, String>> is the value of the key
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : input_map.entrySet()) {
            String playername = entry.getKey();
            String title = playername + " player: ";
            out.println(title);
            String seperation = String.join("", Collections.nCopies(title.length(), "-"));
            out.println(seperation);
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            for (HashMap<String, String> asset : playerAsset) {
                String territory = asset.get("TerritoryName");
                String neighbors = asset.get("Neighbors");
                String units = asset.get("Unit");
                out.println(units + " in " + territory + " " + neighbors);
            }
        }
    }

    /**
     * Player choose playerNum and send to server
     * 
     * @throws IOException
     */
    public void playerChooseNum() throws IOException {
        String clientInput = "";
        // out.println("Client in playerChooseNum");
        String prompt = recvMsg();
        if (prompt.equals("Not the first player. Please wait for the first player to set player number.")) {
            out.println(prompt);
            return;
        }
        while (true) {
            try {
                clientInput = readClientInput(prompt);
                sendResponse(clientInput);
                prompt = recvMsg();
                if (prompt.equals("Valid")) {

                    String playerNum = clientInput;
                    out.println("Successfully choose number of players: " + playerNum);
                    return;
                } else {
                    out.println("Invalid number of players.");
                    playerChooseNum();
                    return;
                }
            } catch (EOFException e) {
                out.println(e.getMessage());
            }
        }
    }

    public boolean playerAssignUnit() throws IOException {
        String prompt = recvMsg();
        if (prompt.equals("finished placement")) {
            out.println("Finished Placement. Please wait for other players to place units.");
            return true;
        }
        String clientInput = "";
        while (true) {
            try {
                clientInput = readClientInput(prompt);
                sendResponse(clientInput);
                prompt = recvMsg();
                if (prompt.equals("finished placement")) {
                    out.println("Finished Placement. Please wait for other players to place units.");
                    return true;
                } else if (prompt.equals("Valid territory name")) {
                    String territoryName = clientInput;
                    out.println("Successfully choose territory: " + territoryName);
                    prompt = recvMsg();
                    continue;
                } else if (prompt.equals("Valid number of units")) {
                    String unitNum = clientInput;
                    out.println("Successfully set unit number: " + unitNum);
                    return false;
                } else {
                    out.println("Fails to assign unit");
                    // prompt = recvMsg();
                    return playerAssignUnit();
                }
            } catch (EOFException e) {
                out.println(e.getMessage());
            }
        }
    }

    public void playerAssignAllUnits() throws IOException {
        while (true) {
            boolean finished = playerAssignUnit();
            if (finished) {
                // out.println("Finished assigning units Clinet Side");
                return;
            }
            // prompt = recvMsg();
        }
    }

}
