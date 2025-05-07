package model.mcts;

import model.*;
import model.Case.EtatCase;
import config.*;
import java.util.*;


/**
 * Classe représentant un nœud de l'arbre de recherche MCTS (ou RAVE).
 * 
 * Chaque nœud correspond à une position atteinte par un coup, et contient
 * les statistiques nécessaires pour guider l'exploration : victoires, pertes,
 * visites, statistiques RAVE, etc.
 */
public class Noeud {

    private Position move;         // Coup menant à ce nœud
    private Noeud parent;          // Référence au parent
    private Grille grille;
    private Case.EtatCase joueurActuel; // Joueur qui a joué ce coup
    private List<Noeud> enfants;   // Enfants de ce nœud
    private int visits;            // Nombre de visites
    private double wins;
    private double losses;
    private double raveVisits;
    private double raveWins;

    
    /**
     * Constructeur complet d’un nœud.
     * 
     * @param grille la grille à cet instant de la partie
     * @param move la position jouée pour arriver à ce nœud
     * @param parent le nœud parent
     * @param joueurActuel le joueur ayant joué ce coup
     * @param enfants la liste des enfants du nœud
     */
    public Noeud(Grille grille, Position move, Noeud parent, Case.EtatCase joueurActuel, List<Noeud> enfants) {
        this.grille = grille;
        this.move = move;
        this.parent = parent;
        this.joueurActuel = joueurActuel;
        this.enfants = enfants;
        this.visits = 0;
        this.wins = 0;
        this.losses = 0;
        this.raveVisits = 0;
        this.raveWins = 0;
    }

    
    /**
     * Constructeur simplifié avec une liste vide d'enfants.
     * 
     * @param grille la grille à cet instant de la partie
     * @param move la position jouée pour arriver à ce nœud
     * @param parent le nœud parent
     * @param joueurActuel le joueur ayant joué ce coup
     */
    public Noeud(Grille grille, Position move, Noeud parent, Case.EtatCase joueurActuel) {
        this(grille, move, parent, joueurActuel, new ArrayList<>());
    }

    /**
     * Retourne le coup associé à ce nœud.
     *
     * @return la position du coup ayant mené à ce nœud.
     */
    public Position getMove() {
        return move;
    }

    /**
     * Retourne le nœud parent.
     *
     * @return le nœud parent.
     */
    public Noeud getParent() {
        return parent;
    }

    /**
     * Définit le nœud parent.
     *
     * @param parent le nœud parent à définir.
     */
    public void setParent(Noeud parent) {
        this.parent = parent;
    }

    /**
     * Retourne la couleur du joueur qui a joué le coup menant à ce nœud.
     *
     * @return l'état représentant le joueur (BLEU ou ROUGE).
     */
    public Case.EtatCase getJoueurActuel() {
        return joueurActuel;
    }

    /**
     * Ajoute un nombre de victoires au nœud.
     *
     * @param wins le nombre de victoires à ajouter.
     */
    public void addWins(double wins) {
        this.wins += wins;
    }

    /**
     * Ajoute un nombre de défaites au nœud.
     *
     * @param losses le nombre de défaites à ajouter.
     */
    public void addLosses(double losses) {
        this.losses += losses;
    }

    /**
     * Retourne le nombre total de victoires accumulées par ce nœud.
     *
     * @return le nombre de victoires.
     */
    public double getWins() {
        return wins;
    }

    /**
     * Retourne le nombre total de défaites accumulées par ce nœud.
     *
     * @return le nombre de défaites.
     */
    public double getLosses() {
        return losses;
    }


    
    /**
     * Calcule la valeur UCT (Upper Confidence Bound for Trees) du nœud.
     * 
     * @return la valeur UCT, ou +infini si le noeud n'a jamais été visité
     */
    protected double UCT() {

        if (getVisits() == 0) {
            return Double.MAX_VALUE;
        }
        int n = getVisits();
        int N = getParent().getVisits();
        double w = getWins();
        return (w / n) + Constants.EPLOITATION_EXPLORATION_CONSTANT * (Math.sqrt(Math.log(N) / n));
    }

    
    /**
     * Calcule le ratio MCTS classique (wins / losses).
     * 
     * @return la valeur du ratio
     */
    public double getRatio() {
        if (getLosses() == 0) {
            if (getWins() == 0) return 0;
            return Double.POSITIVE_INFINITY;
        }
        return this.wins / this.losses;
    }

    public double getRaveVisits() {
        return this.raveVisits;
    }

