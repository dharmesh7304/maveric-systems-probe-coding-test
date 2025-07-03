package com.mavericsystems.coding_test.constant;

import java.util.List;

public enum Direction {
    E,W,N,S;
    private static final List<Direction> directions = List.of(N, E, S, W);

    public Direction turnLeft() {
        int index = (directions.indexOf(this) - 1 + directions.size()) % directions.size();
        return directions.get(index);
    }

    public Direction turnRight() {
        int index = (directions.indexOf(this) + 1) % directions.size();
        return directions.get(index);
    }
}
