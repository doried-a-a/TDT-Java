/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package work.gui.topicDetection;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.PopupMenu;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Asus
 */
public class MyDemo extends JPanel
        implements TreeSelectionListener {

    String helpText = "Click on a story to view";
    private JEditorPane htmlPane;
    private JTree tree;

    public MyDemo(MyTree t) {
        super(new GridLayout(1, 0));

        DefaultMutableTreeNode top
                = new DefaultMutableTreeNode("ROOT");
        createNodes(top, t);

        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        JScrollPane treeView = new JScrollPane(tree);
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);

        initHelp();
        JScrollPane htmlView = new JScrollPane(htmlPane);
        tree.setCellRenderer(new CountryTreeCellRenderer());
        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        add(splitPane);

    }

    class CountryTreeCellRenderer implements TreeCellRenderer {

        private JLabel label;

        CountryTreeCellRenderer() {
            label = new JLabel();
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            Object o = ((DefaultMutableTreeNode) value).getUserObject();
            if (o instanceof Leaf) {
                Leaf story = (Leaf) o;
                String test = "";
                if (story.isIsCorrect()) {
                    URL imageUrl = getClass().getResource("yes.png");
                    label.setIcon(new ImageIcon(imageUrl));
                    test = "Yes";
                } else {
                    URL imageUrl = getClass().getResource("no.png");
                    label.setIcon(new ImageIcon(imageUrl));
                    test = "NO";
                }
                label.setText(story.getName() );
            } else {
                URL imageUrl = getClass().getResource("folder.png");
                label.setIcon(new ImageIcon(imageUrl));
                label.setText("" + value);
            }
            return label;
        }
    }

    private void createNodes(DefaultMutableTreeNode top, MyTree base) {
        ArrayList<MyTree> topics;
        DefaultMutableTreeNode topic = null;
        DefaultMutableTreeNode story = null;
        topics = ((Composed) base).getContenent();
        for (MyTree tmp : topics) {
            Composed currentTopic = (Composed) tmp;
            topic = new DefaultMutableTreeNode(currentTopic.getName());
            top.add(topic);
            for (MyTree tmp1 : currentTopic.getContenent()) {
                Leaf currentStory = (Leaf) tmp1;
                story = new DefaultMutableTreeNode(currentStory);

                topic.add(story);

            }

        }

    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            if (node.getUserObject() instanceof Leaf) {
                Leaf currentStory = (Leaf) node.getUserObject();
                htmlPane.setText(currentStory.getContenent());
            }
        } else {

            initHelp();
        }
    }

    private void initHelp() {
        htmlPane.setText(helpText);
    }

}
