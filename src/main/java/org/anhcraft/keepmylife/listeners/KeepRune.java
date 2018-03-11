package org.anhcraft.keepmylife.listeners;

import org.anhcraft.keepmylife.KeepReason;
import org.anhcraft.keepmylife.api.KeepMyLifeAPI;
import org.anhcraft.keepmylife.events.KeepPlayerItemEvent;
import org.anhcraft.keepmylife.utils.Configuration;
import org.anhcraft.keepmylife.utils.Strings;
import org.anhcraft.spaciouslib.protocol.ActionBar;
import org.anhcraft.spaciouslib.protocol.Title;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KeepRune implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onkeep(KeepPlayerItemEvent e){
        if(e.getReason().equals(KeepReason.DayNight)){
            if(!e.isKeep() && Configuration.config.getBoolean("keep_rune.enable")){
                boolean keep = false;

                List<ItemStack> newInv = new ArrayList<>();
                for(ItemStack item : e.getDrops()){
                    if(KeepMyLifeAPI.isKeepRune(item) && !keep){
                        keep = true;
                        Strings.sendPlayer(Configuration.config.getString("keep_rune.message"), e.getPlayer());
                        if(Configuration.config.getBoolean("keep_rune.actionbar.enable")){
                            ActionBar.create(Configuration.config.getString("keep_rune.actionbar.message")).sendPlayer(e.getPlayer());
                        }
                        if(Configuration.config.getBoolean("keep_rune.title.enable")){
                            Title.create(Configuration.config.getString("keep_rune.title.title"), Title.Type.TITLE).sendPlayer(e.getPlayer());
                            Title.create(Configuration.config.getString("keep_rune.title.subtitle"), Title.Type.SUBTITLE).sendPlayer(e.getPlayer());
                        }

                        if(1 < item.getAmount()){
                            item.setAmount(item.getAmount()-1);
                            newInv.add(item);
                        }
                    } else {
                        newInv.add(item);
                    }
                }

                if(keep){
                    e.keep(true);
                    e.setDrops(new ArrayList<>());
                    e.getPlayer().getInventory().setContents(newInv.toArray(new ItemStack[newInv.size()]));
                    e.getPlayer().updateInventory();
                }
            }
        }
    }
}