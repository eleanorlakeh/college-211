package com.java;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import jxl.Workbook;
import jxl.write.*;

/**
 * Utility methods for MatchingApplication.
 * @author Eleanor Harris (harri27e@mtholyoke.edu)
 * */

public class Utils {

  /** Reads a CSV file and produces a List of Students.
   */
  public static List<Student> readStudents(String filepath) {

    List<Student> students = new LinkedList<>();
    int counter = 0;

    try {
      BufferedReader CSVFile = new BufferedReader(new FileReader(filepath));

      String dataRow = CSVFile.readLine(); // Read first line
      
      while (dataRow != null) {
        if (counter != 0) {
        	
        	  //Split around commas, but ignore commas within double quotes
          String[] dataArray = dataRow.split(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");
          String email = dataArray[1];
          String discipline = dataArray[3];
          List<String> processes = makeListFromString(dataArray[4]);
          List<String> interests = makeListFromString(dataArray[5]);
          students.add(new Student(email, discipline, processes, interests)); // Add a student with the parsed data.
        }
        dataRow = CSVFile.readLine(); // Read next line of data.
        counter++;
      }
      CSVFile.close(); // Close the file once all data has been read.
    } catch (Exception e) {
      e.printStackTrace();
    }

    return students;
  }
  
  /** Given "a, b, c" returns a List containing elements "a", "b", and "c".
   */
  private static List<String> makeListFromString(String input) {
	  String[] elts = input.split(",");
	  for(int i = 0; i < elts.length; i++) {
		  String cur = elts[i];
		  if (cur.length() > 0 && cur.charAt(0) == '"') {
			cur = cur.substring(1);
		  }
		  if (cur.length() > 0 && cur.charAt(cur.length() - 1) == '"') {
		    	cur = cur.substring(0, cur.length() - 1);
		  }
		  elts[i] = cur;
	  }
	  return Arrays.asList(elts);
  }

  /**
   * Given a list of students, makes a graph for which two students are connected if they are a
   * possible match (aka they have no common interests).
   */
  public static UndirectedGraph<Student> makeGraphFromStudents(List<Student> students) {
    UndirectedGraph<Student> graph = new UndirectedGraph<Student>();
    for (int i = 0; i < students.size(); i++) {

      // Add node representing current student
      Student cur = students.get(i);
      graph.addNode(cur);

      // To that node, add all possible matches (aka, create edges)
      for (int j = i + 1; j < students.size(); j++) {
        Student possMatch = students.get(j);
        if (cur.isMatch(possMatch)) {
          graph.addNode(possMatch); // Will do nothing if node is already in graph
          graph.addEdge(cur, possMatch);
        }
      }
    }
    return graph;
  }

  /** Writes the matched graph to an Excel workbook at a given filepath. */
  public static void writeToWorkbook(UndirectedGraph<Student> matching, String loc) {

    WritableWorkbook workbook = null;
    try {
      workbook = Workbook.createWorkbook(new File(loc));

      // Create an Excel sheet and add column labels
      WritableSheet excelSheet = workbook.createSheet("Sheet 1", 0);
      excelSheet.addCell(new Label(0, 0, "Student"));
      excelSheet.addCell(new Label(1, 0, "Partner"));
      excelSheet.addCell(new Label(2, 0, "What they have in common"));
      
      Iterator<Student> students = matching.iterator();
      int counter = 1;
      while (students.hasNext()) {
        Student s = students.next();
        Object[] allEdges = matching.edgesFrom(s).toArray();
        excelSheet.addCell(new Label(0, counter, s.getEmail()));
        if (allEdges.length != 0) {
        	    Student sPartner = (Student) allEdges[0];
            excelSheet.addCell(new Label(1, counter, sPartner.getEmail()));
            excelSheet.addCell(new Label(2, counter, s.getCommonElts(sPartner)));
        }
        else {
        		excelSheet.addCell(new Label(1, counter, "NO MATCH"));
        }
        counter++;
      }

      workbook.write();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (workbook != null) {
        try {
          workbook.close();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (WriteException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
