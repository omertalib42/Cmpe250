import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Simulation {
    public static ArrayList<Player> players;
    public static PriorityQueue<Event> events;
    public static Facilities facility;
    public static Comparator<Event> compTraining = Comparator.comparing(Event::getStartTime).thenComparing(Event::getID);
    public static Comparator<Event> compMassage = Comparator.comparing(Event::getNegativeSkillLevel).thenComparing(Event::getStartTime).thenComparing(Event::getID);
    public static Comparator<Event> compPhysio = Comparator.comparing(Event::getNegativeDuration).thenComparing(Event::getStartTime).thenComparing(Event::getID);
    public static Comparator<Therapist> compTherapist = Comparator.comparing(Therapist::getID);

    public static int invalidAttempts = 0;
    public static int cancelledAttempts = 0;
    public static Player patient;
    public static double timePassed = 0.0;
    public static String[] args;

    public Simulation (ArrayList<Player> players, PriorityQueue<Event> events,
                       PriorityQueue<Therapist> therapists, Facilities facility, String[] args) {
        Simulation.players = players;
        Simulation.events = events;
        Simulation.facility = facility;
        Simulation.facility.physio.therapists = therapists;
        Simulation.patient = players.get(0);
        Simulation.args = args;
    }

    public void simulate() throws FileNotFoundException {
        while(events.size() > 0) {
            Event event = events.poll();
            if(event.door == Door.ENTER) {
                switch (event.type) {
                    case TRAINING -> facility.training.addPlayer(event);
                    case MASSAGE -> facility.massage.addPlayer(event);
                    default -> facility.physio.addPlayer(event);
                }
            }
            else {
                switch (event.type) {
                    case TRAINING -> facility.training.discardPlayer(event);
                    case MASSAGE -> facility.massage.discardPlayer(event);
                    default -> facility.physio.discardPlayer(event);
                }
            }
        }


//        PrintStream out = System.out;
        PrintStream out = new PrintStream(args[1]);


        out.println(facility.training.maxSize);
        out.println(facility.physio.maxSize);
        out.println(facility.massage.maxSize);
        out.printf("%.3f\n", facility.training.numberOfTrainings == 0 ? 0 :
                facility.training.queueWaiting / facility.training.numberOfTrainings);
        out.printf("%.3f\n", facility.physio.numberOfPhysios == 0 ? 0 :
                facility.physio.queueWaiting / facility.physio.numberOfPhysios);
        out.printf("%.3f\n", facility.massage.numberOfMassages == 0 ? 0 :
                facility.massage.queueWaiting / facility.massage.numberOfMassages);
        out.printf("%.3f\n", facility.training.numberOfTrainings == 0 ? 0 :
                facility.training.totalTime / facility.training.numberOfTrainings);
        out.printf("%.3f\n", facility.physio.numberOfPhysios == 0 ? 0 :
                facility.physio.totalTime / facility.physio.numberOfPhysios);
        out.printf("%.3f\n", facility.massage.numberOfMassages == 0 ? 0 :
                facility.massage.totalTime / facility.massage.numberOfMassages);
        out.printf("%.3f\n", facility.training.numberOfTrainings == 0 ? 0 :
                (facility.training.totalTime + facility.physio.totalTime
                + facility.training.queueWaiting + facility.physio.queueWaiting) / facility.training.numberOfTrainings);

        Simulation.players.sort(Comparator.comparing(Player::getNegativePhysioQueueTime).thenComparing(Player::getId));
        out.printf("%d %.3f\n", players.get(0).id, -1.0 * players.get(0).getNegativePhysioQueueTime());

        ArrayList<Player> maxClients = new ArrayList<>();
        for (Player player : players) {
            if(player.massageCount == 3) {
                maxClients.add(player);
            }
        }

        maxClients.sort(Comparator.comparing(Player::getMassageQueueTime).thenComparing(Player::getId));
        if(maxClients.size() == 0) {
            out.println(-1 + " " + -1);
        } else {
            out.printf("%d %.3f\n", maxClients.get(0).id, maxClients.get(0).massageQueueTime);
        }

        out.println(Simulation.invalidAttempts);
        out.println(Simulation.cancelledAttempts);
        out.printf("%.3f\n", Simulation.timePassed);

    }

}
