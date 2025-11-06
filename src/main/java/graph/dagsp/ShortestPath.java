package main.java.graph.dagsp;

import main.java.graph.data.GraphDataHelper;
import main.java.graph.metrics.MetricsTracker;

import java.io.IOException;
import java.util.*;

public class ShortestPath {

    private static final MetricsTracker metrics = new MetricsTracker();

    public static int[] findShortestPaths(String fileName, int start) throws IOException {
        metrics.reset();
        metrics.start();

        List<List<Integer>> graph = GraphDataHelper.parseGraphFromFile(fileName);
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        List<Integer> topoOrder = topologicalSort(graph);

        for (int node : topoOrder) {
            if (dist[node] != Integer.MAX_VALUE) {
                for (int next : graph.get(node)) {
                    metrics.recordOperation();
                    if (dist[next] > dist[node] + 1) {
                        dist[next] = dist[node] + 1;
                    }
                }
            }
        }

        metrics.stop();
        System.out.println("Shortest Path (DAG) finished — " + metrics);

        return dist;
    }

    private static List<Integer> topologicalSort(List<List<Integer>> graph) {
        int n = graph.size();
        boolean[] visited = new boolean[n];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) dfs(i, graph, visited, stack);
        }

        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) order.add(stack.pop());
        return order;
    }

    private static void dfs(int node, List<List<Integer>> graph, boolean[] visited, Stack<Integer> stack) {
        visited[node] = true;
        for (int next : graph.get(node)) {
            if (!visited[next]) dfs(next, graph, visited, stack);
        }
        stack.push(node);
    }
}
