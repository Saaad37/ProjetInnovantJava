import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WindowPanel extends JPanel implements Runnable {

    /*
     * Initialisation des variables de valeurs de pression, % de N2, % de O2,
     * et profondeur.
     */
    int pressureVal;
    int prcntN2Val;
    int prcntO2Val;
    int depthVal;

    // TODO Comments and drawing graphs

    /*
     * Initialisation des variables.
     */

    Thread windowThread; // Initialisation du thread, qui va repeter un processus indéfiniment.
    Random rand = new Random(); // Initialisation d'une instance de Random qui va permettre de choisir des
                                // valeurs au hasard
    Font font = new Font("Helvetica", Font.PLAIN, 20); // Création d'une nouvelle police.
    int[] values = new int[4]; // Création d'une liste qui va contenir les valeurs
    int[][] savedValues = new int[7200][4]; // Création d'une liste qui contient une liste, Elle va sauvegarder les
                                            // valeurs
    // la liste savedValues va ressembler à ça [[], [], [], ...]
    int timerIterations; // Création d'une variable qui va s'incrementer de un chaque seconde et sera
                         // l'index des valeurs sauvegardé

    /*
     * Initialisation des texte qui vont apparaître sur la fenêtre
     */

    JLabel pressureTxt = new JLabel();
    JLabel prcntN2Txt = new JLabel();
    JLabel prcntO2Txt = new JLabel();
    JLabel depthTxt = new JLabel();
    JLabel savedText = new JLabel();

    String pressureWarning;
    String N2Warning;
    String O2Warning;
    String depthWarning;

    // Variable qui servent un savoir quand une seconde passe

    int fps = 60;
    long timer;
    double deltaT;
    long finalTime;
    double interval;

    public WindowPanel() { // Constructeur.

        // Appliquer la police a tous les textes

        pressureTxt.setFont(font);
        prcntN2Txt.setFont(font);
        prcntO2Txt.setFont(font);
        depthTxt.setFont(font);
        savedText.setFont(font);

        // Donner un rectangle comme limite de textes

        pressureTxt.setBounds(new Rectangle(10, 100, 720, 50));
        prcntN2Txt.setBounds(new Rectangle(10, 150, 720, 50));
        prcntO2Txt.setBounds(new Rectangle(10, 200, 720, 50));
        depthTxt.setBounds(new Rectangle(10, 250, 720, 50));
        savedText.setBounds(new Rectangle(50, 400, 720, 50));

        // this veut dire cette classe JPanel

        this.setLayout(null);
        this.setPreferredSize(new Dimension(720, 600));
        this.setBackground(Color.WHITE);

        this.add(pressureTxt);
        this.add(prcntN2Txt);
        this.add(prcntO2Txt);
        this.add(depthTxt);
        this.add(savedText);
    }

    // Commencer le thread

    public void startThread() {
        windowThread = new Thread(this);
        windowThread.start();
    }

    // Au début du programme il va choisir une valeur aléatoire puis va choisir des
    // variables au alentour de la valeur choisis précedemment

    public void assignValues() {
        if (timerIterations == 0) {
            pressureVal = rand.nextInt(15, 100);
            prcntN2Val = rand.nextInt(92);
            prcntO2Val = rand.nextInt(80, 100);
            depthVal = rand.nextInt(120);
        } else {
            pressureVal = rand.nextInt(Math.abs(pressureVal - 10), pressureVal + 10);
            prcntN2Val = rand.nextInt(Math.abs(prcntN2Val - 10), prcntN2Val + 10);
            prcntO2Val = rand.nextInt(Math.abs(prcntO2Val - 10), prcntO2Val + 10);
            depthVal = rand.nextInt(Math.abs(depthVal - 10), depthVal + 10);

        }

        // Assigner ces valeurs à la liste des valeurs

        values[0] = pressureVal;
        values[1] = prcntN2Val;
        values[2] = prcntO2Val;
        values[3] = depthVal;
    }

    // Va voir si les valeurs sont dans les normes sinon, elle va afficher un
    // message d'alert

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

    // Sauvegarde la liste de valeurs dans la liste des valeurs sauvegardé
    public void saveValues() {
        savedValues[timerIterations] = values;
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
                deltaT--;
                timer++;
            }
            if (timer >= 1000000000) { // Si la valeur timer est >= a 1sec
                showSavedValues(); // Afficher les valeurs de la seconde d'avant
                assignValues(); // Assimiler de nouvelles valeurs
                checkValues(); // S'assure que si les valeurs ne sont pas dans les normes afficher un message
                               // d'alert
                displayValues(); // Afficher les valeurs
                saveValues(); // Sauvegarder les valeurs dans la liste des valeurs sauvegardées
                timerIterations++; // Incrémente un a combien de secondes sont passer depuis le debut du programme.
                timer = 0;
            }

        }
    }

    public void displayValues() {

        // Afficher le texte des valeurs.

        pressureTxt.setText(
                "<html><body><p>Pressure :" + pressureVal + " bar " + pressureWarning + " </p><br> </body></html>");
        prcntN2Txt.setText("<html><body><p>N2 :" + prcntN2Val + "% " + N2Warning + "</p><br></body></html>");
        prcntO2Txt.setText("<html><body><p>O2 :" + prcntO2Val + "% " + O2Warning + "</p><br></body></html>");
        depthTxt.setText("<html><body><p>Depth :" + depthVal + "m " + depthWarning + "</p><br></body></html>");

        // **L'utilisation du html afin faire un saut de ligne.

    }

    // Afficher les valeurs de la seconde d'avant ssi plus d'une seconde est passé
    // depuis le début du programme
    public void showSavedValues() {
        if (timerIterations >= 1) {
            savedText.setText("Pressure: " + savedValues[timerIterations - 1][0]
                    + " bar N2 : " + savedValues[timerIterations - 1][1]
                    + "% O2: " + savedValues[timerIterations - 1][2]
                    + "% Depth: " + savedValues[timerIterations - 1][3] + " m");
        }
    }
}
