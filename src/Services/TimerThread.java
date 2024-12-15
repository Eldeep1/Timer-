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

    public TimerThread(Timer timer) {
        this.timer = timer;
        this.ID = timer.getID();
        this.running = true;
        this.service = new TimerService();
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    public void run() {
        while (running && !timer.getRemainingDuration().equals(Duration.ZERO)) {
            timer.setRemainingDuration(timer.getRemainingDuration().minusSeconds(1));

            service.startTimer(timer, ID);
            try {
                System.out.println(
                        "Timer " + timer.getLabel() + ": " + timer.getRemainingDuration().toSeconds()
                                + " seconds left.");
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                Logger.getLogger(TimerThread.class.getName()).log(Level.SEVERE, "Thread sleep interrupted", e);
            }
        }
        if (timer.getRemainingDuration().equals(Duration.ZERO))
            service.notifyUser(this);
        // } else
        // System.out.println("Timer " + ID + " has paused.");
    }

    public void stopThread() {
        running = false;
    }
}