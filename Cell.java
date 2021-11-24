package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Cell
{
    private int x, y;
    private final Handler handler;

    private final HashMap<String, Boolean> walls;
    private boolean visited = false;

    private final int rows = Settings.width / Settings.tile_size;
    private final int cols = Settings.height / Settings.tile_size;

    public Cell(int x, int y, Handler handler)
    {
        this.x = x;
        this.y = y;
        this.handler = handler;

        walls = new HashMap<>();
        walls.put("top", true);
        walls.put("right", true);
        walls.put("bottom", true);
        walls.put("left", true);
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    public void set(String key, boolean value)
    {
        walls.replace(key, value);
    }

    public void render(Graphics2D g2d)
    {
        if(visited)
        {
            g2d.setColor(Settings.bg2);
            g2d.fillRect(x * Settings.tile_size, y * Settings.tile_size, Settings.tile_size, Settings.tile_size);
        }

        g2d.setColor(Settings.line_color);
        if(walls.get("top")) g2d.drawLine(x * Settings.tile_size, y * Settings.tile_size, x * Settings.tile_size + Settings.tile_size, y * Settings.tile_size);
        if(walls.get("right")) g2d.drawLine(x * Settings.tile_size + Settings.tile_size, y * Settings.tile_size, x * Settings.tile_size + Settings.tile_size, y * Settings.tile_size + Settings.tile_size);
        if(walls.get("bottom")) g2d.drawLine(x * Settings.tile_size, y * Settings.tile_size + Settings.tile_size, x * Settings.tile_size + Settings.tile_size, y * Settings.tile_size + Settings.tile_size);
        if(walls.get("left")) g2d.drawLine(x * Settings.tile_size, y * Settings.tile_size, x * Settings.tile_size, y * Settings.tile_size + Settings.tile_size);
    }

    public void drawCurrentCell(Graphics2D g2d)
    {
        g2d.setColor(Settings.curr_cell_color);
        g2d.fillRect(x * Settings.tile_size + 1, y * Settings.tile_size + 1, Settings.tile_size - 1, Settings.tile_size - 1);
    }

    public boolean check_cell(int x, int y)
    {
        return x >= 0 && x <= rows - 1 && y >= 0 && y <= cols - 1;
    }

    public int check_neighbours()
    {
        ArrayList<Integer> neighbours = new ArrayList<>();
        boolean top = check_cell(x, y - 1);
        boolean right = check_cell(x + 1, y);
        boolean bottom = check_cell(x, y + 1);
        boolean left = check_cell(x - 1, y);

        int top_index = x * cols + y - 1;
        int right_index = (x + 1) * cols + y;
        int bottom_index = x * cols + y + 1;
        int left_index = (x - 1) * cols + y;

        if(top && !handler.cells.get(top_index).visited) neighbours.add(top_index);
        if(right && !handler.cells.get(right_index).visited) neighbours.add(right_index);
        if(bottom && !handler.cells.get(bottom_index).visited) neighbours.add(bottom_index);
        if(left && !handler.cells.get(left_index).visited) neighbours.add(left_index);

        Random r = new Random();
        return neighbours.size() > 0 ? neighbours.get(r.nextInt(neighbours.size())) : -1;
    }
}