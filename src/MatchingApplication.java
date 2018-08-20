import java.util.List;

public class MatchingApplication {

    public static void main(String[] args) {
        List<Student> students = Utils.readStudents("src/responses.csv");
        UndirectedGraph<Student> studentGraph = Utils.makeGraphFromStudents(students);
        UndirectedGraph<Student> matching = EdmondsMatching.maximumMatching(studentGraph);
        System.out.println(matching);
        Utils.writeToWorkbook(matching, "result.xls");
    }

}
