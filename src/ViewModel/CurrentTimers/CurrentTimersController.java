package ViewModel.CurrentTimers;

import Services.Shared;
import ViewModel.HomePage.HomePageController;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CurrentTimersController implements Initializable {

    private List<TextField> labelTextField;
    private List<TextField> timeTextField;
    private List<Integer> identifiers;
    @FXML
    private VBox textAreaContainer; // Add a VBox in your main FXML to hold the AnchorPanes

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize your controller here
        this.labelTextField = Shared.labeltextField;
        this.timeTextField = Shared.timetextField;
        this.identifiers = Shared.identifiers;
        createAnchorPanes();

    }

    private void createAnchorPanes() {
        try {
            for (int i = 0; i < labelTextField.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CurrentTimers/AnchorPaneTemplate.fxml"));
                AnchorPane anchorPane = loader.load();

                // Set the label text
                Label label = (Label) anchorPane.lookup("#description");
                label.setText(labelTextField.get(i).getText());
//                
//                // Set the duration text
//                Label time = (Label) anchorPane.lookup("#duration");
//                time.setText(timeTextField.get(i).getText());

                int id = identifiers.get(i);
                
//                Shared.durations.putIfAbsent(id, new SimpleStringProperty(timeTextField.get(i).getText())); // Default value
                //set the ID to each pane

                // Set the controller for the AnchorPane
                AnchorPaneController anchorPaneController = loader.getController();
                anchorPaneController.setID(id);

                textAreaContainer.getChildren().add(anchorPane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

@FXML
    public void callAddTimersPage(ActionEvent e) {
        
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomePage/HomePage.fxml"));
            root = loader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
