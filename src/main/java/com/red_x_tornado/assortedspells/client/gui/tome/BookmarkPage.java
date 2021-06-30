package com.red_x_tornado.assortedspells.client.gui.tome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.util.spell.Spell;

public class BookmarkPage extends Page {

	private final List<Bookmark> bookmarks = new ArrayList<>();
	private boolean firstInit = false;

	public BookmarkPage() {}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
		if (!firstInit) {
			refreshBookmarks();
			firstInit = true;
		}

		final int maxWidth = 146 - 15 - 16;
		font.drawString(matrixStack, "Bookmarks", x + (maxWidth - font.getStringWidth("Bookmarks")) / 2, y, 0x000000);

		for (Bookmark bookmark : bookmarks)
			bookmark.render(matrixStack, mouseX, mouseY);

		minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Bookmark bookmark : bookmarks)
			if (bookmark.mouseClicked(mouseX, mouseY))
				return true;

		return super.mouseClicked(mouseX, mouseY, button);
	}

	public void refreshBookmarks() {
		bookmarks.clear();

		int y = this.y + font.FONT_HEIGHT * 2;
		for (Spell spell : caps.getBookmarks()) {
			final Bookmark bookmark = new Bookmark(x, y, screen.findSpellPage(spell), font, screen);
			bookmarks.add(bookmark);
			y += bookmark.height;
		}

		Collections.sort(bookmarks);
	}
}