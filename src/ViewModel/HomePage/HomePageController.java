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

    @FXML
    public void onDoneButtonClick(ActionEvent e) {
        Shared shared = new Shared();
        for (int i = 0; i < tempLabeltextField.size(); i++) {
            Shared.labeltextField.add(tempLabeltextField.get(i));
            Shared.timetextField.add(tempTimetextField.get(i));
            Shared.identifiers.add(IDTracker);

            Shared.durations.putIfAbsent(IDTracker, new SimpleStringProperty(tempTimetextField.get(i).getText())); // Default
                                                                                                                   // value

            // System.out.println("the timer with time: " + timetextField.get(i).getText() +
            // " and label: " + labeltextField.get(i).getText() + "is set");
            shared.addTimer(IDTracker, tempLabeltextField.get(i).getText(), Duration.ofSeconds(20));
            IDTracker++;

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
