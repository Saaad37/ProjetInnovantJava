package main;

import components.Window;

public class Main {
    public static void main(String[] args) {
        // GraphManager g = new GraphManager();
        WindowPanel wp = new WindowPanel();
        Window window = new Window();

        window.add(wp); // Ajout du panel à la fenêtre.

        // g.pack(); // Affichage du graphe.
        window.pack(); // Affichage de la fenêtre.
    }
}
