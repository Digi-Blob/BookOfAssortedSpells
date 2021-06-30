package com.red_x_tornado.assortedspells.client.gui.tome;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.red_x_tornado.assortedspells.network.ASNetworkManager;
import com.red_x_tornado.assortedspells.network.msg.SpellSelectionMessage;
import com.red_x_tornado.assortedspells.util.spell.SpellInstance;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

import net.minecraft.util.text.TranslationTextComponent;

public class QuickspellPage extends Page {

	private static final int SLOT_1_X = 46;
	private static final int SLOT_1_Y = 4;
	private static final int SLOT_2_X = 89;
	private static final int SLOT_2_Y = 30;
	private static final int SLOT_3_X = 89;
	private static final int SLOT_3_Y = 79;
	private static final int SLOT_4_X = 46;
	private static final int SLOT_4_Y = 105;
	private static final int SLOT_5_X = 4;
	private static final int SLOT_5_Y = 79;
	private static final int SLOT_6_X = 4;
	private static final int SLOT_6_Y = 30;

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		blit(matrixStack, x, y, 146, 0, 109, 125, TEX_X, TEX_Y);

		final int sel = getSelectedSpell(mouseX, mouseY);

		drawSpell(matrixStack, x + SLOT_1_X, y + SLOT_1_Y, caps.getQuickSpells()[0], sel == 0);
		drawSpell(matrixStack, x + SLOT_2_X, y + SLOT_2_Y, caps.getQuickSpells()[1], sel == 1);
		drawSpell(matrixStack, x + SLOT_3_X, y + SLOT_3_Y, caps.getQuickSpells()[2], sel == 2);
		drawSpell(matrixStack, x + SLOT_4_X, y + SLOT_4_Y, caps.getQuickSpells()[3], sel == 3);
		drawSpell(matrixStack, x + SLOT_5_X, y + SLOT_5_Y, caps.getQuickSpells()[4], sel == 4);
		drawSpell(matrixStack, x + SLOT_6_X, y + SLOT_6_Y, caps.getQuickSpells()[5], sel == 5);

		if (sel != -1) {
			final SpellInstance spell = caps.getQuickSpells()[sel];
			if (spell != null) {
				final TranslationTextComponent name = new TranslationTextComponent(spell.getSpell().getLangKey());
				font.drawText(matrixStack, name, x + 15 + (78 - font.getStringPropertyWidth(name)) / 2F, y + 55, 0x000000);
			}
		}
	}

	private void drawSpell(MatrixStack matrixStack, int x, int y, @Nullable SpellInstance spell, boolean sel) {
		if (spell == null) return;

		final SpellType type = spell.getSpell().getType();

		blit(matrixStack, x, y, typeU(type), typeV(type, sel), 16, 16, TEX_X, TEX_Y);
	}

	private int getSelectedSpell(int mouseX, int mouseY) {
		if (mouseX > x + SLOT_1_X && mouseX < x + SLOT_1_X + 16 && mouseY > y + SLOT_1_Y && mouseY < y + SLOT_1_Y + 16)
			return 0;
		else if (mouseX > x + SLOT_2_X && mouseX < x + SLOT_2_X + 16 && mouseY > y + SLOT_2_Y && mouseY < y + SLOT_2_Y + 16)
			return 1;
		else if (mouseX > x + SLOT_3_X && mouseX < x + SLOT_3_X + 16 && mouseY > y + SLOT_3_Y && mouseY < y + SLOT_3_Y + 16)
			return 2;
		else if (mouseX > x + SLOT_4_X && mouseX < x + SLOT_4_X + 16 && mouseY > y + SLOT_4_Y && mouseY < y + SLOT_4_Y + 16)
			return 3;
		else if (mouseX > x + SLOT_5_X && mouseX < x + SLOT_5_X + 16 && mouseY > y + SLOT_5_Y && mouseY < y + SLOT_5_Y + 16)
			return 4;
		else if (mouseX > x + SLOT_6_X && mouseX < x + SLOT_6_X + 16 && mouseY > y + SLOT_6_Y && mouseY < y + SLOT_6_Y + 16)
			return 5;
		else return -1;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		final int sel = getSelectedSpell((int) mouseX, (int) mouseY);

		if (sel != -1) {
			final SpellInstance spell = caps.getQuickSpells()[sel];
			if (spell != null) {
				if (isShiftDown()) {
					final SpellPage page = screen.findSpellPage(spell.getSpell());
					if (page != null)
						screen.switchToPage(page);
				} else if (spell != caps.getSelected()) {
					caps.getPlayer().sendStatusMessage(new TranslationTextComponent(spell.getSpell().getLangKey()), true);
					caps.select(spell.getSpell());
					ASNetworkManager.get().sendToServer(new SpellSelectionMessage(spell.getSpell().getId()));
				}
			}
		}

		return false;
	}

	public static int typeU(SpellType type) {
		switch (type) {
		case AIR:
			return 121;
		case ARCANE:
			return 169;
		case COSMIC:
			return 185;
		case DARK:
			return 154;
		case EARTH:
			return 105;
		case FIRE:
			return 73;
		case LIGHT:
			return 137;
		case WATER:
			return 89;
		default:
			return -1;
		}
	}

	public static int typeV(SpellType type, boolean sel) {
		return 182 + (sel ? 16 : 0);
	}
}