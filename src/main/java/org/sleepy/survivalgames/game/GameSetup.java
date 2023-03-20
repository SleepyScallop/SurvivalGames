package org.sleepy.survivalgames.game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.sleepy.survivalgames.SurvivalGames;
import org.sleepy.survivalgames.queue.QueueSetup;

import java.util.List;

public class GameSetup implements Listener {
    private final SurvivalGames plugin;
    QueueSetup queueSetup;
    List<Player> queue;
    String prefix;
    Location spawnLoc;
    Location gameLoc;

    public GameSetup(SurvivalGames plugin) {
        this.plugin = plugin;
        queue = plugin.queue;
        queueSetup = plugin.queueSetup;
        prefix = plugin.prefix;
        gameLoc = plugin.gameLocation;
        spawnLoc = plugin.spawnLocation;
    }

    public void gameReady() {
        List<Player> playersReady = queue;
        for (Player players : playersReady) {
            this.plugin.gameActive = true;
            this.plugin.playerMove = false;
            players.teleport(locMath(players));
            players.sendMessage(prefix + "The game will begin in 30 seconds.");
            new BukkitRunnable() {
                public void run() {
                    if (plugin.gameActive && queue.size() == plugin.gameSize) {
                        players.sendMessage(prefix + "The game will now begin, pvp is enabled.");
                        plugin.pvpIsEnabled = true;
                        plugin.playerMove = true;
                        cancel();
                    } else if (plugin.gameActive && queue.size() < plugin.gameSize) {
                        players.sendMessage(prefix + "Waiting for more players to join...");
                    }
                }
            }.runTaskTimer(plugin, 600L, 20L);
        }
        while (plugin.gameActive && queue.size() <= 1) {
            Player winner = queue.get(0);
            queueSetup.leaveQueue(winner);
            winner.sendMessage(prefix + "You have won Survival Games!");
            winner.sendMessage(prefix + "Teleporting back to spawn shortly...");
            new BukkitRunnable() {
                int counter = 0;
                public void run() {
                    plugin.gameEvents.Firework(winner);
                    counter++;
                    if (counter >= 10) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
            new BukkitRunnable() {
                public void run() {
                    plugin.gameActive = false;
                    plugin.pvpIsEnabled = false;
                    winner.teleport(spawnLoc);
                    winner.stopAllSounds();
                    winner.sendMessage(prefix + "You have been teleported to spawn.");
                    cancel();
                }
            }.runTaskLater(plugin, 200L);
            break;
        }
    }

    public Location locMath(Player player) {
        Location gameLoc = plugin.gameLocation;
        double x = gameLoc.getX() + 2;
        double y = gameLoc.getY();
        double z = gameLoc.getZ() + 8;
        Location newLoc = new Location(player.getWorld(), x, y, z);
        return newLoc;
    }
}
