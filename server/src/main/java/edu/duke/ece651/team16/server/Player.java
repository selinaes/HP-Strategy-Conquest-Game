package edu.duke.ece651.team16.server;

import java.util.List;

public class Player {

    private List<Territory> Territories;
    private String color;
    private Connection connection;

    /**
     * Constructor of the player
     * 
     * @param color
     * @param connection
     */
    Player(String color, Connection connection, List<Territory> Territories) {
        this.color = color;
        this.connection = connection;
        this.Territories = Territories;
    }

    /**
     * Add the territories of the player in the placement phase
     * 
     * @param territory
     */
    public void addTerritories(Territory... territory) {
        for (Territory t : territory) {
            this.Territories.add(t);
            t.setOwner(this);
        }
    }

    /**
     * get the territories of the player in the placement phase
     * 
     * @return Territories of the player
     */

    public List<Territory> getTerritories() {
        return this.Territories;
    }


    /**
     * get the color of the player
     * 
     * @return color of the player
     */
    public String getColor() {
        return this.color;
    }

    /**
     * get connection of the player
     * 
     * @return connection
     */
    public Connection getConnection() {
        return this.connection;
    }
}
