import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WindowPanel extends JPanel implements Runnable{

    int pressureVal;
    int prcntN2Val;
    int prcntO2Val;
    int depthVal;

    /*
    Inisialisation des variables et la plus importante windowThread qui est de type Thread qui permet d'éxecuter
    un processus dans une boucle qui tourne tant que windowThread n'est pas null

    Et des listes qui permetent de stocker les valeurs précédente
    // TODO Faire un graphe qui représente les valeurs stockées
     */


    Thread windowThread;
    Random rand = new Random();
    Font font = new Font("Helvetica", Font.PLAIN,20);
    int[] values = new int[4];
    int[][] savedValues = new int[7200][values.length + 1]; // [[], [], [], ...]
    /* Création d'une liste de taille 7200 qui va s'actualiser toute les secondes
    Ce qui fait 7200sec de plongée ce qui est laargement suiffisant qui va stocké une nouvelle liste
    qui contient les valeurs actueles.
    */
    int timerIterations;


    JLabel pressureTxt = new JLabel();
    JLabel prcntN2Txt = new JLabel();
    JLabel prcntO2Txt = new JLabel();
    JLabel depthTxt = new JLabel();
    JLabel savedText = new JLabel();

    // Création des messages d'alertes.

    String pressureWarning;
    String N2Warning;
    String O2Warning;
    String depthWarning;

    int fps = 60;
    long timer;
    double deltaT;
    long finalTime;
    double interval;

    /*
    WindowPanel est la constructeur de la class WindowPanel
    qui initialise les variables plus haut.
     */

    public WindowPanel(){

        pressureTxt.setFont(font);
        prcntN2Txt.setFont(font);
        prcntO2Txt.setFont(font);
        depthTxt.setFont(font);
        savedText.setFont(font);

        pressureTxt.setBounds(new Rectangle(10, 100, 720, 50));
        prcntN2Txt.setBounds(new Rectangle(10, 150, 720, 50));
        prcntO2Txt.setBounds(new Rectangle(10, 200, 720, 50));
        depthTxt.setBounds(new Rectangle(10, 250, 720, 50));
        savedText.setBounds(new Rectangle(50, 400, 720,50));

        this.setLayout(null);
        this.setPreferredSize(new Dimension(720, 600));
        this.setBackground(Color.WHITE);

        this.add(pressureTxt);
        this.add(prcntN2Txt);
        this.add(prcntO2Txt);
        this.add(depthTxt);
        this.add(savedText);
    }

    // Fonction qui fait en sorte que windowThread ne sois pas null tant que la fenêtre n'est pas fermé

    public void startThread(){
        windowThread = new Thread(this);
        windowThread.start();
    }

    // Fonction qui actualise les valeurs et les actualisent dans la liste qui les stockes

    public void assignValues(){
        pressureVal = rand.nextInt(100);
        prcntN2Val = rand.nextInt(100);
        prcntO2Val = rand.nextInt(100);
        depthVal = rand.nextInt(120);
        
        values[0] = pressureVal;
        values[1] = prcntN2Val;
        values[2] = prcntO2Val;
        values[3] = depthVal;
    }   

    /*
    Fonction qui fait en sorte que si les valeurs sont anormales elle alerte l'utilisateur des dangers
     */

    public void checkValues(){
        if(pressureVal >= 50){
            pressureWarning = "RETURN TO THE SURFACE PRESSURE TOO HIGH";
            pressureTxt.setForeground(Color.RED);
        }else if(pressureVal < 50){
            pressureWarning = "Normal pressure, just be careful..";
            pressureTxt.setForeground(Color.BLACK);
        }
        if(prcntN2Val >= 10){
            N2Warning = "RETURN TO THE SURFACE N2 IN BLOOD IS TOO HIGH";
            prcntN2Txt.setForeground(Color.RED);
        }else if(prcntN2Val < 10){
            N2Warning = "Normal N2 percentages";
            prcntN2Txt.setForeground(Color.black);
        }
        if(prcntO2Val >= 92){
            O2Warning = "Normal Saturation.";
            prcntO2Txt.setForeground(Color.black);
        }else if(prcntO2Val < 92){
            O2Warning = "O2 SATURATION IS TOO LOW !";
            prcntO2Txt.setForeground(Color.RED);
        }
        if(depthVal >= 1 && depthVal < 58){
            depthWarning = "You are using a normal nitrox mixture.";
            depthTxt.setForeground(new Color(98, 209, 24));
        }else if(depthVal > 58 && depthVal <= 60){
            depthWarning = "Be careful you are switching to a trimix gaz mixture";
            depthTxt.setForeground(Color.gray);
        }else if(depthVal > 60 && depthVal < 120){
            depthWarning = "Be careful you are not using a normal gaz mixture";
            depthTxt.setForeground(Color.RED);
        }

    }

    // Fonction qui assimile une liste a

    public void saveValues(){
        savedValues[timerIterations] = values;
    }

    // Fonction principal qui s'éxecute lors ce qu'on commence le Thread
    @Override
    public void run() {
        interval = 1000000000/fps;
        deltaT = 0;
        finalTime = System.nanoTime();
        long currentTime;
        timer = 0;
        
        while(windowThread != null){
            // Afin d'éviter les erreurs d'IndexOutOfBound
            if(timerIterations < 0) timerIterations = 0;

            currentTime = System.nanoTime();

            deltaT += (currentTime - finalTime) / interval;
            timer += currentTime - finalTime;
            finalTime = currentTime;


            // Condition qui vérifie si DeltaT >= 1 pour ajouter 1 ns au timer
            if(deltaT >= 1)
            {
                deltaT--;
                timer++;
            }
            if(timer >= 1000000000){ // Quand le timer atteinds 10^9 ns (1s) ça va éxecuter tout le processus
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

    /*
        Assimiliation du text dans les JLabel déclaré au début de la class.
        Utilisation du html pour pouvoir faire un saut de ligne ce qui est impossible sans ce dernier
     */

    public void displayValues() {

        pressureTxt.setText("<html><body><p>Pressure :" + pressureVal + " bar " + pressureWarning + " </p><br> </body></html>");
        prcntN2Txt.setText("<html><body><p>N2 :" + prcntN2Val + "% " + N2Warning + "</p><br></body></html>");
        prcntO2Txt.setText("<html><body><p>O2 :" + prcntO2Val + "% " + O2Warning + "</p><br></body></html>");
        depthTxt.setText("<html><body><p>Depth :" + depthVal + "m " + depthWarning + "</p><br></body></html>");

        
    }


    // Afficher les valeurs de la seconde d'avant.
    public void showSavedValues(){
        if(timerIterations >= 1){
            savedText.setText("Pressure: " + savedValues[timerIterations - 1][0]
            + " bar N2 : " + savedValues[timerIterations - 1][1] 
            + "% O2: " + savedValues[timerIterations - 1][2]
            + "% Depth: " + savedValues[timerIterations - 1][3] + " m");
        }
    }
}
