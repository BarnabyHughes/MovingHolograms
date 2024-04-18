package me.barnaby.movingholograms.listeners;

import me.barnaby.movingholograms.MovingHolograms;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final MovingHolograms movingHolograms;

    public PlayerJoin(MovingHolograms movingHolograms) {
        this.movingHolograms = movingHolograms;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        movingHolograms.getTrackerManager().getTrackers().forEach(tracker -> {
            tracker.hideHologram(event.getPlayer().getUniqueId());
        });
    }


}
