package com.elance.charts.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.elance.charts.records.Gender;
import com.elance.charts.records.Record;

public class DataProcessorGender extends DataProcessor{

	private int maleNumber;
	private int femaleNumber;
	
	public DataProcessorGender(List<Record> records) {
		super(records);
		
		maleNumber = calculateNumberAcordingToGender(Gender.MALE);
		femaleNumber = calculateNumberAcordingToGender(Gender.FEMALE);
	}
	
	private int calculateNumberAcordingToGender(Gender gender){
		int result = 0;
		
		for (Record record : records) {
			if(record.getGender() == gender){
				result++;
			}
		}
		
		return result;
	}

	@Override
	protected void calculateChartDataTimeOpen() {

		Map<String, Map<Double, Double>> result = new LinkedHashMap<>();

		Map<Double, Double> female = new TreeMap<>();
		Map<Double, Double> male = new TreeMap<>();

		String xAxisUnits = "";
		for (Record record : records) {

			if (record.isOpened()) {
				double timeOpen = record.getTimeToOpen();
				if (record.getGender() == Gender.MALE) {
					n = maleNumber;
					addRecordDataToMap(timeOpen, male);
				} else if (record.getGender() == Gender.FEMALE){
					n = femaleNumber;
					addRecordDataToMap(timeOpen, female);
				}
			}
			
			if(xAxisUnits.length()<1){
				xAxisUnits = record.getUnits().toString();
			}
		}

		result.put("Male", male);
		result.put("Female", female);

		ChartInfo chartInfo = new ChartInfo();
		chartInfo.setChartTitle("Time to open");
		chartInfo.setxAxisName("Time to open");
		chartInfo.setxAxisUnits(xAxisUnits);
		chartInfo.setyAxisName("opened");
		chartInfo.setyAxisUnints("%");
		plots.put(chartInfo, result);
	}

	@Override
	protected void calculateChartDataTimeClicked() {
		Map<String, Map<Double, Double>> result = new TreeMap<>();

		Map<Double, Double> female = new TreeMap<>();
		Map<Double, Double> male = new TreeMap<>();

		String xAxisUnits = "";
		for (Record record : records) {

			if (record.isClicked()) {
				double timeClicked = record.getTimeToClick();
				if (record.getGender() == Gender.MALE) {
					n = maleNumber;
					addRecordDataToMap(timeClicked, male);
				} else if (record.getGender() == Gender.FEMALE){
					n = femaleNumber;
					addRecordDataToMap(timeClicked, female);
				}
			}
			
			if(xAxisUnits.length()<1){
				xAxisUnits = record.getUnits().toString();
			}
		}

		result.put("Male", male);
		result.put("Female", female);

		
		ChartInfo chartInfo = new ChartInfo();
		chartInfo.setChartTitle("Time to click");
		chartInfo.setxAxisName("Time to click");
		chartInfo.setxAxisUnits(xAxisUnits);
		chartInfo.setyAxisName("clicked");
		chartInfo.setyAxisUnints("%");
		
		
		plots.put(chartInfo, result);
	}
}
