package dev.anhcraft.advancedkeep.integration.bridge;

import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GriefPreventionBridge implements ClaimIntegration {

    @Override
    public @NotNull ClaimStatus getClaimStatus(@NotNull Location location, @Nullable Player player) {
        Claim claim = GriefPrevention.getPlugin(GriefPrevention.class).dataStore.getClaimAt(location, false, false, null);
        if (claim == null) {
            return ClaimStatus.WILD;
        }
        return player != null && claim.hasExplicitPermission(player, ClaimPermission.Access) ? ClaimStatus.TRUSTED : ClaimStatus.UNTRUSTED;
    }
}
