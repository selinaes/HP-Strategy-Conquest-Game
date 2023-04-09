package edu.duke.ece651.team16.controller;

import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.Optional;

public class GamePlayDisplay {
    /**
     * This is a private method used to format the units information received from
     * the server
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

    public int getUnitNum(String unitsInfo) {
        int sum = 0;
        String[] unitsInfoArray = unitsInfo.split(",");
        for (int i = 0; i < 7; i++) {
            sum += Integer.parseInt(unitsInfoArray[i]);
        }
        return sum;
    }

    public ArrayList<Integer> getUnitNumArray(String unitsInfo) {
        ArrayList<Integer> unitNumArray = new ArrayList<>();
        String[] unitsInfoArray = unitsInfo.split(",");
        for (int i = 0; i < 7; i++) {
            unitNumArray.add(Integer.parseInt(unitsInfoArray[i]));
        }
        return unitNumArray;
    }

    // public ArrayList<String> setUpgradeInfo() {
    // // create the text input fields, level, intiial, amount
    // TextField textField1 = new TextField();
    // TextField textField2 = new TextField();
    // TextField textField3 = new TextField();

    // // create the dialog and set the content
    // TextInputDialog dialog = new TextInputDialog();
    // dialog.setHeaderText(null);
    // dialog.setTitle("Enter Upgrade UnitNumber, Initial Level, # levels to
    // Upgrade");
    // dialog.getDialogPane()
    // .setContent(new VBox(10, new Label("Upgrade UnitNumber: "), textField1, new
    // Label("Initial Level: "),
    // textField2,
    // new Label("# levels to Upgrade: "), textField3));

    // // show the dialog and wait for the user response
    // System.out.println("Before showAndWait");
    // Optional<String> result = dialog.showAndWait();
    // System.out.println("After showAndWait");

    // // check if the user clicked OK and retrieve the input values
    // ArrayList<String> res = new ArrayList<>();
    // System.out.println("Before if");
    // if (result.isPresent()) {
    // res.add(textField1.getText());
    // System.out.println(textField1.getText());
    // res.add(textField2.getText());
    // System.out.println(textField2.getText());
    // res.add(textField3.getText());
    // System.out.println(textField3.getText());
    // // process the input values here
    // }
    // System.out.println(res);
    // return res;
    // }

    // private ArrayList<String> setLevelsOfUnits() {
    // // create the text input fields
    // TextField textField1 = new TextField();
    // TextField textField2 = new TextField();

    // // create the dialog and set the content
    // TextInputDialog dialog = new TextInputDialog();
    // dialog.setHeaderText(null);
    // dialog.setTitle("Enter Units Level and Number");
    // dialog.getDialogPane()
    // .setContent(new VBox(10, new Label("level : "), textField1, new
    // Label("Number: "), textField2));

    // // show the dialog and wait for the user response
    // Optional<String> result = dialog.showAndWait();

    // // check if the user clicked OK and retrieve the input values
    // ArrayList<String> res = new ArrayList<>();
    // if (result.isPresent()) {
    // res.add(textField1.getText());
    // res.add(textField2.getText());
    // // process the input values here
    // }
    // return res;
    // }

}