package com.company;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Stack;

public class Scene extends Canvas implements Runnable
{
    private final Window window;
    private Thread thread;
    private boolean running = false;

    private final Handler handler;
    private Cell curr_cell;
    private Cell next_cell;
    private Stack<Cell> stack = new Stack<>();

    public Scene()
    {
        this.setPreferredSize(new Dimension(Settings.width, Settings.height));

        handler = new Handler();
        for(int x = 0; x < Settings.width / Settings.tile_size; x++)
        {
            for(int y = 0; y < Settings.height / Settings.tile_size; y++)
            {
                handler.addCell(new Cell(x, y, handler));
            }
        }
        curr_cell = handler.cells.get(0);

        window = new Window(Settings.width, Settings.height, Settings.title, this);
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop()
    {
        try
        {
            thread.join();
            running = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1)
            {
                update();
                delta--;
            }
            if(running)
            {
                render();
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                window.setTitle(frames + " fps | " + Settings.title);
                frames = 0;
            }
        }
        stop();
    }

    private void update()
    {
        curr_cell.setVisited(true);
        int index = curr_cell.check_neighbours();
        if(index != -1)
        {
            next_cell = handler.cells.get(index);
            next_cell.setVisited(true);
            stack.add(curr_cell);
            removeWalls(curr_cell, next_cell);
            curr_cell = next_cell;
        }
        else
        {
            if(!stack.isEmpty()) curr_cell = stack.pop();
        }
    }

    private void removeWalls(Cell curr_cell, Cell next_cell)
    {
        int dx = curr_cell.getX() - next_cell.getX();
        int dy = curr_cell.getY() - next_cell.getY();

        if(dx == 1)
        {
            curr_cell.set("left", false);
            next_cell.set("right", false);
        }
        else if(dx == -1)
        {
            curr_cell.set("right", false);
            next_cell.set("left", false);
        }

        if(dy == 1)
        {
            curr_cell.set("top", false);
            next_cell.set("bottom", false);
        }
        else if(dy == -1)
        {
            curr_cell.set("bottom", false);
            next_cell.set("top", false);
        }
    }

    private void render()
    {
        BufferStrategy bs = this.getBufferStrategy();

        if(bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Settings.bg);
        g2d.fillRect(0, 0, Settings.width, Settings.height);

        handler.render(g2d);
        curr_cell.drawCurrentCell(g2d);

        g2d.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        new Scene();
    }
}