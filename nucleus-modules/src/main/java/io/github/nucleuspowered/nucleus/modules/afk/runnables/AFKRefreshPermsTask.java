/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.afk.runnables;

import io.github.nucleuspowered.nucleus.modules.afk.services.AFKHandler;
import io.github.nucleuspowered.nucleus.scaffold.task.TaskBase;
import io.github.nucleuspowered.nucleus.services.INucleusServiceCollection;
import org.spongepowered.api.scheduler.Task;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import com.google.inject.Inject;

public class AFKRefreshPermsTask implements TaskBase {

    private final AFKHandler handler;

    @Inject
    public AFKRefreshPermsTask(final INucleusServiceCollection serviceCollection) {
        this.handler = serviceCollection.getServiceUnchecked(AFKHandler.class);
    }

    @Override public boolean isAsync() {
        return true;
    }

    @Override public Duration interval() {
        return Duration.of(2, ChronoUnit.MINUTES);
    }

    @Override public void accept(final Task task) {
        this.handler.invalidateAfkCache();
    }
}