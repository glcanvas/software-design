package com.nduginets.softwaredesign.drawing.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixGraph extends AbstractGraph {

    private final boolean[][] matrix;

    public MatrixGraph(boolean[][] matrix) {
        super(matrix.length, countEdges(matrix));
        this.matrix = matrix;
    }


    @Override
    public List<Integer> getVertexes() {
        return IntStream.range(0, vertex).boxed().collect(Collectors.toList());
    }

    @Override
    public List<Integer> getEdgesFromVertex(int v) {
        List<Integer> vertexes = new ArrayList<>(vertex);
        for (int i = 0; i < matrix[v].length; i++) {
            if (matrix[v][i]) {
                vertexes.add(i);
            }
        }
        return vertexes;
    }

    private static int countEdges(boolean[][] m) {
        int cnt = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = i; j < m.length; j++) {
                if (m[i][j]) {
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
