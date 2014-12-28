package com.elance.charts.records;

public class Record {

	private Gender gender;
	private String firstName;
	private String secondName;
	private boolean opened;
	private double timeToOpen;
	private boolean clicked;
	private double timeToClick;
	private Units units;
	
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	public void setOpened(String opened) {
		this.opened = parseLogicalData(opened);
	}
	public double getTimeToOpen() {
		return timeToOpen;
	}
	public void setTimeToOpen(double timeToOpen) {
		this.timeToOpen = timeToOpen;
	}
	public void setTimeToOpen(String timeToOpen) {
		this.timeToOpen = parseTimeData(timeToOpen);
	}
	public boolean isClicked() {
		return clicked;
	}
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	public void setClicked(String clicked) {
		this.clicked = parseLogicalData(clicked);
	}
	
	public double getTimeToClick() {
		return timeToClick;
	}
	public void setTimeToClick(double timeToClick) {
		this.timeToClick = timeToClick;
	}
	public void setTimeToClick(String timeToClick) {
		this.timeToClick = parseTimeData(timeToClick);
	}
	public Units getUnits() {
		return units;
	}
	public void setUnits(Units units) {
		this.units = units;
	}
	
	private boolean parseLogicalData(String data){
		if(data.equalsIgnoreCase("y")){
			return true;
		}
		return false;
	}
	
	private double parseTimeData(String data){
		try {
			return Double.parseDouble(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public String toString() {
		return "Record [\tgender=" + gender + ", \tfirstName=" + firstName
				+ ", \tsecondName=" + secondName + ", \topened=" + opened
				+ ", \ttimeToOpen=" + timeToOpen + ", \tclicked=" + clicked
				+ ", \ttimeToClick=" + timeToClick + ", \tunits=" + units + "]";
	}
	
	
}
