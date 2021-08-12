       /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import java.util.ArrayList;
/**
 *
 * @author li471
 */
public class Algorithm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        
        WordNet prueba = new WordNet("synsets.txt", "hypernyms.txt");
     
        Outcast outcast = new Outcast(prueba);
        In input = new In("outcast.txt");

        String[] tr = input.readAllStrings();
    int[] w ={ 58962 };//{ 1663, 8973, 10145, 10599, 11677, 13750, 17149, 31099, 35216, 45236, 65699 };
    int[] v ={ 27758 };//{ 60309, 71139, 76643 };
        
        ArrayList<Integer> a = new ArrayList<>();
        for (int i : w) {
            a.add(i);
        }
        
        ArrayList<Integer> b = new ArrayList<>();
        for (int i : v) {
            b.add(i);
        }
        System.out.println(outcast.outcast(tr));
        
        In in = new In("digraph.txt");
        
        Digraph graph = new Digraph(in);
        
        SAP sap = new SAP(graph);
        System.out.println(sap.ancestor(a, b));
        System.out.println(sap.length(a,b));
        System.out.println(sap.ancestor(12, 12));
        System.out.println(sap.length(12,12));
        System.out.println(sap.ancestor(7, 2));
        System.out.println(sap.length(7,2));
        System.out.println(sap.ancestor(1, 6));
        System.out.println(sap.length(1,6));
                
    }
     
}
