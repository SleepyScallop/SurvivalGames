package org.sleepy.survivalgames.queue;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.sleepy.survivalgames.SurvivalGames;

import java.util.List;

public class QueueSetup implements Listener {
    List<Player> queue;
    private final SurvivalGames plugin;
    String prefix;

    public QueueSetup(SurvivalGames plugin) {
        this.plugin = plugin;
        queue = plugin.queue;
        prefix = plugin.prefix;
    }

    public void joinQueue(Player player) {
        queue.add(player);
    }

    public void leaveQueue(Player player) {
        queue.remove(player);
    }

    public void aboutQueue(Player player) {
        int queueNeeded = plugin.gameSize - queue.size();
        player.sendMessage(prefix + "Queue Information:");
        player.sendMessage(ChatColor.GRAY + "Gamers waiting: " + ChatColor.WHITE + queue.size());
        player.sendMessage(ChatColor.GRAY + "Needed to start: " + ChatColor.WHITE + queueNeeded);
    }
}
