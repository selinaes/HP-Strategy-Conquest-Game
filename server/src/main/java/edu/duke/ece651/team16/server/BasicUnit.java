package edu.duke.ece651.team16.server;

public class BasicUnit implements Unit {
    private Player owner;
    private Territory where;
    private boolean isAttacker;
    private int id;
    private boolean isAlive;

    public BasicUnit(Player owner, Territory where, boolean isAttacker, int id) {
        this.owner = owner;
        this.where = where;
        this.isAttacker = isAttacker;
        this.id = id;
        this.isAlive = true;
    }

    /*
     * Set the territory where the unit is located
     */
    public void setwhere(Territory where) {
        this.where = where;
    }

    /*
     * Get the territory where the unit is located
     */
    public Territory getwhere() {
        return this.where;
    }

    /*
     * Get the owner of the unit
     */
    public Player getOwner() {
        return this.owner;
    }

    /*
     * Get alive status of the unit
     */
    public boolean getAlive() {
        return this.isAlive;
    }

    /*
     * Set attacker status of the unit
     */
    public boolean getisAttacker() {
        return this.isAttacker;
    }

    /*
     * Set the attack status of the unit
     */
    public void setisAttacker() {
        this.isAttacker = true;
    }

    /*
     * Set the unit's alive status
     */
    public void setDead() {
        this.isAlive = false;
    }
}