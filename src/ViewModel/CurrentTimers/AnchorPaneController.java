package ViewModel.CurrentTimers;

import Model.Timer;
import Services.Shared;
import Services.TimerThread;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.time.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AnchorPaneController {

    private int ID;

    @FXML
    private Label duration;
    @FXML
    private Button pauseButton;

        @FXML
    private AnchorPane myPane;
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

        TimerThread thread = Shared.threads.get(ID);
        if (thread.isAlive()) {
            Timer timer = Shared.timers.get(ID);
            timer.setRemainingDuration(thread.getTimer().getRemainingDuration());
            Shared.timers.put(ID, timer);
            thread.stopThread();
            pauseButton.setText("resume");
            System.out.println("this thread id is : " + ID);
        } else {
            Timer timer = Shared.timers.get(ID);
            TimerThread newThread = new TimerThread(timer);
            Shared.threads.put(ID, newThread);
            newThread.start();
            pauseButton.setText("pause");

        }

    }

    @FXML
    public void onResetButtonClick() {
//        Shared.updateDuration(ID, "00:00");
        Timer timer = Shared.timers.get(ID);
        timer.setRemainingDuration(timer.getActualDuration());

        TimerThread oldThread = Shared.threads.get(ID);
        oldThread.stopThread();

        TimerThread thread = new TimerThread(timer);
        Shared.threads.put(ID, thread);
        thread.start();
        pauseButton.setText("pause");

    }

    @FXML
    public void onDeleteButtonClick() {
        Shared.threads.get(ID).stopThread();
        
        Shared.timers.remove(ID);
        Shared.durations.remove(ID);
        Shared.labeltextField.remove(ID);
        Shared.threads.remove(ID);
        
        myPane.getChildren().clear();

    }

}
