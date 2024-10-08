package io.github.m1gw.projectplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.World;
import org.bukkit.block.Block;

public final class ProjectPlugin extends JavaPlugin {

    public static boolean newPlayersCanJoin = true;
    public static String winners = "winners";
    public static String losers = "losers";
    public static String currentPlayers = "currentPlayers";
    public static String spectators = "spectators";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new BattleDeathListener(new Battle()), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamage(), this);

        Bukkit.getConsoleSender().sendMessage("PvP plugin enabled :)");

        //check corrent version
        Bukkit.broadcastMessage("Final for now version 9000");

        // Add Teams
        createTeam(winners);
        createTeam(losers);
        createTeam(currentPlayers);
        createTeam(spectators);

        // Register the command
        this.getCommand("gotospawn").setExecutor(new GoToSpawn());
        this.getCommand("addnewpeopletogame").setExecutor(new AddNewPeopleToGame());
        this.getCommand("battle").setExecutor(new Battle());
        this.getCommand("givewinnersplaying").setExecutor(new GiveWinnersPlaying());
        this.getCommand("decide").setExecutor(new DecideCommand());
    }

    @Override
    public void onDisable() {
    }


    public static void createTeam(String teamName) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        if (scoreboard.getTeam(teamName) != null) {
            Bukkit.getLogger().info("Team " + teamName + " already exists.");
            return;
        }

        Team team = scoreboard.registerNewTeam(teamName);
    }

    public static void giveTools(Player player, int setIndex) {
        player.getInventory().clear();
        ItemStack[][] equipmentSets = new ItemStack[][]{
                { // Diamond set (0)
                        new ItemStack(Material.DIAMOND_CHESTPLATE),
                        new ItemStack(Material.DIAMOND_LEGGINGS),
                        new ItemStack(Material.DIAMOND_BOOTS),
                        new ItemStack(Material.DIAMOND_HELMET),
                        new ItemStack(Material.DIAMOND_SWORD),
                        new ItemStack(Material.BOW),
                        new ItemStack(Material.ARROW, 10),
                        new ItemStack(Material.GOLDEN_APPLE, 1),
                        new ItemStack(Material.SHIELD)
                },
                { // Leather/Wood set (1)
                        new ItemStack(Material.LEATHER_CHESTPLATE),
                        new ItemStack(Material.LEATHER_LEGGINGS),
                        new ItemStack(Material.LEATHER_BOOTS),
                        new ItemStack(Material.LEATHER_HELMET),
                        new ItemStack(Material.WOODEN_SWORD),
                        new ItemStack(Material.BOW),
                        new ItemStack(Material.ARROW, 10),
                        new ItemStack(Material.GOLDEN_APPLE, 1),
                        new ItemStack(Material.SHIELD)
                },
                { // Iron set (2)
                        new ItemStack(Material.IRON_CHESTPLATE),
                        new ItemStack(Material.IRON_LEGGINGS),
                        new ItemStack(Material.IRON_BOOTS),
                        new ItemStack(Material.IRON_HELMET),
                        new ItemStack(Material.IRON_SWORD),
                        new ItemStack(Material.BOW),
                        new ItemStack(Material.ARROW, 10),
                        new ItemStack(Material.GOLDEN_APPLE, 1),
                        new ItemStack(Material.SHIELD)
                }
        };
        ItemStack[] equipmentSet = equipmentSets[setIndex];
        player.getInventory().setChestplate(equipmentSet[0]);
        player.getInventory().setLeggings(equipmentSet[1]);
        player.getInventory().setBoots(equipmentSet[2]);
        player.getInventory().setHelmet(equipmentSet[3]);
        player.getInventory().setItemInOffHand(equipmentSet[8]);

        player.getInventory().addItem(equipmentSet[4]);
        player.getInventory().addItem(equipmentSet[5]);
        player.getInventory().addItem(equipmentSet[6]);
        player.getInventory().addItem(equipmentSet[7]);
    }

    public static void fillBlocks(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material material) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(material);
                }
            }
        }
    }

    public static void sendInfoToUs(String message) {
        Player player1 = Bukkit.getPlayer("mohamadGw");
        Player player2 = Bukkit.getPlayer("Black_2");

        //Bukkit.broadcastMessage("[Pvp info broadcast]: " + message);

        message = "[Pvp info]: " + message;

        if (player1 != null) {
            player1.sendMessage(message);
        }

        if (player2 != null) {
            player2.sendMessage(message);
        }

    }

    public static void removeGlassBlocking() {
        ProjectPlugin.fillBlocks(Bukkit.getWorld("world"), -21, -4, -1, -21, -2, 1, Material.AIR);

        ProjectPlugin.fillBlocks(Bukkit.getWorld("world"), 21, -2, -2, 21, -4, 0, Material.AIR);

    }

}


