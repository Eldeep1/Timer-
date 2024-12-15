/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import Model.Timer;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Depogramming
 */
public class TimerService {
    private static final Semaphore semaphore = new Semaphore(1);
    public static Duration pomodoro= Duration.ZERO;
    public static Map<Integer, TextField> labeltextField = new HashMap<>();
      public static final IntegerProperty numberOfSessions = new SimpleIntegerProperty(0);
    public static Map<Integer, TimerThread> threads = new HashMap<>();
    public static Map<Integer, Timer> timers = new HashMap<>();

    public static Map<Integer, StringProperty> durations = new HashMap<>();


    public TimerService() {

    }

    // Method to update duration
    public static void updateDuration(int id, Duration newDuration) {
        if (durations.containsKey(id)) {

            long hours = newDuration.toHours();
            long minutes = newDuration.toMinutes() % 60;
            long seconds = newDuration.toSeconds() % 60;

            String current_duration = "";

            current_duration += ((hours <= 9 ? ("0" + Long.toString(hours)) : Long.toString(hours)));
            current_duration += " : ";
            current_duration += ((minutes <= 9 ? ("0" + Long.toString(minutes)) : Long.toString(minutes)));
            current_duration += " : ";
            current_duration += ((seconds <= 9 ? ("0" + Long.toString(seconds)) : Long.toString(seconds)));
            durations.get(id).set(current_duration);
        }
    }

    //
    public Timer addTimer(int ID, String label, Duration duration) {
        Timer timer = new Timer();

        // update gui??
        timer.setActualDuration(duration);
        //
        timer.setRemainingDuration(duration);
        timer.setLabel(label);
        timer.setID(ID); // use this until we find better solution
        this.startTimer(timer, ID);
        TimerThread thread = new TimerThread(timer);
        threads.put(ID, thread);
        timers.put(ID, timer);
        thread.start();
        return timer;
    }
    
    public Timer addTimer(int ID, String label, Duration duration, boolean promodoro) {
        Timer timer = addTimer(ID, label, duration);
        timer.setPromodoro(promodoro);
        return timer;
    }
    
    public void startTimer(Timer timer, int ID) {
        timer.setStartTime(LocalTime.now());
        timer.setEndTime(timer.getStartTime().plus(timer.getRemainingDuration()));

        Duration duration = timer.getRemainingDuration();

        TimerService.updateDuration(ID, duration);
    }
    




    public void notifyUser(TimerThread thread) {
        try {
            semaphore.acquire();
            System.out.println("in the critical section");

            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Timer Notification");
                alert.setHeaderText("Timer Alert");
                alert.setContentText("The timer \"" + thread.getTimer().getLabel() + "\" has finished!");

                // Add a custom icon
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/Assets/alarm.png"))); // Add your custom icon here

                alert.showAndWait();
                if (thread.getTimer().isPromodoro()) {
                   numberOfSessions.set(numberOfSessions.get() + 1);

                }
                // Release the semaphore after the user clicks OK
                semaphore.release();
                
                System.out.println("leaving the critical section");
                
            });

        } catch (InterruptedException ex) {
            System.out.println(ex);
        } 
    }
}
