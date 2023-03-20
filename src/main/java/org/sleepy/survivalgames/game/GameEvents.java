package org.sleepy.survivalgames.game;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.sleepy.survivalgames.SurvivalGames;
import org.sleepy.survivalgames.queue.QueueSetup;

import java.util.List;

public class GameEvents implements Listener {
    private final SurvivalGames plugin;
    QueueSetup queueSetup;
    List<Player> queue;
    GameSetup gameSetup;
    Location spawnLoc;
    String prefix;
    String gameName;

    public GameEvents(SurvivalGames plugin) {
        this.plugin = plugin;
        queue = plugin.queue;
        queueSetup = plugin.queueSetup;
        gameSetup = plugin.gameSetup;
        prefix = plugin.prefix;
        gameName = plugin.gameName;
        spawnLoc = plugin.spawnLocation;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        gameName = plugin.gameName;
        player.sendMessage(prefix + "Welcome back to " + gameName + ".");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (queue.contains(player) && plugin.gameActive && event.getEntity().getWorld().equals(plugin.gameWorld)) {
            player.sendMessage(prefix + "You have died and will now be returned to spawn");
            player.teleport(spawnLoc);
            queueSetup.leaveQueue(player);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
            if (plugin.gameActive && event.getEntity().getWorld().equals(plugin.gameWorld)) {
                if (!plugin.pvpIsEnabled) {
                    event.setCancelled(true);
                }
            }
    }

    @EventHandler
    public void onFireDamage(BlockBurnEvent event) {
        if (plugin.gameActive && event.getIgnitingBlock().getWorld().equals(plugin.gameWorld)) {
            if (!plugin.pvpIsEnabled) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileDamage(ProjectileHitEvent event) {
        if (plugin.gameActive && event.getEntity().getWorld().equals(plugin.gameWorld)) {
            if (!plugin.pvpIsEnabled && queue.contains(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void playerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (plugin.gameActive && event.getPlayer().getWorld().equals(plugin.gameWorld)
        && queue.contains(player)) {
            new BukkitRunnable() {
                int counter = 0;
                public void run() {
                    plugin.playerMove = false;
                    event.setCancelled(true);
                    if (++counter >= 30) {
                        cancel(); // 30 ticks = 30 seconds (20 ticks per second)
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L);
            }
        }

    public void Firework(Player player) {
        Location loc = player.getLocation();
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(30);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.MAROON).flicker(true).build());
        fwm.addEffect(FireworkEffect.builder().withTrail().withColor(Color.GREEN).flicker(false).build());
        fw.setFireworkMeta(fwm);
        new BukkitRunnable() {
            public void run() {
                fw.detonate();
                cancel();
            }
        }.runTaskLater(plugin, 20L);
    }
}
