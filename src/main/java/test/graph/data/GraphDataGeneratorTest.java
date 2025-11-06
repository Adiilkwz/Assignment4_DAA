package main.java.test.graph.data;

import main.java.graph.data.GraphDataHelper;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GraphDataGeneratorTest {

    @Test
    void testGeneratedFilesExist() {
        String[] filenames = {
                "small_sparse_1.json", "small_dense_1.json", "small_cyclic_1.json",
                "medium_sparse_1.json", "medium_dense_1.json", "medium_scc_1.json",
                "large_sparse_1.json", "large_dense_1.json", "large_scc_1.json"
        };

        for (String filename : filenames) {
            assertTrue(GraphDataHelper.fileExists(filename), "File should exist: " + filename);
        }
    }

    @Test
    void testCorrectGraphStructure() {
        String[] filenames = {
                "small_sparse_1.json", "small_dense_1.json", "small_cyclic_1.json",
                "medium_sparse_1.json", "medium_dense_1.json", "medium_scc_1.json",
                "large_sparse_1.json", "large_dense_1.json", "large_scc_1.json"
        };

        for (String filename : filenames) {
            try {
                assertTrue(GraphDataHelper.validateGraphStructure(filename), "Invalid graph structure in: " + filename);
            } catch (IOException e) {
                fail("Error reading file: " + filename);
            }
        }
    }

    @Test
    void testMultipleSCCsInMediumGraph() {
        String filename = "medium_scc_1.json";
        try {
            assertTrue(GraphDataHelper.hasMultipleSCCs(filename), "Graph should have multiple SCCs: " + filename);
        } catch (IOException e) {
            fail("Error reading file: " + filename);
        }
    }

    @Test
    void testSmallGraphDetails() {
        String[] filenames = {"small_sparse_1.json", "small_dense_1.json", "small_cyclic_1.json"};

        for (String filename : filenames) {
            try {
                Map<String, Integer> details = GraphDataHelper.getGraphDetails(filename);
                assertTrue(details.get("nodes") >= 6 && details.get("nodes") <= 10,
                        "Number of nodes should be between 6 and 10 in: " + filename);
                assertTrue(details.get("edges") > 0, "Graph should have at least one edge in: " + filename);
            } catch (IOException e) {
                fail("Error reading file: " + filename);
            }
        }
    }

    @Test
    void testMediumGraphDetails() {
        String[] filenames = {"medium_sparse_1.json", "medium_dense_1.json", "medium_scc_1.json"};

        for (String filename : filenames) {
            try {
                Map<String, Integer> details = GraphDataHelper.getGraphDetails(filename);
                assertTrue(details.get("nodes") >= 10 && details.get("nodes") <= 20,
                        "Number of nodes should be between 10 and 20 in: " + filename);
                assertTrue(details.get("edges") > 0, "Graph should have at least one edge in: " + filename);
            } catch (IOException e) {
                fail("Error reading file: " + filename);
            }
        }
    }

    @Test
    void testLargeGraphDetails() {
        String[] filenames = {"large_sparse_1.json", "large_dense_1.json", "large_scc_1.json"};

        for (String filename : filenames) {
            try {
                Map<String, Integer> details = GraphDataHelper.getGraphDetails(filename);
                assertTrue(details.get("nodes") >= 20 && details.get("nodes") <= 50,
                        "Number of nodes should be between 20 and 50 in: " + filename);
                assertTrue(details.get("edges") > 0, "Graph should have at least one edge in: " + filename);
            } catch (IOException e) {
                fail("Error reading file: " + filename);
            }
        }
    }
}
