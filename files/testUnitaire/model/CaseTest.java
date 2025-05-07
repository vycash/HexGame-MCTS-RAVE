package livraison.testUnitaire.model;

import model.Case;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CaseTest {

    private Case testCase;
    private Case voisinCase;

    @BeforeEach
    void setUp() {
        testCase = new Case(new Position(3, 3));
        voisinCase = new Case(new Position(3, 4));
    }

    @Test
    void testInitialCaseState() {
        assertNotNull(testCase, "L'instance de Case ne doit pas être null");
        assertEquals(Case.EtatCase.VIDE, testCase.getOccupe(), "Une nouvelle case doit être vide");
    }

    @Test
    void testSetOccupe() {
        testCase.setOccupe(Case.EtatCase.BLEU);
        assertEquals(Case.EtatCase.BLEU, testCase.getOccupe(), "La case doit être occupée par BLEU");

        testCase.setOccupe(Case.EtatCase.ROUGE);
        assertEquals(Case.EtatCase.ROUGE, testCase.getOccupe(), "La case doit être occupée par ROUGE");

        testCase.setOccupe(Case.EtatCase.VIDE);
        assertEquals(Case.EtatCase.VIDE, testCase.getOccupe(), "La case doit être redevenue vide");
    }

    @Test
    void testEstLibre() {
        assertTrue(testCase.estLibre(), "Une nouvelle case doit être libre");

        testCase.setOccupe(Case.EtatCase.BLEU);
        assertFalse(testCase.estLibre(), "Une case occupée ne doit pas être libre");
    }

    @Test
    void testAjoutVoisins() {
        testCase.ajouterVoisin(Case.Direction.DROITE, voisinCase);
        Map<Case.Direction, Case> voisins = testCase.getVoisins();

        assertEquals(1, voisins.size(), "Un voisin doit avoir été ajouté");
        assertEquals(voisinCase, voisins.get(Case.Direction.DROITE), "Le voisin doit être correct");
    }

    @Test
    void testGetPosition() {
        assertEquals(new Position(3, 3), testCase.getPosition(), "La position doit être correcte");
    }

    @Test
    void testToString() {
        testCase.setOccupe(Case.EtatCase.VIDE);
        assertEquals("\u2B22", testCase.toString(), "L'affichage doit correspondre à une case vide");

        testCase.setOccupe(Case.EtatCase.BLEU);
        assertTrue(testCase.toString().contains("\u001B[34m"), "La case BLEU doit être en bleu");

        testCase.setOccupe(Case.EtatCase.ROUGE);
        assertTrue(testCase.toString().contains("\u001B[31m"), "La case ROUGE doit être en rouge");
    }

    @Test
    void testEquals() {
        Case sameStateCase = new Case(new Position(4, 4));
        sameStateCase.setOccupe(Case.EtatCase.VIDE);
        assertEquals(testCase, sameStateCase, "Deux cases vides doivent être égales");

        sameStateCase.setOccupe(Case.EtatCase.BLEU);
        assertNotEquals(testCase, sameStateCase, "Une case vide et une case occupée ne doivent pas être égales");
    }

    @Test
    void testEtatCaseOpposite() {
        assertEquals(Case.EtatCase.ROUGE, Case.EtatCase.opposite(Case.EtatCase.BLEU), "L'opposé de BLEU doit être ROUGE");
        assertEquals(Case.EtatCase.BLEU, Case.EtatCase.opposite(Case.EtatCase.ROUGE), "L'opposé de ROUGE doit être BLEU");
        assertEquals(Case.EtatCase.VIDE, Case.EtatCase.opposite(Case.EtatCase.VIDE), "L'opposé de VIDE doit rester VIDE");
    }

    @Test
    void testGetPositionVoisine() {
        Position basePos = new Position(3, 3);
        Position expectedPos = Case.Direction.DROITE.getPositionVoisine(basePos);
        assertEquals(new Position(3, 4), expectedPos, "Le déplacement vers la droite doit être correct");
    }
}