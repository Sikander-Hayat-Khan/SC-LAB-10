/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   TODO
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // TODO other tests for instance methods of Graph
    @Test
    public void testAddVertex() {
        Graph<String> g = emptyInstance();
        assertTrue(g.add("A"));
        assertTrue(g.add("B"));
        assertEquals(Set.of("A", "B"), g.vertices());
    }

    @Test
    public void testAddDuplicateVertex() {
        Graph<String> g = emptyInstance();
        assertTrue(g.add("X"));
        assertFalse(g.add("X"));  // duplicate
        assertEquals(Set.of("X"), g.vertices());
    }

    @Test
    public void testRemoveVertexNotInGraph() {
        Graph<String> g = emptyInstance();
        assertFalse(g.remove("Z"));
    }

    @Test
    public void testRemoveExistingVertexNoEdges() {
        Graph<String> g = emptyInstance();
        g.add("A");
        assertTrue(g.remove("A"));
        assertTrue(g.vertices().isEmpty());
    }

    @Test
    public void testRemoveVertexWithEdges() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 5);
        g.set("C", "A", 2);
        assertTrue(g.remove("A"));
        assertEquals(Set.of("B", "C"), g.vertices());
        assertTrue(g.sources("B").isEmpty());
        assertTrue(g.targets("C").isEmpty());
    }

    @Test
    public void testSetAddNewEdge() {
        Graph<String> g = emptyInstance();
        int previous = g.set("A", "B", 10);
        assertEquals(0, previous);
        assertEquals(Set.of("A", "B"), g.vertices());
        assertEquals(Map.of("A", 10), g.sources("B"));
        assertEquals(Map.of("B", 10), g.targets("A"));
    }

    @Test
    public void testSetUpdateExistingEdge() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 5);
        int previous = g.set("A", "B", 9);
        assertEquals(5, previous);
        assertEquals(Map.of("B", 9), g.targets("A"));
    }

    @Test
    public void testSetRemoveExistingEdge() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 4);
        int previous = g.set("A", "B", 0);
        assertEquals(4, previous);
        assertTrue(g.targets("A").isEmpty());
    }

    @Test
    public void testSetRemoveNonExistingEdge() {
        Graph<String> g = emptyInstance();
        int previous = g.set("X", "Y", 0);
        assertEquals(0, previous);
    }

    @Test
    public void testVerticesAfterAddAndRemove() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");
        g.add("C");
        g.remove("B");
        assertEquals(Set.of("A", "C"), g.vertices());
    }

    @Test
    public void testSourcesMultiple() {
        Graph<String> g = emptyInstance();
        g.set("A", "C", 1);
        g.set("B", "C", 2);
        assertEquals(Map.of("A", 1, "B", 2), g.sources("C"));
    }

    @Test
    public void testTargetsMultiple() {
        Graph<String> g = emptyInstance();
        g.set("A", "X", 3);
        g.set("A", "Y", 4);
        assertEquals(Map.of("X", 3, "Y", 4), g.targets("A"));
    }

    @Test
    public void testNoSourcesOrTargetsForIsolatedVertex() {
        Graph<String> g = emptyInstance();
        g.add("A");
        assertTrue(g.sources("A").isEmpty());
        assertTrue(g.targets("A").isEmpty());
    }
    
}
