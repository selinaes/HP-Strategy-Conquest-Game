package edu.duke.ece651.team16.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.beans.Transient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class ViewsTest {
    private String jsonString = "{\"name\":[{\"TerritoryName\":\"a\",\"Neighbors\":\"(next to: b)\",\"Unit\":\"1\"}]}";
    private String jsonStringLog = "{\"Entry\":\"You are the blue player, what would you like to do?\"}";

    private String makeInitialMapString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nname player will have these territories: \n");
        sb.append("-----------------------------------------\n");
        sb.append("a (next to: b)\n\n");

        String output = sb.toString();
        return output;
    }

    private String makeMapString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name player: \n");
        sb.append("-------------\n");
        sb.append("1 in a (next to: b)\n");

        String output = sb.toString();
        return output;
    }

    private String makeLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        String output = sb.toString();
        return output;
    }

    @Test
    public void test_displayInitialMap() throws IOException, Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Views view = new Views(new PrintStream(out));
        view.displayInitialMap("Not Valid");
        System.setOut(view.out);
        view.displayInitialMap(jsonString);
        System.setOut(view.out);
        assertEquals(makeInitialMapString(), out.toString());
    }

    @Test
    public void test_displayMap() throws IOException, Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Views view = new Views(new PrintStream(out));
        view.displayMap("Not Valid");
        view.displayMap(jsonString);
        System.setOut(view.out);
        assertEquals(makeMapString(), out.toString());
    }

    @Test
    public void test_displayEntry() throws IOException, Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Views view = new Views(new PrintStream(out));

        view.displayEntry(jsonString);
        view.displayEntry("Not Valid");
    }

    @Test
    public void test_displayLog() throws IOException, Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Views view = new Views(new PrintStream(out));
        view.displayLog("Not Valid");
        view.displayLog(jsonStringLog);
        System.setOut(view.out);
        System.out.println(out.toString());
        assertEquals(makeLogString(), out.toString());
        view.playerWatchTurn();
    }

    @Test
    public void test_isWatch() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Views view = new Views(new PrintStream(out));
        view.setWatch();
        assertTrue(view.isWatch());
    }

}
