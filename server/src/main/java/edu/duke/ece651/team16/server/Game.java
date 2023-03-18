package edu.duke.ece651.team16.server;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;

public class Game {
    protected List<Player> players;
    private int numPlayer;
    private List<String> colors;
    private GameMap defaultMap;
    private List<Conn> allConnections;
    private String gameState; // setNumPlayer, setPlayerColor, setUnits, placementEnd, worldWar, warEnd
    private int readyPlayer;
    private int unitsPerPlayer;
    private int playerLowBound;
    private int playerHighBound;
    private int gameRound;
    private MessageGenerator messageGenerator;

    /**
     * Constructor for Server class that takes in a serverSocket
     * 
     * @param ServerSocket serverSocket
     */
    public Game(int unitsPerPlayer) {
        this.numPlayer = 4;
        this.players = new ArrayList<Player>();
        this.defaultMap = new GameMap(numPlayer);
        defaultMap.createBasicMap();
        this.colors = defaultMap.getColorList();
        this.allConnections = new ArrayList<Conn>();
        this.gameState = "setNumPlayer";
        this.unitsPerPlayer = unitsPerPlayer;
        this.readyPlayer = 0;
        this.playerLowBound = 2;
        this.playerHighBound = 4;
        this.gameRound = 0;
        this.messageGenerator = new MessageGenerator();
    }

    /**
     * the flow of the game, with diffeeent phases
     * 
     * @param ServerSocket serverSocket: the server socket
     * @param int          numClients: the number of clients
     */
    public void gameFlow(Socket client_socket, int numClients) {
        Player p = doPlacementPhase(client_socket, numClients);
        synchronized (this) {
            this.readyPlayer = 0;
        }
        while (findWinner() == null) {
            if (!players.contains(p)) {
                return; // player exit
            }
            p.getConn().send("Game continues");
            doActionPhase(p);

        }
        Player winner = findWinner();
        // notify this player of winner!
        p.getConn().send(winner.getColor());
        // doActionPhase(p);
    }

    /**
     * 
     * the action phase of the game
     *
     * @param Player p: the player
     */
    public void doActionPhase(Player p) {
        if (p.getisWatch()) {
            // send done
            p.getConn().send("watching");
            notifyAllPlayers(p.getConn(), "worldWar");
        } else if (p.checkLose()) {
            p.getConn().send("Choose watch");
            if (ifChooseWatch(p.getConn()).equals("e")) {
                synchronized (this) {
                    players.remove(p);
                    --numPlayer;
                }
                return;
            } else {
                p.setWatch();
                notifyAllPlayers(p.getConn(), "worldWar");
            }
        } else {
            p.getConn().send("do nothing");
            doAction(p);
        }

        while (true) {
            synchronized (this) {
                if (gameState.equals("worldWar")) {
                    ++readyPlayer;
                    break;
                }
            }
        }
        HashMap<String, String> to_send_log = new HashMap<>();
        // if all players are ready, then execute world war (the last player who reached
        // here)
        synchronized (this) {
            if (readyPlayer == numPlayer) {
                to_send_log = worldwar();
                for (Player player : players) {
                    messageGenerator.sendLog(player, to_send_log);
                }
                this.gameState = "warEnd";
                this.gameRound++;
                this.readyPlayer = 0;

            }
        }

        while (true) {
            synchronized (this) {
                if (gameState.equals("warEnd")) {
                    break;
                }
            }
        }
        HashMap<String, ArrayList<HashMap<String, String>>> to_send1 = messageGenerator.formMap(this.players);
        messageGenerator.sendMap(p, to_send1);

    }

