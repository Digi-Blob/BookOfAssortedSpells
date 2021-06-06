package com.red_x_tornado.assortedspells.capability;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class SpellCapabilityProvider implements ICapabilityProvider, Runnable {

	@CapabilityInject(SpellCapability.class)
	public static Capability<SpellCapability> spellCapability = null;

	public static final ResourceLocation ID = new ResourceLocation(BookOfAssortedSpells.MOD_ID, "spells");

	private final LazyOptional<SpellCapability> optional;

	public SpellCapabilityProvider(PlayerEntity player) {
		optional = LazyOptional.of(() -> new SpellCapability(player));
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == spellCapability ? optional.cast() : LazyOptional.empty();
	}

	@Override
	public void run() {
		optional.invalidate();
	}
}