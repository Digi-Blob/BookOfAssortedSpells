package com.red_x_tornado.assortedspells.network;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.network.msg.SpellSelectionMessage;
import com.red_x_tornado.assortedspells.network.msg.SpellSyncMessage;
import com.red_x_tornado.assortedspells.network.msg.WandBuilderMaterialMessage;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * It's the obligatory network manager that mods need if they want to throw packets at eachother.<br>
 * This manages a {@link SimpleChannel} and adds all the packet types used by the mod.
 */
public class ASNetworkManager {

	private static final String PROTOCOL_VERSION = "1";

	private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(BookOfAssortedSpells.MOD_ID, "sync"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	/**
	 * Initializes the network manager and registers the packet types.
	 */
	public static void init() {
		int id = 0;
		CHANNEL.registerMessage(id++, SpellSelectionMessage.class, SpellSelectionMessage::encode, SpellSelectionMessage::decode, SpellSelectionMessage::handle);
		CHANNEL.registerMessage(id++, SpellSyncMessage.class, SpellSyncMessage::encode, SpellSyncMessage::decode, SpellSyncMessage::handle);
		CHANNEL.registerMessage(id++, WandBuilderMaterialMessage.class, WandBuilderMaterialMessage::encode, WandBuilderMaterialMessage::decode, WandBuilderMaterialMessage::handle);
	}

	public static SimpleChannel get() {
		return CHANNEL;
	}
}