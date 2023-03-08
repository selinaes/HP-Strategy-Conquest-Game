package edu.duke.ece651.team16.server;
import java.util.ArrayList;

public class Territory {
  private String name;
//   private ArrayList<Territory> neighbors;

  /**
   * init Territory
   * @param name
   */
    public Territory(String name) {
        this.name = name;
    }

  /**
   * get terrtory name
   */
    public String getName() {
        return name;
    }


    // /**
    // * add neighbor
    // * @param neighbor
    // */
    // public ArrayList<Territory> getNeighbors() {
    //     return neighbors;
    // }
}
