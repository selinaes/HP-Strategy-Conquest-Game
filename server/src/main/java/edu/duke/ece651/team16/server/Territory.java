package edu.duke.ece651.team16.server;

import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Territory {
  private String name;
  private List<Territory> neighbors;
  private Player owner;
  private ArrayList<Unit> units;
  private Battle battle;

  /**
   * constructs Territory class with name
   * 
   * @param name
   */
  public Territory(String name) {
    this.name = name;
    this.neighbors = new ArrayList<>();
    this.units = new ArrayList<>();
    this.battle = new Battle();
  }

  /*
   * Return the units in the territory
   */

  public ArrayList<Unit> getUnits() {
    return this.units;
  }

  /**
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
    System.out.println("Player " + owner.getColor() + "'s unit size is " + this.units.size());
    if (this.units.size() > 0 && !this.battle.checkGroupExisted(units)) {
      battle.addGroup(units);
    }
  }

  /**
   * Resolve battle phase for one territory
   */
  public String doBattle() {
    defendHome();
    String prelog = battle.GameLog();
    Player winner = battle.resolveBattle();
    // String postlog = battle.GameLog();
    String gameLog = "Battle participants: " + prelog + "\nWinner: " + winner.getColor() + "\n";
    List<Territory> to_add = new ArrayList<>();
    to_add.add(this);
    if (!winner.equals(this.owner)) {
      this.owner.removeTerritory(this);
      winner.addTerritories(to_add);
    }
    this.owner = winner;
    this.units = battle.getParties().get(0);
    System.out.println("After battle " + name + "'s units size is " + units.size());

    return gameLog;
  }

  // public void sendLog(Player player, HashMap<String, String> to_send)
  // throws JsonProcessingException {
  // ObjectMapper objectMapper = new ObjectMapper();
  // // convert the HashMap to a JSON object
  // String jsonString = objectMapper.writeValueAsString(to_send);
  // player.getConnection().send(jsonString);
  // }

  /**
   * // Return units that belong to a certain player and alive
   */
  public ArrayList<Unit> getAliveUnitsFor(Player player) {
    ArrayList<Unit> result = new ArrayList<>();
    for (Unit u : this.units) {
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
    // System.out.println("try add units");
    // System.out.println("Territory: " + unit.getwhere().getName());
    // System.out.println("Owner: " + unit.getOwner());
    for (Unit u : units) {
      u.setwhere(this);
      this.units.add(u);
    }
    // unit.setwhere(this);
    // this.units.add(unit);
    // if (unit.getwhere() == null && this.owner == unit.getOwner()){

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
  public ArrayList<Unit> tryRemoveUnits(int num, Player player) {
    ArrayList<Unit> result = new ArrayList<>();
    ArrayList<Unit> aliveUnitForP = this.getAliveUnitsFor(player);
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
   */
  public String getUnitsString() {
    String result = Integer.toString(this.units.size()) + " units";
    return result;
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
      result += t.getName() + ", ";
    }
    result = result.substring(0, result.length() - 2);
    result += ")";
    return result;
  }

  /**
   * set neighbors for both territories
   * 
   * @param neighbors to be set
   */
  public void setNeighbors(List<Territory> neighbors) {
    this.neighbors = new ArrayList<>(neighbors);
    for (Territory t : neighbors) {
      t.addNeighbor(this);
    }
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

}
