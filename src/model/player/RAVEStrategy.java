package model.player;

import model.*;
import model.mcts.*;
import utils.strategyMessage.MessageHandler;


/**
 * Implémentation de l'optimisation RAVE sur MCTS (Rapid Action Value Estimation).
 * 
 * Cette stratégie permet à un joueur automatique de choisir la meilleure position
 * en utilisant une variante optimisée de MCTS, prenant en compte les actions jouées ailleurs
 * dans l'arbre de recherche.
 */
public class RAVEStrategy implements PlayerStrategy {

    private RAVE rave = new RAVE();

    /**
     * Constructeur de la stratégie RAVE avec un budget d'itérations personnalisé.
     * 
     * @param iterationBudget le nombre d'itérations à effectuer pour la recherche RAVE
     */
    public RAVEStrategy(int iterationBudget) {
        this.rave = new RAVE(iterationBudget);
    }

    /**
     * Constructeur par défaut utilisant le budget d'itérations défini dans les constantes du projet.
     */
    public RAVEStrategy() {
        this.rave = new RAVE();
    }

    /**
     * Place un pion en utilisant l'algorithme RAVE pour déterminer la meilleure position.
     * 
     * @param grille la grille de jeu actuelle
     * @param couleur la couleur de la case hexagonal(état) du joueur
     * @param messageHandler gestionnaire d'affichage des messages
     * @param nonExperimentation indique si les messages doivent être affichés
     * @return la position choisie, ou null si aucun coup valide n'est trouvé
     */
    @Override
    public Position placerPion(Grille grille, Case.EtatCase couleur, MessageHandler messageHandler , boolean nonExperimentation) {
        Position position = rave.trouverMeilleurCoup(grille, couleur);

        if (position != null && grille.estDansGrille(position)) {
            Case cible = grille.getCase(position);
            if (cible.estLibre()) {
                cible.setOccupe(couleur);

                messageHandler.afficherMessage(
                    "RAVE (" + couleur + ") a joué en " + position , nonExperimentation
                );

                return position;
            }
        }
        return null;
    }
}
