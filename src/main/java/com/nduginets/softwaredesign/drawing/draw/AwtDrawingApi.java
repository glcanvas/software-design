package com.nduginets.softwaredesign.drawing.draw;

import com.nduginets.softwaredesign.drawing.DrawGraph;
import com.nduginets.softwaredesign.drawing.graph.AbstractGraph;

import java.awt.*;

public class AwtDrawingApi extends Frame implements DrawingApi {

    private final int width;
    private final int height;

    private Graphics g;
    private AbstractGraph graph;

    public AwtDrawingApi(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int drawingAreaWidth() {
        return width;
    }

    @Override
    public int drawingAreaHeight() {
        return height;
    }

    @Override
    public void drawCircle(Point p, int radius) {
        g.fillOval(p.getX(), p.getY(), radius, radius);
    }

    @Override
    public void drawLine(Point p1, Point p2) {
        g.setColor(Color.BLUE);
        g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public void show(AbstractGraph g) {
        this.graph = g;
        this.pack();
        this.setSize(width, height);
        this.setVisible(true);
        this.setResizable(false);
    }

    public void paint(Graphics g) {
        super.paintComponents(g);
        this.g = g;
        DrawGraph.draw(graph, this);
    }
}
