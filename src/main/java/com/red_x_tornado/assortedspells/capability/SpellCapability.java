package com.red_x_tornado.assortedspells.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class SpellCapability {

	private final PlayerEntity player;

	public SpellCapability(PlayerEntity player) {
		this.player = player;
	}

	public CompoundNBT write() {
		return new CompoundNBT();
	}

	public void read(CompoundNBT nbt) {
		
	}
}