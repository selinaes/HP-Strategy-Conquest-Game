# Risk_Game

![pipeline](https://gitlab.oit.duke.edu/zf70/Risk_Game/badges/main/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/zf70/Risk_Game/badges/main/coverage.svg?job=test)

# Table of Contents
- [Coverage](#Coverage)
- [Schedule](#Schedule)
- [Requirements](#Requirements)
- [Game Detail](#Game-Detail)
  - [Duke Map](#Duke-Map)
  - [Map initialization](#Map-initialization)
  - [Unit initialization](#Unit-initialization)
  - [Resource and Tech Level initialization](#Resource-and-Tech-Level-initialization)
  - [Action Costs](#Action-Costs)
- [Design Overview](#Design-Overview)
  - [UML Diagram](#UML-Diagram)
  - [UI Prototype](#UI-Prototype)

## Coverage
[Detailed coverage](https://zf70.pages.oit.duke.edu/Risk_Game/dashboard.html)

## Schedule
* Evolution 1: 2/24 - 3/24
* Evolution 2: 3/24 - 4/10
* Evoluiton 3: 4/10 - 4/28

[Project Mamagement Sheet](https://docs.google.com/spreadsheets/d/1B2cUguqBPXm3IJBRU1WRPnoBng4p8AiTouAkxmXUhYg/edit#gid=0)

## Requirements
[Evolution1](prj1.pdf)
Evolution2

## Game Detail
### Duke Map
<img src="Resources/DukeMap_init.png" width="600px">

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

### Map initialization
The duke map is initialized with 24 territories. This game supports 2-4 players. For 2 players, each player will have 12 territories, and can choose from colors: red, blue. For 3 players, each player will have 8 territories, and can choose from colors: red, blue, yellow. For 4 players, each player will have 6 territories, and can choose from colors: red, blue, yellow, green. For each pair of adjacent territories, the distance between them is 5.

### Unit initialization
The player each has 24 Freshman units(level 0) at the start of the game to assign to their territories. There is a total of 7 level of units: Freshman(level 0), Sophomore(level 1), Junior(level 2), Senior(level 3), Master(level 4), phD(level 5), Professor(level 6). Each territory will generate 1 Freshman unit(level 0) at the beginning of a new turn.

### Resource and Tech Level initialization
Each territory produces 5 food resources and 5 tech resources per turn. Each player starts with a tech level 1, and can upgrade per turn till tech level 6.

### Action Costs
Attack and move actions cost food resources.
* Attack cost = 8 * numUnits
* Move cost =  distance * numUnits

Research and upgrade actions cost tech resources.
* Research Cost Table

| Upgrade Level | Cost |
| ------------- | ---- |
|     1->2      |  20  |
|     2->3      |  40  |
|     3->4      |  80  |
|     4->5      |  160 |
|     5->6      |  320 |

* Upgrade Cost+Bonus Table

| Cost (Total) | Bonus | Tech Level Required |
| ------------ | ----- | ------------------- |
|     0(0)     |   0   |   Units Start Here  |
|     3(3)     |   1   |          1          |
|     8(11)    |   3   |          2          |
|    19(30)    |   5   |          3          |
|    25(55)    |   8   |          4          |
|    35(90)    |   11  |          5          |
|    50(140)   |   15  |          6          |

## Design Overview
### UML Diagram
```mermaid
classDiagram
    ServerApp "1" --> "1" Server
    ClientApp "1" --> "N" Client
    Server "1" --> "N" Player
    Server "1" --> "1" RuleChecker
    Player "1"--> "N" Territory
    Player "1" --> "N" Order
    Territory "1"--> "N" Unit
    Unit <|.. BasicUnit
    Client "1" --> "1" Connection
    Server "1"--> "1" Connection
    Client "1" --> "1" Actions
    Client "1" --> "1" Views
    Client "1" --> "1" InputChecker
    
    Territory "1" --> "0..1" Battle
    Battle --> Combat
    
    RuleChecker <|-- MoveInputChecker
    RuleChecker <|-- MovePathChecker
    RuleChecker <|-- AttackInputChecker
    RuleChecker <|-- AttackAdjacentChecker

    AttackOrder --|> Order
    MoveOrder --|> Order
    
    Battle "1" --> "N" AttackOrder

class Client {
    
}

class Connection {
    - Socket socket
    + send(): void
    + recv(): void
}

class Actions {
    - BufferedReader inputReader
    - PrintStream out
    - Connection conn
    + selectColor(String prompt): void
    + distributeUnits(String prompt): void
    + enterOrder(String prompt): void
    + loseChoice(String prompt): void
    + commit(String prompt): void
}

class Views {
    - boolean watch
    + displayMap(): String
    + displayChoices(): String
    + displayWin(): String
    + displaylost(): String
}

class InputChecker{
    <<interface>>
    + checkInput()
}

class Server {
    - RuleChecker
    + doPlacementPhase(): void
    + doOrderPhase(): void
    + doOrderTurn(): void
    + determineWinner(): void
    + formMap(): void
}

class Player {
    - Territory[] Territories
    - String name
    - String color
    - Connection connection
    + updateTerritories(Territory[]): void
    + updateUnits(Unit[] unit_in): void
    + doOnePlacement(Territory): void
    + doOneOrder(): void
    + checklost(): boolean
}

class BasicUnit {
    - Player owner
    - Territory where
    - boolean isAttacker
    - int id
    - boolean alive
}

class Unit {
<<interface>>

}

class Territory {
    - string name
    - Territory[] neighbor
    - Unit[] localBasicUnit
    - Player owner
    - Battle ongingBattle
    + update(String playerName, Unit[] units): void
    + getBattle(): Battle
}

class Combat{
    + rolldice(): int
    + determineWin(Unit A, Unit B): void
}

class Battle{
    - HashMap~String playername, Unit[]~Parties
    + resolveBattle(): playername, Units[]
    + checkPartyAlive(String playername): boolean
    + addGroup(): void
}

class Order{
    -Territory from
    -Territory to 
    -string playerName
}
class MoveOrder{
    + tryMove(): boolean
}

class AttackOrder{
    + tryAttack(): boolean
}

class RuleChecker {
    -RuleChecker<T> next
    #checkMyRule()
}

class MoveInputChecker{
    #checkMyRule():
}

class MovePathChecker{
    #checkMyRule():
}

class AttackInputChecker{
    #checkMyRule():
}

class AttackAdjacentChecker{
    #checkMyRule():
}
```

### UI Prototype
<video src="Resources/RiskGameUIDesign.mp4" width="600px">

