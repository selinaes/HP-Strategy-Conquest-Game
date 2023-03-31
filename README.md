# Risk_Game

![pipeline](https://gitlab.oit.duke.edu/zf70/Risk_Game/badges/main/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/zf70/Risk_Game/badges/main/coverage.svg?job=test)

## Coverage
[Detailed coverage](https://zf70.pages.oit.duke.edu/Risk_Game/dashboard.html)

# Table of Contents
- [Schedule](#Schedule)
- [Review](#Review)
  - [Requirements](#Requirements)
- [Design Overview](#Design-Overview)
  - [UML Diagram](#UML-Diagram)
  - [UI Design](#UI-Design)
- [Implementation](#Implementation)

## Schedule
* Evolution 1: 2/24 - 3/24
* Evolution 2: 3/24 - 4/10
* Evoluiton 3: 4/10 - 4/28

[Project Mamagement Sheet](https://docs.google.com/spreadsheets/d/1B2cUguqBPXm3IJBRU1WRPnoBng4p8AiTouAkxmXUhYg/edit#gid=0)

## Review

### Requirements
See requirements in [prj1.pdf](prj1.pdf)

## Design Overview

MoveInputChecker and AttackInputChecker should:
- check units > 0 
- check source territory is yours.
- check destinination territory is yours.

### UML Diagram
```mermaid
classDiagram
    ServerApp "1" --* "1" Server
    ClientApp "1" --* "N" Client
    Client "1" --* "1" Views
    Server "1" --* "1" Game
    Game "1" --* "N" Player
    Game "1" --* "N" Order
    Game "1" --* "1" MessageGenerator
    Game "1" --* "1" GameMap
    Game "1" --* "N" Conn 
    Player "1" --> "1" Conn
    Player "1"--> "N" Territory
    Player "1" --* "1" AssignUnitRuleChecker
    Player "1" --* "N" Unit
    Territory "1"--> "N" Unit
    Unit <|.. BasicUnit
    AttackOrder --* OrderRuleChecker
    MoveOrder --* OrderRuleChecker

    
    AttackOrder --|> Order
    MoveOrder --|> Order

    Territory "1" --* "0..1" Battle
    Battle --* Combat
    
    OrderRuleChecker <|-- MoveInputRuleChecker
    OrderRuleChecker <|-- MovePathRuleChecker
    OrderRuleChecker <|-- AttackInputRuleChecker
    OrderRuleChecker <|-- AttackAdjacentRuleChecker



class ClientApp {
    + main(String[] args): void
}

class Client {
    - BufferedReader socketReceive
    - PrintWriter socketSend
    - Views view
    - PrintStream out
    - BufferedReader inputReader
    - boolean ifExit
    + run(): void
    + ifExit(): boolean
    + runWatchOption(): void
    + recvMsg(): String
    + readClientInput(String prompt): String
    + sendResponse(String response): void
    + waitEveryoneDone(): void
    + playerChooseColor(): void
    + playerChooseNum(): void
    + playerAssignUnit(): boolean
    + playerAssignUnits(): void
    + playerActionTurn(): void
    + playerOneAction(): void
    + checkMoveInputFormat(String clientInput): boolean
    + playerChooseWatch(): void
}



class Views {
    - PrintStream out
    - boolean watch
    - ObjectMapper objectMapper
    + setWatch(): void
    + isWatch(): boolean
    + displayInitialMap(String jsonString): void
    + displayEntry(String to_display): String
    + displayMap(String jsonString): void
    + displayLog(String jsonString): void
    + playerWatchTurn(): void
}


class ServerApp {
    - int port
    - ServerSocket listenSocket
    - Server server
    + createServerSocket(): void
    + createServer(): void
    + runServer(): void
    + main(String[] args): void
}




class Server {
    - ServerSocket listenSocket
    - ThreadPoolExecutor threadPool
    - Game game
    - int numClients
    + Server(ServerSocket: serverSocket)
    + acceptOrNull(): Socket
    + run(): void
}

class MessageGenerator {
    - ObjectMapper objectMapper
    + formEntry(Player p): HashMap<String, String>
    + sendEntry(Player p): void
    + sendLog(Player p, HashMap<String, String> to_send): void
    + sendInitialMap(Conn conn, HashMap<String, ArrayList<HashMap<String, String>>> to_send: void
    + sendMap(Player player, HashMap<String, ArrayList<HashMap<String, String>>> to_send): void
    + formInitialMap(GameMap currentMap, List<String> colors): HashMap<String, ArrayList<HashMap<String, String>>>
    + formMap(List<Player> players): HashMap<String, ArrayList<HashMap<String, String>>>
}

class GameMap{
    - int numPlayer
    - ArrayList<String> ColorList
    - HashMap<String, List<Territory>> map
    + getTerritoryList(): ArrayList<Territory>
    + createBasicMap(): HashMap<String, List<Territory>>
    + createDukeMap(): HashMap<String, List<Territory>>
    + createTestMap(): HashMap<String, List<Territory>>
    + createTest3Map(): HashMap<String, List<Territory>>
    + getColorList(): ArrayList<String>
    + getMap(): HashMap<String, List<Territory>>
    + setMap(HashMap<String, List<Territory>> map): void
}

class Game {
    - List<Player> players
    - int numPlayer
    - List<String> colors
    - GameMap currentMap
    - List<Conn> allConnections
    - String gameState
    - int readyPlayer
    - int unitsPerPlayer 
    - int playerLowBound
    - int playerHighBound
    - int gameRound
    - MessageGenerator messageGenerator
    - HashMap<String, Supplier<HashMap<String, List<Territory>>>> mapCreateFns
    - String mapName
    - setupMapCreateFns(): void
    + gameFlow(Socket client_socket, int numClients): void
    + doActionPhase(Player p): void
    + doPlacementPhase(Socket client_socket, int numClients): Player
    + addPlayer(Player p): void
    + chooseColor(Conn conn): String
    + ifChooseWatch(Conn conn): String
    + chooseNumOfPlayers(Conn conn, int numClients): void
    + getNumPlayer(): int
    + initializeMap(int numOfPlayers): void
    + assignUnits(Player p, Conn conn): void
    + notifyAllPlayers(Conn conn, String newStage): void
    - promptTerritory(Player p, Conn conn): void
    - isValidTerritory(Player p, String territoryName, Conn conn): boolean
    - promptUnitNumber(Player p, String territoryName, Conn conn): void
    - isValidUnitNumber(Player p, String territoryName, String num, Conn conn): boolean
    + setNumPlayer(int num): void
    + doAction(Player p): boolean
    + makeActionOrder(Player p, String actionName): order
    + doOneAction(Player p, String actionName): void
    + checkNameReturnTerritory(String territory_name, GameMap map): Territory
    + worldwar(): HashMap<String, String>
    + generateUnit(): void
    + produceResources(): void
    + findWinner(): Player
}


class Player {
    - List<Territory> Territories
    - ArrayList<Unit> units
    - String color
    - Conn conn
    - int numUnits
    - AssignUnitRuleChecker placementchecker
    - boolean isWatch
    + unplacedUnits(): int
    + findNextUnplacedUnits(int amount): ArrayList<Unit>
    + placeUnitsSameTerritory(String t_name, int num): String
    + getTerritoryNames(): ArrayList<String>
    + addTerritories(List<Territory> territory): void
    + removeTerritory(Territory t): void
    + getTerritories(): List<Territory>
    + getColor(): String
    + getConn(): Conn
    + generateNewUnit(): void
    + checkLose(): boolean
    + setWatch(): void
    + getisWatch(): boolean
}

class Conn {
    - Socket clientsocket
    - BufferedReader in
    - PrintWriter out
    + send(String msg): void
    + recv(): String
    + close(): void
}

class Unit {
    <<interface>>
    + setwhere(Territory where): void
    + getwhere(): Territory
    + getOwner(): Player
    + getAlive(): boolean
    + getisAttacker(): boolean
    + setisAttacker(): void
    + setDead(): void
    + getId(): void
}

class BasicUnit {
    - Player owner
    - Territory where
    - boolean isAttacker
    - int id
    - boolean isAlive
}



class Territory {
    - String name
    - List<Territory> neighbors
    - ArrayList<Unit> units
    - Player owner
    - Battle battle
    + getUnits(): ArrayList<Unit>
    + existsBattle(): boolean
    + defendHome(): void
    + doBattle(): String
    + getAliveUnitsFor(Player player): ArrayList<Unit>
    + tryAddUnits(ArrayList<Unit> units): void
    + tryAddAttackers(ArrayList<Unit> units): void
    + tryRemoveUnits(int num, Player player): ArrayList<Unit>
    + getUnitsString(): String
    + getOwner(): Player
    + setOwner(Player owner): void
    + getName(): String
    + getNeighbors(): List<Territory>
    + getNeighborsNames(): String
    + territoryInfo(): String
    + setNeighbors(List<Territory> neighbors): void
    - addNeighbor(Territory neighbor): void
}

class AssignUnitRuleChecker{
    + checkMyRule(String territory_name, Player player, int amount): String
}

class Combat{
    - int seed
    - int diceNum
    + setSeed(int seed): void
    + setDiceNum(int diceNum): void
    + rolldice(): int
    + determineWin(Unit A, Unit B): Unit
}

class Battle{
    - ArrayList<ArrayList<Unit>> parties
    - Combat combat
    + resolveBattle(): Player
    + addGroup(ArrayList<Unit> units): void
    + setCombat(Combat combat): void
    + getParties(): ArrayList<ArrayList<Unit>>
    + checkGroupExisted(ArrayList<Unit> units): boolean
    + GameLog(): String
    + clearParty(): void
}

class Order {
    - Territory from
    - Territory to 
    - int numUnits
    - Player player
    - GameMap gameMap
    + getPlayer(): Player
    + tryAction(): String
}

class MoveOrder{
    + tryMove(): boolean
}

class AttackOrder{
    + tryAttack(): boolean
}


class OrderRuleChecker {
    -RuleChecker<T> next
    + checkMyRule(): String
}

class MoveInputRuleChecker{
    + checkMyRule(): String
}

class MovePathRuleChecker{
    + checkMyRule(): String
    + dfs(Territory current, Territory destination, Player player, HashMap<String, List<Territory>> gameMap, HashSet<Territory> visited): boolean
}

class AttackInputRuleChecker{
    + checkMyRule(): String
}

class AttackAdjacentRuleChecker{
    + checkMyRule(): String
}

```
### UML Description
Some important details:

| Class  | Method | Description |
| ------------- | ------------- | ------------- |
| Server | doPlacementPhase() | 1. assign territories into N groups and through recv() get player's choice of color. <br />2. assign each player the same number of units |
|^      | doOrderPhase() | game main phase: order, order, order..... |
|^      | doOrderTurn() | 1. Move + Attack <br />2. after completing all actions, add 1 unit to each territory |
| Player | updateUnits() | update units in territory |
| Territory | ongoingBattle(): Battle | SingleTon pattern |
| ^         | getBattle(): Battle | If Battle exists, return Battle, otherwise, create it |
| Combat | determineWin(Unit unit1, Unit unit2)| |
| Battle | resolveBattle(): vector<string Name, Unit[] units[]>| Battling in a circle list|

### UI Design
<video src="Resources/RiskGameUIDesign.mp4" width="600px">

## Implementation
Duke Map
```mermaid
flowchart LR
    baldwin[Baldwin Auditorium]
    smith[Smith Warehouse]
    bryan[Bryan Center]
    wilson[Wilson Recreation Center]
 
   cameron[Cameron Indoor Stadium]
  
 
 
    fuqua[The Fuqua School of Business]
    bookStore[University Book Store]
    wallace[Wallace Wade Stadium]
  
    dukeLemur[Duke Lemur Center]
    smith[Smith Warehouse]
    studentWellness[Student Wellness Center]

   dukeLaw[Duke University School of Law]
   fitzpatrick[Fitzpatrick Center]
   jbDuke[JB Duke Hotel]
   levine[Levine Science Research Center]
   
   penn[Penn Pavilion]
   scienceGarage[Science Garage]


   dukeForest[Duke Forest]
    baldwin --- brodie
    baldwin --- smith

    brodie[Brodie Recreational Center]
    smith --- brodie

    dukeChapel[Duke Chapel]
    dukeChapel --- baldwin

    perkins[Perkins Library]
    broadhead[Broadhead Center]

    broadhead --- dukeChapel
    dukeChapel --- perkins

    nasher[Nasher Museum of Art]
    dukeGarden[Duke Garden]

    dukeGarden --- dukeChapel
    dukeGarden --- nasher
    broadhead --- dukeGarden

    perkins --- fitzpatrick
    fitzpatrick --- levine

    dukeHospital[Duke Hospital]

    dukeHospital --- levine
    dukeHospital --- dukeGarden

    bryan --- broadhead
    penn --- bookStore
    penn --- studentWellness
    studentWellness --- dukeLaw
    bookStore --- bryan

    dukeLaw --- fuqua
    fuqua --- jbDuke

    wilson --- cameron
    wilson --- wallace
    dukeLaw --- wilson
    cameron --- wallace



    jbDuke --- dukeForest
   jbDuke --- scienceGarage
   jbDuke --- dukeLemur
```

<img src="Resources/DukeMap_init.png" width="600px">
