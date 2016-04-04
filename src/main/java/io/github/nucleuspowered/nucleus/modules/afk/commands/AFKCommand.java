/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.afk.commands;

import com.google.inject.Inject;
import io.github.nucleuspowered.nucleus.NameUtil;
import io.github.nucleuspowered.nucleus.Util;
import io.github.nucleuspowered.nucleus.internal.annotations.NoCooldown;
import io.github.nucleuspowered.nucleus.internal.annotations.NoCost;
import io.github.nucleuspowered.nucleus.internal.annotations.NoWarmup;
import io.github.nucleuspowered.nucleus.internal.annotations.Permissions;
import io.github.nucleuspowered.nucleus.internal.annotations.RegisterCommand;
import io.github.nucleuspowered.nucleus.internal.annotations.RunAsync;
import io.github.nucleuspowered.nucleus.internal.command.CommandBase;
import io.github.nucleuspowered.nucleus.internal.permissions.SuggestedLevel;
import io.github.nucleuspowered.nucleus.modules.afk.handlers.AFKHandler;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.channel.MessageChannel;

@RegisterCommand("afk")
@Permissions(suggestedLevel = SuggestedLevel.USER)
@NoCooldown
@NoWarmup
@NoCost
@RunAsync
public class AFKCommand extends CommandBase<Player> {

    @Inject private AFKHandler afkHandler;

    @Override
    public CommandResult executeCommand(Player src, CommandContext args) throws Exception {
        if (permissions.testSuffix(src, "exempt.toggle")) {
            src.sendMessage(Util.getTextMessageWithFormat("command.afk.exempt"));
            return CommandResult.empty();
        }

        boolean isAFK = afkHandler.getAFKData(src).isAFK();

        if (isAFK) {
            afkHandler.updateUserActivity(src.getUniqueId());
            MessageChannel.TO_ALL
                    .send(Util.getTextMessageWithFormat("afk.fromafk", NameUtil.getSerialisedName(src)));
        } else {
            afkHandler.setAFK(src.getUniqueId(), true);
            MessageChannel.TO_ALL
                    .send(Util.getTextMessageWithFormat("afk.toafk", NameUtil.getSerialisedName(src)));
        }

        return CommandResult.success();
    }
}
