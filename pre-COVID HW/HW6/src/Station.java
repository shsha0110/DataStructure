import java.util.ArrayList;

public class Station {

    public String id;
    public String name;
    public String line;

    public Station (String id, String name, String line) {
        this.id = id;
        this.name = name;
        this.line = line;
    }

//    @Override
//    public int compareTo(Object o) {
//        if (o instanceof Station) {
//            Station station = (Station) o;
//            return id.compareTo(station.id);
//        }
//        return 0;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Station) {
            Station station = (Station) o;
            return id.equals(station.id) && name.equals(station.name) && line.equals(station.line);
        }
        return false;
    }

}
