package Services;

import Model.Timer;
import java.time.Duration;
import java.time.LocalTime;

public class TimerServices {
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

    // public void deleteTimer(int ID){
    // remove from the list
    // }

    public void restartTimer(Timer timer, int ID) {
        timer.setRemainingDuration(timer.getActualDuration());
        this.startTimer(timer,ID);
    }

    // public void pauseTimer(Timer t){
    // pause by gui??
    // }

    public void startTimer(Timer timer, int ID) {
        timer.setStartTime(LocalTime.now());
        timer.setEndTime(timer.getStartTime().plus(timer.getRemainingDuration()));

        // System.out.println(timer.getRemainingDuration());
        Duration duration = timer.getRemainingDuration();
  
        Shared.updateDuration(ID, duration);
//         System.out.println("Difference: " + minutes + " minutes and " + seconds + "seconds");
    }
}
