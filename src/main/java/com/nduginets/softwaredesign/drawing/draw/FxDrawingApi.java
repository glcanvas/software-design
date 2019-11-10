package com.nduginets.softwaredesign.drawing.draw;

import com.nduginets.softwaredesign.drawing.DrawGraph;
import com.nduginets.softwaredesign.drawing.graph.AbstractGraph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class FxDrawingApi extends Application implements DrawingApi {

    private static int width = 600;
    private static int height = 500;
    private static AbstractGraph graph;

    private GraphicsContext gc;

    public static void setSize(int widthS, int heightS) {
        width = widthS;
        height = heightS;
    }

    public static void setGraph(AbstractGraph g) {
        graph = g;
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
        gc.setFill(Color.BLACK);
        gc.fillOval(p.getX(), p.getY(), radius, radius);
    }

    @Override
    public void drawLine(Point p1, Point p2) {
        gc.setFill(Color.BLUE);
        gc.moveTo(p1.getX(), p1.getY());
        gc.lineTo(p2.getX(), p2.getY());
        gc.stroke();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        DrawGraph.draw(graph, this);
        VBox root = new VBox(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
