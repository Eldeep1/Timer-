package ViewModel.CurrentTimers;

import Model.Timer;
import Services.TimerService;
import Services.TimerThread;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
        TimerService.durations.get(ID).addListener((observable, oldValue, newValue) -> {
            // Ensure the UI update is on the JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                duration.setText(newValue);
            });
        });

        // Initial UI update
        javafx.application.Platform.runLater(() -> {
            duration.setText(TimerService.durations.get(ID).get());
        });
    }

    @FXML
    public void onPauseButtonClick() {

        TimerThread thread = TimerService.threads.get(ID);
        if (thread.isAlive()) {
            Timer timer = TimerService.timers.get(ID);
            timer.setRemainingDuration(thread.getTimer().getRemainingDuration());
            TimerService.timers.put(ID, timer);
            thread.stopThread();
            pauseButton.setText("resume");
        } else if (TimerService.timers.get(ID).getRemainingDuration().equals(Duration.ZERO)) {

        } else {
            Timer timer = TimerService.timers.get(ID);
            TimerThread newThread = new TimerThread(timer);
            TimerService.threads.put(ID, newThread);
            newThread.start();
            pauseButton.setText("pause");

        }

    }

    @FXML
    public void onResetButtonClick() {
        // Shared.updateDuration(ID, "00:00");
        Timer timer = TimerService.timers.get(ID);
        timer.setRemainingDuration(timer.getActualDuration());

        TimerThread oldThread = TimerService.threads.get(ID);
        oldThread.stopThread();

        TimerThread thread = new TimerThread(timer);
        TimerService.threads.put(ID, thread);
        thread.start();
        pauseButton.setText("pause");

    }

    @FXML
    public void onDeleteButtonClick() {
        TimerService.threads.get(ID).stopThread();

        TimerService.timers.remove(ID);
        TimerService.durations.remove(ID);
        TimerService.labeltextField.remove(ID);
        TimerService.threads.remove(ID);

        myPane.getChildren().clear();

    }

}
