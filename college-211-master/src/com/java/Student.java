package com.java;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data structure to represent each student. Includes identifier and interests.
 * @author Eleanor Harris (harri27e@mtholyoke.edu)
 * */

public class Student {
  private String email;
  private String discipline;
  private List<String> processes;
  private List<String> occupationals;
  
  /** Public constructor. */
  public Student(String email, String discipline, List<String> processes, List<String> occupationals) {
    this.email = email;
    this.discipline = discipline;
    this.processes = processes;
    this.occupationals = occupationals;
  }
  
  /** Getter method to access email. */
  public String getEmail() {
	  return email;
  }
  
  /** Getter method to access discipline. */
  public String getDiscipline() {
	  return discipline;
  }
  
  /** Getter method to access process list. */
  public List<String> getProcesses() {
	  return processes;
  }
  
  /** Getter method to access process list. */
  public String getProcessesString() {
	  return processes.toString().substring(1, processes.toString().length()-1);
  }
  
  /** Getter method to access occupation list as String. */
  public List<String> getOccupationals() {
	  return occupationals;
  }
  
  /** Getter method to access process list as String. */
  public String getOccupationalsString() {
	  return occupationals.toString().substring(1, occupationals.toString().length()-1);
  }

  /** Returns true if the current Student has no common interests with a given Student s. */
  public boolean isMatch(Student s) {
	boolean differentDisciplines = !s.getDiscipline().equals(this.discipline);
	boolean commonProcess = !Collections.disjoint(s.getProcesses(), this.processes);
	boolean commonOccupation = !Collections.disjoint(s.getOccupationals(), this.occupationals);
    return differentDisciplines && (commonProcess || commonOccupation);
  }
  
  /**
   * Return the common interests between two students as a readable String.
   */
  public String getCommonElts(Student s) {
	  List<String> commonProcesses = new ArrayList<String>(processes);
	  commonProcesses.retainAll(s.getProcesses());
	  List<String> commonOccupationals = new ArrayList<String>(occupationals);
	  commonOccupationals.retainAll(s.getOccupationals());
	  commonOccupationals.addAll(commonProcesses);
	  return commonOccupationals.toString().substring(1, commonOccupationals.toString().length()-1);
  }

  /** Returns email to identify Student, since email is a unique identifier in MHC system. */
  @Override
  public String toString() {
    return email;
  }
}
