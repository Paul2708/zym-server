package de.paul2708.restart.timer;

import de.paul2708.restart.RestartPlugin;
import org.bukkit.Bukkit;

/**
 * This runnable restarts the server.
 *
 * @author Paul2708
 */
public class RestartRunnable implements Runnable {

    /**
     * Call scheduler to dispatch commands.
     */
    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RestartPlugin.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "chat announce &c&lDer Server wird in einer Minute neu gestartet!");
        });

        Bukkit.getScheduler().scheduleSyncDelayedTask(RestartPlugin.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
        }, 600);

        Bukkit.getScheduler().scheduleSyncDelayedTask(RestartPlugin.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, 1200);
    }
}
