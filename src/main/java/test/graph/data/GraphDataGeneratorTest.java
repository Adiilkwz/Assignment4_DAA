package main.java.test.graph.data;

import org.junit.jupiter.api.Test;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphDataGeneratorTest {

    // Helper method to check if a file exists
    private boolean fileExists(String fileName) {
        File file = new File("./data/" + fileName);
        return file.exists();
    }

    // Helper method to validate the structure of the graph in the JSON file
    private boolean validateGraphStructure(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("./data/" + fileName)));
        return content.contains("\"graph\"") && content.contains("\"node\"") && content.contains("\"edges\"");
    }

    // Helper method to parse the number of nodes and edges from the file
    private Map<String, Integer> getGraphDetails(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("./data/" + fileName)));
        int nodes = content.split("\"node\"").length - 1;  // Count number of "node" entries
        int edges = content.split("\"edges\"").length - 1; // Count number of "edges" entries
        Map<String, Integer> details = new HashMap<>();
        details.put("nodes", nodes);
        details.put("edges", edges);
        return details;
    }

    // Test that all generated datasets exist
    @Test
    void testGeneratedFilesExist() {
        String[] filenames = {
                "small_sparse_1.json", "small_dense_1.json", "small_cyclic_1.json",
                "medium_sparse_1.json", "medium_dense_1.json", "medium_scc_1.json",
                "large_sparse_1.json", "large_dense_1.json", "large_scc_1.json"
        };

        for (String filename : filenames) {
            assertTrue(fileExists(filename), "File should exist: " + filename);
        }
    }

    // Test that all files have the correct structure
    @Test
    void testCorrectGraphStructure() {
        String[] filenames = {
                "small_sparse_1.json", "small_dense_1.json", "small_cyclic_1.json",
                "medium_sparse_1.json", "medium_dense_1.json", "medium_scc_1.json",
                "large_sparse_1.json", "large_dense_1.json", "large_scc_1.json"
        };

        for (String filename : filenames) {
            try {
                assertTrue(validateGraphStructure(filename), "Invalid graph structure in: " + filename);
            } catch (IOException e) {
                fail("Error reading file: " + filename);
            }
        }
    }

    // Test that the medium SCC graph contains multiple SCCs
    @Test
    void testMultipleSCCsInMediumGraph() {
        String filename = "medium_scc_1.json";
        try {
            assertTrue(hasMultipleSCCs(filename), "Graph should have multiple SCCs: " + filename);
        } catch (IOException e) {
            fail("Error reading file: " + filename);
        }
    }

    // Test that the small graph files have the correct number of nodes and edges
    @Test
    void testSmallGraphDetails() {
        String[] filenames = {
                "small_sparse_1.json", "small_dense_1.json", "small_cyclic_1.json"
        };

        for (String filename : filenames) {
            try {
                Map<String, Integer> details = getGraphDetails(filename);
                assertTrue(details.get("nodes") >= 6 && details.get("nodes") <= 10,
                        "Number of nodes should be between 6 and 10 in: " + filename);
                assertTrue(details.get("edges") > 0, "Graph should have at least one edge in: " + filename);
            } catch (IOException e) {
                fail("Error reading file: " + filename);
            }
        }
    }

    // Test that the medium graph files have the correct number of nodes and edges
    @Test
    void testMediumGraphDetails() {
        String[] filenames = {
                "medium_sparse_1.json", "medium_dense_1.json", "medium_scc_1.json"
        };

        for (String filename : filenames) {
            try {
                Map<String, Integer> details = getGraphDetails(filename);
                assertTrue(details.get("nodes") >= 10 && details.get("nodes") <= 20,
                        "Number of nodes should be between 10 and 20 in: " + filename);
                assertTrue(details.get("edges") > 0, "Graph should have at least one edge in: " + filename);
            } catch (IOException e) {
                fail("Error reading file: " + filename);
            }
        }
    }

    // Test that the large graph files have the correct number of nodes and edges
    @Test
    void testLargeGraphDetails() {
        String[] filenames = {
                "large_sparse_1.json", "large_dense_1.json", "large_scc_1.json"
        };

        for (String filename : filenames) {
            try {
                Map<String, Integer> details = getGraphDetails(filename);
                assertTrue(details.get("nodes") >= 20 && details.get("nodes") <= 50,
                        "Number of nodes should be between 20 and 50 in: " + filename);
                assertTrue(details.get("edges") > 0, "Graph should have at least one edge in: " + filename);
            } catch (IOException e) {
                fail("Error reading file: " + filename);
            }
        }
    }

    private List<List<Integer>> parseGraphFromFile(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("./data/" + fileName)));
        JSONObject json = new JSONObject(content);
        JSONArray graphArray = json.getJSONArray("graph");

        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < graphArray.length(); i++) {
            JSONObject nodeObj = graphArray.getJSONObject(i);
            JSONArray edgesArray = nodeObj.getJSONArray("edges");

            List<Integer> edges = new ArrayList<>();
            for (int j = 0; j < edgesArray.length(); j++) {
                edges.add(edgesArray.getInt(j));
            }
            graph.add(edges);
        }

        return graph;
    }


    // Implementing Tarjan's Algorithm for SCC detection
    private List<List<Integer>> tarjanSCC(List<List<Integer>> graph) {
        int n = graph.size();
        int[] lowLink = new int[n];
        int[] ids = new int[n];
        boolean[] onStack = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        List<List<Integer>> sccs = new ArrayList<>();
        int id = 0;

        // DFS to find SCCs
        for (int i = 0; i < n; i++) {
            if (ids[i] == 0) {
                dfs(i, graph, lowLink, ids, onStack, stack, sccs, id);
            }
        }
        return sccs;
    }

    // DFS helper for Tarjan's algorithm
    private void dfs(int at, List<List<Integer>> graph, int[] lowLink, int[] ids, boolean[] onStack,
                     Stack<Integer> stack, List<List<Integer>> sccs, int id) {
        stack.push(at);
        onStack[at] = true;
        ids[at] = lowLink[at] = ++id;

        // Explore neighbors
        for (int to : graph.get(at)) {
            if (ids[to] == 0) {
                dfs(to, graph, lowLink, ids, onStack, stack, sccs, id);
            }
            if (onStack[to]) {
                lowLink[at] = Math.min(lowLink[at], lowLink[to]);
            }
        }

        // If at root node, pop the stack and create SCC
        if (ids[at] == lowLink[at]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                scc.add(node);
                if (node == at) break;
            }
            sccs.add(scc);
        }
    }

    // Helper method to check for multiple SCCs
    private boolean hasMultipleSCCs(String fileName) throws IOException {
        List<List<Integer>> graph = parseGraphFromFile(fileName);
        List<List<Integer>> sccs = tarjanSCC(graph);

        // Check if there is more than one SCC
        return sccs.size() > 1;
    }
}
