package com.elance.charts.records;

public enum Gender {

	MALE, FEMALE;

	public static Gender parseGender(String gender) {
		switch (gender) {
		case "M":
			return MALE;
		case "F":
			return FEMALE;
		}
		return null;
	}
}
