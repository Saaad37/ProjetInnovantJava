package components;


import java.io.*;

public class FileIO {

    BufferedReader br;
    String username;
    String password;
    File file;
    public FileIO(String filePath){
        try {
            br = new BufferedReader(new FileReader(filePath));
            System.out.println("File Exists.");
        } catch (FileNotFoundException e) {
            file = new File(filePath);
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

    public void writeCreds(EnterCredsBox ecb){
        String username = ecb.getRoot();
        String passoword = ecb.getPassword();
        writeRoot(username);
        writePassword(passoword);

    }

    private void writeRoot(String username){
        try(FileWriter w = new FileWriter(file.getName())){
            w.write(username);
        }catch (IOException e){
            System.out.println("Cannot write in file");
        }
    }

    private void writePassword(String password){
        try(FileWriter w = new FileWriter(file.getName())){
            w.write(password);
        }catch (IOException e){
            System.out.println("Cannot write in file.");
        }
    }

    public static String getOS(){
        return System.getProperty("os.name");
    }
    public File getFile() {return file;}
    public static boolean isFileEmpty(File f){
        return !f.exists() || f.length() == 0;
    }
}
