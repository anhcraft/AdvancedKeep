package dev.anhcraft.advancedkeep.integration.bridge;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DoubleFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import dev.anhcraft.advancedkeep.integration.KeepRatio;
import dev.anhcraft.jvmkit.utils.ObjectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldGuardBridge implements StateIntegration {
    public DoubleFlag KEEP_ITEM_FLAG = new DoubleFlag("keep-item");
    public DoubleFlag KEEP_EXP_FLAG = new DoubleFlag("keep-exp");

    public WorldGuardBridge(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        registry.register(KEEP_ITEM_FLAG);
        registry.register(KEEP_EXP_FLAG);
    }

    @Override
    public @NotNull KeepRatio getKeepRatio(@NotNull Location location, @Nullable Player player) {
        ApplicableRegionSet applicableRegions = WorldGuard.getInstance().getPlatform().getRegionContainer()
                .createQuery()
                .getApplicableRegions(BukkitAdapter.adapt(location));
        return new KeepRatio(
                ObjectUtil.optional(applicableRegions.queryValue(null, KEEP_ITEM_FLAG), 0d),
                ObjectUtil.optional(applicableRegions.queryValue(null, KEEP_EXP_FLAG), 0d)
        );
    }
}