package com.red_x_tornado.assortedspells.util.cast;

import com.red_x_tornado.assortedspells.capability.SpellCapability;

/**
 * Defines a casting handler for a spell.<br>
 * These handlers control when exactly a spell's effects should happen, and how.
 */
public interface ISpellCaster {

	public static ISpellCaster INSTANT = new ISpellCaster() {
		@Override
		public boolean begin(SpellCapability caps, CastContext ctx) {
			if (caps.getPlayer().getEntityWorld().isRemote)
				for (int i = 0; i < ctx.getMaxTicks(); i++)
					ctx.getSpell().getSpell().doBeamEffects(caps, ctx, ctx.interpolatePos(i));

			ctx.getSpell().getSpell().cast(caps, ctx);

			return false;
		}
		@Override
		public boolean tick(SpellCapability caps, CastContext ctx) { return false; }
	};

	public static ISpellCaster DELAYED = new ISpellCaster() {
		@Override
		public boolean begin(SpellCapability caps, CastContext ctx) {
			return true;
		}
		@Override
		public boolean tick(SpellCapability caps, CastContext ctx) {
			ctx.setTicks(ctx.getTicks() + 1);

			if (caps.getPlayer().getEntityWorld().isRemote)
				ctx.getSpell().getSpell().doBeamEffects(caps, ctx, ctx.interpolatePos(ctx.getTicks()));

			if (ctx.getTicks() == ctx.getMaxTicks()) {
				ctx.getSpell().getSpell().cast(caps, ctx);
				return false;
			}

			return true;
		}
	};

	/**
	 * Called when the spell is initially cast.
	 * @param caps The caster's capability.
	 * @param ctx The cast context.
	 * @return {@code true} if {@link #tick(SpellCapability, CastContext) tick()} should be called next tick.
	 */
	public boolean begin(SpellCapability caps, CastContext ctx);

	/**
	 * Called every tick for as long as this returns {@code true}.
	 * @param caps The caster's capability.
	 * @param ctx The cast context.
	 * @return {@code true} if this should be called again next tick, or {@code false} if the spell has been performed.
	 */
	public boolean tick(SpellCapability caps, CastContext ctx);
}