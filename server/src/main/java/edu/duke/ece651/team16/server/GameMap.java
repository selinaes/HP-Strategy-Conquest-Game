package edu.duke.ece651.team16.server;

import java.util.*;

public class GameMap {
  private int numPlayer;
  private ArrayList<String> ColorList;
  private HashMap<String, List<Territory>> map;

  /**
   * constructor of the map
   * 
   * @param numPlayer the number of players
   **/
  public GameMap(int numPlayer) {
    this.numPlayer = numPlayer;
    this.ColorList = new ArrayList<>();

    ArrayList<String> colorList = new ArrayList<>();
    colorList.addAll(Arrays.asList("red", "blue", "yellow", "green"));
    for (int i = 0; i < numPlayer; i++) {
      this.ColorList.add(colorList.get(i));
    }
  }

  /**
   * get TerritoryList
   * 
   * @return a list of territories
   **/
  public ArrayList<Territory> getTerritoryList() {
    System.out.println(map);
    ArrayList<Territory> territoryList = new ArrayList<>();
    System.out.println(this.ColorList);
    for (String c : this.map.keySet()) {
      for (Territory t : this.map.get(c)) {
        // System.out.println("In getTerritoryList" + t);
        territoryList.add(t);
      }
    }
    // System.out.println("In gameMap" + territoryList);
    return territoryList;
  }

  /**
   * create a basic map with two territories and two players
   * 
   * @return a map with two territories
   **/
  public HashMap<String, List<Territory>> createBasicMap() {
    this.numPlayer = 2;
    HashMap<String, List<Territory>> basicMap = new HashMap<String, List<Territory>>();
    Territory red = new Territory("A");
    Territory blue = new Territory("B");
    red.setNeighbors(Arrays.asList(blue));
    red.setDistance(Arrays.asList(blue), Arrays.asList(5));
    ArrayList<Territory> redTerritory = new ArrayList<Territory>();
    ArrayList<Territory> blueTerritory = new ArrayList<Territory>();
    redTerritory.add(red);
    blueTerritory.add(blue);
    basicMap.put("red", redTerritory);
    basicMap.put("blue", blueTerritory);
    setMap(basicMap);
    return basicMap;
  }

