package dev.anhcraft.advancedkeep.integration;

import dev.anhcraft.advancedkeep.integration.bridge.ClaimIntegration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClaimAggregator implements ClaimIntegration {
    private final IntegrationManager integrationManager;

    public ClaimAggregator(IntegrationManager integrationManager) {
        this.integrationManager = integrationManager;
    }

    @Override
    public @NotNull ClaimStatus getClaimStatus(@NotNull Location location, @Nullable Player player) {
        int p = 0;
        for (Integration i : integrationManager.getIntegrations()) {
            if (i instanceof ClaimIntegration) {
                p = Math.max(p, ((ClaimIntegration) i).getClaimStatus(location, player).ordinal());
            }
        }
        return ClaimStatus.values()[p];
    }
}
