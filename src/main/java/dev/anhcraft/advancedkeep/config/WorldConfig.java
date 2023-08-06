package dev.anhcraft.advancedkeep.config;

import dev.anhcraft.advancedkeep.util.Duration;
import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.PostHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.jetbrains.annotations.Nullable;

@Configurable(keyNamingStyle = Configurable.NamingStyle.TRAIN_CASE)
public class WorldConfig {
    @Nullable
    public Duration time;
    public double keepItem;
    public double keepExp;
    @Nullable
    public String permission;
    @Nullable
    public String[] broadcast;
    @Nullable
    public Sound sound;
    public String actionBar;

    @PostHandler
    private void handle() {
        keepExp = Math.min(Math.max(keepExp, 0), 1);
        keepItem = Math.min(Math.max(keepItem, 0), 1);
        if (broadcast != null) {
            for (int i = 0; i < broadcast.length; i++) {
                if (broadcast[i] != null)
                    broadcast[i] = ChatColor.translateAlternateColorCodes('&', broadcast[i]);
            }
        }
    }
}