    /**
     * Create a player and add to the server
     * 
     * @param Socket client_socket: client socket
     * @param int    numClients: number of clients
     */
    public Player doPlacementPhase(Socket client_socket, int numClients) {
        Conn conn = new Conn(client_socket);
        allConnections.add(conn);
        chooseNumOfPlayers(conn, numClients);// gameState = setNumPlayer
        while (true) {
            synchronized (this) {
                if (gameState.equals("setPlayerColor")) {
                    break;
                }
            }
        }

        HashMap<String, ArrayList<HashMap<String, String>>> to_send_initial = messageGenerator
                .formInitialMap(this.defaultMap, this.colors);
        messageGenerator.sendInitialMap(conn, to_send_initial);

        String color = chooseColor(conn);
        Player p = new Player(color, conn, defaultMap.getMap().get(color), this.unitsPerPlayer);
        addPlayer(p);

        assignUnits(p, conn);
        while (true) {
            synchronized (this) {
                if (gameState.equals("placementEnd")) {
                    break;
                }
            }
        }

        HashMap<String, ArrayList<HashMap<String, String>>> to_send = messageGenerator.formMap(this.players);
        messageGenerator.sendMap(p, to_send);
        return p;
    }

    /**
     * Add a player to the server
     * 
     * @param Player p
     */
    public void addPlayer(Player p) {
        players.add(p);
    }

    /**
     * Prompt the player to choose a color
     * 
     * @param Conn conn
     * @return string color chosen by player
     **/
    public String chooseColor(Conn conn) {
        String colorList = "";
        for (String color : colors) {
            colorList += color + " ";
        }
        conn.send(
                "Please enter a color you want to choose. Current available colors are: " + colorList);
        String chosencolor = conn.recv();
        chosencolor = chosencolor.toLowerCase();
        int colorindex = colors.indexOf(chosencolor);
        if (colorindex == -1) {
            conn.send("Invalid color");
            return chooseColor(conn);
        } else {
            conn.send("Valid");
            colors.remove(colorindex);
            return chosencolor;
        }
    }

    /**
     * Prompt the lose player to choose watch or exit
     * 
     * @param Conn conn
     * @return string chosen by player
     **/
    public String ifChooseWatch(Conn conn) {
        conn.send("If you want to watch game, Please enter: w; If you want to exit, Please enter: e");
        String chooseWatch = conn.recv();
        chooseWatch = chooseWatch.toLowerCase();
        System.out.println(chooseWatch);
        if (!chooseWatch.equals("w") && !chooseWatch.equals("e")) {
            conn.send("Invalid choice");
            return ifChooseWatch(conn);
        } else {
            conn.send("Valid");
            return chooseWatch;
        }
    }

    /**
     * prompt the player to choose number of players
     * 
     * @param Conn conn
     * @param int  numClients
     */
    public void chooseNumOfPlayers(Conn conn, int numClients) {
        // synchronized (this) {
        // this.gameState = "setNumPlayer";
        // }
        if (numClients == 1) {
            conn.send(
                    "You are the first player! Please set the number of players in this game(Valid player number: 2-4): ");
            String num = conn.recv();
            try {
                int numOfPlayers = Integer.parseInt(num);
                if (numOfPlayers < this.playerLowBound || numOfPlayers > this.playerHighBound) {
                    conn.send("Invalid number of players");
                    chooseNumOfPlayers(conn, numClients);
                    return;
                }
                conn.send("Valid");
                this.numPlayer = numOfPlayers;
            } catch (NumberFormatException e) {
                conn.send("Invalid number of players");
                chooseNumOfPlayers(conn, numClients);
                return;
            }

            for (Conn c : allConnections) {
                c.send("stage Complete");
            }
            synchronized (this) {
                this.gameState = "setPlayerColor"; // now out of setNumPlayer stage
                initializeMap(this.numPlayer);
            }
        } else {
            conn.send("Not the first player. Please wait for the first player to set player number.");
        }
    }

    /**
     * Count the number of players
     * 
     * @return int number of players
     */
    public int getNumPlayer() {
        return this.numPlayer;
    }

