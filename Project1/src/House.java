
public class House implements Comparable<House> {
    // The instances of House class as desired.
    public int id, duration;
    public double rating;

    // Constructor.
    public House(int id, int duration, double rating){
        this.id = id;
        this.duration = duration;
        this.rating = rating;
    }

    // compareTo method for sorting functions.
    public int compareTo(House h){
        return this.id - h.id;
    }

}
