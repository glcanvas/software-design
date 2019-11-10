package com.nduginets.softwaredesign.drawing;

import com.nduginets.softwaredesign.drawing.draw.AwtDrawingApi;
import com.nduginets.softwaredesign.drawing.draw.FxDrawingApi;
import com.nduginets.softwaredesign.drawing.graph.AbstractGraph;
import com.nduginets.softwaredesign.drawing.graph.ListGraph;
import com.nduginets.softwaredesign.drawing.graph.MatrixGraph;
import javafx.application.Application;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DrawMain {

    public static void main(String[] args) {
        boolean[][] m = new boolean[][]{
                {false, true, false, false},
                {false, false, true, false},
                {false, false, false, true},
                {true, false, false, false}
        };
        List<Pair<Integer, Integer>> edges = new ArrayList<>();
        edges.add(new Pair<>(0, 1));
        edges.add(new Pair<>(2, 5));

        AbstractGraph g = new MatrixGraph(m);
        AbstractGraph g2 = new ListGraph(edges);
        /*FxDrawingApi.setSize(400, 600);
        FxDrawingApi.setGraph(g);
        Application.launch(FxDrawingApi.class);
*/
        AwtDrawingApi awt = new AwtDrawingApi(400, 600);

        awt.show(g2);
    }
}
