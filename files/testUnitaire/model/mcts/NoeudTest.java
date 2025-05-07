package livraison.testUnitaire.model.mcts;

import model.*;
import model.mcts.Noeud;
import model.Case.EtatCase;
import model.player.Player;
import model.player.MonteCarloStrategy;
import utils.strategyMessage.ConsoleMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class NoeudTest {

    private Grille grille;
    private Player joueurBleu;
    private Player joueurRouge;
    private Noeud noeud;

    @BeforeEach
    void setUp() {
        joueurBleu = new Player("Bleu", EtatCase.BLEU, new MonteCarloStrategy(), new ConsoleMessageHandler(true));
        joueurRouge = new Player("Rouge", EtatCase.ROUGE, new MonteCarloStrategy(), new ConsoleMessageHandler(true));
        grille = new Grille(5, joueurBleu, joueurRouge);
        noeud = new Noeud(grille, new Position(2, 2), null, EtatCase.BLEU);
    }

    @Test
    void testAjoutEtAccesEnfants() {
        Noeud enfant = new Noeud(grille, new Position(1, 1), noeud, EtatCase.ROUGE);
        noeud.addEnfant(enfant);
        List<Noeud> enfants = noeud.getEnfants();

        assertEquals(1, enfants.size());
        assertTrue(enfants.contains(enfant));
    }

    @Test
    void testStatistiquesMCTS() {
        noeud.addWins(3);
        noeud.addLosses(1);
        noeud.incrementVisits();

        assertEquals(3, noeud.getWins());
        assertEquals(1, noeud.getLosses());
        assertEquals(1, noeud.getVisits());
        assertEquals(3.0, noeud.getRatio());
    }

    @Test
    void testStatistiquesRAVE() {
        noeud.setRaveWins(2.0);
        noeud.addRaveVisits();

        assertEquals(1, noeud.getRaveVisits());
        assertEquals(2.0, noeud.getRaveWins());
        assertTrue(noeud.amaf() > 0);
    }

    @Test
    void testIsLeafEtIsTerminal() {
        assertTrue(noeud.isLeaf(), "Un nouveau nœud sans enfants est une feuille");
        assertFalse(noeud.isTerminal(), "La partie ne devrait pas encore être terminée");
    }

    @Test
    void testToStringEtToStringRave() {
        String normal = noeud.toString();
        String rave = noeud.toStringRave();

        assertTrue(normal.contains("Move"));
        assertTrue(rave.contains("combinedValue"));
    }

    @Test
    void testEqualsEtHashCode() {
        Noeud n1 = new Noeud(grille, new Position(0, 0), null, EtatCase.BLEU);
        Noeud n2 = new Noeud(grille, new Position(0, 0), null, EtatCase.ROUGE); // même position

        assertEquals(n1, n2, "Deux nœuds avec la même position doivent être égaux");
        assertEquals(n1.hashCode(), n2.hashCode(), "Le hashCode doit aussi être identique");
    }
}