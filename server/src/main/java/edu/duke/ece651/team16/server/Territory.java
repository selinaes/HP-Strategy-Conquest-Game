package edu.duke.ece651.team16.server;

import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Territory {
  private String name;
  private List<Territory> neighbors;
  private Player owner;
  private ArrayList<Unit> units;

  /**
   * constructs Territory class with name
   * 
   * @param name
   */
  public Territory(String name) {
    this.name = name;
    this.neighbors = new ArrayList<>();
    this.units = new ArrayList<>();
  }

  /*
   * Return the units in the territory
   */

  public ArrayList<Unit> getUnits() {
    return this.units;
  }

  /*
   * Add units to the territory in the placement
   * The input unit's owner name is ensured to be the same as territory's owner
   * name
   * The input unit's where is ensured to be null because it is in the placement
   * session
   * 
   * @param unit
   */
  public void tryAddUnits(Unit... units) {
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
