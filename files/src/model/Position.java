package model;

import java.util.Objects;

/**
 * Représente une position dans une grille avec des coordonnées x et y.
 */
public class Position {
    private int x;
    private int y;

    /**
     * Constructeur pour initialiser une position.
     *
     * @param x La coordonnée x
     * @param y La coordonnée y
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retourne la coordonnée x.
     *
     * @return La coordonnée x
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnée y.
     *
     * @return La coordonnée y
     */
    public int getY() {
        return y;
    }

    /**
     * Modifie la coordonnée x.
     *
     * @param x La nouvelle valeur de x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Modifie la coordonnée y.
     *
     * @param y La nouvelle valeur de y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Calcule la distance entre deux positions.
     *
     * @param pos Une autre position
     * @return La distance Manhattan
     */
    public int distance(Position pos) {
        return Math.abs(this.x - pos.x) + Math.abs(this.y - pos.y);
    }

    /**
     * Compare deux positions pour vérifier si elles sont égales.
     *
     * @param obj L'objet à comparer
     * @return true si les positions ont les mêmes coordonnées, false sinon
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    /**
     * Génère un hashCode basé sur les coordonnées.
     *
     * @return Le hashCode de la position
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Retourne une représentation textuelle de la position.
     *
     * @return Une chaîne de caractères représentant la position
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
