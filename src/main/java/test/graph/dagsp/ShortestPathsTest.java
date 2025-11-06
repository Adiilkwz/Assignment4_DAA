package main.java.test.graph.dagsp;

import main.java.graph.dagsp.ShortestPath;
import main.java.graph.data.GraphDataHelper;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ShortestPathsTest {

    @Test
    public void testSmallSparseGraph() throws IOException {
        String file = "small_sparse_1.json";
        assertTrue(GraphDataHelper.fileExists(file), "Файл не найден в /data");

        int[] dist = ShortestPath.findShortestPaths(file, 0);

        System.out.println("Shortest paths from node 0:");
        System.out.println(Arrays.toString(dist));

        assertEquals(0, dist[0]);
    }

    @Test
    public void testSmallDenseGraph() throws IOException {
        String file = "small_dense_1.json";
        assertTrue(GraphDataHelper.fileExists(file), "Файл не найден в /data");

        int[] dist = ShortestPath.findShortestPaths(file, 0);

        System.out.println("Shortest paths from node 0 (dense graph):");
        System.out.println(Arrays.toString(dist));

        assertEquals(0, dist[0]);
    }
}