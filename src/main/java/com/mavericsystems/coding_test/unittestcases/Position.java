package com.mavericsystems.coding_test.unittestcases;

import java.util.Objects;

public class Position {

    int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    Position move(Direction dir, boolean forward) {
        int dx = 0, dy = 0;
        switch (dir) {
            case N -> dy = 1;
            case S -> dy = -1;
            case E -> dx = 1;
            case W -> dx = -1;
        }
        if (!forward) {
            dx = -dx;
            dy = -dy;
        }
        return new Position(this.x + dx, this.y + dy);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return p.x == x && p.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
