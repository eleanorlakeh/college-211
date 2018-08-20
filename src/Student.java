import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Student{
    private String email;
    private List<String> interests;
    public Student match;

    // Public constructor.
    public Student(String email, List<String> interests) {
        this.email = email;
        this.interests = interests;
    }

    // Interest getter method.
    public List<String> getInterests(){
        return interests;
    }

    // Gets a match. If no match, returns null.
    public Student getMatch() { return match; }

    // Assigns a match.
    public void setMatch(Student s) {
        this.match = s;
        s.match = this;
    }

    // Returns true if the Student has no common interests with a given Student s.
    public boolean isMatch(Student s) {
        return Collections.disjoint(s.getInterests(), interests);
    }


    @Override
    public String toString() {
        return email;
    }
}