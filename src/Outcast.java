

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
import edu.princeton.cs.algs4.StdOut;

//----------------------------------------------------------------------------
public class Outcast {

    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    /**
     * This function return the outcast noun (noun which its distance is the
     * biggest)
     *
     * As SAP class has an hashmap where we store computed values we don't have
     * to care about the repetitions which can apper while calculating the sum,
     * considering that it won't be recomputed.
     *
     * @param nouns list of nounaa
     * @return outcast noun
     */
    public String outcast(String[] nouns) {
        String outcast = nouns[0];
        int distance = sum(nouns[0], nouns);
        for (int i = 1; i < nouns.length; i++) {
            int temp = sum(nouns[i], nouns);
            if (distance < temp) {
                distance = temp;
                outcast = nouns[i];
            }
        }
        return outcast;
    }

    /**
     * Helper function, which computes the sum of the distances
     *
     * @param main the noun which we want to compute the sum
     * @param list list of noun
     * @return the sum of the distances of the noun "main"
     */
    private int sum(String main, String[] list) {
        int dist = 0;
        for (String string : list) {
            if (main.equals(string)) {
                continue;
            }
            dist += wordnet.distance(main, string);
        }
        return dist;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
