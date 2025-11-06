package main.java.graph.topo;

import main.java.graph.data.GraphDataHelper;
import main.java.graph.metrics.MetricsTracker;
import java.io.IOException;
import java.util.*;

public class TopologicalSort {

    private static final MetricsTracker metrics = new MetricsTracker();

    public static List<Integer> topologicalOrder(String fileName) throws IOException {
        metrics.reset();
        metrics.start();

        List<List<Integer>> graph = GraphDataHelper.parseGraphFromFile(fileName);
        int n = graph.size();
        int[] indegree = new int[n];

        // Compute indegrees
        for (int i = 0; i < n; i++) {
            for (int v : graph.get(i)) {
                indegree[v]++;
                metrics.recordOperation();
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int node = queue.poll();
            topoOrder.add(node);
            for (int next : graph.get(node)) {
                indegree[next]--;
                metrics.recordOperation();
                if (indegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        metrics.stop();
        System.out.println("Topological Sort completed — " + metrics);

        if (topoOrder.size() != n) {
            System.out.println("⚠ Graph has cycles — topological order incomplete.");
        }

        return topoOrder;
    }
}