  /**
   * create a Duke map with 24 territories
   * 
   * @return a map with 24 territories
   **/
  public HashMap<String, List<Territory>> createDukeMap() {
    ArrayList<Territory> totalTerritory = new ArrayList<Territory>();

    Territory baldwin = new Territory("baldwin");
    Territory broadhead = new Territory("broadhead");
    Territory brodie = new Territory("brodie");
    Territory bryan = new Territory("bryan");
    Territory cameron = new Territory("cameron");
    Territory dukeChapel = new Territory("dukeChapel");
    Territory dukeForest = new Territory("dukeForest");
    Territory dukeGarden = new Territory("dukeGarden");
    Territory dukeHospital = new Territory("dukeHospital");
    Territory dukeLemur = new Territory("dukeLemur");
    Territory dukeLaw = new Territory("dukeLaw");
    Territory fitzpatrick = new Territory("fitzpatrick");
    Territory jbDuke = new Territory("jbDuke");
    Territory levine = new Territory("levine");
    Territory nasher = new Territory("nasher");
    Territory penn = new Territory("penn");
    Territory perkins = new Territory("perkins");
    Territory scienceGarage = new Territory("scienceGarage");
    Territory smith = new Territory("smith");
    Territory studentWellness = new Territory("studentWellness");
    Territory fuqua = new Territory("fuqua");
    Territory bookStore = new Territory("bookStore");
    Territory wallace = new Territory("wallace");
    Territory wilson = new Territory("wilson");

    baldwin.setNeighbors(Arrays.asList(brodie, smith, dukeChapel));
    baldwin.setDistance(Arrays.asList(brodie, smith, dukeChapel), Arrays.asList(5, 5, 5));
    brodie.setNeighbors(Arrays.asList(smith));
    brodie.setDistance(Arrays.asList(smith), Arrays.asList(5));
    wilson.setNeighbors(Arrays.asList(cameron, wallace, dukeLaw));
    wilson.setDistance(Arrays.asList(cameron, wallace, dukeLaw), Arrays.asList(5, 5, 5));
    cameron.setNeighbors(Arrays.asList(wallace, studentWellness));
    cameron.setDistance(Arrays.asList(wallace, studentWellness), Arrays.asList(5, 5));
    fuqua.setNeighbors(Arrays.asList(dukeLaw, jbDuke));
    fuqua.setDistance(Arrays.asList(dukeLaw, jbDuke), Arrays.asList(5, 5));
    jbDuke.setNeighbors(Arrays.asList(scienceGarage, dukeForest, dukeLemur));
    jbDuke.setDistance(Arrays.asList(scienceGarage, dukeForest, dukeLemur), Arrays.asList(5, 5, 5));
    penn.setNeighbors(Arrays.asList(studentWellness, bookStore));
    penn.setDistance(Arrays.asList(studentWellness, bookStore), Arrays.asList(5, 5));
    bryan.setNeighbors(Arrays.asList(bookStore, broadhead));
    bryan.setDistance(Arrays.asList(bookStore, broadhead), Arrays.asList(5, 5));
    dukeChapel.setNeighbors(Arrays.asList(broadhead, perkins));
    dukeChapel.setDistance(Arrays.asList(broadhead, perkins), Arrays.asList(5, 5));
    fitzpatrick.setNeighbors(Arrays.asList(levine, perkins));
    fitzpatrick.setDistance(Arrays.asList(levine, perkins), Arrays.asList(5, 5));
    dukeHospital.setNeighbors(Arrays.asList(levine, dukeGarden));
    dukeHospital.setDistance(Arrays.asList(levine, dukeGarden), Arrays.asList(5, 5));
    dukeGarden.setNeighbors(Arrays.asList(dukeChapel, nasher, broadhead));
    dukeGarden.setDistance(Arrays.asList(dukeChapel, nasher, broadhead), Arrays.asList(5, 5, 5));
    dukeLaw.setNeighbors(Arrays.asList(studentWellness));
    dukeLaw.setDistance(Arrays.asList(studentWellness), Arrays.asList(5));

    totalTerritory.addAll(Arrays.asList(baldwin, broadhead, brodie, bryan, cameron, dukeChapel, dukeForest,
        dukeGarden, dukeHospital, dukeLemur, dukeLaw, fitzpatrick, jbDuke,
        levine, nasher, penn, perkins, scienceGarage, smith, studentWellness,
        fuqua, bookStore, wallace, wilson));

    HashMap<String, List<Territory>> dukemap = new HashMap<>();
    // int numTerritories = numPlayer == 2 ? 12 : numPlayer == 3 ? 8 : numPlayer ==
    // 4 ? 6 : -1;
    int numTerritories;
    if (numPlayer < 1 || numPlayer > 24) {
      numTerritories = -1;
    } else {
      numTerritories = 24 / numPlayer;
    }
    if (numTerritories != -1) {
      List<String> colors = Arrays.asList("red", "blue", "yellow", "green").subList(0, numPlayer);
      for (int i = 0; i < numPlayer; i++) {
        List<Territory> territories = totalTerritory.subList(i * numTerritories, (i + 1) * numTerritories);
        dukemap.put(colors.get(i), new ArrayList<>(territories));
      }
    }
    setMap(dukemap);
    return dukemap;
  }

