package com.nduginets.softwaredesign.drawing.graph;

import java.util.List;

public abstract class AbstractGraph {
    protected final int vertex;
    protected final int edges;

    public AbstractGraph(int vertex, int edges) {
        this.edges = edges;
        this.vertex = vertex;
    }

    public abstract List<Integer> getVertexes();

    public abstract List<Integer> getEdgesFromVertex(int v);

    public int getVertex() {
        return vertex;
    }

    public int getEdges() {
        return edges;
    }
}
