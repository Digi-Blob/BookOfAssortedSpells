package com.red_x_tornado.assortedspells.client.gui.tome;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.network.ASNetworkManager;
import com.red_x_tornado.assortedspells.network.msg.SpellSelectionMessage;
import com.red_x_tornado.assortedspells.util.cast.ISpellCaster;
import com.red_x_tornado.assortedspells.util.spell.SpellInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SpellPage extends Page {

	private final SpellInstance spell;

	public SpellPage(SpellInstance spell) {
		this.spell = spell;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
		final int maxWidth = 146 - 15 - 16;
		final int color = 0x000000;

		drawSelectButton(matrixStack, mouseX, mouseY);
		drawBookmarkButton(matrixStack, mouseX, mouseY);

		final TranslationTextComponent name = trans(spell.getSpell().getLangKey());
		final ITextComponent clazz = trans("assortedspells.gui.spell.class", trans(spell.getSpell().getClazz().getLangKey()));
		final ITextComponent type = trans("assortedspells.gui.spell.type", trans(spell.getSpell().getType().getLangKey()));
		final ITextComponent castingStyle = trans("assortedspells.gui.spell.casting_style", trans("assortedspells.gui.spell.casting_style." + (spell.getSpell().getCaster() == ISpellCaster.DELAYED ? "delayed" : "instant")));
		final ITextComponent difficulty = trans("assortedspells.gui.spell.difficulty", trans(spell.getSpell().getBaseDifficulty().getLangKey()));
		final ITextComponent cooldown = trans("assortedspells.gui.spell.cooldown", ticksToSeconds(spell.getMaxCooldown()));
		final ITextComponent duration = trans("assortedspells.gui.spell.duration", ticksToSeconds(spell.getDuration()));
		final ITextComponent casts = trans("assortedspells.gui.spell.max_casts", spell.getMaxCasts());

		font.drawText(matrixStack, name, x + (maxWidth - font.getStringPropertyWidth(name)) / 2, y, color);

		final int ty = y + 12;

		font.drawText(matrixStack, clazz, x, ty, color);
		font.drawText(matrixStack, type, x, ty + 9 * 1, color);
		font.drawText(matrixStack, castingStyle, x, ty + 9 * 2, color);
		font.drawText(matrixStack, difficulty, x, ty + 9 * 3, color);
		font.drawText(matrixStack, cooldown, x, ty + 9 * 4, color);
		font.drawText(matrixStack, duration, x, ty + 9 * 5, color);
		font.drawText(matrixStack, casts, x, ty + 9 * 6, color);

		final TranslationTextComponent desc = new TranslationTextComponent(spell.getSpell().getDescLangKey());

		font.func_238418_a_(desc, x, ty + 9 * 8, maxWidth, color);

		minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
	}

	private String ticksToSeconds(int ticks) {
		final float seconds = ticks / 20F;
		final String base;
		if (seconds % 1 == 0) base = Integer.toString((int) seconds);
		else base = Float.toString(seconds);

		return base + " second" + (seconds == 1 ? "" : "s");
	}

	protected void drawSelectButton(MatrixStack matrixStack, int mouseX, int mouseY) {
		final int x = this.x + width - 40;
		final int y = this.y - 2;
		final boolean sel = mouseX > x && mouseX < x + 10 && mouseY > y && mouseY < y + 10;
		final boolean shiftDown = InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)
				|| InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT);

		if (shiftDown) {
			blit(matrixStack, x, y, 235, sel ? 152 : 142, 10, 10, TEX_X, TEX_Y);
			int v = 162;
			for (int i = 0; i < caps.getQuickSpells().length; i++)
				if (caps.getQuickSpells()[i] == spell) {
					v = 172 + i * 10;
					break;
				}
			blit(matrixStack, x, y, 235, v, 10, 10, TEX_X, TEX_Y);
		} else
			blit(matrixStack, x, y, 245, sel ? 152 : 142, 10, 10, TEX_X, TEX_Y);
	}

	protected void drawBookmarkButton(MatrixStack matrixStack, int mouseX, int mouseY) {
		final int x = this.x + width - 40;
		final int y = this.y + 10;
		final boolean sel = mouseX > x && mouseX < x + 10 && mouseY > y && mouseY < y + 10;

		blit(matrixStack, x, y, 245, sel ? 172 : 162, 10, 10, TEX_X, TEX_Y);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (clickedSelect(mouseX, mouseY))
			return true;
		if (clickedBookmark(mouseX, mouseY))
			return true;

		return false;
	}

	protected boolean clickedSelect(double mouseX, double mouseY) {
		final int x = this.x + width - 40;
		final int y = this.y - 2;
		if (mouseX > x && mouseX < x + 10 && mouseY > y && mouseY < y + 10) {
			playClickSound();
			if (spell != caps.getSelected()) {
				caps.getPlayer().sendStatusMessage(new TranslationTextComponent(spell.getSpell().getLangKey()), true);
				caps.select(spell.getSpell());
				ASNetworkManager.get().sendToServer(new SpellSelectionMessage(spell.getSpell().getId()));
			}
			return true;
		}

		return false;
	}

	protected boolean clickedBookmark(double mouseX, double mouseY) {
		final int x = this.x + width - 40;
		final int y = this.y + 10;
		if (mouseX > x && mouseX < x + 10 && mouseY > y && mouseY < y + 10) {
			playClickSound();
			// TODO: Bookmarks
			return true;
		}

		return false;
	}
}