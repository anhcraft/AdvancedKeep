package dev.anhcraft.advancedkeep.task;

import dev.anhcraft.advancedkeep.AdvancedKeep;
import dev.anhcraft.advancedkeep.config.WorldConfig;
import dev.anhcraft.advancedkeep.util.Duration;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldTimeChangeTask extends BukkitRunnable {
    private final Map<String, Duration> lastWorldDuration = new HashMap<>();
    private final AdvancedKeep plugin;

    public WorldTimeChangeTask(AdvancedKeep plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Map.Entry<String, List<WorldConfig>> ent : plugin.worlds.entrySet()) {
            World w = Bukkit.getWorld(ent.getKey());
            if (w == null) {
                continue;
            }
            long time = w.getTime();

            for (WorldConfig config : ent.getValue()) {
                if (config.time != null && time >= config.time.getBegin() && time <= config.time.getEnd()) {
                    Duration d = lastWorldDuration.get(ent.getKey());
                    if (d == null || !d.equals(config.time)) {
                        for (Player player : w.getPlayers()) {
                            if (config.broadcast != null) {
                                player.sendMessage(config.broadcast);
                            }
                            if (config.sound != null) {
                                player.playSound(player.getLocation(), config.sound, 1, 1);
                            }
                            if (config.actionBar != null) {
                                player.spigot().sendMessage(
                                        ChatMessageType.ACTION_BAR,
                                        TextComponent.fromLegacyText(config.actionBar)
                                );
                            }
                        }
                        lastWorldDuration.put(ent.getKey(), config.time);
                    }
                    break;
                }
            }
        }
    }
}
