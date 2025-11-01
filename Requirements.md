1. Büyük Resim – Bu ödev ne istiyor?

Ders: CMPE 224-343, Fall 2025

Ödev adı: Programming Homework 2

Teslim: 9 Kasım 2025 Pazar 23:55’e kadar (LMS’e yükleme) 

CMPE224-343 HW2 Fall2025

Dil / Platform: Java

Dış kütüphane: YOK. Kendi graf yapını kendin yazacaksın. “Ben ArrayList of ArrayList kullandım ama Graph diye sınıf yazmadım” dersen puanlamazlar diyor. “Any solutions without using graph data structure are not evaluated!” cümlesi tam bu. 

CMPE224-343 HW2 Fall2025

İki programlama sorusu var:

Q1 (25p): Yönlü graf üzerinde topological sort veya cycle bulma

Q2 (25p): Yönlü graf üzerinde SCC (Kosaraju-Sharir 2-pass) + condensation DAG mantığı + ağın tamamını strongly connected yapmak için minimum yeni kenarları söyleme. 

CMPE224-343 HW2 Fall2025

Ayrıca rapor (%50) istiyor ve formatı çok net: Information, Problem Statement and Code Design, Implementation and Functionality, Testing, Final Assessments sırasına uymak zorundasın. 

CMPE224-343 HW2 Fall2025

Kodlar VPL’e ayrı ayrı yüklenecek. Yüklemezsen notlanmaz. Ve “sınıf isimlerini değiştirme” şartı var. 

CMPE224-343 HW2 Fall2025

Kod yorumları puanlanıyor. Örneği de vermiş: her methodun üstüne Summary / Precondition / Postcondition yaz. Aynı senin daha önce kullandığın şablon. 

CMPE224-343 HW2 Fall2025

2. Q1 – “Schedulable mı değil mi?” (Topological Sort + Cycle)
2.1. Problem

Sana N tane iş (0..N-1) ve M tane yönlü bağımlılık veriyorlar. u v satırı “u önce bitsin sonra v başlasın” demek yani u → v kenarı.
İstediği şey:

Eğer bu graf DAG ise (directed acyclic graph)

“Schedulable” yaz

Altına Order: ... diye herhangi bir geçerli topolojik sıra yaz.

Eğer graf DAG değilse (yani en az bir yönlü cycle varsa)

“Not schedulable” yaz

Altına Cycle: ... diye bir tane yönlü cycle’ı sırayla yaz (başladığın vertexle bitir).
Örn: Cycle: 0 1 2 0
Bu cycle’ın rotasyonu da olur diyor. 

CMPE224-343 HW2 Fall2025

2.2. Önemli ayrıntılar

Girdi formatı:

N M
u v
u v
...


N: vertex sayısı, M: kenar sayısı. Vertex label’ları 0..N-1 aralığında.

Self loop / parallel edge yok diyor ama “programın bunlara dayanıklı olsun” da demiş. Yani sen eklerken “aynı kenar varsa ekleme” veya “u==v ise görmezden gel” dersen +puan. 

CMPE224-343 HW2 Fall2025

2.3. Yöntem (DFS ile)

Ödev ipucu veriyor:

Topological order için DFS postorder’ı ters çevir

Cycle tespiti için onStack[] kullan

Cycle bulunduğunda parent/edgeTo[] ile cycle’ı rekonstrükte et.
Bu, Sedgewick’in “DirectedCycle”, “DepthFirstOrder” ve “Topological” üçlüsünün aynısı. Yani hocanız da aynısını istiyor. 

CMPE224-343 HW2 Fall2025

2.4. Çıktı formatı

DAG ise:

Schedulable
Order: 0 1 2 3 ...


Cycle varsa:

Not schedulable
Cycle: 0 1 2 0

3. Q2 – “SCC bul, sonra ağı strongly connected yap”
3.1. Problem

Bu sefer vertex = datacenter, directed edge = tek yönlü bağlantı.
İstediği 4 şey var:

Kosaraju-Sharir 2-pass ile tüm SCC’leri bul.

Her SCC’yi “Component i: ...” formatında yaz. i artan sırada olacak ama sıralama “component’in en küçük vertex ID’sine göre artan” olacak. Yani önce {0,1,2} sonra {3,4,5} gibi. (Sample buna örnek.) 

CMPE224-343 HW2 Fall2025

Eğer SCC sayısı = 1 ise:

“Already strongly connected”

“New edges needed: 0”

