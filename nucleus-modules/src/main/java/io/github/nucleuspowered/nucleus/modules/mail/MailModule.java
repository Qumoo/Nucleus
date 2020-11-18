/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.mail;

import io.github.nucleuspowered.nucleus.module.IModule;
import io.github.nucleuspowered.nucleus.modules.mail.commands.ClearMailCommand;
import io.github.nucleuspowered.nucleus.modules.mail.commands.MailCommand;
import io.github.nucleuspowered.nucleus.modules.mail.commands.MailOtherCommand;
import io.github.nucleuspowered.nucleus.modules.mail.commands.SendMailCommand;
import io.github.nucleuspowered.nucleus.modules.mail.listeners.MailListener;
import io.github.nucleuspowered.nucleus.modules.mail.services.MailHandler;
import io.github.nucleuspowered.nucleus.scaffold.command.ICommandExecutor;
import io.github.nucleuspowered.nucleus.scaffold.listener.ListenerBase;
import io.github.nucleuspowered.nucleus.services.INucleusServiceCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public final class MailModule implements IModule {

    public static final String ID = "mail";

    @Override
    public void init(final INucleusServiceCollection serviceCollection) {
        serviceCollection.registerService(MailHandler.class, new MailHandler(serviceCollection), false);
    }

    @Override
    public Collection<Class<? extends ICommandExecutor>> getCommands() {
        return Arrays.asList(
                ClearMailCommand.class,
                MailCommand.class,
                MailOtherCommand.class,
                SendMailCommand.class
        );
    }

    @Override
    public Optional<Class<?>> getPermissions() {
        return Optional.of(MailPermissions.class);
    }

    @Override public Collection<Class<? extends ListenerBase>> getListeners() {
        return Collections.singleton(MailListener.class);
    }
}
