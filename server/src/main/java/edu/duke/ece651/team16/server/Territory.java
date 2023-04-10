package edu.duke.ece651.team16.server;

import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Territory {
  private String name;
  private List<Territory> neighbors;
  private HashMap<Territory, Integer> neighborDistance;
  private Player owner;
  private ArrayList<Unit> units;
  private Battle battle;
  private int foodRate; // how much food to produce each round in this territory
  private int techRate; // how much tech to produce each round in this territory

  /**
   * constructs Territory class with name
   * 
   * @param name
   */
  public Territory(String name) {
    this.name = name;
    this.neighbors = new ArrayList<>();
    this.neighborDistance = new HashMap<>();
    this.units = new ArrayList<>();
    this.battle = new Battle();
    this.foodRate = 5;
    this.techRate = 5;
  }

  /**
   * set food rate
   * 
   * @param foodRate
   */
  public void setFoodRate(int foodRate) {
    this.foodRate = foodRate;
  }

  /**
   * set tech rate
   * 
   * @param techRate
   */
  public void setTechRate(int techRate) {
    this.techRate = techRate;
  }

  /**
   * get food rate
   * 
   * @return foodRate
   */
  public int getFoodRate() {
    return this.foodRate;
  }

  /**
   * get tech rate
   * 
   * @return techRate
   */
  public int getTechRate() {
    return this.techRate;
  }

  /*
   * Return the units in the territory
   * 
   * @return units
   */

  public ArrayList<Unit> getUnits() {
    return this.units;
  }

  /**
   * check if battle exists
   * 
   * @return if battle exists
   */
  public boolean existsBattle() {
    if (battle.getParties().size() > 0) {
      return true;
    }
    return false;
  }

  /**
   * If there is a battle, add units to the battle to defend the territory
   */
  public void defendHome() {
    if (this.units.size() > 0 && !this.battle.checkGroupExisted(units)) {
      battle.addGroup(units);
    }
  }

  /**
   * Resolve battle phase for one territory
   * 
   * @return game log
   */
  public String doBattle() {
    defendHome();
    String prelog = battle.GameLog();
    Player winner = battle.resolveBattle();
    String gameLog = "Battle participants: " + prelog + "\nBattle Winner: " + winner.getColor() + "\n";
    List<Territory> to_add = new ArrayList<>();
    to_add.add(this);
    if (!winner.equals(this.owner)) {
      this.owner.removeTerritory(this);
      winner.addTerritories(to_add);
    }
    this.owner = winner;
    this.units = battle.getParties().get(0);
    battle.clearParty();
    return gameLog;
  }

  /**
   * Return units that are certain level belong to a certain player and alive
   * 
   * @param player
   * @return units
   */
  public ArrayList<Unit> getAliveUnitsFor(Player player, int level) {
    ArrayList<Unit> result = new ArrayList<>();
    for (Unit u : this.getUnitsByLevel(level)) {
      if (u.getOwner() == player && u.getAlive() == true) {
        result.add(u);
      }
    }
    return result;
  }

  /**
   * Add units to the territory in the placement
   * The input unit's owner name is ensured to be the same as territory's owner
   * name
   * The input unit's where is ensured to be null because it is in the placement
   * session
   * 
   * @param unit
   */
  public void tryAddUnits(ArrayList<Unit> units) {
    for (Unit u : units) {
      u.setwhere(this);
      this.units.add(u);
    }
  }

  /**
   * Add units to the territory in the battle
   */
  public void tryAddAttackers(ArrayList<Unit> units) {
    if (units.size() > 0) {
      this.battle.addGroup(units);
    }
  }

  /**
   * Remove units from the territory
   * 
   * @param num    number of units to be removed
   * @param player the player who owns the units
   * @return the removed units
   */
  public ArrayList<Unit> tryRemoveUnits(int num, Player player, int level) {
    ArrayList<Unit> result = new ArrayList<>();
    ArrayList<Unit> aliveUnitForP = this.getAliveUnitsFor(player, level);
    for (int i = 0; i < num; i++) {
      Unit u = aliveUnitForP.get(i);
      u.setwhere(null);
      result.add(u);
      this.units.remove(u);
    }
    return result;
  }

  /*
   * Get the number of units in the territory
   * 
   * @return units in string
   */
  public String getUnitsString() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < 7; i++) {
      result.append(String.valueOf(this.getUnitsByLevel(i).size()) + ",");
    }
    return result.toString();
  }

  /**
   * get owner
   * 
   * @return owner of the territory
   */
  public Player getOwner() {
    return owner;
  }

  /**
   * set owner
   * 
   * @param owner of the territory
   */
  public void setOwner(Player owner) {
    this.owner = owner;
  }

  /**
   * get terrtory name
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * get neighbor
   * 
   * @return neighbors
   */
  public List<Territory> getNeighbors() {
    return this.neighbors;
  }

  /**
   * Get Neighbor Names
   * 
   * @return formatted neighbor names
   */
  public String getNeighborsNames() {
    String result = "";
    result += "(next to: ";
    for (Territory t : neighbors) {
      result += t.getName() + ": ";
      result += neighborDistance.get(t) + ", ";
    }
    result = result.substring(0, result.length() - 2);
    result += ")";

    return result;
  }

  /**
   * Get Display info about food and tech rate
   * 
   * @return to_display
   */
  public String territoryInfo() {
    String result = "";
    result += "Food Rate: " + this.foodRate + ", Tech Rate: "
        + this.techRate + " ";
    return result;
  }

  /**
   * set neighbors for both territories
   * 
   * @param neighbors to be set
   */
  public void setNeighbors(List<Territory> neighbors) {
    for (Territory t : neighbors) {
      addNeighbor(t);
      t.addNeighbor(this);
    }
  }

  /*
   * set distance for both territories
   */
  public void setDistance(List<Territory> neighbors, List<Integer> distance) {
    for (int i = 0; i < neighbors.size(); i++) {
      neighborDistance.put(neighbors.get(i), distance.get(i));
      neighbors.get(i).getDistanceMap().put(this, distance.get(i));
    }
  }

  /*
   * get distance map
   */
  public HashMap<Territory, Integer> getDistanceMap() {
    return neighborDistance;
  }

  public int getDistance(Territory neighbor) {
    return neighborDistance.get(neighbor);
  }

  /**
   * add neighbor
   * 
   * @param neighbor
   */
  private void addNeighbor(Territory neighbor) {
    if (!this.neighbors.contains(neighbor)) {
      this.neighbors.add(neighbor);
    }
  }

  /**
   * Overwrite equals
   * 
   * @param o
   * @return boolean value
   */
  @Override
  public boolean equals(Object o) {
    if (o != null && o.getClass().equals(getClass())) {
      Territory t = (Territory) o;
      return this.name.equals(t.name) && this.neighbors.size() == t.neighbors.size()
          && this.getNeighborsNames().equals(t.getNeighborsNames());
    }
    return false;
  }

  /**
   * Overwrite toString
   * 
   * @return String
   */
  @Override
  public String toString() {
    return "Territory{name='" + name + "'}";
  }

  /**
   * get units by level
   * 
   * @return units
   */
  public ArrayList<Unit> getUnitsByLevel(int level) {
    ArrayList<Unit> result = new ArrayList<>();
    for (Unit u : this.units) {
      if (u.getLevel() == level) {
        result.add(u);
      }
    }
    return result;
  }
}
