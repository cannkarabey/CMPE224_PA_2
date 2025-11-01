//-----------------------------------------------------
// Title: Directed Cycle Finder Class
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: HW2 - Q2
// Description: Reads a directed graph, finds SCCs using
// Kosaraju-Sharir, prints components in required format,
// then prints minimum edges to make the graph strongly
// connected.
//-----------------------------------------------------
import java.util.*;

public class SCCTester {

    //--------------------------------------------------------
    // Summary: Main method to find SCCs and calculate minimum edges needed for strong connectivity.
    // Precondition: Input contains valid N, M, and M edge pairs.
    // Postcondition: Prints all SCCs and the minimum number of edges needed.
    //--------------------------------------------------------
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int N = in.nextInt();
        int M = in.nextInt();
        Digraph G = new Digraph(N);

        for (int i = 0; i < M; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            if (u != v) {
                G.addEdge(u, v);
            }
        }

        KosarajuSCC scc = new KosarajuSCC(G);
        int compCount = scc.count();

        // component -> vertices list
        List<List<Integer>> components = new ArrayList<>();
        for (int i = 0; i < compCount; i++) {
            components.add(new ArrayList<>());
        }
        for (int v = 0; v < N; v++) {
            components.get(scc.id(v)).add(v);
        }

        // sort vertices inside each component
        for (List<Integer> comp : components) {
            Collections.sort(comp);
        }

        // we must print components in increasing order of their smallest vertex
        // so we create a list of (minVertex, compIndex)
        List<int[]> order = new ArrayList<>();
        for (int i = 0; i < components.size(); i++) {
            int minV = components.get(i).get(0);
            order.add(new int[]{minV, i});
        }
        // sort by min vertex
        order.sort(Comparator.comparingInt(a -> a[0]));

        // print components
        int printedIndex = 1;
        for (int[] pair : order) {
            int idx = pair[1];
            System.out.print("Component " + printedIndex + ": ");
            List<Integer> comp = components.get(idx);
            for (int i = 0; i < comp.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(comp.get(i));
            }
            System.out.println();
            printedIndex++;
        }

        // if only one SCC, we are done
        if (compCount == 1) {
            System.out.println("Already strongly connected");
            System.out.println("New edges needed: 0");
            in.close();
            return;
        }

        // build condensation info: find in-degree and out-degree of each SCC
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

        List<Integer> sources = new ArrayList<>();
        List<Integer> sinks = new ArrayList<>();
        for (int i = 0; i < compCount; i++) {
            if (inDeg[i] == 0) sources.add(i);
            if (outDeg[i] == 0) sinks.add(i);
        }

        int needed = Math.max(sources.size(), sinks.size());
        System.out.println("New edges needed: " + needed);

        // now we should print "add: x y"
        // simple ring-like connection:
        for (int i = 0; i < needed; i++) {
            int fromComp = sinks.get(i % sinks.size());
            int toComp = sources.get(i % sources.size());

            // pick representative vertex from these components (first vertex)
            int fromVertex = components.get(fromComp).get(0);
            int toVertex = components.get(toComp).get(0);

            System.out.println("add: " + fromVertex + " " + toVertex);
        }

        in.close();
    }
}
