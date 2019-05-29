package edu.handong.analysis;

//import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVRecord;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
//import edu.handong.analysise.utils.NotEnoughArgumentException;
import edu.handong.analysise.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	private ArrayList<Course> courses;
	private String dataPath;// csv file to be analyzed
	private String resultPath; // the file path where the results are saved.
	private int startYear;
	private int endYear;	
	private int analysis;
	private String courseCode = null;
	private boolean help;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 * @throws Exception 
	 */
	public void run(String[] args) throws Exception{
		
		Options options = createOptions();
		
		if(parseOption(options, args)){
			if (help){
				System.out.println("help");
				printHelp(options);
				System.exit(0);
			}
			
			else if(analysis == 1){
			
				ArrayList<CSVRecord> lines = Utils.getLines(dataPath, true);
				
				try {
			
				students = loadStudentCourseRecords(lines);
			
				// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
				Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
			
				// Generate result lines to be saved.
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
			
				// Write a file (named like the value of resultPath) with linesTobeSaved.
				Utils.writeAFile(linesToBeSaved, resultPath);
				
				} catch (Exception e) {
					printHelp(options);
					System.exit(0);
				}
			}
			
			else if(analysis == 2) {
				
				ArrayList<CSVRecord> lines = Utils.getLines(dataPath, true);
				
				if(courseCode == null) {
					System.out.println("Error. input option -c");
					printHelp(options);
					System.exit(0);
				}
				
				try {
				
				students = loadStudentCourseRecords(lines);
				
				Map<String, Student> sortedStudents = new TreeMap<String,Student>(students);
				
				courses = loadCourse(lines);
				
				ArrayList<String> linesToBeSaved = countPerCourseNameAndYear(courses, sortedStudents);
				
				Utils.writeAFile(linesToBeSaved, resultPath);	
				
				} catch (Exception e) {
					printHelp(options);
					System.exit(0);
				}
				
			}
			
			else {
				System.out.println("Your input is wrong. please input analysis 1 or 2");
				System.exit(0);
			}
			
		}
	}
	
	private boolean parseOption(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			dataPath = cmd.getOptionValue("i");
			resultPath = cmd.getOptionValue("o");
			analysis = Integer.parseInt(cmd.getOptionValue("a"));	
			courseCode = cmd.getOptionValue("c");		
			startYear = Integer.parseInt(cmd.getOptionValue("s"));
			endYear = Integer.parseInt(cmd.getOptionValue("e"));
			help = cmd.hasOption("h");
					
		} catch(Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;
		
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer = "";
		formatter.printHelp("HGU Course Counter",header,options,footer,true);
	}

	private Options createOptions() {
		Options options = new Options();
		
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());
		 
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg()
				.argName("Analysis option")
				.required()
				.build());
		
		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg()
				.argName("Course code")
				.build());
		
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()
				.argName("Start year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g., -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page")
				.argName("Help")
				.build());
		
		return options;
	}

	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<CSVRecord> lines) {
		students = new HashMap<String,Student>();
		
		Student aStudent = new Student(lines.get(0).get(0));
		
		for(CSVRecord aline:lines) {	
	
				Course aCourse = new Course(aline);

				if(aStudent.getStudentId().equals(aCourse.getStudentId()) && (aCourse.getYearTaken()>=startYear && aCourse.getYearTaken()<=endYear)){
					aStudent.addCourse(aCourse);
					students.put(aStudent.getStudentId(),aStudent);
				}
				
				else {
					aStudent = new Student(aCourse.getStudentId());
					aStudent.addCourse(aCourse);
				}
		}
		
		return students;
	}
	
	private ArrayList<Course> loadCourse(ArrayList<CSVRecord> lines){
		
		ArrayList<Course> thisCourse = new ArrayList<Course>();
		
		for(CSVRecord aLine:lines) {
			Course aCourse = new Course(aLine);
			
			if(aCourse.getCourseCode().equals(courseCode))
				thisCourse.add(aCourse);
		}
		
		return thisCourse;
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		ArrayList<String> coursesTakenInEachSemester = new ArrayList<String>();
		String st = null;
		
		coursesTakenInEachSemester.add("studentID,totalNumberOfSemester,Semester,NumCoursesTakenInTheSemester");
			
		for (String k : sortedStudents.keySet()) {
		    Student s = sortedStudents.get(k);
		    
		    for(int i = 1; i <= s.getSemestersByYearAndSemester().size(); i++) {
		    	st = k + "," + s.getSemestersByYearAndSemester().size() + "," + i + "," + s.getNumCourseInNthSemester(i);
			    coursesTakenInEachSemester.add(st);
		    }
		}
		
		return coursesTakenInEachSemester; 
	}
	
	private ArrayList<String> countPerCourseNameAndYear(ArrayList<Course> courses, Map<String, Student> sortedStudents) {
		ArrayList<String> countCourse = new ArrayList<String>();
		countCourse.add("Year,Semester,CouseCode,CourseName,TotalStudents,StudentsTaken,Rate");
		int semester = 1;
		
		String s = null;
		
		for(int i = startYear; i <= endYear; i++) {
			for(int j = semester; j <= 4; j++) {
				float total = 0;
				int studentsInSemester = 0 ;
			
				//각 년도와 학기에 수강 신청한 totalstudent 구하기 
				for(String key:sortedStudents.keySet()) {
					Student student = sortedStudents.get(key);
				
					for(Course c:student.getCoursesTaken()) {
						if(c.getYearTaken() == i && c.getSemesterCourseTaken() == j) {
							total++;
							break;
						}
					}
				}
			
				//각 semester별 해당 과목 수강생 파악 
			
				for(Course c: courses) {
					if(c.getYearTaken() == i) {
							if(c.getSemesterCourseTaken() == j)
								studentsInSemester++;	
					}
				}
				
				s = i + "," + j + "," + courseCode + "," + courses.get(0).getCourseName() + "," + (int)total + "," + studentsInSemester + ",";
				if(total == 0)
					s+= "0%";
				else {
					s += String.format("%.1f",(float)(studentsInSemester/total)*100.0);
					s += "%";
				}
				countCourse.add(s);
			}
		}
		return countCourse;
	}
}
