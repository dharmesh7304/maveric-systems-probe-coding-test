package com.mavericsystems.coding_test.service;

import com.mavericsystems.coding_test.constant.Direction;
import com.mavericsystems.coding_test.dto.ControlRequest;
import com.mavericsystems.coding_test.dto.ControlResponse;
import com.mavericsystems.coding_test.model.Coordinate;
import com.mavericsystems.coding_test.model.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProbeService {

    public ControlResponse controlProbe(ControlRequest req) {
        int maxX = req.getGrid().width();
        int maxY = req.getGrid().height();

        Position pos = req.getStart();
        Direction direction = pos.direction();
        int x = pos.x();
        int y = pos.y();

        Set<String> obstacleSet = req.getObstacles().stream()
            .map(c -> c.x() + "," + c.y())
            .collect(Collectors.toSet());

        List<Coordinate> visited = new ArrayList<>();
        List<Coordinate> blockedByObstacle = new ArrayList<>();
        List<Coordinate> blockedByBoundary = new ArrayList<>();

        visited.add(new Coordinate(x, y));

        List<String> traceLog = new ArrayList<>();
        traceLog.add("START at (" + x + "," + y + ") facing " + direction);

        for (char cmd : req.getCommands().toCharArray()) {
            int nextX = x, nextY = y;

            switch (cmd) {
                case 'L' -> {
                    direction = direction.turnLeft();
                    traceLog.add("Turned LEFT → Now facing " + direction);
                }
                case 'R' -> {
                    direction = direction.turnRight();
                    traceLog.add("Turned RIGHT → Now facing " + direction);
                }
                case 'F', 'B' -> {
                    String moveType = (cmd == 'F') ? "FORWARD" : "BACKWARD";
                    switch (direction) {
                        case N -> nextY += (cmd == 'F' ? 1 : -1);
                        case S -> nextY -= (cmd == 'F' ? 1 : -1);
                        case E -> nextX += (cmd == 'F' ? 1 : -1);
                        case W -> nextX -= (cmd == 'F' ? 1 : -1);
                    }

                    String key = nextX + "," + nextY;
                    if (nextX < 0 || nextX >= maxX || nextY < 0 || nextY >= maxY) {
                        blockedByBoundary.add(new Coordinate(nextX, nextY));
                        traceLog.add("BLOCKED at (" + nextX + "," + nextY + ") → OUT OF BOUNDS");
                    } else if (obstacleSet.contains(key)) {
                        blockedByObstacle.add(new Coordinate(nextX, nextY));
                        traceLog.add("BLOCKED at (" + nextX + "," + nextY + ") → OBSTACLE");
                    } else {
                        x = nextX;
                        y = nextY;
                        visited.add(new Coordinate(x, y));
                        traceLog.add("Moved " + moveType + " to (" + x + "," + y + ")");
                    }
                }
            }
        }
        traceLog.add("END at (" + x + "," + y + ") facing " + direction);
        Position finalPosition = new Position(x, y, direction);
        return new ControlResponse(finalPosition, visited, blockedByObstacle, blockedByBoundary,traceLog);
    }
}
