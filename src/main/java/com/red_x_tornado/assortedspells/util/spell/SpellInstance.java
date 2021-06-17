package com.red_x_tornado.assortedspells.util.spell;

import java.util.Objects;

import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.util.cast.ISpellCaster;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * Like {@link ItemStack ItemStacks}, we have extra data to track that cannot affect all of a type of spell.<br>
 * This includes the {@linkplain #getLevel() level}, the {@linkplain #getMaxCasts() max casts}, the {@linkplain #getMaxCooldown() cooldown},
 * and other stuff.
 */
public class SpellInstance implements Comparable<SpellInstance> {

	private final Spell spell;
	private int level = 1;
	private int maxCasts = 1;
	private int casts = 1;
	private int cooldown = 0;
	private int maxCooldown;
	private int duration;

	public SpellInstance(Spell spell) {
		Objects.requireNonNull(spell, "Spell cannot be null!");
		this.spell = spell;
		duration = spell.getBaseDuration();
		maxCooldown = spell.getBaseCooldown();
	}

	/**
	 * Whether the player can cast the spell represented by this {@link SpellInstance}.
	 * @param caps The caster's capability.
	 * @param wand The wand {@link ItemStack}.
	 * @return {@code true} if the player can cast this spell.
	 */
	public boolean canCast(SpellCapability caps, ItemStack wand) {
		if (cooldown > 0 || casts == 0) return false;

		return true;
	}

	/**
	 * Called upon cast to handle cooldowns and such.
	 * @param caps The caster's capability.
	 * @param caster The casting handler.
	 */
	public void applyCast(SpellCapability caps, ISpellCaster caster) {
		if (caster == ISpellCaster.INSTANT || caster == ISpellCaster.DELAYED) {
			casts--;
			cooldown = maxCooldown;
		}
	}

	/**
	 * Called once every tick to process the cooldown.
	 */
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

	public int getMaxCooldown() {
		return maxCooldown;
	}

	public void setMaxCooldown(int value) {
		maxCooldown = value;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int value) {
		duration = value;
	}

	@Override
	public int compareTo(SpellInstance o) {
		if (o == null) return 1;
		if (o == this) return 0;

		return getSpell().compareTo(o.getSpell());
	}

	public void write(PacketBuffer buf) {
		buf.writeResourceLocation(spell.getId());
		buf.writeInt(level);
		buf.writeInt(maxCasts);
		buf.writeInt(maxCooldown);
		buf.writeInt(duration);
	}

	public static SpellInstance read(PacketBuffer buf) {
		final ResourceLocation id = buf.readResourceLocation();
		final SpellInstance spell = new SpellInstance(Spell.find(id));

		spell.setLevel(buf.readInt());
		spell.setMaxCasts(buf.readInt());
		spell.setMaxCooldown(buf.readInt());
		spell.setDuration(buf.readInt());

		return spell;
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
		final Spell spell = Spell.find(id);
		if (spell == null) throw new IllegalArgumentException("Failed to find spell with id " + id + "!");
		final SpellInstance instance = new SpellInstance(spell);

		instance.setLevel(nbt.getInt("level"));
		instance.setMaxCasts(nbt.getInt("maxCasts"));
		instance.setCasts(nbt.getInt("casts"));
		instance.setCooldown(nbt.getInt("cooldown"));

		return instance;
	}
}