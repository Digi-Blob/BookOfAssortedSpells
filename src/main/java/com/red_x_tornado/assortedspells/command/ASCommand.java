package com.red_x_tornado.assortedspells.command;

import static net.minecraft.command.Commands.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.network.msg.SpellSyncMessage;
import com.red_x_tornado.assortedspells.util.spell.Spell;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class ASCommand {

	public static LiteralArgumentBuilder<CommandSource> create() {
		return literal("assortedspells").requires(src -> src.hasPermissionLevel(2))
				.then(literal("unlock").then(literal("*").executes(ASCommand::unlockAll))
						.then(argument("id", ResourceLocationArgument.resourceLocation())
						.suggests(suggestSpells(caps -> () -> Spell.getSpells().stream().filter(s -> !caps.isKnown(s)).iterator()))
						.executes(ASCommand::unlock)))
				.then(literal("discover").then(literal("*").executes(ASCommand::discoverAll))
						.then(argument("id", ResourceLocationArgument.resourceLocation())
						.suggests(suggestSpells(caps -> () -> Spell.getSpells().stream().filter(s -> !caps.isDiscovered(s)).iterator()))
						.executes(ASCommand::discover)))
				.then(literal("undiscover").then(literal("*").executes(ASCommand::undiscoverAll))
						.then(argument("id", ResourceLocationArgument.resourceLocation())
						.suggests(suggestSpells(caps -> () -> Spell.getSpells().stream().filter(s -> caps.isDiscovered(s)).iterator()))
						.executes(ASCommand::undiscover)));
	}

	private static int unlock(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final ResourceLocation id = ResourceLocationArgument.getResourceLocation(ctx, "id");
		final PlayerEntity player = ctx.getSource().asPlayer();
		final SpellCapability caps = SpellCapability.get(player);

		final Spell spell = Spell.find(id);
		if (spell == null) {
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.spellnotfound", id));
			return 0;
		}

		if (caps.unlock(spell)) {
			SpellSyncMessage.sync(player);
			ctx.getSource().sendFeedback(new TranslationTextComponent("assortedspells.command.unlock.success", id), false);
		} else
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.unlock.fail", id));

		return Command.SINGLE_SUCCESS;
	}

	private static int unlockAll(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final PlayerEntity player = ctx.getSource().asPlayer();
		final SpellCapability caps = SpellCapability.get(player);

		for (Spell spell : Spell.getSpells())
			caps.unlock(spell);

		SpellSyncMessage.sync(player);
		ctx.getSource().sendFeedback(new TranslationTextComponent("assortedspells.command.unlock_all.success"), false);

		return Command.SINGLE_SUCCESS;
	}

	private static int discover(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final ResourceLocation id = ResourceLocationArgument.getResourceLocation(ctx, "id");
		final PlayerEntity player = ctx.getSource().asPlayer();
		final SpellCapability caps = SpellCapability.get(player);

		final Spell spell = Spell.find(id);
		if (spell == null) {
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.spellnotfound", id));
			return 0;
		}

		if (caps.discover(spell)) {
			SpellSyncMessage.sync(player);
			ctx.getSource().sendFeedback(new TranslationTextComponent("assortedspells.command.discover.success", id), false);
		} else
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.discover.fail", id));

		return Command.SINGLE_SUCCESS;
	}

	private static int discoverAll(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final PlayerEntity player = ctx.getSource().asPlayer();
		final SpellCapability caps = SpellCapability.get(player);

		for (Spell spell : Spell.getSpells())
			caps.discover(spell);

		SpellSyncMessage.sync(player);
		ctx.getSource().sendFeedback(new TranslationTextComponent("assortedspells.command.discover_all.success"), false);

		return Command.SINGLE_SUCCESS;
	}

	private static int undiscover(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final ResourceLocation id = ResourceLocationArgument.getResourceLocation(ctx, "id");
		final PlayerEntity player = ctx.getSource().asPlayer();
		final SpellCapability caps = SpellCapability.get(player);

		final Spell spell = Spell.find(id);
		if (spell == null) {
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.spellnotfound", id));
			return 0;
		}

		if (caps.undiscover(spell)) {
			SpellSyncMessage.sync(player);
			ctx.getSource().sendFeedback(new TranslationTextComponent("assortedspells.command.undiscover.success", id), false);
		} else
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.undiscover.fail", id));

		return Command.SINGLE_SUCCESS;
	}

	private static int undiscoverAll(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final PlayerEntity player = ctx.getSource().asPlayer();
		final SpellCapability caps = SpellCapability.get(player);

		for (Spell spell : Spell.getSpells())
			caps.undiscover(spell);

		SpellSyncMessage.sync(player);
		ctx.getSource().sendFeedback(new TranslationTextComponent("assortedspells.command.undiscover_all.success"), false);

		return Command.SINGLE_SUCCESS;
	}

	public static SuggestionProvider<CommandSource> suggestSpells(SpellProvider provider) {
		return (ctx, builder) -> {
			final SpellCapability caps = SpellCapability.get(ctx.getSource().asPlayer());

			for (Spell spell : provider.apply(caps)) {
				final ResourceLocation id = spell.getId();
				final String str = id.toString();
				if (str.startsWith(builder.getRemaining()) || id.getPath().startsWith(builder.getRemaining()))
					builder.suggest(str);
			}

			return builder.buildFuture();
		};
	}

	public static interface SpellProvider {
		public Iterable<Spell> apply(SpellCapability caps) throws CommandSyntaxException;
	}
}