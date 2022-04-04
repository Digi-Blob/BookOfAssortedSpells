package com.red_x_tornado.assortedspells.util.cast;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.util.spell.SpellInstance;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

public class CastContext {

	private final SpellInstance spell;
	private final ISpellCaster caster;

	private final Vector3d start;
	private final Vector3d target;
	private final Vector3d lookDirection;
	@Nullable
	private final Direction hitFace;
	@Nullable
	private final Entity targetEntity;

	private final ItemStack wandStack;

	private final double duration;

	private int ticks = 0;
	private Vector3d pos;

	public CastContext(SpellInstance spell, ISpellCaster caster, Vector3d start, Vector3d target, Vector3d lookDirection, @Nullable Direction hitFace, @Nullable Entity targetEntity, ItemStack wandStack) {
		this.spell = spell;
		this.caster = caster;
		this.start = start;
		this.target = target;
		this.lookDirection = lookDirection;
		this.hitFace = hitFace;
		this.targetEntity = targetEntity;
		this.wandStack = wandStack;
		pos = start;
		duration = start.distanceTo(target) / spell.getSpell().getMaxDistance() * spell.getDuration();
	}

	public SpellInstance getSpell() {
		return spell;
	}

	public ISpellCaster getCaster() {
		return caster;
	}

	public Vector3d getTarget() {
		return target;
	}

	public Vector3d getDirection() {
		return lookDirection;
	}

	@Nullable
	public Entity getTargetEntity() {
		return targetEntity;
	}

	public ItemStack getWandStack() {
		return wandStack;
	}

	protected Vector3d interpolatePos(int ticks) {
		final double percent = ticks / duration;
		final double x = MathHelper.lerp(percent, start.x, target.x);
		final double y = MathHelper.lerp(percent, start.y, target.y);
		final double z = MathHelper.lerp(percent, start.z, target.z);
		return new Vector3d(x, y, z);
	}

	public <T extends Entity> T moveToTarget(T entityIn) {
		final Vector3d result = correct(entityIn, target, hitFace);

		entityIn.moveForced(result);

		return entityIn;
	}

	public static Vector3d correct(Entity entityIn, Vector3d pos, @Nullable Direction hitFace) {
		if (entityIn == null) throw new NullPointerException("entityIn is null!");
		if (hitFace == null) return pos;

		final AxisAlignedBB bounds = entityIn.getBoundingBox();
		final Vector3i dir = hitFace.getDirectionVec();

		double x = pos.x;
		double y = pos.y;
		double z = pos.z;

		x += dir.getX() * (bounds.getXSize() / 2);
		if (hitFace != Direction.UP)
			y += dir.getY() * bounds.getYSize();
		z += dir.getZ() * (bounds.getZSize() / 2);

		return new Vector3d(x, y, z);
	}

	public double getDuration() {
		return duration;
	}

	public int getMaxTicks() {
		return MathHelper.ceil(duration);
	}

	public Direction getHitFace() {
		return hitFace;
	}

	public Vector3d getPos() {
		return pos;
	}

	public void setPos(Vector3d value) {
		this.pos = value;
	}

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int value) {
		ticks = value;
	}
}