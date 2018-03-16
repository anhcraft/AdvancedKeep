package org.anhcraft.keepmylife.listeners;

import org.anhcraft.keepmylife.api.KeepReason;
import org.anhcraft.keepmylife.events.KeepPlayerItemEvent;
import org.anhcraft.keepmylife.utils.Configuration;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayerDeathDefault implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e) {
        if(e.getKeepInventory()){
            return;
        }
        if(Configuration.compatible.getBoolean("death_keep_inventory")) {
            e.setKeepInventory(true);
        }
        if(Configuration.compatible.getBoolean("death_keep_exp")) {
            e.setDroppedExp(0);
            e.setKeepLevel(true);
        }
        Player p = e.getEntity();
        // WHTIELIST
        if(Configuration.config.getBoolean("keep_items_whitelist.enable")) {
            if(PlayerDeath.checkWhitelistedWorld(p)) {
                KeepPlayerItemEvent ev = new KeepPlayerItemEvent(p, Arrays.stream(p.getInventory()
                        .getContents()).collect(Collectors.toList()), true, KeepReason.WHITELIST);
                Bukkit.getPluginManager().callEvent(ev);

                if(!ev.isKeep()) {
                    for(ItemStack item : ev.getDrops()) {
                        if(!InventoryUtils.isNull(item)) {
                            if(Configuration.compatible.getBoolean("death_drop_items")) {
                                p.getWorld().dropItemNaturally(p.getLocation(), item);
                            }
                            if(Configuration.compatible.getBoolean("death_clear_inventory_after_drop")) {
                                p.getInventory().remove(item);
                                p.updateInventory();
                            }
                        }
                    }
                }
                return;
            }
        }

        // DAY/NIGHT
        if(Configuration.config.getBoolean("keep_items_dayNight.enable")) {
            Boolean a = false;
            if(PlayerDeath.listWorldsKeep.contains(p.getWorld())) {
                a = true;
            }
            KeepPlayerItemEvent ev = new KeepPlayerItemEvent(p,
                    Arrays.stream(p.getInventory().getContents()).collect(Collectors.toList()), a,
                    KeepReason.DAY_NIGHT);
            Bukkit.getPluginManager().callEvent(ev);

            if(!ev.isKeep()) {
                for(ItemStack item : ev.getDrops()){
                    if(!InventoryUtils.isNull(item)) {
                        if(Configuration.compatible.getBoolean("death_drop_items")) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        if(Configuration.compatible.getBoolean("death_clear_inventory_after_drop")) {
                            p.getInventory().remove(item);
                            p.updateInventory();
                        }
                    }
                }
            }
            return;
        }

        // DEFAULT
        KeepPlayerItemEvent ev = new KeepPlayerItemEvent(p, Arrays.stream(p.getInventory()
                .getContents()).collect(Collectors.toList()), false, KeepReason.DEFAULT);
        Bukkit.getPluginManager().callEvent(ev);
        if(!ev.isKeep()) {
            for(ItemStack item : ev.getDrops()){
                if(!InventoryUtils.isNull(item)) {
                    if(Configuration.compatible.getBoolean("death_drop_items")) {
                        p.getWorld().dropItemNaturally(p.getLocation(), item);
                    }
                    if(Configuration.compatible.getBoolean("death_clear_inventory_after_drop")) {
                        p.getInventory().remove(item);
                        p.updateInventory();
                    }
                }
            }
        }
    }
}