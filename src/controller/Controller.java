package controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import config.Constants;
import model.Case;
import model.Grille;
import model.Position;
import model.player.Player;
import model.player.PlayerFactory;
import utils.strategyMessage.GraphicMessageHandler;
import utils.strategyMessage.MessageHandler;
import vue.GrillePanel;
import vue.MainFrame;
import vue.Page;
import vue.Panels;

/**
 * Contrôleur principal de l'application.
 * - Crée et gère la grille (modèle)
 * - Gère les joueurs (humain, IA)
 * - Gère la boucle de jeu
 * - Met à jour la vue (GrillePanel) en colorant les cases
 */
public class Controller {

    private MainFrame fenetre;          // Fenêtre principale (vue)
    private Grille grille;             // Le modèle (la "logique" du plateau)
    private GrillePanel grillePanel;   // Le panel Swing qui dessine la grille
    private final List<Player> joueurs;
    private Player joueurActuel;

    private final MessageHandler messageHandler;

    public Controller() {

    	this.messageHandler = new GraphicMessageHandler(true);

        this.joueurs = new ArrayList<>();
        this.grille = null;

        this.grillePanel = new GrillePanel(this); 
        SwingUtilities.invokeLater(() -> {
			this.fenetre = new MainFrame(this);
		});
    }

    /**
     * Initialise les deux joueurs (humain ou IA) à partir du panneau de configuration.
     * Crée la grille et affecte le joueur courant.
     * 
     * @param configurationPanel panneau contenant les choix des joueurs
     */
    public void initialiserJoueurs(Panels configurationPanel) {
        String choixJ1 = configurationPanel.getChoixJoueurs()[0];
        String choixJ2 = configurationPanel.getChoixJoueurs()[1]; 

        Player joueur1 = creerJoueur(1, choixJ1, Case.EtatCase.BLEU, messageHandler);
        Player joueur2 = creerJoueur(2, choixJ2, Case.EtatCase.ROUGE, messageHandler);

        joueurs.clear();
        joueurs.add(joueur1);
        joueurs.add(joueur2);

        this.grille = new Grille(getTaille(), joueur1, joueur2);
        this.joueurActuel = joueur1;
        grille.setJoueurActuel(joueur1);
    }

    /**
     * Crée un joueur à partir des paramètres fournis.
     * 
     * @param numero le numéro du joueur (1 ou 2)
     * @param choix le type de joueur ("Humain", "Aléatoire", "Robot/MCTS", etc.)
     * @param couleur la couleur (état) associée au joueur
     * @param msgHandler le gestionnaire de messages
     * @return une instance de Player
     */
    private static Player creerJoueur(int numero, String choix, Case.EtatCase couleur, MessageHandler msgHandler) {
        String nom;
        String strat;
      switch (choix) {
        case "Humain":
          nom = "Humain_" + numero;
          strat = "human";
          break;
       case "Aléatoire":
          nom = "Random_" + numero;
          strat = "random";
          break;
       case "Robot/MCTS":
          nom = "MCTS_" + numero;
          strat = "mcts";
          break;
        case "Robot/RAVE":
          nom = "RAVE_" + numero;
          strat = "rave";
          break;
       default:
         throw new IllegalArgumentException("Type de joueur inconnu: " + choix);
     }

        return PlayerFactory.creerPlayer(nom, couleur, strat, msgHandler);
    }

    /**
     * Lance la partie après l'initialisation.
     * Affiche une alerte si les joueurs ne sont pas prêts.
     */
    public void demarrerPartie() {
        if (grille == null || joueurs.size() < 2) {
            JOptionPane.showMessageDialog(null, "Joueurs non initialisés !");
            return;
        }
        jouerTour(); 
    }

    /**
     * Gère un tour de jeu :
     * - Fait jouer le joueur actuel,
     * - Colore la case jouée dans la vue,
     * - Vérifie la victoire,
     * - Passe au joueur suivant si la partie continue.
     */
    public void jouerTour() {
        if (joueurActuel == null) return;

        boolean actionOK = joueurActuel.jouer(grille , true);
        if (actionOK) {
            Position p = joueurActuel.getLastMove(); 
            if (p != null) {
                // In the Controller class, when calling colorerCase
                System.out.println("Move: (" + p.getX() + ", " + p.getY() + ")");
                grillePanel.colorerCase(p.getX(), p.getY()-p.getX(), joueurActuel.getCouleur().toString());
            }

            if (grille.verifierVictoire(joueurActuel)) {
                JOptionPane.showMessageDialog(null, 
                    "Le joueur " + joueurActuel.getNom() + " a gagné !",
                    "Partie terminée",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            passerAuJoueurSuivant();
        }
    }

    /**
     * Passe au joueur suivant dans la liste.
     * Si c'est une IA, elle joue automatiquement son tour.
     */
    private void passerAuJoueurSuivant() {
        if (joueurs.size() < 2 || grille == null) return;

        int indexActuel = joueurs.indexOf(joueurActuel);
        int indexSuivant = (indexActuel + 1) % joueurs.size();

        joueurActuel = joueurs.get(indexSuivant);
        grille.setJoueurActuel(joueurActuel);

        // Si le nouveau joueur est IA (nom ne commence pas par "Humain_"), on rejoue tout de suite
        if (!joueurActuel.getNom().startsWith("Humain_")) {
            jouerTour();
        }
    }

    /**
     * Ferme l'application avec une confirmation utilisateur.
     */
    public void fermerApplication() {
        int choix = JOptionPane.showConfirmDialog(
            fenetre, 
            "Voulez-vous vraiment quitter ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION
        );
        if (choix == JOptionPane.YES_OPTION) {
            if (fenetre != null) {
                fenetre.dispose();
            }
            System.exit(0);
        }
    }

    /**
     * Affiche une boîte de dialogue avec un message "À propos".
     * 
     * @param message le message à afficher
     */
    public void afficherMessageAPropos(String message) {
        JOptionPane.showMessageDialog(null, message, "À propos", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Gère la navigation entre les différentes pages de l'application.
     * 
     * @param page la page cible à afficher
     */
    public void naviguer(Page page) {
        if (fenetre == null) return;
        if (Page.ACCEUIL.equals(page)) {
            fenetre.retournerEnArriere(page);
        } else {
            fenetre.configurationView();
        }
    }

    /**
     * Retourne le composant graphique qui affiche la grille.
     * 
     * @return le GrillePanel associé au contrôleur
     */
    public GrillePanel getGrillePanel() {
        return grillePanel;
    }
   
    
    /**
     * Retourne la grille de jeu actuelle.
     * 
     * @return la grille
     */
    public Grille getGrille() {
        return grille;
    }

    
    /**
     * Retourne la taille de la grille, définie dans les constantes.
     * 
     * @return la taille de la grille
     */
    public int getTaille() {
        return Constants.GRID_SIZE;
    }
   
}
