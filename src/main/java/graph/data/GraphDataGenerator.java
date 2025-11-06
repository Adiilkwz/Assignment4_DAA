package main.java.graph.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphDataGenerator {
    public static List<List<Integer>> generateGraph(int n, boolean isSparse, boolean isCyclic, boolean hasMultipleSCCs) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        Random rand = new Random();

        if (hasMultipleSCCs) {
            int sccCount = 2;
            int nodesPerSCC = n / sccCount;

            for (int i = 0; i < nodesPerSCC; i++) {
                int current = i;
                int next = (i + 1) % nodesPerSCC;
                graph.get(current).add(next);
            }

            for (int i = nodesPerSCC; i < n; i++) {
                int current = i;
                int next = (i + 1 == n) ? nodesPerSCC : i + 1;
                graph.get(current).add(next);
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && rand.nextDouble() < 0.2) {
                        if ((i < nodesPerSCC && j < nodesPerSCC) || (i >= nodesPerSCC && j >= nodesPerSCC)) {
                            graph.get(i).add(j);
                        }
                    }
                }
            }

            int u = rand.nextInt(nodesPerSCC);
            int v = nodesPerSCC + rand.nextInt(n - nodesPerSCC);
            graph.get(u).add(v);

        } else {
            int edgeCount = isSparse ? (n * (n - 1)) / 4 : (n * (n - 1)) / 2;
            for (int i = 0; i < edgeCount; i++) {
                int u = rand.nextInt(n);
                int v = rand.nextInt(n);
                if (u != v && !graph.get(u).contains(v)) {
                    graph.get(u).add(v);
                }
            }

            if (isCyclic) {
                int u = rand.nextInt(n);
                int v = rand.nextInt(n);
                if (u != v && !graph.get(u).contains(v)) {
                    graph.get(u).add(v);
                }
            }
        }

        return graph;
    }

    public static void saveGraphToFile(List<List<Integer>> graph, String fileName) {
        try {
            File dataDirectory = new File("./data");
            if (!dataDirectory.exists()) {
                dataDirectory.mkdirs();
            }

            File file = new File("./data/" + fileName);
            FileWriter writer = new FileWriter(file);
            writer.write("{ \"graph\": [\n");

            for (int i = 0; i < graph.size(); i++) {
                writer.write("  {\"node\": " + i + ", \"edges\": " + graph.get(i).toString() + "}");
                if (i < graph.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]}\n");
            writer.close();
            System.out.println("Graph saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateAllGraphs() {
        generateGraphForSize(6, "small_sparse_1.json", true, false, false);
        generateGraphForSize(8, "small_dense_1.json", false, false, false);
        generateGraphForSize(6, "small_cyclic_1.json", true, true, false);

        generateGraphForSize(12, "medium_sparse_1.json", true, false, false);
        generateGraphForSize(15, "medium_dense_1.json", false, false, false);
        generateGraphForSize(18, "medium_scc_1.json", true, true, true); // Graph with multiple SCCs

        generateGraphForSize(25, "large_sparse_1.json", true, false, false);
        generateGraphForSize(30, "large_dense_1.json", false, false, false);
        generateGraphForSize(40, "large_scc_1.json", true, true, true); // Graph with multiple SCCs
    }

    private static void generateGraphForSize(int n, String fileName, boolean isSparse, boolean isCyclic, boolean hasMultipleSCCs) {
        List<List<Integer>> graph = generateGraph(n, isSparse, isCyclic, hasMultipleSCCs);
        saveGraphToFile(graph, fileName);
    }

    public static void main(String[] args) {
        generateAllGraphs();
    }
}

