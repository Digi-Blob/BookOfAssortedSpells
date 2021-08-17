package com.red_x_tornado.assortedspells.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.client.gui.research.ResearchAttemptEntry;
import com.red_x_tornado.assortedspells.client.gui.research.ScrollableList;
import com.red_x_tornado.assortedspells.client.gui.research.SpellResearchEntry;
import com.red_x_tornado.assortedspells.network.ASNetworkManager;
import com.red_x_tornado.assortedspells.network.msg.research.ResearchSelectionMessage;
import com.red_x_tornado.assortedspells.tileentity.container.ResearchTableContainer;
import com.red_x_tornado.assortedspells.tileentity.container.WandBuilderContainer;
import com.red_x_tornado.assortedspells.util.research.MatchType;
import com.red_x_tornado.assortedspells.util.research.ResearchAttempt;
import com.red_x_tornado.assortedspells.util.research.ResearchInstance;
import com.red_x_tornado.assortedspells.util.spell.Spell;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ResearchTableScreen extends ContainerScreen<ResearchTableContainer> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation(BookOfAssortedSpells.MOD_ID, "textures/gui/research_table.png");

	private final SpellCapability caps;

	private ScrollableList<SpellResearchEntry> leftPane;
	private ScrollableList<ResearchAttemptEntry> rightPane;

	public ResearchTableScreen(ResearchTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.caps = SpellCapability.get(inv.player);
		//xSize = 287;
		ySize = 182;
		playerInventoryTitleY = ySize - 95;
	}

	@Override
	protected void init() {
		super.init();
		leftPane = new ScrollableList<>(minecraft, guiLeft - 66, guiTop + 19, 52, 156, 16 + 8 + 2, guiLeft - 13);
		rightPane = new ScrollableList<>(minecraft, guiLeft + 183, guiTop + 19, 52 + 38, 156, 16 + 8 + 2, guiLeft + 236 + 38);
		addListener(leftPane);
		addListener(rightPane);
		populateResearch();
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (leftPane.isMouseOver(mouseX, mouseY))
			return leftPane.mouseDragged(mouseX, mouseY, button, dragX, dragY);
		else if (rightPane.isMouseOver(mouseX, mouseY))
			return rightPane.mouseDragged(mouseX, mouseY, button, dragX, dragY);

		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	public void selectResearch(@Nullable Spell s) {
		if (container.selected != null && container.selected.getResearch().getSpell() == s) return;
		container.onSelectionChange(s == null ? null : caps.getResearch().get(s));
		ASNetworkManager.get().sendToServer(new ResearchSelectionMessage(container.selected.getResearch().getSpell()));
		refreshResearchAttempts();
	}

	public void refreshResearchAttempts() {
		rightPane.getElements().clear();
		if (container.selected != null)
			for (ResearchAttempt attempt : container.selected.getAttempts())
				rightPane.getElements().add(new ResearchAttemptEntry(attempt));
	}

	public void populateResearch() {
		final List<ResearchInstance> research = new ArrayList<>(caps.getResearch().values());
		Collections.sort(research);
		leftPane.getElements().clear();
		for (ResearchInstance res : research)
			leftPane.getElements().add(new SpellResearchEntry(this, res.getResearch().getSpell()));
	}

	protected void drawAttempt(MatrixStack matrixStack) {
		if (container.selected == null) return;

		final SpellType[] runes = container.getRuneSpellTypes();
		if (runes == null) return;
		final ResearchAttempt attempt = container.selected.find(runes);
		if (attempt == null) return;

		Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);

		int i = 0;

		for (Slot slot : container.runeSlots) {
			if (slot.isEnabled()) {
				final ResearchAttempt.RunePair pair = attempt.runes()[i++];

				final int v = 192;
				final int u;
				if (pair.match() == MatchType.FAIL)
					u = 19;
				else if (pair.match() == MatchType.EXACT)
					u = 1;
				else u = 10;

				blit(matrixStack,
						guiLeft + slot.xPos + 4, guiTop + slot.yPos + 20,
						u, v,
						8, 8, 288, 256);
			}
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		try {
			renderBackground(matrixStack);

			super.render(matrixStack, mouseX, mouseY, partialTicks);

			matrixStack.push();
			matrixStack.translate(0, 0, 0);

			leftPane.render(matrixStack, mouseX, mouseY, partialTicks);
			rightPane.render(matrixStack, mouseX, mouseY, partialTicks);

			matrixStack.pop();

			if (container.selected != null) {
				font.drawText(matrixStack, new TranslationTextComponent(container.selected.getResearch().getSpell().getLangKey()),
						guiLeft + 10, guiTop + 20, 0x000000);
			}

			drawAttempt(matrixStack);

			renderHoveredTooltip(matrixStack, mouseX, mouseY);
		} catch (Exception e) {
			BookOfAssortedSpells.LOGGER.error("Something questionable occurred:", e);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		minecraft.getTextureManager().bindTexture(BACKGROUND);
		// Background
		blit(matrixStack, guiLeft, guiTop, 0, 0, 176, 182, 288, 256);
		// Right Pane
		blit(matrixStack, guiLeft + 176, guiTop, 176, 0, 73 + 38, 182, 288, 256);
		// Left Pane
		blit(matrixStack, guiLeft - 73, guiTop, 176, 0, 73 - 14, 182, 288, 256);
		blit(matrixStack, guiLeft - 14, guiTop, 273, 0, 14, 182, 288, 256);

		// Draw rune slots.
		for (Slot slot : container.runeSlots) {
			if (slot.isEnabled())
				blit(matrixStack,
						guiLeft + slot.xPos - 1, guiTop + slot.yPos - 1,
						4, 208,
						18, 18, 288, 256);
		}
	}
}