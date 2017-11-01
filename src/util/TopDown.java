package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class TopDown {

    private String[] productions;
    private Map<String, Set<String>> mapaProducoes;
    private Map<String, Set<String>> mapaFirst;
    private Map<String, Set<String>> mapaFollow;

    private Stack<String> pilha;
    private Stack<String> pilhaEntrada;
    private List<String> entrada;

    private Set<String> terminais;
    private Map<String, Map<String, String>> tabela;

    public TopDown(String[] productions, String[] mensagem) {
        mapaProducoes = new LinkedHashMap<>();
        mapaFirst = new HashMap<>();
        mapaFollow = new LinkedHashMap<>();
        terminais = new HashSet<>();
        tabela = new HashMap<>();

        pilha = new Stack<>();
        pilhaEntrada = new Stack<>();
        entrada = new ArrayList<>(Arrays.asList(mensagem));
        System.out.println(entrada.toString());
        this.productions = productions;
        System.out.println(Arrays.toString(productions));
        createMap();
        first();
        follow();
        findTerminals();
        createTable();
        setStack();
        reconhecer();
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
                if (symbol.equals("E") || symbol.matches("[a-z]") || symbol.matches("\\d")) {
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
                        if (line.isEmpty()){
                            System.out.println("ERROR: Couldn't recognize the entered Grammar."
                                    + "\nThere's no FIRST related with production " + symbol);
                            System.exit(1);
                        }
                    }
                    else{
                        System.out.println("ERROR: Couldn't recognize the entered Grammar."
                                + "\nProduction symbol (" + symbol + ") is not acceptable.");
                        System.exit(1);
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
                if (symbol.matches("[a-z]") || symbol.equals("E") || symbol.matches("\\d")) {
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
        mapaProducoes.entrySet().forEach((e) -> {   //Loop nas chaves dos mapas
            String currentKey = e.getKey();
            Set<String> line = new HashSet<>();
            if (currentKey.equals("S")) {           //What if S is not the beginning symbol? 
                line.add("$");
            }
            line.addAll(findFollow(currentKey));

            mapaFollow.put(currentKey, line);
        });

    }

    private Set<String> findFollow(String NaoTerminal) {
        Set<String> newfollow = new HashSet<>();
        System.out.println("---Var: " + NaoTerminal);
        if (mapaFollow.get(NaoTerminal) == null) {
            System.out.println("Não está no mapa");
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
                            System.out.println("Matches .*(" + NaoTerminal + "[A-Z])$");
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
        return set.stream().anyMatch((s) -> (s.contains(NaoTerminal)));
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MONATAGEM TABELA">
    private void findTerminals() {
        mapaProducoes.entrySet().forEach((e) -> {
            e.getValue().forEach((f) -> {
                String[] split = f.split("");
                for (String s : split) {
                    if (s.matches("^(.*([a-z]|\\$|E))")) {
                        terminais.add(s);
                    }
                }

            });
        });
        terminais.add("$");
    }

    private void createTable() {
        System.out.println("\n=== CRIAR TABELA ===");
        mapaProducoes.entrySet().forEach((e) -> {
            System.out.println("\nNova linha: " + e.getValue().toString());
            Map<String, String> mapa = new HashMap<>();

            e.getValue().forEach((f) -> {
                Set<String> set = getFirst(e.getKey(), f);
                System.out.println("Set: " + set.toString() + "   ");

                set.forEach((g) -> {
                    System.out.println("Coluna: " + g + " Valor: " + f);
                    mapa.put(g, f);
                });
            });
            tabela.put(e.getKey(), mapa);
        });
    }

    private Set<String> getFirst(String NaoTerminal, String prod) {
        Set<String> line = new HashSet<>();

        System.out.println("Prod: " + prod);
        if (prod.matches("^([a-z][a-zA-Z]*)")) {
            System.out.println("inicia com terminal");
            line.add(prod.substring(0, 1));
            return line;
        } else {
            if (prod.matches("^([A-Z][a-zA-Z]*)") && !prod.equals("E")) {
                System.out.println("Inicia com Não terminal");
                line.addAll(mapaFirst.get(prod.substring(0, 1)));
                return line;
            }

            if (prod.equals("E")) {
                System.out.println("E encontrado, seta follow de " + NaoTerminal);
                return mapaFollow.get(NaoTerminal);
            }
            return line;
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RECONHECIMENTO">
    private void setStack() {
        Collections.reverse(entrada);
        pilhaEntrada.addAll(entrada);
        //for (String s : entrada) {
            ///pilhaEntrada.add(s);
        //}
    }

    private void reconhecer() {
        System.out.println("\n=== RECONHECIMENTO ===");
        pilha.add("$");
        pilha.add("S");

        while (!pilha.isEmpty()) {
            System.out.println("\nPilha: " + pilha);
            System.out.println("Entrada: " + pilhaEntrada);

            if (pilha.lastElement().equals(pilhaEntrada.lastElement()) && pilhaEntrada.lastElement().matches("^([a-z]|\\$)$")) {
                pilhaEntrada.pop();
                System.out.println("Desempilha: " + pilha.pop());
            } else {
                if (pilha.lastElement().matches("[A-Z]") && !pilha.lastElement().equals("E")) {
                    String elemento = pilha.pop();
                    if (tabela.get(elemento).get(pilhaEntrada.lastElement()) == null) {
                        throw new RuntimeException("Mensagem não reconhecida");
                    } else {
                        String[] prod = tabela.get(elemento).get(pilhaEntrada.lastElement()).split("");
                        System.out.println("Empilha: " + Arrays.toString(prod));
                        List<String> p = Arrays.asList(prod);
                        Collections.reverse(p);
                        pilha.addAll(p);
                        //for (String s : p) {
                            //pilha.add(s);
                        //}
                    }

                } else {
                    if (pilha.lastElement().equals("E")) {
                        System.out.println("Desempilha palavra vazia.");
                        pilha.pop();
                    }
                }
            }
        }
        if (pilha.isEmpty() && pilhaEntrada.isEmpty()) {
            System.out.println("MENSAGEM RECONHECIDA !!!");
        }

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PRINT">
    public void printMapa() {
        System.out.println("\n=== MAPA ===");
        for (Map.Entry<String, Set<String>> e : mapaProducoes.entrySet()) {
            System.out.println(e.getKey() + e.getValue().toString());
        }
    }

    public void printFirst() {
        System.out.println("\n=== FIRST ===");
        mapaFirst.entrySet().forEach((e) -> {
            System.out.println(e.getKey() + e.getValue().toString());
        });
    }

    public void printFollow() {
        System.out.println("\n=== FOLLOW ===");
        mapaFollow.entrySet().forEach((e) -> {
            System.out.println(e.getKey() + e.getValue().toString());
        });
    }

    public void printTerminais() {
        System.out.println("\n=== TERMINAIS ===");
        System.out.println(terminais.toString());
    }

    public void printTabela() {
        System.out.println("\n=== TABELA ===");
        tabela.entrySet().forEach((e) -> {
            e.getValue().entrySet().forEach((f) -> {
                System.out.print(f.getKey() + " " + f.getValue() + "  ");

            });
            System.out.print("\n");
        });
    }

    public void printEntrada() {
        System.out.println("\n=== PILHA ENTRADA ===");
        System.out.println("Entrada: " + entrada);
        System.out.println("Pilha Entrada: " + pilhaEntrada);
        System.out.println("Pilha Simbolos: " + pilha);
    }

    //</editor-fold>
}
