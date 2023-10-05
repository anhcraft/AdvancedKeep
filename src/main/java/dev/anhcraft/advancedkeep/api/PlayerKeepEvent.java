package dev.anhcraft.advancedkeep.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerKeepEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private boolean keepInventory;
    private List<ItemStack> dropItems;
    private List<ItemStack> keepItems;
    private boolean keepExp;

    public PlayerKeepEvent(@NotNull Player who, boolean keepInventory, @NotNull List<ItemStack> dropItems, @NotNull List<ItemStack> keepItems, boolean keepExp) {
        super(who);
        this.keepInventory = keepInventory;
        this.dropItems = dropItems;
        this.keepItems = keepItems;
        this.keepExp = keepExp;
    }

    public boolean shouldKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    @NotNull
    public List<ItemStack> getDropItems() {
        return dropItems;
    }

    public void setDropItems(@NotNull List<ItemStack> dropItems) {
        this.dropItems = dropItems;
    }

    @NotNull
    public List<ItemStack> getKeepItems() {
        return keepItems;
    }

    public void setKeepItems(@NotNull List<ItemStack> keepItems) {
        this.keepItems = keepItems;
    }

    public boolean shouldKeepExp() {
        return keepExp;
    }

    public void setKeepExp(boolean keepExp) {
        this.keepExp = keepExp;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
