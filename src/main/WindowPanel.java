package main;

import javax.swing.*;

import components.Button;
import components.SoundSystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class WindowPanel extends JPanel implements Runnable {

    /*
     * Initialisation des variables de valeurs de pression, Pression partielle de
     * N2, pression partielle de O2,
     * et profondeur.
     */

    final double XO2 = 0.2f; // %
    final double XN2 = 0.8f; // %
    final double P0 = 1.013f; // bar
    final int P0Pasc = 101325;
    final int rhoSaltedWater = 1025; // kg/m3
    final double g = 9.81f; // m/s^2 ou N/Kg
    final String alarmPath = "/assets/alarm.wav";

    double pressureVal;
    double pressureValPasc;
    double PaN2Val;
    double PaO2Val;
    double depthVal;

    int frameCounter;

    // TODO Faire un graphe des valeurs depuis le debut du programme

    /*
     * Initialisation des variables.
     */

    double maxValN2 = (4 * XN2) * 750;

    SoundSystem soundSys = new SoundSystem(alarmPath);

    Button startButton = new Button(new Rectangle(75, 500, 80, 35), "Start");
    Button stopButton = new Button(new Rectangle(275, 500, 80, 35), "Stop");
    Button pauseButton = new Button(new Rectangle(475, 500, 80, 35), "Pause");

    Thread windowThread; // Initialisation du thread, qui va repeter un processus indéfiniment.
    Random rand = new Random(); // Initialisation d'une instance de Random qui va permettre de choisir des
                                // valeurs au hasard
    Font font = new Font("Helvetica", Font.PLAIN, 20); // Création d'une nouvelle police.
    Double[] values = new Double[4]; // Création d'une liste qui va contenir les valeurs
    ArrayList<Double[]> savedValues = new ArrayList<Double[]>(); // Création d'une liste qui contient une liste, Elle va
                                                                 // sauvegarder les
                                                                 // valeurs
    // la liste savedValues va ressembler à ça [[], [], [], ...]

    int timerIterations; // Création d'une variable qui va s'incrementer de un chaque seconde et sera
    int minPassed;
    // l'index des valeurs sauvegardé
    Color bgColor = new Color(34, 48, 97);
    Color defaultColor = Color.WHITE;
    ImageIcon subroticIcon = new ImageIcon(
            Objects.requireNonNull(this.getClass().getResource("/assets/subrotic.jpg")));

    /*
     * Initialisation des texte qui vont apparaître sur la fenêtre
     */

    JLabel pressureTxt = new JLabel();
    JLabel PaN2Txt = new JLabel();
    JLabel PaO2Txt = new JLabel();
    JLabel depthTxt = new JLabel();
    JLabel savedText = new JLabel();
    JLabel subroticLogo = new JLabel();
    JLabel timerTxt = new JLabel();

    String N2Warning;
    String depthWarning;
    String timerString;

    // Variable qui servent a savoir quand une seconde passe

    int fps = 60;
    long timer;
    double deltaT;
    long finalTime;
    double interval;

    public WindowPanel() { // Constructeur.

        // Appliquer la police a tous les textes

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (windowThread == null) {
                    startThread();
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopThread();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopThread();
                resetValues();
            }
        });

        frameCounter = 0;

        pressureTxt.setFont(font);
        PaN2Txt.setFont(font);
        PaO2Txt.setFont(font);
        depthTxt.setFont(font);
        savedText.setFont(font);
        timerTxt.setFont(font);
        subroticLogo.setIcon(subroticIcon);

        pressureTxt.setForeground(defaultColor);
        PaN2Txt.setForeground(defaultColor);
        PaO2Txt.setForeground(defaultColor);
        depthTxt.setForeground(defaultColor);
        timerTxt.setForeground(defaultColor);
        savedText.setForeground(defaultColor);

        // Donner un rectangle comme limite de textes
        subroticLogo.setBounds(new Rectangle(20, 20, 125, 125));
        PaN2Txt.setBounds(new Rectangle(50, 150, 720, 50));
        PaO2Txt.setBounds(new Rectangle(50, 200, 720, 50));
        depthTxt.setBounds(new Rectangle(50, 250, 720, 50));
        pressureTxt.setBounds(new Rectangle(50, 300, 720, 50));
        savedText.setBounds(new Rectangle(10, 425, 720, 50));
        timerTxt.setBounds(new Rectangle(600, 10, 100, 50));

        subroticIcon.setImage(
                resizeImage(subroticIcon, subroticLogo.getBounds().width, subroticLogo.getBounds().height).getImage());

        // this veut dire cette classe JPanel

        this.setLayout(null);
        this.setPreferredSize(new Dimension(720, 600));
        this.setBackground(bgColor);

        this.add(subroticLogo);
        this.add(pressureTxt);
        this.add(PaN2Txt);
        this.add(PaO2Txt);
        this.add(depthTxt);
        this.add(savedText);
        this.add(timerTxt);
        this.add(startButton);
        this.add(pauseButton);
        this.add(stopButton);
    }

    // Commencer le thread

    private void startThread() {
        windowThread = new Thread(this);
        windowThread.start();
    }

    private void stopThread() {
        windowThread = null;
        soundSys.stopSound();
    }

    // Au début du programme il va choisir une valeur aléatoire puis va choisir des
    // variables au alentour de la valeur choisis précedemment

    private void resetValues() {
        depthVal = 0;
        savedValues.clear();
        Arrays.fill(values, (double) 0);
        timerIterations = 0;
        minPassed = 0;
        timer = 0;
        checkValues();
        displayValues();
        displayTimer();
        System.out.println(savedValues.toString());
    }

    private void assignValues() {
        pressureValPasc = P0Pasc + (rhoSaltedWater * g * depthVal); // Pa
        pressureVal = pressureValPasc * Math.pow(10, -5); // bar
        PaN2Val = (pressureValPasc / 133) * XN2; // mmHg
        PaO2Val = (pressureValPasc / 133) * XO2; // mmHg
        depthVal++;

        // Assigner ces valeurs à la liste des valeurs

        values[0] = depthVal;
        values[1] = significativeFigures(pressureVal);
        values[2] = significativeFigures(PaN2Val);
        values[3] = significativeFigures(PaO2Val);
    }

    // Va voir si les valeurs sont dans les normes sinon, elle va afficher un
    // message d'alert

    private void checkValues() {
        if (PaN2Val >= maxValN2 - 375) {
            N2Warning = "N2 Partial too high narcose may happen";
            blinkingColors(PaN2Txt, Color.red);
            soundSys.playSound();
        } else if (PaN2Val < maxValN2 - 375) {
            N2Warning = "Normal N2 Partial Pression";
            PaN2Txt.setForeground(defaultColor);
        }
        if (depthVal >= 1 && depthVal <= 27) {
            depthWarning = "You are using a normal nitrox mixture.";
            depthTxt.setForeground(new Color(98, 209, 24));
        } else if (depthVal > 27 && depthVal <= 30) {
            depthWarning = "Be careful you are switching to a trimix gaz mixture";
            depthTxt.setForeground(Color.gray);
        } else if (depthVal > 30 && depthVal <= 60) {
            depthWarning = "You will be using a Heliox gaz mixture";
            depthTxt.setForeground(Color.GREEN);
        } else if (depthVal > 60 && depthVal <= 120) {
            depthWarning = "Be careful you aren't using any normal mixture";
            depthTxt.setForeground(Color.yellow);
        }

        if (PaN2Val < maxValN2 - 375 && pressureVal < 50 && depthVal < 120) {
            soundSys.stopSound();
        }

    }

    // Sauvegarde la liste de valeurs dans la liste des valeurs sauvegardé
    private void saveValues() {
        savedValues.add(values);
    }

    // Début du Thread

    @Override
    public void run() {
        interval = 1000000000 / fps;
        deltaT = 0;
        finalTime = System.nanoTime();
        long currentTime;
        timer = 0;

        while (windowThread != null) { // Boucle qui va tourner tant que le programme n'est pas fermé

            if (timerIterations < 0) // Condition de précaution afin d'éviter des erreurs
                timerIterations = 0;

            currentTime = System.nanoTime(); // Sauvegarde le temps actuel en nanoseconde

            deltaT += (currentTime - finalTime) / interval;
            timer += currentTime - finalTime;
            finalTime = currentTime;

            if (deltaT >= 1) {
                frameCounter += 5;
                deltaT--;
                timer++;
            }
            if (timer >= 1000000000) { // Si la valeur timer est >= a 1sec
                displayTimer();
                showSavedValues(); // Afficher les valeurs de la seconde d'avant
                assignValues(); // Assimiler de nouvelles valeurs
                checkValues();
                /*
                 * S'assure que si les valeurs ne sont pas dans les normes
                 * afficher un message
                 * d'alert
                 */
                displayValues(); // Afficher les valeurs
                saveValues(); // Sauvegarder les valeurs dans la liste des valeurs sauvegardées
                timerIterations++; // Incrémente un a combien de secondes sont passer depuis le debut du programme.
                if (timerIterations % 60 == 0) {
                    minPassed++;
                }
                timer = 0;
            }
        }
    }

    private void displayTimer() {
        int secondsPassed = timerIterations;
        timerString = Integer.toString(minPassed) + " min " + Integer.toString(secondsPassed - minPassed * 60) + " s";
        timerTxt.setText(timerString);

    }

    private void displayValues() {

        // Afficher le texte des valeurs.

        depthTxt.setText("<html><body><p>Depth :" + (int) depthVal + "m " + depthWarning + "</p><br></body></html>");
        pressureTxt.setText(
                "<html><body><p>Pressure :" + significativeFigures(pressureVal) + " bar</p><br> </body></html>");
        PaO2Txt.setText("<html><body><p>PaO2 :" + significativeFigures(PaO2Val) + " mmHg " + "</p><br></body></html>");
        PaN2Txt.setText("<html><body><p>PaN2 :" + significativeFigures(PaN2Val) + " mmHg " + N2Warning
                + "</p><br></body></html>");

        // **L'utilisation du html afin faire un saut de ligne.

    }

    // Afficher les valeurs de la seconde d'avant ssi plus d'une seconde est passé
    // depuis le début du programme

    private void showSavedValues() {
        if (timerIterations >= 1 && windowThread != null) {
            savedText.setText("Profondeur: " + savedValues.get(timerIterations - 1)[0] + " m " +
                    "Pression " + savedValues.get(timerIterations - 1)[1] + " bar " +
                    "PaN2: " + savedValues.get(timerIterations - 1)[2] + " Pa" +
                    " PaO2: " + savedValues.get(timerIterations - 1)[3] + " Pa");
        }
    }

    private ImageIcon resizeImage(ImageIcon imageIcon, int w, int h) {
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImage);
        return imageIcon;
    }

    private double significativeFigures(double val) {
        BigDecimal bd = new BigDecimal(val);
        bd = bd.round(new MathContext(3));
        return bd.doubleValue();
    }

    private void blinkingColors(Component comp, Color color) {
        if ((frameCounter / 20) % 2 == 0) {
            comp.setForeground(color);
        } else {
            comp.setForeground(defaultColor);
        }
    }

    public ArrayList<Double[]> getSavedValues() {
        return savedValues;
    }

}
