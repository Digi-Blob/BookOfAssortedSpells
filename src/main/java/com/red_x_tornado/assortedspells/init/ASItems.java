package com.red_x_tornado.assortedspells.init;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.item.WandItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ASItems {

	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BookOfAssortedSpells.MOD_ID);

	public static final RegistryObject<Item> WAND = REGISTRY.register("wand", () -> new WandItem(props().maxStackSize(1)));

	public static Item.Properties props() {
		return new Item.Properties().group(BookOfAssortedSpells.TAB);
	}
}