package io.github.m1gw.projectplugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.List;

public class OnPlayerJoin implements Listener {
    // Define the corners of the allowed area
    private final Location corner1 = new Location(Bukkit.getWorld("world"), 32, -5, -17);
    private final Location corner2 = new Location(Bukkit.getWorld("world"), -27, 10, 18);


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!event.getPlayer().isOp()){
            Location teleportLocation = new Location(player.getWorld(), 29, 3, 4);
            // Set the yaw and pitch manually
            teleportLocation.setYaw(90); // Set yaw to 90 (facing to the right)
            teleportLocation.setPitch(0); // Set pitch to 0 (looking straight ahead)
            // Teleport the player to the specified location
            player.teleport(teleportLocation);
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if the player is op and returns if so
        if (player.isOp()) {
            return;
        }

        Location playerLocation = player.getLocation();

        // Check if the player is outside the allowed area
        if (isOutsideBox(playerLocation, corner1, corner2, player.getGameMode() == GameMode.SPECTATOR)) {
            String message = "Player " + player.getName() + " is outside allowed area!";
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.isOp()) {
                    onlinePlayer.sendMessage(message);
                }
            }
            // Send the message to the console
            Bukkit.getConsoleSender().sendMessage(message);

            Location teleportLocation = new Location(player.getWorld(), 29, 3, 4);

            // Set the yaw and pitch manually
            teleportLocation.setYaw(90); // Set yaw to 90 (facing to the right)
            teleportLocation.setPitch(0); // Set pitch to 0 (looking straight ahead)

            // Teleport the player to the specified location
            player.teleport(teleportLocation);
        }
    }

    // Check if a location is outside a box defined by two corners
    private boolean isOutsideBox(Location location, Location corner1, Location corner2, boolean isSpectator) {
        Location adjustedCorner2 = corner2;

        if (isSpectator) {
            adjustedCorner2 = new Location(Bukkit.getWorld("world"), corner2.getX(), 20.0, corner2.getZ());
        }

        return location.getX() < Math.min(corner1.getX(), adjustedCorner2.getX()) ||
                location.getY() < Math.min(corner1.getY(), adjustedCorner2.getY()) ||
                location.getZ() < Math.min(corner1.getZ(), adjustedCorner2.getZ()) ||
                location.getX() > Math.max(corner1.getX(), adjustedCorner2.getX()) ||
                location.getY() > Math.max(corner1.getY(), adjustedCorner2.getY()) ||
                location.getZ() > Math.max(corner1.getZ(), adjustedCorner2.getZ());
    }

}