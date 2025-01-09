package main;

import components.EnterCredsBox;
import components.ErrorDialogBox;
import components.FileIO;
import components.Window;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String name = System.getProperty("user.name");
        if(FileIO.getOS().equalsIgnoreCase("Linux")){
            String dirPath = "/home/" + name + "/SurveillanceSystem";
            File dir = new File(dirPath);
            if(!dir.exists()){
                if(dir.mkdir()){
                    System.out.println("Directory created successfully.");
                    EnterCredsBox ecb = new EnterCredsBox();
                    ecb.setVisible(true);
                    FileIO creds = new FileIO(dirPath + "/creds.txt");
                    while(ecb.isbClicked()){
                        creds.writeCreds(ecb);
                    }

                }else{
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
        }else if(FileIO.getOS().equalsIgnoreCase("Windows 10") ||
                FileIO.getOS().equalsIgnoreCase("Windows 11")){
                String dirPath = "C:\\Users\\" + name + "\\SurveillanceSystem";
                File dir = new File(dirPath);
                if(!dir.exists()){
                    if(dir.mkdir()){
                        System.out.println("Directory created successfully.");
                    }else{
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

    private void startProg(){
        WindowPanel wp = new WindowPanel();
        Window window = new Window("Surveillance System");

        window.add(wp);

        window.pack(); // Affichage de la fenêtre.
    }
}
