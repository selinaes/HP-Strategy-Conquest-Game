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

    /*
     * Constructor
     * 
     * @param IP: server IP
     * 
     * @param port: server port
     * 
     * @param inputSource: system input
     * 
     * @param out: system output
     * 
     * @throws IOException
     */
    public Client(String IP, int port, BufferedReader inputSource, PrintStream out) throws IOException {
        // views = new Views();
        // this.clientIO = new ClientIO(IP, port);
        // clientIO.send("Client are trying to connect to server...");
        this.out = out;
        this.inputReader = inputSource;
        try {
            this.clientSocket = new Socket(IP, port);
            this.socketReceive = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.socketSend = new PrintWriter(clientSocket.getOutputStream(), true);

            // system input and output
            // new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Failed to initialize Connection.");
        }
    }

    /*
     * Run client
     * 
     * @throws IOException
     */
    public void run() throws IOException {
        while (true) {
            // step1: Init Game setting: Color
            playerChooseColor();

            displayMap();

            // step2: Init Game setting: Territory, Units

            // step3: Do placement

            // step4: Play game

        }

        // this.close();
    }

    /*
     * Receive message from server
     * 
     * @return message
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

    /*
     * Read client input from system input
     * 
     * @param prompt: prompt message
     * 
     * @return client input
     * 
     * @throws IOException
     */
    public String readClientInput(String prompt) throws IOException {
        out.println(prompt);
        String s = null;
        s = this.inputReader.readLine();
        if (s == null) {
            throw new EOFException();
        }
        return s;
    }

    /*
     * Send response to server
     * 
     * @param response: response message
     * 
     * @throws IOException
     */
    public void sendResponse(String response) throws IOException {
        this.socketSend.println(response);
    }

    /*
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

    /*
     * Player choose color and send to server
     * 
     * @throws IOException
     */
    public void playerChooseColor() throws IOException {
        // receive color choosing prompt from server
        String prompt = recvMsg();
        String clientInput = "";
        boolean validInput = false;
        while (!validInput) {
            try {
                clientInput = readClientInput(prompt);
                sendResponse(clientInput);
                // successful choose color
                String color = clientInput;
                out.println("Successfully set color: " + color);
            } catch (IllegalArgumentException e) {
                out.println(e.getMessage());
            }
        }
    }

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
                out.println(territory + " " + neighbors);
            }
        }
    }
}
