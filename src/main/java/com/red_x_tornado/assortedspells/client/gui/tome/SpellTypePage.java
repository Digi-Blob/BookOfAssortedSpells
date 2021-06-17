package com.red_x_tornado.assortedspells.client.gui.tome;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

import net.minecraft.util.text.TranslationTextComponent;

public class SpellTypePage extends Page {

	public final SpellType type;

	public SpellTypePage(SpellType type) {
		this.type = type;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
		final int maxWidth = 146 - 15 - 16;

		// + maxWidth - 34
		blit(matrixStack, x + 4, y - 16 - 23, 256, 0, 24, 196, TEX_X, TEX_Y);
		blit(matrixStack, x + 4 + 4, y - 16 - 23 + 4, QuickspellPage.typeU(type), QuickspellPage.typeV(type, false), 16, 16, TEX_X, TEX_Y);

		final TranslationTextComponent name = new TranslationTextComponent(type.getLangKey());

		font.drawText(matrixStack, name,
				x + (maxWidth - font.getStringPropertyWidth(name)) / 2,
				y + 56, 0x000000);
		font.drawString(matrixStack, "Spells", x + (maxWidth - font.getStringWidth("Spells")) / 2, y + 64, 0x000000);
	}

	@Override
	public int getTypeBookmarkOffset(boolean left) {
		return left ? -32 : 32;
	}
}