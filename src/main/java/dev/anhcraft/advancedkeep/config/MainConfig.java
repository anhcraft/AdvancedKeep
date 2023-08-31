package dev.anhcraft.advancedkeep.config;

import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Path;
import dev.anhcraft.config.annotations.Validation;

import java.util.Collections;
import java.util.Map;

@Configurable(keyNamingStyle = Configurable.NamingStyle.TRAIN_CASE)
public class MainConfig {
    @Path("death-tracker.enabled")
    public boolean deathTrackerEnabled;

    @Path("death-tracker.message")
    @Validation(notNull = true)
    public String deathTrackerMessage;

    @Validation(notNull = true, silent = true)
    public Map<ClaimStatus, Boolean> claimKeepItem = Collections.emptyMap();

    @Validation(notNull = true, silent = true)
    public Map<ClaimStatus, Boolean> claimKeepExp = Collections.emptyMap();

    public long timeCheckInterval;
}
