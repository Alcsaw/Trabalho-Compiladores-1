/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho.de.compiladores;

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
         String[] prod = {"S->cAa", "A->cB|B", "B->bcB|E|C|D", "C->c|E", "D->a|b"};
        TopDown top = new TopDown(prod);
        top.printMapa();
        top.printFirst();
    }
    
}
