/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import Model.Timer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

/**
 *
 * @author Depogramming
 */
public class Shared {

    public static List<TextField> labeltextField = new ArrayList<>();
    public static List<TextField> timetextField = new ArrayList<>();
    public static List<Integer> identifiers = new ArrayList<>();
    // Map to hold durations (ID -> Duration)

    public static Map<Integer, TimerThread> threads = new HashMap<>();
    public static Map<Integer, Timer> timers = new HashMap<>();

    public static Map<Integer, StringProperty> durations = new HashMap<>();

    private TimerServices service;

    public Shared() {
        this.service = new TimerServices();

    }

    // Method to update duration
    public static void updateDuration(int id, Duration newDuration) {
        if (durations.containsKey(id)) {
            System.out.println("the id of the timer is: " + id + " and the new duration is " + newDuration);
            
            long hours = newDuration.toHours();                    
            long minutes = newDuration.toMinutes()%60;
            long seconds = newDuration.toSeconds() % 60;

            String current_duration = "";
            
            current_duration+=((hours<=9 ? ("0"+Long.toString(hours)):Long.toString(hours)));
            current_duration += " : ";
            current_duration+=((minutes<=9 ? ("0"+Long.toString(minutes)):Long.toString(minutes)));
            current_duration += " : ";
            current_duration+=((seconds<=9 ? ("0"+Long.toString(seconds)):Long.toString(seconds)));
            System.out.println(current_duration);
            durations.get(id).set(current_duration);
        }
    }

    //
    public Timer addTimer(int ID, String label, Duration duration) {
        Timer timer = service.addTimer(ID, label, duration);
        TimerThread thread = new TimerThread(timer);
        threads.put(ID, thread);
        timers.put(ID, timer);
        thread.start();
        // update gui??
        return timer;
    }
}
