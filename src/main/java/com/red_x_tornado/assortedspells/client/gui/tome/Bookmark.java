package com.red_x_tornado.assortedspells.client.gui.tome;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.client.gui.TomeScreen;
import com.red_x_tornado.assortedspells.util.spell.Spell;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class Bookmark implements Comparable<Bookmark> {

	private int x;
	private int y;
	public final int width;
	public final int height;

	private ITextComponent name;
	private SpellPage page;
	private FontRenderer font;
	private TomeScreen screen;

	public Bookmark(int x, int y, SpellPage page, FontRenderer font, TomeScreen screen) {
		this.x = x;
		this.y = y;
		this.page = page;
		this.font = font;
		this.screen = screen;

		name = new TranslationTextComponent(page.getSpell().getSpell().getLangKey());
		width = font.getStringPropertyWidth(name);
		height = font.FONT_HEIGHT;
	}

	public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
		final boolean sel = inBounds(mouseX, mouseY);
		final int color = sel ? 0x777777 : 0x000000;
		font.drawText(matrixStack, name, x, y, color);
	}

	public boolean inBounds(int mouseX, int mouseY) {
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}

	public boolean mouseClicked(double mouseX, double mouseY) {
		if (inBounds((int) mouseX, (int) mouseY)) {
			screen.switchToPage(page);
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return page.getSpell().getSpell().getId().toString();
	}

	@Override
	public int compareTo(Bookmark o) {
		if (o == null) return 1;
		if (o == this) return 0;

		return page.getSpell().getSpell().getId().getPath().compareTo(o.page.getSpell().getSpell().getId().getPath());
	}
}