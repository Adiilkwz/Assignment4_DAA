package main.java.graph.scc;

import java.util.*;

public class SCCUtils {

    public static void printSCCs(List<List<Integer>> sccs) {
        System.out.println("Detected " + sccs.size() + " strongly connected components:");
        for (int i = 0; i < sccs.size(); i++) {
            System.out.println("SCC " + (i + 1) + ": " + sccs.get(i));
        }
    }

    public static boolean hasMultipleSCCs(List<List<Integer>> sccs) {
        return sccs.size() > 1;
    }
}
