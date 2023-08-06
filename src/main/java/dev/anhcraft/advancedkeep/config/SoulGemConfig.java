package dev.anhcraft.advancedkeep.config;

import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.bukkit.utils.ItemBuilder;

@Configurable(keyNamingStyle = Configurable.NamingStyle.TRAIN_CASE)
public class SoulGemConfig {
    public ItemBuilder item;
    public String nbtTag;
}
