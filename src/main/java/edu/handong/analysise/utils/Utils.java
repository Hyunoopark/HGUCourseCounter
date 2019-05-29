package edu.handong.analysise.utils;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

//import java.io.BufferedWriter;
//import java.io.BufferedReader;
//import java.io.File;
import java.io.FileNotFoundException;
//import java.io.Reader;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.List;

public class Utils {

	public static ArrayList<CSVRecord> getLines(String file, boolean removeHeader) throws FileNotFoundException, IOException {

		ArrayList<CSVRecord> lines = new ArrayList<CSVRecord>();
		
		try {
			CSVParser parser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT.withHeader()); 
			
			for (CSVRecord record : parser) 
				lines.add(record); 
	
		} catch (FileNotFoundException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}
		
		return lines;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		try (CSVPrinter printer = new CSVPrinter(new FileWriter(targetFileName), CSVFormat.DEFAULT)) {
		    for(String line:lines) {
		    	String[] aline = line.split(",");
		    	printer.printRecord(aline);
		    }
		    System.out.println("Finish!");
		 } catch (IOException ex) {
		     ex.printStackTrace();
		 }
	}

}
