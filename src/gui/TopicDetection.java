
package gui;

/**
 *
 * @author Asus
 */
import work.gui.topicDetection.MyDemo;
import work.gui.topicDetection.MyTree;
import javax.swing.JFrame;
import javax.swing.UIManager;
import work.*;
public class TopicDetection {

    public TopicDetection(MyTree t) {
          
            try {
                UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        

        //Create and set up the window.
        JFrame frame = new JFrame("Topic Detection System");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Add content to the window.
        frame.add(new MyDemo(t));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        
        
        
    }
    
}
