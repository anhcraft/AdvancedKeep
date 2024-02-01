package dev.anhcraft.advancedkeep.integration.bridge;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TownyBridge implements ClaimIntegration {

    @Override
    public @NotNull ClaimStatus getClaimStatus(@NotNull Location location, @Nullable Player player) {
        if (TownyAPI.getInstance().isWilderness(location))
            return ClaimStatus.WILD;
        if (player != null) {
            Resident res = TownyAPI.getInstance().getResident(player.getUniqueId());
            try {
                if (res != null && res.getTown().isInsideTown(location))
                    return ClaimStatus.TRUSTED;
            } catch (NotRegisteredException ignored) {}
        }
        return ClaimStatus.UNTRUSTED;
    }
}
