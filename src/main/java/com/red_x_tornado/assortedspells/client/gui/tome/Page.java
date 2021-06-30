package com.red_x_tornado.assortedspells.client.gui.tome;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.client.gui.TomeScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class Page extends AbstractGui implements IGuiEventListener {

	protected static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(BookOfAssortedSpells.MOD_ID, "textures/gui/tome.png");

	protected static final int TEX_X = 384;
	protected static final int TEX_Y = 256;

	protected Minecraft minecraft;
	protected FontRenderer font;
	protected SpellCapability caps;
	protected TomeScreen screen;

	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public void init(Minecraft minecraft, FontRenderer font, SpellCapability caps, TomeScreen screen) {
		this.minecraft = minecraft;
		this.font = font;
		this.caps = caps;
		this.screen = screen;
	}

	public void setPos(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean inBounds(double mouseX, double mouseY) {
		if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)
			return true;

		return false;
	}

	public int getTypeBookmarkOffset(boolean left) {
		return 0;
	}

	public TranslationTextComponent trans(String key, Object... args) {
		return new TranslationTextComponent(key, args);
	}

	public void playClickSound() {
		minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1F));
	}

	public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY);
}