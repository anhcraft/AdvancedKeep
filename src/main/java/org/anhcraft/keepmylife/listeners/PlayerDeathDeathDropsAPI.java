package org.anhcraft.keepmylife.listeners;

import net.minefs.DeathDropsAPI.PlayerDeathDropEvent;
import org.anhcraft.keepmylife.KeepReason;
import org.anhcraft.keepmylife.events.KeepPlayerItemEvent;
import org.anhcraft.keepmylife.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayerDeathDeathDropsAPI implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathDropEvent e) {
        Player p = e.getPlayer();
        if(Configuration.config.getBoolean("keep_items.enable")) {
            if(PlayerDeath.isPlayerInSpecialWorld(p)) {
                if(p.hasPermission(Configuration.config.getString("keep_items.permission"))
                        || p.isOp()) {
                    KeepPlayerItemEvent ev = new KeepPlayerItemEvent(p,
                            Arrays.stream(p.getInventory().getContents()).collect(Collectors.toList())
                            , true,
                            KeepReason.Whitelist);
                    Bukkit.getPluginManager().callEvent(ev);

                    if(!ev.isKeep()) {
                        if(ev.getDrops().contains(e.getItem())){
                            e.setCancelled(false);
                        } else {
                            e.setCancelled(true);
                        }
                        return;
                    }
                }
                return;
            }
        }
        if(Configuration.config.getBoolean("keep_items_dayNight.enable")) {
            Boolean b = false;
            if(PlayerDeath.listWorldsKeep.contains(p.getWorld())) {
                b = true;
            }
            KeepPlayerItemEvent ev = new KeepPlayerItemEvent(p,
                    Arrays.stream(p.getInventory().getContents()).collect(Collectors.toList())
                    , b,
                    KeepReason.DayNight);
            Bukkit.getPluginManager().callEvent(ev);

            if(!ev.isKeep()) {
                if(ev.getDrops().contains(e.getItem())){
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}