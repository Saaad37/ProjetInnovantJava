package main;

import components.ErrorDialogBox;
import components.FileIO;
import components.Window;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        WindowPanel wp = new WindowPanel();
        Window window = new Window("Surveillance System");

        window.add(wp);

        window.pack(); // Affichage de la fenêtre.
    }
}
