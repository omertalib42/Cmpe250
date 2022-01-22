
public class Player {
    public int id, skill;
    public Status status;
    public int massageCount = 0;
    public double physioQueueTime = 0;
    public double massageQueueTime = 0;
    public Therapist therapist;

    public Player(int id, int skill) {
        this.id = id;
        this.skill = skill;
        this.status = Status.FREE;
    }

    public int getId() {
        return this.id;
    }

    public double getNegativePhysioQueueTime() {
        return - this.physioQueueTime;
    }

    public double getMassageQueueTime() {
        return this.massageQueueTime;
    }

}
