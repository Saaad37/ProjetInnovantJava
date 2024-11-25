import javax.swing.*;
import java.awt.*;

public class GraphManager extends JFrame {

    public GraphManager(){
        this.setPreferredSize(new Dimension(500, 500));
        this.setVisible(true);
    }

    @Override
    public void paintComponents(Graphics g){
        super.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g;
    }

}
