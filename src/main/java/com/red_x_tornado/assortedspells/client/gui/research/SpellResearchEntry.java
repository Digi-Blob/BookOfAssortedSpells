package com.red_x_tornado.assortedspells.client.gui.research;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.client.gui.GuiUtil;
import com.red_x_tornado.assortedspells.client.gui.ResearchTableScreen;
import com.red_x_tornado.assortedspells.util.spell.Spell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.TranslationTextComponent;

public class SpellResearchEntry extends ScrollableList.Element {

	private final ResearchTableScreen parent;
	private final Spell spell;

	public SpellResearchEntry(ResearchTableScreen parent, Spell spell) {
		this.parent = parent;
		this.spell = spell;
	}

	@Override
	public void onClick(double mouseX, double mouseY, int button) {
		parent.selectResearch(spell);
	}

	@Override
	public void render(MatrixStack matrixStack, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, float partialTicks) {
		final FontRenderer font = Minecraft.getInstance().fontRenderer;

		fill(matrixStack, x, y, x + width, y + height - 1, 0xFFbbbbbb);
		GuiUtil.hfill(matrixStack, x, y, x + width - 1, y + height - 1, 1, hovered ? 0xFFFFFFFF : 0xFF333333);

		GuiUtil.drawSplitText(font, matrixStack,
				new TranslationTextComponent(spell.getLangKey()),
				x + 1, y, width - 2, height, 0x0000FF, true, true);
	}
}