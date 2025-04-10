package livraison.testUnitaire.model.player;

import model.*;
import model.Case.EtatCase;
import model.player.MonteCarloStrategy;
import model.player.Player;
import utils.strategyMessage.ConsoleMessageHandler;
import utils.strategyMessage.MessageHandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MonteCarloStrategyTest {

    @Test
    void testMonteCarloJoueUnePositionValide() {
        MessageHandler handler = new ConsoleMessageHandler(true);
        MonteCarloStrategy strategy = new MonteCarloStrategy(50); // petit budget pour test rapide
        Player p1 = new Player("MCTS_Player", EtatCase.BLEU, strategy, handler);
        Player p2 = new Player("Adversaire", EtatCase.ROUGE, strategy, handler);

        Grille grille = new Grille(3, p1, p2);
        Position pos = strategy.placerPion(grille, EtatCase.BLEU, handler, true);

        assertNotNull(pos, "MonteCarlo doit retourner une position valide");
        assertTrue(grille.estDansGrille(pos), "La position retournée doit être dans la grille");
        assertEquals(EtatCase.BLEU, grille.getCase(pos).getOccupe(), "La case doit être occupée par le joueur BLEU");
    }

    @Test
    void testMonteCarloRetourneNullSiGrillePleine() {
        MessageHandler handler = new ConsoleMessageHandler(true);
        MonteCarloStrategy strategy = new MonteCarloStrategy(10);
        Player p1 = new Player("MCTS_Player", EtatCase.BLEU, strategy, handler);
        Player p2 = new Player("Adversaire", EtatCase.ROUGE, strategy, handler);
    
        Grille grille = new Grille(2, p1, p2); // Grille 2x2 = 4 cases
    
        // Occuper toutes les cases
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                grille.getCase(new Position(x, y)).setOccupe(EtatCase.ROUGE);
            }
        }
    
        Position pos = strategy.placerPion(grille, EtatCase.BLEU, handler, true);
        assertNull(pos, "La stratégie doit retourner null si aucune case libre");
    }
}