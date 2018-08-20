import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.sun.corba.se.spi.ior.Writeable;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

/**
 * Utility methods for MatchingApplication.
 * @author Eleanor Harris (harri27e@mtholyoke.edu)
 * */

public class Utils {

  /* Reads a CSV file and produces a List of Students.
   * Code partially sourced from beginwithjava.blogspot.com/2011/05/java-csv-file-reader.html.
   */
  public static List<Student> readStudents(String filepath) {

    List<Student> students = new LinkedList<>();
    int counter = 0;

    try {
      BufferedReader CSVFile = new BufferedReader(new FileReader(filepath));

      String dataRow = CSVFile.readLine(); // Read first line

      while (dataRow != null) {
        if (counter != 0) {
          String[] dataArray = dataRow.split(",");
          String email = dataArray[1];
          List<String> interests = new LinkedList<>();
          for (int i = 3; i < dataArray.length; i++) {
            String interest = stripQuotes(dataArray[i]);
            if (!interest.equals("Other")) {
              interests.add(interest);
            }
          }
          students.add(new Student(email, interests)); // Add a student with the parsed data.
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

  /** Private helper method to remove double quotes from before or after a String. */
  private static String stripQuotes(String s) {
    if (s.length() > 0 && s.charAt(0) == '"') {
      s = s.substring(1);
    }
    if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
      s = s.substring(0, s.length() - 1);
    }
    return s;
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

      Iterator<Student> students = matching.iterator();
      int counter = 1;
      while (students.hasNext()) {
        Student s = students.next();
        Object[] allEdges = matching.edgesFrom(s).toArray();
        Student sPartner = allEdges.length == 1 ? (Student) allEdges[0] : null;
        excelSheet.addCell(new Label(0, counter, s.toString()));
        excelSheet.addCell(new Label(1, counter, sPartner.toString()));
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
