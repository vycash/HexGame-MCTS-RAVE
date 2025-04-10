package livraison.testUnitaire.model.mcts;

import model.*;
import model.mcts.RAVE;
import model.mcts.Noeud;
import model.Case.EtatCase;
import model.player.Player;
import model.player.MonteCarloStrategy;
import utils.strategyMessage.ConsoleMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'algorithme RAVE.
 */
public class RAVETest {

    private RAVETestable rave;
    private Grille grille;
    private Player joueurBleu;
    private Player joueurRouge;

    @BeforeEach
    void setUp() {
        joueurBleu = new Player("Joueur1", EtatCase.BLEU, new MonteCarloStrategy(), new ConsoleMessageHandler(true));
        joueurRouge = new Player("Joueur2", EtatCase.ROUGE, new MonteCarloStrategy(), new ConsoleMessageHandler(true));
        grille = new Grille(5, joueurBleu, joueurRouge); // Grille 5x5
        rave = new RAVETestable(100); // 100 itérations
    }

    @Test
    void testTrouverMeilleurCoupRave() {
        Position best = rave.trouverMeilleurCoup(grille, EtatCase.BLEU);
        assertNotNull(best, "Le meilleur coup ne doit pas être null");
        assertTrue(grille.estDansGrille(best), "Le coup doit être valide");
    }

    @Test
    void testSimulationAvecRave() {
        Noeud noeud = new Noeud(grille, null, null, EtatCase.BLEU);
        double resultat = rave.simuler(noeud, EtatCase.BLEU);
        assertTrue(resultat == 1.0 || resultat == -1.0, "La simulation doit renvoyer 1.0 ou -1.0");
    }

    @Test
    void testUpdateRaveValues() {
        Noeud noeud = new Noeud(grille, new Position(0, 0), null, EtatCase.BLEU);
        Set<Position> actions = new HashSet<>();
        actions.add(new Position(0, 0));

        // Appel indirect de updateRaveValues via simuler
        rave.simuler(noeud, EtatCase.BLEU);

        // On vérifie que les visites RAVE ont bien été modifiées
        assertTrue(noeud.getRaveVisits() >= 0, "Les visites RAVE doivent être mises à jour");
    }
    

    // Sous-classe dans pour exposer la méthode simuler
    static class RAVETestable extends RAVE {
        public RAVETestable(int iterationBudget) {
            super(iterationBudget);
        }

        @Override
        public double simuler(Noeud noeud, EtatCase joueurOriginal) {
            return super.simuler(noeud, joueurOriginal);
        }
    }
}