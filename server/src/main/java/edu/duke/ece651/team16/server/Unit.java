package edu.duke.ece651.team16.server;

public interface Unit {
    /*
     * Set the territory where the unit is located
     */
    public void setwhere(Territory where);

    /*
     * Get the territory where the unit is located
     */
    public Territory getwhere();

    /*
     * Get the owner of the unit
     */
    public Player getOwner();

    /*
     * Get the alive status of the unit
     */
    public boolean getAlive();
}