package main.java.graph.metrics;

public class MetricsTracker {

    private long startTime;
    private long endTime;
    private int operationCount;

    public void start() {
        startTime = System.nanoTime();
        operationCount = 0;
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public void recordOperation() {
        operationCount++;
    }

    public double getElapsedTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public long getElapsedTimeNs() {
        return endTime - startTime;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public void reset() {
        startTime = 0;
        endTime = 0;
        operationCount = 0;
    }

    @Override
    public String toString() {
        return String.format("Time: %.3f ms | Operations: %d", getElapsedTimeMs(), operationCount);
    }
}
