package com.nduginets.softwaredesign.drawing.draw;


public interface DrawingApi {

    int drawingAreaWidth();

    int drawingAreaHeight();

    void drawCircle(Point p, int radius);

    void drawLine(Point p1, Point p2);

}
