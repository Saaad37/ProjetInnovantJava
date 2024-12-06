package main;

import javax.swing.SwingUtilities;

import components.CG;
import components.Window;
import components.XYC;

public class Main {

    public static void main(String[] args) {
        long timer;
        long finalTime;
        long currentTime;
        double deltaT;
        double interval = 1000000000 / 60;

        WindowPanel wp = new WindowPanel();
        Window window = new Window();
        CG demo = new CG(wp.getSavedValues());

        demo.setVisible(true);
        window.add(wp);

        finalTime = System.nanoTime();
        timer = 0;
        deltaT = 0;
        while (true) {
            currentTime = System.nanoTime();
            deltaT += (currentTime - finalTime) / interval;
            timer += currentTime - finalTime;
            finalTime = currentTime;

            if (!wp.isPaused() || !wp.isStopped() && !(wp.getSavedValues().isEmpty())) {

                if (deltaT >= 1) {
                    deltaT--;
                    timer++;
                }
                if (timer >= 1000000000) {
                    if (!wp.isPaused() && wp.isStopped())
                        demo.resetAll();
                    demo.updateUI();
                    timer = 0;
                }
            }
            demo.pack();
            window.pack(); // Affichage de la fenêtre.

        }

    }
}
