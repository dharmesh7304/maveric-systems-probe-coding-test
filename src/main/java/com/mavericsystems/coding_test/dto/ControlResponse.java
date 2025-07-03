package com.mavericsystems.coding_test.dto;

import com.mavericsystems.coding_test.model.Coordinate;
import com.mavericsystems.coding_test.model.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ControlResponse {
    public Position finalPosition;
    public List<Coordinate> visited;
    public List<Coordinate> blockedByObstacle;
    public List<Coordinate> blockedByBoundary;
    public List<String> traceLog;
}