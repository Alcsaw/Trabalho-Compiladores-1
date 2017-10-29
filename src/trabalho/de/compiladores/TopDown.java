package trabalho.de.compiladores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TopDown {

    private String[] productions;
    private Map<String, List<String>> mapa;
    private Map<String, List<String>> first;
    private Stack<String> stack;

    public TopDown(String[] productions) {
        mapa = new HashMap<>();
        first = new HashMap<>();
        stack = new Stack<>();
        this.productions = productions;
        createMap();
        first();
    }

    private void createMap() {
        for (String s : productions) {
            mapa.put(s.substring(0, s.indexOf("->")), Arrays.asList(s.substring(s.indexOf("->") + 2, s.length()).split("\\|")));
        }
    }

    private void first() {
        for (Map.Entry<String, List<String>> e : mapa.entrySet()) {
            List<String> line = new ArrayList<>();

            e.getValue().forEach((s) -> {
                //if (s.substring(0, 1).equals("E") || s.substring(0, 1).matches("[a-z]")) {
                    line.add(s.substring(0, 1));
                //} else {
                    //if (s.substring(0, 1).matches("[A-Z]")) {
                        stack.push(s.substring(0, 1));
                   // }
                //}
            });
            first.put(e.getKey(), line);
        }

    }

    private boolean hasNt(){
        
        
        
        return false;
    }
    
     public void printMapa() {
        System.out.println("\nMAPA");
        for (Map.Entry<String, List<String>> e : mapa.entrySet()) {
            System.out.println(e.getKey() + e.getValue().toString());
        }
    }

    public void printFirst() {
        System.out.println("\nFIRST");
        first.entrySet().forEach((e) -> {
            System.out.println(e.getKey() + Arrays.toString(e.getValue().toArray()));
        });
    }
}
