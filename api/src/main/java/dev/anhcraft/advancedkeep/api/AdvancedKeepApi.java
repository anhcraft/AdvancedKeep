package dev.anhcraft.advancedkeep.api;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AdvancedKeepApi {
  /**
   * Gets soul gem for the given amount.
   * @param amount the amount
   * @return the soul gem
   */
  @NotNull
  ItemStack getSoulGem(int amount);
}
