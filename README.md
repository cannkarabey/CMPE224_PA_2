# CMPE224 / 343 - Homework 2 (Fall 2025)

## 📘 Genel Bakış
Bu ödev, **yönlü grafik (directed graph)** veri yapısını **Java** diliyle sıfırdan tasarlayıp üzerinde iki farklı problem çözmeyi amaçlamaktadır.  
Her iki problem de aynı grafik yapısını kullanır, ancak farklı algoritmalar uygular:

1. **Q1 – Görev Planlama (Topological Sort & Cycle Detection)**  
2. **Q2 – Güçlü Bağlantılı Bileşenler (Strongly Connected Components – Kosaraju Algorithm)**  

Kodlar dışında, ayrıca %50 oranında not etkisine sahip olan **rapor** kısmı da hazırlanmalıdır.

---

## 🧩 Q1: Görev Planlama (Schedulability Check)

### 📖 Problem Tanımı
Her biri diğerine bağımlı olabilen işler (tasks) için yönlü bir grafik verilmiştir.  
Her kenar `u → v`, “u işi bitmeden v işi başlayamaz” anlamına gelir.

- Eğer graf **DAG** (Directed Acyclic Graph) ise, işler sıralanabilir.  
- Eğer graf **cycle** (döngü) içeriyorsa, işler **schedulable değildir.**

### 🧠 Gereken Algoritma
- **DFS tabanlı topological sort**  
- **Cycle detection (onStack + edgeTo yaklaşımı)**  
- **Postorder → reverse postorder** ile topolojik sıra elde edilir.  
- Cycle bulunduğunda bir tane yönlü cycle yazdırılır.

### 💡 Girdi Formatı

N M
u v
u v

- `N`: düğüm sayısı  
- `M`: kenar sayısı  
- Her satır `u → v` bağımlılığını belirtir.  

### 💬 Çıktı Formatı
**Eğer DAG ise:**

Schedulable
Order: 0 1 2 3 ...


**Eğer cycle varsa:**
Not schedulable
Cycle: 0 1 2 0


### 🧮 Kullanılan Sınıflar
- `Digraph` → Yönlü graf yapısı  
- `DirectedCycle` → Döngü tespiti  
- `DepthFirstOrder` → DFS sonrası postorder listesi  
- `Topological` → Sıralama kontrolü  
- `SchedulerTester` → Main sınıfı, tüm işlemleri yürütür  

---

## 🧭 Q2: Güçlü Bağlantılı Bileşenler (SCC)

### 📖 Problem Tanımı
Verilen yönlü bir graf için:
1. Tüm **Strongly Connected Components (SCC)**’leri bul.  
2. Her SCC’yi “Component i: ...” formatında yazdır.  
3. Eğer graf zaten strongly connected ise:
```
Already strongly connected
New edges needed: 0
```
4. Değilse, **grafı strongly connected yapmak için gereken minimum yeni kenar** sayısını bul.

### 🧠 Gereken Algoritma
- **Kosaraju-Sharir 2-pass algorithm**
1. Grafın tersini (`reverse()`) al.
2. Ters graf üzerinde DFS → reverse postorder elde et.
3. Orijinal graf üzerinde bu sırayla DFS çalıştır → her DFS yeni SCC oluşturur.
- Her bileşenin in-degree ve out-degree bilgileri hesaplanır.
- Gerekli yeni kenar sayısı:
```
max(#sources, #sinks)
```

### 💬 Çıktı Formatı
```
Component 1: 0 1 2
Component 2: 3 4 5
Component 3: 6
Component 4: 7
New edges needed: 2
add: 5 6
add: 7 0
```

veya eğer zaten strongly connected ise:
```
Component 1: 0 1 2 3
Already strongly connected
New edges needed: 0
```

### 🧮 Kullanılan Sınıflar
- `Digraph` → Yönlü graf yapısı (Q1 ile ortak)
- `KosarajuSCC` → SCC tespiti
- `DepthFirstOrder` → DFS sırası için
- `SCCTester` → Main sınıfı, Q2’nin yürütücüsü

---

## 🧱 Ortak Kod Yapısı

