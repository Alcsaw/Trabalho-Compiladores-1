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
        mapaFollow = new LinkedHashMap<>();

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
        System.out.println("\n");
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
            line.addAll(findFollow(e.getKey()));

            mapaFollow.put(e.getKey(), line);
        });

    }

    private Set<String> findFollow(String NaoTerminal) {
        Set<String> newfollow = new HashSet<>();
        System.out.println("---Var: " + NaoTerminal);
        if (mapaFollow.get(NaoTerminal) == null) {
            System.out.println("Nãp está no mapa");
            mapaProducoes.entrySet().forEach((k) -> { //Loop para achar a ocorrência da chave em outras produções

                if (containsNaoTerminal(k.getValue(), NaoTerminal)) {
                    System.out.println("Key: " + k.getKey() + " contains " + NaoTerminal);
                    k.getValue().forEach((j) -> {
                        System.out.println("K: " + j);

                        if (j.matches(".*(" + NaoTerminal + ")$")) { //se exister algo como [1 ou mais simbolos][Nao terminal analisado]
                            System.out.println("Matches .*(" + NaoTerminal + ")$");
                            System.out.println("Key: " + k.getKey() + " Nao terminal: " + NaoTerminal);
                            if (!NaoTerminal.equals(k.getKey())) {
                                if (mapaFollow.get(k.getKey()) == null) { //Se nao existir o follow do Simbolo a esqueda da produção então procura ele
                                    System.out.println("Find follow: " + k.getKey());
                                    newfollow.addAll(findFollow(k.getKey()));
                                } else {
                                    newfollow.addAll(mapaFollow.get(k.getKey()));
                                }
                            }
                        }

                        if (j.matches(".*(" + NaoTerminal + "[a-z]+)$")) { //se exister algo como [0 ou mais simbolos][Nao terminal analisado][Terminal]
                            System.out.println("Matches .*(" + NaoTerminal + "[a-z])$");
                            newfollow.add(j.substring(j.indexOf(NaoTerminal) + 1, j.indexOf(NaoTerminal) + 2));
                        }

                        if (j.matches(".*(" + NaoTerminal + "[A-Z]+[a-zA-Z]*)$")) {
                            System.out.println("Matches .*(" + NaoTerminal +"[A-Z])$");
                            newfollow.addAll(mapaFirst.get(j.substring(j.indexOf(NaoTerminal) + 1, j.indexOf(NaoTerminal) + 2)));
                        }

                    });
                }
            });
            System.out.println("Line added to FOLLOW: " + newfollow.toString());
            mapaFollow.put(NaoTerminal, newfollow);
        }

        return mapaFollow.get(NaoTerminal);
    }

    private boolean containsNaoTerminal(Set<String> set, String NaoTerminal) {
        if (set.stream().anyMatch((s) -> (s.contains(NaoTerminal)))) {
            return true;
        }
        return false;
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
