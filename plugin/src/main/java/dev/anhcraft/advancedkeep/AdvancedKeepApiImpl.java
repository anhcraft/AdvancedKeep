package dev.anhcraft.advancedkeep;

import dev.anhcraft.advancedkeep.api.AdvancedKeepApi;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AdvancedKeepApiImpl implements AdvancedKeepApi {
  private final AdvancedKeep plugin;

  public AdvancedKeepApiImpl(AdvancedKeep plugin) {
    this.plugin = plugin;
  }

  @Override
  public @NotNull ItemStack getSoulGem(int amount) {
    return plugin.getSoulGem(amount);
  }
}
