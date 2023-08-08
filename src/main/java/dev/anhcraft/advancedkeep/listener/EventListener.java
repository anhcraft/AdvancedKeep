package dev.anhcraft.advancedkeep.listener;

import dev.anhcraft.advancedkeep.AdvancedKeep;
import dev.anhcraft.advancedkeep.config.WorldConfig;
import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import dev.anhcraft.advancedkeep.integration.KeepRatio;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EventListener implements Listener {

    private final AdvancedKeep plugin;
    private final ItemStack empty;

    public EventListener(AdvancedKeep plugin) {
        this.plugin = plugin;
        this.empty = new ItemStack(Material.AIR);
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void death(PlayerDeathEvent event){
        Player p = event.getEntity();
        Location location = p.getLocation();

        if (plugin.mainConfig.sendDeathLocationEnabled && p.hasPermission("keep.death-location")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                plugin.mainConfig.sendDeathLocationMessage,
                location.getBlockX(), location.getBlockY(), location.getBlockZ()
            )));
        }

        if (event.getKeepInventory() && event.getKeepLevel()) {
            event.setDroppedExp(0);
            event.getDrops().clear();
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] Already kept all - Skipped",
                        p.getName()
                ));
            }
            return;
        }

        double keepItemRatio = event.getKeepInventory() ? 1 : 0;
        double keepExpRatio = event.getKeepLevel() ? 1 : 0;

        if (p.hasPermission("keep.item")) {
            keepItemRatio = 1;
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] Keep item guaranteed",
                        p.getName()
                ));
            }
        }

        if (p.hasPermission("keep.exp")) {
            keepExpRatio = 1;
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] Keep exp guaranteed",
                        p.getName()
                ));
            }
        }

        World w = p.getWorld();
        List<WorldConfig> wg = plugin.worlds.get(w.getName());
        if(wg != null) {
            for (WorldConfig config : wg) {
                long time = w.getTime();
                if ((config.time == null || (time >= config.time.getBegin() && time <= config.time.getEnd())) &&
                        (config.permission == null || p.hasPermission(config.permission))) {
                    keepItemRatio = Math.max(keepItemRatio, config.keepItem);
                    keepExpRatio = Math.max(keepExpRatio, config.keepExp);
                    if (plugin.debug) {
                        plugin.getLogger().info(String.format(
                                "[Debug#%s] Pre-check: Keep item = %.1f, keep exp = %.1f",
                                p.getName(), keepItemRatio, keepExpRatio
                        ));
                    }
                }
            }
        }

        KeepRatio keepRatio = plugin.integrationManager.getStateAggregator().getKeepRatio(location, p);
        keepItemRatio = Math.max(keepItemRatio, keepRatio.item());
        keepExpRatio = Math.max(keepExpRatio, keepRatio.exp());

        ClaimStatus claimStatus = plugin.integrationManager.getClaimAggregator().getClaimStatus(location, p);
        keepItemRatio = Math.max(keepItemRatio, plugin.mainConfig.claimKeepItemRatio.getOrDefault(claimStatus, 0d));
        keepExpRatio = Math.max(keepExpRatio, plugin.mainConfig.claimKeepExpRatio.getOrDefault(claimStatus, 0d));

        if (Math.abs(keepItemRatio - 1.0) < 0.01 && Math.abs(keepExpRatio - 1.0) < 0.01) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setKeepLevel(true);
            event.setDroppedExp(0);
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] 100%% kept all items & exp",
                        p.getName()
                ));
            }
            return;
        }

        ItemStack[] items = p.getInventory().getContents();
        int presentCount = 0;

        for (int i = 0; i < items.length; i++) {
            if (plugin.isSoulGem(items[i])) {
                ItemStack cloneGem = items[i].clone();
                cloneGem.setAmount(cloneGem.getAmount() - 1);
                p.getInventory().setItem(i, cloneGem);
                presentCount = -1;
                break;
            } else if (isPresent(items[i])) {
                presentCount++;
            }
        }

        if (presentCount == -1) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setKeepLevel(true);
            event.setDroppedExp(0);
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] Kept all items & exp due to soul gem",
                        p.getName()
                ));
            }
            return;
        }

        event.setKeepInventory(true);
        event.getDrops().clear(); // 1.14.4 fix

        if (Math.abs(keepItemRatio - 1.0) < 0.01) {
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] 100%% Keep Item | Kept all items",
                        p.getName()
                ));
            }
        } else {
            int keepPresent = (int) (presentCount * keepItemRatio);
            ItemStack[] keptItems = new ItemStack[items.length];

            for (int i = 0; i < items.length; i++) {
                if (keepPresent > 0 &&
                        isPresent(items[i]) &&
                        ThreadLocalRandom.current().nextBoolean()) {
                    keptItems[i] = items[i];
                    keepPresent--;
                } else {
                    keptItems[i] = empty;
                    if (isPresent(items[i])) {
                        p.getWorld().dropItemNaturally(location, items[i]);
                    }
                }
            }

            p.getInventory().setContents(keptItems);

            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] Kept %d items (including empty), dropped %d items",
                        p.getName(), keepPresent, items.length - keepPresent
                ));
            }
        }

        if (Math.abs(keepExpRatio - 1.0) < 0.01) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] 100%% Keep Exp | Kept all exp",
                        p.getName()
                ));
            }
        } else {
            event.setKeepLevel(false);
            int lastExp = p.getTotalExperience();
            int keepExp = (int) (lastExp * keepExpRatio);
            event.setNewTotalExp(keepExp);
            event.setDroppedExp(lastExp - keepExp);
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] Kept %d exp, dropped %d exp",
                        p.getName(), keepExp, event.getDroppedExp()
                ));
            }
        }
    }

    private boolean isPresent(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }
}
