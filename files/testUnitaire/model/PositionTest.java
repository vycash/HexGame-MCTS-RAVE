package livraison.testUnitaire.model;

import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    private Position position1;
    private Position position2;
    private Position position3;

    /**
     * Initialisation des objets avant chaque test.
     */
    @BeforeEach
    void setUp() {
        position1 = new Position(3, 4);
        position2 = new Position(3, 4);
        position3 = new Position(5, 7);
    }

    /**
     * Vérifie que les getters fonctionnent correctement.
     */
    @Test
    void testGetters() {
        assertEquals(3, position1.getX(), "La coordonnée X doit être 3");
        assertEquals(4, position1.getY(), "La coordonnée Y doit être 4");
    }

    /**
     * Vérifie que les setters mettent à jour correctement les coordonnées.
     */
    @Test
    void testSetters() {
        position1.setX(10);
        position1.setY(15);
        assertEquals(10, position1.getX(), "X doit être mis à jour à 10");
        assertEquals(15, position1.getY(), "Y doit être mis à jour à 15");
    }

    /**
     * Teste la méthode qui calcule la distance entre deux positions.
     */
    @Test
    void testDistance() {
        assertEquals(4, position1.distance(new Position(5, 2)), "La distance doit être 4");
        assertEquals(0, position1.distance(position2), "La distance doit être 0 (même position)");
        assertEquals(5, position1.distance(new Position(3, 9)), "La distance doit être 5 (même axe X)");
    }

    /**
     * Vérifie que la comparaison entre deux objets `Position` fonctionne.
     */
    @Test
    void testEquals() {
        assertEquals(position1, position2, "Les positions avec les mêmes coordonnées doivent être égales");
        assertNotEquals(position1, position3, "Deux positions différentes ne doivent pas être égales");
    }

    /**
     * Vérifie que la méthode `hashCode` est bien cohérente avec `equals()`.
     */
    @Test
    void testHashCode() {
        assertEquals(position1.hashCode(), position2.hashCode(), "Des objets égaux doivent avoir le même hashCode");
        assertNotEquals(position1.hashCode(), position3.hashCode(), "Des objets différents doivent avoir des hashCodes différents");
    }

    /**
     * Vérifie que `toString()` retourne bien la bonne représentation textuelle.
     */
    @Test
    void testToString() {
        assertEquals("(3, 4)", position1.toString(), "La représentation en chaîne doit être '(3, 4)'");
        assertEquals("(5, 7)", position3.toString(), "La représentation en chaîne doit être '(5, 7)'");
    }
}