package com.red_x_tornado.assortedspells.util.research;

public enum MatchType {
	FAIL, // Rune not present.
	EXACT, // Rune correctly placed.
	OFFSET; // The rune is present in the code, but isn't correctly placed.
}