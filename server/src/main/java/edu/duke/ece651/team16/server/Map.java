package edu.duke.ece651.team16.server;

import java.util.*;

public class Map {
  private int numPlayer;


    public HashMap<String, List<Territory>> createBasicMap() {
        HashMap<String, List<Territory>> basicMap = new HashMap<String, List<Territory>>();
        Territory red = new Territory("red");
        Territory blue = new Territory("blue");
        red.setNeighbors(Arrays.asList(blue));
        ArrayList<Territory> redTerritory= new ArrayList<Territory>();
        ArrayList<Territory> blueTerritory= new ArrayList<Territory>();
        redTerritory.add(red);
        blueTerritory.add(blue);
        basicMap.put("Red", redTerritory);
        basicMap.put("Blue",blueTerritory);
        return basicMap;
    }

    public HashMap<String, List<Territory>> createDukeMap() {
      //create a list of territories with the territorry called "duke garden" and "duke chapel"
        ArrayList<Territory> totalTerritory= new ArrayList<Territory>();

        Territory baldwin = new Territory("Baldwin Auditorium");
        Territory broadhead = new Territory("Broadhead Center");
        Territory brodie = new Territory("Brodie Recreational Center");
        Territory bryan = new Territory("Bryan Center");
        Territory cameron = new Territory("Cameron Indoor Stadium");
        Territory dukeChapel = new Territory("Duke Chapel");
        Territory dukeForest = new Territory("Duke Forest");
        Territory dukeGarden = new Territory("Duke garden");
        Territory dukeHospital = new Territory("Duke Hospital");
        Territory dukeLemur = new Territory("Duke Lemur Center");
        Territory dukeLaw = new Territory("Duke University School of Law");
        Territory fitzpatrick = new Territory("Fitzpatrick Center");
        Territory jbDuke = new Territory("JB Duke Hotel");
        Territory levine = new Territory("Levine Science Research Center");
        Territory nasher = new Territory("Nasher Museum of Art");
        Territory penn = new Territory("Penn Pavilion");
        Territory perkins = new Territory("Perkins Library");
        Territory scienceGarage = new Territory("Science Garage");
        Territory smith = new Territory("Smith Warehouse");
        Territory studentWellness = new Territory("Student Wellness Center");
        Territory fuqua = new Territory("The Fuqua School of Business");
        Territory bookStore = new Territory("University Book Store");
        Territory wallace = new Territory("Wallace Wade Stadium");
        Territory wilson = new Territory("Wilson Recreation Center");

        baldwin.setNeighbors(Arrays.asList(brodie, smith));
        brodie.setNeighbors(Arrays.asList(smith));
        wilson.setNeighbors(Arrays.asList(cameron,wallace));
        cameron.setNeighbors(Arrays.asList(wallace));
        fuqua.setNeighbors(Arrays.asList(dukeLaw,jbDuke));
        jbDuke.setNeighbors(Arrays.asList(scienceGarage, dukeForest, dukeLemur));
        studentWellness.setNeighbors(Arrays.asList(dukeLaw));
        penn.setNeighbors(Arrays.asList(studentWellness, bookStore));
        bryan.setNeighbors(Arrays.asList(bookStore,broadhead));
        dukeChapel.setNeighbors(Arrays.asList(broadhead, perkins));
        fitzpatrick.setNeighbors(Arrays.asList(levine, perkins));
        dukeHospital.setNeighbors(Arrays.asList(levine, dukeGarden));
        dukeGarden.setNeighbors(Arrays.asList(dukeChapel, nasher, broadhead));


        totalTerritory.add(baldwin);
        totalTerritory.add(broadhead);
        totalTerritory.add(brodie);
        totalTerritory.add(bryan);
        totalTerritory.add(cameron);
        totalTerritory.add(dukeChapel);
        totalTerritory.add(dukeForest);
        totalTerritory.add(dukeGarden); 
        totalTerritory.add(dukeHospital);
        totalTerritory.add(dukeLemur);
        totalTerritory.add(dukeLaw);
        totalTerritory.add(fitzpatrick);
        totalTerritory.add(jbDuke);
        totalTerritory.add(levine);
        totalTerritory.add(nasher);
        totalTerritory.add(penn);
        totalTerritory.add(perkins);
        totalTerritory.add(scienceGarage);
        totalTerritory.add(smith);
        totalTerritory.add(studentWellness);
        totalTerritory.add(fuqua);
        totalTerritory.add(bookStore);
        totalTerritory.add(wallace);
        totalTerritory.add(wilson);
        

        HashMap<String, List<Territory>> dukemap = new HashMap<>();
        int numTerritories = numPlayer == 2 ? 12 : numPlayer == 3 ? 8 : numPlayer == 4 ? 6 : -1;

        if (numTerritories != -1) {
            List<String> colors = Arrays.asList("Red", "Blue", "Yellow", "Green").subList(0, numPlayer);
            for (int i = 0; i < numPlayer; i++) {
                List<Territory> territories = totalTerritory.subList(i * numTerritories, (i + 1) * numTerritories);
                dukemap.put(colors.get(i), new ArrayList<>(territories));
            }
        }

        return dukemap;
    }
}