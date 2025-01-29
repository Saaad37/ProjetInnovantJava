package main;

import components.EnterCredsBox;
import components.ErrorDialogBox;
import components.FileIO;
import components.Window;

public class Main {
    public static WindowPanel wp = new WindowPanel();

    public static void main(String[] args) {
        wp.runDB("root", Creds.password);
        startProg();
    }

    public static void startProg(){
        Window window = new Window("Surveillance System");

        window.add(wp);

        window.pack(); // Affichage de la fenêtre.
    }
}
