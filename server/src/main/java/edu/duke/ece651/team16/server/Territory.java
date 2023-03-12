package edu.duke.ece651.team16.server;
import java.util.*;

public class Territory {
  private String name;
  private List<Territory> neighbors;
  private Player owner;

  /**
   * init Territory
   * @param name
   */
    public Territory(String name) {
        this.name = name;
        this.neighbors = new ArrayList<Territory>();
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
   * get terrtory name
   */
    public String getName() {
        return name;
    }
    

    /**
    * get neighbor
    * @param neighbor
    * @return neighbors
    */
    public List<Territory> getNeighbors() {
        return neighbors;
    }

    /**
    *Get Neighbor Name
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
     * set neighbors
     * 
     * @param neighbors
     */
    public void setNeighbors(List<Territory> neighbors) {
      this.neighbors = neighbors;
      for (Territory t : neighbors) {
        t.addNeighbor(this);
      }
    }

    /**
     * add neighbor
     * 
     * @param neighbor
     */
    public void addNeighbor(Territory neighbor) {
      if (!neighbors.contains(neighbor)) {
        neighbors.add(neighbor);
      }
    }

}
