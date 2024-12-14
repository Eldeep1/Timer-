/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ViewModel.HomePage;

import Services.Shared;
import ViewModel.CurrentTimers.CurrentTimersController;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Depogramming
 */
public class HomePageController implements Initializable {

    private List<AnchorPane> anchorPane;
    @FXML
    private VBox textAreaContainer;

    private List<TextField> timetextField;
    private List<TextField> labeltextField;
    private List<Integer> identifiers;

    private List<TextField> tempTimetextField;
    private List<TextField> tempLabeltextField;
    private List<Integer> tempIdentifiers;
    private static int IDTracker = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the TextArea array
        anchorPane = new ArrayList<>();

        // initializtion temporary array lists
        tempTimetextField = new ArrayList<>();
        tempLabeltextField = new ArrayList<>();
        tempIdentifiers = new ArrayList<>();

        // initializing the main array lists
        timetextField = Shared.timetextField;
        labeltextField = Shared.labeltextField;
        identifiers = Shared.identifiers;

    }

    @FXML
    public void addNewEntry(ActionEvent e) {

        // should add validations here
        AnchorPane newAnchorPane = new AnchorPane();
        newAnchorPane.setPrefHeight(74.0);
        newAnchorPane.setPrefWidth(448.0);

        // tempIdentifiers.add(IDTracker);
        // IDTracker++;

        // Create new TextFields and add them to the AnchorPane
        TextField timeTextField = new TextField();
        timeTextField.setLayoutX(8.0);
        timeTextField.setLayoutY(25.0);
        timeTextField.setPromptText("HH:MM:SS");
        // Set a default value for the timeTextField
        timeTextField.setText("00:00:00");
        tempTimetextField.add(timeTextField);

        TextField labelTextField = new TextField();
        labelTextField.setLayoutX(179.0);
        labelTextField.setLayoutY(25.0);
        labelTextField.setPromptText("Description");
        tempLabeltextField.add(labelTextField);

        newAnchorPane.getChildren().addAll(timeTextField, labelTextField);

        anchorPane.add(newAnchorPane);

        System.out.println("lol");

        textAreaContainer.getChildren().add(newAnchorPane);
    }

    private static Duration parseDuration(String timeString) {
        String[] parts = timeString.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid time format: " + timeString);
        }

        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);

            // Validation
            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
                throw new IllegalArgumentException("Invalid time values: " + timeString);
            }

            return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time format: " + timeString, e);
        }
    }

    @FXML
    public void onDoneButtonClick(ActionEvent e) {
        Shared shared = new Shared();
        for (int i = 0; i < tempLabeltextField.size(); i++) {
            IDTracker++;
            Shared.labeltextField.add(tempLabeltextField.get(i));
            Shared.timetextField.add(tempTimetextField.get(i));
            Shared.identifiers.add(IDTracker);

            Shared.durations.putIfAbsent(IDTracker, new SimpleStringProperty(tempTimetextField.get(i).getText()));

            // Get duration from text field and convert to Duration object
            Duration timerDuration = parseDuration(tempTimetextField.get(i).getText());
            System.out.println(timerDuration);
            shared.addTimer(IDTracker, tempLabeltextField.get(i).getText(), timerDuration);
        }

        callCurrentTimersPage(e);
    }

    @FXML
    public void callCurrentTimersPage(ActionEvent e) {

        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CurrentTimers/CurrentTimers.fxml"));
            root = loader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
