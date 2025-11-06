package main.java.graph.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphDataGenerator {

    // Updated logic to ensure multiple SCCs
    public static List<List<Integer>> generateGraph(int n, boolean isSparse, boolean isCyclic, boolean hasMultipleSCCs) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        Random rand = new Random();

        if (hasMultipleSCCs) {
            // Force exactly 2 SCCs for clarity
            int sccCount = 2;
            int nodesPerSCC = n / sccCount;

            // Create SCC #1
            for (int i = 0; i < nodesPerSCC; i++) {
                int current = i;
                int next = (i + 1) % nodesPerSCC;
                graph.get(current).add(next);
            }

            // Create SCC #2
            for (int i = nodesPerSCC; i < n; i++) {
                int current = i;
                int next = (i + 1 == n) ? nodesPerSCC : i + 1;
                graph.get(current).add(next);
            }

            // Add internal random edges within each SCC
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && rand.nextDouble() < 0.2) { // 20% chance of internal edge
                        if ((i < nodesPerSCC && j < nodesPerSCC) || (i >= nodesPerSCC && j >= nodesPerSCC)) {
                            graph.get(i).add(j);
                        }
                    }
                }
            }

            // Optionally connect one SCC to another in one direction only
            int u = rand.nextInt(nodesPerSCC);
            int v = nodesPerSCC + rand.nextInt(n - nodesPerSCC);
            graph.get(u).add(v);

            // Note: we do NOT add reverse edges (v → u),
            // so SCCs remain separate (two distinct SCCs).
        } else {
            // Regular random graph generation (no multiple SCCs)
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



    // Method to save the graph as a JSON file
    public static void saveGraphToFile(List<List<Integer>> graph, String fileName) {
        try {
            // Ensure the data directory exists
            File dataDirectory = new File("./data");
            if (!dataDirectory.exists()) {
                dataDirectory.mkdirs();  // Create the directory if it doesn't exist
            }

            // Save the graph to a file in the data directory
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

    // Generate 9 graphs with the specifications
    public static void generateAllGraphs() {
        // Small graphs (6-10 nodes)
        generateGraphForSize(6, "small_sparse_1.json", true, false, false);
        generateGraphForSize(8, "small_dense_1.json", false, false, false);
        generateGraphForSize(6, "small_cyclic_1.json", true, true, false);

        // Medium graphs (10-20 nodes)
        generateGraphForSize(12, "medium_sparse_1.json", true, false, false);
        generateGraphForSize(15, "medium_dense_1.json", false, false, false);
        generateGraphForSize(18, "medium_scc_1.json", true, true, true); // Graph with multiple SCCs

        // Large graphs (20-50 nodes)
        generateGraphForSize(25, "large_sparse_1.json", true, false, false);
        generateGraphForSize(30, "large_dense_1.json", false, false, false);
        generateGraphForSize(40, "large_scc_1.json", true, true, true); // Graph with multiple SCCs
    }

    // Helper method to generate and save graph for a specific size and configuration
    private static void generateGraphForSize(int n, String fileName, boolean isSparse, boolean isCyclic, boolean hasMultipleSCCs) {
        List<List<Integer>> graph = generateGraph(n, isSparse, isCyclic, hasMultipleSCCs);
        saveGraphToFile(graph, fileName);
    }

    public static void main(String[] args) {
        // Generate all the graphs as specified
        generateAllGraphs();
    }
}

