package edu.handong.analysis.datamodel;

public class Course {
	private String studentId;
	private String yearMonthGraduated;
	private String firstMajor;
	private String secondMajor;
	private String courseCode;
	private String courseName;
	private String courseCredit;
	private int yearTaken;
	private int semesterCourseTaken;
	
	public Course(String line) {
		String[] splitLine = line.split(", ");
		
		studentId = splitLine[0];
		yearMonthGraduated = splitLine[1];
		firstMajor = splitLine[2];
		secondMajor = splitLine[3];
		courseCode = splitLine[4];
		courseName = splitLine[5];
		courseCredit = splitLine[6];
		yearTaken = Integer.parseInt(splitLine[7]);
		semesterCourseTaken = Integer.parseInt(splitLine[8]);
	}
	
	public int getYearTaken() {
		return yearTaken;
	}
	
	public int getSemesterCourseTaken() {
		return semesterCourseTaken;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public String getStudentId() {
		return studentId;
	}

}
