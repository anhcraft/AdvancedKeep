package dev.anhcraft.advancedkeep.config;

import dev.anhcraft.advancedkeep.integration.ClaimStatus;
import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Path;
import dev.anhcraft.config.annotations.Validation;

import java.util.Collections;
import java.util.Map;

@Configurable(keyNamingStyle = Configurable.NamingStyle.TRAIN_CASE)
public class MainConfig {
    @Path("send-death-location.enabled")
    public boolean sendDeathLocationEnabled;

    @Path("send-death-location.message")
    @Validation(notNull = true)
    public String sendDeathLocationMessage;

    @Validation(notNull = true, silent = true)
    public Map<ClaimStatus, Boolean> claimKeepItem = Collections.emptyMap();

    @Validation(notNull = true, silent = true)
    public Map<ClaimStatus, Boolean> claimKeepExp = Collections.emptyMap();
}
