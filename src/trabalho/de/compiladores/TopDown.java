package trabalho.de.compiladores;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class TopDown {

    private String[] productions;
    private Map<String, Set<String>> mapaProducoes;
    private Map<String, Set<String>> mapaFirst;
    private Map<String, Set<String>> mapaFollow;
    private Stack<String> stack;

    public TopDown(String[] productions) {
        mapaProducoes = new LinkedHashMap<>();
        mapaFirst = new HashMap<>();
        mapaFollow = new HashMap<>();

        stack = new Stack<>();
        this.productions = productions;
        createMap();
        first();
    }

    private void createMap() {
        for (String s : productions) {
            mapaProducoes.put(s.substring(0, s.indexOf("->")), new HashSet<>(Arrays.asList(s.substring(s.indexOf("->") + 2, s.length()).split("\\|"))));
        }
        printMapa();
    }

    private void first() {
        for (Map.Entry<String, Set<String>> e : mapaProducoes.entrySet()) {
            Set<String> line = new HashSet<>();

            e.getValue().forEach((s) -> {
                String symbol = s.substring(0, 1);
                System.out.println("Symbol: " + symbol);
                if (symbol.equals("E") || symbol.matches("[a-z]")) {
                    line.add(symbol);
                } else {
                    if (symbol.matches("[A-Z]")) {

                        line.addAll(findReplacement(symbol));

                    }
                }
            });
            System.out.println("Line added to FIRST: " + line.toString());
            mapaFirst.put(e.getKey(), line);
        }
    }

    public Set<String> findReplacement(String naoTerminal) {
        Set<String> newFisrt = new HashSet<>();
        System.out.println("Não Terminal: " + naoTerminal);
        if (mapaFirst.get(naoTerminal) == null) {
            
            mapaProducoes.get(naoTerminal).forEach((e) -> {
                System.out.println("e: " + e);
                String symbol = e.substring(0, 1);
                if(symbol.matches("[a-z]") || symbol.equals("E")){
                    newFisrt.add(symbol);
                }else{
                    newFisrt.addAll(findReplacement(symbol));
                }
                
            });
            mapaFirst.put(naoTerminal, newFisrt);
        }
        
        return mapaFirst.get(naoTerminal);
    }

    public void printMapa() {
        System.out.println("\nMAPA");
        for (Map.Entry<String, Set<String>> e : mapaProducoes.entrySet()) {
            System.out.println(e.getKey() + e.getValue().toString());
        }
        System.out.println("\n");
    }

    public void printFirst() {
        System.out.println("\nFIRST");
        mapaFirst.entrySet().forEach((e) -> {
            System.out.println(e.getKey() + Arrays.toString(e.getValue().toArray()));
        });
    }
}
