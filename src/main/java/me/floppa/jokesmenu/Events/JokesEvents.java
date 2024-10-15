package me.floppa.jokesmenu.Events;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import me.floppa.jokesmenu.Jokesmenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.level.Level;
import org.bukkit.*;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class JokesEvents implements Listener {
    static boolean activated;
    static boolean golemtroll = false;
    static boolean vizertroll = false;
    static ServerPlayer playerclone;
    static Wither wither;
    static Player player1;


    public static boolean isActivated() {
        return activated;
    }

    public JokesEvents() {
        inventory = new ItemStack[0];
    }


    private static ItemStack[] inventory = new ItemStack[0];

    private static void makePlayerReadyToWither(Player player) {
        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 128));

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
        bow.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().addItem(bow);
        player.getEquipment().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.TOTEM_OF_UNDYING));
    }

    public static void savedInventory(ItemStack[] itemstacks, Player player) {
        player.getInventory().clear();
        if (itemstacks.length == 0) {
            player.sendMessage("You don't have any items in saved inventory.");
        } else {
            for (ItemStack item : inventory) {
                if (item != null && item.getType() != Material.AIR) {
                    player.getInventory().addItem(item);
                }
            }
        }
    }

    @EventHandler
    public void onWitherDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Wither && vizertroll) {
            savedInventory(inventory, player1);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (wither != null && Objects.equals(player1.getPlayer(), event.getEntity())) {
            Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> savedInventory(inventory, player1), 135L);
            wither.remove();
        }
    }


    public static void changeSkin(CraftPlayer player, ProfileProperty property) {
        CraftPlayerProfile profile = (CraftPlayerProfile) player.getPlayerProfile();
        profile.getProperties().removeIf((profileProperty) -> profileProperty.getName().equals("textures"));
        profile.getProperties().add(new ProfileProperty("textures", property.getValue(), property.getSignature()));
        player.setPlayerProfile(profile);
    }

    public static void spawnGolem(Player player) {
                    golemtroll = true;
                    Golem golem = (Golem) player.getWorld().spawnEntity(player.getLocation(), EntityType.IRON_GOLEM);
                    player.showTitle(Title.title(Component.text("§4§l1"), Component.text("")));
                    Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> player.showTitle(Title.title(Component.text("§4§l2"), Component.text(""))), 60L);
                    Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> player.showTitle(Title.title(Component.text("§4§l3"), Component.text(""))), 130L);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            double distance = golem.getLocation().distance(player.getLocation());
                            if (distance > 40 && golemtroll) {
                                Vector distan = player.getLocation().toVector().subtract(golem.getLocation().toVector());
                                distan.normalize();
                                distan.clone().multiply(2.5);
                                Location playertoloc = player.getLocation().add(distan);
                                golem.teleport(playertoloc);
                                golem.setTarget(player);
                            } else if (!golemtroll) {
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
                            golem.setKiller(player);
                            golem.setTarget(player);
                            player.getInventory().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.TOTEM_OF_UNDYING));
                        }
                    }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 170L);
                    new BukkitRunnable() {
                        public void run() {
                            golemtroll = false;
                            player.getInventory().remove(Material.TOTEM_OF_UNDYING);
                        }
                    }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 1000L);
    }

    public static void spawnClone(Player player) {
        CraftPlayer craftplayertojokee = (CraftPlayer) player;
        UUID uuidclone = UUID.randomUUID();
        Connection fakeConnection = new Connection(PacketFlow.CLIENTBOUND);
        MinecraftServer server = MinecraftServer.getServer();
        GameProfile profile = new GameProfile(uuidclone, player.getName());

        playerclone = new ServerPlayer(server, Objects.requireNonNull(server.getLevel(Level.OVERWORLD)), profile, new ClientInformation("en_US", 5, ChatVisiblity.FULL, true, 1, HumanoidArm.RIGHT, false, false));
        playerclone.connection = new ServerGamePacketListenerImpl(server, fakeConnection, playerclone, CommonListenerCookie.createInitial(profile, false));
        playerclone.setPos(player.getX(), player.getY(), player.getZ());

        changeSkin(playerclone.getBukkitEntity(), new ProfileProperty("", craftplayertojokee.getProfile().getProperties().get("textures").toString()));
        craftplayertojokee.getHandle().connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, playerclone));
        craftplayertojokee.getHandle().connection.send(new ClientboundAddEntityPacket(playerclone.getId(), playerclone.getUUID(), playerclone.getX(), playerclone.getY(), playerclone.getZ(), playerclone.getYHeadRot(), playerclone.getBukkitYaw(), net.minecraft.world.entity.EntityType.PLAYER, playerclone.getEntityData().hashCode(), playerclone.getDeltaMovement(), playerclone.getY()));
        playerclone.attack(craftplayertojokee.getHandle());
        AtomicBoolean task = new AtomicBoolean(true);
        new BukkitRunnable() {
            public void run() {
                if(task.get()) {
                    CraftEntity HitEntity = (CraftEntity) player;
                    playerclone.teleportRelative(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                    playerclone.connection.send(new ClientboundAnimatePacket(playerclone, 0));
                    playerclone.connection.send(new ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor.EYES, craftplayertojokee.getX(), craftplayertojokee.getY(), craftplayertojokee.getZ()));
                    playerclone.attack(HitEntity.getHandle());
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Jokesmenu.getPlugin(Jokesmenu.class), 5L, 50L);
        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> task.set(false), 650L);
        Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> playerclone.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED), 750L);
    }

    public static void spawnTrollCreeper(Player player) {
        @NotNull Vector direction = player.getLocation().getDirection();
        @NotNull Vector behindDirection = direction.clone().normalize().multiply(-1);
        behindDirection.multiply(1);
        Location tospawn = player.getLocation().clone().add(behindDirection);
        Creeper creeper = (Creeper) player.getWorld().spawnEntity(tospawn, EntityType.CREEPER);
        new BukkitRunnable() {
            @Override
            public void run() {
                creeper.remove();
            }
        }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 20L);
    }

    public static void spawnWither(Player player) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Jokesmenu.getPlugin(Jokesmenu.class), () -> makePlayerReadyToWither(player), 15L);
        player.showTitle(Title.title(Component.text("§4Приготовьтесь"), Component.text("1")));
        inventory = player.getInventory().getContents().clone();
        Bukkit.getScheduler().runTaskLaterAsynchronously(Jokesmenu.getPlugin(Jokesmenu.class), () -> player.getInventory().clear(), 50L);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Jokesmenu.getPlugin(Jokesmenu.class), () -> player.showTitle(Title.title(Component.text("§4§l2"), Component.text("§a§lпоставьте кровать"))), 80L);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Jokesmenu.getPlugin(Jokesmenu.class), () -> player.showTitle(Title.title(Component.text("§4§l3"), Component.text("§a§lпоставьте кровать"))), 170L);
        new BukkitRunnable() {
            @Override
            public void run() {
                vizertroll = true;
                makePlayerReadyToWither(player);
                wither = (Wither) player.getWorld().spawnEntity(player.getLocation(), EntityType.WITHER);
                wither.setTarget(player);
                player.getInventory().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.TOTEM_OF_UNDYING));
                wither.addPotionEffect(PotionEffectType.CONFUSION.createEffect(10000000, 3));
                wither.setHealth(100);
                player.addPotionEffect(PotionEffectType.HEAL.createEffect(1000, 8));
                player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(1000, 4));
            }
        }.runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), 260L);
    }

    private static void sendMessageAboutRandom(Player player, String message) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);
        player.sendMessage(message);

    }

    public static void randomSpawn(Player player) {
        if(!activated) {
            String[] arraySpawn = {"Golem", "Wither", "Creeper", "Clone", "SpawnCreeper", "SpawnSkeleton", "SpawnLlama", "SpawnBee", "SpawnEnderMan", "SpawnBlaze", "DestroyingLook"};
            Random rand = new Random();
            activated = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(Bukkit.getPlayer(player.getPlayer().getName()) == null) {
                        player.sendMessage("Деактивирован");
                        Bukkit.getLogger().info("Deactivated " + player.getName() + "'s");
                        cancel();
                    } else {
                    int random = rand.nextInt(11);
                    String which = arraySpawn[random];
                    Bukkit.getLogger().info("Selected joke - " + which);
                    switch (which) {
                        case "Golem":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §4§lГолем под солями");
                            spawnGolem(player);
                            break;
                        case "Wither":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §0§lМини испытание");
                            spawnWither(player);
                            player1 = player;
                            break;
                        case "Creeper":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §a§lКрипэ");
                            spawnTrollCreeper(player);
                            break;
                        case "Clone":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §lТвой брат");
                            spawnClone(player);
                            break;
                        case "SpawnCreeper":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §a§lпКрипэ");
                            @NotNull Vector direction = player.getLocation().getDirection();
                            @NotNull Vector behindDirection = direction.clone().normalize().multiply(-1);
                            behindDirection.multiply(1);
                            Location tospawn = player.getLocation().clone().add(behindDirection);
                            Creeper creeper = (Creeper) player.getWorld().spawnEntity(tospawn, EntityType.CREEPER);
                            creeper.setTarget(player);
                            break;
                        case "SpawnSkeleton":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §lСкелет реально");
                            Skeleton skeleton = (Skeleton) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);
                            skeleton.setTarget(player);
                            break;
                        case "SpawnLlama":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §b§lШтрафостанец");
                            Llama lama = (Llama) player.getWorld().spawnEntity(player.getLocation(), EntityType.LLAMA);
                            lama.setTarget(player);
                            break;
                        case "SpawnBee":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §e§lПчелка");
                            Bee bee = (Bee) player.getWorld().spawnEntity(player.getLocation(), EntityType.BEE);
                            bee.setTarget(player);
                            break;
                        case "SpawnEnderMan":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §0§lКакой-то ендер");
                            Enderman enderman = (Enderman) player.getWorld().spawnEntity(player.getLocation(), EntityType.ENDERMAN);
                            enderman.setTarget(player);
                            break;
                        case "SpawnBlaze":
                            sendMessageAboutRandom(player, "§0[§4§lПолный§e§lРанд§a§lом§0] §lБесплатный буст прохождения");
                            Blaze blaze = (Blaze) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLAZE);
                            blaze.setTarget(player);
                            break;
                        case "DestroyingLook":
                            sendMessageAboutRandom(player, "§e§lBETA §0[§4§lПолный§e§lРанд§a§lом§0] §4§lУничтожающий взгляд");
                            AtomicBoolean running = new AtomicBoolean(true);
                            new BukkitRunnable() {
                                public void run() {
                                    if(running.get()) {
                                        Objects.requireNonNull(Objects.requireNonNull(player.getWorld().rayTraceBlocks(player.getLocation(), player.getEyeLocation().getDirection(), 15)).getHitBlock()).breakNaturally();
                                    } else {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(Jokesmenu.getPlugin(Jokesmenu.class), 1L, 10L);
                            Bukkit.getScheduler().runTaskLater(Jokesmenu.getPlugin(Jokesmenu.class), () -> running.set(false), 350L);
                            break;
                    }
                    }
                }
            }.runTaskTimer(Jokesmenu.getPlugin(Jokesmenu.class), 1L, 19000L);
        } else {
            player.sendMessage("Уже активировано");
        }
    }
}
