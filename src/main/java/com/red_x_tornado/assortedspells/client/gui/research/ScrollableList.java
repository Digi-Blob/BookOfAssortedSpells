package com.red_x_tornado.assortedspells.client.gui.research;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.client.gui.GuiUtil;
import com.red_x_tornado.assortedspells.client.gui.ResearchTableScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ScrollableList<T extends ScrollableList.Element> extends Widget {

	private final Minecraft mc;

	private final List<T> list = new ArrayList<>();
	private final int slotHeight;
	private final int scrollBarX;
	private final int width;

	private int scrollY = 0;
	private int scrollAmount = 0;
	private boolean scrolling = false;

	public ScrollableList(Minecraft mc, int x, int y, int width, int height, int slotHeight, int scrollBarX) {
		super(x, y, scrollBarX + 6, height, new StringTextComponent(""));
		this.width = width;
		this.mc = mc;
		this.slotHeight = slotHeight;
		this.scrollBarX = scrollBarX;
	}

	public List<T> getElements() {
		return list;
	}

	protected int getScrollbarY() {
		return y + scrollY;
	}

	protected int getMaxScroll() {
		if (list.isEmpty() || slotHeight * list.size() <= height)
			return 0;
		return list.size() - (height / slotHeight);
	}

	protected boolean clickedScrollbar(double mouseX, double mouseY) {
		return mouseX >= scrollBarX && mouseX < scrollBarX + 6
				&& mouseY >= getScrollbarY() && mouseY < getScrollbarY() + 27;
	}

	@Override
	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		for (int i = scrollAmount; i < list.size(); i++) {
			if (i < 0) continue;
			final int slotY = y + (i - scrollAmount) * slotHeight;
			if (slotY >= y + height) break;
			list.get(i).render(matrixStack, x, slotY, width, slotHeight, list.get(i).isMouseOver(mouseX, mouseY, x, slotY, width, slotHeight), mouseX, mouseY, partialTicks);
		}

		mc.getTextureManager().bindTexture(ResearchTableScreen.BACKGROUND);

		final int u;
		if (scrolling || clickedScrollbar(mouseX, mouseY))
			u = 12;
		else
			u = 6;
		blit(matrixStack, scrollBarX, getScrollbarY(), u, 228, 6, 27, 288, 256);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (clickedScrollbar(mouseX, mouseY) && getMaxScroll() > 0) {
			scrolling = true;
			return true;
		}

		scrolling = false;

		if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
			for (int i = scrollAmount; i < list.size(); i++) {
				final int slotY = y + (i - scrollAmount) * slotHeight;
				if (mouseY >= slotY && mouseY < slotY + slotHeight) {
					list.get(i).onClick(mouseX, mouseY, button);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		scrolling = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (scrolling) {
			if (mouseY < y)
				scrollY = scrollAmount = 0;
			else if (mouseY >= y + height) {
				scrollAmount = getMaxScroll();
				scrollY = height - slotHeight;
			} else {
				final double factor = (mouseY - y) / height;
				scrollAmount = (int) ((getMaxScroll() + 1) * factor);
				scrollY = (int) mouseY - y - 13;
				if (scrollY < 0)
					scrollY = 0;
				else if (scrollY > height - slotHeight - 1)
					scrollY = height - slotHeight - 1;
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (mouseX >= scrollBarX && mouseX < scrollBarX + 6
				&& mouseY >= y && mouseY < y + height) {
			if (delta < 0)
				scrollAmount++;
			else scrollAmount--;

			final int maxScroll = getMaxScroll();

			if (scrollAmount > maxScroll)
				scrollAmount = maxScroll;
			else if (scrollAmount < 0)
				scrollAmount = 0;

			if (maxScroll != 0)
				scrollY = (int) (((double) scrollAmount / maxScroll) * (height - slotHeight - 1));
			if (scrollY < 0) scrollY = 0;

			return true;
		}

		return false;
	}

	public static abstract class Element extends AbstractGui {
		public void onClick(double mouseX, double mouseY, int button) {}
		public abstract void render(MatrixStack matrixStack, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, float partialTicks);
		public boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
			return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
		}
	}
}