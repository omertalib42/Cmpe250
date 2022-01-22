import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class project3main {
    public static void main(String[] args) throws FileNotFoundException {

        Scanner reader = new Scanner(new File(args[0]));
        PrintStream printer = new PrintStream(args[1]);

        int timeLimit = Integer.parseInt(reader.nextLine());
        int numCities = Integer.parseInt(reader.nextLine());
        String[] capitals = reader.nextLine().split(" ");

        HashMap<String, ArrayList<Edge>> mpLeft = new HashMap<>();
        HashMap<String, ArrayList<Edge>> mpRight = new HashMap<>();
        HashMap<String, String> parent = new HashMap<>();
        HashMap<String, Integer> distance = new HashMap<>();
        PriorityQueue<Path> paths = new PriorityQueue<>(Comparator.comparing(Path::getPathLength));

        int dcnt = 1;
        while (numCities-- > 0) {
            String[] line = reader.nextLine().split(" ");
            mpLeft.put(line[0], new ArrayList<>());
            distance.put(line[0], Integer.MAX_VALUE);
            if (line[0].charAt(0) == 'd') {
                dcnt++;
            }
            for (int i = 1; line.length > i; i += 2) {
                if (line[0].startsWith("d") || line[i].startsWith("d")) {
                    if (!mpRight.containsKey(line[0])) {
                        mpRight.put(line[0], new ArrayList<>());
                    }
                    mpRight.get(line[0]).add(new Edge(line[0], line[i], Integer.parseInt(line[i + 1])));
                    if (!mpRight.containsKey(line[i])) {
                        mpRight.put(line[i], new ArrayList<>());
                    }
                    mpRight.get(line[i]).add(new Edge(line[i], line[0], Integer.parseInt(line[i + 1])));
                } else {
                    mpLeft.get(line[0]).add(new Edge(line[0], line[i], Integer.parseInt(line[i + 1])));
                }
            }
        }

        paths.add(new Path(0, capitals[0]));
        parent.put(capitals[0], capitals[0]);
        distance.put(capitals[0], 0);

        while (paths.size() > 0) {
            Path path = paths.poll();
            String from = path.id;
            for (int i = 0; mpLeft.get(path.id).size() > i; i++) {
                String to = mpLeft.get(path.id).get(i).to;
                int weight = mpLeft.get(from).get(i).weight;
                if (distance.get(from) + weight < distance.get(to)) {
                    parent.put(to, from);
                    distance.put(to, distance.get(from) + weight);
                    if (!to.equals(capitals[1])) {
                        paths.add(new Path(distance.get(to), to));
                    }
                }
            }
        }

        if (distance.get(capitals[1]) == Integer.MAX_VALUE) {
            printer.print("-1");
        } else {
            ArrayList<String> path = new ArrayList<>();
            String start = capitals[0], end = capitals[1];
            while (true) {
                path.add(end);
                if (start.equals(end)) {
                    break;
                }
                end = parent.get(end);
            }
            Collections.reverse(path);
            for (String node : path) {
                printer.print(node + " ");
            }
        }

        HashMap<String, Integer> visited = new HashMap<>();
        paths.add(new Path(0, capitals[1]));
        long mstCost = 0;
        if (mpRight.containsKey(capitals[1])) {
            while (paths.size() > 0) {
                Path edge = paths.poll();
                String from = edge.id;
                if (visited.containsKey(from)) {
                    continue;
                }
                visited.put(from, 1);
                mstCost += edge.pathLength;
                for (int i = 0; mpRight.get(edge.id).size() > i; i++) {
                    String to = mpRight.get(edge.id).get(i).to;
                    if (visited.containsKey(to)) {
                        continue;
                    }
                    int weight = mpRight.get(from).get(i).weight;
                    paths.add(new Path(weight, to));
                }
            }
        }
        printer.println();
        if(distance.get(capitals[1]) > timeLimit) {
            printer.println(-1);
        } else if(visited.size() == dcnt) {
            printer.println(2 * mstCost);
        } else {
            printer.println(-2);
        }
    }

}
