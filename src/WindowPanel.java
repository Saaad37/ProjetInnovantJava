import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class WindowPanel extends JPanel implements Runnable {

    /*
     * Initialisation des variables de valeurs de pression, Pression partielle de
     * N2, pression partielle de O2,
     * et profondeur.
     */

    float XO2 = 0.2f; // %
    float XN2 = 0.3f; // %
    float P0 = 1.013f; // mmHg
    int P0Pasc = 101325;
    int rhoSaltedWater = 1025; // kg/m3
    float g = 9.81f; // m/s^2 ou N/Kg

    float pressureVal;
    float pressureValPasc;
    float PaN2Val;
    float PaO2Val;
    float depthVal;

    // TODO Faire un graphe des valeurs depuis le debut du programme

    /*
     * Initialisation des variables.
     */

    Thread windowThread; // Initialisation du thread, qui va repeter un processus indéfiniment.
    Random rand = new Random(); // Initialisation d'une instance de Random qui va permettre de choisir des
                                // valeurs au hasard
    Font font = new Font("Helvetica", Font.PLAIN, 20); // Création d'une nouvelle police.
    float[] values = new float[4]; // Création d'une liste qui va contenir les valeurs
    float[][] savedValues = new float[7200][4]; // Création d'une liste qui contient une liste, Elle va sauvegarder les
                                                // valeurs
    // la liste savedValues va ressembler à ça [[], [], [], ...]

    int timerIterations; // Création d'une variable qui va s'incrementer de un chaque seconde et sera
                         // l'index des valeurs sauvegardé
    Color bgColor = new Color(34, 48, 97);
    ImageIcon subroticIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("./subrotic.jpg")));

    /*
     * Initialisation des texte qui vont apparaître sur la fenêtre
     */

    JLabel pressureTxt = new JLabel();
    JLabel PaN2Txt = new JLabel();
    JLabel PaO2Txt = new JLabel();
    JLabel depthTxt = new JLabel();
    JLabel savedText = new JLabel();
    JLabel subroticLogo = new JLabel();

    String pressureWarning;
    String N2Warning;
    String O2Warning;
    String depthWarning;

    // Variable qui servent a savoir quand une seconde passe

    int fps = 60;
    long timer;
    double deltaT;
    long finalTime;
    double interval;

    public WindowPanel() { // Constructeur.

        // Appliquer la police a tous les textes

        pressureTxt.setFont(font);
        PaN2Txt.setFont(font);
        PaO2Txt.setFont(font);
        depthTxt.setFont(font);
        savedText.setFont(font);
        subroticLogo.setIcon(subroticIcon);

        pressureTxt.setForeground(Color.white);
        PaN2Txt.setForeground(Color.white);
        PaO2Txt.setForeground(Color.white);
        depthTxt.setForeground(Color.white);
        savedText.setForeground(Color.white);

        // Donner un rectangle comme limite de textes
        subroticLogo.setBounds(new Rectangle(20, 20, 125, 125));
        PaN2Txt.setBounds(new Rectangle(50, 150, 720, 50));
        PaO2Txt.setBounds(new Rectangle(50, 200, 720, 50));
        depthTxt.setBounds(new Rectangle(50, 250, 720, 50));
        pressureTxt.setBounds(new Rectangle(50, 300, 720, 50));
        savedText.setBounds(new Rectangle(10, 425, 720, 50));

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
    }

    // Commencer le thread

    public void startThread() {
        windowThread = new Thread(this);
        windowThread.start();
    }

    // Au début du programme il va choisir une valeur aléatoire puis va choisir des
    // variables au alentour de la valeur choisis précedemment

    public void assignValues() {
        depthVal++;
        pressureValPasc = (P0Pasc + (rhoSaltedWater * g * depthVal));// Pascal
        pressureVal = pressureValPasc / 100000;
        PaN2Val = (pressureVal - P0) * 750 * XN2; // mmHg
        PaO2Val = (pressureVal - P0) * 750 * XO2; // mmHg

        // Assigner ces valeurs à la liste des valeurs

        values[0] = depthVal;
        values[1] = significativeFigures(pressureVal, 3);
        values[2] = significativeFigures(PaN2Val, 2);
        values[3] = significativeFigures(PaO2Val, 2);
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
        if (PaN2Val >= 10) {
            N2Warning = "RETURN TO THE SURFACE N2 IN BLOOD IS TOO HIGH";
            PaN2Txt.setForeground(Color.RED);
        } else if (PaN2Val < 10) {
            N2Warning = "Normal N2 percentages";
            PaN2Txt.setForeground(Color.black);
        }
        if (PaO2Val >= 92) {
            O2Warning = "Normal Saturation.";
            PaO2Txt.setForeground(Color.black);
        } else if (PaO2Val < 92) {
            O2Warning = "O2 SATURATION IS TOO LOW !";
            PaO2Txt.setForeground(Color.RED);
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
                // checkValues();
                /*
                 * S'assure que si les valeurs ne sont pas dans les normes
                 * afficher un message
                 * d'alert
                 */
                displayValues(); // Afficher les valeurs
                saveValues(); // Sauvegarder les valeurs dans la liste des valeurs sauvegardées
                timerIterations++; // Incrémente un a combien de secondes sont passer depuis le debut du programme.
                timer = 0;
            }

        }
    }

    public void displayValues() {

        // Afficher le texte des valeurs.

        depthTxt.setText("<html><body><p>Depth :" + (int) depthVal + "m " + depthWarning + "</p><br></body></html>");
        pressureTxt.setText(
                "<html><body><p>Pressure :" + significativeFigures(pressureVal, 3) + " bar" + pressureWarning
                        + " </p><br> </body></html>");
        PaO2Txt.setText("<html><body><p>PaO2 :" + significativeFigures(PaO2Val, 3) + " mmHg " + O2Warning + "</p><br></body></html>");
        PaN2Txt.setText("<html><body><p>PaN2 :" + significativeFigures(PaN2Val, 3) + " mmHg " + N2Warning + "</p><br></body></html>");

        // **L'utilisation du html afin faire un saut de ligne.

    }

    // Afficher les valeurs de la seconde d'avant ssi plus d'une seconde est passé
    // depuis le début du programme
    public void showSavedValues() {
        if (timerIterations >= 1) {
            savedText.setText("Profondeur: " + savedValues[timerIterations - 1][0] + " m " +
                    "Pression " + savedValues[timerIterations - 1][1] + " bar " +
                    "PaN2: " + savedValues[timerIterations - 1][2] + " Pa" +
                    " PaO2: " + savedValues[timerIterations - 1][3] + " Pa");
        }
    }

    public ImageIcon resizeImage(ImageIcon imageIcon, int w, int h) {
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImage);
        return imageIcon;
    }

    public float significativeFigures(float val, int fig){
        double newVal = (double) val;
        return (float) Math.round((newVal*Math.pow(10, fig))/Math.pow(10, fig));
    }

}
