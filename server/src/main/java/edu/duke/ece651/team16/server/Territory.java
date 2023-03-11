package edu.duke.ece651.team16.server;
import java.util.ArrayList;

public class Territory {
  private String name;
  private ArrayList<Territory> neighbors;
  private Player owner;

  /**
   * init Territory
   * @param name
   */
    public Territory(String name) {
        this.name = name;
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
     * add neighbor
     * 
     * @param neighbor
     */
    public void setNeighbor(Territory neighbor) {
      neighbors.add(neighbor);
    }

    /**
    * get neighbor
    * @param neighbor
    * @return neighbors
    */
    public ArrayList<Territory> getNeighbors() {
        return neighbors;
    }
}
