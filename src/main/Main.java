package main;

import javax.swing.SwingUtilities;

import components.Graph;
import components.Window;

public class Main {
    public static void main(String[] args) {
        WindowPanel wp = new WindowPanel();
        Window window = new Window();
        Graph g = new Graph("t", "Pressure", wp.getSavedValues(), 1);
        Window winGraph = new Window();

        winGraph.add(g);
        window.add(wp); // Ajout du panel à la fenêtre.

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.repaint();
            }
        });

        winGraph.pack();
        window.pack(); // Affichage de la fenêtre.
    }
}
