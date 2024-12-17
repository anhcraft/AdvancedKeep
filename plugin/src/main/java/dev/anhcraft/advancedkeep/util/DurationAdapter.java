package dev.anhcraft.advancedkeep.util;

import dev.anhcraft.config.ConfigDeserializer;
import dev.anhcraft.config.ConfigSerializer;
import dev.anhcraft.config.adapters.TypeAdapter;
import dev.anhcraft.config.struct.SimpleForm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class DurationAdapter implements TypeAdapter<Duration> {
    @Override
    public @Nullable SimpleForm simplify(@NotNull ConfigSerializer configSerializer, @NotNull Type type, @NotNull Duration duration) throws Exception {
        return SimpleForm.of(duration.getBegin()+":"+duration.getEnd());
    }

    @Override
    public @Nullable Duration complexify(@NotNull ConfigDeserializer configDeserializer, @NotNull Type type, @NotNull SimpleForm simpleForm) throws Exception {
        if (simpleForm.isString()) {
            String[] split = simpleForm.asString().split(":");
            return new Duration(
                    Integer.parseInt(split[0]),
                    Integer.parseInt(split[1])
            );
        }
        return null;
    }
}
