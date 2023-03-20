package org.sleepy.survivalgames;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.sleepy.survivalgames.game.GameEvents;
import org.sleepy.survivalgames.game.GameSetup;
import org.sleepy.survivalgames.queue.QueueCommands;
import org.sleepy.survivalgames.queue.QueueEvents;
import org.sleepy.survivalgames.queue.QueueSetup;

import java.util.LinkedList;
import java.util.List;

public class SurvivalGames extends JavaPlugin implements Listener {
    public QueueCommands queueCommands;
    public QueueEvents queueEvents;
    public QueueSetup queueSetup;
    public GameSetup gameSetup;
    public GameEvents gameEvents;
    public String gameName;
    public String prefix;
    public String uglyprefix;
    public boolean gameActive;
    public boolean pvpIsEnabled;
    public boolean playerMove;
    public List<Player> queue;
    public Location gameLocation;
    public Location spawnLocation;
    public String gameWorld;
    public int gameSize;

    @Override
    public void onEnable() {
        queue = new LinkedList<>();
        uglyprefix = this.getConfig().getString("plugin-prefix");
        prefix = ChatColor.GRAY + "[" + ChatColor.GOLD + uglyprefix + ChatColor.GRAY + "] ";
        queueSetup = new QueueSetup(this);
        queueEvents = new QueueEvents(this);
        gameName = this.getConfig().getString("game-name");
        gameLocation = this.getConfig().getLocation("game-location");
        gameWorld = this.getConfig().getString("game-world");
        spawnLocation = this.getConfig().getLocation("spawn-location");
        gameSize = this.getConfig().getInt("game-size");
        pvpIsEnabled = false;
        playerMove = true;
        gameActive = false;
        gameSetup = new GameSetup(this);
        gameEvents = new GameEvents(this);
        queueCommands = new QueueCommands(this);
        saveDefaultConfig();
        reloadConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(queueCommands, this);
        getServer().getPluginManager().registerEvents(queueEvents, this);
        getServer().getPluginManager().registerEvents(queueSetup, this);
        getServer().getPluginManager().registerEvents(gameSetup, this);
        getServer().getPluginManager().registerEvents(gameEvents, this);
        getCommand("sg").setExecutor(queueCommands);
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
    }
}
