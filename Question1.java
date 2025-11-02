//-----------------------------------------------------
// Title: Question1 - Smart Factory Task Scheduler
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: CMPE224/343 HW2 - Question 1
// Description:
// You are hired as the Conductor of a smart factory’s assembly line orchestra.
// Each task is represented as a vertex, and a directed edge u → v means
// "task u must be completed before task v can start."
// The goal is to determine if the task dependency graph is schedulable (a DAG).
// If schedulable, print a valid topological order.
// If not, print one directed cycle as evidence.
//-----------------------------------------------------

import java.util.*;

//-----------------------------------------------------
// Class: Digraph
// Summary: Represents a directed graph using adjacency lists.
// Precondition: Number of vertices (V) must be greater than 0.
// Postcondition: Creates an adjacency list for each vertex.
//-----------------------------------------------------
class Digraph {
    private final int V;                   // number of vertices
    private int E;                         // number of edges
    private List<Integer>[] adj;           // adjacency list

    //-----------------------------------------------------
    // Summary: Constructs an empty directed graph with V vertices.
    // Precondition: V > 0.
    // Postcondition: Initializes empty adjacency lists for all vertices.
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
    // Summary: Adds a directed edge from vertex v to vertex w.
    // Precondition: 0 <= v,w < V and v != w.
    // Postcondition: Adds edge (v -> w) if it does not already exist.
    //-----------------------------------------------------
    public void addEdge(int v, int w) {
        if (v == w) return; // ignore self-loops
        if (!adj[v].contains(w)) { // avoid duplicate edges
            adj[v].add(w);
            E++;
        }
    }

    //-----------------------------------------------------
    // Summary: Returns an iterable list of all adjacent vertices from v.
    // Precondition: 0 <= v < V.
    // Postcondition: Returns vertices that are directly reachable from v.
    //-----------------------------------------------------
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    //-----------------------------------------------------
    // Summary: Returns the total number of vertices in the graph.
    //-----------------------------------------------------
    public int V() {
        return V;
    }

    //-----------------------------------------------------
    // Summary: Returns the total number of edges in the graph.
    //-----------------------------------------------------
    public int E() {
        return E;
    }
}

//-----------------------------------------------------
// Class: DirectedCycle
// Summary: Detects if a directed graph contains any cycle using DFS.
// Precondition: Input graph G must not be null.
// Postcondition: Stores one cycle path in 'cycle' if a cycle exists.
//-----------------------------------------------------
class DirectedCycle {
    private boolean[] marked;       // tracks visited vertices
    private boolean[] onStack;      // tracks recursion stack
    private int[] edgeTo;           // stores parent links
    private Stack<Integer> cycle;   // stores the detected cycle

    //-----------------------------------------------------
    // Summary: Initializes cycle detection on graph G using DFS.
    // Precondition: G != null.
    // Postcondition: If a cycle exists, it is recorded in 'cycle'.
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

    //-----------------------------------------------------
    // Summary: Performs DFS to explore and detect cycles.
    // Precondition: Vertex v must be within 0..V-1.
    // Postcondition: Marks visited nodes and records a cycle if found.
    //-----------------------------------------------------
    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (cycle != null) return; // stop once a cycle is found
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            } else if (onStack[w]) {
                // Cycle found: trace it back
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
    // Summary: Returns true if the graph has a directed cycle.
    //-----------------------------------------------------
    public boolean hasCycle() {
        return cycle != null;
    }

    //-----------------------------------------------------
    // Summary: Returns the detected cycle as an iterable list of vertices.
    //-----------------------------------------------------
    public Iterable<Integer> cycle() {
        return cycle;
    }
}

//-----------------------------------------------------
// Class: DepthFirstOrder
// Summary: Produces reverse postorder of all vertices in a graph.
// Precondition: Graph G must not be null.
// Postcondition: reversePost stack stores vertices in reverse postorder.
//-----------------------------------------------------
class DepthFirstOrder {
    private boolean[] marked;
    private Stack<Integer> reversePost;

    //-----------------------------------------------------
    // Summary: Computes the reverse postorder for graph G.
    // Precondition: G != null.
    // Postcondition: Vertices are ordered by completion time (reverse DFS order).
    //-----------------------------------------------------
    public DepthFirstOrder(Digraph G) {
        marked = new boolean[G.V()];
        reversePost = new Stack<>();
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    //-----------------------------------------------------
    // Summary: Performs DFS from vertex v and records postorder.
    // Precondition: Vertex v must be unvisited.
    // Postcondition: Pushes vertex to reversePost after visiting its descendants.
    //-----------------------------------------------------
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
        reversePost.push(v);
    }

    //-----------------------------------------------------
    // Summary: Returns vertices in reverse postorder (useful for topological sort).
    //-----------------------------------------------------
    public Iterable<Integer> reversePost() {
        return reversePost;
    }
}

//-----------------------------------------------------
// Class: Topological
// Summary: Determines if a graph is a DAG and computes its topological order.
// Precondition: Input graph G must not be null.
// Postcondition: If graph is acyclic, produces valid order; otherwise null.
//-----------------------------------------------------
class Topological {
    private Iterable<Integer> order;

    //-----------------------------------------------------
    // Summary: Constructs a Topological object and computes ordering.
    // Precondition: Graph G must be initialized.
    // Postcondition: order is null if the graph contains a directed cycle.
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

    //-----------------------------------------------------
    // Summary: Returns the computed topological order.
    //-----------------------------------------------------
    public Iterable<Integer> order() {
        return order;
    }

    //-----------------------------------------------------
    // Summary: Returns true if the graph is acyclic (i.e., a DAG).
    //-----------------------------------------------------
    public boolean isDAG() {
        return order != null;
    }
}

//-----------------------------------------------------
// Class: Question1 (Main)
// Summary: Reads input graph, determines task schedulability,
// and prints either a valid task order or a detected cycle.
// Precondition: Input format must follow assignment specification.
// Postcondition: Outputs "Schedulable" with order or "Not schedulable" with cycle.
//-----------------------------------------------------
public class Question1 {

    //-----------------------------------------------------
    // Summary: Entry point of the program.
    // Reads the number of tasks and dependencies, builds the graph,
    // checks for cycles, and prints the appropriate result.
    // Precondition: Input is provided in correct format: N M followed by M edges.
    // Postcondition: Prints topological order or one detected cycle.
    //-----------------------------------------------------
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int M = in.nextInt();

        Digraph G = new Digraph(N);

        //-----------------------------------------------------
        // Read edges and build the directed graph.
        //-----------------------------------------------------
        for (int i = 0; i < M; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            G.addEdge(u, v);
        }

        //-----------------------------------------------------
        // Perform topological sorting or detect cycles.
        //-----------------------------------------------------
        Topological topo = new Topological(G);

        //-----------------------------------------------------
        // If graph is acyclic, print valid order.
        //-----------------------------------------------------
        if (topo.isDAG()) {
            System.out.println("Schedulable");
            System.out.print("Order: ");
            List<Integer> orderList = new ArrayList<>();
            for (int v : topo.order()) {
                orderList.add(v);
            }
            Collections.reverse(orderList); // reversePost -> correct topological order
            for (int i = 0; i < orderList.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(orderList.get(i));
            }
            System.out.println();
        }

        //-----------------------------------------------------
        // If graph has a cycle, print the cycle as proof.
        //-----------------------------------------------------
        else {
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
