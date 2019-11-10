package com.nduginets.softwaredesign.drawing.graph;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListGraph extends AbstractGraph {

    private final List<Integer> vertexes;
    private final List<Pair<Integer, Integer>> edges;

    public ListGraph(List<Pair<Integer, Integer>> edges) {
        super(countVertex(edges), edges.size());
        this.vertexes = getVertexes(edges);
        this.edges = edges;
    }

    @Override
    public List<Integer> getVertexes() {
        return vertexes;
    }

    @Override
    public List<Integer> getEdgesFromVertex(int v) {
        Set<Integer> unique = new HashSet<>();
        for (Pair<Integer, Integer> p : edges) {
            if (p.getKey() == v) {
                unique.add(p.getValue());
            }
        }
        return new ArrayList<>(unique);
    }

    private static int countVertex(List<Pair<Integer, Integer>> e) {
        Set<Integer> v = new HashSet<>();
        for (Pair<Integer, Integer> p : e) {
            v.add(p.getKey());
            v.add(p.getValue());
        }
        return v.stream().max(Integer::compareTo).orElse(0) + 1;
    }

    private static List<Integer> getVertexes(List<Pair<Integer, Integer>> e) {
        return IntStream.range(0, countVertex(e)).boxed().collect(Collectors.toList());
    }
}
