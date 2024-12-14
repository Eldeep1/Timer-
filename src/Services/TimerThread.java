/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;


import Model.Timer;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class TimerThread extends Thread {
    private final Timer timer;
    private final int ID;
    private final TimerService service;
    private volatile boolean running;
    private volatile boolean paused;
     

    public TimerThread(Timer timer) {
        this.timer = timer;
        this.ID = timer.getID();
        this.running = true;
        this.paused = false;
        this.service = new TimerService();
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    public void run() {
        while (running && !timer.getRemainingDuration().equals(Duration.ZERO)) {
            synchronized (this) {
                while (paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Logger.getLogger(TimerThread.class.getName()).log(Level.SEVERE,
                                "Thread interrupted while paused", e);
                    }
                }
            }

            // Decrease timer by 1 second
            timer.setRemainingDuration(timer.getRemainingDuration().minusSeconds(1));

            // Call the service method (assuming it updates some UI or logs)
            service.startTimer(timer,ID);
            //meow
               // call the update function
            try {
                System.out.println(
                        "Timer " + timer.getLabel() + ": " + timer.getRemainingDuration().toSeconds()
                                + " seconds left.");
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                Logger.getLogger(TimerThread.class.getName()).log(Level.SEVERE, "Thread sleep interrupted", e);
            }
        }
        if(timer.getRemainingDuration().equals(Duration.ZERO)){
            service.notifyUser(this);
            //System.out.println("Timer " + ID + " has finished.");
        }
        else
            System.out.println("Timer " + ID + " has paused.");
    }

    // Stop the thread completely
    public void stopThread() {
        running = false;
        resumeThread(); // In case it's paused, ensure it can exit
    }

    // Pause the timer thread
    public synchronized void pauseThread() {
        if (!paused) {
            paused = true;
            System.out.println("Timer " + ID + " is paused.");
        }
    }

    // Resume the timer thread
    public synchronized void resumeThread() {
        if (paused) {
            paused = false;
            notify(); // Notify the waiting thread to continue
            System.out.println("Timer " + ID + " is resumed.");
        }
    }
}