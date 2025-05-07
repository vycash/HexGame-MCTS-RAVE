package livraison.testUnitaire.model;
import model.Grille;
import model.Case;
import model.Position;
import model.player.Player;
import model.player.HumanStrategy;
import model.player.PlayerStrategy;
import utils.strategyMessage.ConsoleMessageHandler;
import utils.strategyMessage.MessageHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GrilleTest {
    private Grille grille;
    private Player joueur1;
    private Player joueur2;
    private MessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        messageHandler = new ConsoleMessageHandler(true);

        // Fix: Include the MessageHandler when creating Players
        joueur1 = new Player("Joueur1", Case.EtatCase.BLEU, new HumanStrategy(), messageHandler);
        joueur2 = new Player("Joueur2", Case.EtatCase.ROUGE, new HumanStrategy(), messageHandler);

        // Initialize the Grid
        grille = new Grille(5, joueur1, joueur2);
    }

    @Test
    void testGrilleInitialization() {
        assertNotNull(grille, "La grille ne doit pas être null");
        assertEquals(5, grille.getTaille(), "La taille de la grille doit être correcte");
        assertNotNull(grille.getCases(), "Les cases ne doivent pas être nulles");
        assertEquals(joueur1, grille.getJoueurActuel(), "Le premier joueur doit être le joueur 1");
        assertEquals(2, grille.getJoueurs().size(), "Il doit y avoir deux joueurs");
    }

    @Test
    void testSetJoueurActuel_InvalidPlayer() {
        Player fakePlayer = new Player("Fake", Case.EtatCase.BLEU, new HumanStrategy(), messageHandler);
        
        // Before setting
        assertEquals(joueur1, grille.getJoueurActuel(), "Le joueur actuel doit être joueur1");
        
        // Try setting an invalid player
        grille.setJoueurActuel(fakePlayer); 
        
        // The player should remain unchanged
        assertEquals(joueur1, grille.getJoueurActuel(), "Le joueur actuel ne doit pas changer");
    }

    @Test
    void testGetCase_ValidPosition() {
        Position pos = new Position(2, 3);
        assertNotNull(grille.getCase(pos), "Une case valide doit exister");
    }

    @Test
    void testGetCase_InvalidPosition() {
        Position pos = new Position(-1, -1);
        assertThrows(IllegalArgumentException.class, () -> grille.getCase(pos), "Doit lever une exception pour position invalide");
    }

    @Test
    void testGrilleTermineeFalse() {
        assertFalse(grille.estTerminee(), "La partie ne doit pas être terminée au début");
    }

    @Test
    void testGenererCasesDisponibles() {
        Map<Integer, Position> casesDisponibles = grille.genererCasesDisponibles();
        assertFalse(casesDisponibles.isEmpty(), "Il doit y avoir des cases disponibles");
    }

    @Test
    void testCopyGrille() {
        Grille copie = grille.copy();
        assertEquals(grille, copie, "La copie de la grille doit être identique à l'originale");
    }
}