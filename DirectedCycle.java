//-----------------------------------------------------
// Title: Directed Cycle Finder Class
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: HW2 - Q1
// Description: This class detects a directed cycle in a
// given digraph using DFS and on-stack technique.
//-----------------------------------------------------
import java.util.Stack;

public class DirectedCycle {

    private boolean[] marked;
    private int[] edgeTo;
    private boolean[] onStack;
    private Stack<Integer> cycle;  // if non-null, we found a cycle

    //--------------------------------------------------------
    // Summary: Runs DFS on the given digraph to find a cycle.
    // Precondition: G is not null
    // Postcondition: If a cycle exists, it will be stored in 'cycle'.
    //--------------------------------------------------------
    public DirectedCycle(Digraph G) {
        int V = G.V();
        marked = new boolean[V];
        onStack = new boolean[V];
        edgeTo = new int[V];
        for (int v = 0; v < V; v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    //--------------------------------------------------------
    // Summary: Performs depth-first search to detect cycles using on-stack technique.
    // Precondition: v is a valid vertex index, G is not null.
    // Postcondition: Marks vertices, updates edgeTo array, and stores cycle if found.
    //--------------------------------------------------------
    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (cycle != null) return; // already found
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            } else if (onStack[w]) {
                // found a cycle
                cycle = new Stack<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    //--------------------------------------------------------
    // Summary: Checks if the graph has a directed cycle.
    // Precondition: constructor must be called first.
    // Postcondition: true if a cycle is found.
    //--------------------------------------------------------
    public boolean hasCycle() {
        return cycle != null;
    }

    //--------------------------------------------------------
    // Summary: Returns the directed cycle found.
    // Precondition: hasCycle() == true
    // Postcondition: The cycle stack is returned.
    //--------------------------------------------------------
    public Iterable<Integer> cycle() {
        return cycle;
    }
}
