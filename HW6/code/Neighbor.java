public class Neighbor {

    public String departuresID;
    public String arrivalsID;
    public int duration;

    public Neighbor(String departuresID, String arrivalsID, int duration) {
        this.departuresID = departuresID;
        this.arrivalsID = arrivalsID;
        this.duration = duration;
    }

    public Neighbor(String departuresID, String arrivalsID) {
        this(departuresID, arrivalsID, Subway.INF);
    }

}
