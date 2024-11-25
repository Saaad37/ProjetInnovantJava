import javax.swing.*;

public class Window extends JFrame{

    WindowPanel wp;

    public Window(){
        wp = new WindowPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Surveillance system");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.add(wp); // Ajouter le panel à la fenêtre.
        this.setVisible(true);
    }



}
