package com.red_x_tornado.assortedspells.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

// Hi.
public class SpellCapability {

	private final PlayerEntity player;

	public SpellCapability(PlayerEntity player) {
		this.player = player;
	}

	public static SpellCapability get(PlayerEntity player) {
		return player.getCapability(SpellCapabilityProvider.spellCapability).orElseThrow(() -> new IllegalStateException("All players should have spell capabilities!"));
	}

	public void copyFrom(SpellCapability caps) {
		
	}

	public CompoundNBT write() {
		final CompoundNBT nbt = new CompoundNBT();

		return nbt;
	}

	public void read(CompoundNBT nbt) {
		
	}
}