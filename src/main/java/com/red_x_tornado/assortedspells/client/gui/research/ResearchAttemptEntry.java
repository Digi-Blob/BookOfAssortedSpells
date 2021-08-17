package com.red_x_tornado.assortedspells.client.gui.research;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.red_x_tornado.assortedspells.client.gui.ResearchTableScreen;
import com.red_x_tornado.assortedspells.item.RuneItem;
import com.red_x_tornado.assortedspells.util.research.MatchType;
import com.red_x_tornado.assortedspells.util.research.ResearchAttempt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.item.ItemStack;

public class ResearchAttemptEntry extends ScrollableList.Element {

	private final ResearchAttempt attempt;

	public ResearchAttemptEntry(ResearchAttempt attempt) {
		this.attempt = attempt;
	}

	@SuppressWarnings("deprecation") // RenderSystem stuff; we can't avoid it because ItemRenderer doesn't use the MatrixStack.
	@Override
	public void render(MatrixStack matrixStack, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, float partialTicks) {
		Minecraft.getInstance().getTextureManager().bindTexture(ResearchTableScreen.BACKGROUND);
		if (attempt.runes().length != 7)
			x += (width - 16 * attempt.runes().length) / 2;

		RenderSystem.pushMatrix();
		switch (attempt.runes().length) {
		case 7:
			RenderSystem.translatef(x, y, 0);
			RenderSystem.scalef(0.8F, 0.8F, 1F);
			RenderSystem.translatef(-x, -y, 0);
			break;
		default: break;
		}

		for (int i = 0; i < attempt.runes().length; i++) {
			final ResearchAttempt.RunePair pair = attempt.runes()[i];

			final int v = 192;
			final int u;
			if (pair.match() == MatchType.FAIL)
				u = 19;
			else if (pair.match() == MatchType.EXACT)
				u = 1;
			else u = 10;

			AbstractGui.blit(matrixStack, x + i * 16 + 4, y + 16, u, v, 8, 8, 288, 256);
		}

		for (int i = 0; i < attempt.runes().length; i++) {
			final ResearchAttempt.RunePair pair = attempt.runes()[i];
			final ItemStack stack = new ItemStack(RuneItem.fromSpellType(pair.rune()));
			Minecraft.getInstance().getItemRenderer()
			.renderItemAndEffectIntoGUI(stack, x + i * 16, y);
		}

		RenderSystem.popMatrix();
	}
}