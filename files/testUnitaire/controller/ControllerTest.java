package livraison.testUnitaire.controller;

import controller.Controller;
import model.Case;
import model.Grille;
import model.Position;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vue.GamePanel;
import vue.Page;
import vue.Panels;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    private Controller controller;
    private Panels panels; 

    @BeforeEach
    void setUp() {
        controller = new Controller();
        panels = new Panels(controller, 1, 2, new GamePanel(controller));
        
        // Simuler les choix de joueurs
        String[] choix = {"Aléatoire", "Robot/MCTS"};
        panels.getChoixJoueurs()[0] = choix[0];
        panels.getChoixJoueurs()[1] = choix[1];
        
    }

    @Test
    void testInitialiserJoueursCreeDeuxJoueurs() {
        controller.initialiserJoueurs(panels);

        Grille grille = controller.getGrille();
        assertNotNull(grille, "La grille ne doit pas être null");
        Player joueurActuel = grille.getJoueurActuel();
        assertNotNull(joueurActuel, "Le joueur actuel ne doit pas être null");
        assertEquals(Case.EtatCase.BLEU, joueurActuel.getCouleur(), "Le joueur actuel doit être BLEU");
    }

    @Test
    void testDemarrerPartieSansJoueurAfficheAlerte() {
        
        assertDoesNotThrow(() -> controller.demarrerPartie());
    }

    @Test
    void testJouerTourAppliqueUnCoupSiPossible() {
        controller.initialiserJoueurs(panels);
        Grille grille = controller.getGrille();
        Player joueur = grille.getJoueurActuel();

        Position moveBefore = joueur.getLastMove();
        controller.jouerTour();
        Position moveAfter = joueur.getLastMove();

        assertNotEquals(moveBefore, moveAfter, "Le joueur devrait avoir joué un coup");
    }

    @Test
    void testNavigationVersAccueil() {
        assertDoesNotThrow(() -> controller.naviguer(Page.ACCEUIL));
    }

    @Test
    void testNavigationVersJeu() {
        assertDoesNotThrow(() -> controller.naviguer(Page.JEU));
    }
}