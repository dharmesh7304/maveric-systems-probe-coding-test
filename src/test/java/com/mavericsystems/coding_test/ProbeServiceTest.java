package com.mavericsystems.coding_test;

import com.mavericsystems.coding_test.constant.Direction;
import com.mavericsystems.coding_test.dto.ControlRequest;
import com.mavericsystems.coding_test.dto.ControlResponse;
import com.mavericsystems.coding_test.model.Coordinate;
import com.mavericsystems.coding_test.model.Grid;
import com.mavericsystems.coding_test.model.Position;
import com.mavericsystems.coding_test.service.ProbeService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProbeServiceTest {

    private final ProbeService service = new ProbeService();

    @Test
    void shouldMoveForwardWithoutObstacleOrBoundary() {
        Grid grid = new Grid(5, 5);
        Position position = new Position(1, 2, Direction.N);
        ControlRequest request = new ControlRequest(grid, position, "FF", List.of());

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(1, 4, Direction.N), response.getFinalPosition());
        assertEquals(List.of(
                new Coordinate(1, 2),
                new Coordinate(1, 3),
                new Coordinate(1, 4)
        ), response.getVisited());
        assertTrue(response.getBlockedByObstacle().isEmpty());
        assertTrue(response.getBlockedByBoundary().isEmpty());
    }

    //Add Tests for Turning and Backward
    @Test
    void shouldTurnLeftAndMove() {
        ControlRequest request = new ControlRequest(
                new Grid(5, 5),
                new Position(2, 2, Direction.N),
                "LFF",
                List.of()
        );

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(0, 2, Direction.W), response.getFinalPosition());
        assertEquals(List.of(
                new Coordinate(2, 2),
                new Coordinate(1, 2),
                new Coordinate(0, 2)
        ), response.getVisited());
    }

    //Test: Should Avoid Obstacle
    @Test
    void shouldStopBeforeObstacle() {
        ControlRequest request = new ControlRequest(
                new Grid(5, 5),
                new Position(1, 2, Direction.E),
                "FF",
                List.of(new Coordinate(3, 2))
        );

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(2, 2, Direction.E), response.getFinalPosition());
        assertEquals(List.of(
                new Coordinate(1, 2),
                new Coordinate(2, 2)
        ), response.getVisited());
        assertEquals(List.of(new Coordinate(3, 2)), response.getBlockedByObstacle());
    }

    //Test: Should Stop at Boundary
    @Test
    void shouldStopAtBoundary() {
        ControlRequest request = new ControlRequest(
                new Grid(3, 3),
                new Position(2, 2, Direction.E),
                "FF",
                List.of()
        );

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(2, 2, Direction.E), response.getFinalPosition());
        assertEquals(List.of(new Coordinate(2, 2)), response.getVisited());
        assertEquals(List.of(new Coordinate(3, 2), new Coordinate(4, 2)), response.getBlockedByBoundary());
    }

    //Empty Command String
    @Test
    void shouldStayInPlaceOnEmptyCommands() {
        ControlRequest request = new ControlRequest(
                new Grid(5, 5),
                new Position(2, 2, Direction.S),
                "", // No commands
                List.of()
        );

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(2, 2, Direction.S), response.getFinalPosition());
        assertEquals(List.of(new Coordinate(2, 2)), response.getVisited());
        assertTrue(response.getBlockedByObstacle().isEmpty());
        assertTrue(response.getBlockedByBoundary().isEmpty());
    }

    //2. Start at Corner, Try to Move Outside Grid
    @Test
    void shouldBlockMoveAtBottomLeftCorner() {
        ControlRequest request = new ControlRequest(
                new Grid(5, 5),
                new Position(0, 0, Direction.S),
                "F", // move South → y = -1 (out of bounds)
                List.of()
        );

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(0, 0, Direction.S), response.getFinalPosition());
        assertEquals(List.of(new Coordinate(0, 0)), response.getVisited());
        assertEquals(List.of(new Coordinate(0, -1)), response.getBlockedByBoundary());
    }

    //Full Loop (Turn 360° and Move)
    @Test
    void shouldRotateFullCircleAndMove() {
        ControlRequest request = new ControlRequest(
                new Grid(5, 5),
                new Position(2, 2, Direction.N),
                "RRRRFF", // rotate 360° and move forward twice
                List.of()
        );

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(2, 4, Direction.N), response.getFinalPosition());
        assertEquals(List.of(
                new Coordinate(2, 2),
                new Coordinate(2, 3),
                new Coordinate(2, 4)
        ), response.getVisited());
    }

    //Rapid Turns with Movement
    @Test
    void shouldHandleRapidTurningWithMoves() {
        ControlRequest request = new ControlRequest(
                new Grid(5, 5),
                new Position(1, 1, Direction.N),
                "RFLFRFLB",
                List.of(new Coordinate(2, 2)) // obstacle to avoid
        );

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(1, 1, Direction.W), response.getFinalPosition());
        assertEquals(List.of(
                new Coordinate(1, 1),
                new Coordinate(1, 2), // forward
                new Coordinate(2, 2), // blocked
                new Coordinate(1, 2)  // back to previous
        ), response.getVisited());
        assertEquals(List.of(new Coordinate(2, 2)), response.getBlockedByObstacle());
    }

    //Multiple Obstacles with Blocking
    @Test
    void shouldTrackMultipleObstaclesAndBlockedMoves() {
        ControlRequest request = new ControlRequest(
                new Grid(5, 5),
                new Position(0, 0, Direction.E),
                "FFFFFF", // try to go past (0,0) → (6,0)
                List.of(new Coordinate(3, 0), new Coordinate(4, 0))
        );

        ControlResponse response = service.controlProbe(request);

        assertEquals(new Position(2, 0, Direction.E), response.getFinalPosition());
        assertEquals(List.of(
                new Coordinate(0, 0),
                new Coordinate(1, 0),
                new Coordinate(2, 0)
        ), response.getVisited());
        assertEquals(List.of(
                new Coordinate(3, 0),
                new Coordinate(4, 0),
                new Coordinate(5, 0),
                new Coordinate(6, 0)
        ), response.getBlockedByObstacle());
        //the response is perfectly fine,above coordinate never visited because it starts 2,0 and 3,0 obstetrical
    }
}