//-----------------------------------------------------
// Title: Directed Cycle Finder Class
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: HW2 - Q2
// Description: Computes strongly connected components
// of a digraph using 2-pass Kosaraju-Sharir algorithm.
//-----------------------------------------------------
public class KosarajuSCC {

    private boolean[] marked;
    private int[] id;
    private int count;

    //--------------------------------------------------------
    // Summary: Runs Kosaraju algorithm on digraph G.
    // Precondition: G is not null
    // Postcondition: Each vertex is assigned a component id.
    //--------------------------------------------------------
    public KosarajuSCC(Digraph G) {
        // 1) run DFS on reverse graph to get reverse postorder
        Digraph R = G.reverse();
        DepthFirstOrder order = new DepthFirstOrder(R);

        // 2) run DFS on original graph in that order
        marked = new boolean[G.V()];
        id = new int[G.V()];

        for (int v : order.reversePost()) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    //--------------------------------------------------------
    // Summary: Performs depth-first search and assigns component id to vertices.
    // Precondition: v is a valid vertex index, G is not null.
    // Postcondition: Vertex v and its descendants are marked with current component id.
    //--------------------------------------------------------
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    //--------------------------------------------------------
    // Summary: Returns the number of strongly connected components.
    // Precondition: Constructor must be called first.
    // Postcondition: The count of SCCs is returned.
    //--------------------------------------------------------
    public int count() {
        return count;
    }

    //--------------------------------------------------------
    // Summary: Returns the component id of vertex v.
    // Precondition: 0 <= v < V, constructor must be called first.
    // Postcondition: The component id of v is returned.
    //--------------------------------------------------------
    public int id(int v) {
        return id[v];
    }
}
