package livraison.testUnitaire.model.mcts;

import model.*;
import model.mcts.MCTS;
import model.mcts.Noeud;
import model.Case.EtatCase;
import model.player.Player;
import model.player.MonteCarloStrategy;
import utils.strategyMessage.ConsoleMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe MCTS.
 */
public class MCTSTest {

    private MCTSForTest mcts;
    private Grille grille;
    private Player joueurBleu;
    private Player joueurRouge;

    @BeforeEach
    void setUp() {
        joueurBleu = new Player("Joueur1", EtatCase.BLEU, new MonteCarloStrategy(), new ConsoleMessageHandler(true));
        joueurRouge = new Player("Joueur2", EtatCase.ROUGE, new MonteCarloStrategy(), new ConsoleMessageHandler(true));
        grille = new Grille(5, joueurBleu, joueurRouge); // Grille de taille 5x5
        mcts = new MCTSForTest(100); // 100 iterations pour tester
    }

    @Test
    void testTrouverMeilleurCoup() {
        Position meilleurCoup = mcts.trouverMeilleurCoup(grille, EtatCase.BLEU);
        assertNotNull(meilleurCoup, "Le meilleur coup ne doit pas être null");
        assertTrue(grille.estDansGrille(meilleurCoup), "Le coup trouvé doit être dans la grille");
    }

    @Test
    void testSelectionner() {
        Noeud racine = new Noeud(grille, null, null, EtatCase.BLEU);
        Noeud selection = mcts.selectionner(racine);
        assertNotNull(selection, "La sélection ne doit pas être null");
    }

    @Test
    void testDevelopper() {
        Noeud noeud = new Noeud(grille, null, null, EtatCase.BLEU);
        int avant = noeud.getEnfants().size();
        mcts.developper(noeud);
        int apres = noeud.getEnfants().size();
        assertTrue(apres > avant, "Le développement doit ajouter un enfant");
    }

    @Test
    void testSimuler() {
        Noeud noeud = new Noeud(grille, null, null, EtatCase.BLEU);
        double resultat = mcts.simuler(noeud, EtatCase.BLEU);
        assertTrue(resultat == 1.0 || resultat == -1.0, "Le résultat doit être 1.0 ou -1.0");
    }

    @Test
    void testRetropropager() {
        Noeud noeud = new Noeud(grille, null, null, EtatCase.BLEU);
        mcts.retropropager(noeud, 1.0);
        assertEquals(1, noeud.getWins(), "Le nombre de victoires doit être mis à jour");
        assertEquals(1, noeud.getVisits(), "Le nombre de visites doit être incrémenté");
    }

    // Sous-Classe fille pour exposer les méthodes protected
    static class MCTSForTest extends MCTS {
        public MCTSForTest(int budget) {
            super(budget);
        }

        @Override public Noeud selectionner(Noeud n) { return super.selectionner(n); }
        @Override public void developper(Noeud n) { super.developper(n); }
        @Override public double simuler(Noeud n, EtatCase j) { return super.simuler(n, j); }
        @Override public void retropropager(Noeud n, double r) { super.retropropager(n, r); }
    }
}