    public double getRaveWins() {
        return this.raveWins;
    }

    
    /**
     * Calcule la valeur AMAF (All Moves As First) utilisée en RAVE.
     * 
     * @return la valeur AMAF, ou +infini si pertes nulles
     */
    public double amaf() {
        if (this.raveVisits == 0) {
            if (this.raveWins == 0) return 0;
            return Double.POSITIVE_INFINITY;
        }
        return this.raveWins / this.raveVisits;
    }

    
    /**
     * Retourne la valeur MCTS (ratio classique).
     * 
     * @return la valeur MCTS
     */
    public double mctsValue() {
        return getRatio();
    }

    
    /**
     * Calcule une valeur combinée entre MCTS et RAVE, en tenant compte du nombre de visites.
     * 
     * @return la valeur combinée
     */
    public double getCombinedValue() {
        if (amaf() == Double.POSITIVE_INFINITY || mctsValue() == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
        if (this.raveVisits == 0) return mctsValue(); // Fall back to MCTS value if RAVE data is insufficient
        double k = 3 * this.raveVisits; // Set k to 3 times the number of RAVE visits for balanced weighting
        double beta = k / ( this.raveVisits + k);
        return beta * amaf() + (1 - beta) * mctsValue();
    }

    /**
     * Incrémente le nombre de visites de ce nœud.
     */
    public void incrementVisits() {
        this.visits++;
    }

    /**
     * Définit le nombre de visites du nœud.
     *
     * @param visits le nombre de visites à définir.
     */
    public void setVisits(int visits) {
        this.visits = visits;
    }

    /**
     * Retourne le nombre de visites du nœud.
     *
     * @return le nombre de visites.
     */
    public int getVisits() {
        return visits;
    }

    /**
     * Retourne la liste des enfants de ce nœud.
     *
     * @return la liste des nœuds enfants.
     */
    public List<Noeud> getEnfants() {
        return enfants;
    }

    /**
     * Ajoute un nœud enfant à la liste des enfants, s'il n'est pas déjà présent.
     *
     * @param enfant le nœud enfant à ajouter.
     */
    public void addEnfant(Noeud enfant) {
        if (!enfants.contains(enfant)) {
            enfants.add(enfant);
        }
    }

    /**
     * Retourne la grille associée à ce nœud.
     *
     * @return la grille correspondant à l'état de jeu de ce nœud.
     */
    public Grille getGrille() {
        return grille;
    }

    /**
     * Incrémente le nombre de visites RAVE pour ce nœud.
     */
    public void addRaveVisits() {
        this.raveVisits++;
    }

    /**
     * Définit le nombre de victoires RAVE.
     *
     * @param val la valeur des victoires RAVE à définir.
     */
    public void setRaveWins(Double val) {
        this.raveWins = val;
    }
    
    /**
     * Vérifie si le nœud est complètement développé (tous les coups possibles ont été explorés).
     * 
     * @return true si entièrement développé, false sinon
     */
    public boolean isFullyExpanded() {
        return enfants.size() == getGrille().genererCasesDisponibles().size();
    }

    
    /**
     * Vérifie si le nœud est une feuille (aucun enfant ou état terminal).
     * 
     * @return true si feuille, false sinon
     */
    public boolean isLeaf() {
        return this.enfants.size() == 0 || grille.genererCasesDisponibles().isEmpty() ||
               grille.verifierVictoire(getJoueurActuel()) ||
               grille.verifierVictoire(EtatCase.opposite(getJoueurActuel())) ||
               this.grille.estTerminee();
    }

    
    /**
     * Vérifie si le nœud correspond à un état terminal (fin de partie ou victoire).
     * 
     * @return true si état terminal, false sinon
     */
    public boolean isTerminal() {
        return this.grille.estTerminee() ||
               this.grille.verifierVictoire(this.joueurActuel) ||
               this.grille.verifierVictoire(EtatCase.opposite(this.joueurActuel));
    }

    
    /**
     * Retourne un enfant non visité aléatoire parmi les enfants du nœud.
     * 
     * @return un enfant non visité, ou null si tous visités
     */
    public Noeud getRandomUnvisitedChild() {
        List<Noeud> unvisitedChildren = new ArrayList<>();
        for (Noeud enfant : enfants) {
            if (enfant.getVisits() == 0) {
                unvisitedChildren.add(enfant);
            }
        }
        if (unvisitedChildren.isEmpty()) {
            return null;
        }
        return unvisitedChildren.get(new Random().nextInt(unvisitedChildren.size()));
    }

    @Override
    public String toString() {
        return "Move " + " : " + getMove() +
               " | Score = " + getRatio() +
               " | Visits = " + getVisits() +
               " | Wins = " + getWins() +
               " | Losses = " + getLosses();
    }

    public String toStringRave() {
        return "Move " + " : " + getMove() +
               " | combinedValue = " + getCombinedValue() +
               " | RAVE_Visits = " + getRaveVisits() +
               " | RAVE_Wins = " + getRaveWins() +
               " | MCTS_value = " + mctsValue() +
               " | Visits = " + getVisits();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Noeud noeud = (Noeud) o;
        return move.equals(noeud.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(move);
    }
}