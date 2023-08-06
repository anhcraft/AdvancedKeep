package dev.anhcraft.advancedkeep;

import co.aikar.commands.PaperCommandManager;
import com.google.common.base.Preconditions;
import dev.anhcraft.advancedkeep.cmd.MainCommand;
import dev.anhcraft.advancedkeep.config.MainConfig;
import dev.anhcraft.advancedkeep.config.SoulGemConfig;
import dev.anhcraft.advancedkeep.config.WorldConfig;
import dev.anhcraft.advancedkeep.integration.IntegrationManager;
import dev.anhcraft.advancedkeep.listener.EventListener;
import dev.anhcraft.advancedkeep.task.WorldTimeChangeTask;
import dev.anhcraft.advancedkeep.util.ConfigHelper;
import dev.anhcraft.jvmkit.utils.FileUtil;
import dev.anhcraft.jvmkit.utils.IOUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class AdvancedKeep extends JavaPlugin {
    private SoulGemConfig soulGemConfig;
    public MainConfig mainConfig;
    public Map<String, List<WorldConfig>> worlds = new HashMap<>();
    private NamespacedKey SOUL_GEM_KEY;
    public boolean debug;
    public IntegrationManager integrationManager;

    @Override
    public void onEnable() {
        reload();

        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        PaperCommandManager pcm = new PaperCommandManager(this);
        pcm.enableUnstableAPI("help");
        pcm.registerCommand(new MainCommand(this));

        integrationManager = new IntegrationManager(this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    public ItemStack getSoulGem(int amount) {
        ItemStack item = soulGemConfig.item.build();
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(SOUL_GEM_KEY, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public boolean isSoulGem(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            return itemStack.getItemMeta().getPersistentDataContainer().has(SOUL_GEM_KEY, PersistentDataType.BYTE);
        }
        return false;
    }

    public void reload() {
        getServer().getScheduler().cancelTasks(this);

        getDataFolder().mkdir();
        mainConfig = ConfigHelper.load(MainConfig.class, requestConfig("config.yml"));
        soulGemConfig = ConfigHelper.load(SoulGemConfig.class, requestConfig("soul-gem.yml"));
        worlds.clear();
        YamlConfiguration config = requestConfig("worlds.yml");
        for (String w : config.getKeys(false)) {
            ConfigurationSection worldConfig = config.getConfigurationSection(w);
            List<WorldConfig> list = new LinkedList<>(); // dont use TreeMap since time is nullable
            for (String t : worldConfig.getKeys(false)) {
                WorldConfig wc = ConfigHelper.load(WorldConfig.class, worldConfig.getConfigurationSection(t));
                list.add(wc);
            }
            Collections.reverse(list);
            worlds.put(w, list);
        }

        SOUL_GEM_KEY = new NamespacedKey(this, soulGemConfig.nbtTag);

        new WorldTimeChangeTask(this).runTaskTimerAsynchronously(this, 0L, 20L);
    }

    public YamlConfiguration requestConfig(String path) {
        File f = new File(getDataFolder(), path);
        Preconditions.checkArgument(f.getParentFile().exists());

        if (!f.exists()) {
            try {
                FileUtil.write(f, IOUtil.readResource(AdvancedKeep.class, "/config/" + path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(f);
    }

}
