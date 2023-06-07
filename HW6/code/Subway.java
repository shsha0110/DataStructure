import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Subway {

    public static List<Station> Stations = new ArrayList<>();
    public static Map<String, List<Edge>> Neighbors = new HashMap<>();
    public static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        // TASK1 ) Process subway line data
        String targetFilePath = args.length == 1 ? args[0] : "";
        List<String[]>[] data = processSubwayLineData(targetFilePath);
        // TASK1 ) Build Stations
        List<String[]> stationData = data[0];
        buildStations(stationData);
        // TASK2 ) Build Neighbors
        List<String[]> neighborData = data[1];
        buildNeighbors(neighborData);
        // TASK3 ) Update transfer data
        List<String[]> transferData = data[2];
        updateTransfer(transferData);
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

    /** 0. Process data **/
    private static List<String[]>[] processSubwayLineData(String targetFilePath) {
        // TASK1 ) Initialize array of data list
        List<String[]>[] data = (List<String[]>[]) new Object[3];
        // TASK2 ) Open file
        File dataFile = new File(targetFilePath);
        // TASK2 ) Read file
        try (Scanner input = new Scanner(dataFile)) {
            int paragraph = 0;
            while (input.hasNext()) {
                String line = input.nextLine();
                // TASK2.1 ) If line is empty(end of first paragraph), break
                if (line.isEmpty() || line.isBlank()) {
                    paragraph++;
                    continue;
                }
                // TASK2.2 ) Split a line as three tokens(id-name-line)
                String[] information = line.split("\\s");
                // TASK2.3 ) Add information in list
                data[paragraph].add(information);
            }
        } catch (IOException e) {

        }
        return data;
    }

    /** 1. Build Stations **/
    private static void buildStations(List<String[]> stationData) {
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
            Neighbors.put(newStation.id, new ArrayList<Edge>());
        }
    }

    /** 2. Build Neighbors **/
    private static void buildNeighbors(List<String[]> neighborData) {
        for (String[] data : neighborData) {
            // TASK1 ) Segregate data
            String departuresID = data[0];
            String arrivalsID = data[1];
            int duration = Integer.parseInt(data[2]);
            // TASK2 ) Create Neighbor object
            Edge neighbor = new Edge(departuresID, arrivalsID, duration);
            // TASK3 ) Add Neighbor object in Neighbors
            Neighbors.get(departuresID).add(neighbor);
        }
    }

    /** 3. Update Transfer Time **/
    private static void updateTransfer(List<String[]> transferData) {
        // TASK1 ) Update isTransferStation
        updateIsTransferStation();
        // TASK2 ) Update transfer time
        updateTransferTime(transferData);
        // TASK3 ) Connect between transfer station with the same name
        updateTransferEdge();
    }

    private static void updateIsTransferStation() {
        for (Station station : Stations) {
            List<Station> stationsWithSameName = findStationByName(station.name);
            if (stationsWithSameName.size() >= 2) {
                // TASK1 ) Update isTransferStation
                station.isTransferStation = true;
                // TASK2 ) Set default transfer time as 5
                station.transferTime = 5;
            }
        }
    }

    private static void updateTransferTime(List<String[]> transferData) {
        for (String[] data : transferData) {
            // TASK1 ) Segregate data
            String stationName = data[0];
            int transferTime = Integer.parseInt(data[1]);
            // TASK2 ) Find station by name
            List<Station> stationWithSameName = findStationByName(stationName);
            for (Station station : stationWithSameName) {
                // TASK3 ) Update transfer time
                station.transferTime = transferTime;
            }
        }
    }

    private static void updateTransferEdge() {
        for (Station station : Stations) {
            List<Station> stationsWithSameName = findStationByName(station.name);
            if (stationsWithSameName.size() >= 2) {
                for (Station stationWithSameName : stationsWithSameName) {
                    if (!station.equals(stationWithSameName)) {
                        // TASK ) Create edge between transfer stations that have the same name, and set transfer time
                        Neighbors.get(station.id).add(new Edge(station.id, stationWithSameName.id, station.transferTime));
                    }
                }
            }
        }
    }

    /** 4. Process command **/
    private static void command(String input) {
        // TASK1 ) Parsing input, departures and arrivals
        Edge pair = parseInput(input);
        // TASK2 ) Find the shortest path using Dijkstra algorithm
        Stack<Edge> shortestPath = findShortestPath(pair);
        // TASK3 ) Print path
        printPath(shortestPath);
        // TASK4 ) Print duration
        printDuration(shortestPath);
    }

    private static Edge parseInput(String input) {
        // TASK1 ) Split input, delimited by a space
        String[] stationNames = input.split("\\s");
        String departuresName = stationNames[0];
        String arrivalsName = stationNames[1];
        // TASK2 ) Find Station object corresponding to station name
        Station departures = findStationByName(departuresName).get(0);
        Station arrivals = findStationByName(arrivalsName).get(0);
        // TASK3 ) Create Edge object
        Edge pair = new Edge(departures.id, arrivals.id);
        return pair;
    }

    private static Stack<Edge> findShortestPath(Edge pair) {
        Station departures = findStationByID(pair.departuresID);
        Station arrivals = findStationByID(pair.arrivalsID);
        Stack<Edge> shortedPath = dijkstra(departures, arrivals);
        return shortedPath;
    }

    private static Stack<Edge> dijkstra(Station departures, Station arrivals) {
        Map<Station, Boolean> visited = new HashMap<>();
        Map<Station, Integer> distance = new HashMap<>();
        Map<Station, Stack<Edge>> path = new HashMap<>();

        for (Station station : Stations) {
            visited.put(station, false);
            distance.put(station, INF);
            Stack<Edge> pathStack = new Stack<>();
            pathStack.push(new Edge(null, null, Integer.MIN_VALUE));
            path.put(station, pathStack);
        }

        dijkstraHelp(departures, arrivals, visited, distance, path);

        return path.get(arrivals);
    }

    private static void dijkstraHelp(Station departures, Station arrivals, Map<Station, Boolean> visited, Map<Station, Integer> distance, Map<Station, Stack<Edge>> path) {
        visited.put(departures, true);
        distance.put(departures, 0);

        List<Edge> neighborsOfDepartures = Neighbors.get(departures.id);
        for (Edge neighbor : neighborsOfDepartures) {
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

            List<Edge> neighborsOfCurrentStation = Neighbors.get(currStation.id);
            for (Edge neighbor : neighborsOfCurrentStation) {
                Station neighborStation = findStationByID(neighbor.arrivalsID);
                int cost = distance.get(currStation) + neighbor.duration;
                if (cost < distance.get(neighborStation)) {
                    distance.put(neighborStation, cost);
                    path.get(neighborStation).pop();
                    path.get(neighborStation).push(neighbor);
                } else {
                    path.get(neighborStation).push(new Edge(null, null, Integer.MIN_VALUE));
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

    private static void printPath(Stack<Edge> shortestPath) {
        List<Edge> path = new ArrayList<>(shortestPath);
        String result = findStationByID(path.get(0).arrivalsID).name;
        for (Edge log : path) {
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

    private static void printDuration(Stack<Edge> shortestPath) {
        List<Edge> path = new ArrayList<>(shortestPath);
        int result = 0;
        for (Edge log : path) {
            if (log.duration < 0) {
                continue;
            }
            result += log.duration;
        }
        System.out.println(result);
    }

    /** 5. etc **/
    private static List<Station> findStationByName(String stationName) {
        List<Station> result = new ArrayList<>();
        for (Station station : Stations) {
            if (stationName.equals(station.name)) {
                result.add(station);
            }
        }
        return result;
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
