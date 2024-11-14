import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WindowPanel extends JPanel implements Runnable{

    Thread windowThread;
    Random rand = new Random();
    Font font = new Font("Helvetica", Font.PLAIN,20);
    JLabel[] texts;

    int pressureVal;
    int prcntN2Val;
    int prcntO2Val;
    int depthVal;

    JLabel pressureTxt = new JLabel();
    JLabel prcntN2Txt = new JLabel();
    JLabel prcntO2Txt = new JLabel();
    JLabel depthTxt = new JLabel();

    JLabel txt = new JLabel();

    int fps = 60;
    long timer;
    double deltaT;
    long finalTime;
    double interval;

    public WindowPanel(){

        pressureTxt.setFont(font);
        prcntN2Txt.setFont(font);
        prcntO2Txt.setFont(font);
        depthTxt.setFont(font);

        pressureTxt.setBounds(new Rectangle(10, 100, 400, 20));
        prcntN2Txt.setBounds(new Rectangle(10, 140, 400, 50));
        prcntO2Txt.setBounds(new Rectangle(10, 200, 400, 50));
        depthTxt.setBounds(new Rectangle(10, 250, 400, 50));

        this.setLayout(null);
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

    public void assignValues(){
        pressureVal = rand.nextInt(100);
        prcntN2Val = rand.nextInt(100);
        prcntO2Val = rand.nextInt(100);
        depthVal = rand.nextInt(120);
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
                setValues();
                timer = 0;
            }

        }
    }

    public void setValues() {

        pressureTxt.setText("<html><body>Pressure :" + pressureVal + " bar<br> </body></html>");
        prcntN2Txt.setText("<html><body>N2 :" + prcntN2Val + "%<br> </body></html>");
        prcntO2Txt.setText("<html><body>O2 :" + prcntO2Val + "%<br></body></html>");
        depthTxt.setText("<html><body>Depth :" + depthVal + "m<br></body></html>");

    }


}
