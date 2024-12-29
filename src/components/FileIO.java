package components;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileIO {

    BufferedReader br;
    public FileIO(String filePath){

        try {
            br = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            ErrorDialogBox err = new ErrorDialogBox("Could not locate text file");
            err.setVisible(true);
        }

    }
}
