package edu.duke.ece651.team16.server;

import java.util.List;
import java.util.ArrayList;

public class Player {
    private List<Territory> Territories;
    private ArrayList<Unit> units;
    private String color;
    private Conn conn;
    private int numUnits;
    private final AssignUnitRuleChecker placementchecker;
    private boolean isWatch;
    private int foodResource;
    private int techResource;
    private int techLevel;
    private boolean hasResearched;
    private int delayedTech;

    private boolean doubleResourceSwitch; // true for on, false for off
    private boolean moreUnitSwitch; // true for on, false for off
    private boolean disregardAdjacencySwitch; // true for on, false for off
    private boolean diceAdvantageSwitch; // true for on, false for off

    private Player ally;
    private Player pendingally;

    /**
     * Constructor of the player
     * 
     * @param color
     * @param Conn
     */
    Player(String color, Conn conn, List<Territory> Territories, int numUnits) {
        this.color = color;
        this.conn = conn;
        this.Territories = new ArrayList<>();
        addTerritories(Territories);
        this.numUnits = numUnits; // All owned units including dead and unplaced
        this.units = new ArrayList<>();
        this.placementchecker = new AssignUnitRuleChecker();
        for (int i = 0; i < numUnits; i++) {
            this.units.add(new AdvancedUnit(this, null, false, i));
        }
        this.isWatch = false;
        this.foodResource = 0;
        this.delayedTech = 0;
        this.techResource = 0;
        this.techLevel = 1;
        this.hasResearched = false;

        // switches for special abilities
        this.doubleResourceSwitch = false;
        this.moreUnitSwitch = false;
        this.disregardAdjacencySwitch = false;
        this.diceAdvantageSwitch = false;

        this.ally = null;
        this.pendingally = null;
    }

    public void setAlly(Player ally) {
        this.ally = ally;
    }

    public Player getAlly() {
        return this.ally;
    }

    public void setPendingAlly(Player pendingally) {
        this.pendingally = pendingally;
    }

    public Player getPendingAlly() {
        return this.pendingally;
    }

    public void updateResearchRound(boolean status) {
        this.hasResearched = status;
    }

    public boolean getHasResearched() {
        return this.hasResearched;
    }

    public void setDoubleResourceSwitch(boolean status) {
        this.doubleResourceSwitch = status;
    }

    public void setMoreUnitSwitch(boolean status) {
        this.moreUnitSwitch = status;
    }

    public void setDisregardAdjacencySwitch(boolean status) {
        this.disregardAdjacencySwitch = status;
    }

    public void setDiceAdvantageSwitch(boolean status) {
        this.diceAdvantageSwitch = status;
    }

    public boolean getDisregardAdjacencySwitch() {
        return this.disregardAdjacencySwitch;
    }

    public boolean getDiceAdvantageSwitch() {
        return this.diceAdvantageSwitch;
    }

    public void resetAllSwitches() {
        this.doubleResourceSwitch = false;
        this.moreUnitSwitch = false;
        this.disregardAdjacencySwitch = false;
        this.diceAdvantageSwitch = false;
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
     * Remove the territories
     * 
     * @param territory
     */
    public void removeTerritory(Territory t) {
        Territories.remove(t);
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
     * get conn of the player
     * 
     * @return conn
     */
    public Conn getConn() {
        return this.conn;
    }

    /**
     * Generate new Units on each territory each round. If special skill is on,
     * generate
     * 2 units on each territory
     */
    public void generateNewUnit() {
        if (this.moreUnitSwitch) {
            for (Territory t : Territories) {
                Unit newUnit1 = new AdvancedUnit(this, t, false, numUnits++);
                Unit newUnit2 = new AdvancedUnit(this, t, false, numUnits++);
                ArrayList<Unit> newGenerated = new ArrayList<>();
                newGenerated.add(newUnit1);
                newGenerated.add(newUnit2);
                t.tryAddUnits(newGenerated);// territory add unit
                this.units.add(newUnit1);// player's all units + 1 * num(territroy)
                this.units.add(newUnit2);// player's all units + 1 * num(territroy)
            }
        } else {
            for (Territory t : Territories) {
                Unit newUnit = new AdvancedUnit(this, t, false, numUnits++);
                ArrayList<Unit> newGenerated = new ArrayList<>();
                newGenerated.add(newUnit);
                t.tryAddUnits(newGenerated);// territory add unit
                this.units.add(newUnit);// player's all units + 1 * num(territroy)
            }
        }

    }

    /**
     * Check if the player has lost the game
     * 
     * @return true if the player has lost the game
     */
    public boolean checkLose() {
        if (Territories.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * set the player to be a watcher
     */
    public void setWatch() {
        this.isWatch = true;
    }

    /**
     * get if player's watching
     * 
     * @return isWatch
     */
    public boolean getisWatch() {
        return this.isWatch;
    }

    /**
     * get the player's food resource
     * 
     * @return foodResource
     */
    public int getFoodResource() {
        return this.foodResource;
    }

    /**
     * get the player's tech resource
     * 
     * @return techResource
     */
    public int getTechResource() {
        return this.techResource;
    }

    /**
     * remove the player's food resource(both positive and negative)
     */
    public void removeFoodResource(int amount) {
        this.foodResource -= amount;
    }

    /**
     * remove the player's tech resource(both positive and negative)
     */
    public void removeTechResource(int amount) {
        this.techResource -= amount;
    }

    /**
     * add the player's food+tech resources per turn
     * depend on special ability status, 2* or normal
     */
    public void newResourcePerTurn() {
        if (this.doubleResourceSwitch) {
            for (int i = 0; i < Territories.size(); i++) {
                this.foodResource += Territories.get(i).getFoodRate() * 2;
                this.techResource += Territories.get(i).getTechRate() * 2;
            }
        } else {
            for (int i = 0; i < Territories.size(); i++) {
                this.foodResource += Territories.get(i).getFoodRate();
                this.techResource += Territories.get(i).getTechRate();
            }
        }
    }

    /**
     * display the player's food resource + tech resource
     * 
     * @return the string
     */
    public String displayResourceLevel() {
        return "(Food: " + this.foodResource + " Tech: " + this.techResource + " Tech Level: " + this.techLevel + ")";
    }

    /**
     * update the player's tech level
     */
    public void updateTechLevel() {
        if (this.techLevel == 5 && this.delayedTech == 0) {
            this.delayedTech = 1;
        } else if (this.techLevel < 5) {
            this.techLevel++;
        }
    }

    /**
     * get the player's tech level
     * 
     * @return techLevel
     */
    public int getTechLevel() {
        return this.techLevel;
    }

    /**
     * reset the player's delayed tech
     */
    public void resetDelay() {
        if (this.delayedTech == 1) {
            this.techLevel++;
            this.delayedTech = 0;
        }
    }

    /**
     * get the player's delayed tech
     * 
     * @return delayedTech
     */
    public int getDelayedTech() {
        return this.delayedTech;
    }

    // /*
    // * get ally's territory that distance is minimum
    // */
    // public Territory getAllyTerritoryMinDistance(Territory t) {
    // Territory minDistanceTerritory = null;
    // int min = INFINITY;
    // for (Territory ally : t.getOwner().getTerritories()) {
    // if (t.getDistanceTo(ally) < min) {
    // min = t.getDistanceTo(ally);
    // minDistanceTerritory = ally;
    // }
    // }
    // return minDistanceTerritory;
    // }
}
