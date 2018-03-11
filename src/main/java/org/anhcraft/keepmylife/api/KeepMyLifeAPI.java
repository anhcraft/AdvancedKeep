package org.anhcraft.keepmylife.api;

import org.anhcraft.keepmylife.utils.Configuration;
import org.anhcraft.spaciouslib.inventory.SItems;
import org.anhcraft.spaciouslib.nbt.NBTManager;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.anhcraft.spaciouslib.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KeepMyLifeAPI {
    public static ItemStack getKeepRune(){
        SItems s = new SItems(
                Configuration.config.getString("keep_rune.item.name"),
                Material.valueOf(Configuration.config.getString("keep_rune.item.material").toUpperCase()),
                1,
                (short) Configuration.config.getInt("keep_rune.item.durability")
        ).setLores(Configuration.config.getStringList("keep_rune.item.lores"))
                .unbreakable(Configuration.config.getBoolean("keep_rune.item.unbreakable"));
        if(Configuration.config.getBoolean("keep_rune.item.hide_unbreakable")){
            s.flag(ItemFlag.HIDE_UNBREAKABLE);
        }
        if(Configuration.config.getBoolean("keep_rune.item.hide_enchants")){
            s.flag(ItemFlag.HIDE_ENCHANTS);
        }
        for(String n : Configuration.config.getStringList("keep_rune.item.enchants")){
            String[] t = n.split(":");
            s.addEnchant(Enchantment.getByName(t[0].toUpperCase()),
                    Integer.parseInt(StringUtils.toStrNumber(t[1])));
        }
        return new NBTManager(s.getItem()).setBoolean(Configuration.config.getString("keep_rune.item.nbt_tag"), true).toItemStack(s.getItem());
    }

    public static boolean isKeepRune(ItemStack item){
        return !InventoryUtils.isNull(item) && new NBTManager(item).hasKey(Configuration.config.getString("keep_rune.item.nbt_tag"));
    }
}
