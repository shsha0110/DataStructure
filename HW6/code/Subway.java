import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Subway {

    public static List<Station> Stations = new ArrayList<>();
    private static Map<String, List<Station>> StationMapByName = new HashMap<>();
    private static Map<String, Station> StationMapByID = new HashMap<>();
    public static Map<String, List<Edge>> Neighbors = new HashMap<>();
    public static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        // TASK1 ) Process subway line data
        String targetFilePath = args[0];
        List<List<String[]>> data = processSubwayLineData(targetFilePath);
        // TASK1 ) Build Stations
        List<String[]> stationData = data.get(0);
        buildStations(stationData);
        // TASK2 ) Build Neighbors
        List<String[]> neighborData = data.get(1);
        buildNeighbors(neighborData);
        // TASK3 ) Update transfer data
        List<String[]> transferData = data.get(2);
        updateTransfer(transferData);
        // TASK3 ) Read input
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            // TASK4 ) Process input
            String input;
            while ((input = br.readLine()) != null) {
                if ("QUIT".equals(input)) {
                    break;
                } else {
                    command(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 0. Process data **/
    private static List<List<String[]>> processSubwayLineData(String targetFilePath) {
        // TASK1 ) Initialize array of data list
        List<List<String[]>> data = new ArrayList<List<String[]>>();
        for (int i = 0; i < 3; i++) {
            data.add(new ArrayList<String[]>());
        }
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
                data.get(paragraph).add(information);
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
            StationMapByID.put(id, newStation);
            List<Station> stationsWithSameName = StationMapByName.getOrDefault(name, new ArrayList<>());
            stationsWithSameName.add(newStation);
            StationMapByName.put(name, stationsWithSameName);
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

    private final static int DEFAULT_TRANSFER_TIME = 5;
    private static void updateIsTransferStation() {
        for (Station station : Stations) {
            List<Station> stationsWithSameName = findStationsByName(station.name);
            if (stationsWithSameName.size() >= 2) {
                // TASK1 ) Update isTransferStation
                station.isTransferStation = true;
                // TASK2 ) Set default transfer time as 5
                station.transferTime = DEFAULT_TRANSFER_TIME;
            }
        }
    }

    private static void updateTransferTime(List<String[]> transferData) {
        for (String[] data : transferData) {
            // TASK1 ) Segregate data
            String stationName = data[0];
            int transferTime = Integer.parseInt(data[1]);
            // TASK2 ) Find station by name
            List<Station> stationWithSameName = findStationsByName(stationName);
            for (Station station : stationWithSameName) {
                // TASK3 ) Update transfer time
                station.transferTime = transferTime;
            }
        }
    }

    private static void updateTransferEdge() {
        for (Station station : Stations) {
            List<Station> stationsWithSameName = findStationsByName(station.name);
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
        Station[] pair = parseInput(input);
        // TASK2 ) Find the shortest path using Dijkstra algorithm
        Map<Station, Edge> shortestPath = findShortestPath(pair);
        // TASK3 ) Print path
        printPath(pair, shortestPath);
        // TASK4 ) Print duration
        printDuration(pair, shortestPath);
    }

    private static Station[] parseInput(String input) {
        // TASK1 ) Split input, delimited by a space
        String[] stationNames = input.split("\\s");
        String departuresName = stationNames[0];
        String arrivalsName = stationNames[1];
        // TASK2 ) Find Station object corresponding to station name
        Station departures = findStationsByName(departuresName).get(0);
        Station arrivals = findStationsByName(arrivalsName).get(0);

        return new Station[]{departures, arrivals};
    }

    private static Map<Station, Edge> findShortestPath(Station[] pair) {
        Station departures = pair[0], arrivals = pair[1];
        Map<Station, Boolean> visited = new HashMap<>();
        Map<Station, Integer> distance = new HashMap<>();
        Map<Station, Edge> shortedPath = new HashMap<>();
        // TASK1 ) Initialize visited log as false, distance as infinite, shorted path as null
        for (Station station : Stations) {
            visited.put(station, false);
            distance.put(station, INF);
            shortedPath.put(station, null);
        }
        // TASK2 ) Execute Dijkstra algorithm and update above matrices
        dijkstra(departures, arrivals, visited, distance, shortedPath);
        return shortedPath;
    }

    private static void dijkstra(Station departures, Station arrivals, Map<Station, Boolean> visited,
                                 Map<Station, Integer> distance, Map<Station, Edge> path) {
        // Set the initial distance of the departure station to 0
        distance.put(departures, 0);

        // Create a priority queue (min heap) to store stations, using a custom comparator to sort by distance
        PriorityQueue<Station> minDistanceStations = new PriorityQueue<>((Station a, Station b) -> distance.get(a) - distance.get(b));

        // Add the departure station to the queue
        minDistanceStations.add(departures);

        // Continue this loop until the queue is empty
        while (!minDistanceStations.isEmpty()) {
            // Take out the station with the shortest distance
            Station currStation = minDistanceStations.poll();

            // Skip if the station has been visited
            if (visited.get(currStation)) continue;

            // Mark the current station as visited
            visited.put(currStation, true);

            // If the current station is the destination, we've found the shortest path
            if (currStation.equals(arrivals)) {
                return;
            }

            // Get all neighboring stations of the current station
            List<Edge> neighborsOfCurrentStation = Neighbors.get(currStation.id);
            for (Edge neighbor : neighborsOfCurrentStation) {
                Station neighborStation = findStationByID(neighbor.arrivalsID);

                // Calculate the cost of the path through the current station
                int cost = distance.get(currStation) != INF && neighbor.duration != INF ? distance.get(currStation) + neighbor.duration : INF;

                // If this cost is less than the previous cost to get to the neighboring station, update it
                if (cost < distance.get(neighborStation)) {
                    distance.put(neighborStation, cost);
                    path.put(neighborStation, neighbor);

                    // Add the neighboring station to the queue
                    minDistanceStations.add(neighborStation);
                }
            }
        }
    }

    private static void printPath(Station[] pair, Map<Station, Edge> shortestPath) {
        Station departures = pair[0], arrivals = pair[1];
        String result = "";
        Station currStation = arrivals;
        // 도착역이 환승역인 경우, 환승하지 않고 도착한 것으로 처리
        for (int i = 0; i < shortestPath.size(); i++) {
            Edge currEdge = shortestPath.get(currStation);
            Station prevStation = findStationByID(currEdge.departuresID);
            if (prevStation.name.equals(arrivals.name)) {
                currStation = prevStation;
            } else {
                break;
            }
        }

        boolean transferred = false;
        for (int i = 0; i < shortestPath.size(); i++) {
            Edge currEdge = shortestPath.get(currStation);
            Station prevStation = findStationByID(currEdge.departuresID);
            // 환승하는 경우
            if (currStation.name.equals(prevStation.name) && !currStation.line.equals(prevStation.line)) {
                result = String.format("[%s] ", currStation.name) + result;
                transferred = true;
            }
            // 환승하지 않는 경우
            else {
                if (!transferred) {
                    result = String.format("%s ", currStation.name) + result;
                }
                transferred = false;
            }
            // 출발역이 환승역인 경우, 환승을 했다고 생각하고 처리
            if (prevStation.name.equals(departures.name)) {
                result = String.format("%s ", prevStation.name) + result;
                break;
            }

            currStation = prevStation;
        }
        //System.out.println(result.trim());
        System.out.print(result.trim() + "\r\n");
    }

    private static void printDuration(Station[] pair, Map<Station, Edge> shortestPath) {
        Station departures = pair[0], arrivals = pair[1];
        int result = 0;
        Station currStation = arrivals;
        // 도착역이 환승역인 경우, 환승하지 않고 도착한 것으로 처리
        for (int i = 0; i < shortestPath.size(); i++) {
            Edge currEdge = shortestPath.get(currStation);
            Station prevStation = findStationByID(currEdge.departuresID);
            if (prevStation.name.equals(arrivals.name)) {
                currStation = prevStation;
            } else {
                break;
            }
        }

        for (int i = 0; i < shortestPath.size(); i++) {
            Edge currEdge = shortestPath.get(currStation);
            Station prevStation = findStationByID(currEdge.departuresID);
            result += currEdge.duration;
            // 출발역이 환승역인 경우, 환승을 했다고 생각하고 처리
            if (prevStation.name.equals(departures.name)) break;
            currStation = prevStation;
        }
        //System.out.println(result);
        System.out.print(result + "\r\n");
    }

    /** 5. etc **/
    private static List<Station> findStationsByName(String name) {
        return StationMapByName.get(name);
    }

    private static Station findStationByID(String id) {
        return StationMapByID.get(id);
    }
}
