import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Subway {

    public static List<Station> Stations = new ArrayList<>();
    public static Map<String, List<Neighbor>> Neighbors = new HashMap<>();
    public static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        // TASK1 ) Build Stations
        buildStations();
        // TASK2 ) Build Neighbors
        buildNeighbors();
        // TASK3 ) Read input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // TASK4 ) Process input
        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0) { break; }
                else { command(input); }
            }
            catch (IOException e) { }
        }
    }

    /** 1. Build Stations **/
    private static void buildStations() {
        // TASK1 ) Load each station data
        List<String[]> stationData = getStationData();
        // TASK2 ) Create Station object, iterating through stationData
        createStationObjects(stationData);
    }

    private static List<String[]> getStationData() {
        // TASK1 ) Initialize station data list
        List<String[]> stationData = new ArrayList<>();
        // TASK1 ) Open file
        File stationDataFile = new File("seoul.txt");
        // TASK2 ) Read file
        try (Scanner input = new Scanner(stationDataFile)) {
            while (input.hasNext()) {
                String line = input.nextLine();
                // TASK2.1 ) If line is empty(end of first paragraph), break
                if (line.isEmpty() || line.isBlank()) {
                    break;
                }
                // TASK2.2 ) Split a line as three tokens(id-name-line)
                String[] information = line.split("\\s");
                // TASK2.3 ) Add information in list
                stationData.add(information);
            }
        } catch (IOException e) {

        }
        return stationData;
    }

    private static void createStationObjects(List<String[]> stationData) {
        for (String[] data : stationData) {
            // TASK1 ) Segregate data
            String id = data[0];
            String name = data[1];
            String line = data[2];
            // TASK2 ) Create Station object
            Station newStation = new Station(id, name, line);
            // TASK3 ) Add Station object in Stations
            Stations.add(newStation);
            // TASK4 ) Add Station id in Neighbors as key, and initialize value
            Neighbors.put(newStation.id, new ArrayList<Neighbor>());
        }
    }

    /** 2. Build Neighbors **/
    private static void buildNeighbors() {
        // TASK1 ) Load each neighbor data
        List<String[]> neighborData = getNeighborData();
        // TASK2 ) Create Station object, iterating through stationData
        createNeighborObjects(neighborData);
    }

    private static List<String[]> getNeighborData() {
        // TASK1 ) Initialize station data list
        List<String[]> neighborData = new ArrayList<>();
        // TASK1 ) Open file
        File neighborDataFile = new File("seoul.txt");
        // TASK2 ) Read file
        try (Scanner input = new Scanner(neighborDataFile)) {
            // TASK2.1 ) Skip paragraph of station data
            while (input.hasNext()) {
                String line = input.nextLine();
                // TASK2.1 ) If line is empty(end of first paragraph), break
                if (line.isEmpty() || line.isBlank()) {
                    break;
                }
            }
            // TASK2.2 ) Process neighbor data
            while (input.hasNext()) {
                String line = input.nextLine();
                // TASK2.2.1 ) Split a line as three tokens(departures id-arrivals id-duration)
                String[] information = line.split("\\s");
                // TASK2.3 ) Add information in list
                neighborData.add(information);
            }
        } catch (IOException e) {

        }
        return neighborData;
    }

    private static void createNeighborObjects(List<String[]> neighborData) {
        for (String[] data : neighborData) {
            // TASK1 ) Segregate data
            String departuresID = data[0];
            String arrivalsID = data[1];
            int duration = Integer.parseInt(data[2]);
            // TASK2 ) Create Neighbor object
            Neighbor newNeighbor = new Neighbor(departuresID, arrivalsID, duration);
            // TASK3 ) Add Neighbor object in Neighbors
            Neighbors.get(departuresID).add(newNeighbor);
        }
    }

    /** 3. Process command **/
    private static void command(String input) {
        // TASK1 ) Parsing input, departures and arrivals
        Neighbor pair = parseInput(input);
        // TASK2 ) Find the shortest path using Dijkstra algorithm
        Stack<Neighbor> shortestPath = findShortestPath(pair);
        // TASK3 ) Print path
        printPath(shortestPath);
        // TASK4 ) Print duration
        printDuration(shortestPath);
    }

    private static Neighbor parseInput(String input) {
        // TASK1 ) Split input, delimited by a space
        String[] stationNames = input.split("\\s");
        String departuresName = stationNames[0];
        String arrivalsName = stationNames[1];
        // TASK2 ) Find Station object corresponding to station name
        Station departures = findStationByName(departuresName);
        Station arrivals = findStationByName(arrivalsName);
        // TASK3 ) Create Neighbor object
        Neighbor pair = new Neighbor(departures.id, arrivals.id);
        return pair;
    }

    private static Stack<Neighbor> findShortestPath(Neighbor pair) {
        Station departures = findStationByID(pair.departuresID);
        Station arrivals = findStationByID(pair.arrivalsID);
        Stack<Neighbor> shortedPath = dijkstra(departures, arrivals);
        return shortedPath;
    }

    private static Stack<Neighbor> dijkstra(Station departures, Station arrivals) {
        Map<Station, Boolean> visited = new HashMap<>();
        Map<Station, Integer> distance = new HashMap<>();
        Map<Station, Stack<Neighbor>> path = new HashMap<>();

        for (Station station : Stations) {
            visited.put(station, false);
            distance.put(station, INF);
            Stack<Neighbor> pathStack = new Stack<>();
            pathStack.push(new Neighbor(null, null, Integer.MIN_VALUE));
            path.put(station, pathStack);
        }

        dijkstraHelp(departures, arrivals, visited, distance, path);

        return path.get(arrivals);
    }

    private static void dijkstraHelp(Station departures, Station arrivals, Map<Station, Boolean> visited, Map<Station, Integer> distance, Map<Station, Stack<Neighbor>> path) {
        visited.put(departures, true);
        distance.put(departures, 0);

        List<Neighbor> neighborsOfDepartures = Neighbors.get(departures.id);
        for (Neighbor neighbor : neighborsOfDepartures) {
            Station neighborStation = findStationByID(neighbor.arrivalsID);
            distance.put(neighborStation, neighbor.duration);
        }

        for (int trials = 0; trials < Stations.size()-1; trials++) {
            Station currStation = closestStation(visited, distance);
            visited.put(currStation, true);

            System.out.println(currStation.name);

            if (currStation.equals(arrivals)) {
                return;
            }

            List<Neighbor> neighborsOfCurrentStation = Neighbors.get(currStation.id);
            for (Neighbor neighbor : neighborsOfCurrentStation) {
                Station neighborStation = findStationByID(neighbor.arrivalsID);
                int cost = distance.get(currStation) + neighbor.duration;
                if (cost < distance.get(neighborStation)) {
                    distance.put(neighborStation, cost);
                    path.get(neighborStation).pop();
                    path.get(neighborStation).push(neighbor);
                } else {
                    path.get(neighborStation).push(new Neighbor(null, null, Integer.MIN_VALUE));
                }
            }
        }
    }

    private static Station closestStation(Map<Station, Boolean> visited, Map<Station, Integer> distance) {
        int minCost = INF;
        Station closestStation = null;
        for (Station station : Stations) {
            if ((!visited.get(station)) && (distance.get(station) < minCost)) {
                minCost = distance.get(station);
                closestStation = station;
            }
        }
        return closestStation;
    }

    private static void printPath(Stack<Neighbor> shortestPath) {
        List<Neighbor> path = new ArrayList<>(shortestPath);
        String result = findStationByID(path.get(0).arrivalsID).name;
        for (Neighbor log : path) {
            if (log.departuresID == null || log.arrivalsID == null) {
                continue;
            }
            Station departures = findStationByID(log.departuresID);
            Station arrivals = findStationByID(log.arrivalsID);
            String departuresLine = departures.line;
            String arrivalsLine = arrivals.line;
            if (!departuresLine.equals(arrivalsLine)) {
                result += String.format(" [%s]", arrivals.name);
            } else {
                result += String.format(" %s", arrivals.name);
            }
        }
        System.out.println(result);
    }

    private static void printDuration(Stack<Neighbor> shortestPath) {
        List<Neighbor> path = new ArrayList<>(shortestPath);
        int result = 0;
        for (Neighbor log : path) {
            if (log.duration < 0) {
                continue;
            }
            result += log.duration;
        }
        System.out.println(result);
    }

    /** 4. etc **/
    private static Station findStationByName(String stationName) {
        for (Station station : Stations) {
            if (stationName.equals(station.name)) {
                return station;
            }
        }
        return null;
    }

    private static Station findStationByID(String stationID) {
        for (Station station : Stations) {
            if (stationID.equals(station.id)) {
                return station;
            }
         }
        return null;
    }
}
