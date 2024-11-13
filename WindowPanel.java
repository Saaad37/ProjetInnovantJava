import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WindowPanel extends JPanel implements Runnable{

    Thread windowThread;
    Random rand = new Random();
    int pressureVal;
    int prcntN2Val;
    int prcntO2Val;
    int depthVal;

    JLabel pressureTxt = new JLabel();
    JLabel prcntN2Txt = new JLabel();
    JLabel prcntO2Txt = new JLabel();
    JLabel depthTxt = new JLabel();

    int[] values;

    int fps = 60;
    long timer;
    double deltaT;
    long finalTime;
    double interval;

    public WindowPanel(){

        pressureTxt.setVerticalTextPosition(JLabel.CENTER);
        prcntO2Txt.setVerticalTextPosition(JLabel.CENTER);
        prcntN2Txt.setVerticalTextPosition(JLabel.CENTER);
        depthTxt.setVerticalTextPosition(JLabel.CENTER);


        this.setPreferredSize(new Dimension(720, 600));
        this.add(pressureTxt);
        this.add(prcntN2Txt);
        this.add(prcntO2Txt);
        this.add(depthTxt);

    }

    public void startThread(){
        windowThread = new Thread(this);
        windowThread.start();
    }

    public int[] assignValues(){
        pressureVal = rand.nextInt(100);
        prcntN2Val = rand.nextInt(100);
        prcntO2Val = rand.nextInt(100);
        depthVal = rand.nextInt(120);
        return new int[] {pressureVal, prcntN2Val, prcntO2Val, depthVal};
    }

    @Override
    public void run() {
            interval = 1000000000/fps;
            deltaT = 0;
            finalTime = System.nanoTime();
            long currentTime;
            timer = 0;

        while(windowThread != null){

            currentTime = System.nanoTime();

            deltaT += (currentTime - finalTime) / interval;
            timer += currentTime - finalTime;
            finalTime = currentTime;


            if(deltaT >= 1)
            {
                assignValues();
                deltaT--;
                timer++;
            }
            if(timer >= 1000000000){
                showValuesInWindow();
                timer = 0;
            }

        }
    }

    public void showValuesInWindow() {

        pressureTxt.setText("Pressure " + Integer.toString(pressureVal) + " bar");
        prcntN2Txt.setText("N2 " + Integer.toString(prcntN2Val) + "%");
        prcntO2Txt.setText("O2 " + Integer.toString(prcntO2Val) + "%");
        depthTxt.setText("depth " + Integer.toString(depthVal) + "m");
        JLabel[] texts = new JLabel[] {pressureTxt, prcntN2Txt, prcntO2Txt, depthTxt};


    }


}
