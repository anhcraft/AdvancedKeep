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
    public @NotNull KeepStatus getKeepStatus(@NotNull Location location, @Nullable Player player) {
        boolean item = false;
        boolean exp = false;
        for (Integration i : integrationManager.getIntegrations()) {
            if (i instanceof StateIntegration) {
                if (((StateIntegration) i).getKeepStatus(location, player).item()) item = true;
                if (((StateIntegration) i).getKeepStatus(location, player).exp()) exp = true;
            }
        }
        return new KeepStatus(item, exp);
    }
}
