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
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

/**
 *
 * @author Depogramming
 */
public class Shared {
        public static List<TextField> labeltextField=new ArrayList<>();
        public static List<TextField> timetextField=new ArrayList<>();
        public static List<Integer> identifiers=new ArrayList<>();
    // Map to hold durations (ID -> Duration)

    public static Map<Integer, TimerThread> threads = new HashMap<>();
    public static Map<Integer, Timer> timers = new HashMap<>();
    
    public static Map<Integer, StringProperty> durations = new HashMap<>();
    
    private TimerServices service;

    public Shared() {
            this.service = new TimerServices();

    }
    
    

    // Method to update duration
    public static void updateDuration(int id, String newDuration) {
        if (durations.containsKey(id)) {
            System.out.println("the id of the timer is: "+ id+" and the new duration is"+newDuration);
            
            durations.get(id).set(newDuration);
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
