package com.nduginets.softwaredesign.drawing.draw;

import com.nduginets.softwaredesign.drawing.graph.AbstractGraph;

public interface DrawingApi {

    int drawingAreaWidth();

    int drawingAreaHeight();

    void drawCircle(Point p, int radius);

    void drawLine(Point p1, Point p2);

}
