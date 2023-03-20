package org.sleepy.survivalgames.queue;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.sleepy.survivalgames.SurvivalGames;
import org.sleepy.survivalgames.game.GameSetup;

import java.util.List;

public class QueueCommands implements Listener, CommandExecutor {
    private final SurvivalGames plugin;
    QueueSetup queueSetup;
    List<Player> queue;
    GameSetup gameSetup;
    String prefix;

    public QueueCommands(SurvivalGames plugin) {
        this.plugin = plugin;
        queue = plugin.queue;
        queueSetup = plugin.queueSetup;
        gameSetup = plugin.gameSetup;
        prefix = plugin.prefix;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if (!plugin.gameActive) {
            if (args.length <= 0) {
                player.sendMessage(prefix + "Usage: /sg join/leave/about");
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "join":
                    if (queue.contains(player)) {
                        player.sendMessage(prefix + "You have already joined the queue for the game!");
                        break;
                    }
                    queueSetup.joinQueue(player);
                    player.sendMessage(prefix + "You have joined the queue for the game.");
                    if (queue.size() == plugin.gameSize) {
                        gameSetup.gameReady();
                        break;
                    }
                    break;
                case "leave":
                    if (!queue.contains(player)) {
                        player.sendMessage(prefix + "You are not currently in the queue for a game!");
                        break;
                    }
                    queueSetup.leaveQueue(player);
                    player.sendMessage(prefix + "You have left the queue for the game.");
                    break;
                case "about":
                    queueSetup.aboutQueue(player);
                    break;
                default:
                    player.sendMessage(prefix + "Usage: /sg join/leave/about");
                    break;
            }
        } else {
            player.sendMessage(prefix + "Game in progress, please wait until it is completed...");
            return true;
        }
        return true;
    }
}
