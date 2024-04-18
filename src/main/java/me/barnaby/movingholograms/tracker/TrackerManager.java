package me.barnaby.movingholograms.tracker;

import lombok.Getter;
import me.barnaby.movingholograms.MovingHolograms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class TrackerManager {

    @Getter
    private final List<Tracker> trackers = new ArrayList<>();
    @Getter
    private final MovingHolograms movingHolograms;
    public TrackerManager(MovingHolograms movingHolograms) {
        this.movingHolograms = movingHolograms;
    }


    public Tracker getTracker(String id) {
        return trackers.stream().filter(tracker -> tracker.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
    public void loadTrackers() {
        FileConfiguration config = movingHolograms.getConfigManager().getConfig();

        if (config.isConfigurationSection("trackers")) {
            ConfigurationSection trackersSection = config.getConfigurationSection("trackers");

            for (String trackerKey : trackersSection.getKeys(false)) {

                List<World> worlds = new ArrayList<>();

                if (trackersSection.getConfigurationSection(trackerKey).contains("worlds")) {
                    Bukkit.getWorlds().forEach(world -> {
                        if (world.getName().startsWith(trackersSection.getString(trackerKey + ".worlds")))
                            worlds.add(world);
                    });
                }

                else {
                    World world = Bukkit.getWorld(trackersSection.getString(trackerKey + ".world"));
                    if (world == null) Bukkit.getLogger().warning("World " + trackersSection.getString(trackerKey + ".world") + " not found!");
                    else worlds.add(Bukkit.getWorld(trackersSection.getString(trackerKey + ".world")));
                }

                worlds.forEach(world -> {
                    Bukkit.getLogger().info("Tracker successfully loaded in: " + world.getName());
                    double x = trackersSection.getDouble(trackerKey + ".x");
                    double y = trackersSection.getDouble(trackerKey + ".y");
                    double z = trackersSection.getDouble(trackerKey + ".z");

                    Location location = new Location(world, x,y,z);

                    List<String> hologramLines = trackersSection.getStringList(trackerKey + ".text");

                    Tracker tracker = new Tracker(trackerKey, location, hologramLines, movingHolograms);
                    trackers.add(tracker);
                });

            }
        }
    }

    public void unloadTrackers() {
        trackers.forEach(Tracker::delete);
    }
}
