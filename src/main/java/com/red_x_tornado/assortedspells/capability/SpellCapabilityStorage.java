package com.red_x_tornado.assortedspells.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class SpellCapabilityStorage implements Capability.IStorage<SpellCapability> {

	@Override
	public INBT writeNBT(Capability<SpellCapability> capability, SpellCapability instance, Direction side) {
		return instance.write();
	}

	@Override
	public void readNBT(Capability<SpellCapability> capability, SpellCapability instance, Direction side, INBT nbt) {
		instance.read((CompoundNBT) nbt);
	}
}