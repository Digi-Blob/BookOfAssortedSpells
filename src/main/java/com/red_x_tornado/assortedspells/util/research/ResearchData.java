package com.red_x_tornado.assortedspells.util.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.red_x_tornado.assortedspells.util.spell.Spell;
import com.red_x_tornado.assortedspells.util.spell.SpellDifficulty;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

public class ResearchData implements Comparable<ResearchData> {

	private static final Random RAND = new Random();

	private final Spell spell;
	private final SpellType[] key;

	public ResearchData(Spell spell) {
		this.spell = spell;
		key = new SpellType[getKeyLength(spell.getBaseDifficulty())];
		randomiseKey(RAND);
	}

	protected void randomiseKey(Random rand) {
		final List<SpellType> pool = new ArrayList<>();
		Collections.addAll(pool, SpellType.values());

		for (int i = 0; i < key.length; i++) {
			final SpellType rune = pool.remove(rand.nextInt(pool.size()));
			key[i] = rune;
		}
	}

	public static int getKeyLength(SpellDifficulty difficulty) {
		switch (difficulty) {
		case SIMPLE: return 3;
		case EASY: return 4;
		case COMPLICATED: return 5;
		case INSANE: return 7;
		default: return 3;
		}
	}

	public static ResearchData forSpell(Spell spell) {
		return new ResearchData(spell);
	}

	public Spell getSpell() {
		return spell;
	}

	public SpellType[] getKey() {
		return key;
	}

	public MatchType matches(int index, SpellType guess) {
		if (key[index] == guess) return MatchType.EXACT;

		for (SpellType type : key)
			if (type == guess)
				return MatchType.OFFSET;

		return MatchType.FAIL;
	}

	@Override
	public int compareTo(ResearchData o) {
		if (o == null) return 1;

		return spell.compareTo(o.spell);
	}
}