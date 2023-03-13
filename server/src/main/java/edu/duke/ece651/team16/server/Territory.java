package edu.duke.ece651.team16.server;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

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

  // Overwrite equals
  @Override
  public boolean equals(Object o) {
    if (o != null && o.getClass().equals(getClass())) {
      Territory t = (Territory) o;
      // boolean nameEqual = t.getName().equals(this.getName());
      return this.name.equals(t.name) && this.neighbors.size() == t.neighbors.size()
          && this.getNeighborsNames().equals(t.getNeighborsNames());
    }
    return false;
  }

  @Override
  public String toString() {
    return "Territory{name='" + name + "'}";
  }

  // @Override
  // public boolean equals(Object p) {
  // // requires that o has *exactly* the same class as "this" object
  // if (p.getClass().equals(getClass())) {
  // Territory c = (Territory) p;
  // if (name.equals(c.name)) {
  // return neighbors == c.neighbors;
  // }
  // return false;
  // }
  // return false;
  // }

  // @Override
  // public int hashCode() {
  // return Objects.hash(name, neighbors);
  // }

}
