
public class Student implements Comparable<Student>{
    // The instances of Student class as desired.
    public int id, duration;
    public double rating;
    public String name;

    // Constructor.
    public Student(int id, int duration, double rating, String name){
        this.id = id;
        this.duration = duration;
        this.rating = rating;
        this.name = name;
    }

    // compareTo method for sorting functions.
    public int compareTo(Student s){
        return this.id - s.id;
    }

}
