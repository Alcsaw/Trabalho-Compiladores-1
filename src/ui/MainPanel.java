package ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import util.TopDown;

/**
 *
 * @author m95952
 */
public class MainPanel extends javax.swing.JPanel {

    private String[] input = null;

    public MainPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        campoFilePath = new javax.swing.JTextField();
        botaoFindFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        campoConteudo = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        botaoAnalisar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        campoMensagem = new javax.swing.JTextField();

        jLabel1.setText("Arquivo");

        botaoFindFile.setText("Procurar");
        botaoFindFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoFindFileActionPerformed(evt);
            }
        });

        campoConteudo.setColumns(20);
        campoConteudo.setRows(5);
        jScrollPane1.setViewportView(campoConteudo);

        jLabel2.setText("Conteúdo");

        botaoAnalisar.setText("Analisar");
        botaoAnalisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAnalisarActionPerformed(evt);
            }
        });

        jLabel3.setText("Mensagem de entrada");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botaoAnalisar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(campoMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(campoFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(botaoFindFile)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botaoAnalisar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campoFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botaoFindFile))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botaoFindFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoFindFileActionPerformed
        // TODO add your handling code here:
        JFileChooser jf = new JFileChooser();
        jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jf.showOpenDialog(null);

        File file = jf.getSelectedFile();
        if (file != null) {
            campoFilePath.setText(file.getName());
            try {
                byte[] byteArray = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                String content = new String(byteArray, "UTF-8");
                input = content.split("\\r?\\n");
                campoConteudo.setText(content);
                campoConteudo.append("\n");
                campoConteudo.append(Arrays.toString(input));
            } catch (IOException ex) {
                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_botaoFindFileActionPerformed

    private void botaoAnalisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAnalisarActionPerformed
        // TODO add your handling code here:
        try {
            validar();
            TopDown top = new TopDown(input, campoMensagem.getText().split(""));
            top.printMapa();
            top.printFirst();
            top.printFollow();
            top.printTerminais();
            top.printTabela();
            top.printEntrada();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_botaoAnalisarActionPerformed

    private void validar() throws Exception {
        if(input == null){
            throw new Exception("Insira um arquivo de entrada.");
        }
        
        if (campoMensagem.getText().isEmpty()) {
            throw new Exception("Insira a mensagem!");
        }

        if (!campoMensagem.getText().matches(".*(\\$)$")) {
            throw new Exception("A mensagem deve acabar com $.");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoAnalisar;
    private javax.swing.JButton botaoFindFile;
    private javax.swing.JTextArea campoConteudo;
    private javax.swing.JTextField campoFilePath;
    private javax.swing.JTextField campoMensagem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
