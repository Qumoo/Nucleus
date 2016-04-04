/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.world.commands;

import io.github.nucleuspowered.nucleus.Util;
import io.github.nucleuspowered.nucleus.internal.annotations.Permissions;
import io.github.nucleuspowered.nucleus.internal.annotations.RegisterCommand;
import io.github.nucleuspowered.nucleus.internal.command.CommandBase;
import io.github.nucleuspowered.nucleus.internal.permissions.SuggestedLevel;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Sets spawn of world.
 *
 * Command Usage: /world setspawn Permission: nucleus.world.setspawn.base
 */
@Permissions(root = "world", suggestedLevel = SuggestedLevel.ADMIN)
@RegisterCommand(value = {"setspawn"}, subcommandOf = WorldCommand.class)
public class SetSpawnWorldCommand extends CommandBase<Player> {

    @Override
    public CommandResult executeCommand(Player pl, CommandContext args) throws Exception {
        pl.getWorld().getProperties().setSpawnPosition(pl.getLocation().getBlockPosition());
        pl.sendMessage(Util.getTextMessageWithFormat("command.world.setspawn.success"));
        return CommandResult.success();
    }
}
