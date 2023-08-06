package dev.anhcraft.advancedkeep.config;

import dev.anhcraft.config.annotations.Configurable;
import dev.anhcraft.config.annotations.Path;
import dev.anhcraft.config.annotations.Validation;

@Configurable(keyNamingStyle = Configurable.NamingStyle.TRAIN_CASE)
public class MainConfig {
    @Path("send-death-location.enabled")
    public boolean sendDeathLocationEnabled;

    @Path("send-death-location.message")
    @Validation(notNull = true)
    public String sendDeathLocationMessage;
}
