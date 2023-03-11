package edu.duke.ece651.team16.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Views {
    private boolean watch;

    public Views() {
        watch = true;
    }

    public String displayMap(HashMap<String, ArrayList<String>> map) {
        StringBuilder sb = new StringBuilder();
        for (String playername : map.keySet()) {
            String title = playername + " player: ";
            sb.append(title + "\n");
            String seperation = String.join("", Collections.nCopies(title.length(), "-"));
            sb.append(seperation + "\n");
            for (String territory : map.get(playername)) {
                sb.append(territory + "\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
