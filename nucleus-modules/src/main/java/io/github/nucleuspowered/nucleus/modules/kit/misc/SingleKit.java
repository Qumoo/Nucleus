/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.kit.misc;

import com.google.common.collect.Lists;
import io.github.nucleuspowered.nucleus.Util;
import io.github.nucleuspowered.nucleus.api.module.kit.data.Kit;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public class SingleKit implements Kit {

    private final String name;
    private final List<ItemStackSnapshot> stacks = Lists.newArrayList();
    private final List<String> commands = new ArrayList<>();
    @Nullable private Duration interval;
    private double cost = 0;
    private boolean autoRedeem = false;
    private boolean oneTime = false;
    private boolean displayOnRedeem = true;
    private boolean ignoresPermission = false;
    private boolean hidden = false;
    private boolean firstJoin = false;

    public SingleKit(final String name) {
        this.name = name;
    }

    public SingleKit(final String name, final Kit toClone) {
        this(name, toClone.getStacks(),
                toClone.getCooldown().orElse(null),
                toClone.getCost(),
                toClone.isAutoRedeem(),
                toClone.isOneTime(),
                toClone.isDisplayMessageOnRedeem(),
                toClone.ignoresPermission(),
                toClone.isHiddenFromList(),
                toClone.getCommands(),
                toClone.isFirstJoinKit());
    }

    public SingleKit(final String name,
            final List<ItemStackSnapshot> itemStackSnapshots,
            @Nullable final Duration interval, final double cost, final boolean autoRedeem, final boolean oneTime, final boolean displayOnRedeem,
            final boolean ignoresPermission, final boolean hidden, final List<String> commands, final boolean firstJoin) {
        this(name);
        this.stacks.addAll(itemStackSnapshots);
        this.interval = interval;
        this.cost = cost;
        this.autoRedeem = autoRedeem;
        this.oneTime = oneTime;
        this.displayOnRedeem = displayOnRedeem;
        this.ignoresPermission = ignoresPermission;
        this.hidden = hidden;
        this.firstJoin = firstJoin;
        this.commands.addAll(commands);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public List<ItemStackSnapshot> getStacks() {
        return Lists.newArrayList(this.stacks);
    }

    @Override
    public Kit setStacks(final List<ItemStackSnapshot> stacks) {
        this.stacks.clear();
        this.stacks.addAll(stacks);
        return this;
    }

    @Override
    public Optional<Duration> getCooldown() {
        return Optional.ofNullable(this.interval);
    }

    @Override
    public Kit setCooldown(@Nullable final Duration interval) {
        this.interval = interval;
        return this;
    }

    @Override
    public double getCost() {
        return Math.max(0, this.cost);
    }

    @Override
    public Kit setCost(final double cost) {
        this.cost = Math.max(0, cost);
        return this;
    }

    @Override
    public boolean isAutoRedeem() {
        return this.autoRedeem;
    }

    @Override
    public Kit setAutoRedeem(final boolean autoRedeem) {
        this.autoRedeem = autoRedeem;
        return this;
    }

    @Override
    public boolean isOneTime() {
        return this.oneTime;
    }

    @Override
    public Kit setOneTime(final boolean oneTime) {
        this.oneTime = oneTime;
        return this;
    }

    @Override
    public List<String> getCommands() {
        return Lists.newArrayList(this.commands);
    }

    @Override
    public Kit setCommands(final List<String> commands) {
        this.commands.clear();
        this.commands.addAll(commands);
        return this;
    }

    @Override
    public Kit updateKitInventory(final Inventory inventory) {
        final List<Inventory> slots = Lists.newArrayList(inventory.slots());
        final List<ItemStackSnapshot> stacks = slots.stream()
                .filter(x -> x.peek().isPresent() && x.peek().get().getType() != ItemTypes.NONE)
                .map(x -> x.peek().get().createSnapshot()).collect(Collectors.toList());

        // Add all the stacks into the kit list.
        return setStacks(stacks);
    }

    @Override
    public Kit updateKitInventory(final Player player) {
        return updateKitInventory(Util.getStandardInventory(player));
    }

    @Override
    public void redeemKitCommands(final Player player) {
        final ConsoleSource source = Sponge.getServer().getConsole();
        final String playerName = player.getName();
        getCommands().forEach(x -> Sponge.getCommandManager().process(source, x.replace("{{player}}", playerName)));
    }

    @Override
    public boolean isDisplayMessageOnRedeem() {
        return this.displayOnRedeem;
    }

    @Override
    public Kit setDisplayMessageOnRedeem(final boolean displayMessage) {
        this.displayOnRedeem = displayMessage;
        return this;
    }

    @Override
    public boolean ignoresPermission() {
        return this.ignoresPermission ;
    }

    @Override
    public Kit setIgnoresPermission(final boolean ignoresPermission) {
        this.ignoresPermission = ignoresPermission;
        return this;
    }

    @Override
    public boolean isHiddenFromList() {
        return this.hidden;
    }

    @Override
    public Kit setHiddenFromList(final boolean hide) {
        this.hidden = hide;
        return this;
    }

    @Override
    public boolean isFirstJoinKit() {
        return this.firstJoin;
    }

    @Override
    public Kit setFirstJoinKit(final boolean firstJoinKit) {
        this.firstJoin = firstJoinKit;
        return this;
    }

}