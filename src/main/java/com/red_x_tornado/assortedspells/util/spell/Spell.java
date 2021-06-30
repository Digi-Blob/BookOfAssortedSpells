package com.red_x_tornado.assortedspells.util.spell;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.util.cast.CastContext;
import com.red_x_tornado.assortedspells.util.cast.ISpellCaster;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Contains all the functionality and attributes regarding spells.
 */
public abstract class Spell implements Comparable<Spell> {

	private static final Map<ResourceLocation,Spell> ALL_SPELLS = new TreeMap<>();

	private final SpellClass clazz;
	private final SpellType type;
	private final SpellDifficulty difficulty;
	private final int baseCooldown;
	private final int baseDuration;
	private final int maxDistance;
	private final ISpellCaster caster;

	private final ResourceLocation id;
	private final String langKey;

	/**
	 * Constructs a {@link Spell}.
	 * @param clazz The spell class.
	 * @param type The spell type.
	 * @param difficulty The spell difficulty.
	 * @param baseCooldown How long (in ticks) the time after casting where you can't cast the spell should last.
	 * @param baseDuration How long a casting of the spell should last. For delayed spells this is how long it takes for it to reach its target.
	 * @param maxDistance The maximum distance the spell can travel.
	 * @param id The spell id.
	 * @param caster The casting handler for the spell. See the fields in {@link ISpellCaster}.
	 */
	public Spell(SpellClass clazz, SpellType type, SpellDifficulty difficulty, int baseCooldown, int baseDuration, int maxDistance, ResourceLocation id, ISpellCaster caster) {
		this.clazz = clazz;
		this.type = type;
		this.difficulty = difficulty;
		this.baseCooldown = baseCooldown;
		this.baseDuration = baseDuration;
		this.maxDistance = maxDistance;
		this.id = id;
		this.caster = caster;
		langKey = "spell." + id.getNamespace() + "." + id.getPath();
		ALL_SPELLS.put(id, this);
	}

	@Nullable
	public static Spell find(ResourceLocation id) {
		return ALL_SPELLS.get(id);
	}

	public static Set<ResourceLocation> getSpellIds() {
		return ALL_SPELLS.keySet();
	}

	public static Collection<Spell> getSpells() {
		return ALL_SPELLS.values();
	}

	public ISpellCaster getCaster() {
		return caster;
	}

	/**
	 * Called when the spell has been cast and it has reached its target.<br>
	 * This should handle any effects the spell might have.
	 * @param caps The caster's capability.
	 * @param ctx The cast context.
	 * @see #clientCast(SpellCapability, CastContext)
	 * @see #serverCast(SpellCapability, CastContext)
	 */
	public void cast(SpellCapability caps, CastContext ctx) {
		if (caps.getPlayer().getEntityWorld().isRemote)
			clientCast(caps, ctx);
		else serverCast(caps, ctx);
	}

	/**
	 * Called when the spell has been cast and it has reached its target.<br>
	 * This should handle server-side logic for the spell, like damaging an entity or applying an effect.
	 * @param caps The caster's capability.
	 * @param ctx The cast context.
	 */
	protected abstract void serverCast(SpellCapability caps, CastContext ctx);

	/**
	 * Called when the spell has been cast and it has reached its target.<br>
	 * This should handle client-side logic for the spell, like particle effects.
	 * @param caps The caster's capability.
	 * @param ctx The cast context.
	 */
	protected void clientCast(SpellCapability caps, CastContext ctx) {}

	/**
	 * Called on the client to perform any special effects on the beam.
	 * @param caps The caster's capability.
	 * @param ctx The cast context.
	 * @param pos The position to perform the effects at.
	 */
	public void doBeamEffects(SpellCapability caps, CastContext ctx, Vector3d pos) {}

	/**
	 * Whether this spell should prefer targeting entities over blocks.<br>
	 * Specifically, if you were to aim a spell at a creeper, this determines whether the spell will hit the creeper or go through it.
	 * @return {@code true} if entity targeting should be performed.
	 */
	public boolean prefersEntities() {
		return true;
	}

	public SpellClass getClazz() {
		return clazz;
	}

	public SpellType getType() {
		return type;
	}

	public SpellDifficulty getBaseDifficulty() {
		return difficulty;
	}

	public int getBaseCooldown() {
		return baseCooldown;
	}

	public int getBaseDuration() {
		return baseDuration;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public ResourceLocation getId() {
		return id;
	}

	public String getLangKey() {
		return langKey;
	}

	public String getDescLangKey() {
		return langKey + ".desc";
	}

	@Override
	public int compareTo(Spell o) {
		if (o == null) return 1;
		if (o == this) return 0;

		// Try sorting by type first.
		final int typeOrder = getType().compareTo(o.getType());
		if (typeOrder != 0) return typeOrder;

		// Otherwise try an alphabetical sort on the id.
		return getId().getPath().compareTo(o.getId().getPath());
	}
}