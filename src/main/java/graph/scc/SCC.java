package main.java.graph.scc;

import main.java.graph.metrics.MetricsTracker;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class SCC {

    private static final MetricsTracker metrics = new MetricsTracker();

    private static List<List<Integer>> loadGraphFromTasksJson(String fileName) throws IOException {
        String filePath = Paths.get("./data", fileName).toString();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            JSONObject json = new JSONObject(new JSONTokener(fis));

            int n = json.getInt("n");
            JSONArray edges = json.getJSONArray("edges");

            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int i = 0; i < edges.length(); i++) {
                JSONObject e = edges.getJSONObject(i);
                int u = e.getInt("u");
                int v = e.getInt("v");
                graph.get(u).add(v);
            }

            return graph;
        }
    }

    public static List<List<Integer>> findSCCs(String fileName) throws IOException {
        metrics.reset();
        metrics.start();

        List<List<Integer>> graph;
        if (fileName.equalsIgnoreCase("tasks.json")) {
            graph = loadGraphFromTasksJson(fileName);
        } else {
            graph = main.java.graph.data.GraphDataHelper.parseGraphFromFile(fileName);
        }

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
        System.out.println("SCC (Tarjan) completed — " + metrics);
        printSCCInfo(sccs);

        List<List<Integer>> condensation = buildCondensationGraph(graph, sccs);
        System.out.println("Condensation Graph (DAG of components): " + condensation);

        return sccs;
    }

    private static void dfs(int at, List<List<Integer>> graph, int[] ids, int[] low,
                            boolean[] onStack, Stack<Integer> stack,
                            List<List<Integer>> sccs, int[] idCounter) {
        metrics.recordOperation();

        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = idCounter[0]++;

        for (int to : graph.get(at)) {
            metrics.recordOperation();
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

    private static void printSCCInfo(List<List<Integer>> sccs) {
        System.out.println("Detected " + sccs.size() + " SCCs:");
        for (int i = 0; i < sccs.size(); i++) {
            System.out.println("SCC " + (i + 1) + " (size " + sccs.get(i).size() + "): " + sccs.get(i));
        }
    }

    private static List<List<Integer>> buildCondensationGraph(List<List<Integer>> graph, List<List<Integer>> sccs) {
        int compCount = sccs.size();
        Map<Integer, Integer> nodeToComp = new HashMap<>();

        for (int i = 0; i < compCount; i++) {
            for (int node : sccs.get(i)) {
                nodeToComp.put(node, i);
            }
        }

        List<Set<Integer>> dagSet = new ArrayList<>();
        for (int i = 0; i < compCount; i++) dagSet.add(new HashSet<>());

        for (int u = 0; u < graph.size(); u++) {
            int cu = nodeToComp.get(u);
            for (int v : graph.get(u)) {
                int cv = nodeToComp.get(v);
                if (cu != cv) dagSet.get(cu).add(cv);
            }
        }

        List<List<Integer>> dag = new ArrayList<>();
        for (Set<Integer> s : dagSet) {
            dag.add(new ArrayList<>(s));
        }
        return dag;
    }
}
