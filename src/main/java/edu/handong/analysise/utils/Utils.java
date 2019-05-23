package edu.handong.analysise.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {

	public static ArrayList<String> getLines(String file, boolean removeHeader) {
		File csvFile = new File(file);
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			FileReader in = new FileReader(csvFile);
			BufferedReader bufReader = new BufferedReader(in);
			String line = null;
		
            while((line = bufReader.readLine()) != null) {
            	lines.add(line);
            }
            if(removeHeader)
            	lines.remove(0);
			
			bufReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Error opening the File " + csvFile);
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Error IO exception " + csvFile);
			System.exit(0);
		}

		return lines;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		File result = new File(targetFileName);
		result.getParentFile().mkdirs();
		
		FileWriter outputStream = null;

		try {
			result.createNewFile();
			outputStream = new FileWriter(result);
			
		} catch (IOException e) {
			System.out.println("IOException Error");
			System.exit(0);
		}
		
		try {
		outputStream.append("studentID");
		outputStream.append(",");
		outputStream.append("totalNumberOfSemester");
		outputStream.append(",");
		outputStream.append("Semester");
		outputStream.append(",");
		outputStream.append("NumCoursesTakenInTheSemester");
		outputStream.append("\n");
		
		for(String aline:lines) {
			outputStream.write(aline);
			outputStream.write("\n");
		}
		
		System.out.println("Success!");
		outputStream.close();
		
		} catch (IOException e) {
			System.out.println("IOException Error");
			System.exit(0);
		}
		
	}

}
