package main.java.test.graph.metrics;

import main.java.graph.metrics.MetricsTracker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MetricsTrackerTest {

    @Test
    void testTimingAndOperations() {
        MetricsTracker tracker = new MetricsTracker();
        tracker.start();

        for (int i = 0; i < 1000; i++) {
            tracker.recordOperation();
        }

        tracker.stop();

        assertTrue(tracker.getElapsedTimeMs() >= 0, "Time should be non-negative");
        assertEquals(1000, tracker.getOperationCount(), "Operation count should match recorded ops");
    }

    @Test
    void testResetFunctionality() {
        MetricsTracker tracker = new MetricsTracker();
        tracker.start();
        tracker.recordOperation();
        tracker.stop();
        tracker.reset();

        assertEquals(0, tracker.getOperationCount());
        assertEquals(0, tracker.getElapsedTimeNs());
    }
}