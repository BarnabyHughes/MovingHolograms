package me.barnaby.movingholograms.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LocationUtil {

    public static Location getLocationInDirection(Location playerLocation, Location targetLocation, int distance) {
        // Get the direction vector from player to target
        Vector direction = targetLocation.clone().toVector().subtract(playerLocation.toVector()).normalize();

        // Calculate the new location
        double x = playerLocation.getX() + direction.getX() * distance;
        double y = playerLocation.getY() + direction.getY() * distance;
        double z = playerLocation.getZ() + direction.getZ() * distance;

        // Create and return the new location
        return new Location(playerLocation.clone().getWorld(), x, y+1, z);
    }

}
