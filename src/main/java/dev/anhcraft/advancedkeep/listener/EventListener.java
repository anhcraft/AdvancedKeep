package dev.anhcraft.advancedkeep.listener;

import dev.anhcraft.advancedkeep.AdvancedKeep;
import dev.anhcraft.advancedkeep.api.PlayerKeepEvent;
import dev.anhcraft.advancedkeep.config.WorldConfig;
import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import dev.anhcraft.advancedkeep.integration.KeepStatus;
import dev.anhcraft.config.bukkit.utils.ColorUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventListener implements Listener {

    private final AdvancedKeep plugin;

    public EventListener(AdvancedKeep plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void death(PlayerDeathEvent event){
        Player p = event.getEntity();
        Location location = p.getLocation();

        if (plugin.mainConfig.deathTrackerEnabled && p.hasPermission("keep.death-tracker")) {
            p.sendMessage(ColorUtil.colorize(plugin.mainConfig.deathTrackerMessage
                    .replace("{world}", location.getWorld().getName())
                    .replace("{x}", String.valueOf(location.getBlockX()))
                    .replace("{y}", String.valueOf(location.getBlockY()))
                    .replace("{z}", String.valueOf(location.getBlockZ()))
            ));
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

        boolean keepInventory = event.getKeepInventory();
        boolean keepExp = event.getKeepLevel();

        if (p.hasPermission("keep.item")) {
            keepInventory = true;
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] Keep item guaranteed",
                        p.getName()
                ));
            }
        }

        if (p.hasPermission("keep.exp")) {
            keepExp = true;
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
                    if (config.keepItem) keepInventory = true;
                    if (config.keepExp) keepExp = true;
                    if (plugin.debug) {
                        plugin.getLogger().info(String.format(
                                "[Debug#%s] Pre-check: Keep item = %b, keep exp = %b",
                                p.getName(), keepInventory, keepExp
                        ));
                    }
                }
            }
        }

        KeepStatus keepRatio = plugin.integrationManager.getStateAggregator().getKeepRatio(location, p);
        if (keepRatio.item()) keepInventory = true;
        if (keepRatio.exp()) keepExp = true;

        ClaimStatus claimStatus = plugin.integrationManager.getClaimAggregator().getClaimStatus(location, p);
        if (plugin.mainConfig.claimKeepItem.getOrDefault(claimStatus, false)) keepInventory = true;
        if (plugin.mainConfig.claimKeepExp.getOrDefault(claimStatus, false)) keepExp = true;

        if (keepInventory && keepExp) {
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

        List<ItemStack> toDropItems = new ArrayList<>(Arrays.asList(items));
        List<ItemStack> toKeepItems = new ArrayList<>();

        PlayerKeepEvent keepEvent = new PlayerKeepEvent(p, keepInventory, toDropItems, toKeepItems, keepExp);
        plugin.getServer().getPluginManager().callEvent(keepEvent);
        toDropItems = keepEvent.getDropItems();
        toKeepItems = keepEvent.getKeepItems();
        keepInventory = keepEvent.shouldKeepInventory();
        keepExp = keepEvent.shouldKeepExp();

        if (keepInventory) {
            if (plugin.debug) {
                plugin.getLogger().info(String.format(
                        "[Debug#%s] 100%% Keep Item | Kept all items",
                        p.getName()
                ));
            }
        } else {
            for (ItemStack item : toDropItems) {
                if (isPresent(item)) {
                    p.getWorld().dropItemNaturally(location, item);
                }
            }

            p.getInventory().setContents(toKeepItems.toArray(new ItemStack[0]));

            if (plugin.debug) {
                plugin.getLogger().info(String.format("[Debug#%s] Dropped all items", p.getName()));
            }
        }

        if (keepExp) {
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
            if (plugin.debug) {
                plugin.getLogger().info(String.format("[Debug#%s] Dropped all exp", p.getName()));
            }
        }
    }

    private boolean isPresent(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }
}
