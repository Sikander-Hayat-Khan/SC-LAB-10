/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {

    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    // Abstraction function:
    //      AF(c) = A weighted directed graph with vertices "vertices"
    //              and edges as listed in "edges".
    //
    // Representation invariant:
    //      vertices != null
    //      edges != null
    //      every edge's source and target exist in vertices
    //      all edge weights >= 0
    //
    // Safety from rep exposure:
    //      vertices() returns unmodifiable set
    //      edges are immutable (Edge is immutable)
    //      sources() and targets() return new Maps each time

    public ConcreteEdgesGraph() {
        checkRep();
    }

    private void checkRep() {
        for (Edge e : edges) {
            assert vertices.contains(e.getSource()) : "edge source not in vertices";
            assert vertices.contains(e.getTarget()) : "edge target not in vertices";
            assert e.getWeight() >= 0 : "edge weight negative";
        }
    }

    @Override
    public boolean add(String vertex) {
        Objects.requireNonNull(vertex);
        boolean added = vertices.add(vertex);
        checkRep();
        return added;
    }

    @Override
    public int set(String source, String target, int weight) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        if (weight < 0) throw new IllegalArgumentException("weight < 0");

        vertices.add(source);
        vertices.add(target);

        int oldWeight = 0;
        Iterator<Edge> it = edges.iterator();
        while (it.hasNext()) {
            Edge e = it.next();
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                oldWeight = e.getWeight();
                if (weight == 0) {
                    it.remove();
                } else {
                    it.remove();
                    edges.add(new Edge(source, target, weight));
                }
                checkRep();
                return oldWeight;
            }
        }
        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }
        checkRep();
        return oldWeight;
    }

    @Override
    public boolean remove(String vertex) {
        Objects.requireNonNull(vertex);
        boolean removed = vertices.remove(vertex);
        if (removed) {
            edges.removeIf(e -> e.getSource().equals(vertex) || e.getTarget().equals(vertex));
        }
        checkRep();
        return removed;
    }

    @Override
    public Set<String> vertices() {
        return Collections.unmodifiableSet(new HashSet<>(vertices));
    }

    @Override
    public Map<String, Integer> sources(String target) {
        Objects.requireNonNull(target);
        Map<String, Integer> result = new HashMap<>();
        for (Edge e : edges) {
            if (e.getTarget().equals(target)) {
                result.put(e.getSource(), e.getWeight());
            }
        }
        return result;
    }

    @Override
    public Map<String, Integer> targets(String source) {
        Objects.requireNonNull(source);
        Map<String, Integer> result = new HashMap<>();
        for (Edge e : edges) {
            if (e.getSource().equals(source)) {
                result.put(e.getTarget(), e.getWeight());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices: ").append(vertices).append("\nEdges:\n");
        for (Edge e : edges) {
            sb.append("  ").append(e.toString()).append("\n");
        }
        return sb.toString();
    }

    /** Immutable edge class for ConcreteEdgesGraph */
    static class Edge {
        private final String source;
        private final String target;
        private final int weight;

        // Abstraction function:
        //   AF(e) = a directed edge from source to target with weight
        // Representation invariant:
        //   - source != null
        //   - target != null
        //   - weight >= 0
        // Safety from rep exposure:
        //   - all fields are private final, immutable

        public Edge(String source, String target, int weight) {
            Objects.requireNonNull(source);
            Objects.requireNonNull(target);
            if (weight < 0) throw new IllegalArgumentException("weight < 0");
            this.source = source;
            this.target = target;
            this.weight = weight;
            checkRep();
        }

        private void checkRep() {
            assert source != null;
            assert target != null;
            assert weight >= 0;
        }

        public String getSource() { return source; }
        public String getTarget() { return target; }
        public int getWeight() { return weight; }

        @Override
        public String toString() {
            return source + " -> " + target + " (" + weight + ")";
        }
    }
}

