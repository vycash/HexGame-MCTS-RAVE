package model;

import config.Constants;
import utils.strategyMessage.*;
import model.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe Orchestrateur qui gère une partie de Hex.
 * 
 * Deux modes principaux :
 *
 * Mode console (interactif) : le joueur choisit sa stratégie et interagit avec le jeu.
 * Mode expérimentation (non interactif) : la partie est automatisée (MCTS, RAVE, etc.).
 *
 * En mode expérimentation, seuls les messages indispensables (ceux demandant une saisie
 * en console ou les retours d'information en console) peuvent être affichés, 
 * et les joueurs sont créés automatiquement.
 * 
 */
public class Orchestrateur {

    /** La grille du jeu Hex. */
    private final Grille grille;

    /** Les deux joueurs (BLEU et ROUGE). */
    private final List<Player> joueurs;

    /** Le joueur dont c'est le tour (initialisé au début). */
    private Player joueur_qui_commence;

    /** Gestionnaire de messages pour l'affichage en console. */
    private final MessageHandler messageHandler;

    /** Scanner utilisé en mode console. */
    private final Scanner scanner;

    /** Indique si on est en mode expérimentation (non interactif). */
    private boolean experimentationMode;

    /**
     * Constructeur principal de la classe Orchestrateur (version console 
     * ou "semi-expérimentation").
     * 
     * En mode console : on demande au joueur de choisir qui commence.
     * En mode expérimentation (si experimentationMode = true), 
     *   on crée deux joueurs MCTS par défaut, mais on appelle encore getJoueurQuiCommence() 
     *   (ce qui peut déclencher un prompt).
     *
     * @param taille Taille de la grille.
     * @param messageHandler Gestionnaire de messages pour l'affichage.
     * @param experimentationMode Indique si on est en mode expérimentation.
     */
    public Orchestrateur(int taille, MessageHandler messageHandler, boolean experimentationMode) {
        this.scanner = new Scanner(System.in);
        this.messageHandler = messageHandler;
        this.experimentationMode = experimentationMode;

        // Initialise les deux joueurs (interactif ou par défaut MCTS) 
        this.joueurs = initialiserJoueurs();

        // Crée la grille (avec le joueur BLEU, le joueur ROUGE)
        this.grille = new Grille(taille, joueurs.get(0), joueurs.get(1));

        // Demande (ou non) qui commence 
        this.joueur_qui_commence = getJoueurQuiCommence();
    }

    /**
     * Constructeur pour le mode expérimentation complet (sans prompts).
     * 
     * Crée deux joueurs (souvent MCTS vs MCTS) avec des budgets distincts.
     * Détermine directement qui commence (BLEU ou ROUGE).
     * Ne déclenche aucune saisie console (pas d'appel à getJoueurQuiCommence()).
     * 
     *
     * @param taille Taille de la grille (ex. 5, 7, 9...).
     * @param budgetJ1 Budget MCTS pour le joueur BLEU.
     * @param budgetJ2 Budget MCTS pour le joueur ROUGE.
     * @param startPlayer "BLEU" ou "ROUGE" (qui commence).
     * @param strategieJ1 "RAVE" ou "MCTS" pour le premier Joueur(qui commence).
     * @param strategieJ2 "RAVE" ou "MCTS" pour le deuxiéme Joueur(qui commence).
     * @param experimentationMode Indique si on est en mode expérimentation (true = pas de prompt).
     */
    public Orchestrateur(int taille, int budgetJ1, int budgetJ2, 
                         String startPlayer, String strategieJ1, String strategieJ2, boolean experimentationMode) {
        this.scanner = new Scanner(System.in);
        this.messageHandler = new ConsoleMessageHandler(false);
        this.experimentationMode = experimentationMode;

     // Création dynamique des deux joueurs avec la stratégie passée en paramètre (MCTS ou RAVE)
        Player j1 = PlayerFactory.creerPlayerAutomatique("J1_" + strategieJ1, Case.EtatCase.BLEU, strategieJ1, budgetJ1, messageHandler);
        Player j2 = PlayerFactory.creerPlayerAutomatique("J2_" + strategieJ2, Case.EtatCase.ROUGE, strategieJ2, budgetJ2, messageHandler);

        this.joueurs = new ArrayList<>();
        joueurs.add(j1);
        joueurs.add(j2);

        this.grille = new Grille(taille, j1, j2);

        // Choix du joueur qui commence
        if ("BLEU".equalsIgnoreCase(startPlayer)) {
            this.joueur_qui_commence = j1;
        } else {
            this.joueur_qui_commence = j2;
        }
    }

    /**
     * Constructeur par défaut (mode console avec taille par défaut).
     */
    public Orchestrateur() {
        this(Constants.GRID_SIZE, new ConsoleMessageHandler(true), false);
    }

    /**
     * Retourne la grille du jeu.
     */
    public Grille getGrille() {
        return this.grille;
    }

    /**
     * Retourne la liste des joueurs (BLEU, ROUGE).
     */
    public List<Player> getJoueurs() {
        return this.joueurs;
    }

    /**
     * Méthode qui détermine le joueur qui commence.
     * 
     * En mode console, on demande à l'utilisateur de choisir (BLEU ou ROUGE).
     * En mode expérimentation, si vous appelez ce constructeur, on fait également un prompt 
     *   (dans le nouveau constructeur {@code (int, int, int, String, boolean)}, on ne l'appelle pas).
     * 
     * @return Le joueur choisi pour commencer la partie.
     */
    public Player getJoueurQuiCommence() {
        messageHandler.afficherMessage("Choisissez le joueur qui commence", true);
        messageHandler.afficherMessage("1. BLEU", true);
        messageHandler.afficherMessage("2. ROUGE", true);

        String input = scanner.nextLine();
        int playerID = 1;
        try {
            if (!input.isEmpty()) {
                playerID = Integer.parseInt(input);
            }
        } catch (NumberFormatException e) {
            messageHandler.afficherErreur("Entrée invalide. Le joueur 1 sera choisi par défaut.");
        }
        
        Player chosen;
        switch (playerID) {
            case 1:
                chosen = this.joueurs.get(0);
                break;
            case 2:
                chosen = this.joueurs.get(1);
                break;
            default:
                messageHandler.afficherErreur("Entrée invalide. Le joueur 1 sera choisi par défaut.");
                chosen = this.joueurs.get(0);
        }
        return chosen;
    }

    /**
     * Initialise les deux joueurs (BLEU, ROUGE) selon qu'on est en mode console 
     * ou en mode expérimentation "simplifié".
     * 
     * Mode console : L'utilisateur choisit la stratégie (Humain, Random, MCTS, RAVE).
     * Mode expérimentation : On crée deux MCTS (MCTS_1 et MCTS_2) par défaut.
     * 
     * @return Liste contenant deux joueurs (index 0 = BLEU, 1 = ROUGE).
     */
    private List<Player> initialiserJoueurs() {
        List<Player> joueurs = new ArrayList<>();
        joueurs.add(creerJoueur(1, "BLEU"));
        joueurs.add(creerJoueur(2, "ROUGE"));
        return joueurs;
    }

    /**
     * Crée un joueur selon le mode (console ou expérimentation).
     *
     * En mode console : prompt pour choisir la stratégie.
     * En mode expérimentation : crée un MCTS par défaut.
     * 
     * @param numero  Numéro du joueur (1 ou 2).
     * @param couleur BLEU ou ROUGE.
     * @return Le Player créé.
     */
    private Player creerJoueur(int numero, String couleur) {
        if (experimentationMode) {
            // Mode expérimentation : MCTS par défaut
            String nom = "MCTS_" + numero;
            String typeStrategy = "mcts";
            return PlayerFactory.creerPlayer(nom, Case.EtatCase.valueOf(couleur), typeStrategy, messageHandler);
        }

        // Mode console : prompt
        messageHandler.afficherMessage("Choisissez le type pour le joueur " + numero 
                                       + " (" + couleur + ") :", true);
        messageHandler.afficherMessage("1 : Humain", true);
        messageHandler.afficherMessage("2 : Random", true);
        messageHandler.afficherMessage("3 : Monte Carlo Tree Search (MCTS)", true);
        messageHandler.afficherMessage("4 : RAVE", true);

        int choix = 0;
        while (choix < 1 || choix > 4) {
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                messageHandler.afficherErreur("Entrée invalide. Veuillez entrer 1, 2, 3 ou 4.");
            }
        }
        String nom;
        String typeStrategy;
        switch (choix) {
            case 1:
                nom = "Humain_" + numero;
                typeStrategy = "human";
                break;
            case 2:
                nom = "Random_" + numero;
                typeStrategy = "random";
                break;
            case 3:
                nom = "MCTS_" + numero;
                typeStrategy = "mcts";
                break;
            case 4:
                nom = "RAVE_" + numero;
                typeStrategy = "rave";
                break;
            default:
                // Par défaut, si jamais
                nom = "Humain_" + numero;
                typeStrategy = "human";
                break;
        }
        return PlayerFactory.creerPlayer(nom, Case.EtatCase.valueOf(couleur), typeStrategy, messageHandler);
    }

    /**
     * Lance la partie.
     * 
     * En mode console : exécute play() (affichage complet, prompt...).
     * En mode expérimentation : partie automatique (sans attente).
     * 
     */
    public void demarrerPartie() {
        messageHandler.afficherMessage("Début de la partie !", true);

        if (experimentationMode) {
            // Partie automatique
            while (!grille.estTerminee()) {
                joueur_qui_commence.jouer(grille, experimentationMode);
                if (grille.verifierVictoire(joueur_qui_commence)) {
                    break;
                }
                joueur_qui_commence = passerAuJoueurSuivant(joueur_qui_commence);
            }
        } else {
            // Mode console
            play();
        }
    }

    /**
     * Joue automatiquement une partie (sans affichage interactif), 
     * et renvoie l'identité du vainqueur.
     * 
     * Renvoie :
     * 
     *  1 si le premier joueur (index 0) gagne
     *  2 si le second joueur (index 1) gagne
     *  -1 si match nul (ce qui est rare à Hex)
     * 
     * @return 1, 2 ou -1 selon le vainqueur (ou nul).
     */
    public int jouerPartieAutomatique() {
        while (!grille.estTerminee()) {
            joueur_qui_commence.jouer(grille, experimentationMode);
            if (grille.verifierVictoire(joueur_qui_commence)) {
                if (joueur_qui_commence == joueurs.get(0)) {
                    afficherResultatFinal();
                    return 1;
                } else {
                    afficherResultatFinal();
                    return 2;
                }
            }
            joueur_qui_commence = passerAuJoueurSuivant(joueur_qui_commence);
        }
        // Si la grille est "terminée" sans vainqueur
        afficherResultatFinal();
        return -1;
    }

    /**
     * Méthode interne (mode console) :
     * Affiche la grille, gère le prompt, attend l'entrée de l'utilisateur entre les tours, etc.
     */
    private void play() {
        int tour = 0;
        while (!grille.estTerminee()) {
            tour++;
            messageHandler.afficherMessage("Tour " + tour + " - C'est au tour de " 
                    + joueur_qui_commence.getNom() + " (" + joueur_qui_commence.getCouleur() + ")", true);

            grille.afficherGrille();

            if (joueur_qui_commence.getNom().startsWith("Humain_")) {
                messageHandler.afficherMessage("Entrez un numéro entre 1 et " 
                        + (grille.getTaille() * grille.getTaille()) + " pour choisir une case.", true);
            }

            boolean actionReussie = joueur_qui_commence.jouer(grille, experimentationMode);
            if (!actionReussie) {
                messageHandler.afficherErreur("Action invalide. Veuillez réessayer.");
                continue;
            }

            messageHandler.afficherMessage(joueur_qui_commence.getNom() + " a placé son pion.", true);

            if (grille.verifierVictoire(joueur_qui_commence)) {
                messageHandler.afficherMessage("Le joueur " + joueur_qui_commence.getNom() + " a gagné !", true);
                break;
            }

            grille.afficherGrille();
            joueur_qui_commence = passerAuJoueurSuivant(joueur_qui_commence);

            if (!experimentationMode) {
                attendreEntrer();
            }
            effacerEcran();
        }

        if (!experimentationMode) {
            attendreEntrer();
        }
        afficherResultatFinal();
    }

    /**
     * Méthode pouvant être utilisée pour enchaîner plusieurs parties 
     * en mode expérimentation "semi-automatique" (prompt pour le nombre de parties).
     */
    public void lancerExperimentation() {
        String input = this.messageHandler.demanderInput("Entrer le nombre de partie souhaitée : ");
        int nbParties = Integer.parseInt(input);

        while (nbParties > 0) {
            play();
            nbParties--;
            // Réinitialise la grille pour la partie suivante
            this.grille.clear();
        }
    }

    /**
     * Passe au joueur suivant (index 0 => 1 => 0 => 1, etc.).
     *
     * @param joueurActuel Le joueur actuellement en train de jouer.
     * @return Le joueur suivant (dans la liste).
     */
    public Player passerAuJoueurSuivant(Player joueurActuel) {
        int indexActuel = joueurs.indexOf(joueurActuel);
        return joueurs.get((indexActuel + 1) % joueurs.size());
    }

    /**
     * Affiche le résultat final dans la console (sans logger).
     */
    private void afficherResultatFinal() {
        messageHandler.afficherMessage("\nLa partie est terminée !", false);
        String resultat = "Match nul !";
        for (Player joueur : joueurs) {
            if (grille.verifierVictoire(joueur)) {
                resultat = "Gagnant : " + joueur.getNom() + " (" + joueur.getCouleur() + ")";
                messageHandler.afficherMessage(resultat, false);
                break;
            }
        }
        grille.afficherGrille();
    }

    /**
     * Attend que l'utilisateur appuie sur Entrée pour continuer (uniquement en mode console).
     */
    private void attendreEntrer() {
        messageHandler.afficherMessage("Appuyez sur Entrée pour passer au tour suivant...", true);
        scanner.nextLine();
    }

    /**
     * Efface l'écran de la console via des séquences ANSI (optionnel).
     */
    private void effacerEcran() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
