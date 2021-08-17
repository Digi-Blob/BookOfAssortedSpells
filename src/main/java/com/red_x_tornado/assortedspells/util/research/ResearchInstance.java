package com.red_x_tornado.assortedspells.util.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.client.ClientEvents;
import com.red_x_tornado.assortedspells.util.spell.Spell;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;

public class ResearchInstance implements Comparable<ResearchInstance> {

	private final ResearchData research;
	private final List<ResearchAttempt> attempts = new ArrayList<>(0);

	public ResearchInstance(ResearchData research) {
		this.research = research;
	}

	public ResearchData getResearch() {
		return research;
	}

	public List<ResearchAttempt> getAttempts() {
		return attempts;
	}

	@Nullable
	public ResearchAttempt find(SpellType[] key) {
		root:
		for (ResearchAttempt attempt : attempts) {
			for (int i = 0; i < attempt.runes().length; i++) {
				final ResearchAttempt.RunePair pair = attempt.runes()[i];
				if (pair.rune() != key[i])
					continue root;
			}
			return attempt;
		}

		return null;
	}

	public ResearchAttempt addAttempt(SpellType[] key) {
		final ResearchAttempt.RunePair[] pairs = new ResearchAttempt.RunePair[key.length];
		for (int i = 0; i < research.getKey().length; i++) {
			final ResearchAttempt.RunePair pair = new ResearchAttempt.RunePair(key[i], research.matches(i, key[i]));
			pairs[i] = pair;
		}

		final ResearchAttempt attempt = new ResearchAttempt(pairs);

		if (!attempts.contains(attempt)) {
			attempts.add(attempt);
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEvents::refreshResearchAttempts);
		}

		return attempt;
	}

	public CompoundNBT toNBT(boolean writeMatches) {
		final CompoundNBT nbt = new CompoundNBT();

		nbt.putString("id", research.getSpell().getId().toString());

		final ListNBT list = new ListNBT();
		for (ResearchAttempt attempt : attempts) {
			if (writeMatches) {
				final ListNBT runeList = new ListNBT();
				for (ResearchAttempt.RunePair rp : attempt.runes()) {
					final CompoundNBT tag = new CompoundNBT();
					tag.putByte("rune", (byte) rp.rune().ordinal());
					tag.putByte("match", (byte) rp.match().ordinal());
					runeList.add(tag);
				}
				list.add(runeList);
			} else
			list.add(new ByteArrayNBT(Arrays.stream(attempt.runes())
					.map(r -> ((Integer) r.rune().ordinal()).byteValue())
					.collect(Collectors.toList())));
		}
		nbt.put("attempts", list);

		return nbt;
	}

	public static ResearchInstance fromNBT(CompoundNBT nbt, boolean readMatches) {
		final ResourceLocation id = new ResourceLocation(nbt.getString("id"));
		final Spell spell = Spell.find(id);
		final ResearchInstance ret = new ResearchInstance(ResearchData.forSpell(spell));

		if (readMatches) {
			final ListNBT list = nbt.getList("attempts", Constants.NBT.TAG_LIST);
			for (INBT tag : list) {
				final ListNBT runes = (ListNBT) tag;
				final ResearchAttempt.RunePair[] pairs = new ResearchAttempt.RunePair[runes.size()];

				for (int i = 0; i < runes.size(); i++) {
					final CompoundNBT rune = runes.getCompound(i);
					final SpellType type = SpellType.values()[rune.getByte("rune")];
					final MatchType match = MatchType.values()[rune.getByte("match")];
					final ResearchAttempt.RunePair pair = new ResearchAttempt.RunePair(type, match);
					pairs[i] = pair;
				}

				final ResearchAttempt attempt = new ResearchAttempt(pairs);
				ret.attempts.add(attempt);
			}
		} else {
			final ListNBT list = nbt.getList("attempts", Constants.NBT.TAG_BYTE_ARRAY);
			for (INBT tag : list) {
				final byte[] runes = ((ByteArrayNBT) tag).getByteArray();
				final ResearchAttempt.RunePair[] pairs = new ResearchAttempt.RunePair[runes.length];

				for (int i = 0; i < runes.length; i++) {
					final SpellType type = SpellType.values()[runes[i]];
					final MatchType match = ret.getResearch().matches(i, type);
					final ResearchAttempt.RunePair pair = new ResearchAttempt.RunePair(type, match);
					pairs[i] = pair;
				}

				final ResearchAttempt attempt = new ResearchAttempt(pairs);
				ret.attempts.add(attempt);
			}
		}

		return ret;
	}

	@Override
	public int compareTo(ResearchInstance o) {
		if (o == null) return 1;

		return research.compareTo(o.research);
	}
}