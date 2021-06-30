package com.red_x_tornado.assortedspells.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.client.gui.tome.BookmarkPage;
import com.red_x_tornado.assortedspells.client.gui.tome.Page;
import com.red_x_tornado.assortedspells.client.gui.tome.QuickspellPage;
import com.red_x_tornado.assortedspells.client.gui.tome.SpellPage;
import com.red_x_tornado.assortedspells.client.gui.tome.SpellTypePage;
import com.red_x_tornado.assortedspells.util.spell.Spell;
import com.red_x_tornado.assortedspells.util.spell.SpellInstance;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

public class TomeScreen extends Screen {

	private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(BookOfAssortedSpells.MOD_ID, "textures/gui/tome.png");

	private static final int TEX_X = 384;
	private static final int TEX_Y = 256;

	private static final int X_SIZE = 146 * 2 - 12;
	private static final int Y_SIZE = 180;

	private int screenX;
	private int screenY;

	private ArrowButton leftArrow;
	private ArrowButton rightArrow;

	private final SpellCapability caps;
	private final List<SpellInstance> spells;
	private final List<Page> pages = new ArrayList<>();

	private int page = 0;
	private BookmarkPage bookmarkPage;
	private Map<SpellType,Integer> spellTypePages = new EnumMap<>(SpellType.class);

	public TomeScreen() {
		super(new StringTextComponent(""));
		caps = SpellCapability.get(Minecraft.getInstance().player);
		spells = new ArrayList<>(caps.getKnownSpells());
		try {
			Collections.sort(spells);
			fillPages();
		} catch (Exception e) {
			BookOfAssortedSpells.LOGGER.error("An exception occurred on tome screen init!", e);
		}
	}

	public static void open() {
		Minecraft.getInstance().displayGuiScreen(new TomeScreen());
	}

	protected void fillPages() {
		pages.clear();

		pages.add(new QuickspellPage());
		pages.add(bookmarkPage = new BookmarkPage());

		for (SpellType type : SpellType.values()) {
			spellTypePages.put(type, pages.size());
			pages.add(new SpellTypePage(type));

			for (SpellInstance spell : spells)
				if (spell.getSpell().getType() == type)
					pages.add(new SpellPage(spell));
		}
	}

	@Override
	protected void init() {
		screenX = (width - X_SIZE) / 2;
		screenY = (height - Y_SIZE) / 2;

		rightArrow = new ArrowButton(screenX + 135 + 103, screenY + 155, false, b -> turnPage(false));
		leftArrow = new ArrowButton(screenX + 25, screenY + 155, true, b -> turnPage(true));

		addListener(leftArrow);
		addListener(rightArrow);

		for (Page page : pages)
			page.init(minecraft, font, caps, this);
	}

	protected void turnPage(boolean left) {
		page = MathHelper.clamp(page + (left ? -2 : 2), 0, pages.size() % 2 == 0 ? pages.size() : pages.size() - 1);
	}

	public void switchToPage(Page page) {
		final int idx = pages.indexOf(page);
		if (idx == -1) return;
		this.page = idx % 2 == 0 ? idx : idx - 1;
	}

	@Nullable
	public SpellPage findSpellPage(Spell spell) {
		return (SpellPage) pages.stream()
				.filter(p -> p instanceof SpellPage && ((SpellPage) p).getSpell().getSpell().equals(spell))
				.findFirst()
				.orElse(null);
	}

	public BookmarkPage getBookmarkPage() {
		return bookmarkPage;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);

		try {
			minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);

			final Page rightPage = page + 1 >= pages.size() ? null : pages.get(page + 1);

			blit(matrixStack, screenX + 135, screenY, 0, 0, 146, 180, TEX_X, TEX_Y);

			final int rightOffset = rightPage != null ? rightPage.getTypeBookmarkOffset(false) : 0;

			if (rightPage != null) { // Draw tab.
				int tab = 0;
				for (SpellType type : SpellType.values()) {
					if (rightPage instanceof SpellTypePage && ((SpellTypePage) rightPage).type == type)
						continue;
					final Integer index = spellTypePages.get(type);
					if (index == null || index <= page + 1) continue;
					blit(matrixStack, screenX + 135 + 10 + tab + rightOffset, screenY - 18, 256, 0, 24, 24, TEX_X, TEX_Y);
					blit(matrixStack, screenX + 135 + 10 + tab + 4 + rightOffset, screenY - 18 + 4, QuickspellPage.typeU(type), QuickspellPage.typeV(type, false), 16, 16, TEX_X, TEX_Y);
					tab += 14;
				}
			}

			if (rightPage != null) {
				rightPage.setPos(screenX + 135 + 15, screenY + 16, 146, 180);
				rightPage.render(matrixStack, mouseX, mouseY);
			} else {
				final int maxWidth = 146 - 15 - 16;
				final int x = screenX + 135 + 15;
				final int y = screenY + 16 + 9 * 6;
				font.drawString(matrixStack, "This space intentionally", x + (maxWidth - font.getStringWidth("This space intentionally")) / 2, y, 0x000000);
				font.drawString(matrixStack, "left blank.", x + (maxWidth - font.getStringWidth("left blank.")) / 2, y + 9, 0x000000);
				minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
			}

			rightArrow.render(matrixStack, mouseX, mouseY, partialTicks);

			final Page leftPage = page >= pages.size() ? null : pages.get(page);

			blit(matrixStack, screenX, screenY, 140, 180, 146, 0, -140, 180, TEX_X, TEX_Y);

