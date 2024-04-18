package me.barnaby.movingholograms.commands;

import me.barnaby.movingholograms.tracker.Tracker;
import me.barnaby.movingholograms.tracker.TrackerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RogueTrackerCommand implements CommandExecutor {

    private final TrackerManager trackerManager;

    public RogueTrackerCommand(TrackerManager trackerManager) {
        this.trackerManager = trackerManager;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!s.equalsIgnoreCase("roguetracker")
                && (!s.equalsIgnoreCase("movingholograms"))
        && (!s.equalsIgnoreCase("tracker"))) return true;

        if (strings.length<3 || !commandSender.hasPermission("*")) {
            sendHelp(commandSender);
            return true;
        }

        if (strings[2].equalsIgnoreCase("on") || strings[2].equalsIgnoreCase("off")) {
            Tracker tracker = trackerManager.getTracker(strings[0]);
            if (tracker == null) {
                commandSender.sendMessage(ChatColor.RED + "Tracker " + strings[0] + " not found!");
                return true;
            }

            Player player = Bukkit.getPlayer(strings[1]);
            if (player == null) {
                commandSender.sendMessage(ChatColor.RED + "Player " + strings[1] + " not found!");
                return true;
            }


            if (strings[2].equalsIgnoreCase("on"))
                tracker.enableHologram(player.getUniqueId());
            else
                tracker.disableHologram(player.getUniqueId());
            commandSender.sendMessage(ChatColor.GREEN + "Success!");
        }
        else sendHelp(commandSender);

        return false;
    }

    private void sendHelp(CommandSender commandSender) {
        commandSender.sendMessage("");
        commandSender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "MovingHolograms - Made by Barnaby (barneyh)");
        commandSender.sendMessage(ChatColor.WHITE + "Commands:");
        commandSender.sendMessage(ChatColor.WHITE + "/tracker <tracker> <player> on");
        commandSender.sendMessage(ChatColor.WHITE + "/tracker <tracker> <player> off");
        commandSender.sendMessage("");
    }
}
