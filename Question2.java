//-----------------------------------------------------
// Title: Question2 - Intercloud SCC Connector
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: CMPE224/343 HW2 - Question 2
// Description:
// You are the network architect of a global intercloud. Each data center is a vertex, and each
// one-way communication link u -> v means "messages can flow from u to v".
// Your task is to:
// 1) Find all Strongly Connected Components (SCCs) using the 2-pass Kosaraju-Sharir algorithm.
// 2) Print each SCC in the required format, ordered by the smallest vertex in that SCC.
// 3) If there is more than one SCC, conceptually build the condensation DAG and determine the
//    minimum number of new directed links required to make the whole network strongly connected.
//    (Known fact: answer = max(#sources, #sinks))
// 4) Print one valid constructive set of links in the format:
//       add: x y
//    Any valid solution is acceptable (ring-like connection between sinks and sources).
//-----------------------------------------------------

import java.util.*;

//-----------------------------------------------------
// Digraph Class - basic directed graph with adjacency list
//-----------------------------------------------------
class Digraph {
    private final int V;                 // number of vertices
    private int E;                       // number of edges
    private List<Integer>[] adj;         // adjacency lists

    //-----------------------------------------------------
    // Summary: Constructs a directed graph with V vertices (0..V-1)
    // Precondition: V > 0
    // Postcondition: Initializes an empty adjacency list for each vertex
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
    // Summary: Adds a directed edge v -> w to the graph
    // Precondition: 0 <= v, w < V
    // Postcondition: Edge is added, parallel edges and self-loops are ignored defensively
    //-----------------------------------------------------
    public void addEdge(int v, int w) {
        if (v == w) return; // ignore self-loops safely
        if (!adj[v].contains(w)) { // avoid parallel edges
            adj[v].add(w);
            E++;
        }
    }

    //-----------------------------------------------------
    // Summary: Returns adjacency list of vertex v
    // Precondition: 0 <= v < V
    // Postcondition: Iterable list of neighbors of v is returned
    //-----------------------------------------------------
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    //-----------------------------------------------------
    // Summary: Returns number of vertices
    //-----------------------------------------------------
    public int V() {
        return V;
    }

    //-----------------------------------------------------
    // Summary: Returns number of edges
    //-----------------------------------------------------
    public int E() {
        return E;
    }

    //-----------------------------------------------------
    // Summary: Returns the reverse of this digraph
    // Precondition: none
    // Postcondition: A new Digraph whose edges are reversed is returned
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
// DepthFirstOrder Class - used for reverse postorder in Kosaraju
//-----------------------------------------------------
class DepthFirstOrder {
    private boolean[] marked;
    private Stack<Integer> reversePost;

    //-----------------------------------------------------
    // Summary: Computes reverse postorder of graph G
    // Precondition: G != null
    // Postcondition: reversePost contains vertices in reverse postorder
    //-----------------------------------------------------
    public DepthFirstOrder(Digraph G) {
        marked = new boolean[G.V()];
        reversePost = new Stack<>();
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
        reversePost.push(v);
    }

    //-----------------------------------------------------
    // Summary: Returns vertices in reverse postorder
    //-----------------------------------------------------
    public Iterable<Integer> reversePost() {
        return reversePost;
    }
}

//-----------------------------------------------------
// KosarajuSCC Class - 2-pass SCC detector
//-----------------------------------------------------
class KosarajuSCC {
    private boolean[] marked;
    private int[] id;
    private int count;

    //-----------------------------------------------------
    // Summary: Runs Kosaraju-Sharir on graph G
    // Precondition: G != null
    // Postcondition: Each vertex is assigned to a component with id[v]
    //-----------------------------------------------------
    public KosarajuSCC(Digraph G) {
        // 1) DFS on reversed graph to get reverse postorder
        Digraph R = G.reverse();
        DepthFirstOrder order = new DepthFirstOrder(R);

        // 2) DFS on original graph in that order
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

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    //-----------------------------------------------------
    // Summary: Returns component id of vertex v
    //-----------------------------------------------------
    public int id(int v) {
        return id[v];
    }

    //-----------------------------------------------------
    // Summary: Returns total number of components
    //-----------------------------------------------------
    public int count() {
        return count;
    }
}

//-----------------------------------------------------
// Main Class - Question2
//-----------------------------------------------------
public class Question2 {

    //-----------------------------------------------------
    // Summary: Entry point.
    // Reads N, M and M edges. Builds digraph. Finds SCCs.
    // Prints components in increasing order of their smallest vertex.
    // Then, if more than 1 SCC exists, computes min new edges needed
    // using max(#sources, #sinks) rule and prints a valid constructive set.
    // Precondition: Input format must follow the problem statement.
    // Postcondition: Output is printed in required format.
    //-----------------------------------------------------
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int N = in.nextInt();
        int M = in.nextInt();
        Digraph G = new Digraph(N);

        for (int i = 0; i < M; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            G.addEdge(u, v);
        }

        // 1) Find SCCs
        KosarajuSCC scc = new KosarajuSCC(G);
        int compCount = scc.count();

        // 2) Collect vertices per component
        List<List<Integer>> comps = new ArrayList<>();
        for (int i = 0; i < compCount; i++) {
            comps.add(new ArrayList<>());
        }
        for (int v = 0; v < N; v++) {
            comps.get(scc.id(v)).add(v);
        }

        // 3) Sort vertices inside each component
        for (List<Integer> comp : comps) {
            Collections.sort(comp);
        }

        // 4) We need to print components in increasing order of their smallest vertex
        // Build (minVertex, compIndex) pairs
        List<int[]> order = new ArrayList<>();
        for (int i = 0; i < comps.size(); i++) {
            int minV = comps.get(i).get(0);
            order.add(new int[]{minV, i});
        }
        order.sort(Comparator.comparingInt(a -> a[0]));

        // 5) Print components in required format
        int printedId = 1;
        for (int[] pair : order) {
            int idx = pair[1];
            System.out.print("Component " + printedId +: );
            List<Integer> comp = comps.get(idx);
            for (int j = 0; j < comp.size(); j++) {
                if (j > 0) System.out.print(" ");
                System.out.print(comp.get(j));
            }
            System.out.println();
            printedId++;
        }

        // If there is only one SCC, done
        if (compCount == 1) {
            System.out.println("Already strongly connected");
            System.out.println("New edges needed: 0");
            in.close();
            return;
        }

        // 6) Build condensation info: find in-degree and out-degree per component
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

        // 7) Count sources and sinks
        List<Integer> sources = new ArrayList<>();
        List<Integer> sinks = new ArrayList<>();
        for (int i = 0; i < compCount; i++) {
            if (inDeg[i] == 0) sources.add(i);
            if (outDeg[i] == 0) sinks.add(i);
        }

        int needed = Math.max(sources.size(), sinks.size());
        System.out.println("New edges needed: " + needed);

        // 8) Construct a valid set of edges
        // Simple pattern: connect sinks[i] -> sources[i], wrap with modulo
        for (int i = 0; i < needed; i++) {
            int fromComp = sinks.get(i % sinks.size());
            int toComp = sources.get(i % sources.size());

            // pick representative vertex from each component (first vertex is smallest)
            int fromVertex = comps.get(fromComp).get(0);
            int toVertex = comps.get(toComp).get(0);

            System.out.println("add: " + fromVertex + " " + toVertex);
        }

        in.close();
    }
}