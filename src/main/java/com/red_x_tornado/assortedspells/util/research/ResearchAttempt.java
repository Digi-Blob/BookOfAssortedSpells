package com.red_x_tornado.assortedspells.util.research;

import java.util.Arrays;
import java.util.Objects;

import com.red_x_tornado.assortedspells.util.spell.SpellType;

public class ResearchAttempt {

	private final RunePair[] runes;

	public ResearchAttempt(RunePair[] runes) {
		this.runes = runes;
	}

	public RunePair[] runes() {
		return runes;
	}

	public boolean isCorrect() {
		for (RunePair rp : runes)
			if (rp.match != MatchType.EXACT)
				return false;

		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		return Arrays.equals(runes, ((ResearchAttempt) obj).runes);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(runes);
	}

	@Override
	public String toString() {
		return Arrays.toString(runes);
	}

	public static class RunePair {

		private final SpellType rune;
		private final MatchType match;

		public RunePair(SpellType rune, MatchType match) {
			this.rune = rune;
			this.match = match;
		}

		public SpellType rune() {
			return rune;
		}

		public MatchType match() {
			return match;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			final RunePair o = (RunePair) obj;

			return rune == o.rune && match == o.match;
		}

		@Override
		public int hashCode() {
			return Objects.hash(rune, match);
		}

		@Override
		public String toString() {
			return "[" + rune + ": " + match + "]";
		}
	}
}