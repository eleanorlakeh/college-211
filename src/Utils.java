import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.sun.corba.se.spi.ior.Writeable;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

public class Utils {

    /** For now, dummy method. */
    public static List<Student> readStudents() {
        List<String> eleanorsInterests = Arrays.asList("art", "music");
        List<String> kaylasInterests = Arrays.asList("art", "policy");
        List<String> marysInterests = Arrays.asList("music");
        List<String> sabrinasInterests = Arrays.asList("nonprofit");
        Student eleanor = new Student("eleanor@", eleanorsInterests);
        Student kayla = new Student("kayla@", kaylasInterests);
        Student mary = new Student("mary@", marysInterests);
        Student sabrina = new Student("sabrina@", sabrinasInterests);
        List<Student> students = new LinkedList<>();
        students.addAll(Arrays.asList(eleanor, kayla, mary, sabrina));
        return students;
    }

    // Author: https://beginwithjava.blogspot.com/2011/05/java-csv-file-reader.html
    public static List<Student> readStudents(String filepath) {

        List<Student> students = new LinkedList<>();
        int counter = 0;

        try {
            BufferedReader CSVFile =
                    new BufferedReader(new FileReader(filepath));

            String dataRow = CSVFile.readLine(); // Read first line

            while (dataRow != null) {
                if (counter != 0) {
                    String[] dataArray = dataRow.split(",");
                    String email = dataArray[1];
                    List<String> interests = new LinkedList<>();
                    for(int i = 3; i < dataArray.length; i++) {
                        String interest = stripQuotes(dataArray[i]);
                        if(!interest.equals("Other")) {
                            interests.add(interest);
                        }
                    }
                    students.add(new Student(email, interests)); // Add a student with the parsed data.
                }
                dataRow = CSVFile.readLine(); // Read next line of data.
                counter++;
            }
            // Close the file once all data has been read.
            CSVFile.close();
        } catch(Exception e) {
            e.printStackTrace();
        }


        return students;
    }

    private static String stripQuotes(String s) {
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /** Given a list of students, makes a graph for which two students are connected
     * if they are a possible match (aka they have no common interests).
     */
    public static UndirectedGraph<Student> makeGraphFromStudents(List<Student> students) {
        UndirectedGraph<Student> graph = new UndirectedGraph<Student>();
        for (int i = 0; i < students.size(); i++) {
            Student cur = students.get(i);
            graph.addNode(cur);
            for (int j = i + 1; j < students.size(); j++) {
                Student possMatch = students.get(j);
                if (cur.isMatch(possMatch)) {
                    graph.addNode(possMatch);
                    graph.addEdge(cur, possMatch);
                }
            }
        }
        return graph;
    }

    public static void writeToWorkbook(UndirectedGraph<Student> matching, String loc){

        WritableWorkbook workbook = null;
        try{
            workbook = Workbook.createWorkbook(new File(loc));

            // create an Excel sheet
            WritableSheet excelSheet = workbook.createSheet("Sheet 1", 0);

            Label l1 = new Label(0, 0, "Student");
            excelSheet.addCell(l1);
            Label l2 = new Label(1, 0, "Partner");
            excelSheet.addCell(l2);

            System.out.println("added some stuff");

            Iterator<Student> students = matching.iterator();
            int counter = 1;
            while (students.hasNext()) {
                Student s = students.next();
                Object[] allEdges = matching.edgesFrom(s).toArray();
                Student sPartner = allEdges.length == 1 ? (Student)allEdges[0] : null;
                excelSheet.addCell(new Label(0, counter, s.toString()));
                excelSheet.addCell(new Label(1, counter, sPartner.toString()));
                counter++;
            }

            workbook.write();
        } catch(Exception e){
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
