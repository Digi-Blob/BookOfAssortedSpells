package com.red_x_tornado.assortedspells.network.msg;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.client.ClientEvents;
import com.red_x_tornado.assortedspells.network.ASNetworkManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class SpellSyncMessage {

	private final SpellCapability caps;

	public SpellSyncMessage(SpellCapability caps) {
		this.caps = caps;
	}

	public static void encode(SpellSyncMessage msg, PacketBuffer buf) {
		msg.caps.write(buf);
	}

	public static SpellSyncMessage decode(PacketBuffer buf) {
		final SpellCapability caps = new SpellCapability(null);

		caps.read(buf);

		return new SpellSyncMessage(caps);
	}

	public static void handle(SpellSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientEvents.handleSync(msg.caps));
		});
		ctx.get().setPacketHandled(true);
	}

	public static void sync(PlayerEntity player) {
		final SpellCapability caps = SpellCapability.get(player);
		sync(player, caps);
	}

	public static void sync(PlayerEntity player, SpellCapability caps) {
		final ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		ASNetworkManager.get().send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SpellSyncMessage(caps));
	}
}