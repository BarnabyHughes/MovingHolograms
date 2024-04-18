package me.barnaby.movingholograms;

import lombok.Getter;
import me.barnaby.movingholograms.commands.RogueTrackerCommand;
import me.barnaby.movingholograms.config.ConfigManager;
import me.barnaby.movingholograms.placeholder.MovingHologramsExpansion;
import me.barnaby.movingholograms.tracker.TrackerManager;
import me.barnaby.movingholograms.tracker.TrackerRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MovingHolograms extends JavaPlugin {

    @Getter
    private final ConfigManager configManager = new ConfigManager(this);

    @Getter
    private final TrackerManager trackerManager = new TrackerManager(this);

    @Override
    public void onEnable() {
        configManager.init();

        trackerManager.loadTrackers();
        new TrackerRunnable(trackerManager).runTaskTimer(this, 20, 2);

        registerCommands();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new MovingHologramsExpansion(trackerManager).register();
        }
    }

    @Override
    public void onDisable() {
        trackerManager.unloadTrackers();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        //pm.registerEvents();
    }

    private void registerCommands() {
        this.getCommand("movingholograms").setExecutor(new RogueTrackerCommand(trackerManager));


    }
}