1. Prerequisites

Before running the project, ensure the following tools are installed:

Tool	Version	Description
Java JDK	17 or higher	Required to compile and run code
Maven	3.8+	Used to build and test project
JUnit 5	Included via Maven	For unit testing
org.json	Included via Maven	For reading graph data from JSON
2. Project Structure
   project-root/
   │
   ├── src/
   │   ├── main/java/main/java/graph/
   │   │   ├── data/          # GraphDataHelper and JSON parsing
   │   │   ├── scc/           # Strongly Connected Components (Tarjan)
   │   │   ├── topo/          # Topological Sort
   │   │   ├── metrics/       # MetricsTracker for timing/ops
   │   │   ├── paths/         # Shortest and Longest Path algorithms
   │   │
   │   └── test/java/main/java/test/graph/
   │       ├── scc/           # SCCTest.java
   │       ├── topo/          # TopoSortTest.java
   │       ├── shortest/      # ShortestPathsTest.java
   │       └── longest/       # LongestPathsTest.java
   │
   ├── data/
   │   ├── tasks.json
   │   ├── small_sparse_1.json
   │   ├── small_cyclic_1.json
   │   ├── medium_scc_1.json
   │   └── ...
   │
   ├── pom.xml
   └── README.md

3. Build the Project

From the project root, compile all Java sources:

mvn clean compile

4. Run All Tests

To run all algorithm tests (SCC, Topological Sort, Shortest Path, Longest Path):

mvn test


Or run individual test classes:
mvn -Dtest=SCCTest test
mvn -Dtest=TopoSortTest test
mvn -Dtest=ShortestPathsTest test
mvn -Dtest=LongestPathsTest test

5. Run from IDE (IntelliJ IDEA, VS Code, Eclipse)

Open the project in your IDE.

Make sure the working directory is set to the project root.

Open the desired test class (e.g. SCCTest.java).

Right-click → Run 'SCCTest'.

View algorithm performance and detected components in the console.

6. Example Output
   SCC (Tarjan’s Algorithm):
   SCC (Tarjan) completed — Time: 7.368 ms | Operations: 62
   Detected 2 SCCs:
   SCC 1 (size 9): [16, 15, 14, 13, 12, 11, 10, 9, 17]
   SCC 2 (size 9): [8, 7, 6, 5, 4, 3, 2, 1, 0]
   Condensation Graph (DAG of components): [[], [0]]

Topological Sort:
Topological Sort completed — Time: 6.929 ms | Operations: 8
⚠ Graph has cycles — topological order incomplete.

Shortest Path:
Shortest Path (DAG) finished — Time: 7.437 ms | Operations: 2
Shortest paths from node 0:
[0, 2147483647, 2147483647, 2, 2147483647, 1]

Longest Path:
Longest Path (DAG) finished — Time: 7.236 ms | Operations: 21
Critical path: [0, 4]

7. Notes

All input files must be located in the ./data directory.

File names used in tests must match exactly (e.g., "tasks.json").

The algorithms automatically print execution time and operation count for performance comparison.

You can extend GraphDataHelper to support additional file formats if needed.

That’s It!

After following these steps, all algorithms and tests should run successfully — just as your current results show.