package ViewModel.CurrentTimers;

import Services.Shared;
import ViewModel.HomePage.HomePageController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
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


    @FXML
    private VBox textAreaContainer; // Add a VBox in your main FXML to hold the AnchorPanes

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize your controller here

        createAnchorPanes();

    }

    private void createAnchorPanes() {
    try {
        // Iterate over the map entries
        for (Map.Entry<Integer, TextField> entry : Shared.labeltextField.entrySet()) {
            int id = entry.getKey(); 
            TextField textField = entry.getValue(); // Get the TextField (value)

            // Load the AnchorPane template
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CurrentTimers/AnchorPaneTemplate.fxml"));
            AnchorPane anchorPane = loader.load();

            
            Label label = (Label) anchorPane.lookup("#description");
            label.setText(textField.getText());

            // Initialize the controller for this AnchorPane
            AnchorPaneController anchorPaneController = loader.getController();
            anchorPaneController.setID(id);

            // Add the AnchorPane to the container
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
