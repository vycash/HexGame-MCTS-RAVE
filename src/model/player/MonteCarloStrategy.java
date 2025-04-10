package model.player;

import model.*;
import model.mcts.*;
import utils.strategyMessage.MessageHandler;

/**
 * Implémentation de la stratégie de jeu basée sur l'algorithme Monte Carlo Tree Search (MCTS).
 * 
 * Cette stratégie permet de sélectionner la meilleure action en simulant de nombreuses parties
 * pour évaluer la valeur des coups possibles à partir de la configuration actuelle du jeu.
 */
public class MonteCarloStrategy implements PlayerStrategy {

    private MCTS mcts = new MCTS();

    /**
     * Constructeur de la stratégie Monte Carlo avec un budget d'itérations personnalisé.
     * 
     * @param iterationBudget le nombre d'itérations à effectuer pour la recherche MCTS
     */
    public MonteCarloStrategy(int iterationBudget) {
        this.mcts = new MCTS(iterationBudget);
    }

    /**
     * Constructeur par défaut utilisant le budget d'itérations défini dans les constantes du projet.
     */
    public MonteCarloStrategy() {
        this.mcts = new MCTS();
    }

    
    /**
     * Place un pion en utilisant l'algorithme Monte Carlo Tree Search pour déterminer la meilleure position.
     * 
     * @param grille la grille de jeu actuelle
     * @param couleur la couleur (état) du joueur
     * @param messageHandler gestionnaire d'affichage des messages
     * @param nonExperimentation indique si les messages doivent être affichés
     * @return la position choisie, ou null si aucun coup valide n'est trouvé
     */
    @Override
    public Position placerPion(Grille grille, Case.EtatCase couleur, MessageHandler messageHandler , boolean nonExperimentation) {
        Position position = mcts.trouverMeilleurCoup(grille, couleur);

        if (position != null && grille.estDansGrille(position)) {
            Case cible = grille.getCase(position);
            if (cible.estLibre()) {
                cible.setOccupe(couleur);

                messageHandler.afficherMessage(
                    "MonteCarloTreeSearch (" + couleur + ") a joué en " + position , nonExperimentation
                );

                return position;
            }
        }
        return null;
    }
}
