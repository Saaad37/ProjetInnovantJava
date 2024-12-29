package components;


import java.io.*;

public class FileIO {

    BufferedReader br;
    public FileIO(String filePath){
        try {
            br = new BufferedReader(new FileReader(filePath));
            System.out.println("File Exists.");
        } catch (FileNotFoundException e) {
            File file = new File(filePath);
            try {
                if(file.createNewFile()){
                    System.out.println("File Created " + file.getName());
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
