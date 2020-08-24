package io.github.nucleuspowered.nucleus.modules.admin.parameter;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.Entity;

import java.util.function.Predicate;

public final class AdminParameters {

    public final static Parameter.Value<Predicate<Entity>> ENTITY_PARAMETER = Parameter
            .builder(new TypeToken<Predicate<Entity>>() {})
            .parser(new EntityTypeValueParameter())
            .setKey("type")
            .build();

    private AdminParameters() { }

}