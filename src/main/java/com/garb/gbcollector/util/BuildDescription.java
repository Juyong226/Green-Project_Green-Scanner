package com.garb.gbcollector.util;

public class BuildDescription {

	public static String get(String descFormat, String... args) {
		String desc = String.format(descFormat, args);
		return desc;
	}
}
