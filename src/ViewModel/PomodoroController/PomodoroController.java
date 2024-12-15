/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ViewModel.PomodoroController;

import Model.Timer;
import Services.TimerService;
import Services.TimerThread;
import ViewModel.HomePage.HomePageController;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Depogramming
 */
public class PomodoroController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label duration;

    private int ID;

    @FXML
    private TextField promoTime;
    @FXML
    private Button pauseButton;

    @FXML
    private Label sessionNum;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.ID = 999;

        sessionNum.setText(String.valueOf(TimerService.numberOfSessions));

        TimerService.numberOfSessions.addListener((observable, oldValue, newValue) -> {
            javafx.application.Platform.runLater(() -> {
                sessionNum.setText(String.valueOf(newValue.intValue()));
            });
        });

        javafx.application.Platform.runLater(() -> {
            sessionNum.setText(String.valueOf(TimerService.numberOfSessions.get()));
        });

    }

    @FXML
    public void onPauseButtonClick() {
        TimerThread thread = TimerService.threads.get(ID);

        if (thread != null) {
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

    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d: %02d: %02d", hours, minutes, seconds);
    }

    @FXML
    public void onResetButtonClick() {
        Timer timer = TimerService.timers.get(ID);
        timer.setRemainingDuration(timer.getActualDuration());

        TimerThread oldThread = TimerService.threads.get(ID);
        oldThread.stopThread();

        TimerThread thread = new TimerThread(timer);
        TimerService.threads.put(ID, thread);

        // Convert Duration to String and update the label
        duration.setText(formatDuration(timer.getActualDuration()));

        // Uncomment to start the thread if required
        // thread.start();
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
    public void startPromodoro(ActionEvent e) {
        TimerService shared = new TimerService();
//            Shared.timetextField.put(IDTracker,tempTimetextField.get(i));
        TimerService.durations.putIfAbsent(ID, new SimpleStringProperty(promoTime.getText()));

        // Get duration from text field and convert to Duration object
        Duration timerDuration = parseDuration(promoTime.getText());

        shared.addTimer(ID, "session ended !", timerDuration, true);

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
