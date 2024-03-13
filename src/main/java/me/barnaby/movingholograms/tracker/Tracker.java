package me.barnaby.movingholograms.tracker;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import jdk.vm.ci.meta.SpeculationLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.barnaby.movingholograms.MovingHolograms;
import me.barnaby.movingholograms.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tracker {

    @Getter
    private final Location holoLocation;
    @Getter
    private final List<String> hologramLines;
    //private Hologram hologram;
    @Getter
    private final String id;
    private final MovingHolograms movingHolograms;
    private final Map<Player, Hologram> canSee = new HashMap<>();

    public Tracker(String id, Location holoLocation, List<String> hologramLines, MovingHolograms movingHolograms) {
        this.id = id;
        this.holoLocation = holoLocation;
        this.hologramLines = hologramLines;
        this.movingHolograms = movingHolograms;
    }

    public void enableHologram(Player player) {
        if (canSee.get(player) == null) {
            canSee.put(player, DHAPI.createHologram(id+ "_" + player.getName(), holoLocation.clone()));
            hologramLines.forEach(line -> {
                DHAPI.addHologramLine(canSee.get(player), line);
            });
        }
        canSee.get(player).show(player, 0);
    }
    public void disableHologram(Player player) {
        canSee.remove(player);
        if (canSee.get(player) != null) canSee.get(player).delete();

    }

    public void reloadHologramLocation() {
        canSee.keySet().forEach(player -> {
            int distance = movingHolograms.getConfigManager().getConfig().getInt("config.distance_from_player");
            if (player.getWorld() != canSee.get(player).getLocation().getWorld()) return;
            if (player.getLocation().distance(holoLocation.clone()) < distance) {
                DHAPI.moveHologram(id + "_" + player.getName(), holoLocation.clone().add(0, 1, 0));
                return;
            }
            Location location1 = LocationUtil.getLocationInDirection(player.getLocation().clone(), holoLocation.clone(), distance);
            DHAPI.moveHologram(id+ "_" + player.getName(), location1.clone());
        });
    }
}
