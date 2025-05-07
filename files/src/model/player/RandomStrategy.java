package model.player;

import java.util.Random;
import java.util.Map;
import model.*;
import utils.strategyMessage.MessageHandler;

/**
 * Implémentation d'une stratégie de joueur basée sur le hasard.
 * 
 * Cette stratégie choisit aléatoirement une case libre dans la grille pour y placer un pion.
 * Elle peut être utilisée comme référence de base ou pour simuler un joueur non intelligent.
 */
public class RandomStrategy implements PlayerStrategy {

    private final Random random = new Random();

    
    
    /**
     * Place un pion sur une case choisie aléatoirement parmi les cases disponibles de la grille.
     * 
     * @param grille la grille de jeu actuelle
     * @param couleur la couleur (état) du joueur
     * @param messageHandler gestionnaire d'affichage des messages
     * @param nonExperimentation indique si les messages doivent être affichés
     * @return la position choisie, ou null si aucune case disponible
     */
    @Override
    public Position placerPion(Grille grille, Case.EtatCase couleur, MessageHandler messageHandler , boolean nonExperimentation) {
        Map<Integer, Position> disponibles = grille.genererCasesDisponibles();
        if (disponibles.isEmpty()) {
            return null; 
        }
        int choix = random.nextInt(disponibles.size()) + 1;
        Position pos = disponibles.get(choix);

        if (pos != null && grille.estDansGrille(pos)) {
            Case c = grille.getCase(pos);
            if (c.estLibre()) {
                c.setOccupe(couleur);

                messageHandler.afficherMessage("RandomStrategy (" + couleur + ") a joué en " + pos , nonExperimentation);

                return pos;
            }
        }
        return null;
    }
}
