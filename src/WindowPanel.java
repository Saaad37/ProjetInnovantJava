import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class WindowPanel extends JPanel implements Runnable {

    int pressureVal;
    int prcntN2Val;
    int prcntO2Val;
    int depthVal;

    // TODO Comments and drawing graphs

    Thread windowThread;
    Random rand = new Random();
    Font font = new Font("Helvetica", Font.PLAIN, 20);
    JLabel[] texts;
    Color bgColor = new Color(24, 124, 209);
    int[] values = new int[4];
    int[][] savedValues = new int[7200][4]; // [[], [], [], ...]
    int timerIterations;

    ArrayList<Integer> pressureBound = new ArrayList<Integer>();
    ArrayList<Integer> prcntN2Bound = new ArrayList<Integer>();
    ArrayList<Integer> prcntO2Bound = new ArrayList<Integer>();
    ArrayList<Integer> depthBound = new ArrayList<Integer>();

    JLabel pressureTxt = new JLabel();
    JLabel prcntN2Txt = new JLabel();
    JLabel prcntO2Txt = new JLabel();
    JLabel depthTxt = new JLabel();
    JLabel savedText = new JLabel();

    String pressureWarning;
    String N2Warning;
    String O2Warning;
    String depthWarning;

    int fps = 60;
    long timer;
    double deltaT;
    long finalTime;
    double interval;

    public WindowPanel() {

        pressureTxt.setFont(font);
        prcntN2Txt.setFont(font);
        prcntO2Txt.setFont(font);
        depthTxt.setFont(font);
        savedText.setFont(font);

        pressureTxt.setBounds(new Rectangle(10, 100, 720, 50));
        prcntN2Txt.setBounds(new Rectangle(10, 150, 720, 50));
        prcntO2Txt.setBounds(new Rectangle(10, 200, 720, 50));
        depthTxt.setBounds(new Rectangle(10, 250, 720, 50));
        savedText.setBounds(new Rectangle(50, 400, 720, 50));

        this.setLayout(null);
        this.setPreferredSize(new Dimension(720, 600));
        this.setBackground(Color.WHITE);

        this.add(pressureTxt);
        this.add(prcntN2Txt);
        this.add(prcntO2Txt);
        this.add(depthTxt);
        this.add(savedText);
    }

    public void startThread() {
        windowThread = new Thread(this);
        windowThread.start();
    }

    public void assignValues() {
        if (timerIterations == 0) {
            pressureVal = rand.nextInt(15, 100);
            prcntN2Val = rand.nextInt(92);
            prcntO2Val = rand.nextInt(80, 100);
            depthVal = rand.nextInt(120);
        } else {
            pressureVal = rand.nextInt(Math.abs(pressureVal - 5), pressureVal + 5);
            prcntN2Val = rand.nextInt(Math.abs(prcntN2Val - 5), prcntN2Val + 5);
            prcntO2Val = rand.nextInt(Math.abs(prcntO2Val - 5), prcntO2Val + 5);
            depthVal = rand.nextInt(Math.abs(depthVal - 5), depthVal + 5);

        }
        values[0] = pressureVal;
        values[1] = prcntN2Val;
        values[2] = prcntO2Val;
        values[3] = depthVal;
    }

    public void checkValues() {
        if (pressureVal >= 50) {
            pressureWarning = "RETURN TO THE SURFACE PRESSURE TOO HIGH";
            pressureTxt.setForeground(Color.RED);
        } else if (pressureVal < 50) {
            pressureWarning = "Normal pressure, just be careful..";
            pressureTxt.setForeground(Color.BLACK);
        }
        if (prcntN2Val >= 10) {
            N2Warning = "RETURN TO THE SURFACE N2 IN BLOOD IS TOO HIGH";
            prcntN2Txt.setForeground(Color.RED);
        } else if (prcntN2Val < 10) {
            N2Warning = "Normal N2 percentages";
            prcntN2Txt.setForeground(Color.black);
        }
        if (prcntO2Val >= 92) {
            O2Warning = "Normal Saturation.";
            prcntO2Txt.setForeground(Color.black);
        } else if (prcntO2Val < 92) {
            O2Warning = "O2 SATURATION IS TOO LOW !";
            prcntO2Txt.setForeground(Color.RED);
        }
        if (depthVal >= 1 && depthVal < 58) {
            depthWarning = "You are using a normal nitrox mixture.";
            depthTxt.setForeground(new Color(98, 209, 24));
        } else if (depthVal > 58 && depthVal <= 60) {
            depthWarning = "Be careful you are switching to a trimix gaz mixture";
            depthTxt.setForeground(Color.gray);
        } else if (depthVal > 60 && depthVal < 120) {
            depthWarning = "Be careful you are not using a normal gaz mixture";
            depthTxt.setForeground(Color.RED);
        }

    }

    public void saveValues() {
        savedValues[timerIterations] = values;
    }

    @Override
    public void run() {
        interval = 1000000000 / fps;
        deltaT = 0;
        finalTime = System.nanoTime();
        long currentTime;
        timer = 0;

        while (windowThread != null) {

            if (timerIterations < 0)
                timerIterations = 0;

            currentTime = System.nanoTime();

            deltaT += (currentTime - finalTime) / interval;
            timer += currentTime - finalTime;
            finalTime = currentTime;

            if (deltaT >= 1) {
                deltaT--;
                timer++;
            }
            if (timer >= 1000000000) {
                showSavedValues();
                assignValues();
                checkValues();
                displayValues();
                saveValues();
                timerIterations++;
                timer = 0;
            }

        }
    }

    public void displayValues() {

        pressureTxt.setText(
                "<html><body><p>Pressure :" + pressureVal + " bar " + pressureWarning + " </p><br> </body></html>");
        prcntN2Txt.setText("<html><body><p>N2 :" + prcntN2Val + "% " + N2Warning + "</p><br></body></html>");
        prcntO2Txt.setText("<html><body><p>O2 :" + prcntO2Val + "% " + O2Warning + "</p><br></body></html>");
        depthTxt.setText("<html><body><p>Depth :" + depthVal + "m " + depthWarning + "</p><br></body></html>");

    }

    public void showSavedValues() {
        if (timerIterations >= 1) {
            savedText.setText("Pressure: " + savedValues[timerIterations - 1][0]
                    + " bar N2 : " + savedValues[timerIterations - 1][1]
                    + "% O2: " + savedValues[timerIterations - 1][2]
                    + "% Depth: " + savedValues[timerIterations - 1][3] + " m");
        }
    }
}
