package com.elance.charts.html;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.elance.charts.data.ChartInfo;

public class HTMLGeneratorController {

	private Map<ChartInfo, Map<String, Map<Double, Double>>> charts;
	
	private static final String outFileName = "out.html"; 
	
	public HTMLGeneratorController(Map<ChartInfo, Map<String, Map<Double, Double>>> charts){
		this.charts = charts;
	}		
	
	public List<String> createCharts(){
		
		List<String> chartHtmlFiles = new ArrayList<>();
		for (ChartInfo chartInfo : charts.keySet()) {
			HTMLGenerator generator;
			try {
				generator = new HTMLGenerator();
				generator.setTitle(chartInfo.getChartTitle());
				generator.setToolTipValueSuffix(chartInfo.getyAxisUnints());
				generator.setSeries(charts.get(chartInfo));
				generator.setxAxisTitle(chartInfo.getSignForXAxis());
				generator.setyAxisTitle(chartInfo.getSignForYAxis());
				
				chartHtmlFiles.add(generator.create(outFileName));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return chartHtmlFiles;
	}
	
}
