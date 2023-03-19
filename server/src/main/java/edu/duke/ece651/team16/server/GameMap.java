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

    // Territory baldwin = new Territory("Baldwin Auditorium");
    // Territory broadhead = new Territory("Broadhead Center");
    // Territory brodie = new Territory("Brodie Recreational Center");
    // Territory bryan = new Territory("Bryan Center");
    // Territory cameron = new Territory("Cameron Indoor Stadium");
    // Territory dukeChapel = new Territory("Duke Chapel");
    // Territory dukeForest = new Territory("Duke Forest");
    // Territory dukeGarden = new Territory("Duke Garden");
    // Territory dukeHospital = new Territory("Duke Hospital");
    // Territory dukeLemur = new Territory("Duke Lemur Center");
    // Territory dukeLaw = new Territory("Duke University School of Law");
    // Territory fitzpatrick = new Territory("Fitzpatrick Center");
    // Territory jbDuke = new Territory("JB Duke Hotel");
    // Territory levine = new Territory("Levine Science Research Center");
    // Territory nasher = new Territory("Nasher Museum of Art");
    // Territory penn = new Territory("Penn Pavilion");
    // Territory perkins = new Territory("Perkins Library");
    // Territory scienceGarage = new Territory("Science Garage");
    // Territory smith = new Territory("Smith Warehouse");
    // Territory studentWellness = new Territory("Student Wellness Center");
    // Territory fuqua = new Territory("The Fuqua School of Business");
    // Territory bookStore = new Territory("University Book Store");
    // Territory wallace = new Territory("Wallace Wade Stadium");
    // Territory wilson = new Territory("Wilson Recreation Center");

    baldwin.setNeighbors(Arrays.asList(brodie, smith, dukeChapel));
    brodie.setNeighbors(Arrays.asList(smith));
    wilson.setNeighbors(Arrays.asList(cameron, wallace, dukeLaw));
    cameron.setNeighbors(Arrays.asList(wallace, studentWellness));
    fuqua.setNeighbors(Arrays.asList(dukeLaw, jbDuke));
    jbDuke.setNeighbors(Arrays.asList(scienceGarage, dukeForest, dukeLemur));
    penn.setNeighbors(Arrays.asList(studentWellness, bookStore));
    bryan.setNeighbors(Arrays.asList(bookStore, broadhead));
    dukeChapel.setNeighbors(Arrays.asList(broadhead, perkins));
    fitzpatrick.setNeighbors(Arrays.asList(levine, perkins));
    dukeHospital.setNeighbors(Arrays.asList(levine, dukeGarden));
    dukeGarden.setNeighbors(Arrays.asList(dukeChapel, nasher, broadhead));

    totalTerritory.addAll(Arrays.asList(baldwin, broadhead, brodie, bryan, cameron, dukeChapel, dukeForest,
        dukeGarden, dukeHospital, dukeLemur, dukeLaw, fitzpatrick, jbDuke,
        levine, nasher, penn, perkins, scienceGarage, smith, studentWellness,
        fuqua, bookStore, wallace, wilson));

    HashMap<String, List<Territory>> dukemap = new HashMap<>();
    // int numTerritories = numPlayer == 2 ? 12 : numPlayer == 3 ? 8 : numPlayer == 4 ? 6 : -1;
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


  /**
   * Create a test map for testing purposes
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
   * @return a test map
   */
  public HashMap<String, List<Territory>> createTest3Map() {
    HashMap<String, List<Territory>> test3Map = new HashMap<String, List<Territory>>();
    Territory red = new Territory("R");
    Territory blue = new Territory("B");
    Territory yellow = new Territory("Y");
    red.setNeighbors(Arrays.asList(blue, yellow));
    blue.setNeighbors(Arrays.asList(yellow));
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