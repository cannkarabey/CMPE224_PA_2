//-----------------------------------------------------
// Title: Directed Cycle Finder Class
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: HW2
// Description: Computes preorder, postorder and
// reverse postorder of a digraph.
//-----------------------------------------------------
import java.util.Stack;

public class DepthFirstOrder {

    private boolean[] marked;
    private Stack<Integer> reversePost;

    //--------------------------------------------------------
    // Summary: Initializes DFS order computation for the given digraph.
    // Precondition: G is not null.
    // Postcondition: All vertices are visited and reversePost is populated.
    //--------------------------------------------------------
    public DepthFirstOrder(Digraph G) {
        marked = new boolean[G.V()];
        reversePost = new Stack<>();
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    //--------------------------------------------------------
    // Summary: Performs depth-first search and adds vertex to reversePost after visiting all descendants.
    // Precondition: v is a valid vertex index, G is not null.
    // Postcondition: Vertex v and its descendants are marked, v is pushed to reversePost.
    //--------------------------------------------------------
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        reversePost.push(v);
    }

    //--------------------------------------------------------
    // Summary: Returns the reverse postorder of all vertices.
    // Precondition: Constructor must be called first.
    // Postcondition: Returns an iterable of vertices in reverse postorder.
    //--------------------------------------------------------
    public Iterable<Integer> reversePost() {
        return reversePost;
    }
}
