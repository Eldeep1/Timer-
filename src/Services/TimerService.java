package Services;

import Model.Timer;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class TimerService {

    private static final Semaphore semaphore = new Semaphore(1);
    public static Duration pomodoro = Duration.ZERO;
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

    public Timer addTimer(int ID, String label, Duration duration) {
        Timer timer = new Timer();

        timer.setActualDuration(duration);
        timer.setRemainingDuration(duration);
        timer.setLabel(label);
        timer.setID(ID);
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
            // System.out.println("Entering the critical section");
            System.out.println("Thread " + thread.getTimer().getLabel() + " is in the critical section");

            javafx.application.Platform.runLater(() -> {
                AudioClip notificationSound = new AudioClip(
                        getClass().getResource("/Assets/notification_sound.mp3").toString());
                notificationSound.play();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Timer Notification");
                alert.setHeaderText("Timer Alert");

                // Initialize countdown
                IntegerProperty countdown = new SimpleIntegerProperty(10);

                // Set initial content text
                alert.setContentText("The timer \"" + thread.getTimer().getLabel() + "\" has finished!\nClosing in "
                        + countdown.get() + " seconds.");

                // Add a custom icon
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/Assets/alarm.png")));

                // Create a Timeline for the countdown
                Timeline timeline = new Timeline(
                        new KeyFrame(
                                javafx.util.Duration.seconds(1),
                                event -> {
                                    countdown.set(countdown.get() - 1);
                                    if (countdown.get() <= 0) {
                                        alert.close();
                                    } else {
                                        alert.setContentText("The timer \"" + thread.getTimer().getLabel()
                                                + "\" has finished!\nClosing in " + countdown.get() + " seconds.");
                                    }
                                }));
                timeline.setCycleCount(10);
                timeline.play();

                // Handle alert closure
                alert.setOnHidden(event -> {
                    if (thread.getTimer().isPromodoro()) {
                        numberOfSessions.set(numberOfSessions.get() + 1);
                    }
                    notificationSound.stop();
                    timeline.stop();
                    semaphore.release();
                    System.out.println("Thread: " + thread.getTimer().getLabel() + " is leaving the critical section");
                });

                alert.show();
            });

        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
