package dev.anhcraft.advancedkeep.integration.bridge;

import dev.anhcraft.advancedkeep.integration.Integration;
import dev.anhcraft.advancedkeep.integration.KeepStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StateIntegration extends Integration {
    @NotNull
    KeepStatus getKeepRatio(@NotNull Location location, @Nullable Player player);
}
