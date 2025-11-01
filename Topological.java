//-----------------------------------------------------
// Title: Directed Cycle Finder Class
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: HW2 - Q1
// Description: If a digraph is a DAG, this class
// computes its topological order using reverse postorder.
//-----------------------------------------------------
public class Topological {

    private Iterable<Integer> order;

    //--------------------------------------------------------
    // Summary: Creates a topological order if graph is a DAG.
    // Precondition: G is not null
    // Postcondition: order will be null if G has a cycle.
    //--------------------------------------------------------
    public Topological(Digraph G) {
        DirectedCycle cycleFinder = new DirectedCycle(G);
        if (!cycleFinder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        } else {
            order = null;
        }
    }

    //--------------------------------------------------------
    // Summary: Returns the topological order.
    // Precondition: Graph must be DAG.
    // Postcondition: topological order is returned or null.
    //--------------------------------------------------------
    public Iterable<Integer> order() {
        return order;
    }

    //--------------------------------------------------------
    // Summary: Checks if the graph is a directed acyclic graph (DAG).
    // Precondition: Constructor must be called first.
    // Postcondition: Returns true if graph is a DAG, false otherwise.
    //--------------------------------------------------------
    public boolean isDAG() {
        return order != null;
    }
}
