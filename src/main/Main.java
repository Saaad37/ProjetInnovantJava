package main;

import javax.swing.SwingUtilities;

import components.Window;

public class Main {
    public static void main(String[] args) {
        WindowPanel wp = new WindowPanel();
        Window window = new Window();

        window.add(wp);

        window.pack(); // Affichage de la fenêtre.
    }
}
