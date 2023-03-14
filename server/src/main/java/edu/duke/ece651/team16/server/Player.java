package edu.duke.ece651.team16.server;

import java.util.List;
import java.util.ArrayList;
public class Player {

    private List<Territory> Territories;
    private ArrayList<Unit> units;
    private String name;
    private String color;
    private Connection connection;
    private int numUnits;

    /**
     * Constructor of the player
     * 
     * @param name
     * @param color
     * @param connection
     */
    Player(String name, String color, Connection connection, List<Territory> Territories, int numUnits) {
        this.name = name;
        this.color = color;
        this.connection = connection;
        this.Territories = Territories;
        this.numUnits = numUnits;
        this.units = new ArrayList<>();
        for (int i = 0; i < numUnits; i++) {
            this.units.add(new BasicUnit(this, null, false, i));
        }
    }


    /**
     * get the number of units of the player remaining
     * 
     * @return number of units
     */
    public int unplacedUnits() {
        int unplacedNum = 0;
        for (Unit u: this.units) {
            if (u.getwhere() == null) {
                unplacedNum++;
            }
        }
        return unplacedNum;
    }

    /**
     * Find next unplaced unit
     * 
     * @return the unit
     */
    public Unit findNextUnplacedUnit() {
        for (Unit u: this.units) {
            if (u.getwhere() == null) {
                return u;
            }
        }
        return null;
    }
    
    /*
     * Place the unit in the territory
     */
    public void placeUnit(String t_name, Unit u) {
        System.out.println("Outer loop, Territory name: " + t_name);
        // System.out.println("Player's territory size:  " + getTerritoryNames().size());
        // System.out.println("Player's territory:  " + getTerritoryNames().get(0));
        // System.out.println("Check if it contains this territory:  " + getTerritoryNames().contains(t_name));
        if (getTerritoryNames().contains(t_name)) {
            System.out.println("Territory name: " + t_name);
            Territory t = Territories.get(getTerritoryNames().indexOf(t_name));
            t.tryAddUnits(u);
        }
    }

    /*
     * Place the units in the same territory
     */
    public void placeUnitsSameTerritory(String t_name, int num) {
        for (int i = 0; i < num; i++) {
            Unit u = findNextUnplacedUnit();
            placeUnit(t_name, u);
        }
        
    }

    /*
     * return all territory name for the player
     * 
     * @return territory names
     */
    public ArrayList<String> getTerritoryNames() {
        ArrayList<String> territoryNames = new ArrayList<>();
        for (Territory t: this.Territories) {
            territoryNames.add(t.getName());
        }
        return territoryNames;
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
     * get the name of the player
     * 
     * @return name of the player
     */
    public String getName() {
        return this.name;
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
