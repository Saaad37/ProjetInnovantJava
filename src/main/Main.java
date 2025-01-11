package main;

import components.EnterCredsBox;
import components.ErrorDialogBox;
import components.FileIO;
import components.Window;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String name = System.getProperty("user.name");
        if (FileIO.getOS().equalsIgnoreCase("Linux")) {
            String dirPath = "/home/" + name + "/SurveillanceSystem";
            File dir = new File(dirPath);
            if (!dir.exists()) {
                if (dir.mkdir()) {
                    FileIO creds = new FileIO(dirPath + "/creds.txt");
                    WriteAndSave(creds);
                }
            } else {
                FileIO creds = new FileIO(dirPath + "/creds.txt");
                if (!creds.getFile().exists()) {
                    WriteAndSave(creds);
                }
            }
        } else if (FileIO.getOS().equalsIgnoreCase("Windows 10") ||
                FileIO.getOS().equalsIgnoreCase("Windows 11")) {
            String dirPath = "C:\\Users\\" + name + "\\SurveillanceSystem";
            File dir = new File(dirPath);
            if (!dir.exists()) {
                if (dir.mkdir()) {
                    System.out.println("Directory created successfully.");
                } else {
                    ErrorDialogBox e = new ErrorDialogBox("Cannot create the directory");
                    e.setVisible(true);
                    try {
                        e.wait(2500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        }
        // TODO Mac OS X condition.

    }

    private static void WriteAndSave(FileIO creds) {
        if (creds.getFile().exists()) {
            System.out.println("Directory created successfully.");
            EnterCredsBox ecb = new EnterCredsBox(creds);
            ecb.setVisible(true);

            startProg();
        } else {
            ErrorDialogBox e = new ErrorDialogBox("Cannot created the directory");
            e.setVisible(true);
            try {
                e.wait(2500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    }

    private static void startProg(){
        WindowPanel wp = new WindowPanel();
        Window window = new Window("Surveillance System");

        window.add(wp);

        window.pack(); // Affichage de la fenêtre.
    }
}
