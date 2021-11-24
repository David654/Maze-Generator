package com.company;

import java.awt.*;
import java.util.ArrayList;

public class Handler
{
    public ArrayList<Cell> cells = new ArrayList<>();
    public ArrayList<Integer> stack = new ArrayList<>();

    public void addCell(Cell cell)
    {
        cells.add(cell);
    }

    public void update()
    {

    }

    public void render(Graphics2D g2d)
    {
        for(Cell cell : cells)
        {
            cell.render(g2d);
        }
    }
}