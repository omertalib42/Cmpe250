import java.util.PriorityQueue;

public class PhysioFacility {

    public PriorityQueue<Event> current, queue;
    public PriorityQueue<Therapist> therapists = new PriorityQueue<>(Simulation.compTherapist);
    public int limit;
    public int maxSize = 0;
    public double numberOfPhysios = 0;
    public double queueWaiting = 0;
    public double totalTime = 0;


    public PhysioFacility() {
        this.current = new PriorityQueue<>(Simulation.compPhysio);
        this.queue = new PriorityQueue<>(Simulation.compPhysio);
    }

    public void addPlayer(Event e) {
        Simulation.timePassed = e.startTime;
        if (e.player.status != Status.TRAINING && e.player.status != Status.PHYSIO_QUEUE) {
            // System.out.println("Cancelled physio attempt by " + e.player.id + " at " + e.startTime);
            Simulation.cancelledAttempts += 1;
            return;
        }
        if(current.size() == limit) {
            queue.add(e);
            // System.out.println("Added " + e.player.id + " to physio queue at " + e.startTime);
            e.player.status = Status.PHYSIO_QUEUE;
            maxSize = Math.max(maxSize, queue.size());
        } else {
            current.add(e);
            // System.out.println("Added " + e.player.id + " to physio at " + e.startTime);
            e.player.status = Status.PHYSIO;
            if(therapists.size() > 0) {
                Therapist therapist = therapists.poll();
                totalTime += therapist.time;
                Simulation.events.add(new Event(Service.THERAPY, e.id, e.startTime + therapist.time, therapist.time, e.player, Door.EXIT, e.eventID));
                e.player.therapist = therapist;
            }
        }
    }

    public void discardPlayer(Event e) {
        Simulation.timePassed = e.startTime;
        if(current.size() > 0) {
            current.poll();
            this.numberOfPhysios += 1;
            // System.out.println("Discarded " + e.player.id + " from physio at " + e.startTime);
            e.player.status = Status.FREE;
            therapists.add(e.player.therapist);
            if(queue.size() > 0) {
                Event queueEvent = queue.poll();
                queueWaiting += e.startTime - queueEvent.startTime;
                queueEvent.player.physioQueueTime += e.startTime - queueEvent.startTime;
                Simulation.events.add(new Event(Service.THERAPY, queueEvent.id, e.startTime, queueEvent.duration, queueEvent.player, Door.ENTER, queueEvent.eventID));
            }
        }
    }

}
