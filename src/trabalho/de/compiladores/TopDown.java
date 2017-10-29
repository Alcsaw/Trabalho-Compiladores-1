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
        follow();
    }

    private void createMap() {
        for (String s : productions) {
            mapaProducoes.put(s.substring(0, s.indexOf("->")), new HashSet<>(Arrays.asList(s.substring(s.indexOf("->") + 2, s.length()).split("\\|"))));
        }
        printMapa();
    }

    //<editor-fold defaultstate="collapsed" desc="FIRST">
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
                        Set<String> temp = findReplacement(symbol);

                        if (temp.contains("E") && s.length() > 1) { //Se existir algo como A->XYZ, X->a|E, Y->b|c, Z->d,e
                            for (String letter : s.split("")) {
                                temp = findReplacement(letter);
                                if (temp.contains("E") == false) {
                                    break;
                                }
                            }
                        }
                        line.addAll(temp);

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
                if (symbol.matches("[a-z]") || symbol.equals("E")) {
                    newFisrt.add(symbol);
                } else {
                    newFisrt.addAll(findReplacement(symbol));
                }

            });
            mapaFirst.put(naoTerminal, newFisrt);
        }

        return mapaFirst.get(naoTerminal);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="FOLLOW">
    private void follow() {
        mapaProducoes.entrySet().forEach((e) -> { //Loop nas chaves dos mapas
            Set<String> line = new HashSet<>();
            if (e.getKey().equals("S")) {
                line.add("$");
            }
            
            mapaProducoes.entrySet().forEach((k) ->{ //Loop para achar a ocorrência da chave em outras produções
                if(k.getValue().contains(e.getKey())){
                    k.getValue().forEach((j) -> {
                        if(j.matches(".+("+ e.getKey() + ")$")){ //se exister algo como (1 ou mais simbolos)(Nao terminal analisado)
                            
                        }
                        
                        if(j.matches(".+("+ e.getKey() + "[a-z])$")){
                            
                        }
                        
                    });
                }
            });
            mapaFollow.put(e.getKey(), line);
        });

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PRINT">
    public void printMapa() {
        System.out.println("\nMAPA");
        for (Map.Entry<String, Set<String>> e : mapaProducoes.entrySet()) {
            System.out.println(e.getKey() + e.getValue().toString());
        }
    }

    public void printFirst() {
        System.out.println("\nFIRST");
        mapaFirst.entrySet().forEach((e) -> {
            System.out.println(e.getKey() + e.getValue().toString());
        });
    }

    public void printFollow() {
        System.out.println("\nFOLLOW");
        mapaFollow.entrySet().forEach((e) -> {
            System.out.println(e.getKey() + e.getValue().toString());
        });
    }

    //</editor-fold>
}
