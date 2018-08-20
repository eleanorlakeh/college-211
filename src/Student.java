import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Data structure to represent each student. Includes identifier and interests.
 * @author Eleanor Harris (harri27e@mtholyoke.edu)
 * */

public class Student {
  private String email;
  private List<String> interests;

  /** Public constructor. */
  public Student(String email, List<String> interests) {
    this.email = email;
    this.interests = interests;
  }

  /** Getter method to access interest list. */
  public List<String> getInterests() {
    return interests;
  }

  /** Returns true if the current Student has no common interests with a given Student s. */
  public boolean isMatch(Student s) {
    return Collections.disjoint(s.getInterests(), interests);
  }

  /** Returns email to identify Student, since email is a unique identifier in MHC system. */
  @Override
  public String toString() {
    return email;
  }
}
