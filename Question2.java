//-----------------------------------------------------
// Title: Question2 - Intercloud SCC Connector
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: CMPE224/343 HW2 - Question 2
// Description:
// You are the network architect of a global intercloud. Each data center is a vertex, 
// and each one-way communication link u -> v means "messages can flow from u to v".
// The goal is to find strongly connected components (SCCs) using the Kosaraju-Sharir algorithm 
// and determine the minimum number of new links required to make the graph strongly connected.
//-----------------------------------------------------

import java.util.*;

//-----------------------------------------------------
// Class: Digraph
// Summary: Represents a directed graph using adjacency lists.
// Precondition: Number of vertices V must be greater than 0.
// Postcondition: Initializes adjacency lists for each vertex.
//-----------------------------------------------------
class Digraph {
    private final int V;                     // number of vertices
    private int E;                           // number of edges
    private List<Integer>[] adj;             // adjacency lists

    //-----------------------------------------------------
    // Summary: Constructs a directed graph with V vertices (0..V-1).
    // Precondition: V > 0.
    // Postcondition: Initializes an empty adjacency list for each vertex.
    //-----------------------------------------------------
    @SuppressWarnings("unchecked")
    public Digraph(int V) {
        this.V = V;
        this.E = 0;
        this.adj = (List<Integer>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    //-----------------------------------------------------
    // Summary: Adds a directed edge from vertex v to vertex w.
    // Precondition: 0 <= v,w < V and v != w.
    // Postcondition: Edge (v -> w) is added if not already present.
    //-----------------------------------------------------
    public void addEdge(int v, int w) {
        if (v == w) return; // ignore self-loops
        if (!adj[v].contains(w)) { // avoid duplicates
            adj[v].add(w);
            E++;
        }
    }

    //-----------------------------------------------------
    // Summary: Returns the adjacency list of vertex v.
    // Precondition: 0 <= v < V.
    // Postcondition: Iterable list of vertices adjacent to v is returned.
    //-----------------------------------------------------
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    //-----------------------------------------------------
    // Summary: Returns the number of vertices in the graph.
    //-----------------------------------------------------
    public int V() {
        return V;
    }

    //-----------------------------------------------------
    // Summary: Returns the number of edges in the graph.
    //-----------------------------------------------------
    public int E() {
        return E;
    }

    //-----------------------------------------------------
    // Summary: Creates and returns the reverse of this digraph.
    // Precondition: None.
    // Postcondition: A new Digraph object with all edges reversed is returned.
    //-----------------------------------------------------
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

//-----------------------------------------------------
// Class: DepthFirstOrder
// Summary: Computes the reverse postorder of a directed graph using DFS.
// Precondition: Input graph must not be null.
// Postcondition: Produces an order suitable for Kosaraju’s second pass.
//-----------------------------------------------------
class DepthFirstOrder {
    private boolean[] marked;
    private Stack<Integer> reversePost;

    //-----------------------------------------------------
    // Summary: Initializes the DFS and computes reverse postorder of G.
    // Precondition: G != null.
    // Postcondition: Vertices are pushed in reverse postorder on the stack.
    //-----------------------------------------------------
    public DepthFirstOrder(Digraph G) {
        marked = new boolean[G.V()];
        reversePost = new Stack<>();
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    //-----------------------------------------------------
    // Summary: Performs DFS starting from vertex v.
    // Precondition: Vertex v must be within 0..V-1.
    // Postcondition: All reachable vertices from v are visited and recorded.
    //-----------------------------------------------------
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
        reversePost.push(v);
    }

    //-----------------------------------------------------
    // Summary: Returns vertices in reverse postorder.
    //-----------------------------------------------------
    public Iterable<Integer> reversePost() {
        return reversePost;
    }
}

//-----------------------------------------------------
// Class: KosarajuSCC
// Summary: Implements the 2-pass Kosaraju-Sharir algorithm to find SCCs.
// Precondition: Graph must be a valid directed graph.
// Postcondition: Each vertex is assigned a component ID representing its SCC.
//-----------------------------------------------------
class KosarajuSCC {
    private boolean[] marked;
    private int[] id;
    private int count;

    //-----------------------------------------------------
    // Summary: Constructs and executes the Kosaraju SCC algorithm.
    // Precondition: G != null.
    // Postcondition: Each vertex receives a component label (id[v]).
    //-----------------------------------------------------
    public KosarajuSCC(Digraph G) {
        Digraph R = G.reverse();
        DepthFirstOrder order = new DepthFirstOrder(R);
        marked = new boolean[G.V()];
        id = new int[G.V()];
        count = 0;

        for (int v : order.reversePost()) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    //-----------------------------------------------------
    // Summary: Depth-first search that labels vertices of one SCC.
    // Precondition: G != null, v within 0..V-1.
    // Postcondition: All vertices reachable from v are labeled with current count.
    //-----------------------------------------------------
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    //-----------------------------------------------------
    // Summary: Returns the SCC ID assigned to vertex v.
    //-----------------------------------------------------
    public int id(int v) {
        return id[v];
    }

    //-----------------------------------------------------
    // Summary: Returns the total number of strongly connected components.
    //-----------------------------------------------------
    public int count() {
        return count;
    }
}

//-----------------------------------------------------
// Class: Question2
// Summary: Main program that reads graph input, finds SCCs, prints results,
// and computes the minimum number of new edges needed to connect all SCCs.
// Precondition: Input format follows assignment specification (N, M, edges).
// Postcondition: Correctly prints SCCs, connectivity status, and new links.
//-----------------------------------------------------
public class Question2 {

    //-----------------------------------------------------
    // Summary: Entry point of the program.
    // Reads graph data, performs SCC computation, and outputs formatted results.
    // Precondition: Input graph data is provided in correct order.
    // Postcondition: SCCs, connectivity information, and additional edges printed.
    //-----------------------------------------------------
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int M = in.nextInt();
        Digraph G = new Digraph(N);

        //-----------------------------------------------------
        // Read M directed edges from input and add them to the graph.
        //-----------------------------------------------------
        for (int i = 0; i < M; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            G.addEdge(u, v);
        }

        //-----------------------------------------------------
        // Run Kosaraju’s algorithm to identify SCCs.
        //-----------------------------------------------------
        KosarajuSCC scc = new KosarajuSCC(G);
        int compCount = scc.count();

        //-----------------------------------------------------
        // Collect vertices in their respective components.
        //-----------------------------------------------------
        List<List<Integer>> comps = new ArrayList<>();
        for (int i = 0; i < compCount; i++) comps.add(new ArrayList<>());
        for (int v = 0; v < N; v++) comps.get(scc.id(v)).add(v);

        //-----------------------------------------------------
        // Sort vertices within each SCC and prepare for ordered printing.
        //-----------------------------------------------------
        for (List<Integer> comp : comps) Collections.sort(comp);

        //-----------------------------------------------------
        // Order components by their smallest vertex for stable output.
        //-----------------------------------------------------
        List<int[]> order = new ArrayList<>();
        for (int i = 0; i < comps.size(); i++) {
            order.add(new int[]{comps.get(i).get(0), i});
        }
        order.sort(Comparator.comparingInt(a -> a[0]));

        //-----------------------------------------------------
        // Print each component in formatted order.
        //-----------------------------------------------------
        int printedId = 1;
        for (int[] pair : order) {
            int idx = pair[1];
            System.out.print("Component " + printedId + ": ");
            List<Integer> comp = comps.get(idx);
            for (int j = 0; j < comp.size(); j++) {
                if (j > 0) System.out.print(" ");
                System.out.print(comp.get(j));
            }
            System.out.println();
            printedId++;
        }

        //-----------------------------------------------------
        // If there is only one SCC, no new edges are needed.
        //-----------------------------------------------------
        if (compCount == 1) {
            System.out.println("Already strongly connected");
            System.out.println("New edges needed: 0");
            in.close();
            return;
        }

        //-----------------------------------------------------
        // Build condensation graph information (in-degree/out-degree).
        //-----------------------------------------------------
        int[] inDeg = new int[compCount];
        int[] outDeg = new int[compCount];

        for (int v = 0; v < N; v++) {
            int vComp = scc.id(v);
            for (int w : G.adj(v)) {
                int wComp = scc.id(w);
                if (vComp != wComp) {
                    outDeg[vComp]++;
                    inDeg[wComp]++;
                }
            }
        }

        //-----------------------------------------------------
        // Identify source and sink components.
        //-----------------------------------------------------
        List<Integer> sources = new ArrayList<>();
        List<Integer> sinks = new ArrayList<>();
        for (int i = 0; i < compCount; i++) {
            if (inDeg[i] == 0) sources.add(i);
            if (outDeg[i] == 0) sinks.add(i);
        }

        //-----------------------------------------------------
        // Compute and print the minimum number of new edges required.
        //-----------------------------------------------------
        int needed = Math.max(sources.size(), sinks.size());
        System.out.println("New edges needed: " + needed);

        //-----------------------------------------------------
        // Construct and print a valid set of new edges to connect all SCCs.
        //-----------------------------------------------------
        for (int i = 0; i < needed; i++) {
            int fromComp = sinks.get(i % sinks.size());
            int toComp = sources.get(i % sources.size());
            int fromVertex = comps.get(fromComp).get(0);
            int toVertex = comps.get(toComp).get(0);
            System.out.println("add: " + fromVertex + " " + toVertex);
        }

        in.close();
    }
}
