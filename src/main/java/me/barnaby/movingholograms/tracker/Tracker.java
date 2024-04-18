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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Tracker {

    @Getter
    private final Location holoLocation;
    @Getter
    private final List<String> hologramLines;
    //private Hologram hologram;
    @Getter
    private final String id;
    private final MovingHolograms movingHolograms;
    private final Map<UUID, Hologram> canSee = new HashMap<>();

    public Tracker(String id, Location holoLocation, List<String> hologramLines, MovingHolograms movingHolograms) {
        this.id = id;
        this.holoLocation = holoLocation;
        this.hologramLines = hologramLines;
        this.movingHolograms = movingHolograms;
    }

    public void enableHologram(UUID player) {
        if (canSee.get(player) == null) {
            canSee.put(player, DHAPI.createHologram(id + "_" +
                    Bukkit.getPlayer(player).getName(), holoLocation.clone()));
            hologramLines.forEach(line -> {
                DHAPI.addHologramLine(canSee.get(player), line);
            });
        }
        Hologram hologram = canSee.get(player);
        Bukkit.getOnlinePlayers().forEach(pl -> {
            if (pl.getUniqueId() == player) return;
            hologram.setHidePlayer(pl);
        });}

    public void showHologram(UUID player) {
        if (canSee.get(player) != null) canSee.get(player).show(Bukkit.getPlayer(player), 0);
    }

    public void hideHologram(UUID player) {
        if (canSee.get(player) != null) canSee.get(player).hide(Bukkit.getPlayer(player));
    }


    public void disableHologram(UUID player) {
        if (canSee.get(player) != null) canSee.get(player).delete();
        canSee.remove(player);
    }

    public void delete() {
        canSee.values().forEach(Hologram::delete);
    }

    public void reloadHologramLocation() {
        canSee.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            int distance = movingHolograms.getConfigManager().getConfig().getInt("config.distance_from_player");
            if (player.getWorld() != canSee.get(uuid).getLocation().getWorld()) return;
            if (player.getLocation().distance(holoLocation.clone()) < distance) {
                DHAPI.moveHologram(id + "_" + player.getName(), holoLocation.clone().add(0, 1, 0));
                return;
            }
            Location location1 = LocationUtil.getLocationInDirection(player.getLocation().clone(), holoLocation.clone(), distance);
            DHAPI.moveHologram(id+ "_" + player.getName(), location1.clone());
        });
    }
}
