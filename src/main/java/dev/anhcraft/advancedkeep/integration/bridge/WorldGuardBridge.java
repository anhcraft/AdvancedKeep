package dev.anhcraft.advancedkeep.integration.bridge;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import dev.anhcraft.advancedkeep.integration.KeepStatus;
import dev.anhcraft.jvmkit.utils.ObjectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldGuardBridge implements StateIntegration {
    public BooleanFlag KEEP_ITEM_FLAG = new BooleanFlag("keep-item");
    public BooleanFlag KEEP_EXP_FLAG = new BooleanFlag("keep-exp");

    public WorldGuardBridge(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        registry.register(KEEP_ITEM_FLAG);
        registry.register(KEEP_EXP_FLAG);
    }

    @Override
    public @NotNull KeepStatus getKeepStatus(@NotNull Location location, @Nullable Player player) {
        ApplicableRegionSet applicableRegions = WorldGuard.getInstance().getPlatform().getRegionContainer()
                .createQuery()
                .getApplicableRegions(BukkitAdapter.adapt(location));
        return new KeepStatus(
                ObjectUtil.optional(applicableRegions.queryValue(null, KEEP_ITEM_FLAG), false),
                ObjectUtil.optional(applicableRegions.queryValue(null, KEEP_EXP_FLAG), false)
        );
    }
}