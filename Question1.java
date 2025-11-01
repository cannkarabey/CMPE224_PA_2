//-----------------------------------------------------
// Title: Question1 - Smart Factory Task Scheduler
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: CMPE224/343 HW2 - Question 1
// Description: 
// You are hired as the Conductor of a smart factory’s assembly line orchestra.
// Each task is an instrument (vertex), and a directed edge u → v means
// "instrument u must finish before instrument v can begin".
// The goal is to determine if the dependency graph (tasks and edges) 
// is schedulable (i.e., a Directed Acyclic Graph - DAG).
// If schedulable, print a valid topological order.
// If not, print one directed cycle as a witness.
//-----------------------------------------------------

import java.util.*;

//-----------------------------------------------------
// Digraph Class - Represents a directed graph using adjacency lists
//-----------------------------------------------------
class Digraph {
    private final int V;                   // number of vertices
    private int E;                         // number of edges
    private List<Integer>[] adj;           // adjacency list

    //-----------------------------------------------------
    // Summary: Constructor that initializes an empty digraph with V vertices
    // Precondition: V > 0
    // Postcondition: Creates adjacency lists for each vertex
    //-----------------------------------------------------
    @SuppressWarnings("unchecked")
    public Digraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (List<Integer>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    //-----------------------------------------------------
    // Summary: Adds a directed edge from vertex v to vertex w
    // Precondition: 0 <= v, w < V
    // Postcondition: Edge (v -> w) is added to the adjacency list of v
    //-----------------------------------------------------
    public void addEdge(int v, int w) {
        if (v == w) return; // ignore self-loops
        if (!adj[v].contains(w)) { // avoid parallel edges
            adj[v].add(w);
            E++;
        }
    }

    //-----------------------------------------------------
    // Summary: Returns all adjacent vertices from v
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
}

//-----------------------------------------------------
// DirectedCycle Class - Detects a cycle using DFS
//-----------------------------------------------------
class DirectedCycle {
    private boolean[] marked;
    private boolean[] onStack;
    private int[] edgeTo;
    private Stack<Integer> cycle; // stores vertices in the detected cycle

    //-----------------------------------------------------
    // Summary: Runs DFS-based cycle detection on the digraph
    // Precondition: Graph G must be non-null
    // Postcondition: If a directed cycle exists, it is stored in 'cycle'
    //-----------------------------------------------------
    public DirectedCycle(Digraph G) {
        int V = G.V();
        marked = new boolean[V];
        onStack = new boolean[V];
        edgeTo = new int[V];
        for (int v = 0; v < V; v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (cycle != null) return; // stop if already found
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            } else if (onStack[w]) {
                cycle = new Stack<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
                Collections.reverse((List<?>) cycle);
                return;
            }
        }
        onStack[v] = false;
    }

    //-----------------------------------------------------
    // Summary: Checks if a cycle exists
    //-----------------------------------------------------
    public boolean hasCycle() {
        return cycle != null;
    }

    //-----------------------------------------------------
    // Summary: Returns the detected cycle
    //-----------------------------------------------------
    public Iterable<Integer> cycle() {
        return cycle;
    }
}

//-----------------------------------------------------
// DepthFirstOrder Class - Produces reverse postorder of vertices
//-----------------------------------------------------
class DepthFirstOrder {
    private boolean[] marked;
    private Stack<Integer> reversePost;

    //-----------------------------------------------------
    // Summary: Computes reverse postorder of all vertices in the graph
    // Precondition: G is non-null
    // Postcondition: reversePost stack contains vertices in topological order
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
// Topological Class - Determines if a graph is DAG and computes order
//-----------------------------------------------------
class Topological {
    private Iterable<Integer> order;

    //-----------------------------------------------------
    // Summary: Creates a topological order if graph has no directed cycle
    // Precondition: G is non-null
    // Postcondition: order is null if a cycle exists
    //-----------------------------------------------------
    public Topological(Digraph G) {
        DirectedCycle cycleFinder = new DirectedCycle(G);
        if (!cycleFinder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        } else {
            order = null;
        }
    }

    public Iterable<Integer> order() {
        return order;
    }

    public boolean isDAG() {
        return order != null;
    }
}

//-----------------------------------------------------
// Main Class - Question1
//-----------------------------------------------------
public class Question1 {

    //-----------------------------------------------------
    // Summary: Main entry point
    // Reads input, builds graph, determines schedulability.
    // Precondition: Input format must follow problem description.
    // Postcondition: Prints topological order or one directed cycle.
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

        Topological topo = new Topological(G);

        if (topo.isDAG()) {
            System.out.println("Schedulable");
            System.out.print("Order: ");
            List<Integer> orderList = new ArrayList<>();
            for (int v : topo.order()) {
                orderList.add(v);
            }
            Collections.reverse(orderList); // DFS reversePost -> correct order
            for (int i = 0; i < orderList.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(orderList.get(i));
            }
            System.out.println();
        } else {
            System.out.println("Not schedulable");
            DirectedCycle cf = new DirectedCycle(G);
            System.out.print("Cycle: ");
            boolean first = true;
            for (int v : cf.cycle()) {
                if (!first) System.out.print(" ");
                System.out.print(v);
                first = false;
            }
            System.out.println();
        }
        in.close();
    }
}
