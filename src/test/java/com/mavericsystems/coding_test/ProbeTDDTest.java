package com.mavericsystems.coding_test;

import com.mavericsystems.coding_test.constant.Direction;
import com.mavericsystems.coding_test.unittestcases.Position;
import com.mavericsystems.coding_test.unittestcases.Probe;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProbeTDDTest {

    @Test
    void shouldMoveForwardNorth() {
        Probe probe = createProbe(2, 2, Direction.N);
        probe.executeCommands("F");
        assertEquals(new Position(2, 3), probe.getPosition());
    }

    @Test
    void shouldMoveBackwardSouth() {
        Probe probe = createProbe(2, 2, Direction.S);
        probe.executeCommands("B");
        assertEquals(new Position(2, 3), probe.getPosition());
    }

    @Test
    void shouldTurnLeftFromNorthToWest() {
        Probe probe = createProbe(1, 1, Direction.N);
        probe.executeCommands("L");
        assertEquals(Direction.W, probe.getDirection());
    }

    @Test
    void shouldTurnRightFromNorthToEast() {
        Probe probe = createProbe(1, 1, Direction.N);
        probe.executeCommands("R");
        assertEquals(Direction.E, probe.getDirection());
    }

    @Test
    void shouldNotMoveForwardIfBlockedByObstacle() {
        Set<Position> obstacles = Set.of(new Position(2, 3));
        Probe probe = new Probe(2, 2, Direction.N, obstacles, 6, 6);
        probe.executeCommands("F");
        assertEquals(new Position(2, 2), probe.getPosition());
    }

    @Test
    void shouldNotMoveOutsideGrid() {
        Probe probe = new Probe(0, 0, Direction.S, Set.of(), 6, 6);
        probe.executeCommands("F");
        assertEquals(new Position(0, 0), probe.getPosition());
    }

    @Test
    void shouldTrackVisitedCoordinates() {
        Probe probe = createProbe(2, 2, Direction.N);
        probe.executeCommands("FRFRF");

        List<Position> expected = List.of(
                new Position(2, 2),
                new Position(2, 3),
                new Position(3, 3),
                new Position(3, 2)
        );
        assertEquals(expected, probe.getVisited());
    }

    @Test
    void shouldReturnFinalPositionAndDirectionAfterCommandSequence() {
        Set<Position> obstacles = Set.of(new Position(3, 3));
        Probe probe = new Probe(2, 2, Direction.N, obstacles, 6, 6);
        probe.executeCommands("FRFLFF");

        assertEquals(new Position(2, 4), probe.getPosition());
        assertEquals(Direction.N, probe.getDirection());
    }

    private Probe createProbe(int x, int y, Direction direction) {
        return new Probe(x, y, direction, Set.of(), 6, 6);
    }
}
