//-----------------------------------------------------
// Title: Question2 - Intercloud SCC Connector
// Author:    YUSUF KARABEY / MEHMET YİĞİT AÇDOYURAN
// ID:        14857072936   / 10130437010
// Section:   02            / 01
// Assignment: CMPE224/343 HW2 - Question 2
// Description:
// You are the network architect of a global intercloud. Each data center is a vertex, 
// and each one-way communication link u -> v means "messages can flow from u to v".
// The goal is to find strongly connected components (SCCs) using the Kosaraju-Sharir algorithm 
// and determine the minimum number of new links required to make the graph strongly connected.
//-----------------------------------------------------

import java.io.*;
import java.util.*;

public class Question2 {

    //--------------------------------------------------------
    // Summary: Directed graph yapısı; hem normal adjacency list
    //          hem de ters (reverse) adjacency list içerir.
    // Precondition: n >= 0 olmalıdır.
    // Postcondition: adj ve radj dizileri oluşturulur, her biri boş liste içerir.
    //--------------------------------------------------------
    static class Digraph {
        final int n;
        final ArrayList<Integer>[] adj, radj;

        @SuppressWarnings("unchecked")
        Digraph(int n) {
            this.n = n;
            adj = new ArrayList[n];
            radj = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                adj[i] = new ArrayList<>();
                radj[i] = new ArrayList<>();
            }
        }

