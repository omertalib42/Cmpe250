import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;

public class project1main {
    public static void main(String[] args) {

        // java.util.Scanner for the input.
        Scanner in;
        // java.io.PrintStream for the output.
        PrintStream out;
        // Try-Catch block for inconvenient file & argument issues.
        try {
            in = new Scanner(new FileReader(args[0]));
            out = new PrintStream(args[1]);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        }

        // Storing House and Student objects in ArrayLists.
        ArrayList<House> houses = new ArrayList<>();
        Set<Student> students = new TreeSet<>();

        // Reading the file while there are remaining elements.
        while (in.hasNext()) {
            // Input phase.
            String type = in.next();
            int id = in.nextInt();
            String name = "";
            if (type.equals("s")) {
                name = in.next();
            }
            int duration = in.nextInt();
            double rating = in.nextDouble();
            if (type.equals("h")) {
                House house = new House(id, duration, rating);
                houses.add(house);
            } else {
                Student student = new Student(id, duration, rating, name);
                students.add(student);
            }
        }

        // Sorting Houses by their id's.
        // Check House.java - compareTo() method for more information.
        Collections.sort(houses);

        // In each semester (from 1 to 8)
        for (int i = 1; 8 >= i; i++) {
            // Check each house
            for (House house : houses) {
                // If that house is full, continue with other houses.
                if (house.duration > 0) {
                    house.duration--;
                    continue;
                }
                // If not, try to place each remaining student.
                for (Student student : students) {
                    // If that student's rating is higher than house's rating or that student is graduated
                    // Do nothing.
                    if (student.rating > house.rating || i > student.duration) {
                        continue;
                    }
                    // Place that student to that house.
                    students.remove(student);
                    house.duration = student.duration - i;
                    break;
                }
            }
        }

        // Print the remaining students.
        for(Student student : students) {
            out.println(student.name);
        }
    }
}