### 📂 Dosya Organizasyonu
```
│── Digraph.java
│── DirectedCycle.java
│── DepthFirstOrder.java
│── Topological.java
│── SchedulerTester.java (Q1 main)
│── KosarajuSCC.java
│── SCCTester.java (Q2 main)
```

Her sınıfın başında açıklayıcı başlık bloğu (Title, Author, ID, Section, Assignment, Description) bulunmalıdır.  
Her methodun üzerinde ayrıca şu üçlü açıklama bulunmalıdır:
```java
// Summary: ...
// Precondition: ...
// Postcondition: ...
```

## 📄 Rapor Formatı (%50)
| Bölüm | Açıklama | Puan |
|-------|----------|------|
| 1. Information | Ad, ID, Section, Assignment no | 2.5 |
| 2. Problem Statement & Code Design | Problemi anlat, yapıyı ve modülleri açıkla, structure chart ekle | 15 |
| 3. Implementation & Functionality | Her methodun işlevi, parametreleri, pseudocode | 20 |
| 4. Testing | Test verileri, beklenen çıktı, analiz | 7.5 |
| 5. Final Assessment | Zorluklar, öğrenilenler, AI kullanımı, etik beyan | 5 |

## ⚠️ Kurallar & Uyarılar
- **Kütüphane kullanımı yasak.** Kendi Digraph sınıfını yazmak zorundasın.
- **Dosya isimlerini değiştirme.** VPL kontrolü başarısız olur.
- **Yorumlar zorunlu.** Her method açıklanmalı.
- **Kod düzeni önemlidir.** Girinti, boşluk, camelCase kullanımına dikkat.
- **Rapor PDF olarak yüklenmeli.**
- **AI kullanımı açıkça belirtilmelidir.**

## ✅ Özet
| Soru | Konu | Ana Algoritma | Çıktı | Dosyalar |
|------|------|----------------|--------|---------|
| Q1 | Görev planlama | DFS + Topological Sort | Schedulable / Not Schedulable | Digraph, DirectedCycle, Topological, SchedulerTester |
| Q2 | SCC + Yeni Kenarlar | Kosaraju 2-pass | SCC listesi + new edges | Digraph, KosarajuSCC, SCCTester |

## 📚 Kaynak ve İlham
Bu ödev, Robert Sedgewick - Algorithms (4th Edition) kitabındaki yönlü grafik uygulamalarına dayanmaktadır.
Ayrıca, CMPE224 dersi kapsamında anlatılan:

DFS, postorder, cycle detection,

Kosaraju ve condensation DAG kavramlarını pratiğe dökmeyi amaçlar.

## 💬 Sonuç
Bu ödev sayesinde:

Graf veri yapısı temelleri öğrenilir.

DFS, topological sort, SCC algoritmaları uygulamalı olarak pekiştirilir.

Kapsamlı kod dokümantasyonu ve raporlama becerileri geliştirilir.

Gerçek dünya problemine benzer görev planlama ve ağ bağlantısı senaryoları modellenir.

------------------------------------------------------------------------------------------

# CMPE224 / 343 - Homework 2 (Fall 2025)

## 📘 Overview
This assignment aims to design a **directed graph** data structure from scratch in **Java** and solve two different problems using it.  
Both problems use the same graph structure but apply different algorithms:

1. **Q1 – Task Scheduling (Topological Sort & Cycle Detection)**  
2. **Q2 – Strongly Connected Components (Kosaraju Algorithm)**  

In addition to the code, a **report** section with a 50% weight on the grade must also be prepared.

---

## 🧩 Q1: Task Scheduling (Schedulability Check)

### 📖 Problem Definition
Given a directed graph for tasks that may depend on each other.  
Each edge `u → v` means "task v cannot start until task u is completed."

- If the graph is a **DAG** (Directed Acyclic Graph), tasks can be scheduled.  
- If the graph contains a **cycle**, tasks are **not schedulable.**

### 🧠 Required Algorithm
- **DFS-based topological sort**  
- **Cycle detection (onStack + edgeTo approach)**  
- **Postorder → reverse postorder** yields the topological order.  
- When a cycle is found, one directed cycle is printed.

### 💡 Input Format
```
N M
u v
u v
...
```
- `N`: number of nodes  
- `M`: number of edges  
- Each line specifies a `u → v` dependency.  

