package main;

import components.Window;

public class Main {
    public static void main(String[] args) {
        WindowPanel wp = new WindowPanel();
        Window window = new Window("Surveillance System");

        window.add(wp);

        window.pack(); // Affichage de la fenêtre.
    }
}
