package com.elance.charts.html;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HTMLGenerator {

	private static final String templateFilePath = "lib/chart.html";

	private static final String TITLE_REPLACER = "${title}";
	private static final String SUBTITLE_RAPLECER = "${subtitle}";
	private static final String X_AXIS_TIK_INTERVAL_RAPLECER = "${x_tik_interval}";
	private static final String X_AXIS_TITLE_RAPLECER = "${x_title}";
	private static final String Y_AXIS_TITLE_RAPLECER = "${y_title}";
	private static final String Y_AXIS_MIN_VALUE_RAPLECER = "${y_min_value}";
	private static final String TOOLTIP_VALUE_SUFFIX_RAPLECER = "${tooltip_suffix}";
	private static final String SERIES_RAPLECER = "${series}";

	private String title = "";
	private String subTitle = "";
	private String xAxisTitle = "";
	private double xAxisTikInterval = 1;
	private String yAxisTitle = "";
	private double yAxisMinValue = 0;
	private String toolTipValueSuffix = "";
	private Map<String, Map<Double, Double>> series;

	private List<String> htmlTemplateData;

	private File template = new File(templateFilePath);
	
	
	private double maxXValue;

	public HTMLGenerator() throws FileNotFoundException {
//		if (!template.exists()) {
//			throw new FileNotFoundException(String.format(
//					"template file \"%s\" not found", templateFilePath));
//		}
	}

	public String generate() {
		htmlTemplateData = readTemplate(template);
		generateHTML();
		return convertListToString(htmlTemplateData);
	}

	private String convertListToString(List<String> list) {
		String result = "";

		for (String string : list) {
			result += string + "\n";
		}

		return result;
	}
	

	public String create(String fileName) {
		generate();
		try {
			File file = createOutFile(fileName);
			writeToFile(file, htmlTemplateData);
			return file.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File createOutFile(String fileName) {
		File file = new File(fileName);

		int counter = 1;
		while (file.exists()) {
			file = new File(fileName);
			String newFileName = fileName.substring(0, fileName.indexOf("."));
			newFileName += "(" + counter + ")";
			newFileName += fileName.substring(fileName.indexOf("."));
			file = new File(newFileName);
			counter++;
		}

		return file;
	}

	private void writeToFile(File file, List<String> list) throws IOException {
		PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter(
				file, false)));

		for (String elem : list) {
			out1.println(elem);
		}
		out1.close();

	}

	private void generateHTML() {
		String series = convertMapSeriesToStringSeries(this.series);
		substituteValues(SERIES_RAPLECER, series);
		substituteValues(TITLE_REPLACER, title);
		substituteValues(SUBTITLE_RAPLECER, subTitle);
		substituteValues(X_AXIS_TIK_INTERVAL_RAPLECER, calculateXTikInterval() + "");
		substituteValues(Y_AXIS_TITLE_RAPLECER, yAxisTitle);
		substituteValues(Y_AXIS_MIN_VALUE_RAPLECER, yAxisMinValue + "");
		substituteValues(X_AXIS_TITLE_RAPLECER, xAxisTitle);
		substituteValues(TOOLTIP_VALUE_SUFFIX_RAPLECER, toolTipValueSuffix);
	}

	private boolean substituteValues(String replacer, String value) {
		for (String row : htmlTemplateData) {
			if (row.contains(replacer)) {
				int index = htmlTemplateData.indexOf(row);
				htmlTemplateData.remove(row);
				row = row.replace(replacer, value);
				htmlTemplateData.add(index, row);
				return true;
			}
		}

		return false;
	}

	private String convertMapSeriesToStringSeries(
			Map<String, Map<Double, Double>> series) {
		String result = "";

		for (String curveName : series.keySet()) {
			result += String.format("{name: '%s',", curveName);

			result += "data:[";

			Map<Double, Double> values = addPoints(series.get(curveName));

			for (Double xValue : values.keySet()) {
				Double yValue = values.get(xValue);
				yValue = (double) Math.round(yValue * 100 * 10) / 10;
				result += String.format("[%s,%s],", xValue, yValue);
			}
			result += "]},";
		}

		return result;
	}

	private Map<Double, Double> addPoints(Map<Double, Double> map) {
		Map<Double, Double> result = new TreeMap<>();

		KeyValue startPoint = new KeyValue(1.0, 0.0);
		KeyValue firstElement = getFirstValueOfMap(map);
		KeyValue prevPoint = new KeyValue();

		if (firstElement.getKey() == startPoint.getKey()) {
			prevPoint = firstElement;
		} else {
			prevPoint = startPoint;
		}
		result.put(prevPoint.key, prevPoint.value);
		for (Double xValue : map.keySet()) {
			KeyValue currentPoint = new KeyValue(xValue, map.get(xValue));
			if (currentPoint.key - 1 >= 1) {
				result.put(currentPoint.key - 1, prevPoint.value);
			}
			result.put(currentPoint.key, currentPoint.value);
			prevPoint = currentPoint;
		}
		
		if(prevPoint.getKey() < maxXValue){
			result.put(maxXValue, prevPoint.getValue());
		}
		return result;
	}

	private double getMaxKeyValue(Map<String, Map<Double, Double>> map) {
		double max = Double.MIN_VALUE;
		for (String key : map.keySet()) {

			for (Double _key : map.get(key).keySet()) {
				if (_key > max) {
					max = _key;
				}
			}
		}
		return max;
	}

	private KeyValue getFirstValueOfMap(Map<Double, Double> map) {
		for (Double double1 : map.keySet()) {
			KeyValue keyValue = new KeyValue();
			keyValue.setKey(double1);
			keyValue.setValue(map.get(double1));
			return keyValue;
		}

		return null;
	}

	private List<String> readTemplate(File file) {
		List<String> result = new ArrayList<>();
		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(file));
			br.readLine();
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return result;
	}
	
	private int calculateXTikInterval(){
		return (int) Math.ceil((double)maxXValue / 20);
	}

	public String getxAxisTitle() {
		return xAxisTitle;
	}

	public void setxAxisTitle(String xAxisTitle) {
		this.xAxisTitle = xAxisTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public double getxAxisTikInterval() {
		return xAxisTikInterval;
	}

	public void setxAxisTikInterval(double xAxisTikInterval) {
		this.xAxisTikInterval = xAxisTikInterval;
	}

	public String getyAxisTitle() {
		return yAxisTitle;
	}

	public void setyAxisTitle(String yAxisTitle) {
		this.yAxisTitle = yAxisTitle;
	}

	public double getyAxisMinValue() {
		return yAxisMinValue;
	}

	public void setyAxisMinValue(double yAxisMinValue) {
		this.yAxisMinValue = yAxisMinValue;
	}

	public String getToolTipValueSuffix() {
		return toolTipValueSuffix;
	}

	public void setToolTipValueSuffix(String toolTipValueSuffix) {
		this.toolTipValueSuffix = toolTipValueSuffix;
	}

	public Map<String, Map<Double, Double>> getSeries() {
		return series;
	}

	public void setSeries(Map<String, Map<Double, Double>> series) {
		this.series = series;
		maxXValue = getMaxKeyValue(series);
	}

	private class KeyValue {
		private double key;
		private double value;

		public KeyValue() {
			super();
		}

		public KeyValue(Double key, Double value) {
			super();
			this.key = key;
			this.value = value;
		}

		public double getKey() {
			return key;
		}

		public void setKey(double key) {
			this.key = key;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

	}
}
