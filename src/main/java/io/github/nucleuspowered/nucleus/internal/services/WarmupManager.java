/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.internal.services;

import com.google.common.collect.Maps;
import io.github.nucleuspowered.nucleus.api.service.NucleusWarmupManagerService;
import io.github.nucleuspowered.nucleus.internal.interfaces.CancellableTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import javax.annotation.concurrent.GuardedBy;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class WarmupManager implements NucleusWarmupManagerService {

    private final Object mapLock = new Object();

    @GuardedBy("mapLock")
    private final Map<UUID, Task> warmupTasks = Maps.newHashMap();

    @Override
    public void addWarmup(UUID player, Task task) {
        Task t;
        synchronized (mapLock) {
            t = warmupTasks.put(player, task);
        }

        if (t != null) {
            t.cancel();

            Consumer<Task> ct = t.getConsumer();
            if (ct instanceof CancellableTask) {
                ((CancellableTask) ct).onCancel();
            }
        }
    }

    @Override
    public boolean removeWarmup(UUID player) {
        if (warmupTasks.containsKey(player)) {
            Task t;
            synchronized (mapLock) {
                t = warmupTasks.remove(player);
            }

            if (t != null && t.cancel()) {
                if (t.getConsumer() instanceof CancellableTask) {
                    ((CancellableTask) t.getConsumer()).onCancel();
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void cleanup() {
        synchronized (mapLock) {
            warmupTasks.entrySet().stream().filter(v -> !Sponge.getGame().getScheduler().getScheduledTasks().contains(v.getValue()))
                    .map(Map.Entry::getKey)
                    .forEach(warmupTasks::remove);
        }
    }
}
