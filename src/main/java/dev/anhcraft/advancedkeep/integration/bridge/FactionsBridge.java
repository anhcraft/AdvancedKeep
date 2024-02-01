package dev.anhcraft.advancedkeep.integration.bridge;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FactionsBridge implements ClaimIntegration {

    @Override
    public @NotNull ClaimStatus getClaimStatus(@NotNull Location location, @Nullable Player player) {
        Faction f = Board.getInstance().getFactionAt(FLocation.wrap(location));
        if(f == null || f.isWilderness())
            return ClaimStatus.WILD;
        Faction pf = FPlayers.getInstance().getByPlayer(player).getFaction();
        return pf != null && pf.getId().equals(f.getId()) ? ClaimStatus.TRUSTED : ClaimStatus.UNTRUSTED;
    }
}