Eğer SCC sayısı > 1 ise:

Condensation DAG’ini kafanda kur (print etme)

Bu DAG’de source SCC sayısını ve sink SCC sayısını bul

Cevap = max(#sources, #sinks)

Sonra da o kadar “add: x y” satırı bas

“Her geçerli çözüm olur” diyor, yani klasik “sink’leri source’lara halka yap” çözümü kabul. 

CMPE224-343 HW2 Fall2025

3.2. Neden böyle?

Condensation DAG her zaman DAG’dir. DAG’i strongly connected yapmak için en az max(src,sink) kadar yeni kenar gerekir – bu dersin/theory’nin bilinen sonucu. Onu uyguluyorsun.

3.3. Girdi/Çıktı formatı

Girdi:

N M
u v
...


Çıktı Örnek 1:

Component 1: 0 1 2
Component 2: 3 4 5
Component 3: 6
Component 4: 7
New edges needed: 2
add: 5 6
add: 7 0


Burada yaptı: ilk SCC’nin en küçük vertex’i 0 → o Component 1
ikinci SCC’nin en küçüğü 3 → o Component 2
vs.
Sonra 2 tane kenar bastı. Tam da açıklamaya uygun. 

CMPE224-343 HW2 Fall2025

Çıktı Örnek 2 (zaten strongly connected):

Component 1: 0 1 2 3
Already strongly connected
New edges needed: 0

4. Rapor kısmı (bunu da anlaman gerekiyor)

Sen kodu yazacaksın ama rapor da %50. Raporda şunlar olacak:

Information (%2.5) – ID, ad-soyad, section, assignment no

Problem Statement and Code Design (%15) – kısaca problemi anlatıp structure chart göster (top-down decomposition)

Implementation and Functionality (%20) – her alt modülü açıkla, parametreleri, pseudocode’u ver

Testing (%7.5) – kendi testlerini anlat, ek test data üret, neyi test ettin yaz

Final Assessments (%5) – zorluklar, ne öğrendin, AI kullandın mı, etik sınır vs. (özellikle AI sorusunu sormuşlar)
Bunu PDF olarak 3 sayfayı geçmeyecek şekilde yüklüyorsun. 

CMPE224-343 HW2 Fall2025

5. Kod organizasyonu nasıl olmalı?

Bu tür ödevlerde asıl kritik şey: ortak bir Digraph sınıfı yazıp hem Q1’de hem Q2’de kullanmak. Çünkü ikisi de yönlü graf istiyor. Ödev “kendi graph data structure’ını yaz” dediği için şöyle bir yapı en temiz olanı:

Digraph.java – yönlü graf, adjacency list tutar

DirectedDFS.java veya “ihtiyaç var mı?” – Q2’de bile ters graf üstünden DFS yapacağız

DepthFirstOrder.java – Q1’de topological için, Q2’de reversed graph üstünden order için

DirectedCycle.java – Q1 için cycle bulucu

Topological.java – Q1 için topological order verici

KosarajuSCC.java – Q2 için

Scheduler.java (veya HW2Q1.java) – Q1’in main’i

SCCMaker.java (veya HW2Q2.java) – Q2’nin main’i

Ödev “sınıf isimlerini değiştirme” demiş ama PDF’te kesin isimler listeli değil; genelde VPL’de sana bir main ismi verirler. Senin yapacağın en güvenli şey: iki ayrı main class yazmak ve adlarını PDF’teki örneğe benzetmek:

SchedulerTester.java (Q1 için) – PDF’teki örnek başlık da zaten “Scheduler tester class” diyor. 

CMPE224-343 HW2 Fall2025

SCCTester.java (Q2 için)

Aşağıda bu isimlerle template veriyorum.

6. Code Template’leri

Şimdi sana doldurup çalıştırabileceğin temel bir iskelet veriyorum. Hepsi Java, hepsinde senin istediğin yorum formatı var (Title, Author, ID, Section, Assignment, Description) ve her method’un üstünde Summary/Pre/Post var. Sen sadece:

adını

id’ni

section’ını

assignment no’yu

ve method gövdelerini
dolduracaksın.

6.1. Digraph.java
//-----------------------------------------------------
// Title: Directed Graph Class
// Author: YOUR_NAME
// ID: YOUR_ID
// Section: YOUR_SECTION
// Assignment: HW2 - Q1/Q2
// Description: This class represents a directed graph
// using adjacency lists. It will be used in both
// scheduler (topological sort) and SCC questions.
//-----------------------------------------------------
import java.util.ArrayList;
import java.util.List;

public class Digraph {

    private final int V;               // number of vertices
    private int E;                     // number of edges
    private List<Integer>[] adj;       // adjacency lists

    //--------------------------------------------------------
    // Summary: Constructs a digraph with V vertices and no edges.
    // Precondition: V > 0
    // Postcondition: A new directed graph with V empty adjacency lists is created.
    //--------------------------------------------------------
    @SuppressWarnings("unchecked")
    public Digraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (List<Integer>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    //--------------------------------------------------------
    // Summary: Adds a directed edge v -> w to the graph.
    // Precondition: 0 <= v < V, 0 <= w < V
    // Postcondition: Edge (v, w) is inserted into adjacency list of v.
    //--------------------------------------------------------
    public void addEdge(int v, int w) {
        // optional: ignore self-loops or parallels if needed
        adj[v].add(w);
        E++;
    }

    //--------------------------------------------------------
    // Summary: Returns the adjacency list of vertex v.
    // Precondition: 0 <= v < V
    // Postcondition: The adjacency list of v is returned.
    //--------------------------------------------------------
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    //--------------------------------------------------------
    // Summary: Returns number of vertices.
    // Precondition: none
    // Postcondition: vertex count is returned.
    //--------------------------------------------------------
    public int V() {
        return V;
    }

    //--------------------------------------------------------
    // Summary: Returns number of edges.
    // Precondition: none
    // Postcondition: edge count is returned.
    //--------------------------------------------------------
    public int E() {
        return E;
    }

    //--------------------------------------------------------
    // Summary: Returns the reverse of this digraph.
    // Precondition: none
    // Postcondition: A new digraph whose edges are reversed is returned.
    //--------------------------------------------------------
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

6.2. DirectedCycle.java (Q1 için cycle tespit)
//-----------------------------------------------------
// Title: Directed Cycle Finder Class
// Author: YOUR_NAME
// ID: YOUR_ID
// Section: YOUR_SECTION
// Assignment: HW2 - Q1
// Description: This class detects a directed cycle in a
// given digraph using DFS and on-stack technique.
//-----------------------------------------------------
import java.util.Stack;

public class DirectedCycle {

    private boolean[] marked;
    private int[] edgeTo;
    private boolean[] onStack;
    private Stack<Integer> cycle;  // if non-null, we found a cycle

    //--------------------------------------------------------
    // Summary: Runs DFS on the given digraph to find a cycle.
    // Precondition: G is not null
    // Postcondition: If a cycle exists, it will be stored in 'cycle'.
    //--------------------------------------------------------
    public DirectedCycle(Digraph G) {
        int V = G.V();
        marked = new boolean[V];
        onStack = new boolean[V];
        edgeTo = new int[V];
        for (int v = 0; v < V; v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (cycle != null) return; // already found
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            } else if (onStack[w]) {
                // found a cycle
                cycle = new Stack<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    //--------------------------------------------------------
    // Summary: Checks if the graph has a directed cycle.
    // Precondition: constructor must be called first.
    // Postcondition: true if a cycle is found.
    //--------------------------------------------------------
    public boolean hasCycle() {
        return cycle != null;
    }

    //--------------------------------------------------------
    // Summary: Returns the directed cycle found.
    // Precondition: hasCycle() == true
    // Postcondition: The cycle stack is returned.
    //--------------------------------------------------------
    public Iterable<Integer> cycle() {
        return cycle;
    }
}

6.3. DepthFirstOrder.java (Q1 & Q2 için)
//-----------------------------------------------------
// Title: Depth First Order Class
// Author: YOUR_NAME
// ID: YOUR_ID
// Section: YOUR_SECTION
// Assignment: HW2
// Description: Computes preorder, postorder and
// reverse postorder of a digraph.
//-----------------------------------------------------
import java.util.Stack;

public class DepthFirstOrder {

    private boolean[] marked;
    private Stack<Integer> reversePost;

    public DepthFirstOrder(Digraph G) {
        marked = new boolean[G.V()];
        reversePost = new Stack<>();
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        reversePost.push(v);
    }

    public Iterable<Integer> reversePost() {
        return reversePost;
    }
}

6.4. Topological.java (Q1 için)
//-----------------------------------------------------
// Title: Topological Order Class
// Author: YOUR_NAME
// ID: YOUR_ID
// Section: YOUR_SECTION
// Assignment: HW2 - Q1
// Description: If a digraph is a DAG, this class
// computes its topological order using reverse postorder.
//-----------------------------------------------------
public class Topological {

    private Iterable<Integer> order;

    //--------------------------------------------------------
    // Summary: Creates a topological order if graph is a DAG.
    // Precondition: G is not null
    // Postcondition: order will be null if G has a cycle.
    //--------------------------------------------------------
    public Topological(Digraph G) {
        DirectedCycle cycleFinder = new DirectedCycle(G);
        if (!cycleFinder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        } else {
            order = null;
        }
    }

    //--------------------------------------------------------
    // Summary: Returns the topological order.
    // Precondition: Graph must be DAG.
    // Postcondition: topological order is returned or null.
    //--------------------------------------------------------
    public Iterable<Integer> order() {
        return order;
    }

    public boolean isDAG() {
        return order != null;
    }
}

6.5. SchedulerTester.java (Q1 main’i)
//-----------------------------------------------------
// Title: Scheduler tester class
// Author: YOUR_NAME
// ID: YOUR_ID
// Section: YOUR_SECTION
// Assignment: HW2 - Q1
// Description: Reads a precedence-constrained task graph
// from standard input, checks if it is schedulable. If it is
// prints a valid topological order, otherwise prints a cycle.
//-----------------------------------------------------
import java.util.Scanner;

public class SchedulerTester {

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


Bu sınıf doğrudan PDF’teki Q1 input/output formatını basıyor. “Any valid order/cycle is OK” dediği için bu kabul. 

CMPE224-343 HW2 Fall2025

6.6. KosarajuSCC.java (Q2’nin kalbi)
//-----------------------------------------------------
// Title: Kosaraju-Sharir SCC Class
// Author: YOUR_NAME
// ID: YOUR_ID
// Section: YOUR_SECTION
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

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    public int count() {
        return count;
    }

    public int id(int v) {
        return id[v];
    }
}

6.7. SCCTester.java (Q2 main’i)

Burada biraz daha iş var çünkü:

SCC’leri yazacağız (küçük vertex’i küçük olan önce)

Sonra #SCC == 1 mi bakacağız

Değilse source/sink sayacağız

Sonra da kenar önereceğiz

Aşağıdaki template bunu yapacak şekilde düzenlenmiş ama sen istersen add: ... eşlemesini farklı yapabilirsin, çünkü ödev “her geçerli set kabul” diyor. 👇

//-----------------------------------------------------
// Title: SCC Tester Class
// Author: YOUR_NAME
// ID: YOUR_ID
// Section: YOUR_SECTION
// Assignment: HW2 - Q2
// Description: Reads a directed graph, finds SCCs using
// Kosaraju-Sharir, prints components in required format,
// then prints minimum edges to make the graph strongly
// connected.
//-----------------------------------------------------
import java.util.*;

public class SCCTester {

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


Bu template:

SCC’leri istenen sırada basıyor

#SCC == 1 ise doğru mesajı basıyor

değilse kaynak/sink sayıyor

sonra da max(src,sink) kadar kenar basıyor
PDF’in istediği tam bu. 

CMPE224-343 HW2 Fall2025

7. Dikkat etmen gereken son kurallar

İsimleri değiştirme – VPL’de hangi ismi beklediğini LMS’te görebilirsin. Eğer orada “Main.java” diyorsa, sen yukarıdaki kodları tek dosyada toparlayıp Main’den çağırırsın.

Yorumları eksik bırakma – TA açıkça “kodu incelemeden puanlayacağım, her şeyi yorumla” diyor. Biz yukarıya formatı koyduk.

Indentation – tab kullanma diyorlardı senin derste de, burada da “indentation, indentation…” diye uyarmış.

Raporunda AI sorusuna cevap ver – özellikle sormuşlar. 

CMPE224-343 HW2 Fall2025

8. Özet

Q1: Aynı grafı okuyorsun → cycle var mı? → yoksa topo sırayı yaz, varsa cycle’ı yaz.

Q2: Aynı tip grafı okuyorsun → Kosaraju ile SCC → SCC’leri küçük vertex’e göre sırala → eğer 1 SCC → “Already strongly connected” → else → source/sink say → max(src,sink) kadar add: yaz.

Hepsi kendi graph sınıfınla yapılacak.

Hepsi Java, iyi yorumlanmış olacak.

Rapor zorunlu.