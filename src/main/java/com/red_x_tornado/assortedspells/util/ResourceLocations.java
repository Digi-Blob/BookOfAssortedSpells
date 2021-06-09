package com.red_x_tornado.assortedspells.util;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;

import net.minecraft.util.ResourceLocation;

public class ResourceLocations {

	public static ResourceLocation mc(String path) {
		return new ResourceLocation("minecraft", path);
	}

	public static ResourceLocation as(String path) {
		return new ResourceLocation(BookOfAssortedSpells.MOD_ID, path);
	}
}