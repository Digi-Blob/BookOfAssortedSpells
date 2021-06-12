package com.red_x_tornado.assortedspells.capability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.red_x_tornado.assortedspells.util.cast.CastContext;
import com.red_x_tornado.assortedspells.util.cast.ISpellCaster;
import com.red_x_tornado.assortedspells.util.spell.Spell;
import com.red_x_tornado.assortedspells.util.spell.SpellInstance;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.Constants;

public class SpellCapability {

	public static final int MAX_QUICK_SPELLS = 6;

	private final PlayerEntity player;

	private final List<SpellInstance> knownSpells = new ArrayList<>();
	private final SpellInstance[] quickSpells = new SpellInstance[MAX_QUICK_SPELLS];

	private List<Pair<CastContext,ISpellCaster>> casters = new ArrayList<>(1);

	@Nullable
	private SpellInstance selected;

	public SpellCapability(PlayerEntity player) {
		this.player = player;
	}

	public static SpellCapability get(PlayerEntity player) {
		return player.getCapability(SpellCapabilityProvider.spellCapability)
				.orElseThrow(() -> new IllegalStateException("All players should have spell capabilities!"));
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public List<SpellInstance> getKnownSpells() {
		return knownSpells;
	}

	public void unlock(Spell spell) {
		knownSpells.add(new SpellInstance(spell));
	}

	public SpellInstance[] getQuickSpells() {
		return quickSpells;
	}

	public int quickSpellIndexFromSelection() {
		if (selected == null) return -1;

		for (int i = 0; i < quickSpells.length; i++)
			if (quickSpells[i] == selected)
				return i;

		return -1;
	}

	public boolean isKnown(Spell spell) {
		return getKnownSpells().stream().anyMatch(ins -> ins.getSpell().getId().equals(spell.getId()));
	}

	@Nullable
	public SpellInstance getSelected() {
		return selected;
	}

	public void select(@Nullable Spell spell) {
		selected = spell == null ? null : getKnownSpells().stream().filter(s -> s.getSpell() == spell).findFirst().orElse(null);
	}

	public void castSelected(ItemStack stack) {
		cast(getSelected(), stack);
	}

	protected void cast(SpellInstance spell, ItemStack stack) {
		final ISpellCaster caster = spell.getSpell().getCaster();

		final int distance = spell.getSpell().getMaxDistance();
		final Vector3d start = player.getEyePosition(0F);
		final Vector3d look = player.getLook(0F);
		Vector3d end = new Vector3d(start.x + look.x * distance, start.y + look.y * distance, start.z + look.z * distance);

		final RayTraceResult ray = player.pick(distance, 0F, false);
		final double maxDist = ray.getHitVec().distanceTo(start);
		end = new Vector3d(start.x + look.x * maxDist, start.y + look.y * maxDist, start.z + look.z * maxDist);

		final EntityRayTraceResult entityRay = spell.getSpell().prefersEntities() ? ProjectileHelper.rayTraceEntities(player.world, player, start, end, new AxisAlignedBB(start, end), null) : null;

		final Entity targetEntity = entityRay == null ? null : entityRay.getEntity();
		final Direction hitFace = ray instanceof BlockRayTraceResult ? ((BlockRayTraceResult) ray).getFace() : null;

		final Vector3d hit;

		if (targetEntity != null) {
			// We need to correct the ending position's height so that it doesn't always end at the entity's feet.
			final Vector3d newHit = targetEntity.getBoundingBox().rayTrace(start, end).orElse(null);
			hit = newHit == null ? entityRay.getHitVec() : newHit;
		} else hit = ray.getHitVec();

		final CastContext ctx = new CastContext(spell, start, hit, look, hitFace, targetEntity);

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
		for (int i = 0; i < MAX_QUICK_SPELLS; i++)
			quickSpells[i] = caps.quickSpells[i];
		selected = caps.getSelected();
	}

	public void write(PacketBuffer buf) {
		buf.writeInt(knownSpells.size());
		for (SpellInstance spell : knownSpells)
			spell.write(buf);

		for (int i = 0; i < MAX_QUICK_SPELLS; i++) {
			final SpellInstance spell = quickSpells[i];
			if (spell == null) buf.writeBoolean(false);
			else {
				buf.writeBoolean(true);
				buf.writeInt(knownSpells.indexOf(spell));
			}
		}

		buf.writeInt(selected == null ? -1 : knownSpells.indexOf(selected));
	}

	public void read(PacketBuffer buf) {
		final int count = buf.readInt();

		knownSpells.clear();
		for (int i = 0; i < count; i++)
			knownSpells.add(SpellInstance.read(buf));

		for (int i = 0; i < MAX_QUICK_SPELLS; i++)
			quickSpells[i] = buf.readBoolean() ? knownSpells.get(buf.readInt()) : null;

		final int idx = buf.readInt();
		selected = idx == -1 ? null : knownSpells.get(idx);
	}

	public CompoundNBT write() {
		final CompoundNBT nbt = new CompoundNBT();

		final ListNBT known = new ListNBT();

		for (SpellInstance spell : knownSpells)
			known.add(spell.toNBT());

		nbt.put("knownSpells", known);
		nbt.putIntArray("quickSpells", Arrays.stream(quickSpells).mapToInt(s -> s == null ? -1 : knownSpells.indexOf(s)).toArray());
		if (getSelected() != null)
			nbt.putInt("selected", knownSpells.indexOf(getSelected()));

		return nbt;
	}

	public void read(CompoundNBT nbt) {
		knownSpells.clear();

		final ListNBT known = nbt.getList("knownSpells", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < known.size(); i++)
			knownSpells.add(SpellInstance.fromNBT(known.getCompound(i)));

		final SpellInstance[] arr = Arrays.stream(nbt.getIntArray("quickSpells")).mapToObj(i -> i == -1 ? null : knownSpells.get(i)).toArray(SpellInstance[]::new);

		for (int i = 0; i < MAX_QUICK_SPELLS; i++)
			quickSpells[i] = arr[i];

		selected = nbt.contains("selected", Constants.NBT.TAG_INT) ? knownSpells.get(nbt.getInt("selected")) : null;
	}
}