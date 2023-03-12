package edu.duke.ece651.team16.server;

import java.util.List;
import java.util.ArrayList;

public class Territory {
  private String name;
  private List<Territory> neighbors;
  private Player owner;

  /**
   * init Territory
   * 
   * @param name
   */
  public Territory(String name) {
    this.name = name;
    this.neighbors = new ArrayList<>();

  }

  /**
   * get owner
   * 
   * @return owner
   */
  public Player getOwner() {
    return owner;
  }

  /**
   * set owner
   * 
   * @param owner
   */
  public void setOwner(Player owner) {
    this.owner = owner;
  }

  /**
   * get terrtory name
   */
  public String getName() {
    return name;
  }

  /**
   * get neighbor
   * 
   * @param neighbor
   * @return neighbors
   */
  public List<Territory> getNeighbors() {
    return this.neighbors;
  }

  /**
   * Get Neighbor Name
   * 
   * @return result
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
   * @param neighbors
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

}