			final int offset = leftPage != null ? leftPage.getTypeBookmarkOffset(false) : 0;

			if (leftPage != null) { // Draw tab.
				int tab = 0;
				for (SpellType type : SpellType.values()) {
					if (leftPage instanceof SpellTypePage && ((SpellTypePage) leftPage).type == type)
						continue;
					final Integer index = spellTypePages.get(type);
					if (index == null || index >= page) continue;
					blit(matrixStack, screenX + 10 + tab + offset, screenY - 18, 256, 0, 24, 24, TEX_X, TEX_Y);
					blit(matrixStack, screenX + 10 + tab + 4 + offset, screenY - 18 + 4, QuickspellPage.typeU(type), QuickspellPage.typeV(type, false), 16, 16, TEX_X, TEX_Y);
					tab += 14;
				}
			}

			if (leftPage != null) {
				leftPage.setPos(screenX + 18, screenY + 16, 140, 180);
				leftPage.render(matrixStack, mouseX, mouseY);
			} else {
				final int maxWidth = 146 - 15 - 16;
				final int x = screenX + 15;
				final int y = screenY + 16 + 9 * 6;
				font.drawString(matrixStack, "This space intentionally", x + (maxWidth - font.getStringWidth("This space intentionally")) / 2, y, 0x000000);
				font.drawString(matrixStack, "left blank.", x + (maxWidth - font.getStringWidth("left blank.")) / 2, y + 9, 0x000000);
				minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
			}

			leftArrow.render(matrixStack, mouseX, mouseY, partialTicks);

			super.render(matrixStack, mouseX, mouseY, partialTicks);
		} catch (Exception e) { // I might have accidentally screwed something up, so let's not crash.
			BookOfAssortedSpells.LOGGER.error("Exception rendering tome GUI:", e);
		}
	}

	private void hfill(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
		hLine(matrixStack, x1, x2, y1, color);
		hLine(matrixStack, x1, x2, y2, color);
		vLine(matrixStack, x1, y1, y2, color);
		vLine(matrixStack, x2, y1, y2, color);
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

		final Page leftPage = page >= pages.size() ? null : pages.get(page);
		final Page rightPage = page + 1 >= pages.size() ? null : pages.get(page + 1);

		if (leftPage != null && leftPage.keyPressed(scanCode, keyCode, modifiers))
			return true;

		if (rightPage != null && rightPage.keyPressed(scanCode, keyCode, modifiers))
			return true;

		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		final Page leftPage = page >= pages.size() ? null : pages.get(page);
		final Page rightPage = page + 1 >= pages.size() ? null : pages.get(page + 1);

		if (leftPage != null && leftPage.inBounds(mouseX, mouseY) && leftPage.mouseClicked(mouseX, mouseY, button))
			return true;

		if (rightPage != null && rightPage.inBounds(mouseX, mouseY) && rightPage.mouseClicked(mouseX, mouseY, button))
			return true;

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		final Page leftPage = page >= pages.size() ? null : pages.get(page);
		final Page rightPage = page + 1 >= pages.size() ? null : pages.get(page + 1);

		if (leftPage != null && leftPage.inBounds(mouseX, mouseY) && leftPage.mouseDragged(mouseX, mouseY, button, dragX, dragY))
			return true;

		if (rightPage != null && rightPage.inBounds(mouseX, mouseY) && rightPage.mouseDragged(mouseX, mouseY, button, dragX, dragY))
			return true;

		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		final Page leftPage = page >= pages.size() ? null : pages.get(page);
		final Page rightPage = page + 1 >= pages.size() ? null : pages.get(page + 1);

		if (leftPage != null && leftPage.inBounds(mouseX, mouseY))
			leftPage.mouseMoved(mouseX, mouseY);

		if (rightPage != null && rightPage.inBounds(mouseX, mouseY))
			rightPage.mouseMoved(mouseX, mouseY);

		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		final Page leftPage = page >= pages.size() ? null : pages.get(page);
		final Page rightPage = page + 1 >= pages.size() ? null : pages.get(page + 1);

		if (leftPage != null && leftPage.inBounds(mouseX, mouseY) && leftPage.mouseReleased(mouseX, mouseY, button))
			return true;

		if (rightPage != null && rightPage.inBounds(mouseX, mouseY) && rightPage.mouseReleased(mouseX, mouseY, button))
			return true;

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		final Page leftPage = page >= pages.size() ? null : pages.get(page);
		final Page rightPage = page + 1 >= pages.size() ? null : pages.get(page + 1);

		if (leftPage != null && leftPage.inBounds(mouseX, mouseY) && leftPage.mouseScrolled(mouseX, mouseY, delta))
			return true;

		if (rightPage != null && rightPage.inBounds(mouseX, mouseY) && rightPage.mouseScrolled(mouseX, mouseY, delta))
			return true;

		return super.mouseScrolled(mouseX, mouseY, delta);
	}

	private static class ArrowButton extends Button {

		private final boolean left;

		public ArrowButton(int x, int y, boolean left, IPressable pressedAction) {
			super(x, y, 18, 10, new StringTextComponent(""), pressedAction);
			this.left = left;
		}

		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			Minecraft.getInstance().getTextureManager().bindTexture(BOOK_TEXTURE);

			blit(matrixStack, x, y, isHovered() ? 18 : 0, left ? 191 : 181, width, height, TEX_X, TEX_Y);
		}
	}
}