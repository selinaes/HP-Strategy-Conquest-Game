package edu.duke.ece651.team16.server;

public interface Unit {
    /*
     * Set the territory where the unit is located
     */
    public void setwhere(Territory where);

    /**
     * Get the territory where the unit is located
     *
     * @return get Territory
     */
    public Territory getwhere();

    /**
     * Get the owner of the unit
     *
     * @return Player player
     */
    public Player getOwner();

    /**
     * Get the alive status of the unit
     * 
     * * @return boolean isAlive
     */
    public boolean getAlive();

    /**
     * Get the attack status of the unit
     * 
     * @return boolean isAttacker
     */
    public boolean getisAttacker();

    /**
     * Set the attack status of the unit
     */
    public void setisAttacker();

    /**
     * Set the unit's alive status
     */
    public void setDead();

    /**
     * get unit id
     */
    public int getId();

    /**
     * get unit type
     */
    public String getName();

    /**
     * get unit level
     */
    public int getLevel();

    /**
     * get unit bonus
     */
    public int getBonus();

    /**
     * upgrade
     */
    public void upgrade(int upgradeAmount);

}