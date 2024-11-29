public class Main {
    public static void main(String[] args) {
        WindowPanel wp = new WindowPanel();
        Window window = new Window();
        window.add(wp); // Ajouter le panel à la fenêtre.
        window.pack(); // Affichage de la fenêtre.
    }
}
