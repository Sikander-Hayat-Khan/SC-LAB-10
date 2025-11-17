package graph;

import java.util.*;

/**
 * An implementation of Graph using vertices list representation.
 */
public class ConcreteVerticesGraph implements Graph<String> {

    private final List<Vertex> vertices = new ArrayList<>();

    // Abstraction function:
    //   AF(c) = weighted directed graph with vertices in the list
    // Representation invariant:
    //   - vertices != null
    //   - each Vertex != null
    //   - vertex names are unique
    //   - all target weights >= 0
    // Safety from rep exposure:
    //   - returns copies or unmodifiable sets/maps
    //   - Vertex class encapsulates mutable targets

    public ConcreteVerticesGraph() {
        checkRep();
    }

    private void checkRep() {
        assert vertices != null;
        Set<String> names = new HashSet<>();
        for (Vertex v : vertices) {
            assert v != null;
            assert !names.contains(v.getName());
            names.add(v.getName());
            for (int w : v.getTargets().values()) {
                assert w >= 0;
            }
        }
    }

    private Vertex findVertex(String name) {
        for (Vertex v : vertices) {
            if (v.getName().equals(name)) return v;
        }
        return null;
    }

    @Override
    public boolean add(String vertex) {
        Objects.requireNonNull(vertex);
        if (findVertex(vertex) != null) return false;
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }

    @Override
    public int set(String source, String target, int weight) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        if (weight < 0) throw new IllegalArgumentException("weight < 0");

        Vertex src = findVertex(source);
        if (src == null) {
            src = new Vertex(source);
            vertices.add(src);
        }
        Vertex tgt = findVertex(target);
        if (tgt == null) {
            tgt = new Vertex(target);
            vertices.add(tgt);
        }

        int oldWeight = src.getTargets().getOrDefault(target, 0);
        if (weight == 0) {
            src.removeTarget(target);
        } else {
            src.addTarget(target, weight);
        }
        checkRep();
        return oldWeight;
    }

    @Override
    public boolean remove(String vertex) {
        Objects.requireNonNull(vertex);
        Vertex v = findVertex(vertex);
        if (v == null) return false;
        vertices.remove(v);
        for (Vertex u : vertices) {
            u.removeTarget(vertex);
        }
        checkRep();
        return true;
    }

    @Override
    public Set<String> vertices() {
        Set<String> names = new HashSet<>();
        for (Vertex v : vertices) names.add(v.getName());
        return Collections.unmodifiableSet(names);
    }

    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> result = new HashMap<>();
        for (Vertex v : vertices) {
            if (v.getTargets().containsKey(target)) {
                result.put(v.getName(), v.getTargets().get(target));
            }
        }
        return result;
    }

    @Override
    public Map<String, Integer> targets(String source) {
        Vertex v = findVertex(source);
        if (v == null) return Collections.emptyMap();
        return new HashMap<>(v.getTargets());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex v : vertices) {
            sb.append(v.getName()).append(": ").append(v.getTargets()).append("\n");
        }
        return sb.toString();
    }

    /** Mutable vertex class for ConcreteVerticesGraph */
    static class Vertex {
        private final String name;
        private final Map<String, Integer> targets = new HashMap<>();

        // Abstraction function:
        //   AF(v) = vertex with name and its outgoing edges with weights
        // Representation invariant:
        //   - name != null
        //   - all weights >= 0
        // Safety from rep exposure:
        //   - targets is private, returns copies on access

        public Vertex(String name) {
            this.name = Objects.requireNonNull(name);
            checkRep();
        }

        private void checkRep() {
            assert name != null;
            for (int w : targets.values()) {
                assert w >= 0;
            }
        }

        public String getName() { return name; }
        public Map<String, Integer> getTargets() { return new HashMap<>(targets); }
        public void addTarget(String target, int weight) {
            Objects.requireNonNull(target);
            if (weight < 0) throw new IllegalArgumentException("weight < 0");
            targets.put(target, weight);
            checkRep();
        }
        public void removeTarget(String target) {
            targets.remove(target);
            checkRep();
        }

        @Override
        public String toString() {
            return name + " -> " + targets;
        }
    }
}