  public HashMap<String, List<Territory>> createHPMap() {
    ArrayList<Territory> totalTerritory = new ArrayList<Territory>();
    Territory headmaster = new Territory("Headmaster's Office");
    Territory lake = new Territory("Black Lake");
    Territory willow = new Territory("Whomping Willow");
    Territory hospital = new Territory("Hospital Wing");
    Territory quidditch = new Territory("Quidditch Pitch");
    Territory forest = new Territory("Forbidden Forest");
    Territory requirement = new Territory("Room of Requirement");
    Territory library = new Territory("Hogwarts Library");
    Territory astronomy = new Territory("Astronomy Tower");
    Territory hall = new Territory("Great Hall");
    Territory classroom = new Territory("Potions Classroom");
    Territory chamber = new Territory("Chamber of Secret");

    headmaster.setNeighbors(Arrays.asList(lake, requirement));
    headmaster.setDistance(Arrays.asList(lake, requirement), Arrays.asList(5, 5));
    willow.setNeighbors(Arrays.asList(lake, hall, library));
    willow.setDistance(Arrays.asList(lake, hall, library), Arrays.asList(5, 5, 5));
    hospital.setNeighbors(Arrays.asList(quidditch, astronomy, forest, classroom));
    hospital.setDistance(Arrays.asList(quidditch, astronomy, forest, classroom), Arrays.asList(5, 5, 5, 5));
    astronomy.setNeighbors(Arrays.asList(forest, quidditch));
    astronomy.setDistance(Arrays.asList(forest, quidditch), Arrays.asList(5, 5));
    chamber.setNeighbors(Arrays.asList(library, requirement, hall, classroom));
    chamber.setDistance(Arrays.asList(library, requirement, hall, classroom), Arrays.asList(5, 5, 5, 5));
    quidditch.setNeighbors(Arrays.asList(requirement));
    quidditch.setDistance(Arrays.asList(requirement), Arrays.asList(5));
    totalTerritory.addAll(Arrays.asList(headmaster, lake, willow, hospital, quidditch, forest, requirement, library,
        astronomy, hall, classroom, chamber));
    HashMap<String, List<Territory>> HPmap = new HashMap<>();
    int numTerritories;
    if (numPlayer < 1 || numPlayer > 12) {
      numTerritories = -1;
    } else {
      numTerritories = 12 / numPlayer;
    }
    if (numTerritories != -1) {
      List<String> colors = Arrays.asList("red", "blue", "yellow", "green").subList(0, numPlayer);
      for (int i = 0; i < numPlayer; i++) {
        List<Territory> territories = totalTerritory.subList(i * numTerritories, (i + 1) * numTerritories);
        HPmap.put(colors.get(i), new ArrayList<>(territories));
      }
    }
    setMap(HPmap);
    return HPmap;
  }

  /**
   * Create a test map for testing purposes
   * 
   * @return a test map
   */
  public HashMap<String, List<Territory>> createTestMap() {
    this.numPlayer = 4;
    HashMap<String, List<Territory>> testMap = new HashMap<String, List<Territory>>();
    Territory red = new Territory("R");
    Territory blue = new Territory("B");
    Territory yellow = new Territory("Y");
    Territory green = new Territory("G");
    red.setNeighbors(Arrays.asList(blue, yellow, green));
    blue.setNeighbors(Arrays.asList(yellow, green));
    yellow.setNeighbors(Arrays.asList(green));
    testMap.put("red", new ArrayList<>(Arrays.asList(red)));
    testMap.put("blue", new ArrayList<>(Arrays.asList(blue)));
    testMap.put("yellow", new ArrayList<>(Arrays.asList(yellow)));
    testMap.put("green", new ArrayList<>(Arrays.asList(green)));
    setMap(testMap);
    return testMap;
  }

  /**
   * Create a test map for testing purposes
   * 
   * @return a test map
   */
  public HashMap<String, List<Territory>> createTest3Map() {
    HashMap<String, List<Territory>> test3Map = new HashMap<String, List<Territory>>();
    Territory red = new Territory("R");
    Territory blue = new Territory("B");
    Territory yellow = new Territory("Y");
    red.setNeighbors(Arrays.asList(blue, yellow));
    red.setDistance(Arrays.asList(blue, yellow), Arrays.asList(5, 5));
    blue.setNeighbors(Arrays.asList(yellow));
    blue.setDistance(Arrays.asList(yellow), Arrays.asList(5));
    test3Map.put("red", new ArrayList<>(Arrays.asList(red)));
    test3Map.put("blue", new ArrayList<>(Arrays.asList(blue)));
    test3Map.put("yellow", new ArrayList<>(Arrays.asList(yellow)));
    setMap(test3Map);
    return test3Map;
  }

  /**
   * get the color list of the map
   * 
   * @return the color list of the map
   **/
  public ArrayList<String> getColorList() {
    return ColorList;
  }

  /**
   * get the map
   * 
   * @return the map for the game
   **/
  public HashMap<String, List<Territory>> getMap() {
    return map;
  }

  /*
   * set the map
   * 
   * @param map the map for the game
   */
  public void setMap(HashMap<String, List<Territory>> map) {
    this.map = map;
  }
}