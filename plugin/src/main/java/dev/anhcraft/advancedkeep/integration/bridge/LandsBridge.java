package dev.anhcraft.advancedkeep.integration.bridge;

import dev.anhcraft.advancedkeep.AdvancedKeep;
import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LandsBridge implements ClaimIntegration {
    private final LandsIntegration addon;

    public LandsBridge(AdvancedKeep plugin) {
        addon = LandsIntegration.of(plugin);
    }

    @Override
    public @NotNull ClaimStatus getClaimStatus(@NotNull Location location, @Nullable Player player) {
        Area area = addon.getArea(location);
        if (area == null) {
            return ClaimStatus.WILD;
        }
        return player != null && area.isTrusted(player.getUniqueId()) ? ClaimStatus.TRUSTED : ClaimStatus.UNTRUSTED;
    }
}
