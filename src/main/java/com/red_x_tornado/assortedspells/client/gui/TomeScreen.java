package com.red_x_tornado.assortedspells.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.red_x_tornado.assortedspells.BookOfAssortedSpells;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class TomeScreen extends Screen {

	private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(BookOfAssortedSpells.MOD_ID, "textures/gui/tome.png");

	private static final int X_SIZE = 1;
	private static final int Y_SIZE = 1;

	private int screenX;
	private int screenY;

	public TomeScreen() {
		super(new StringTextComponent(""));
	}

	public static void open() {
		Minecraft.getInstance().displayGuiScreen(new TomeScreen());
	}

	@Override
	protected void init() {
		screenX = (width - X_SIZE) / 2;
		screenY = (height - Y_SIZE) / 2;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);

		//minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);

		for (int i = 0; i < 100; i++) {
			minecraft.getTextureManager().bindTexture(new ResourceLocation("minecraft", minecraft.world.rand.nextBoolean() ? "textures/item/diamond_sword.png" : "textures/item/diamond_pickaxe.png"));
			RenderSystem.color4f(minecraft.world.rand.nextFloat(), minecraft.world.rand.nextFloat(), minecraft.world.rand.nextFloat(), 1F);
			blit(matrixStack, minecraft.world.rand.nextInt(minecraft.getMainWindow().getScaledWidth()), minecraft.world.rand.nextInt(minecraft.getMainWindow().getScaledHeight()), 0, 0, 16, 16, 16, 16);
		}

		RenderSystem.color4f(1F, 1F, 1F, 1F);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean keyPressed(int scanCode, int keyCode, int modifiers) {
		if (super.keyPressed(scanCode, keyCode, modifiers))
			return true;

		if (scanCode == 256 || minecraft.gameSettings.keyBindInventory.isActiveAndMatches(InputMappings.getInputByCode(scanCode, keyCode))) {
			minecraft.displayGuiScreen(null);
			return true;
		}

		return false;
	}
}