package main;

import javax.swing.SwingUtilities;

import components.CG;
import components.Window;

public class Main {
    public static void main(String[] args) {
        WindowPanel wp = new WindowPanel();
        Window window = new Window();
        Window winGraph = new Window();
        CG g = new CG(wp.getSavedValues(), 1, winGraph);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.updateDataset();
            }
        });

        winGraph.add(g);
        winGraph.setResizable(true);
        window.add(wp);

        winGraph.pack();
        window.pack(); // Affichage de la fenêtre.
    }
}
