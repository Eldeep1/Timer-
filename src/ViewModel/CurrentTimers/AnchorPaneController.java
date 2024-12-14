package ViewModel.CurrentTimers;

import Model.Timer;
import Services.Shared;
import Services.TimerThread;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.time.Duration;
public class AnchorPaneController {
    private int ID;

    @FXML
    private Label duration;

public void setID(int id) {
    this.ID = id;
    
    // Add a listener to update the UI when the duration changes
    Shared.durations.get(ID).addListener((observable, oldValue, newValue) -> {
        // Ensure the UI update is on the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            duration.setText(newValue);
        });
    });

    // Initial UI update
    javafx.application.Platform.runLater(() -> {
        duration.setText(Shared.durations.get(ID).get());
    });
}
    @FXML
    public void onPauseButtonClick() {
        
        System.out.println("we clicked on pause");
        
                Model.Timer timer =new Timer();
                timer.setID(ID);
                
        TimerThread thread = new TimerThread(timer);
                
        timer.setRemainingDuration(thread.getTimer().getRemainingDuration());
        Shared.timers.put(ID, timer);
        thread.stopThread();

        
    }
    
    @FXML
    public void onResetButtonClick(){
                Shared.updateDuration(ID, "00:00");

//        TextField newOne= new TextField("meowww");
//        Shared.labeltextField.set(Shared.identifiers.indexOf(ID), newOne);
    }
    
    @FXML
    public void onDeleteButtonClick(){
        System.out.println("we clicked on delete button");
    }
   
}