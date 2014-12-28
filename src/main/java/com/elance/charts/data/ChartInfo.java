package com.elance.charts.data;

public class ChartInfo {

	private String chartTitle;
	private String xAxisName;
	private String yAxisName;
	private String xAxisUnits;
	private String yAxisUnints;
	
	
	
	
	public String getChartTitle() {
		return chartTitle;
	}
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}
	public String getxAxisName() {
		return xAxisName;
	}
	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}
	public String getyAxisName() {
		return yAxisName;
	}
	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}
	public String getxAxisUnits() {
		return xAxisUnits;
	}
	public void setxAxisUnits(String xAxisUnits) {
		this.xAxisUnits = xAxisUnits;
	}
	public String getyAxisUnints() {
		return yAxisUnints;
	}
	public void setyAxisUnints(String yAxisUnints) {
		this.yAxisUnints = yAxisUnints;
	}
	
	public String getSignForXAxis(){
		return String.format("%s (%s)", xAxisName,xAxisUnits);
	}
	
	public String getSignForYAxis(){
		return String.format("%s %s", yAxisUnints, yAxisName);
	}
	@Override
	public String toString() {
		return "ChartInfo [xAxisName=" + xAxisName + ", yAxisName=" + yAxisName
				+ ", xAxisUnits=" + xAxisUnits + ", yAxisUnints=" + yAxisUnints
				+ "]";
	}

	
}
