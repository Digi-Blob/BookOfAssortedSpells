package com.red_x_tornado.assortedspells.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.AbstractGui;

public class GuiUtil extends AbstractGui {

	private static final GuiUtil INSTANCE = new GuiUtil();

	public static void hfill(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
		INSTANCE.hLine(matrixStack, x1, x2, y1, color);
		INSTANCE.hLine(matrixStack, x1, x2, y2, color);
		INSTANCE.vLine(matrixStack, x1, y1, y2, color);
		INSTANCE.vLine(matrixStack, x2, y1, y2, color);
	}
}