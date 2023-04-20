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

public class ChatClient {
    private BufferedReader socketReceive;
    private PrintWriter socketSend;
    final PrintStream out;
    final BufferedReader inputReader;

    public ChatClient(BufferedReader inputSource, PrintStream out,
            BufferedReader socketReceive, PrintWriter socketSend) throws IOException {
        this.inputReader = inputSource;
        this.out = out;
        this.socketReceive = socketReceive;
        this.socketSend = socketSend;
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
}