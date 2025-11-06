package main.java.graph.topo;

import java.util.List;

public class TopoSortUtils {

    public static boolean isValidTopologicalOrder(List<List<Integer>> graph, List<Integer> order) {
        int n = graph.size();
        int[] position = new int[n];
        for (int i = 0; i < n; i++) {
            position[order.get(i)] = i;
        }

        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) {
                if (position[u] > position[v]) {
                    return false; // invalid order
                }
            }
        }
        return true;
    }
}
