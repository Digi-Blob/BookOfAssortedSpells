package com.red_x_tornado.assortedspells.client.model;

import java.util.Objects;

import net.minecraft.util.ResourceLocation;

public class WandModelKey {

	private final ResourceLocation cap1;
	private final ResourceLocation rod;
	private final ResourceLocation cap2;

	public WandModelKey(ResourceLocation cap1, ResourceLocation rod, ResourceLocation cap2) {
		this.cap1 = cap1;
		this.rod = rod;
		this.cap2 = cap2;
	}

	public ResourceLocation cap1() {
		return cap1;
	}

	public ResourceLocation rod() {
		return rod;
	}

	public ResourceLocation cap2() {
		return cap2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		final WandModelKey o = (WandModelKey) obj;

		return cap1.equals(o.cap1) && rod.equals(o.rod) && cap2.equals(o.cap2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cap1, rod, cap2);
	}

	@Override
	public String toString() {
		return "WandModelKey[" + cap1 + ", " + rod + ", " + cap2 + "]";
	}
}