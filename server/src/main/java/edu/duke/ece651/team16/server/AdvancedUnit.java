package edu.duke.ece651.team16.server;

import java.util.HashMap;

public class AdvancedUnit extends BasicUnit {
    private int bonus;
    private int level;
    private String name;
    public static HashMap<Integer, String> upgradeName;
    public static HashMap<Integer, Integer> levelBonusTable;

    /*
     * Constructor for AdvancedUnit
     * 
     * @param owner the owner of the unit
     * 
     * @param where the territory where the unit is
     * 
     * @param isAttacker whether the unit is an attacker
     */
    public AdvancedUnit(Player owner, Territory where, boolean isAttacker, int id) {
        super(owner, where, isAttacker, id);
        this.bonus = 0;
        this.level = 0;
        this.name = "First-year";
        upgradeName = new HashMap<Integer, String>();
        levelBonusTable = new HashMap<Integer, Integer>();
        initializeBonusTable();
        initializeNameTable();
    }

    /*
     * initialize the name table
     */
    private void initializeNameTable() {
        // level, name
        upgradeName.put(0, "First-Year");
        upgradeName.put(1, "Second-Year");
        upgradeName.put(2, "Third-Year");
        upgradeName.put(3, "Fourth-Year");
        upgradeName.put(4, "Fifth-Year");
        upgradeName.put(5, "Six-Year");
        upgradeName.put(6, "Seventh-year");
    }

    /*
     * initialize the bonus table
     */
    private void initializeBonusTable() {
        // level, bonus
        levelBonusTable.put(0, 0);
        levelBonusTable.put(1, 1);
        levelBonusTable.put(2, 3);
        levelBonusTable.put(3, 5);
        levelBonusTable.put(4, 8);
        levelBonusTable.put(5, 11);
        levelBonusTable.put(6, 15);
    }

    /*
     * assume that the player has enough tech resource
     * 
     * @param upgradeAmount the amount of upgrade
     */
    @Override
    public void upgrade(int upgradeAmount) {
        this.level += upgradeAmount;
        this.bonus = levelBonusTable.get(this.level);
        this.name = upgradeName.get(this.level);
    }

    /*
     * get the bonus of the unit
     * 
     * @return the bonus of the unit
     */
    @Override
    public int getBonus() {
        return this.bonus;
    }

    /*
     * get the level of the unit
     * 
     * @return the level of the unit
     */
    @Override
    public int getLevel() {
        return this.level;
    }

    /*
     * set the bonus of the unit
     * 
     * @param bonus the bonus of the unit
     */
    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    /*
     * set the level of the unit
     * 
     * @param level the level of the unit
     */

    public void setLevel(int level) {
        this.level = level;
    }

    /*
     * get the name of the unit
     * 
     * @return the name of the unit
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * set the name of the unit
     * 
     * @param name the name of the unit
     */
    public void setName(String name) {
        this.name = name;
    }

}
