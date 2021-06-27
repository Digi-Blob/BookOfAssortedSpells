package com.red_x_tornado.assortedspells.network.msg.tome;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.util.spell.Spell;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class QuickspellModificationMessage {

	private final ResourceLocation spell;
	private final byte newSlot;

	public QuickspellModificationMessage(ResourceLocation spell, byte newSlot) {
		this.spell = spell;
		this.newSlot = newSlot;
	}

	public static void encode(QuickspellModificationMessage msg, PacketBuffer buf) {
		buf.writeResourceLocation(msg.spell);
		buf.writeByte(msg.newSlot);
	}

	public static QuickspellModificationMessage decode(PacketBuffer buf) {
		return new QuickspellModificationMessage(buf.readResourceLocation(), buf.readByte());
	}

	public static void handle(QuickspellModificationMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final SpellCapability caps = SpellCapability.get(ctx.get().getSender());
			final Spell spell = Spell.find(msg.spell);

			if (spell == null)
				BookOfAssortedSpells.LOGGER.warn("{} tried to change quickspell status of non-existent spell: {}!", ctx.get().getSender().getName(), msg.spell);
			else if (!caps.isKnown(spell))
				BookOfAssortedSpells.LOGGER.warn("{} tried to change quickspell status of unknown spell {}!", ctx.get().getSender().getName(), msg.spell);

			else if (msg.newSlot >= caps.getQuickSpells().length || msg.newSlot < -1)
				BookOfAssortedSpells.LOGGER.warn("{} tried to change quickspell status of {} using bad slot index {}!", ctx.get().getSender().getName(), msg.spell, msg.newSlot);
			else {
				if (msg.newSlot == -1) {
					final int index = caps.findSpellInQuickSpells(spell);
					if (index != -1)
						caps.getQuickSpells()[index] = null;
					else BookOfAssortedSpells.LOGGER.warn("{} tried to remove quickspell {} that isn't in their quickspells!", ctx.get().getSender().getName(), msg.spell);
				}
				else {
					final int index = caps.findSpellInQuickSpells(spell);
					if (index != -1)
						caps.getQuickSpells()[index] = null;
					caps.getQuickSpells()[msg.newSlot] = caps.getKnown(spell);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}