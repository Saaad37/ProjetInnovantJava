import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame(); // Initialisation de la fenêtre
        WindowPanel wp = new WindowPanel(); // Création d'une instance du panel


        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Surveillance System");
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.add(wp); // Ajouter le panel à la fenêtre.
        window.setVisible(true);

        window.pack(); // Affichage de la fenêtre.
        wp.startThread(); // Faire en sorte que le Thread ne sois pas nul et donc lance la boucle

    }
}
