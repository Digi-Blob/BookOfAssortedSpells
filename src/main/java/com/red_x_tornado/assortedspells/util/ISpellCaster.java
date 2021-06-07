package com.red_x_tornado.assortedspells.util;

import com.red_x_tornado.assortedspells.capability.SpellCapability;

import net.minecraft.item.ItemStack;

public interface ISpellCaster {

	public static ISpellCaster INSTANT = new ISpellCaster() {
		@Override
		public boolean begin(SpellCapability caps, CastContext ctx) {
			if (caps.getPlayer().getEntityWorld().isRemote) {
				for (int i = 0; i < ctx.getMaxTicks(); i++)
					ctx.getSpell().getSpell().doBeamEffects(caps, ctx, ctx.getNextPos(i, 0F));
			}

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
				ctx.getSpell().getSpell().doBeamEffects(caps, ctx, ctx.getNextPos(ctx.getTicks(), 0F));

			if (ctx.getTicks() == ctx.getMaxTicks()) {
				ctx.getSpell().getSpell().cast(caps, ctx);
				return false;
			}

			return true;
		}
	};

	public boolean begin(SpellCapability caps, CastContext ctx);
	public boolean tick(SpellCapability caps, CastContext ctx);
}