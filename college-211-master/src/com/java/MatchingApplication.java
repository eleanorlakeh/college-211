package com.java;
import java.util.List;

/**
 * Application to make pairs of students in College 211, based on chosen career areas.
 * @author Eleanor Harris (harri27e@mtholyoke.edu)
 * */

public class MatchingApplication {

  // Command line arguments: path/to/student/data, desired/path/to/result/sheet
  public static void main(String[] args) {
    List<Student> students = Utils.readStudents("src/com/resources/student_responses.csv");
    UndirectedGraph<Student> studentGraph = Utils.makeGraphFromStudents(students);
    UndirectedGraph<Student> matching = EdmondsMatching.maximumMatching(studentGraph);
    Utils.writeToWorkbook(matching, "new_result.csv");
  }
}