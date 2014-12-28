package com.elance.charts.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.elance.charts.records.Record;

public class DataProcessor {

	
	
	protected List<Record> records;

	protected int n;

	protected Map<ChartInfo, Map<String, Map<Double, Double>>> plots;

	public DataProcessor(List<Record> records) {
		this.records = records;

		plots = new LinkedHashMap<>();
		n = records.size();
	}

	public void calculate() {
		calculateChartDataTimeOpen();
		calculateChartDataTimeClicked();
	}

	protected void calculateChartDataTimeOpen() {

		Map<String, Map<Double, Double>> result = new LinkedHashMap<>();
		Map<Double, Double> temp = new TreeMap<>();

		String xAxisUnits = "";
		for (Record record : records) {
			if (record.isOpened()) {
				addRecordDataToMap(record.getTimeToOpen(), temp);
			}
			
			if(xAxisUnits.length()<1){
				xAxisUnits = record.getUnits().toString();
			}
		}

		result.put("", temp);
		ChartInfo chartInfo = new ChartInfo();
		chartInfo.setChartTitle("Time to open");
		chartInfo.setxAxisName("Time to open");
		chartInfo.setxAxisUnits(xAxisUnits);
		chartInfo.setyAxisName("opened");
		chartInfo.setyAxisUnints("%");
		plots.put(chartInfo, result);
	}

	protected void calculateChartDataTimeClicked() {
		Map<String, Map<Double, Double>> result = new LinkedHashMap<>();
		Map<Double, Double> temp = new TreeMap<>();

		String xAxisUnits = "";
		for (Record record : records) {
			if (record.isClicked()) {
				addRecordDataToMap(record.getTimeToClick(), temp);
			}
			
			if(xAxisUnits.length()<1){
				xAxisUnits = record.getUnits().toString();
			}
		}

		result.put("", temp);
		ChartInfo chartInfo = new ChartInfo();
		chartInfo.setChartTitle("Time to click");
		chartInfo.setxAxisName("Time to click");
		chartInfo.setxAxisUnits(xAxisUnits);
		chartInfo.setyAxisName("clicked");
		chartInfo.setyAxisUnints("%");
		plots.put(chartInfo, result);
	}

	protected void addRecordDataToMap(double timeData, Map<Double, Double> data) {
		putNewKeyToDataMap(timeData, data);
		increseAllUpper(timeData, data);
	}

	private void putNewKeyToDataMap(Double key, Map<Double, Double> data) {
		if (!data.containsKey(key)) {
			data.put(key, 1.0 / n);
			Double newPart = getTotalPartBefor(key, data) + data.get(key);
			data.put(key, newPart);
		} else {
			data.put(key, calculateNewPart(data.get(key)));
		}
	}

	private void increseAllUpper(double key, Map<Double, Double> data) {
		for (Double keyValue : data.keySet()) {
			if (keyValue <= key) {
				continue;
			}

			Double part = calculateNewPart(data.get(keyValue));
			data.put(keyValue, part);
		}
	}

	private double calculateNewPart(double oldPart) {
		Double newNumber = n * oldPart + 1;
		return newNumber / n;
	}

	private double getTotalPartBefor(double key, Map<Double, Double> data) {
		double part = 0;
		for (Double _key : data.keySet()) {
			if (_key >= key) {
				break;
			}
			part = data.get(_key);
		}

		return part;
	}

	public Map<ChartInfo, Map<String, Map<Double, Double>>> getPlots() {
		return plots;
	}

}
