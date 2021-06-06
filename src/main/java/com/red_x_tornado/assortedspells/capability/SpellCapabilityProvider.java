package com.red_x_tornado.assortedspells.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class SpellCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

	@CapabilityInject(SpellCapability.class)
	public static Capability<?> spellCapability = null;

	private final LazyOptional<SpellCapability> optional;

	public SpellCapabilityProvider(PlayerEntity player) {
		optional = LazyOptional.of(() -> new SpellCapability(player));
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == spellCapability ? optional.cast() : LazyOptional.empty();
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		optional.orElseThrow(() -> new IllegalStateException("Capability should not be null!"))
			.read(nbt);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return optional.orElseThrow(() -> new IllegalStateException("Capability should not be null!"))
				.write();
	}
}