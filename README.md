# Risk_Game

# Table of Contents

- [Review](#Review)
  - [Requirements](#Requirements)
- [Design Overview](#Design-Overview)
  - [UML Diagram](#UML-Diagram)
- [Implementation](#Implementation)

## Review

### Requirements

See requirements in [proj1.pdf](/proj1.pdf)

## Design Overview

### UML Diagram
```mermaid
classDiagram
    Animal <|-- Duck
    Animal <|-- Fish
    Animal <|-- Zebra
    Animal : +int age
    Animal : +String gender
    Animal: +isMammal()
    Animal: +mate()
    class Duck{
      +String beakColor
      +swim()
      +quack()
    }
    class Fish{
      -int sizeInFeet
      -canEat()
    }
    class Zebra{
      +bool is_wild
      +run()
    }
```
## Implementation
