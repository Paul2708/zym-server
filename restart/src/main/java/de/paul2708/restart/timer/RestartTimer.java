package de.paul2708.restart.timer;

import java.time.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class holds a timer to schedule the restart task.
 *
 * @author Paul2708
 */
public class RestartTimer {

    private static final LocalTime RESTART_TIME = LocalTime.MIDNIGHT;

    private ScheduledExecutorService executor;

    /**
     * Create a new restart timer by creating a new scheduled executor service.
     */
    public RestartTimer() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Calculate the delay and set the scheduling.
     */
    public void start() {
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Berlin"));
        LocalDateTime todayMidnight = LocalDateTime.of(today, RestartTimer.RESTART_TIME);

        long midnightMillis = todayMidnight.toInstant(ZoneOffset.of("+1")).toEpochMilli()
                - TimeUnit.MINUTES.toMillis(1);
        long delay = midnightMillis - System.currentTimeMillis();

        if (delay <= TimeUnit.MINUTES.toMillis(5)) {
            delay =  todayMidnight.plusDays(1).toInstant(ZoneOffset.of("+1")).toEpochMilli()
                    - System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1);
        }

        this.executor.schedule(new RestartRunnable(), delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop the scheduler.
     */
    public void stop() {
        this.executor.shutdown();
    }
}
