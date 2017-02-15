/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import files.DocumentDictionary;
import files.StoryFile;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import tfidf.IIDFProvider;
import tfidf.IdfExternalProvider;
import tfidf.TfidfVectorSpaceDocumentRepresentation;
import work.TDTJava;

/**
 *
 * @author doried
 */
public class TopicTrackingGui extends javax.swing.JFrame {

    private List<TfidfVectorSpaceDocumentRepresentation> docs;
    private DocumentDictionary docDic;
    List<TfidfVectorSpaceDocumentRepresentation> result = null;
    private IdfExternalProvider idfProvider;
    private int currentStory=-1;
    List<TfidfVectorSpaceDocumentRepresentation> trainingStories = new ArrayList<TfidfVectorSpaceDocumentRepresentation>();
    
    public TopicTrackingGui(List<TfidfVectorSpaceDocumentRepresentation> docs, DocumentDictionary docDic,IdfExternalProvider idfProvider) {
        initComponents();
        this.docs = docs;
        this.docDic = docDic;
        this.idfProvider = idfProvider;
        
        DefaultListModel listModel = new DefaultListModel();
        lstInterest.setModel(listModel);
        
        cboStories.removeAllItems();
        lstInterest.removeAll();
        
        for(TfidfVectorSpaceDocumentRepresentation doc : docs){
            cboStories.addItem(doc);
        }
        
        lstInterest.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                try{
                    TfidfVectorSpaceDocumentRepresentation selected = 
                            (TfidfVectorSpaceDocumentRepresentation) lstInterest.getSelectedValue();
                    txtInterestStory.setText(docDic.getStoryFileByUrl(selected.getStoryFile().getStoryUrl()).getStoryContent());
                }
                catch(Exception ee){
                    System.err.println(ee.toString());
                }
            }
        });
         
    }

    
    
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboStories = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtDecision = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtInterestStory = new javax.swing.JTextArea();
        btnAddStory = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstInterest = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtText = new javax.swing.JTextArea();
        btnTrack = new javax.swing.JButton();
        txtTitle = new javax.swing.JTextField();
        txtNextStory = new javax.swing.JButton();
        txtPrevStory = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cboStories.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("add story");

        jLabel2.setText("Stories of interest:");

        jLabel3.setText("Decision");

        txtInterestStory.setColumns(20);
        txtInterestStory.setLineWrap(true);
        txtInterestStory.setRows(5);
        jScrollPane1.setViewportView(txtInterestStory);

        btnAddStory.setText("add");
        btnAddStory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStoryActionPerformed(evt);
            }
        });

        lstInterest.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstInterest);

        txtText.setColumns(20);
        txtText.setLineWrap(true);
        txtText.setRows(5);
        jScrollPane3.setViewportView(txtText);

        btnTrack.setText("Track!");
        btnTrack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrackActionPerformed(evt);
            }
        });

        txtNextStory.setText("next story");
        txtNextStory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNextStoryActionPerformed(evt);
            }
        });

        txtPrevStory.setText("prev story");
        txtPrevStory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrevStoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addComponent(txtTitle)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(cboStories, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddStory, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTrack, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDecision, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrevStory, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNextStory, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboStories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btnAddStory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTrack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDecision, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNextStory)
                    .addComponent(txtPrevStory))
                .addGap(18, 18, 18))
        );

        jLabel3.getAccessibleContext().setAccessibleDescription("Scaled Similarity is ");

        getAccessibleContext().setAccessibleName("Topic Tracking Task");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddStoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStoryActionPerformed
        try{
            TfidfVectorSpaceDocumentRepresentation selected = (TfidfVectorSpaceDocumentRepresentation)
                cboStories.getSelectedItem();
            
            DefaultListModel model = (DefaultListModel) lstInterest.getModel();
            if(model.contains(selected))
                return;
            model.addElement(selected);
            trainingStories.add(selected);
            
        }
        catch(Exception ee){
            System.err.println(ee.toString());
        }
    }//GEN-LAST:event_btnAddStoryActionPerformed

    private void btnTrackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrackActionPerformed
        try {
            List<TfidfVectorSpaceDocumentRepresentation> training = new ArrayList<>();
            result = TDTJava.getStoriesOnTopic(docs, trainingStories,idfProvider);
            currentStory = 0;
            seeNextStory();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }//GEN-LAST:event_btnTrackActionPerformed

    private void txtNextStoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNextStoryActionPerformed
        currentStory++;
        seeNextStory();
    }//GEN-LAST:event_txtNextStoryActionPerformed

    private void txtPrevStoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrevStoryActionPerformed
        currentStory--;
        seeNextStory();
    }//GEN-LAST:event_txtPrevStoryActionPerformed

    private void seeNextStory(){
        if(result == null || currentStory <0 || currentStory>=docs.size()){
            System.out.println("End!");
            txtDecision.setText("End!");
            return;
        }
        
        TfidfVectorSpaceDocumentRepresentation doc = docs.get(currentStory);
        StoryFile file = docDic.getStoryFileByUrl(doc.getStoryFile().getStoryUrl());
        txtTitle.setText(doc.getStoryFile().toString());
        txtText.setText(file.getStoryContent());
        if(result.contains(doc))
            txtDecision.setText("Yes!");
        else
            txtDecision.setText("No");
        
    }
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddStory;
    private javax.swing.JButton btnTrack;
    private javax.swing.JComboBox cboStories;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList lstInterest;
    private javax.swing.JTextField txtDecision;
    private javax.swing.JTextArea txtInterestStory;
    private javax.swing.JButton txtNextStory;
    private javax.swing.JButton txtPrevStory;
    private javax.swing.JTextArea txtText;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables
}
