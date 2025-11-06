package main.java.test.graph.scc;

import main.java.graph.scc.SCC;
import main.java.graph.scc.SCCUtils;
import main.java.graph.data.GraphDataHelper;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    @Test
    void testMediumGraphSCC() throws IOException {
        String file = "medium_scc_1.json";
        assertTrue(GraphDataHelper.fileExists(file), "Graph file not found");

        List<List<Integer>> sccs = SCC.findSCCs(file);
        SCCUtils.printSCCs(sccs);

        assertTrue(SCCUtils.hasMultipleSCCs(sccs), "Expected multiple SCCs in graph");
    }

    @Test
    void testSmallGraphSCC() throws IOException {
        String file = "small_sparse_1.json";
        assertTrue(GraphDataHelper.fileExists(file), "Graph file not found");

        List<List<Integer>> sccs = SCC.findSCCs(file);
        assertNotNull(sccs);
        assertFalse(sccs.isEmpty(), "There should be at least one SCC");
    }
}
