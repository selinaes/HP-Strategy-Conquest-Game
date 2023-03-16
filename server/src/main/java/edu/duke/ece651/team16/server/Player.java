package edu.duke.ece651.team16.server;

import java.util.List;
import java.util.ArrayList;

public class Player {

    private List<Territory> Territories;
    private ArrayList<Unit> units;
    private String color;
    private Connection connection;
    private int numUnits;
    private final AssignUnitRuleChecker placementchecker;

    /**
     * Constructor of the player
     * 
     * @param color
     * @param connection
     */
    Player(String color, Connection connection, List<Territory> Territories, int numUnits) {
        this.color = color;
        this.connection = connection;
        this.Territories = new ArrayList<>();
        addTerritories(Territories);
        this.numUnits = numUnits;
        this.units = new ArrayList<>();
        this.placementchecker = new AssignUnitRuleChecker();
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
        for (Unit u : this.units) {
            if (u.getwhere() == null) {
                unplacedNum++;
            }
        }
        return unplacedNum;
    }

    /**
     * Find next unplaced unit
     * Precondion ensured there are enough amounts of unplaced units
     * 
     * @return the unit
     */
    public ArrayList<Unit> findNextUnplacedUnits(int amount) {
        int count = 0;
        ArrayList<Unit> unplacedUnits = new ArrayList<>();
        for (Unit u : this.units) {
            if (u.getwhere() != null) {
                continue;
            } else {
                unplacedUnits.add(u);
                count++;
                if (count >= amount) {
                    break;
                }
            }
        }
        return unplacedUnits;
    }

    /*
     * Place the units in the same territory
     */
    public String placeUnitsSameTerritory(String t_name, int num) {
        if (placementchecker.checkMyRule(t_name, this, num) == null) {
            // System.out.println("Territory name: " + t_name);
            Territory t = Territories.get(getTerritoryNames().indexOf(t_name));
            ArrayList<Unit> units = findNextUnplacedUnits(num);
            t.tryAddUnits(units);
        }
        return placementchecker.checkMyRule(t_name, this, num);

        // for (int i = 0; i < num; i++) {
        // Unit u = findNextUnplacedUnit();
        // if (placementRuleChecker.checkPlacement(u, t_name, this) == null) {
        // // System.out.println("Territory name: " + t_name);
        // Territory t = Territories.get(getTerritoryNames().indexOf(t_name));
        // t.tryAddUnits(u);
        // } else {
        // return placementRuleChecker.checkPlacement(u, t_name, this);
        // }

        // // placeUnit(t_name, u);
        // }
        // return null;

    }

    /*
     * return all territory name for the player
     * 
     * @return territory names
     */
    public ArrayList<String> getTerritoryNames() {
        ArrayList<String> territoryNames = new ArrayList<>();
        for (Territory t : this.Territories) {
            territoryNames.add(t.getName());
        }
        return territoryNames;
    }

    /**
     * Add the territories of the player in the placement phase
     * 
     * @param territory
     */
    public void addTerritories(List<Territory> territory) {
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
