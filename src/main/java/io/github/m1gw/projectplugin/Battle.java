package io.github.m1gw.projectplugin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Battle implements CommandExecutor {
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args == null) {
                sender.sendMessage("Starting the battle...");
                Team team = scoreboard.getTeam(ProjectPlugin.currentPlayers);
                Set<OfflinePlayer> members = team.getPlayers();
                if (members.size() >= 2) {
                    List<OfflinePlayer> membersList = new ArrayList<>(members);
                    int index1, index2;
                    Random random = new Random();

                    do {
                        index1 = random.nextInt(members.size());
                        index2 = random.nextInt(members.size());
                    } while (index1 == index2);

                    OfflinePlayer player1 = membersList.get(index1);
                    OfflinePlayer player2 = membersList.get(index2);
                    sender.sendMessage("found players: " + player1.getName() + " and " + player2.getName());

                    // teleport both players to the battle area
                    Player player1Player = Bukkit.getPlayer(player1.getName());
                    Player player2Player = Bukkit.getPlayer(player2.getName());
                    player1Player.teleport(new org.bukkit.Location(Bukkit.getWorld("world"), -23, 4, 0, -90, 0));
                    player2Player.teleport(new org.bukkit.Location(Bukkit.getWorld("world"), 23, -4, -1, 90, 0));

                    // give them tools
                    ProjectPlugin.giveTools(player1Player, 1);
                    ProjectPlugin.giveTools(player2Player, 1);

                    // remove them from the currentPlayers team
                    team.removePlayer(player1);
                    team.removePlayer(player2);

                }
                else {
                    sender.sendMessage("Not enough players to start the battle");
                }
            }
        }
        return true;
    }
}
