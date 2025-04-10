package livraison.testUnitaire.model.player;

import model.player.*;
import model.Case;
import utils.strategyMessage.ConsoleMessageHandler;
import utils.strategyMessage.MessageHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerFactoryTest {

    private final MessageHandler messageHandler = new ConsoleMessageHandler(true);

    @Test
    void testCreerPlayerRandom() {
        Player p = PlayerFactory.creerPlayer("RandomPlayer", Case.EtatCase.BLEU, "random", messageHandler);
        assertNotNull(p);
        assertEquals("RandomPlayer", p.getNom());
        assertEquals(Case.EtatCase.BLEU, p.getCouleur());
    }

    @Test
    void testCreerPlayerHuman() {
        Player p = PlayerFactory.creerPlayer("HumanPlayer", Case.EtatCase.ROUGE, "human", messageHandler);
        assertNotNull(p);
        assertEquals("HumanPlayer", p.getNom());
        assertEquals(Case.EtatCase.ROUGE, p.getCouleur());
    }

    @Test
    void testCreerPlayerAutomatiqueMCTS() {
        Player p = PlayerFactory.creerPlayerAutomatique("AutoMCTS", Case.EtatCase.BLEU, "mcts", 50, messageHandler);
        assertNotNull(p);
        assertEquals("AutoMCTS", p.getNom());
        assertEquals(50, PlayerFactory.getIterationBudget("AutoMCTS"));
    }

    @Test
    void testCreerPlayerAutomatiqueRAVE() {
        Player p = PlayerFactory.creerPlayerAutomatique("AutoRAVE", Case.EtatCase.ROUGE, "rave", 70, messageHandler);
        assertNotNull(p);
        assertEquals("AutoRAVE", p.getNom());
        assertEquals(70, PlayerFactory.getIterationBudget("AutoRAVE"));
    }

    @Test
    void testCreerPlayerInvalidStrategy() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PlayerFactory.creerPlayer("Unknown", Case.EtatCase.BLEU, "strategie_inconnue", messageHandler);
        });
        String expectedMessage = "Stratégie inconnue";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testCreerPlayerAutomatiqueInvalidStrategy() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PlayerFactory.creerPlayerAutomatique("Invalid", Case.EtatCase.ROUGE, "invalid", 10, messageHandler);
        });
        String expectedMessage = "Stratégie inconnue";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}