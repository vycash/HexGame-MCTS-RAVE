package livraison.testUnitaire.model.player;

import model.Case;
import model.Grille;
import model.Position;
import model.player.Player;
import model.player.PlayerStrategy;
import utils.strategyMessage.ConsoleMessageHandler;
import utils.strategyMessage.MessageHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe Player.
 */
class PlayerTest {

    private Player joueur;
    private MessageHandler messageHandler;
    private Grille grille;
    private PlayerStrategy strategieMock;

    @BeforeEach
    void setUp() {
        messageHandler = new ConsoleMessageHandler(true);  // Simule l'affichage console
        strategieMock = (g, couleur, mh, nonExperimentation) -> new Position(2, 3); // Simule une stratégie simple
        joueur = new Player("TestJoueur", Case.EtatCase.BLEU, strategieMock, messageHandler);
        grille = new Grille(5, joueur, new Player("Adversaire", Case.EtatCase.ROUGE, strategieMock, messageHandler));
    }

    @Test
    void testCreationJoueur() {
        assertNotNull(joueur, "Le joueur doit être créé sans erreur.");
        assertEquals("TestJoueur", joueur.getNom(), "Le nom du joueur doit être correct.");
        assertEquals(Case.EtatCase.BLEU, joueur.getCouleur(), "La couleur du joueur doit être correcte.");
    }

    @Test
    void testJouerUnCoup() {
        boolean coupJoue = joueur.jouer(grille, false);
        assertTrue(coupJoue, "Le joueur doit réussir à jouer un coup.");
        assertEquals(new Position(2, 3), joueur.getLastMove(), "La position jouée doit être mémorisée.");
    }

    @Test
    void testJoueurAvecStrategieNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("JoueurErreur", Case.EtatCase.ROUGE, null, messageHandler);
        }, "Un joueur ne doit pas être créé avec une stratégie null.");
    }

    @Test
    void testJoueurAvecMessageHandlerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("JoueurErreur", Case.EtatCase.ROUGE, strategieMock, null);
        }, "Un joueur ne doit pas être créé avec un MessageHandler null.");
    }

    @Test
    void testDernierCoupNonJoué() {
        assertNull(joueur.getLastMove(), "Avant de jouer, la dernière position jouée doit être null.");
    }
}