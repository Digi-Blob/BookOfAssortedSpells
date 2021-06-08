package com.red_x_tornado.assortedspells.util;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class CastContext {

	private final SpellInstance spell;
	private final Vector3d start;
	private final Vector3d target;
	@Nullable
	private final Entity targetEntity;

	private final double duration;

	private int ticks = 0;
	private Vector3d pos;

	public CastContext(SpellInstance spell, Vector3d start, Vector3d target, @Nullable Entity targetEntity) {
		this.spell = spell;
		this.target = target;
		this.targetEntity = targetEntity;
		this.start = start;
		pos = start;
		duration = start.distanceTo(target) / spell.getSpell().getMaxDistance() * spell.getDuration();
	}

	public SpellInstance getSpell() {
		return spell;
	}

	public Vector3d getTarget() {
		return target;
	}

	@Nullable
	public Entity getTargetEntity() {
		return targetEntity;
	}

	protected Vector3d interpolatePos(int ticks) {
		final double percent = ticks / duration;
		final double x = MathHelper.lerp(percent, start.x, target.x);
		final double y = MathHelper.lerp(percent, start.y, target.y);
		final double z = MathHelper.lerp(percent, start.z, target.z);
		return new Vector3d(x, y, z);
	}

	public double getDuration() {
		return duration;
	}

	public int getMaxTicks() {
		return MathHelper.ceil(duration);
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