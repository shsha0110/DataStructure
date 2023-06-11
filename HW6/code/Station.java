public class Station {
    public String id;
    public String name;
    public String line;
    public int transferTime;
    public boolean isTransferStation;

    public Station (String id, String name, String line) {
        this.id = id;
        this.name = name;
        this.line = line;
        this.transferTime = Integer.MAX_VALUE;
        this.isTransferStation = false;
    }

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
