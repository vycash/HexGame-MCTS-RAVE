package vue;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;

/**
 * La classe MainFrame représente la fenêtre principale de l'application du jeu de HEX.
 * Elle gère l'affichage des différents panels de l'application en utilisant un layout de type CardLayout,
 * permettant ainsi de passer facilement entre les différentes vues (MainMenu, GamePage, etc.).
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    /**
     * Largeur de la fenêtre principale de l'application en pixels.
     * Définit la largeur de la fenêtre où la vue sera affichée.
     */
    public static final int Largeur = 1500;

    /**
     * Hauteur de la fenêtre principale de l'application en pixels.
     * Définit la hauteur de la fenêtre où la vue sera affichée.
     */
    public static final int Hauteur = 800;

    /**
     * Le contrôleur de l'application qui fait le lien entre la vue (MainFrame) et le modèle (logique du jeu).
     * Il est responsable de la gestion des actions de l'utilisateur, de la navigation entre les vues et de l'état du jeu.
     */
    private Controller controleur;

    /**
     * Le panel qui contient l'ensemble des vues, utilisant un `CardLayout` pour gérer les transitions entre les différentes pages de l'application (par exemple, l'écran d'accueil, la page de configuration, etc.).
     */
    private JPanel cardPanel;

    /**
     * Le gestionnaire de layout utilisé pour gérer les vues dans le `cardPanel`.
     * Le `CardLayout` permet de basculer entre différentes vues, comme un système de cartes, permettant une navigation fluide.
     */
    private CardLayout card;


    /**
     * Construit la fenêtre principale de l'application, initialisant le layout, les dimensions et
     * les composants de base, y compris la barre de menu et le panel principal pour l'affichage des vues.
     *
     * @param controleur Le contrôleur qui fait le lien entre les vues et le modèle de l'application.
     */
    public MainFrame(Controller controleur) {
        this.controleur = controleur;

        this.setTitle("Jeu de Hex");
        this.setSize(Largeur, Hauteur);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        Panels barreDeMenu = new Panels(this, this.controleur);
        this.add(barreDeMenu, BorderLayout.NORTH);

        this.card = new CardLayout();
        this.cardPanel = new JPanel(card);

        initialiseCardPanel();

        this.add(this.cardPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    /**
     * Initialise le panel principal utilisant un CardLayout. Cette méthode prépare l'application
     * à afficher différentes vues comme l'écran d'accueil et l'écran de configuration.
     */
    private void initialiseCardPanel() {
        AccueilPanel accueilContainer = new AccueilPanel(this.controleur, Largeur, Hauteur);
        this.cardPanel.add(accueilContainer, "Accueil");

    }

    /**
     * Affiche la vue de configuration (GamePage) par exemple.
     * Ajoute un nouveau panel "GamePage" au CardLayout principal et l'affiche.
     *
     */
    public void configurationView() {
        GamePanel configurationPanel = new GamePanel(this.controleur);
        this.cardPanel.add(configurationPanel, "Configuration");
        this.card.show(cardPanel, "Configuration");

    }

    /**
     * Permet de retourner à la vue précédemment affichée, basée sur le mode spécifié.
     * Cette méthode est principalement utilisée pour naviguer entre les différentes vues de l'application.
     */
    public void retournerEnArriere(Page page) {
        if (Page.ACCEUIL.equals(page)) {
            this.card.show(cardPanel, "Accueil");
        } else {
            this.card.show(cardPanel, "Configuration");
        }
    }

   
}
