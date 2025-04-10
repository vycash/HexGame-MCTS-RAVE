package model.player;

import model.Case;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;


/**
 * Implémentation de la stratégie de jeu pour un joueur humain.
 * 
 * Cette stratégie demande à l'utilisateur de saisir les coordonnées (x, y)
 * via la console pour jouer un coup, et valide la position choisie.
 */
public class HumanStrategy implements PlayerStrategy {

	
	
	/**
     * Demande au joueur humain de saisir une position valide sur la grille pour placer son pion.
     * Vérifie que la position est dans la grille et que la case est libre.
     * 
     * @param grille la grille de jeu actuelle
     * @param couleur la couleur (état) du joueur
     * @param messageHandler gestionnaire d'affichage et de saisie des messages
     * @param nonExperimentation indique si les messages doivent être affichés
     * @return la position saisie et validée par le joueur, ou null en cas d'annulation
     */
    @Override
    public Position placerPion(Grille grille, Case.EtatCase couleur, MessageHandler messageHandler , boolean nonExperimentation) {
        while (true) {
            String input = messageHandler.demanderInput("Entrez (x y) pour " + couleur + " :");
            if (input == null) {
                messageHandler.afficherErreur("Annulé. Aucun coup n'a été joué.");
                return null;
            }
            String[] parts = input.trim().split("\\s+");
            if (parts.length != 2) {
                messageHandler.afficherErreur("Veuillez entrer deux entiers (x y).");
                continue;
            }
            try {
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                Position pos = new Position(x, y);

                if (!grille.estDansGrille(pos)) {
                    messageHandler.afficherErreur("Position hors du plateau. Réessayez.");
                    continue;
                }

                Case cible = grille.getCase(pos);
                if (cible.estLibre()) {
                    cible.setOccupe(couleur);
                    messageHandler.afficherMessage("Le joueur " + couleur + " a joué en " + pos , nonExperimentation);
                    return pos; 
                } else {
                    messageHandler.afficherErreur("Case déjà occupée. Réessayez.");
                }

            } catch (NumberFormatException e) {
                messageHandler.afficherErreur("Coordonnées invalides.");
            }
        }
    }
}
