package main.java.graph.data;

import main.java.graph.metrics.MetricsTracker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GraphDataHelper {
    private static final MetricsTracker metrics = new MetricsTracker();

    public static boolean fileExists(String fileName) {
        File file = new File("./data/" + fileName);
        return file.exists();
    }

    public static boolean validateGraphStructure(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("./data/" + fileName)));
        return content.contains("\"graph\"") && content.contains("\"node\"") && content.contains("\"edges\"");
    }

    public static Map<String, Integer> getGraphDetails(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("./data/" + fileName)));
        int nodes = content.split("\"node\"").length - 1;
        int edges = content.split("\"edges\"").length - 1;
        Map<String, Integer> details = new HashMap<>();
        details.put("nodes", nodes);
        details.put("edges", edges);
        return details;
    }

    public static List<List<Integer>> parseGraphFromFile(String fileName) throws IOException {
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

    public static List<List<Integer>> tarjanSCC(List<List<Integer>> graph) {
        metrics.reset();
        metrics.start();

        int n = graph.size();
        int[] ids = new int[n];
        int[] low = new int[n];
        boolean[] onStack = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        List<List<Integer>> sccs = new ArrayList<>();
        int[] idCounter = {1};

        for (int i = 0; i < n; i++) {
            if (ids[i] == 0) {
                dfs(i, graph, ids, low, onStack, stack, sccs, idCounter);
            }
        }

        metrics.stop();
        System.out.println("Tarjan finished — " + metrics);
        return sccs;
    }

    private static void dfs(int at, List<List<Integer>> graph, int[] ids, int[] low,
                            boolean[] onStack, Stack<Integer> stack, List<List<Integer>> sccs, int[] idCounter) {
        metrics.recordOperation(); // count DFS visit
        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = idCounter[0]++;

        for (int to : graph.get(at)) {
            metrics.recordOperation(); // count edge exploration
            if (ids[to] == 0) {
                dfs(to, graph, ids, low, onStack, stack, sccs, idCounter);
            }
            if (onStack[to]) {
                low[at] = Math.min(low[at], low[to]);
            }
        }

        if (ids[at] == low[at]) {
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

    public static boolean hasMultipleSCCs(String fileName) throws IOException {
        List<List<Integer>> graph = parseGraphFromFile(fileName);
        List<List<Integer>> sccs = tarjanSCC(graph);
        return sccs.size() > 1;
    }
}