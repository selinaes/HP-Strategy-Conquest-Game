package edu.duke.ece651.team16.server;

public abstract class BasicUnit implements Unit {
    private Player owner;
    private Territory where;
    private boolean isAttacker;
    private int id;
    private boolean isAlive;

    /*
     * Constructor for BasicUnit
     * @param owner the owner of the unit
     * @param where the territory where the unit is located
     * @param isAttacker the attack status of the unit
     * @param id the id of the unit
     */
    public BasicUnit(Player owner, Territory where, boolean isAttacker, int id) {
        this.owner = owner;
        this.where = where;
        this.isAttacker = isAttacker;
        this.id = id;
        this.isAlive = true;
    }

    /*
     * Set the territory where the unit is located
     * @param where the territory where the unit is located
     */
    public void setwhere(Territory where) {
        this.where = where;
    }

    /*
     * Get the territory where the unit is located
     * @return the territory where the unit is located
     */
    public Territory getwhere() {
        return this.where;
    }

    /*
     * Get the owner of the unit
     * @return the owner of the unit
     */
    public Player getOwner() {
        return this.owner;
    }

    /*
     * Get alive status of the unit
     * @return isAlive of the unit
     */
    public boolean getAlive() {
        return this.isAlive;
    }

    /*
     * Get attacker status of the unit
     * @return isAttacker of the unit
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

    /*
     * Get the id of the unit
     * @return the id of the unit
     */
    public int getId() {
        return id;
    }

    /*
     * Get the name of the unit
     * @return the name of the unit
     */
    public String getName() {
        return "BasicUnit";
    }

    /*
     * Get the level of the unit
     * @return the level of the unit
     */
    public int getLevel() {
        return 0;
    }

    /*
     * Get the attack power of the unit
     * @return the attack power of the unit
     */
    public int getBonus() {
        return 0;
    }

    /*
     * upgrade the unit(do nothing)
     * @param amount the amount of upgrade
     */
    public void upgrade(int amount) {
        return;
    }
}