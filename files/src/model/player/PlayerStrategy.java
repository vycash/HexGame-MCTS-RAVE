package model.player;

import model.Case;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;

/**
 * Interface représentant une stratégie de placement de pions pour un joueur.
 */
public interface PlayerStrategy {

    /**
     * Place un pion sur la grille en fonction de la stratégie.
     *
     * @param grille La grille sur laquelle jouer.
     * @param couleur La couleur du joueur (BLEU ou ROUGE).
     * @param messageHandler Gestionnaire de messages pour afficher les actions ou erreurs.
     * @return la position (x,y) réellement jouée, ou null si échec
     */
    Position placerPion(Grille grille, Case.EtatCase couleur, MessageHandler messageHandler , boolean nonExperimentation);
}
