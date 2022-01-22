
import java.util.HashSet;
import java.util.PriorityQueue;

public class TrainingFacility {

    public PriorityQueue<Event> current, queue;
    public int limit;
    public int maxSize = 0;
    public double numberOfTrainings = 0;
    public double queueWaiting = 0;
    public double totalTime = 0;
    HashSet<Integer> alreadyIn = new HashSet<>();


    public TrainingFacility() {
        this.current = new PriorityQueue<>(Simulation.compTraining);
        this.queue = new PriorityQueue<>(Simulation.compTraining);
    }

    public void addPlayer(Event e) {
        Simulation.timePassed = e.startTime;
        if (e.player.status != Status.FREE && e.player.status != Status.TRAINING_QUEUE) {
            // System.out.println("Cancelled training attempt by " + e.player.id + " at " + e.startTime);
            Simulation.cancelledAttempts += 1;
            return;
        }
        if(e.player.status == Status.TRAINING_QUEUE && !alreadyIn.contains(e.eventID)) {
            // System.out.println("Cancelled training attempt by " + e.player.id + " at " + e.startTime);
            Simulation.cancelledAttempts += 1;
            return;
        }
        if (current.size() == limit) {
            queue.add(e);
            // System.out.println("Added " + e.player.id + " to training queue at " + e.startTime);
            e.player.status = Status.TRAINING_QUEUE;
            alreadyIn.add(e.eventID);
            maxSize = Math.max(maxSize, queue.size());
        } else {
            current.add(e);
            // System.out.println("Added " + e.player.id + " to training at " + e.startTime);
            e.player.status = Status.TRAINING;
            totalTime += e.duration;
            alreadyIn.add(e.eventID);
            Simulation.events.add(new Event(Service.TRAINING, e.id, e.startTime + e.duration, e.duration, e.player, Door.EXIT, e.eventID));
        }
    }

    public void discardPlayer(Event e) {
        Simulation.timePassed = e.startTime;
        if(current.size() > 0) {
            current.poll();
            this.numberOfTrainings += 1;
            // System.out.println("Discarded " + e.player.id + " from training at " + e.startTime);
            alreadyIn.remove(e.eventID);
            Simulation.events.add(new Event(Service.THERAPY, e.id, e.startTime, e.duration, e.player, Door.ENTER, e.eventID));
            if(queue.size() > 0) {
                Event queueEvent = queue.poll();
                queueWaiting += e.startTime - queueEvent.startTime;
                Simulation.events.add(new Event(Service.TRAINING, queueEvent.id, e.startTime, queueEvent.duration, queueEvent.player, Door.ENTER, queueEvent.eventID));
            }
        }
    }

}
