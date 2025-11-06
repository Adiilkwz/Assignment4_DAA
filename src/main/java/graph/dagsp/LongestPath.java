package main.java.graph.dagsp;

import main.java.graph.data.GraphDataHelper;
import main.java.graph.metrics.MetricsTracker;

import java.io.IOException;
import java.util.*;

public class LongestPath {

    private static final MetricsTracker metrics = new MetricsTracker();

    public static int[] findLongestPaths(String fileName, int start) throws IOException {
        metrics.reset();
        metrics.start();

        List<List<Integer>> graph = GraphDataHelper.parseGraphFromFile(fileName);
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[start] = 0;

        List<Integer> topoOrder = topologicalSort(graph);

        for (int node : topoOrder) {
            if (dist[node] != Integer.MIN_VALUE) {
                for (int next : graph.get(node)) {
                    metrics.recordOperation();
                    if (dist[next] < dist[node] + 1) {
                        dist[next] = dist[node] + 1;
                    }
                }
            }
        }

        metrics.stop();
        System.out.println("Longest Path (DAG) finished — " + metrics);

        return dist;
    }

    public static List<Integer> getCriticalPath(String fileName, int start) throws IOException {
        int[] dist = findLongestPaths(fileName, start);

        int maxDist = Integer.MIN_VALUE, end = -1;
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] > maxDist) {
                maxDist = dist[i];
                end = i;
            }
        }

        List<Integer> path = new ArrayList<>();
        path.add(start);
        path.add(end);
        return path;
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
