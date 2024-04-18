package me.barnaby.movingholograms.placeholder;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.barnaby.movingholograms.tracker.Tracker;
import me.barnaby.movingholograms.tracker.TrackerManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MovingHologramsExpansion extends PlaceholderExpansion {


    private TrackerManager trackerManager;
    public MovingHologramsExpansion(TrackerManager trackerManager) {
        this.trackerManager = trackerManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "movingholograms";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Barnaby";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }


    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        Tracker tracker = trackerManager.getTracker(params);
        if (tracker == null) {
            Bukkit.getLogger().warning("Placeholder: " + params + " is not a valid tracker!");
            return "null";
        }
        return String.valueOf(getFormattedDistance(player.getLocation(), tracker.getHoloLocation()));
    }

    public String getFormattedDistance(Location location1, Location location2) {
        int roundingFigures = trackerManager.getMovingHolograms().getConfigManager().getConfig().getInt("config.decimal_places_for_distance");

        double distance = location1.distance(location2);

        // Check if the distance is an integer (no decimal places)
        if (distance % 1 == 0) {
            return String.valueOf((int) distance);
        } else {
            // Round the distance to the specified number of decimal places
            BigDecimal roundedDistance = BigDecimal.valueOf(distance).setScale(roundingFigures, RoundingMode.HALF_UP);
            return roundedDistance.stripTrailingZeros().toPlainString();
        }
    }
}
