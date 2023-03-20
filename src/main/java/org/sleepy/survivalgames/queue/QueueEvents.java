package org.sleepy.survivalgames.queue;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.sleepy.survivalgames.SurvivalGames;

import java.util.List;

public class QueueEvents implements Listener {
    private final SurvivalGames plugin;
    QueueSetup queueSetup;
    List<Player> queue;

    public QueueEvents(SurvivalGames plugin) {
        this.plugin = plugin;
        queue = plugin.queue;
        queueSetup = plugin.queueSetup;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (queue.contains(player)) ;
        queueSetup.leaveQueue(player);
    }
}
