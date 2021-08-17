package com.red_x_tornado.assortedspells.network.msg.research;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.tileentity.container.ResearchTableContainer;
import com.red_x_tornado.assortedspells.util.spell.Spell;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class ResearchSelectionMessage {

	@Nullable
	private final ResourceLocation id;

	public ResearchSelectionMessage(@Nullable Spell spell) {
		this(spell == null ? null : spell.getId());
	}

	private ResearchSelectionMessage(@Nullable ResourceLocation id) {
		this.id = id;
	}

	public static void encode(ResearchSelectionMessage msg, PacketBuffer buf) {
		buf.writeBoolean(msg.id != null);
		if (msg.id != null)
			buf.writeResourceLocation(msg.id);
	}

	public static ResearchSelectionMessage decode(PacketBuffer buf) {
		return new ResearchSelectionMessage(buf.readBoolean() ? buf.readResourceLocation() : null);
	}

	public static void handle(ResearchSelectionMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final ServerPlayerEntity sender = ctx.get().getSender();
			final SpellCapability caps = SpellCapability.get(sender);
			final Spell spell = msg.id == null ? null : Spell.find(msg.id);
			if (spell == null && msg.id != null)
				BookOfAssortedSpells.LOGGER.warn("{} tried to select non-existent spell {} for research!", sender.getName().getString(), msg.id);
			else if (spell != null && !caps.isDiscovered(spell))
				BookOfAssortedSpells.LOGGER.warn("{} tried to select undiscovered spell {} for research!", sender.getName().getString(), msg.id);
			else if (spell != null && caps.isKnown(spell))
				BookOfAssortedSpells.LOGGER.warn("{} tried to select already researched spell {} for research!", sender.getName().getString(), msg.id);
			else {
				// I can't wait for instanceof pattern-matching in MC.
				if (sender.openContainer instanceof ResearchTableContainer) {
					final ResearchTableContainer cont = (ResearchTableContainer) sender.openContainer;
					cont.onSelectionChange(spell == null ? null : caps.getResearch().get(spell));
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}