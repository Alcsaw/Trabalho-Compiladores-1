/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


/**
 *
 * @author m95952
 */
public class TrabalhoDeCompiladores {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //String[] prod = {"S->cAa|D", "A->cB|B|F", "B->bcB|E|C", "C->c|E", "D->f|g", "F->CD"};
        String[] prod = {"S->cAa", "A->cB|B", "B->bcB|E"};
        //String[] prod = {"S->3A1", "A->3B|B", "B->23B|E"};
        //String[] prod = {"S->XYZ", "X->aXb|E", "Y->cYZcX|d", "Z->eZYe|f"};
        
        String[] mensagem = {"c","b","c","a","$"};
        //String[] mensagem = {"3","2","3","1","$"};
        TopDown top = new TopDown(prod, mensagem);
        top.printMapa();
        top.printFirst();
        top.printFollow();
        top.printTerminais();
        top.printTabela();
        top.printEntrada();
    }
    
}
