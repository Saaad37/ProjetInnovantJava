package components;


import java.io.*;

public class FileIO {

    BufferedReader br;
    String username;
    String password;
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

    public void readCreds(){
        String line;
        int lineCount = 0;
        do {
            try {
                if ((line = br.readLine()) == null) break;
                if (lineCount == 0) {
                    this.username = line;
                    System.out.println("username: " + username);
                } else if (lineCount == 1) {
                    this.password = line;
                    System.out.println("password: " + password);
                }
                lineCount++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }

    private static String getOS(){
        return System.getProperty("os.name");
    }
}
