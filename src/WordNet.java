
import java.util.ArrayList;
import java.util.HashMap;


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
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedCycle;
// --------------------------------------------------------------------------

public class WordNet {

    // ArrayList to store the nouns with their index (each noun is saved in the corresponding index)
    private final ArrayList<String> list;
    // SAP object
    private final SAP sap;
    // Hashmap in order to storage the noun and the indexes of the noun
    private final HashMap<String, Bag<Integer>> map;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null parameters in the constructor");
        }

        // Creates a input file (synsets)
        In input = new In(synsets);
        // Initializing objects
        list = new ArrayList<String>();
        map = new HashMap<String, Bag<Integer>>();

        // Starts reading synsets file 
        try {
            while (input.hasNextLine()) {
                String line = input.readLine();
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                list.add(parts[1]);
                String[] nouns = parts[1].split(" ");
                for (String n : nouns) {
                    if (map.get(n) != null) {
                        Bag<Integer> bag = map.get(n);
                        bag.add(id);
                    } else {
                        Bag<Integer> bag = new Bag<Integer>();
                        bag.add(id);
                        map.put(n, bag);
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not integer inserted");
        }

        // Creates a input file (hypernyms)
        In input1 = new In(hypernyms);

        // Initializing objects
        Digraph graph = new Digraph(list.size());

        // Start reading hypenyms file while is adding edges to the digraph
        try {
            while (input1.hasNextLine()) {
                String line = input1.readLine();
                String[] conections = line.split(",");

                if (conections.length <= 1) {
                    continue;
                }
                for (int i = 1; i < conections.length; i++) {
                    graph.addEdge(Integer.parseInt(conections[0]), Integer.parseInt(conections[i]));
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not integer inserted");
        }

        // Checking if the graph has cycles
        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle())
            throw new IllegalArgumentException("DAG no introduced");
        
        int rootNum = 0;
        for (int v = 0; v < list.size() ; v++)
            if (graph.outdegree(v) == 0)
                rootNum++;
        if (rootNum != 1)
            throw new IllegalArgumentException("Input has " + rootNum + " roots.");

        sap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();

    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Null argument");
        // Since map is a Hash map, the cost of checking if the noun is in the map is O(1)
        return map.containsKey(word);

        // We can also use the binary search O(log n) algorithm since the input is lexicogramaticatly sorted
        /*
        int l = 0, r = list.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            // Check if x is present at mid
            if (list.get(m).equals(word)) {
                return true;
            }
            // If x greater, ignore left half
            if (list.get(m).compareTo(word) < 0) {
                l = m + 1;
            } // If x is smaller, ignore right half
            else {
                r = m - 1;
            }
        }
        // if we reach here, then element was
        // not present
        return false;
         */
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Bag<Integer> a = check(nounA);
        Bag<Integer> b = check(nounB);
        return sap.length(a, b);

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Bag<Integer> a = check(nounA);
        Bag<Integer> b = check(nounB);
        int index = sap.ancestor(a, b);
        return index == -1 ? "No path exists" : list.get(index);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        System.out.println(wordnet.nouns());
    }

    /**
     * Helper functuon which checks if a noun is in the net
     *
     * @param noun noun to check
     * @return indexs which the noun appears
     */
    private Bag<Integer> check(String noun) {
        if (this.isNoun(noun)) {
            return map.get(noun);
        } else {
            throw new IllegalArgumentException("Noun no found in the wordnet");
        }
    }
}
