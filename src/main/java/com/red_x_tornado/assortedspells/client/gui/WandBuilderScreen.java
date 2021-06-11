package com.red_x_tornado.assortedspells.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.tileentity.container.WandBuilderContainer;

import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

public class WandBuilderScreen extends ContainerScreen<WandBuilderContainer> {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(BookOfAssortedSpells.MOD_ID, "textures/gui/wand_builder.png");

	public WandBuilderScreen(WandBuilderContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		ySize = 169;
		playerInventoryTitleY = ySize - 92;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		minecraft.getTextureManager().bindTexture(BACKGROUND);
		// Draw player inventory portion.
		blit(matrixStack, guiLeft, guiTop + 86, 0, 86, 176, 83, 256, 256);
		if (container.showingPartTab) {
			blit(matrixStack, guiLeft, guiTop, 0, 170, 176, 86, 256, 256);
			drawPartTabSlots(matrixStack);
		} else
			blit(matrixStack, guiLeft, guiTop, 0, 0, 176, 86, 256, 256);

		drawTabs(matrixStack);
	}

	protected void drawPartTabSlots(MatrixStack matrixStack) {
		if (container.selectedPart == 0) {
			blit(matrixStack, guiLeft + 50, guiTop + 15, 178, 63, 20, 20, 256, 256); // Background
			blit(matrixStack, guiLeft + 52, guiTop + 17, 177, 125, 16, 16, 256, 256); // Part
			blit(matrixStack, guiLeft + 124, guiTop + 40, 177, 109, 16, 16, 256, 256);
		} else
			blit(matrixStack, guiLeft + 52, guiTop + 17, 177, 109, 16, 16, 256, 256); // Part
		if (container.selectedPart == 1) {
			blit(matrixStack, guiLeft + 73, guiTop + 15, 178, 63, 20, 20, 256, 256); // Background
			blit(matrixStack, guiLeft + 75, guiTop + 17, 193, 125, 16, 16, 256, 256); // Part
			blit(matrixStack, guiLeft + 124, guiTop + 40, 193, 109, 16, 16, 256, 256);
		} else
			blit(matrixStack, guiLeft + 75, guiTop + 17, 193, 109, 16, 16, 256, 256); // Part
		if (container.selectedPart == 2) {
			blit(matrixStack, guiLeft + 96, guiTop + 15, 178, 63, 20, 20, 256, 256); // Background
			blit(matrixStack, guiLeft + 98, guiTop + 17, 209, 125, 16, 16, 256, 256); // Part
			blit(matrixStack, guiLeft + 124, guiTop + 40, 209, 109, 16, 16, 256, 256);
		} else
			blit(matrixStack, guiLeft + 98, guiTop + 17, 209, 109, 16, 16, 256, 256); // Part
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		super.mouseClicked(mouseX, mouseY, button);

		//if (button == 1)
		if (!switchTabs(mouseX, mouseY) && container.showingPartTab)
			switchParts(mouseX, mouseY);

		return true;
	}

	protected boolean switchTabs(double mouseX, double mouseY) {
		if (!container.showingPartTab) {
			if (mouseX > guiLeft - 30 && mouseX < guiLeft && mouseY > guiTop + 39 && mouseY < guiTop + 65) {
				container.showingPartTab = true;
				playClickSound();
				return true;
			}
		} else if (mouseX > guiLeft - 30 && mouseX < guiLeft && mouseY > guiTop + 10 && mouseY < guiTop + 36) {
			container.showingPartTab = false;
			playClickSound();
			return true;
		}

		return false;
	}

	protected boolean switchParts(double mouseX, double mouseY) {
		final int sel = container.selectedPart;

		if (sel != 0 && mouseX >= guiLeft + 50 && mouseX <= guiLeft + 69 && mouseY >= guiTop + 15 && mouseY <= guiTop + 34) {
			container.setSelectedPart((byte) 0);
			playClickSound();
			return true;
		}

		if (sel != 1 && mouseX >= guiLeft + 73 && mouseX <= guiLeft + 92 && mouseY >= guiTop + 15 && mouseY <= guiTop + 34) {
			container.setSelectedPart((byte) 1);
			playClickSound();
			return true;
		}

		if (sel != 2 && mouseX >= guiLeft + 96 && mouseX <= guiLeft + 115 && mouseY >= guiTop + 15 && mouseY <= guiTop + 34) {
			container.setSelectedPart((byte) 2);
			playClickSound();
			return true;
		}

		return false;
	}

	protected void drawTabs(MatrixStack matrixStack) {
		if (container.showingPartTab) {
			blit(matrixStack, guiLeft - 34, guiTop + 10, 181, 203, 35, 26, 256, 256);
			blit(matrixStack, guiLeft - 32, guiTop + 39, 181, 175, 35, 26, 256, 256);
		} else {
			blit(matrixStack, guiLeft - 32, guiTop + 10, 181, 175, 35, 26, 256, 256);
			blit(matrixStack, guiLeft - 34, guiTop + 39, 181, 203, 35, 26, 256, 256);
		}

		blit(matrixStack, guiLeft - 20, guiTop + 15, 177, 89, 16, 16, 256, 256);
		blit(matrixStack, guiLeft - 20, guiTop + 43, 193, 89, 16, 16, 256, 256);
	}

	protected void playClickSound() {
		minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1F));
	}

	private void hfill(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
		hLine(matrixStack, x1, x2, y1, color);
		hLine(matrixStack, x1, x2, y2, color);
		vLine(matrixStack, x1, y1, y2, color);
		vLine(matrixStack, x2, y1, y2, color);
	}
}