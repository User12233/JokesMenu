package me.floppa.jokesmenu.Events;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.floppa.jokesmenu.Jokesmenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class JokesEvents implements Listener {
    static Player playertojokee;
    static boolean troll = false;
    static boolean canSpawnGolem = true;
    static boolean golemtroll = false;
    static boolean vizer = true;

    public JokesEvents() {
        inventory = new ItemStack[0];
    }

    public static void setPlayertojokee(Player playertojokee) {
        JokesEvents.playertojokee = playertojokee;
    }

    private static void YouMayUseThroughDay(Player player) {
        player.sendMessage("§4§lВы можете использовать это через день");
    }
    Wither wither;
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(player.hasMetadata("Opened Menu")) {
            event.setCancelled(true);

            switch(event.getSlot()) {
                case 10:
                    player.closeInventory();
                    @NotNull Vector direction = playertojokee.getLocation().getDirection();
                    @NotNull Vector behindDirection = direction.clone().normalize().multiply(-1);
                    behindDirection.multiply(1);
                    Location tospawn = playertojokee.getLocation().clone().add(behindDirection);
                    Creeper creeper = (Creeper) playertojokee.getWorld().spawnEntity(tospawn, EntityType.CREEPER);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            creeper.remove();
                        }
                    }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 20L);
                    player.sendMessage(playertojokee.getName() + " has been trolled");
                    break;
                case 12:
                    player.closeInventory();
                    troll = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            troll = false;
                        }
                    }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 300L);
                    break;
                case 14:
                    player.closeInventory();
                    Inventory spawnentities = Bukkit.createInventory(player, 9*6);

                    ItemStack EnderManEgg = new ItemStack(Material.ENDERMAN_SPAWN_EGG);
                    ItemMeta EnderEggMeta = EnderManEgg.getItemMeta();
                    EnderEggMeta.displayName(Component.text("§0§lЭндермен"));
                    EnderManEgg.setItemMeta(EnderEggMeta);

                    ItemStack Zombie = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
                    ItemMeta ZombieMeta = Zombie.getItemMeta();
                    ZombieMeta.displayName(Component.text("§a§lЗомби"));
                    Zombie.setItemMeta(ZombieMeta);

                    ItemStack Blaze = new ItemStack(Material.BLAZE_SPAWN_EGG);
                    ItemMeta BlazeMeta = Blaze.getItemMeta();
                    BlazeMeta.displayName(Component.text("§5§lБлейза"));
                    Blaze.setItemMeta(BlazeMeta);

                    ItemStack Creeper = new ItemStack(Material.CREEPER_SPAWN_EGG);
                    ItemMeta CreeperMeta = Creeper.getItemMeta();
                    CreeperMeta.displayName(Component.text("§a§lКрипер"));
                    Creeper.setItemMeta(CreeperMeta);

                    ItemStack spider = new ItemStack(Material.SPIDER_SPAWN_EGG);
                    ItemMeta SpiderMeta = spider.getItemMeta();
                    SpiderMeta.displayName(Component.text("§7§LПаук"));
                    spider.setItemMeta(SpiderMeta);

                    ItemStack golem = new ItemStack(Material.IRON_GOLEM_SPAWN_EGG);
                    ItemMeta GolemMeta = golem.getItemMeta();
                    GolemMeta.displayName(Component.text("§0[§4§nEXTRA§0]§lГолем"));
                    List<Component> lores = new ArrayList<>();
                    lores.add(Component.text("§4§lУничтожает сразу"));
                    lores.add(Component.text("§4§lНет ни шанса"));
                    GolemMeta.lore(lores);
                    golem.setItemMeta(GolemMeta);

                    ItemStack Lama = new ItemStack(Material.LLAMA_SPAWN_EGG);
                    ItemMeta LAMAMeta = Lama.getItemMeta();
                    LAMAMeta.displayName(Component.text("§0[§1§lADDITIONAL§0] §3§lЛама"));
                    Lama.setItemMeta(LAMAMeta);

                    ItemStack wither = new ItemStack(Material.WITHER_SKELETON_SKULL);
                    ItemMeta WitherMeta = wither.getItemMeta();
                    WitherMeta.displayName(Component.text("§0[§4MEGA§0]§fВизер"));
                    wither.setItemMeta(WitherMeta);



                    spawnentities.setItem(10, EnderManEgg);
                    spawnentities.setItem(12, Zombie );
                    spawnentities.setItem(14, Blaze);
                    spawnentities.setItem(16, Creeper);
                    spawnentities.setItem(29, spider);
                    spawnentities.setItem(31, golem);
                    spawnentities.setItem(33, Lama);
                    spawnentities.setItem(49, wither);
                    spawnentities.setItem(53, new ItemStack(Material.BARRIER));

                    player.openInventory(spawnentities);
                    player.setMetadata("Opened Mobs", new FixedMetadataValue(Jokesmenu.getPlugin(Jokesmenu.class), true));
                    break;
            }
        } else if (player.hasMetadata("Opened Mobs")) {
            event.setCancelled(true);

            switch(event.getSlot()) {
                case 10:
                    player.closeInventory();
                    Enderman enderman = (Enderman) playertojokee.getWorld().spawnEntity(playertojokee.getLocation(), EntityType.ENDERMAN);
                    enderman.setScreaming(true);
                    enderman.setTarget(playertojokee);
                    break;
                case 12:
                    player.closeInventory();
                    Zombie zombie = (Zombie) playertojokee.getWorld().spawnEntity(playertojokee.getLocation(), EntityType.ZOMBIE);
                    zombie.setArmsRaised(true);
                    zombie.setCanBreakDoors(true);
                    zombie.setTarget(playertojokee);
                    break;
                case 14:
                    player.closeInventory();
                    Blaze blaze = (Blaze) playertojokee.getWorld().spawnEntity(playertojokee.getLocation(), EntityType.BLAZE);
                    blaze.setTarget(playertojokee);
                    break;
                case 16:
                    player.closeInventory();
                    Creeper creeper = (Creeper) playertojokee.getWorld().spawnEntity(playertojokee.getLocation(), EntityType.CREEPER);
                    creeper.setTarget(playertojokee);
                    break;
                case 29:
                    player.closeInventory();
                    Spider spider = (Spider) playertojokee.getWorld().spawnEntity(playertojokee.getLocation(), EntityType.SPIDER);
                    spider.setTarget(playertojokee);
                    break;
                case 31:
                    player.closeInventory();
                    if(!canSpawnGolem) {
                        YouMayUseThroughDay(player);
                    } else {
                        golemtroll = true;
                        Golem golem = (Golem) playertojokee.getWorld().spawnEntity(playertojokee.getLocation(), EntityType.IRON_GOLEM);
                        playertojokee.showTitle(Title.title(Component.text("§4§l1"), Component.text("")));
                        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> playertojokee.showTitle(Title.title(Component.text("§4§l2"), Component.text(""))), 60L);
                        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> playertojokee.showTitle(Title.title(Component.text("§4§l3"), Component.text(""))), 130L);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                double distance = golem.getLocation().distance(playertojokee.getLocation());
                                if(distance > 65 && golemtroll) {
                                    Vector distan = playertojokee.getLocation().toVector().subtract(golem.getLocation().toVector());
                                    distan.normalize();
                                    distan.clone().multiply(1.5);
                                    Location playertoloc = playertojokee.getLocation().add(distan);
                                    golem.teleport(playertoloc);
                                    golem.setTarget(playertojokee);
                                } else if(!golemtroll) {
                                    cancel();
                                }
                            }
                        }.runTaskTimer(Jokesmenu.getPlugin(Jokesmenu.class), 0L, 40L);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                golem.addPotionEffect(PotionEffectType.SPEED.createEffect(1000000000, 4));
                                golem.addPotionEffect(PotionEffectType.HEAL.createEffect(1000000000, 1));
                                golem.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(1000000000, 3));
                                golem.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(10000000, 3));
                                golem.setKiller(playertojokee);
                                golem.setTarget(playertojokee);
                                canSpawnGolem = false;
                                playertojokee.getInventory().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.TOTEM_OF_UNDYING));
                            }
                        }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 170L);
                        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> golemtroll = false, 1000L);
                        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> canSpawnGolem = true, 24000L);
                    }
                    break;
                case 33:
                    player.closeInventory();
                    Llama lama = (Llama) playertojokee.getWorld().spawnEntity(playertojokee.getLocation(), EntityType.LLAMA);
                    lama.setTarget(playertojokee);
                    break;
                case 53:
                    player.closeInventory();
                    if(vizer) {
                        makePlayerReadyToWither(playertojokee);
                        playertojokee.showTitle(Title.title(Component.text("§4Приготовьтесь"), Component.text("1")));
                        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> playertojokee.showTitle(Title.title(Component.text("§4§l2"), Component.text("§a§lбыстро приготовьтесь"))), 60L);
                        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> playertojokee.showTitle(Title.title(Component.text("§4§l3"), Component.text("§a§lбыстро приготовьтесь"))), 130L);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                wither = (Wither) playertojokee.getWorld().spawnEntity(playertojokee.getLocation(), EntityType.WITHER);
                                wither.setTarget(playertojokee);
                                JokesEvents.vizer = false;
                                playertojokee.getInventory().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.TOTEM_OF_UNDYING));
                                wither.addPotionEffect(PotionEffectType.CONFUSION.createEffect(10000000, 3));
                            }
                        }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 160L);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                JokesEvents.vizer = true;
                            }
                        }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 48000L);
                        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> returnInventory(inventory, playertojokee), 3500L);
                    } else {
                        player.sendMessage("§4§lВы можете использовать это опять через 2 суток");
                    }
                    break;
                case 41:
                    player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(event.getPlayer().hasMetadata("Opened Menu")) {
            event.getPlayer().removeMetadata("Opened Menu", Jokesmenu.getPlugin(Jokesmenu.class));
        } else if(event.getPlayer().hasMetadata("Opened Mobs")) {
            event.getPlayer().removeMetadata("Opened Mobs", Jokesmenu.getPlugin(Jokesmenu.class));
        }
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent event) {
        if(troll) {
            if(event.getPlayer().getName().equalsIgnoreCase(playertojokee.getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockUse(PlayerInteractEvent event) {
        if(troll) {
            if(event.getPlayer().getName().equalsIgnoreCase(playertojokee.getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(troll){
            if(event.getPlayer().getName().equalsIgnoreCase(playertojokee.getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if(event.getEntity() instanceof Golem && golemtroll) {
            Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> breakblocksinpath(event.getEntity()), 25L);
        }
    }

    private void breakblocksinpath(Entity golem) {
        Location location = golem.getLocation();
        Location frontLocation = location.clone().add(golem.getVelocity().normalize());

        if (frontLocation.getBlock().getType() != Material.AIR) {
            frontLocation.getBlock().setType(Material.AIR);
        }
    }

    @Nullable ItemStack @NotNull [] inventory;
    private void makePlayerReadyToWither(Player player) {
        inventory = player.getInventory().getStorageContents().clone();
        player.getInventory().clear();
        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 128));

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
        bow.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().addItem(bow);
    }

    private void returnInventory(ItemStack[] itemstacks, Player player) {
        for (ItemStack item : itemstacks) {
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }
    }
}