    /**
     * create new map and color list
     * 
     * @param int numOfPlayers
     */
    public void initializeMap(int numOfPlayers) {
        this.defaultMap = new GameMap(numOfPlayers);
        // defaultMap.createDukeMap();
        defaultMap.createTestMap();
        this.colors = defaultMap.getColorList();
    }

    /**
     * Prompt the player to assign all units to territories
     * 
     * @param Player p
     * @param Conn   conn
     */
    public void assignUnits(Player p, Conn conn) {
        while (p.unplacedUnits() > 0) {
            promptTerritory(p, conn);
            String territoryName = conn.recv();
            if (territoryName.equals("done")) {
                break;
            }
            if (!isValidTerritory(p, territoryName, conn)) {
                return;
            }

            promptUnitNumber(p, territoryName, conn);
            String num = conn.recv();
            if (num.equals("done")) {
                break;
            }
            if (!isValidUnitNumber(p, territoryName, num, conn)) {
                return;
            }

            int numOfUnits = Integer.parseInt(num);
            p.placeUnitsSameTerritory(territoryName, numOfUnits);
        }
        notifyAllPlayers(conn, "placementEnd");
    }

    /**
     * Notify all players that the player has finished assigning units
     * 
     * @param Conn   conn
     * @param String newStage
     */
    public void notifyAllPlayers(Conn conn, String newStage) {
        conn.send("finished stage");
        synchronized (this) {
            ++this.readyPlayer;
            if (readyPlayer == numPlayer) {
                System.out.println("All players ready, readyPlayer: " + readyPlayer + " Entering: "
                        + newStage);
                for (Conn c : allConnections) {
                    c.send("stage Complete");
                }
                this.gameState = newStage;
                this.readyPlayer = 0;
            }
        }
    }

    /**
     * Prompt the player to choose a territory
     * 
     * @param Player p
     * @param Conn   conn
     */
    private void promptTerritory(Player p, Conn conn) {
        String msg_territory = "You have " + Integer.toString(p.unplacedUnits())
                + " units left. If you want to finish placement, enter done. Otherwise, choose a territory to assign units to. Please enter the territory name: ";
        conn.send(msg_territory);
    }

    /**
     * Check if the territory name is valid
     * 
     * @param Player p
     * @param String territoryName
     * @param Conn   conn
     * @return boolean
     */
    private boolean isValidTerritory(Player p, String territoryName, Conn conn) {
        if (!p.getTerritoryNames().contains(territoryName)) {
            conn.send("Invalid territory name");
            assignUnits(p, conn);
            return false;
        }
        conn.send("Valid territory name");
        return true;
    }

    /**
     * Prompt the player to choose a number of units
     * 
     * @param Player p
     * @param String territoryName
     * @param Conn   conn
     */
    private void promptUnitNumber(Player p, String territoryName, Conn conn) {
        String msg_amount = "You have " + Integer.toString(p.unplacedUnits())
                + " units left. If you want to finish placement, enter done. Otherwise, how many units do you want to assign to "
                + territoryName + "? Please enter a number: ";
        conn.send(msg_amount);
    }

    /**
     * Check if the number of units is valid
     * 
     * @param Player p
     * @param String territoryName
     * @param String num
     * @param Conn   conn
     * @return boolean
     */
    private boolean isValidUnitNumber(Player p, String territoryName, String num, Conn conn) {
        try {
            int numOfUnits = Integer.parseInt(num);
            if (numOfUnits < 0 || numOfUnits > p.unplacedUnits()) {
                conn.send("Invalid number of units");
                assignUnits(p, conn);
                return false;
            }
            conn.send("Valid number of units");
            return true;
        } catch (NumberFormatException e) {
            conn.send("Invalid number of units");
            assignUnits(p, conn);
            return false;
        }
    }

    /**
     * set number of players
     * 
     * @param int num
     */
    public void setNumPlayer(int num) {
        this.numPlayer = num;
    }

