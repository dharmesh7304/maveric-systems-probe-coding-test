package com.mavericsystems.coding_test.unittestcases;

import com.mavericsystems.coding_test.constant.Direction;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class Probe {
    public Position position;
    public Direction direction;
    Set<Position> obstacles;
    int gridWidth, gridHeight;
    public List<Position> visited;

    public Probe(int dx,int dy,Direction direction, Set<Position> obstacles, int gridWidth, int gridHeight) {
        this.position = new Position(dx,dy);
        this.direction = direction;
        this.obstacles = obstacles;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.visited = new ArrayList<>();
        this.visited.add(this.position);
    }

    boolean isInsideGrid(Position pos) {
        return pos.x >= 0 && pos.x < gridWidth && pos.y >= 0 && pos.y < gridHeight;
    }

    public void executeCommands(String commands) {
        for (char cmd : commands.toCharArray()) {
            switch (cmd) {
                case 'F' -> move(true);
                case 'B' -> move(false);
                case 'L' -> direction = direction.turnLeft();
                case 'R' -> direction = direction.turnRight();
            }
        }
    }
    void move(boolean forward) {
        Position nextPos = position.move(direction, forward);
        if (isInsideGrid(nextPos) && !obstacles.contains(nextPos)) {
            position = nextPos;
            visited.add(new Position(position.x, position.y));
        } else {
            System.out.println("Blocked at " + nextPos + " — obstacle or outside grid.");
        }
    }
    void printStatus() {
        System.out.println("Final Position: " + position);
        System.out.println("Facing: " + direction);
        System.out.println("Visited Coordinates: ");
        for (Position p : visited) {
            System.out.println(" → " + p);
        }
    }

}
