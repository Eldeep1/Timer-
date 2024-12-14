package Services;

import Model.Timer;
import java.time.Duration;
import java.time.LocalTime;

public class TimerServices {
private int ID;
    public Timer addTimer(int ID, String label, Duration duration) {
        this.ID=ID;
        Timer timer = new Timer();
        timer.setActualDuration(duration);
        //
        timer.setRemainingDuration(duration);

        timer.setLabel(label);
        timer.setID(ID); // use this until we find better solution
        this.startTimer(timer);
        return timer;
    }

    // public void deleteTimer(int ID){
    // remove from the list
    // }

    public void restartTimer(Timer timer) {
        timer.setRemainingDuration(timer.getActualDuration());
        this.startTimer(timer);
    }

    // public void pauseTimer(Timer t){
    // pause by gui??
    // }

    public void startTimer(Timer timer) {
        timer.setStartTime(LocalTime.now());
        timer.setEndTime(timer.getStartTime().plus(timer.getRemainingDuration()));

        // System.out.println(timer.getRemainingDuration());
        Duration duration = timer.getRemainingDuration();
        
        long minutes = duration.toMinutes();
        long seconds = duration.toSeconds() % 60;
        Shared.updateDuration(ID, Long.toString(seconds));
//         System.out.println("Difference: " + minutes + " minutes and " + seconds + "seconds");
    }
}
