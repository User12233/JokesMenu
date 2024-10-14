package me.floppa.jokesmenu.Commands;

import me.floppa.jokesmenu.Events.JokesEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class JokesMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player || strings.length > 0) {
            switch(strings[0]) {
                case "everyone", "all", "players":
                    activateOnAllPlayers(Bukkit.getPlayer(commandSender.getName()));
                    break;
                case "activated":
                    if(JokesEvents.isActivated()) {
                        commandSender.sendMessage("Активировано");
                    } else {
                        commandSender.sendMessage("Деактивировано");
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    public void activateOnAllPlayers(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            JokesEvents.randomSpawn((p.getPlayer()));
        }
        player.sendMessage("§aАктивирован на всех игроков");
    }
}
