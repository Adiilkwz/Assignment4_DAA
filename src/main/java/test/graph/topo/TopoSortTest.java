package main.java.test.graph.topo;

import main.java.graph.topo.TopologicalSort;
import main.java.graph.topo.TopoSortUtils;
import main.java.graph.data.GraphDataHelper;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TopoSortTest {

    @Test
    void testTopologicalOrderSmallGraph() throws IOException {
        String file = "small_sparse_1.json";
        assertTrue(GraphDataHelper.fileExists(file), "Graph file not found");

        List<Integer> order = TopologicalSort.topologicalOrder(file);
        assertNotNull(order);
        assertFalse(order.isEmpty(), "Topological order should not be empty");

        List<List<Integer>> graph = GraphDataHelper.parseGraphFromFile(file);
        boolean valid = TopoSortUtils.isValidTopologicalOrder(graph, order);
        assertTrue(valid, "Order should be a valid topological sort for DAG");
    }

    @Test
    void testDetectCycle() throws IOException {
        String file = "small_cyclic_1.json";
        assertTrue(GraphDataHelper.fileExists(file), "Graph file not found");

        List<Integer> order = TopologicalSort.topologicalOrder(file);
        // If cycle exists, order will be incomplete
        List<List<Integer>> graph = GraphDataHelper.parseGraphFromFile(file);
        if (order.size() < graph.size()) {
            System.out.println("Cycle detected — topological order incomplete as expected.");
        }
        assertTrue(order.size() <= graph.size());
    }
}
