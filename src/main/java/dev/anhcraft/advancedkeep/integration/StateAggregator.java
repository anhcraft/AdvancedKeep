package dev.anhcraft.advancedkeep.integration;

import dev.anhcraft.advancedkeep.integration.bridge.StateIntegration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StateAggregator implements StateIntegration {
    private final IntegrationManager integrationManager;

    public StateAggregator(IntegrationManager integrationManager) {
        this.integrationManager = integrationManager;
    }

    @Override
    public @NotNull KeepRatio getKeepRatio(@NotNull Location location, @Nullable Player player) {
        double item = 0;
        double exp = 0;
        for (Integration i : integrationManager.getIntegrations()) {
            if (i instanceof StateIntegration) {
                item = Math.max(item, ((StateIntegration) i).getKeepRatio(location, player).item());
                exp = Math.max(exp, ((StateIntegration) i).getKeepRatio(location, player).exp());
            }
        }
        return new KeepRatio(item, exp);
    }
}
