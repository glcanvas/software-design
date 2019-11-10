package com.nduginets.softwaredesign.drawing;

import com.nduginets.softwaredesign.drawing.draw.DrawingApi;
import com.nduginets.softwaredesign.drawing.draw.Point;
import com.nduginets.softwaredesign.drawing.graph.AbstractGraph;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawGraph {

    public static void draw(AbstractGraph g, DrawingApi api) {
        List<Integer> vertex = g.getVertexes();
        double angelStep = (double) 360 / (double) vertex.size();
        int centerH = api.drawingAreaWidth() / 2;
        int centerW = api.drawingAreaHeight() / 2;
        int radius = Math.min(centerH, centerW) / 2;

        List<Point> points = IntStream.range(0, vertex.size())
                .boxed()
                .map(id -> {
                    double x = radius * Math.sin(angelStep * id) + centerH;
                    double y = radius * Math.cos(angelStep * id) + centerW;
                    return new Point((int) x, (int) y);
                })
                .collect(Collectors.toList());

        points.forEach(p -> api.drawCircle(p, 15));
        for (int idx : vertex) {
            List<Integer> edges = g.getEdgesFromVertex(idx);
            for (int jdx : edges) {
                api.drawLine(points.get(vertex.get(idx)), points.get(vertex.get(jdx)));
            }
        }
    }
}
