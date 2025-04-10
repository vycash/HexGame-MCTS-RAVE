package livraison.testUnitaire.model;

import model.*;
import model.player.*;
import utils.strategyMessage.ConsoleMessageHandler;
import utils.strategyMessage.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrchestrateurTest {

    private Orchestrateur orchestrateur;
    private MessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        // Création d'un message handler fictif pour éviter d'afficher dans la console
        messageHandler = new ConsoleMessageHandler(false);

        // Initialisation d'un orchestrateur avec une grille de taille 5 et en mode interactif
        orchestrateur = new Orchestrateur(5, messageHandler, false);
    }

    @Test
    void testInitialisation() {
        // Vérifier que la grille et les joueurs sont bien initialisés
        assertNotNull(orchestrateur.getGrille(), "La grille ne doit pas être nulle");
        List<Player> joueurs = orchestrateur.getJoueurs();
        assertNotNull(joueurs, "La liste des joueurs ne doit pas être nulle");
        assertEquals(2, joueurs.size(), "Il doit y avoir exactement 2 joueurs");
    }

    @Test
    void testChoixJoueurQuiCommence() {
        // Vérifier que le joueur qui commence est bien un des joueurs existants
        Player joueur = orchestrateur.getJoueurQuiCommence();
        assertTrue(orchestrateur.getJoueurs().contains(joueur), "Le joueur qui commence doit être parmi les joueurs");
    }

    @Test
    void testPasserAuJoueurSuivant() {
        List<Player> joueurs = orchestrateur.getJoueurs();
        Player premierJoueur = joueurs.get(0);
        Player secondJoueur = joueurs.get(1);

        // Vérifier que l'alternance fonctionne correctement
        assertEquals(secondJoueur, orchestrateur.passerAuJoueurSuivant(premierJoueur), "Le joueur suivant doit être le second joueur");
        assertEquals(premierJoueur, orchestrateur.passerAuJoueurSuivant(secondJoueur), "Le joueur suivant doit être le premier joueur");
    }

    @Test
    void testJeuSeTermine() {
        Grille grille = orchestrateur.getGrille();

        // Simuler une partie rapide où un joueur gagne immédiatement
        grille.getCase(0, 0).setOccupe(Case.EtatCase.BLEU);
        grille.getCase(0, 1).setOccupe(Case.EtatCase.BLEU);
        grille.getCase(0, 2).setOccupe(Case.EtatCase.BLEU);
        grille.getCase(0, 3).setOccupe(Case.EtatCase.BLEU);
        grille.getCase(0, 4).setOccupe(Case.EtatCase.BLEU);

        // Vérifier que la partie est considérée comme terminée
        assertTrue(grille.estTerminee(), "La partie doit être terminée après une victoire");
    }
}
