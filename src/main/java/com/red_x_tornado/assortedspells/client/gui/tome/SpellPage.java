package com.red_x_tornado.assortedspells.client.gui.tome;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.util.cast.ISpellCaster;
import com.red_x_tornado.assortedspells.util.spell.SpellInstance;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SpellPage extends Page {

	private final SpellInstance spell;

	public SpellPage(SpellInstance spell) {
		this.spell = spell;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
		final int maxWidth = 146 - 15 - 16;
		final int color = 0x000000;

		drawDropdown(matrixStack, mouseX, mouseY);

		final TranslationTextComponent name = trans(spell.getSpell().getLangKey());
		final ITextComponent clazz = trans("assortedspells.gui.spell.class", trans(spell.getSpell().getClazz().getLangKey()));
		final ITextComponent type = trans("assortedspells.gui.spell.type", trans(spell.getSpell().getType().getLangKey()));
		final ITextComponent castingStyle = trans("assortedspells.gui.spell.casting_style", trans("assortedspells.gui.spell.casting_style." + (spell.getSpell().getCaster() == ISpellCaster.DELAYED ? "delayed" : "instant")));
		final ITextComponent difficulty = trans("assortedspells.gui.spell.difficulty", trans(spell.getSpell().getBaseDifficulty().getLangKey()));
		final ITextComponent cooldown = trans("assortedspells.gui.spell.cooldown", ticksToSeconds(spell.getMaxCooldown()));
		final ITextComponent duration = trans("assortedspells.gui.spell.duration", ticksToSeconds(spell.getDuration()));
		final ITextComponent casts = trans("assortedspells.gui.spell.max_casts", spell.getMaxCasts());

		font.drawText(matrixStack, name, x + (maxWidth - font.getStringPropertyWidth(name)) / 2, y, color);

		final int ty = y + 12;

		font.drawText(matrixStack, clazz, x, ty, color);
		font.drawText(matrixStack, type, x, ty + 9 * 1, color);
		font.drawText(matrixStack, castingStyle, x, ty + 9 * 2, color);
		font.drawText(matrixStack, difficulty, x, ty + 9 * 3, color);
		font.drawText(matrixStack, cooldown, x, ty + 9 * 4, color);
		font.drawText(matrixStack, duration, x, ty + 9 * 5, color);
		font.drawText(matrixStack, casts, x, ty + 9 * 6, color);

		final TranslationTextComponent desc = new TranslationTextComponent(spell.getSpell().getDescLangKey());

		font.func_238418_a_(desc, x, ty + 9 * 8, maxWidth, color);

		minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
	}

	private String ticksToSeconds(int ticks) {
		final float seconds = ticks / 20F;
		final String base;
		if (seconds % 1 == 0) base = Integer.toString((int) seconds);
		else base = Float.toString(seconds);

		return base + " second" + (seconds == 1 ? "" : "s");
	}

	private void drawDropdown(MatrixStack matrixStack, int mouseX, int mouseY) {
		blit(matrixStack, x + width - 56, y - 2, 25, 209, 24, 24, TEX_X, TEX_Y);
	}
}