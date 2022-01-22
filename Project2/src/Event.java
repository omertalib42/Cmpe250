
public class Event {
    public int id;
    public double startTime, duration, endTime;
    public Service type;
    public Player player;
    public Door door;
    public int eventID;

    public Event(String type, int id, double startTime, double duration, Player player, Door door, int eventID) {
        this.type = determineType(type);
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime + duration;
        this.player = player;
        this.door = door;
        this.eventID = eventID;
    }

    public Event(Service type, int id, double startTime, double duration, Player player, Door door, int eventID) {
        this.type = type;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime + duration;
        this.player = player;
        this.door = door;
        this.eventID = eventID;
    }

    public Service determineType(String type) {
        return switch (type) {
            case "t" -> Service.TRAINING;
            case "m" -> Service.MASSAGE;
            default -> Service.THERAPY;
        };
    }

    public String toString() {
        return this.id + " " + this.startTime + " " + this.duration + " " + this.type + " " + this.door;
    }

    public double getStartTime() {
        return this.startTime;
    }

    public Double getNegativeDuration() {
        return - this.duration;
    }

    public int getID() {
        return this.id;
    }

    public int getNegativeSkillLevel() {
        return - this.player.skill;
    }

}

