package com.red_x_tornado.assortedspells.network.msg;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.util.spell.Spell;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class SpellSelectionMessage {

	private final ResourceLocation id;

	public SpellSelectionMessage(ResourceLocation id) {
		this.id = id;
	}

	public static void encode(SpellSelectionMessage msg, PacketBuffer buf) {
		buf.writeResourceLocation(msg.id);
	}

	public static SpellSelectionMessage decode(PacketBuffer buf) {
		return new SpellSelectionMessage(buf.readResourceLocation());
	}

	public static void handle(SpellSelectionMessage msg, Supplier<NetworkEvent.Context> ctx) {
		final Spell spell = Spell.find(msg.id);
		ctx.get().enqueueWork(() -> {
			final SpellCapability caps = SpellCapability.get(ctx.get().getSender());
			if (caps.isKnown(spell))
				caps.select(spell);
		});
		ctx.get().setPacketHandled(true);
	}
}