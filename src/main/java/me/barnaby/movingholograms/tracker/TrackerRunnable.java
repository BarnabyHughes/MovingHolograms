package me.barnaby.movingholograms.tracker;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TrackerRunnable extends BukkitRunnable {

    private final TrackerManager trackerManager;

    public TrackerRunnable(TrackerManager trackerManager) {
        this.trackerManager = trackerManager;
    }


    @Override
    public void run() {
        trackerManager.getTrackers().forEach(tracker -> {
            tracker.reloadHologramLocation();
        });
    }
}
