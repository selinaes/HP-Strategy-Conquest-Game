package edu.duke.ece651.team16.server;

public class AllianceOrder implements Order {
    protected Player player;
    private Player ally;

    /**
     * Constructor for AllianceOrder
     * 
     * @param player the player
     */
    public AllianceOrder(Player player, Player ally) {
        this.player = player;
        this.ally = ally;
    }

    /**
     * Execute the order
     * 
     * @return null
     */
    @Override
    public String tryAction() {
        AllianceRuleChecker checker = new AllianceRuleChecker();
        String result = checker.checkMyRule(player, ally);
        // if has pending ally, and pending ally is the same as player. form allicance
        // successfully
        if (result != null)
            return result;
        if (ally.getPendingAlly() != null && ally.getPendingAlly().getColor().equals(player.getColor())) {
            player.setPendingAlly(null);
            player.setAlly(ally);
            ally.setPendingAlly(null);
            ally.setAlly(player);
            return null;
        }
        player.setPendingAlly(ally);
        result = "Waiting for Alliance";
        return result;

    }

}
