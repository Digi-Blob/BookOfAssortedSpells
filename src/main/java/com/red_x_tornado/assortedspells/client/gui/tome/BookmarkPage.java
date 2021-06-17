package com.red_x_tornado.assortedspells.client.gui.tome;

import com.mojang.blaze3d.matrix.MatrixStack;

public class BookmarkPage extends Page {

	public BookmarkPage() {}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
		final int maxWidth = 146 - 15 - 16;
		font.drawString(matrixStack, "Bookmarks", x + (maxWidth - font.getStringWidth("Bookmarks")) / 2, y, 0x000000);
		minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
	}
}