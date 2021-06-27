package com.red_x_tornado.assortedspells.network.msg.tome;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.util.spell.Spell;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class SpellBookmarkMessage {

	private final ResourceLocation spellId;
	private final boolean bookmark;

	public SpellBookmarkMessage(ResourceLocation spellId, boolean bookmark) {
		this.spellId = spellId;
		this.bookmark = bookmark;
	}

	public static void encode(SpellBookmarkMessage msg, PacketBuffer buf) {
		buf.writeResourceLocation(msg.spellId);
		buf.writeBoolean(msg.bookmark);
	}

	public static SpellBookmarkMessage decode(PacketBuffer buf) {
		return new SpellBookmarkMessage(buf.readResourceLocation(), buf.readBoolean());
	}

	public static void handle(SpellBookmarkMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final SpellCapability caps = SpellCapability.get(ctx.get().getSender());
			final Spell spell = Spell.find(msg.spellId);

			if (spell == null)
				BookOfAssortedSpells.LOGGER.warn("{} tried to bookmark non-existent spell: {}!", ctx.get().getSender().getName(), msg.spellId);
			else if (!caps.isKnown(spell))
				BookOfAssortedSpells.LOGGER.warn("{} tried to bookmark unknown spell {}!", ctx.get().getSender().getName(), msg.spellId);
			else if (msg.bookmark)
				caps.getBookmarks().add(spell);
			else
				caps.getBookmarks().remove(spell);
		});
		ctx.get().setPacketHandled(true);
	}
}