    /**
     * Do Action phase of the game
     * 
     * @param Player p
     * @return boolean
     */
    public boolean doAction(Player p) {
        // choose action step. Client side checked. No reprompt
        messageGenerator.sendEntry(p);
        String action = p.getConn().recv().toLowerCase();
        // perform action, invalid reprompt
        boolean done = false;
        while (!done) {
            if (action.equals("m") || action.equals("a")) { // move or attack
                doOneAction(p, action);
            } else { // done
                done = true;
                notifyAllPlayers(p.getConn(), "worldWar");
                return done;
            }
            done = doAction(p);
        }
        return done;
    }

    /**
     * ask the player to enter info for action: Territory from, Territory to, number
     * of units(e.g. T1, T2, 2)
     * 
     * @param Player p
     * @param String actionname
     * @return Order
     */
    public Order makeActionOrder(Player p, String actionName) {
        p.getConn().send(
                "Please enter in the following format: Territory from, Territory to, number of units(e.g. T1, T2, 2)");
        String actionInput = p.getConn().recv(); // e.g. T1, T2, 2
        String[] input = actionInput.split(", ");
        // parse actionInput
        String from = input[0];
        String to = input[1];
        int num = Integer.parseInt(input[2]);

        // get Territory
        Territory fromTerritory = checkNameReturnTerritory(from, defaultMap);
        Territory toTerritory = checkNameReturnTerritory(to, defaultMap);

        if (fromTerritory == null || toTerritory == null) {
            p.getConn().send("Invalid Territory Name");
            return makeActionOrder(p, actionName);
        }
        if (actionName.equals("m")) {
            Order order = new MoveOrder(fromTerritory, toTerritory, num, p, defaultMap);
            return order;
        }
        return (new AttackOrder(fromTerritory, toTerritory, num, p, defaultMap));

    }

    /**
     * Do one move in the move phase
     * 
     * @param Player p
     * @param String actionName
     */
    public void doOneAction(Player p, String actionName) {
        Order order = makeActionOrder(p, actionName);
        String tryAction = order.tryAction();
        if (tryAction == null) { // valid
            p.getConn().send("Valid");
        }
        // if invalid, send("reason"), then recurse doOneMove()
        else {
            p.getConn().send(tryAction);
            doOneAction(p, actionName);
            return;
        }
    }

    /**
     * check if one placement rule is valid
     * 
     * @param territory_name the territory name
     * @param map            the map
     * @return null if the placement rule is valid, otherwise return the error
     *         message
     */
    public Territory checkNameReturnTerritory(String territory_name, GameMap map) {
        for (String playercolor : map.getMap().keySet()) {
            for (Territory territory : map.getMap().get(playercolor)) {
                if (territory_name.equals(territory.getName())) {
                    return territory;
                }
            }
        }
        return null;
    }

    /**
     * After one turn of moving and attacking, resolve battle for each territory
     */
    public HashMap<String, String> worldwar() {
        HashMap<String, String> worldLog = new HashMap<>();
        for (Player p : players) {
            List<Territory> territoriesCopy = List.copyOf(p.getTerritories()); // immutable copy. if don't copy,
                                                                               // ConcurrentModificationException
            for (Territory territory : territoriesCopy) {
                if (territory.existsBattle()) {
                    System.out.println("Player " + p.getColor() + " turn and " + territory.getName() + " in battle");
                    String battleLog = territory.doBattle();
                    worldLog.put(territory.getName(), battleLog);
                }
            }
        }
        generateUnit();
        return worldLog;
    }

    public void generateUnit() {
        for (Player p : players) {
            p.generateNewUnit();
        }
    }

    /**
     * Check if the game is over, and find the winner
     * 
     * @return winner if the game is over, otherwise return null
     */
    public Player findWinner() {
        List<Player> playersCopy = new ArrayList<>(players); // mutable copy
        for (Player p : players) {
            if (p.checkLose()) {
                playersCopy.remove(p);
            }
        }
        if (playersCopy.size() == 1) {
            return playersCopy.get(0);
        }
        return null;
    }

}
