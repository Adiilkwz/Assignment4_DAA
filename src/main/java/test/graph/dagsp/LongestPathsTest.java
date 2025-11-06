package main.java.test.graph.dagsp;

import main.java.graph.dagsp.LongestPath;
import main.java.graph.data.GraphDataHelper;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LongestPathsTest {

    @Test
    public void testSmallSparseGraph() throws IOException {
        String file = "small_dense_1.json";
        assertTrue(GraphDataHelper.fileExists(file), "Файл не найден в /data");

        int[] dist = LongestPath.findLongestPaths(file, 0);
        System.out.println("Longest paths from node 0 (dense):");
        System.out.println(Arrays.toString(dist));

        boolean hasFinite = Arrays.stream(dist).anyMatch(d -> d > Integer.MIN_VALUE);
        assertTrue(hasFinite, "Не найдено ни одного достижимого узла");

        List<Integer> path = LongestPath.getCriticalPath(file, 0);
        System.out.println("Critical path: " + path);
        assertTrue(path.size() >= 2);
    }

    @Test
    public void testSmallDenseGraph() throws IOException {
        String file = "small_dense_1.json";
        assertTrue(GraphDataHelper.fileExists(file), "Файл не найден в /data");

        int[] dist = LongestPath.findLongestPaths(file, 0);
        System.out.println("Longest paths from node 0 (dense):");
        System.out.println(Arrays.toString(dist));

        assertNotEquals(Integer.MIN_VALUE, dist[0], "Стартовая вершина должна быть достижима");

        boolean hasGreater = Arrays.stream(dist).anyMatch(d -> d > dist[0]);
        assertTrue(hasGreater, "Должен существовать хотя бы один путь длиннее стартового");

        List<Integer> path = LongestPath.getCriticalPath(file, 0);
        System.out.println("Critical path: " + path);
        assertTrue(path.size() >= 2, "Критический путь должен содержать хотя бы 2 вершины");
    }
}