### 💬 Output Format
**If it's a DAG:**
```
Schedulable
Order: 0 1 2 3 ...
```

**If there's a cycle:**
```
Not schedulable
Cycle: 0 1 2 0
```

### 🧮 Classes Used
- `Digraph` → Directed graph structure  
- `DirectedCycle` → Cycle detection  
- `DepthFirstOrder` → Postorder list after DFS  
- `Topological` → Order checking  
- `SchedulerTester` → Main class, executes all operations  

---

## 🧭 Q2: Strongly Connected Components (SCC)

### 📖 Problem Definition
For a given directed graph:
1. Find all **Strongly Connected Components (SCC)**.  
2. Print each SCC in the format "Component i: ...".  
3. If the graph is already strongly connected:
```
Already strongly connected
New edges needed: 0
```
4. Otherwise, find the **minimum number of new edges needed to make the graph strongly connected**.

### 🧠 Required Algorithm
- **Kosaraju-Sharir 2-pass algorithm**
1. Get the reverse of the graph (`reverse()`).
2. Run DFS on the reversed graph → obtain reverse postorder.
3. Run DFS on the original graph in this order → each DFS creates a new SCC.
- Calculate in-degree and out-degree information for each component.
- Number of new edges needed:
```
max(#sources, #sinks)
```

### 💬 Output Format
```
Component 1: 0 1 2
Component 2: 3 4 5
Component 3: 6
Component 4: 7
New edges needed: 2
add: 5 6
add: 7 0
```

or if already strongly connected:
```
Component 1: 0 1 2 3
Already strongly connected
New edges needed: 0
```

### 🧮 Classes Used
- `Digraph` → Directed graph structure (shared with Q1)
- `KosarajuSCC` → SCC detection
- `DepthFirstOrder` → For DFS order
- `SCCTester` → Main class, Q2 executor

---

## 🧱 Common Code Structure

### 📂 File Organization
```
│── Digraph.java
│── DirectedCycle.java
│── DepthFirstOrder.java
│── Topological.java
│── SchedulerTester.java (Q1 main)
│── KosarajuSCC.java
│── SCCTester.java (Q2 main)
```

Each class should have a descriptive header block at the beginning (Title, Author, ID, Section, Assignment, Description).  
Each method should also have the following triple explanation:
```java
// Summary: ...
// Precondition: ...
// Postcondition: ...
```

## 📄 Report Format (50%)
| Section | Description | Points |
|---------|-------------|--------|
| 1. Information | Name, ID, Section, Assignment no | 2.5 |
| 2. Problem Statement & Code Design | Explain the problem, describe structure and modules, include structure chart | 15 |
| 3. Implementation & Functionality | Function of each method, parameters, pseudocode | 20 |
| 4. Testing | Test data, expected output, analysis | 7.5 |
| 5. Final Assessment | Challenges, lessons learned, AI usage, ethical statement | 5 |

## ⚠️ Rules & Warnings
- **No library usage allowed.** You must write your own Digraph class.
- **Do not change file names.** VPL check will fail.
- **Comments are mandatory.** Every method must be explained.
- **Code formatting matters.** Pay attention to indentation, spacing, camelCase usage.
- **Report must be uploaded as PDF.**
- **AI usage must be clearly stated.**

## ✅ Summary
| Question | Topic | Main Algorithm | Output | Files |
|----------|-------|----------------|--------|-------|
| Q1 | Task scheduling | DFS + Topological Sort | Schedulable / Not Schedulable | Digraph, DirectedCycle, Topological, SchedulerTester |
| Q2 | SCC + New Edges | Kosaraju 2-pass | SCC list + new edges | Digraph, KosarajuSCC, SCCTester |

## 📚 Sources and Inspiration
This assignment is based on directed graph applications in Robert Sedgewick's **Algorithms (4th Edition)** book.
Additionally, it aims to put into practice:
- DFS, postorder, cycle detection,
- Kosaraju and condensation DAG concepts taught in the CMPE224 course.

## 💬 Conclusion
Through this assignment:
- Graph data structure fundamentals are learned.
- DFS, topological sort, SCC algorithms are reinforced through practice.
- Comprehensive code documentation and reporting skills are developed.
- Task scheduling and network connectivity scenarios similar to real-world problems are modeled.
