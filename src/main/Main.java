package main;

import components.ErrorDialogBox;
import components.FileIO;
import components.Window;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if(FileIO.getOS().equalsIgnoreCase("Linux") && !(new File("/usr/SurveillanceSystem")).exists()){
            if(new File("/etc/SurveillanceSystem").mkdir()){
                System.out.println("Directory successfully created");
            }else if((new File("/usr/SurveillanceSystem").exists())) return;
               else{
                ErrorDialogBox e = new ErrorDialogBox("Permission denied: Run the program with sudo permissions.");
                e.setVisible(true);
                try {
                    e.wait(2500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
        }else if((FileIO.getOS().equalsIgnoreCase("Windows 10") ||
                FileIO.getOS().equalsIgnoreCase("Windows 11"))
        && !(new File("C:\\Program Files\\SurveillanceSystem").exists())){
            if(new File("C:\\Program Files\\SurveillanceSystem").mkdir()){
                System.out.println("Directory successfully created.");
            }else{
                ErrorDialogBox e = new ErrorDialogBox("Permission denied: Run the program with administrator");
                e.setVisible(true);
                try {
                    e.wait(2500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
            // TODO to see if there is java for macOs if so create the method to create the directory.
        }

        WindowPanel wp = new WindowPanel();
        Window window = new Window("Surveillance System");

        window.add(wp);

        window.pack(); // Affichage de la fenêtre.
    }
}
