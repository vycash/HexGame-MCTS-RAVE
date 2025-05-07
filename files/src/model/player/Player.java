package model.player;

import model.Case;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;


/**
 * Représente un joueur dans le jeu, avec un nom, une couleur, une stratégie et un gestionnaire de messages.
 */
public class Player {

    private final String nom;
    private final Case.EtatCase couleur;
    private final PlayerStrategy strategy;
    private final MessageHandler messageHandler;
    
    // Ajout d'un champ pour mémoriser la dernière case jouée
    private Position lastMove;

    
    
    
    /**
     * Construit un joueur avec les informations fournies.
     *
     * @param nom Le nom du joueur.
     * @param couleur La couleur associée au joueur.
     * @param strategy La stratégie utilisée par le joueur pour placer ses pions.
     * @param messageHandler Le gestionnaire de messages pour l'interaction avec l'utilisateur.
     * @throws IllegalArgumentException si la stratégie ou le messageHandler est null.
     */
    public Player(String nom, Case.EtatCase couleur, PlayerStrategy strategy, MessageHandler messageHandler) {
        if (strategy == null || messageHandler == null) {
            throw new IllegalArgumentException("Stratégie ou messageHandler null !");
        }
        this.nom = nom;
        this.couleur = couleur;
        this.strategy = strategy;
        this.messageHandler = messageHandler;
        this.lastMove = null;
    }

    /**
     * Permet au joueur de jouer un coup sur la grille en utilisant sa stratégie.
     *
     * @param grille La grille de jeu actuelle.
     * @param nonExperimentation Indique si le joueur joue en mode non expérimentation.
     * @return true si un coup a été correctement placé et mémorisé, false sinon.
     */
    public boolean jouer(Grille grille , boolean nonExperimentation) {

    	Position pos = strategy.placerPion(grille, couleur, messageHandler , nonExperimentation);
        if (pos != null) {
            this.lastMove = pos;
            return true;
        }
        return false;
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return le nom du joueur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la couleur du joueur.
     *
     * @return l'état représentant la couleur (BLEU ou ROUGE).
     */
    public Case.EtatCase getCouleur() {
        return couleur;
    }

    /**
     * Retourne la dernière position jouée par ce joueur.
     *
     * @return la dernière position jouée, ou null si aucun coup n'a été joué.
     */
    public Position getLastMove() {
        return lastMove;
    }

    /**
     * Retourne une représentation textuelle du joueur.
     *
     * @return une chaîne décrivant le joueur, sa couleur et sa stratégie.
     */
    @Override
    public String toString(){
        return "Joueur " + nom + " [couleur=" + couleur + ", stratégie=" + strategy + "]";
    }
}
