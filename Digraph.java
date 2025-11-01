//-----------------------------------------------------
// Title: Directed Graph Class
// Author: YOUR_NAME
// ID: YOUR_ID
// Section: YOUR_SECTION
// Assignment: HW2 - Q1/Q2
// Description: This class represents a directed graph
// using adjacency lists. It will be used in both
// scheduler (topological sort) and SCC questions.
//-----------------------------------------------------
import java.util.ArrayList;
import java.util.List;

public class Digraph {

    private final int V;               // number of vertices
    private int E;                     // number of edges
    private List<Integer>[] adj;       // adjacency lists

    //--------------------------------------------------------
    // Summary: Constructs a digraph with V vertices and no edges.
    // Precondition: V > 0
    // Postcondition: A new directed graph with V empty adjacency lists is created.
    //--------------------------------------------------------
    @SuppressWarnings("unchecked")
    public Digraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (List<Integer>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    //--------------------------------------------------------
    // Summary: Adds a directed edge v -> w to the graph.
    // Precondition: 0 <= v < V, 0 <= w < V
    // Postcondition: Edge (v, w) is inserted into adjacency list of v.
    //--------------------------------------------------------
    public void addEdge(int v, int w) {
        // optional: ignore self-loops or parallels if needed
        adj[v].add(w);
        E++;
    }

    //--------------------------------------------------------
    // Summary: Returns the adjacency list of vertex v.
    // Precondition: 0 <= v < V
    // Postcondition: The adjacency list of v is returned.
    //--------------------------------------------------------
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    //--------------------------------------------------------
    // Summary: Returns number of vertices.
    // Precondition: none
    // Postcondition: vertex count is returned.
    //--------------------------------------------------------
    public int V() {
        return V;
    }

    //--------------------------------------------------------
    // Summary: Returns number of edges.
    // Precondition: none
    // Postcondition: edge count is returned.
    //--------------------------------------------------------
    public int E() {
        return E;
    }

    //--------------------------------------------------------
    // Summary: Returns the reverse of this digraph.
    // Precondition: none
    // Postcondition: A new digraph whose edges are reversed is returned.
    //--------------------------------------------------------
    public Digraph reverse() {
        Digraph R = new Digraph(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj[v]) {
                R.addEdge(w, v);
            }
        }
        return R;
    }
}
