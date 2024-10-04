package me.floppa.jokesmenu.Commands;

import me.floppa.jokesmenu.Events.JokesEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JokesMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player) {
            if (strings.length < 1 || Bukkit.getPlayer(strings[0]) == null) {
                player.sendMessage("Использование: /jokesmenu [Ник]");
            } else {
                Inventory inventory = Bukkit.createInventory(player, 9 * 6);

                ItemStack creeper = new ItemStack(Material.CREEPER_HEAD);
                ItemMeta creepermeta = creeper.getItemMeta();
                creepermeta.displayName(Component.text("§e§lЗвук крипера"));
                creeper.setItemMeta(creepermeta);

                ItemStack Barrier = new ItemStack(Material.BARRIER);
                ItemMeta Barriermeta = Barrier.getItemMeta();
                Barriermeta.displayName(Component.text("§4§lОграниченный"));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§4§lИгрок больше не будет взаимодействовать с"));
                lore.add(Component.text("§4§lокружающей средой на 8 тиков"));
                Barriermeta.lore(lore);
                Barrier.setItemMeta(Barriermeta);

                ItemStack SpawnEgg = new ItemStack(Material.ENDERMAN_SPAWN_EGG);
                ItemMeta SpawnEggMeta = SpawnEgg.getItemMeta();
                SpawnEggMeta.displayName(Component.text("§0§lСпавн Моба"));
                SpawnEgg.setItemMeta(SpawnEggMeta);

                inventory.setItem(10, creeper);
                inventory.setItem(12, Barrier);
                inventory.setItem(14, SpawnEgg);

                JokesEvents.setPlayertojokee(Objects.requireNonNull(Bukkit.getPlayer(strings[0])));
                player.openInventory(inventory);
                player.setMetadata("Opened Menu", new FixedMetadataValue(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("JokesMenu")), true));
            }
        } else {
            commandSender.sendMessage("§4Consoles and something else not allowed to use this command");
        }
        return false;
    }
}