        //--------------------------------------------------------
        // Summary: Graf üzerine u->v kenarını ekler; reverse graph’a da v->u ekler.
        // Precondition: 0 <= u < n ve 0 <= v < n olmalıdır.
        // Postcondition: Hem adj[u] hem de radj[v] içine ekleme yapılır.
        //--------------------------------------------------------
        void addEdge(int u, int v) { adj[u].add(v); radj[v].add(u); }
    }

    static Digraph g;

    //--------------------------------------------------------
    // Summary: Programın giriş noktasıdır. Kosaraju algoritması ile
    //          strongly connected components (SCC) bulur; 
    //          ardından grafı strongly-connected yapmak için
    //          minimum gerekli yeni kenarları hesaplar.
    // Precondition: Girdi formatı doğru olmalıdır.
    // Postcondition: Bileşenler yazılır, ardından gereken yeni edge sayısı ve listesi yazılır.
    //--------------------------------------------------------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        Integer N = fs.nextInt(), M = fs.nextInt();
        if (N == null || M == null) return;

        g = new Digraph(N);

        //--------------------------------------------------------
        // Summary: M adet kenarı okuyup graf yapısına ekler.
        // Precondition: u ve v geçerli düğüm indeksleri olmalıdır.
        // Postcondition: adj ve radj yapılarına kenarlar eklenir.
        //--------------------------------------------------------
        for (int i = 0; i < M; i++) {
            int u = fs.nextInt(), v = fs.nextInt();
            if (0 <= u && u < N && 0 <= v && v < N) g.addEdge(u, v);
        }

        //--------------------------------------------------------
        // Summary: Kosaraju algoritmasının 1. aşaması: 
        //          reverse graph üzerinde DFS ile finishing order hesaplama.
        // Precondition: visited dizisi false ile başlamalıdır.
        // Postcondition: order listesi finishing order'a göre doldurulur.
        //--------------------------------------------------------
        boolean[] vis = new boolean[N];
        ArrayList<Integer> order = new ArrayList<>(N);
        for (int i = 0; i < N; i++)
            if (!vis[i])
                dfs1(i, vis, order);

        //--------------------------------------------------------
        // Summary: Kosaraju 2. aşama:
        //          order ters okunarak normal graf üzerinde SCC'ler bulunur.
        // Precondition: order doğru şekilde doldurulmuş olmalıdır.
        // Postcondition: comp dizisi SCC id’lerini tutar, comps listesi bileşenleri içerir.
        //--------------------------------------------------------
        int[] comp = new int[N];
        Arrays.fill(comp, -1);
        ArrayList<ArrayList<Integer>> comps = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            int v = order.get(i);
            if (comp[v] == -1) {
                ArrayList<Integer> bag = new ArrayList<>();
                dfs2(v, comps.size(), comp, bag);
                Collections.sort(bag);
                comps.add(bag);
            }
        }

        //--------------------------------------------------------
        // Summary: SCC bileşenlerini en küçük düğüm değerine göre sıralar.
        // Precondition: comps listesi dolu olmalıdır.
        // Postcondition: compOrder, bileşenlerin yazılma sırasını belirler.
        //--------------------------------------------------------
        ArrayList<Integer> compOrder = new ArrayList<>();
        for (int i = 0; i < comps.size(); i++) compOrder.add(i);
        compOrder.sort((a,b) -> Integer.compare(comps.get(a).get(0), comps.get(b).get(0)));

        //--------------------------------------------------------
        // Summary: Bileşenleri kullanıcıya bildirir.
        // Precondition: comps listesi sıralıdır.
        // Postcondition: Component 1:, Component 2: ... formatında çıktı alınır.
        //--------------------------------------------------------
        for (int pos = 0; pos < compOrder.size(); pos++) {
            int id = compOrder.get(pos);
            System.out.print("Component " + (pos+1) + ":");
            for (int v : comps.get(id)) System.out.print(" " + v);
            System.out.println();
        }

        int C = comps.size();
        //--------------------------------------------------------
        // Summary: Eğer sadece 1 SCC varsa, grafik zaten strongly connected'dır.
        // Precondition: comps listesi oluşturulmuştur.
        // Postcondition: 0 kenar gerektiği yazılır ve program biter.
        //--------------------------------------------------------
        if (C == 1) {
            System.out.println("Already strongly connected");
            System.out.println("New edges needed: 0");
            return;
        }

        //--------------------------------------------------------
        // Summary: Condensation graph için SCC bazında in-degree ve out-degree hesaplanır.
        // Precondition: comp dizisi SCC id’lerini doğru içermelidir.
        // Postcondition: indeg[id] ve outdeg[id] SCC’lerin derecelerini içerir.
        //--------------------------------------------------------
        int[] indeg = new int[C], outdeg = new int[C];
        for (int u = 0; u < N; u++) {
            int cu = comp[u];
            for (int v : g.adj[u]) {
                int cv = comp[v];
                if (cu != cv) {
                    outdeg[cu]++;
                    indeg[cv]++;
                }
            }
        }

        //--------------------------------------------------------
        // Summary: Kaynak SCC'ler (indeg=0) ve kuyu SCC'ler (outdeg=0) bulunur.
        // Precondition: indeg ve outdeg dizileri hesaplanmış olmalıdır.
        // Postcondition: sourceIds ve sinkIds doldurulur.
        //--------------------------------------------------------
        ArrayList<Integer> sourceIds = new ArrayList<>();
        ArrayList<Integer> sinkIds   = new ArrayList<>();

        for (int id = 0; id < C; id++) {
            if (indeg[id] == 0) sourceIds.add(id);
            if (outdeg[id] == 0) sinkIds.add(id);
        }

        //--------------------------------------------------------
        // Summary: Kaynaklar ve kuyular kendi içlerinde minimum vertex değerine göre sıralanır.
        // Precondition: comps listesi bileşenleri içermelidir.
        // Postcondition: sourceIds ve sinkIds sıralanır.
        //--------------------------------------------------------
        Comparator<Integer> byMinVertex =
                (a,b) -> Integer.compare(comps.get(a).get(0), comps.get(b).get(0));

        sourceIds.sort(byMinVertex);
        sinkIds.sort(byMinVertex);

        //--------------------------------------------------------
        // Summary: Strongly connected yapmak için gereken minimum edge sayısını bulur.
        // Precondition: sourceIds ve sinkIds doldurulmuş olmalıdır.
        // Postcondition: needed değeri yazdırılır.
        //--------------------------------------------------------
        int needed = Math.max(sourceIds.size(), sinkIds.size());
        System.out.println("New edges needed: " + needed);

        //--------------------------------------------------------
        // Summary: Kuyuların max elemanı -> Bir sonraki kaynağın min elemanı şeklinde
        //          gerekli kenarları listeler. Döngüsel olarak bağlar (ring).
        // Precondition: comps, sourceIds, sinkIds listeleri dolu olmalıdır.
        // Postcondition: "add: u v" formatında yeni kenarlar yazdırılır.
        //--------------------------------------------------------
        for (int i = 0; i < needed; i++) {
            int sinkComp   = sinkIds.get(i % sinkIds.size());
            int sourceComp = sourceIds.get((i + 1) % sourceIds.size());

            int from = comps.get(sinkComp).get(comps.get(sinkComp).size() - 1); // max eleman
            int to   = comps.get(sourceComp).get(0);                             // min eleman

            System.out.println("add: " + from + " " + to);
        }
    }


    //--------------------------------------------------------
    // Summary: Kosaraju’nun 1. DFS aşaması: Reverse graph üzerinde
    //          iterative DFS yapılır ve finishing order üretilir.
    // Precondition: vis dizisi false ile başlamalıdır.
    // Postcondition: order listesine v'nin bitiş zamanı eklenir.
    //--------------------------------------------------------
    static void dfs1(int u, boolean[] vis, ArrayList<Integer> order) {
        Deque<Integer> st = new ArrayDeque<>();
        Deque<Integer> it = new ArrayDeque<>();
        st.push(u);
        it.push(0);

        while (!st.isEmpty()) {
            int v = st.peek();
            int i = it.pop();

            if (!vis[v]) vis[v] = true;

            ArrayList<Integer> nbrs = g.radj[v];

            if (i < nbrs.size()) {
                it.push(i + 1);
                int w = nbrs.get(i);
                if (!vis[w]) {
                    st.push(w);
                    it.push(0);
                }
            } else {
                st.pop();
                order.add(v);
            }
        }
    }


    //--------------------------------------------------------
    // Summary: Kosaraju’nun 2. aşaması: Normal graf üzerinde DFS ile 
    //          aynı SCC içinde olan düğümleri toplar.
    // Precondition: comp[] dizisinde henüz işlenmemiş düğümlerde -1 olmalıdır.
    // Postcondition: bag listesi bileşendeki düğümleri içerir, comp[v] doldurulur.
    //--------------------------------------------------------
    static void dfs2(int s, int cid, int[] comp, ArrayList<Integer> bag) {
        Deque<Integer> st = new ArrayDeque<>();
        st.push(s);
        comp[s] = cid;

        while (!st.isEmpty()) {
            int v = st.pop();
            bag.add(v);

            for (int w : g.adj[v])
                if (comp[w] == -1) {
                    comp[w] = cid;
                    st.push(w);
                }
        }
    }


    //--------------------------------------------------------
    // Summary: Büyük girişlerde hızlı integer okuma sağlar.
    // Precondition: InputStream geçerli olmalıdır.
    // Postcondition: nextInt() çağrıları integer veya null döndürür.
    //--------------------------------------------------------
    static class FastScanner {
        private final InputStream in;
        private final byte[] buf = new byte[1<<16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream is) { in = is; }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buf);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buf[ptr++];
        }

        //--------------------------------------------------------
        // Summary: Input’tan bir sonraki integer değeri okur.
        // Precondition: Girdi rakamlar ve boşluklardan oluşmalıdır.
        // Postcondition: Bir int veya null döner.
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
