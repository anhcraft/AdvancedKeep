package dev.anhcraft.advancedkeep.integration.bridge;

import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import dev.anhcraft.advancedkeep.integration.Integration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClaimIntegration extends Integration {
    @NotNull
    ClaimStatus getClaimStatus(@NotNull Location location, @Nullable Player player);
}
