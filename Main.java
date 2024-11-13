import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        WindowPanel wp = new WindowPanel();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Surveillance System");
        window.setLocationRelativeTo(null);
        window.add(wp);
        window.setVisible(true);


        window.pack();
        wp.startThread();


    }
}
