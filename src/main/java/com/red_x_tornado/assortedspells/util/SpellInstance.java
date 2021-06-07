package com.red_x_tornado.assortedspells.util;

import com.red_x_tornado.assortedspells.capability.SpellCapability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class SpellInstance {

	private final Spell spell;
	private int level = 1;
	private int maxCasts = 1;
	private int casts = 1;
	private int cooldown = 0;
	private int maxCooldown;
	private int duration;

	public SpellInstance(Spell spell) {
		this.spell = spell;
		duration = spell.getBaseDuration();
		maxCooldown = spell.getBaseCooldown();
	}

	public boolean canCast(SpellCapability caps, ItemStack wand) {
		if (cooldown > 0 || casts == 0) return false;

		return true;
	}

	public void applyCast(SpellCapability caps, ISpellCaster caster) {
		if (caster == ISpellCaster.INSTANT || caster == ISpellCaster.DELAYED) {
			casts--;
			cooldown = maxCooldown;
		}
	}

	public void tick() {
		if (cooldown > 0) {
			cooldown--;
			if (cooldown <= 0) {
				casts++;
				if (casts < maxCasts)
					cooldown = maxCooldown;
			}
		}
	}

	public Spell getSpell() {
		return spell;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int value) {
		level = value;
	}

	public int getMaxCasts() {
		return maxCasts;
	}

	public void setMaxCasts(int value) {
		maxCasts = value;
	}

	public int getCasts() {
		return casts;
	}

	public void setCasts(int value) {
		casts = value;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int value) {
		cooldown = value;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int value) {
		duration = value;
	}

	public CompoundNBT toNBT() {
		final CompoundNBT nbt = new CompoundNBT();

		nbt.putString("spell", spell.getId().toString());
		nbt.putInt("level", level);
		nbt.putInt("maxCasts", maxCasts);
		nbt.putInt("casts", casts);
		nbt.putInt("cooldown", cooldown);

		return nbt;
	}

	public static SpellInstance fromNBT(CompoundNBT nbt) {
		final ResourceLocation id = new ResourceLocation(nbt.getString("spell"));
		final SpellInstance instance = new SpellInstance(Spell.find(id));

		instance.setLevel(nbt.getInt("level"));
		instance.setMaxCasts(nbt.getInt("maxCasts"));
		instance.setCasts(nbt.getInt("casts"));
		instance.setCooldown(nbt.getInt("cooldown"));

		return instance;
	}
}