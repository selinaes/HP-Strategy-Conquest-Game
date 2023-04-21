package edu.duke.ece651.team16.controller;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class GamePlayDisplay {
    /**
     * This is a method used to format the units information received from the
     * server
     * 
     * @param unitsInfo The units information received from the server
     * @return The formatted units information
     */
    public String parseUnitsInfo(String unitsInfo) {
        String[] unitName = { "Freshman", "Sophomore", "Junior", "Senior", "Graduate", "PhD", "Professor" };
        String[] unitsInfoArray = unitsInfo.split(",");
        StringBuilder unitsInfoString = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            unitsInfoString.append(unitName[i] + ": " + unitsInfoArray[i] + ", ");
        }
        unitsInfoString.append(unitName[6] + ": " + unitsInfoArray[6]);
        return unitsInfoString.toString();
    }

    /**
     * This is a private method used to get the total number of units
     * 
     * @param unitsInfo The units information in a string
     * @return The total number of units
     */
    public int getUnitNum(String unitsInfo) {
        int sum = 0;
        String[] unitsInfoArray = unitsInfo.split(",");
        for (int i = 0; i < 7; i++) {
            sum += Integer.parseInt(unitsInfoArray[i]);
        }
        return sum;
    }

    /**
     * This is a method used to format the units information received from
     * 
     * @param unitsInfo The units information in a string
     * @return The formatted units information in an array
     */
    public ArrayList<Integer> getUnitNumArray(String unitsInfo) {
        ArrayList<Integer> unitNumArray = new ArrayList<>();
        String[] unitsInfoArray = unitsInfo.split(",");
        for (int i = 0; i < 7; i++) {
            unitNumArray.add(Integer.parseInt(unitsInfoArray[i]));
        }
        return unitNumArray;
    }

    /**
     * This is a dialog box for upgrade units
     * 
     * @return the user response
     */
    public ArrayList<String> setUpgradeInfo() {
        // create the text input fields, level, intiial, amount
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();

        // create the dialog and set the content
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        dialog.setTitle("Enter Upgrade UnitNumber, Initial Level, # levels to Upgrade");
        dialog.getDialogPane()
                .setContent(new VBox(10, new Label("Upgrade UnitNumber: "), textField1, new Label("Initial Level: "),
                        textField2,
                        new Label("# levels to Upgrade: "), textField3));

        // show the dialog and wait for the user response
        System.out.println("Before showAndWait");
        Optional<String> result = dialog.showAndWait();
        System.out.println("After showAndWait");

        // check if the user clicked OK and retrieve the input values
        ArrayList<String> res = new ArrayList<>();
        System.out.println("Before if");
        if (result.isPresent()) {
            res.add(textField1.getText());
            System.out.println(textField1.getText());
            res.add(textField2.getText());
            System.out.println(textField2.getText());
            res.add(textField3.getText());
            System.out.println(textField3.getText());
        }
        System.out.println(res);
        return res;
    }

    /**
     * This is a dialog box for alliance
     * 
     * @return the user response
     */
    public ArrayList<String> setAllianceInfo() {
        // create the text input fields, level, intiial, amount
        TextField textField1 = new TextField();

        // create the dialog and set the content
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        dialog.setTitle("Make Allicance");
        dialog.getDialogPane()
                .setContent(
                        new VBox(10, new Label("Enter Player Color You Want to Form an Allicance With: "), textField1));
        // show the dialog and wait for the user response
        Optional<String> result = dialog.showAndWait();
        // check if the user clicked OK and retrieve the input values
        ArrayList<String> res = new ArrayList<>();
        if (result.isPresent()) {
            res.add(textField1.getText());
            System.out.println(textField1.getText());
        }
        return res;
    }

    /**
     * This is a dialog box for move and attack units
     * 
     * @return the user response
     */
    public ArrayList<String> setLevelsOfUnits() {
        // create the text input fields
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();

        // create the dialog and set the content
        TextInputDialog dialog = new TextInputDialog();
        dialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        dialog.setHeaderText(null);
        dialog.setTitle("Enter Units Level and Number");
        dialog.getDialogPane()
                .setContent(new VBox(10, new Label("level : "), textField1, new Label("Number: "), textField2));

        Optional<String> result = dialog.showAndWait();

        // check if the user clicked OK and retrieve the input values
        ArrayList<String> res = new ArrayList<>();
        if (result.isPresent()) {
            res.add(textField1.getText());
            res.add(textField2.getText());
        }
        return res;
    }

    /**
     * Get the territory information for print
     * 
     * @param territoryInfo The territory information
     * @return the territory information in a string
     */
    public String getTerritoryInfo(String terrirtoryName, HashMap<String, String> territoryInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("==================== " + terrirtoryName + " =======================\n");
        sb.append("Player " + territoryInfo.get("Player") + "'s \n");
        sb.append("Units: " + parseUnitsInfo(territoryInfo.get("Unit")) + "\n");
        sb.append("Neighbors: " + territoryInfo.get("Neighbors") + "\n");
        sb.append("Rate: " + territoryInfo.get("Rate") + "\n");
        sb.append("Resource: " + territoryInfo.get("Resource") + "\n");
        return sb.toString();
    }

    /**
     * Get the action information for history to print
     * 
     * @param myOrder The action information
     * @return the action information in a string
     */
    public String getActionInfo(ArrayList<String> myOrder) {
        StringBuilder sb = new StringBuilder();
        String action = myOrder.get(0);
        if (action.equals("r")) {
            sb.append("Successful research.\n");
            return sb.toString();
        }
        String oneOrderContent = myOrder.get(1);
        String[] parts = oneOrderContent.split(",");
        if (action.equals("m")) {
            sb.append("Move from " + parts[0].trim() + " to " + parts[1].trim() + " with " + parts[3].trim() + " units("
                    + parts[2].trim() + " level).\n");
        } else if (action.equals("a")) {
            sb.append("Attack from " + parts[0].trim() + " to " + parts[1].trim() + " with " + parts[3].trim()
                    + " units(" + parts[2].trim() + " level).\n");
        } else if (action.equals("u")) {
            int finallevel = Integer.parseInt(parts[2].trim()) + Integer.parseInt(parts[3].trim());
            sb.append("Upgrade at " + parts[0].trim() + " for number of " + parts[1].trim()
                    + " units. Upgrade from initial level of " + parts[2].trim() + " to level of " + finallevel
                    + ".\n");
        }
        return sb.toString();
    }
}