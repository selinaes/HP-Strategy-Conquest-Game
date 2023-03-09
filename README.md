# Risk_Game


![pipeline](https://gitlab.oit.duke.edu/zf70/Risk_Game/badges/docker_and_testCI/pipeline.svg)

![coverage](https://gitlab.oit.duke.edu/zf70/Risk_Game/badges/docker_and_testCI/coverage.svg?job=test)

## Coverage
[Detailed coverage](https://zf70.pages.oit.duke.edu/Risk_Game/dashboard.html)

# Table of Contents
- [Schedule](#Schedule)
- [Review](#Review)
  - [Requirements](#Requirements)
- [Design Overview](#Design-Overview)
  - [UML Diagram](#UML-Diagram)
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
- check source territory is your.
- check destinination territory is your.

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

## Implementation
