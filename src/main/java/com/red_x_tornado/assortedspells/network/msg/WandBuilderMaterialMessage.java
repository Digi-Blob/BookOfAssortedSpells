package com.red_x_tornado.assortedspells.network.msg;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.tileentity.container.WandBuilderContainer;

import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class WandBuilderMaterialMessage {

	private final byte type;

	public WandBuilderMaterialMessage(byte type) {
		this.type = type;
	}

	public static void encode(WandBuilderMaterialMessage msg, PacketBuffer buf) {
		buf.writeByte(msg.type);
	}

	public static WandBuilderMaterialMessage decode(PacketBuffer buf) {
		return new WandBuilderMaterialMessage(buf.readByte());
	}

	public static void handle(WandBuilderMaterialMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final Container c = ctx.get().getSender().openContainer;
			if (c instanceof WandBuilderContainer)
				((WandBuilderContainer) c).setSelectedPart(msg.type);
		});
		ctx.get().setPacketHandled(true);
	}
}