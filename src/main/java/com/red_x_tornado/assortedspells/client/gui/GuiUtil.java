package com.red_x_tornado.assortedspells.client.gui;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;

public class GuiUtil extends AbstractGui {

	private static final GuiUtil INSTANCE = new GuiUtil();

	public static void hfill(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int z, int color) {
		INSTANCE.setBlitOffset(z);
		INSTANCE.hLine(matrixStack, x1, x2, y1, color);
		INSTANCE.hLine(matrixStack, x1, x2, y2, color);
		INSTANCE.vLine(matrixStack, x1, y1, y2, color);
		INSTANCE.vLine(matrixStack, x2, y1, y2, color);
	}

	public static void drawSplitText(FontRenderer font, MatrixStack matrixStack, ITextComponent text, int x, int y, int maxWidth, int maxHeight, int color, boolean centerHorizontal, boolean centerVertical) {
		final List<IReorderingProcessor> name = font.trimStringToWidth(text, maxWidth);

		if (centerVertical)
			y += maxHeight / 2 - (name.size() * font.FONT_HEIGHT) / 2;

		int yOffset = 0;

		for (IReorderingProcessor proc : name) {
			final int textX = centerHorizontal ? x + maxWidth / 2 - font.func_243245_a(proc) / 2 : x;

			font.func_238422_b_(matrixStack, proc, textX, y + yOffset, color);

			yOffset += font.FONT_HEIGHT;
		}
	}
}