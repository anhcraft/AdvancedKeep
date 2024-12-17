package dev.anhcraft.advancedkeep.api;

import org.jetbrains.annotations.NotNull;

public final class ApiProvider {
  private static AdvancedKeepApi api;

  @NotNull
  public static AdvancedKeepApi getApi() {
    if (api == null) {
      throw new IllegalStateException("Api is not initialized");
    }
    return api;
  }
}
