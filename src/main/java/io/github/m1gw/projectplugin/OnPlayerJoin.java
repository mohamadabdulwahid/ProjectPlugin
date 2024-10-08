package io.github.m1gw.projectplugin;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OnPlayerJoin implements Listener {
    // Define the corners of the allowed area
    private final Location corner1 = new Location(Bukkit.getWorld("world"), 38.5, -5.5, -21.5);
    private final Location corner2 = new Location(Bukkit.getWorld("world"), -31.5, 10.5, 21.5);


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getActivePotionEffects().add(new PotionEffect(PotionEffectType.SATURATION, 1000000, 1, false));

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        if (!event.getPlayer().isOp()) {
            TeleportToSpawn(player);
            player.getInventory().clear();
        }

        if(Battle.disconnectedPlayers.contains(player.getName())) {
            player.sendMessage("You have disconnected in the last match. Please wait until we decide if you can play again.");
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
            return;
        }

        if(Battle.bannedPlayers.contains(player.getName())) {
            player.sendMessage("You have been banned from playing. Please contact an admin if you think this is a mistake.");
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
            scoreboard.getTeam(ProjectPlugin.spectators).addEntities(player);
            return;
        }

        if (ProjectPlugin.newPlayersCanJoin) { // && !player.isOp()
            if (scoreboard.getPlayerTeam(player) == null || !scoreboard.getPlayerTeam(player).getName().equals(ProjectPlugin.currentPlayers)) {
                scoreboard.getTeam(ProjectPlugin.currentPlayers).addEntities(player);
                player.setGameMode(GameMode.ADVENTURE);
            }
            else {
                player.getServer().broadcastMessage("Player " + player.getName() + " is already in a team");
                player.getServer().broadcastMessage("So turning him into a spectator...");
                scoreboard.getTeam(ProjectPlugin.spectators).addEntities(player);
            }
        }else{
            scoreboard.getTeam(ProjectPlugin.spectators).addEntities(player);
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if the player is op and returns if so
        if (player.isOp()) {
            return;
        }

        if (player.isFlying() &&
                (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)) {

            player.kickPlayer("Flying is disabled");
        }

        Location playerLocation = player.getLocation();

        // Check if the player is outside the allowed area
        if (isOutsideBox(playerLocation, corner1, corner2, player.getGameMode() == GameMode.SPECTATOR)) {
            String message = "Player " + player.getName() + " is outside allowed area!";
            List<Player> ops = Bukkit.getOnlinePlayers().stream()
                    .filter(Player::isOp)
                    .collect(Collectors.toList());
            for (Player onlinePlayer : ops) {
                onlinePlayer.sendMessage(message);
            }
            // Send the message to the console
            Bukkit.getConsoleSender().sendMessage(message);

            TeleportToSpawn(player);
        }
    }

    // Check if a location is outside a box defined by two corners
    private boolean isOutsideBox(Location location, Location corner1, Location corner2, boolean isSpectator) {
        Location adjustedCorner2 = corner2;

        if (isSpectator) {
            adjustedCorner2 = new Location(Bukkit.getWorld("world"), corner2.getX(), 20.5, corner2.getZ());
        }

        return location.getX() < Math.min(corner1.getX(), adjustedCorner2.getX()) ||
                location.getY() < Math.min(corner1.getY(), adjustedCorner2.getY()) ||
                location.getZ() < Math.min(corner1.getZ(), adjustedCorner2.getZ()) ||
                location.getX() > Math.max(corner1.getX(), adjustedCorner2.getX()) ||
                location.getY() > Math.max(corner1.getY(), adjustedCorner2.getY()) ||
                location.getZ() > Math.max(corner1.getZ(), adjustedCorner2.getZ());
    }


    public static void TeleportToSpawn(Player player) {
        Location teleportLocation = new Location(player.getWorld(), 31.5, 3.1, -0.5);
        teleportLocation.setYaw(90);
        teleportLocation.setPitch(0);
        player.teleport(teleportLocation);
        player.getActivePotionEffects().add(new PotionEffect(PotionEffectType.SATURATION, 1000000, 1, false));
    }
}
