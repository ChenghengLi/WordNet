
import java.util.HashMap;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author Chengheng Li Chen
 */
// ADD ALL IMPORTS FROM "algs4" HERE (Bag, In, StdIn, StdOut, Digraph, Queue)
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
//---------------------------------------------------------------------------

public class SAP {

    // Digraph which we work with
    private final Digraph graph;

    // An hashmap which works to storage computed values (CACHE)
    // I should point out that the HashSet can storage Object classes instances,
    // so we can store lists as integers
    private final HashMap<HashSet<Object>, int[]> storage;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Null argument in the constructor");
        }
        graph = G;
        storage = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return compute(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return compute(v, w)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return compute(v, w)[0];

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return compute(v, w)[1];

    }

    /**
     * Computes the shortest path using bfs and then returns an array with the
     * information which we need: the length of the path and the ancestor
     *
     * @param v index of the first vertex
     * @param w index of the other vertex
     * @return array of lenght 2 which the first value is the length of the path
     * and the second the common ancestor
     */
    private int[] compute(int v, int w) {
        // Checking the input
        check(v);
        check(w);

        // Creating the set
        HashSet<Object> set = new HashSet<>();
        set.add(v);
        set.add(w);

        // Checks if the path was computed
        if (storage.get(set) != null) {
            return storage.get(set);
        }

        // Initializing values to -1
        int dist = -1;
        int index = -1;

        // Initializing arrays for the bfs (Value v)
        boolean[] markedV = new boolean[graph.V()];
        int[] distToV = new int[graph.V()];
        Queue<Integer> q = new Queue<Integer>();

        // First bfs of the value v
        markedV[v] = true;
        distToV[v] = 0;
        q.enqueue(v);
        while (!q.isEmpty()) {
            int s = q.dequeue();
            for (int r : graph.adj(s)) {
                if (!markedV[r]) {
                    distToV[r] = distToV[s] + 1;
                    markedV[r] = true;
                    q.enqueue(r);
                }
            }
        }

        // Initializing arrays for the bfs (Value w)
        boolean[] markedW = new boolean[graph.V()];
        int[] distToW = new int[graph.V()];

        // Second bfs, if w was marked in the first bfs we found the shortest path
        if (!markedV[w]) {
            markedW[w] = true;
            distToW[w] = 0;
        } else {
            if (distToV[w] == 0) {
                int[] result = {0, v};
                return result;
            }
            dist = distToV[w] - 1;
            index = w;
        }

        q.enqueue(w);
        while (!q.isEmpty()) {
            int s = q.dequeue();
            for (int r : graph.adj(s)) {
                if (!markedW[r]) {
                    distToW[r] = distToW[s] + 1;
                    markedW[r] = true;
                    q.enqueue(r);

                }
                if (markedV[r]) {
                    if (dist == -1) {
                        dist = distToV[r] + distToW[s];
                        index = r;
                    } else if (distToV[r] + distToW[s] < dist) {
                        dist = distToV[r] + distToW[s];
                        index = r;
                    }
                }

            }
        }

        // Returning the solution and adding the solution to the computed solution map
        int[] result = {dist == -1 ? -1 : dist + 1, index};
        storage.put(set, result);
        return result;

    }

    /**
     * Computes the shortest path using bfs and then returns an array with the
     * information which we need: the length of the path and the ancestor
     *
     * @param v list if indexes
     * @param w list of indexes
     * @return array of lenght 2 which the first value is the length of the path
     * and the second the common ancestor
     */
    private int[] compute(Iterable<Integer> v, Iterable<Integer> w) {
        check(v);
        check(w);

        HashSet<Object> set = new HashSet<>();
        set.add(v);
        set.add(w);

        if (storage.get(set) != null) {
            return storage.get(set);
        }

        int dist = -1;
        int index = -1;

        boolean[] markedV = new boolean[graph.V()];
        int[] distToV = new int[graph.V()];
        Queue<Integer> q = new Queue<Integer>();

        // Adding all vertices to the queue, starts the first bfs
        for (Integer i : v) {
            markedV[i] = true;
            distToV[i] = 0;
            q.enqueue(i);
        }

        while (!q.isEmpty()) {
            int s = q.dequeue();
            for (int r : graph.adj(s)) {
                if (!markedV[r]) {
                    distToV[r] = distToV[s] + 1;
                    markedV[r] = true;
                    q.enqueue(r);
                }
            }
        }

        boolean[] markedW = new boolean[graph.V()];
        int[] distToW = new int[graph.V()];

        // Adding all vertices to the queue, starts the second bfs
        for (Integer i : w) {
            markedW[i] = true;
            distToW[i] = 0;
            q.enqueue(i);
            // Checking the shortest path
            if (markedV[i]) {
                if (dist == -1) {
                    if (distToV[i] == 0) {
                        int[] result = {0, i};
                        return result;
                    }
                    dist = distToV[i] - 1;
                    index = i;
                } else if (distToV[i] - 1 < dist) {
                    if (distToV[i] == 0) {
                        int[] result = {0, i};
                        return result;
                    }
                    dist = distToV[i] - 1;
                    index = i;
                }
            }
        }

        while (!q.isEmpty()) {
            int s = q.dequeue();
            for (int r : graph.adj(s)) {
                if (!markedW[r]) {
                    distToW[r] = distToW[s] + 1;
                    markedW[r] = true;
                    q.enqueue(r);
                    // Checking the shortest pat
                }
                if (markedV[r]) {
                    if (dist == -1) {
                        dist = distToV[r];
                        index = r;
                    } else if (distToV[r] + distToW[s] < dist) {
                        dist = distToV[r] + distToW[s];
                        index = r;
                    }
                }
            }
        }

        // Returning the solution and adding the solution to the computed solution map
        int[] result = {dist == -1 ? -1 : dist + 1, index};
        storage.put(set, result);
        return result;
    }

    /**
     * Checks if a vertex id is correct
     *
     * @param i vertex to check
     */
    private void check(int i) {
        if (i < 0 || i >= graph.V()) {
            throw new IllegalArgumentException("Vertex index out of range");
        }
    }

    /**
     * Check a list of vertex is correct
     *
     * @param i list of vertex
     */
    private void check(Iterable<Integer> i) {
        if (i == null) {
            throw new IllegalArgumentException("Null argument");
        }
        for (Integer integer : i) {
            if (integer == null) {
                throw new IllegalArgumentException("Null integer");
            }
            check(integer);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
