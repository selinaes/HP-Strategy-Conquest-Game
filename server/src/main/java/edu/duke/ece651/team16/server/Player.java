package edu.duke.ece651.team16.server;
import edu.duke.ece651.team16.shared.Connection;

import java.util.ArrayList;
import java.util.List;


public class Player {
    
    private List<Territory> Territories;
    private String name;
    private String color;
    private Connection connection;

    /**
     * Constructor of the player
     * @param name
     * @param color
     */
    Player(String name, String color){
        this.name = name;
        this.color = color;
        Territories = new ArrayList<>();
    }

    /**
     * Add the territories of the player in the placement phase
     * @param territory
     */
    public void addTerritories(Territory... territory){
        for (Territory t: territory){
            this.Territories.add(t);
        }
    }

    /**
     * get the territories of the player in the placement phase
     * @return Territories
     */
    
    public List<Territory> getTerritories() {
        return this.Territories;
    }

    /**
     * get the name of the player
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * get the color of the player
     * @return color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * get the color of the player
     * @return color
     */
    public String getColor() {
        return this.color;
    }
}
