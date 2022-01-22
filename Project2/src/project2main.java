import java.io.File;
import java.io.IOException;
import java.util.*;

public class project2main {
    public static void main(String[] args) throws IOException {

        Scanner reader = new Scanner(new File(args[0]));

        int n = reader.nextInt();
        ArrayList<Player> players = new ArrayList<>();
        for(int i = 0;n > i;i++){
            int playerID = reader.nextInt();
            int skillLevel = reader.nextInt();
            Player player = new Player(playerID, skillLevel);
            players.add(player);
        }

        int arrivals = reader.nextInt();
        PriorityQueue<Event> events = new PriorityQueue<>(Simulation.compTraining);
        for(int i = 0;arrivals > i;i++){
            String type = reader.next();
            int playerID = reader.nextInt();
            double startTime = reader.nextDouble();
            double duration = reader.nextDouble();
            Event event = new Event(type, playerID, startTime, duration, players.get(playerID), Door.ENTER, i);
            events.add(event);
        }

        int therapistCount = reader.nextInt();
        PriorityQueue<Therapist> therapists = new PriorityQueue<>(Comparator.comparing(Therapist::getID));
        for(int i = 0;therapistCount > i;i++){
            double x = reader.nextDouble();
            Therapist therapist = new Therapist(i, x);
            therapists.add(therapist);
        }

        Facilities facility = new Facilities();

        facility.training.limit = reader.nextInt();
        facility.massage.limit = reader.nextInt();
        facility.physio.limit = therapists.size();
        facility.physio.therapists = therapists;

        Simulation simulation = new Simulation(players, events, therapists, facility, args);
        simulation.simulate();




    }
}
