package com.red_x_tornado.assortedspells.capability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.red_x_tornado.assortedspells.util.CastContext;
import com.red_x_tornado.assortedspells.util.ISpellCaster;
import com.red_x_tornado.assortedspells.util.Spell;
import com.red_x_tornado.assortedspells.util.SpellInstance;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.Constants;

public class SpellCapability {

	private final PlayerEntity player;

	private final List<SpellInstance> knownSpells = new ArrayList<>();
	private final List<SpellInstance> quickSpells = new ArrayList<>();

	private List<Pair<CastContext,ISpellCaster>> casters = new ArrayList<>(1);

	@Nullable
	private SpellInstance selected;

	public SpellCapability(PlayerEntity player) {
		this.player = player;
		// Add six dummy spells to the quick spell list.
		for (int i = 0; i < 6; i++)
			quickSpells.add(null);
	}

	public static SpellCapability get(PlayerEntity player) {
		return player.getCapability(SpellCapabilityProvider.spellCapability).orElseThrow(() -> new IllegalStateException("All players should have spell capabilities!"));
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public SpellInstance getSelected() {
		return new SpellInstance(Spell.LIGHTNING);
	}

	public void castSelected(ItemStack stack) {
		cast(getSelected(), stack);
	}

	protected void cast(SpellInstance spell, ItemStack stack) {
		final ISpellCaster caster = spell.getSpell().getCaster();

		final RayTraceResult ray = player.pick(spell.getSpell().getMaxDistance(), 0F, false);
		final Entity targetEntity = ray instanceof EntityRayTraceResult ? ((EntityRayTraceResult) ray).getEntity() : null;

		final CastContext ctx = new CastContext(spell, player.getEyePosition(1F), ray.getHitVec(), targetEntity);

		if (caster.begin(this, ctx))
			casters.add(Pair.of(ctx, caster));

		spell.applyCast(this, caster);
	}

	public void tick() {
		for (SpellInstance spell : knownSpells)
			spell.tick();

		final Iterator<Pair<CastContext,ISpellCaster>> it = casters.iterator();
		while (it.hasNext()) {
			final Pair<CastContext,ISpellCaster> pair = it.next();
			if (!pair.getRight().tick(this, pair.getLeft()))
				it.remove();
		}
	}

	public void copyFrom(SpellCapability caps) {
		knownSpells.clear();
		knownSpells.addAll(caps.knownSpells);
		quickSpells.clear();
		quickSpells.addAll(caps.quickSpells);
		selected = caps.getSelected();
	}

	public CompoundNBT write() {
		final CompoundNBT nbt = new CompoundNBT();

		final ListNBT known = new ListNBT();

		for (SpellInstance spell : knownSpells)
			known.add(spell.toNBT());

		nbt.put("knownSpells", known);
		nbt.putIntArray("quickSpells", quickSpells.stream().mapToInt(knownSpells::indexOf).toArray());
		if (getSelected() != null)
			nbt.putInt("selected", knownSpells.indexOf(getSelected()));

		return nbt;
	}

	public void read(CompoundNBT nbt) {
		knownSpells.clear();
		quickSpells.clear();

		final ListNBT known = nbt.getList("knownSpells", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < known.size(); i++)
			knownSpells.add(SpellInstance.fromNBT(known.getCompound(i)));

		quickSpells.addAll(Arrays.stream(nbt.getIntArray("quickSpells")).mapToObj(knownSpells::get).collect(Collectors.toList()));
		selected = nbt.contains("selected", Constants.NBT.TAG_INT) ? knownSpells.get(nbt.getInt("selected")) : null;
	}
}