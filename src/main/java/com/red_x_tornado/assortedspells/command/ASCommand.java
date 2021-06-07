package com.red_x_tornado.assortedspells.command;

import static net.minecraft.command.Commands.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.network.msg.SpellSyncMessage;
import com.red_x_tornado.assortedspells.util.Spell;
import com.red_x_tornado.assortedspells.util.SpellInstance;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class ASCommand {

	public static LiteralArgumentBuilder<CommandSource> create() {
		return literal("assortedspells").requires(src -> src.hasPermissionLevel(2))
				.then(literal("unlock").then(argument("id", ResourceLocationArgument.resourceLocation()).suggests((ctx, builder) -> {
					for (ResourceLocation id : Spell.getSpellIds()) {
						final String str = id.toString();
						if (str.startsWith(builder.getRemaining()) || id.getPath().startsWith(builder.getRemaining()))
							builder.suggest(str);
					}

					return builder.buildFuture();
				}).executes(ctx -> {
					final ResourceLocation id = ResourceLocationArgument.getResourceLocation(ctx, "id");
					final Spell spell = Spell.find(id);
					if (spell == null) {
						ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.unlock.notfound", id));
						return 0;
					}

					final PlayerEntity player = ctx.getSource().asPlayer();
					final SpellCapability caps = SpellCapability.get(player);

					caps.unlock(spell);
					SpellSyncMessage.sync(player);
					ctx.getSource().sendFeedback(new TranslationTextComponent("assortedspells.command.unlock.success", id), false);

					return Command.SINGLE_SUCCESS;
				})))
				.then(literal("quickspell").then(argument("index", IntegerArgumentType.integer(0, SpellCapability.MAX_QUICK_SPELLS))
						.then(argument("id", ResourceLocationArgument.resourceLocation()).suggests((ctx, builder) -> {
							final SpellCapability caps = SpellCapability.get(ctx.getSource().asPlayer());
							for (SpellInstance spell : caps.getKnownSpells()) {
								final ResourceLocation id = spell.getSpell().getId();
								final String str = id.toString();
								if (str.startsWith(builder.getRemaining()) || id.getPath().startsWith(builder.getRemaining()))
									builder.suggest(str);
							}

							return builder.buildFuture();
						}).executes(ctx -> {
							final int index = IntegerArgumentType.getInteger(ctx, "index");
							final ResourceLocation id = ResourceLocationArgument.getResourceLocation(ctx, "id");
							final Spell spell = Spell.find(id);
							if (spell == null) {
								ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.quickspell.notfound", id));
								return 0;
							}

							final PlayerEntity player = ctx.getSource().asPlayer();
							final SpellCapability caps = SpellCapability.get(player);
							final SpellInstance instance = caps.getKnownSpells().stream().filter(s -> s.getSpell() == spell).findFirst().orElse(null);

							if (instance == null) {
								ctx.getSource().sendErrorMessage(new TranslationTextComponent("assortedspells.command.quickspell.notknown", id));
								return 0;
							}

							caps.getQuickSpells()[index] = instance;
							SpellSyncMessage.sync(player);
							ctx.getSource().sendFeedback(new TranslationTextComponent("assortedspells.command.quickspell.success", index, id), false);

							return Command.SINGLE_SUCCESS;
						}))));
	}
}