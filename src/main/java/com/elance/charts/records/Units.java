package com.elance.charts.records;

public enum Units {
	HOURS, DAYS;

	public static Units parseUnits(String units) {
		if (units.equalsIgnoreCase("days")) {
			return DAYS;
		}
		return HOURS;
	}
	
}
