package vue;

import java.awt.*;
import javax.swing.*;

import controller.Controller;

/**
 * Panneau principal du jeu, responsable de l'affichage de la grille de jeu et des options de configuration des joueurs.
 */
public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    
    /**
     * Le contrôleur de l'application qui fait le lien entre la vue (GamePanel) et le modèle (logique du jeu).
     * Il gère les interactions utilisateur, l'état du jeu et la mise à jour de la vue.
     */
    private final Controller controller;
    
    
    /**
     * Le panel de configuration qui permet d'afficher les options de configuration des joueurs dans la vue de jeu.
     * Il est utilisé pour afficher les choix des joueurs, les types de joueurs (humain, aléatoire, MCTS, RAVE, etc.).
     */
    private Panels configurationPanel;

    /**
     * Le panel contenant la grille de jeu. Ce panel est visible lorsque le jeu commence et affiche la représentation visuelle du plateau de jeu.
     */
    private JPanel grillePanelContainer;
   
    /**
     * La vue défilable de la grille de jeu. Elle est utilisée pour ajouter un défilement horizontal et vertical si la grille dépasse la taille de l'écran.
     */
    private JScrollPane scrollPane;
   
    
    /**
     * Le composant de type `JSplitPane` qui permet de diviser l'écran en deux parties. Il affiche la vue de la configuration (sur la gauche) et la grille de jeu (sur la droite).
     * Il permet également de redimensionner la largeur de la zone de configuration.
     */
    private JSplitPane splitPane;

    
    
    
    /**
     * Constructeur de la classe GamePanel qui initialise les composants pour l'affichage de la grille
     * et les options de configuration des joueurs.
     *
     * @param controller Le contrôleur qui fait le lien entre la vue et le modèle de l'application.
     */
    public GamePanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        configurationPanel = new Panels(controller, 1, 2, this);
        configurationPanel.setPreferredSize(new Dimension(300, 600));

        grillePanelContainer = new JPanel(new BorderLayout());
        grillePanelContainer.setBorder(
            BorderFactory.createTitledBorder("Grille de jeu de Hex")
        );
        grillePanelContainer.setBackground(Color.BLACK);
        grillePanelContainer.setVisible(false);

        GrillePanel hexGrille = controller.getGrillePanel();

        scrollPane = new JScrollPane(hexGrille);
        scrollPane.setPreferredSize(new Dimension(750, 550));

        grillePanelContainer.add(scrollPane, BorderLayout.CENTER);

        splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            configurationPanel,
            grillePanelContainer
        );
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    /**
     * Appelé lorsque l'utilisateur clique sur "Jouer".
     * Cette méthode affiche la grille et les panels des joueurs configurés.
     */
    public void afficherGrille() {
        grillePanelContainer.setVisible(true);
        grillePanelContainer.removeAll();

        String choixJ1 = configurationPanel.getChoixJoueurs()[0];
        String choixJ2 = configurationPanel.getChoixJoueurs()[1];

        Panels panelJoueur1 = new Panels(controller, choixJ1);
        Panels panelJoueur2 = new Panels(controller, choixJ2, true);

        JPanel panelJoueurs = new JPanel(new GridLayout(1,2));
        panelJoueurs.add(panelJoueur1);
        panelJoueurs.add(panelJoueur2);

        grillePanelContainer.add(panelJoueurs, BorderLayout.NORTH);

        grillePanelContainer.add(scrollPane, BorderLayout.CENTER);

        splitPane.setDividerLocation(500);

        grillePanelContainer.revalidate();
        grillePanelContainer.repaint();
        splitPane.revalidate();
        splitPane.repaint();
        this.revalidate();
        this.repaint();
    }
}
