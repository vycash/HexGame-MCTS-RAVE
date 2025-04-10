package livraison.testUnitaire.model.player;

import model.*;
import model.Case.EtatCase;
import model.player.Player;
import model.player.RandomStrategy;
import utils.strategyMessage.ConsoleMessageHandler;
import utils.strategyMessage.MessageHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RandomStrategyTest {

    @Test
    void testPlacerPionReturnsValidPosition() {
        MessageHandler handler = new ConsoleMessageHandler(true);
        RandomStrategy strategy = new RandomStrategy();
        Player p1 = new Player("Test1", EtatCase.BLEU, strategy, handler);
        Player p2 = new Player("Test2", EtatCase.ROUGE, strategy, handler);

        // Grille 3x3 vide
        Grille grille = new Grille(3, p1, p2);
        Position pos = strategy.placerPion(grille, EtatCase.BLEU, handler, true);

        assertNotNull(pos, "La position choisie ne doit pas être null");
        assertEquals(EtatCase.BLEU, grille.getCase(pos).getOccupe(), "La case doit être occupée par le joueur BLEU");
    }

    @Test
    void testPlacerPionReturnsNullIfNoMoves() {
        MessageHandler handler = new ConsoleMessageHandler(true);
        RandomStrategy strategy = new RandomStrategy();
        Player p1 = new Player("Test1", EtatCase.BLEU, strategy, handler);
        Player p2 = new Player("Test2", EtatCase.ROUGE, strategy, handler);

        // Grille 1x1 déjà occupée
        Grille grille = new Grille(1, p1, p2);
        grille.getCase(new Position(0, 0)).setOccupe(EtatCase.ROUGE);

        Position pos = strategy.placerPion(grille, EtatCase.BLEU, handler, true);
        assertNull(pos, "La stratégie doit retourner null quand aucune case n’est disponible");
    }
}