package Services;

import Model.Timer;
import java.time.Duration;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;

public class TimerServices {
    
    private static final Semaphore semaphore = new Semaphore(1);
    
    public Timer addTimer(int ID, String label, Duration duration) {
        Timer timer = new Timer();
        timer.setActualDuration(duration);
        //
        timer.setRemainingDuration(duration);

        timer.setLabel(label);
        timer.setID(ID); // use this until we find better solution
        this.startTimer(timer, ID);
        return timer;
    }

    public void startTimer(Timer timer, int ID) {
        timer.setStartTime(LocalTime.now());
        timer.setEndTime(timer.getStartTime().plus(timer.getRemainingDuration()));

        // System.out.println(timer.getRemainingDuration());
        Duration duration = timer.getRemainingDuration();
  
        Shared.updateDuration(ID, duration);
//         System.out.println("Difference: " + minutes + " minutes and " + seconds + "seconds");
    }
    
    public void notifyUser(TimerThread thread){
        try {
            semaphore.acquire(); 
            System.out.println("in the critical section");

            // gui show notfication
            System.out.println(thread.getTimer().getLabel());
            Thread.sleep(3000);
            
            semaphore.release(); 
            System.out.println("leaving the critical section");
        } catch (InterruptedException ex) {
            Logger.getLogger(TimerServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
  
        }
    }
}
