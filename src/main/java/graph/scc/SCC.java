package main.java.graph.scc;

import main.java.graph.data.GraphDataHelper;
import main.java.graph.metrics.MetricsTracker;
import java.io.IOException;
import java.util.*;

public class SCC {

    private static final MetricsTracker metrics = new MetricsTracker();

    public static List<List<Integer>> findSCCs(String fileName) throws IOException {
        metrics.reset();
        metrics.start();

        List<List<Integer>> graph = GraphDataHelper.parseGraphFromFile(fileName);
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
        return sccs;
    }

    private static void dfs(int at, List<List<Integer>> graph, int[] ids, int[] low,
                            boolean[] onStack, Stack<Integer> stack, List<List<Integer>> sccs, int[] idCounter) {
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
}
