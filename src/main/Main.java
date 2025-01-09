package main;

import components.ErrorDialogBox;
import components.FileIO;
import components.Window;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        WindowPanel wp = new WindowPanel();
        Window window = new Window("Surveillance System");

        String name = System.getProperty("user.name");
        if(FileIO.getOS().equalsIgnoreCase("Linux")){
            String dirPath = "/home/" + name + "/SurveillanceSystem";
            File dir = new File(dirPath);
            if(!dir.exists()){
                if(dir.mkdir()){
                    System.out.println("Directory created successfully.");
                }else{
                    ErrorDialogBox e = new ErrorDialogBox("Cannot created the file");
                    e.setVisible(true);
                    try {
                        e.wait(2500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.exit(0);
                }
            }
        }

        window.add(wp);

        window.pack(); // Affichage de la fenêtre.
    }
}
