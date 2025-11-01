//-----------------------------------------------------
// Title: Directed Cycle Finder Class
// Author: YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID: 14857072936 / 10130437010
// Section: 02 / 01
// Assignment: HW2 - Q1
// Description: Reads a precedence-constrained task graph
// from standard input, checks if it is schedulable. If it is
// prints a valid topological order, otherwise prints a cycle.
//-----------------------------------------------------
import java.util.Scanner;

public class SchedulerTester {

    //--------------------------------------------------------
    // Summary: Main method to test task scheduling using topological sort.
    // Precondition: Input contains valid N, M, and M edge pairs.
    // Postcondition: Prints "Schedulable" with order or "Not schedulable" with cycle.
    //--------------------------------------------------------
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int N = in.nextInt();
        int M = in.nextInt();
        Digraph G = new Digraph(N);

        for (int i = 0; i < M; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            // u -> v
            if (u != v) {
                G.addEdge(u, v);
            }
        }

        Topological topo = new Topological(G);
        if (topo.isDAG()) {
            System.out.println("Schedulable");
            System.out.print("Order: ");
            boolean first = true;
            for (int v : topo.order()) {
                if (!first) System.out.print(" ");
                System.out.print(v);
                first = false;
            }
            System.out.println();
        } else {
            // need to find and print one cycle
            DirectedCycle cf = new DirectedCycle(G);
            System.out.println("Not schedulable");
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
