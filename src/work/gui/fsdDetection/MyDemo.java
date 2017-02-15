
package work.gui.fsdDetection;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
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
import work.FSDResultItem;

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
                
                FSDResultItem result = story.getResult();
                
                String test="";
                
                // TP
                if (result.isActualDecesion() && result.isOurDesecion()) {
                    URL imageUrl = getClass().getResource("TP.png");
                    label.setIcon(new ImageIcon(imageUrl));
                    test = "TP";
                }
                // FN
                else if(result.isActualDecesion() && result.isOurDesecion()==false){
                    URL imageUrl = getClass().getResource("FN.jpg");
                    label.setIcon(new ImageIcon(imageUrl));
                    test = "FN";
                   // System.out.println("FN : " + result.getDocument().toString());
                }
                //FP
                else if(result.isActualDecesion()==false && result.isOurDesecion()){
                    URL imageUrl = getClass().getResource("FP.png");
                    label.setIcon(new ImageIcon(imageUrl));
                    test = "FP";
                    //System.out.println("FP : " + result.getDocument().toString());
                }
                else{
                    URL imageUrl = getClass().getResource("TN.png");
                    label.setIcon(new ImageIcon(imageUrl));
                    test = "TN";
                }
                label.setText(story.getName() );
            } 
            else {
                URL imageUrl = getClass().getResource("folder.png");
                label.setIcon(new ImageIcon(imageUrl));
                label.setText("" + value);
            }
            return label;
        }
    }

    private void createNodes(DefaultMutableTreeNode top, MyTree base) {
        ArrayList<MyTree> stories = ((Composed) base).getContenent();
        DefaultMutableTreeNode story = null;
        for (MyTree tmp : stories) {
            Leaf currentStory = (Leaf) tmp;
            story = new DefaultMutableTreeNode(currentStory);
            top.add(story);
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
