package com.elance.charts.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.elance.charts.records.Gender;
import com.elance.charts.records.Record;
import com.elance.charts.records.Units;

public class CSVParser {
	
	private File file;
	
	public CSVParser(File filePath){
		this.file = filePath;
	}
	
	public List<Record> parseData(){
		List<Record> result = new ArrayList<>();
		
		int line = 1;//header line
		for (String[] row : readCsvFile(file)) {
			line++;
			try {
				Record record = parseRow(row);
				result.add(record);
			} catch (IndexOutOfBoundsException e) {
				System.err.format("incorrect data in file: %s, line:%s", file,line);
			}
			
		}
		
		return result;
	}

	/**
	 * header: Gender,First Name,Lastname
	 * ,Opened,TimeToOpen,Clicked,TimeToClick,Units
	 * 
	 * @param row
	 * @return
	 */
	private Record parseRow(String[] row) throws IndexOutOfBoundsException {
		Record record = new Record();

		record.setGender(Gender.parseGender(row[0]));

		record.setFirstName(row[1]);
		record.setSecondName(row[2]);

		record.setOpened(row[3]);
		record.setTimeToOpen(row[4]);

		record.setClicked(row[5]);
		record.setTimeToClick(row[6]);

		record.setUnits(Units.parseUnits(row[7]));

		return record;
	}

	private List<String[]> readCsvFile(File file) {
		List<String[]> result = new ArrayList<String[]>();
		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(file));
			// Skip the header
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] textline = line.split(",");
				result.add(textline);
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
}
