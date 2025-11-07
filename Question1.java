//-----------------------------------------------------
// Title: Question1 - Smart Factory Task Scheduler
// Author:    YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID:        14857072936   / 10130437010
// Section:   02            / 01
// Assignment: CMPE224/343 HW2 - Question 1
// Description:
// You are hired as the Conductor of a smart factory’s assembly line orchestra.
// Each task is represented as a vertex, and a directed edge u → v means
// "task u must be completed before task v can start."
// The goal is to determine if the task dependency graph is schedulable (a DAG).
// If schedulable, print a valid topological order.
// If not, print one directed cycle as evidence.
//-----------------------------------------------------

import java.io.*;
import java.util.*;

public class Question1 {

    //--------------------------------------------------------
    // Summary: Directed graph (Digraph) veri yapısını temsil eder.
    // Precondition: n >= 0 olmalıdır.
    // Postcondition: n adet düğüme sahip boş bir adjacency list grafı oluşturulur.
    //--------------------------------------------------------
    static class Digraph {
        final int n;
        final ArrayList<Integer>[] adj;

        @SuppressWarnings("unchecked")
        Digraph(int n) {
            this.n = n;
            adj = new ArrayList[n];
            for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        }

        //--------------------------------------------------------
        // Summary: Graf içine yönlü bir kenar (u → v) ekler.
        // Precondition: 0 <= u < n ve 0 <= v < n olmalıdır.
        // Postcondition: adj[u] listesine v eklenmiş olur.
        //--------------------------------------------------------
        void addEdge(int u, int v) { adj[u].add(v); }
    }

    static Digraph g;
    static int[] state;        // 0 = unvisited, 1 = visiting, 2 = done
    static int[] parent;       // DFS sırasında cycle reconstruct için
    static boolean hasCycle = false;
    static ArrayList<Integer> foundCycle = new ArrayList<>();


    //--------------------------------------------------------
    // Summary: Programın giriş noktasıdır. Grafı okur, cycle kontrolü yapar,
    //          eğer DAG ise lexicographically smallest topolojik sırayı üretir.
    // Precondition: Girdi formatı doğru olmalı; N ve M sayıları okunabilir olmalıdır.
    // Postcondition: Cycle varsa cycle yazılır, yoksa topolojik sıra yazılır.
    //--------------------------------------------------------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        Integer N = fs.nextInt();
        Integer M = fs.nextInt();
        if (N == null || M == null) return;

        g = new Digraph(N);

        //--------------------------------------------------------
        // Summary: M adet kenar okur ve graf yapısına ekler.
        // Precondition: u ve v geçerli düğüm indexleri olmalıdır.
        // Postcondition: g üzerine tüm geçerli kenarlar eklenmiş olur.
        //--------------------------------------------------------
        for (int i = 0; i < M; i++) {
            int u = fs.nextInt(), v = fs.nextInt();
            if (0 <= u && u < N && 0 <= v && v < N) g.addEdge(u, v);
        }

        //--------------------------------------------------------
        // Summary: DFS cycle detection başlatılır.
        // Precondition: g, state ve parent dizileri doğru hazırlanmış olmalıdır.
        // Postcondition: Eğer bir cycle varsa hasCycle = true olur ve foundCycle doldurulur.
        //--------------------------------------------------------
        state = new int[N];
        parent = new int[N];
        Arrays.fill(parent, -1);

        for (int i = 0; i < N && !hasCycle; i++)
            if (state[i] == 0)
                dfs(i);

        //--------------------------------------------------------
        // Summary: Cycle bulunduysa program DAG işlemlerine geçmeden sonlanır.
        // Precondition: DFS tamamlanmış olmalıdır.
        // Postcondition: Cycle bilgisi ekrana yazılır ve program durur.
        //--------------------------------------------------------
        if (hasCycle) {
            System.out.println("Not schedulable");
            System.out.print("Cycle: ");
            for (int i = 0; i < foundCycle.size(); i++) {
                if (i > 0) System.out.print(' ');
                System.out.print(foundCycle.get(i));
            }
            System.out.println();
            return;
        }

        //--------------------------------------------------------
        // Summary: Cycle yoksa topolojik sıralama için Kahn algoritması hazırlanır.
        // Precondition: Grafın DAG olduğunun garanti edilmesi gerekir.
        // Postcondition: indegree dizisi doldurulur, başlangıç düğümleri pq'ye eklenir.
        //--------------------------------------------------------
        int[] indeg = new int[N];
        for (int u = 0; u < N; u++)
            for (int v : g.adj[u])
                indeg[v]++;

