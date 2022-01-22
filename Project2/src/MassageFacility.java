import java.util.HashSet;
import java.util.PriorityQueue;

public class MassageFacility {

    public PriorityQueue<Event> current, queue;
    public int limit;
    public int maxSize = 0;
    public double numberOfMassages = 0;
    public double queueWaiting = 0;
    public double totalTime = 0;
    HashSet<Integer> alreadyIn = new HashSet<>();


    public MassageFacility() {
        this.current = new PriorityQueue<>(Simulation.compMassage);
        this.queue = new PriorityQueue<>(Simulation.compMassage);
    }

    public void addPlayer(Event e) {
        Simulation.timePassed = e.startTime;
        if (e.player.massageCount == 3) {
            if(!alreadyIn.contains(e.eventID)) {
                // System.out.println("Invalid massage attempt by " + e.player.id + " at " + e.startTime);
                Simulation.invalidAttempts += 1;
                return;
            }
        }
        if (e.player.status != Status.FREE && e.player.status != Status.MASSAGE_QUEUE) {
            // System.out.println("Cancelled massage attempt by " + e.player.id + " at " + e.startTime);
            Simulation.cancelledAttempts += 1;
            return;
        }
        if(e.player.status == Status.MASSAGE_QUEUE && !alreadyIn.contains(e.eventID)) {
            // System.out.println("Cancelled massage attempt by " + e.player.id + " at " + e.startTime);
            Simulation.cancelledAttempts += 1;
            return;
        }
        if(e.player.status != Status.MASSAGE_QUEUE) {
            e.player.massageCount += 1;
        }
        if (current.size() == limit) {
            queue.add(e);
            // System.out.println("Added " + e.player.id + " to massage queue at " + e.startTime);
            e.player.status = Status.MASSAGE_QUEUE;
            alreadyIn.add(e.eventID);
            maxSize = Math.max(maxSize, queue.size());
        } else {
            current.add(e);
            // System.out.println("Added " + e.player.id + " to massage at " + e.startTime);
            e.player.status = Status.MASSAGE;
            totalTime += e.duration;
            alreadyIn.add(e.eventID);
            Simulation.events.add(new Event(Service.MASSAGE, e.id, e.startTime + e.duration, e.duration, e.player, Door.EXIT, e.eventID));
        }
    }

    public void discardPlayer(Event e) {
        Simulation.timePassed = e.startTime;
        if(current.size() > 0) {
            current.poll();
            this.numberOfMassages += 1;
            // System.out.println("Discarded " + e.player.id + " from massage at " + e.startTime);
            alreadyIn.remove(e.eventID);
            e.player.status = Status.FREE;
            if(queue.size() > 0) {
                Event queueEvent = queue.poll();
                queueWaiting += e.startTime - queueEvent.startTime;
                queueEvent.player.massageQueueTime += e.startTime - queueEvent.startTime;
                Simulation.events.add(new Event(Service.MASSAGE, queueEvent.id, e.startTime, queueEvent.duration, queueEvent.player, Door.ENTER, queueEvent.eventID));
            }
        }
    }

}
