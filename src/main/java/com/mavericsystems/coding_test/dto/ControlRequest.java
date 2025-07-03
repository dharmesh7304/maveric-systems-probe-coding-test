package com.mavericsystems.coding_test.dto;

import com.mavericsystems.coding_test.model.Coordinate;
import com.mavericsystems.coding_test.model.Grid;
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
public class ControlRequest {
    public Grid grid;
    public Position start;
    public String commands;
    public List<Coordinate> obstacles;
}