        PriorityQueue<Integer> pq = new PriorityQueue<>(); // min-heap
        for (int i = 0; i < N; i++)
            if (indeg[i] == 0)
                pq.add(i);

        ArrayList<Integer> order = new ArrayList<>(N);

        //--------------------------------------------------------
        // Summary: Kahn algoritmasıyla lexicographically smallest topolojik sıra üretilir.
        // Precondition: indegree değerleri doğru hesaplanmış olmalıdır.
        // Postcondition: order listesi topolojik sıralamayı içerir.
        //--------------------------------------------------------
        while (!pq.isEmpty()) {
            int u = pq.poll();
            order.add(u);
            for (int v : g.adj[u]) {
                if (--indeg[v] == 0)
                    pq.add(v);
            }
        }

        //--------------------------------------------------------
        // Summary: Ek güvenlik kontrolü. DAG'da order boyutu N olmalı.
        // Precondition: Kahn algorithm çalışmıştır.
        // Postcondition: Yanlış durumda fallback hata üretir.
        //--------------------------------------------------------
        if (order.size() != N) {
            System.out.println("Not schedulable");
            System.out.println("Cycle: 0 0");
            return;
        }

        //--------------------------------------------------------
        // Summary: DAG olduğundan topolojik sıralama ekrana yazılır.
        // Precondition: order boyutu N olmalıdır.
        // Postcondition: Kullanıcıya sıralama çıktısı basılır.
        //--------------------------------------------------------
        System.out.println("Schedulable");
        System.out.print("Order: ");
        for (int i = 0; i < N; i++) {
            if (i > 0) System.out.print(' ');
            System.out.print(order.get(i));
        }
        System.out.println();
    }


    //--------------------------------------------------------
    // Summary: DFS kullanarak cycle algılar. Visiting durumunda bir düğüme
    //          tekrar gidilirse back-edge bulunmuş olur ve cycle oluşur.
    // Precondition: state ve parent dizileri başlatılmış olmalıdır.
    // Postcondition: Eğer cycle bulunursa hasCycle = true olur ve foundCycle doldurulur.
    //--------------------------------------------------------
    static void dfs(int u) {
        state[u] = 1;  // visiting

        for (int v : g.adj[u]) {
            if (hasCycle) return;

            if (state[v] == 0) {
                parent[v] = u;
                dfs(v);
            }
            else if (state[v] == 1) { // back-edge: cycle
                hasCycle = true;
                foundCycle = buildCycle(u, v);
                return;
            }
        }

        state[u] = 2; // done
    }


    //--------------------------------------------------------
    // Summary: Back-edge tespit edildikten sonra parent zinciriyle cycle yolunu oluşturur.
    // Precondition: parent[] dizisi DFS sırasında doldurulmuş olmalıdır.
    // Postcondition: Cycle sırasını içeren bir liste döner (v ... u → v şeklinde).
    //--------------------------------------------------------
    static ArrayList<Integer> buildCycle(int u, int v) {
        ArrayList<Integer> path = new ArrayList<>();
        int x = u;
        path.add(u);

        while (x != v && x != -1) {
            x = parent[x];
            path.add(x);
        }

        Collections.reverse(path); // v ... u
        path.add(v);               // döngüyü kapat
        return path;
    }


    /* -------- Fast input -------- */

    //--------------------------------------------------------
    // Summary: Büyük girişler için hızlı integer okuma sağlar.
    // Precondition: InputStream geçerli olmalıdır.
    // Postcondition: nextInt() çağrıları integer veya null döndürür.
    //--------------------------------------------------------
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream is) { in = is; }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        //--------------------------------------------------------
        // Summary: Akıştan sıradaki integer değeri okur.
        // Precondition: Girdi formatı rakam ve boşluklardan oluşmalı.
        // Postcondition: Bir int döndürür; girdi bitmişse null döner.
        //--------------------------------------------------------
        Integer nextInt() throws IOException {
            int c, sgn = 1, val = 0;

            do {
                c = read();
                if (c == -1) return null;
            } while (c <= ' ');

            if (c == '-') {
                sgn = -1;
                c = read();
            }

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = read();
            }

            return val * sgn;
        }
    }
}
