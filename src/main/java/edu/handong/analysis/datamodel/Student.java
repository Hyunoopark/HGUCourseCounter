package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken;
	private HashMap<String,Integer> semestersByYearAndSemester;
	
	public Student(String studentId) {
		this.studentId = studentId;
		
		coursesTaken = new ArrayList<Course>();
	}
	
	public void addCourse(Course newRecord) {		
		coursesTaken.add(newRecord);
	}
	
	public HashMap<String,Integer> getSemestersByYearAndSemester() {
		semestersByYearAndSemester = new HashMap<String,Integer>();
		int semester = 1;
		int getYear = coursesTaken.get(0).getYearTaken();
		int getSemester = coursesTaken.get(0).getSemesterCourseTaken();
		String key;
		
		for(Course aCourse : coursesTaken) {
			if(aCourse.getYearTaken() > getYear) {
				getYear = aCourse.getYearTaken();
				getSemester = aCourse.getSemesterCourseTaken();
				semester++;
			}
			if(aCourse.getSemesterCourseTaken() > getSemester) {
				getSemester = aCourse.getSemesterCourseTaken();
				semester++;
			}
			
			key = getYear + "-" + getSemester;
			
			semestersByYearAndSemester.put(key,semester);
		}
		
		return semestersByYearAndSemester;
	}
	
	public int getNumCourseInNthSemester(int semester) {
		int count = 0;
		int getYear = coursesTaken.get(0).getYearTaken();
		int getSemester = coursesTaken.get(0).getSemesterCourseTaken();
		ArrayList<String> key = new ArrayList<String>();
		
		for(Course aCourse : coursesTaken) {
			if(aCourse.getYearTaken() > getYear) {
				getYear = aCourse.getYearTaken();
				getSemester = aCourse.getSemesterCourseTaken();
			}
			if(aCourse.getSemesterCourseTaken() > getSemester) {
				getSemester = aCourse.getSemesterCourseTaken();
			}
			
			key.add(Integer.toString(getYear)+"-"+Integer.toString(getSemester));
		}
		
		for(String k:key) {
			if(getSemestersByYearAndSemester().get(k).equals(semester))
				count++;
		}
		
		return count;
	}
	
	public String getStudentId() {
		return studentId;
	}

}
