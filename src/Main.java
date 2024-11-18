import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame(); // Création d'une fenêtre
        WindowPanel wp = new WindowPanel(); // Création d'une instance du panel

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Faire en sorte que le programme se ferme quand on
                                                               // appuie sur la croix
        window.setTitle("Surveillance System"); // Renommer la fenêtre
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.add(wp); // Ajout du Panel a la fenêtre
        window.setVisible(true); // Faire en sorte que la fenêtre sois visible

        window.pack(); // Afficher la fenêtre
        wp.startThread(); // Commencer le thread

    }
}
