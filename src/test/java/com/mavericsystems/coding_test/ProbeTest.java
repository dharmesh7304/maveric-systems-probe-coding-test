package com.mavericsystems.coding_test;

import com.mavericsystems.coding_test.unittestcases.Direction;
import com.mavericsystems.coding_test.unittestcases.Position;
import com.mavericsystems.coding_test.unittestcases.Probe;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProbeTest {

    @Test
    void testMoveForwardInAllDirections() {
        Set<Position> obstacles = Set.of();

        Probe probeN = new Probe(2, 2, Direction.N, obstacles, 6, 6);
        probeN.executeCommands("F");
        assertEquals(new Position(2, 3), probeN.position);

        Probe probeE = new Probe(2, 2, Direction.E, obstacles, 6, 6);
        probeE.executeCommands("F");
        assertEquals(new Position(3, 2), probeE.position);

        Probe probeS = new Probe(2, 2, Direction.S, obstacles, 6, 6);
        probeS.executeCommands("F");
        assertEquals(new Position(2, 1), probeS.position);

        Probe probeW = new Probe(2, 2, Direction.W, obstacles, 6, 6);
        probeW.executeCommands("F");
        assertEquals(new Position(1, 2), probeW.position);
    }

    @Test
    void testTurnAndBackwardMoves() {
        Probe probe = new Probe(2, 2, Direction.N, Set.of(), 6, 6);

        probe.executeCommands("R");
        assertEquals(Direction.E, probe.direction);

        probe.executeCommands("R");
        assertEquals(Direction.S, probe.direction);

        probe.executeCommands("L");
        assertEquals(Direction.E, probe.direction);

        probe.executeCommands("L");
        assertEquals(Direction.N, probe.direction);

        probe.executeCommands("B");
        assertEquals(new Position(2, 1), probe.position);
    }

    @Test
    void testObstacleAvoidance() {
        Set<Position> obstacles = Set.of(new Position(2, 3));
        Probe probe = new Probe(2, 2, Direction.N, obstacles, 6, 6);
        probe.executeCommands("F");
        assertEquals(new Position(2, 2), probe.position); // Blocked
    }

    @Test
    void testOutOfBounds() {
        Probe probe = new Probe(0, 0, Direction.S, Set.of(), 6, 6);
        probe.executeCommands("F");
        assertEquals(new Position(0, 0), probe.position); // Out of grid
    }

    @Test
    void testFullExecutionWithVisitedPath() {
        Set<Position> obstacles = Set.of(
                new Position(2, 3),
                new Position(3, 3),
                new Position(1, 1)
        );

        Probe probe = new Probe(2, 2, Direction.N, obstacles, 6, 6);
        probe.executeCommands("FRFLFFRBBLFF");

        assertEquals(new Position(1, 4), probe.position);
        assertEquals(Direction.N, probe.direction);

        List<Position> expectedVisited = List.of(
                new Position(2, 2),
                new Position(3, 2),
                new Position(2, 2),
                new Position(1, 2),
                new Position(1, 3),
                new Position(1, 4)
        );

        assertEquals(expectedVisited, probe.visited);
    }
}
