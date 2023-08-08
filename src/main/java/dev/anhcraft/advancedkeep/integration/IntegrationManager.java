package dev.anhcraft.advancedkeep.integration;

import dev.anhcraft.advancedkeep.AdvancedKeep;
import dev.anhcraft.advancedkeep.integration.bridge.LandsBridge;
import dev.anhcraft.advancedkeep.integration.bridge.WorldGuardBridge;
import dev.anhcraft.config.utils.ObjectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IntegrationManager {
    private final Map<String, Integration> integrationMap = new HashMap<>();
    private final AdvancedKeep mainPlugin;
    private final ClaimAggregator claimAggregator;
    private final StateAggregator stateAggregator;

    public IntegrationManager(AdvancedKeep mainPlugin) {
        this.mainPlugin = mainPlugin;

        tryHook("Lands", LandsBridge.class);
        tryHook("WorldGuard", WorldGuardBridge.class);

        claimAggregator = new ClaimAggregator(this);
        stateAggregator = new StateAggregator(this);
    }

    private void tryHook(String plugin, Class<? extends Integration> clazz) {
        if (mainPlugin.mainConfig.integration.getOrDefault(plugin, true) &&
                mainPlugin.getServer().getPluginManager().isPluginEnabled(plugin)) {
            Object instance = null;
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 1) {
                    try {
                        instance = constructor.newInstance(this.mainPlugin);
                        break;
                    } catch (InstantiationException | IllegalAccessException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                } else if (constructor.getParameterCount() == 0) {
                    try {
                        instance = constructor.newInstance();
                        break;
                    } catch (InstantiationException | IllegalAccessException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (instance == null) {
                try {
                    instance = ObjectUtil.newInstance(clazz);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
            integrationMap.put(plugin, (Integration) instance);
            this.mainPlugin.getLogger().info("[Integration] Hooked to " + plugin);
        }
    }

    public Integration getIntegration(String plugin) {
        return integrationMap.get(plugin);
    }

    Collection<Integration> getIntegrations() {
        return integrationMap.values();
    }

    public ClaimAggregator getClaimAggregator() {
        return claimAggregator;
    }

    public StateAggregator getStateAggregator() {
        return stateAggregator;
    }
}
