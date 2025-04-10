package model;

import java.util.*;

/**
 * Représente une case hexagonale dans la grille du jeu Hex.
 * Chaque case est associée à une position et à un état (occupée ou non), 
 * et une liste de voisins définis par des directions spécifiques.
 */
public class Case {

    /**
     * Position de la case dans la grille.
     */
    private Position position;

    /**
     * L'état d'occupation de la case.
     * Peut être VIDE, soit occupée par un joueur BLEU ou ROUGE.
     */
    private EtatCase occupe;

    /**
     * Une map des voisins directs de la case,
     * où la clé est la direction et la valeur est la case voisine.
     */
    private Map<Direction, Case> voisins;

    /**
     * Enumération représentant l'état d'occupation d'une case.
     */
    public enum EtatCase {
        /** La case est vide. */
        VIDE, 
        /** La case est occupée par le joueur bleu. */
        BLEU, 
        /** La case est occupée par le joueur rouge. */
        ROUGE;

        /**
         * Retourne l'état opposé.
         *
         * @param e l'état actuel
         * @return l'état opposé (ROUGE si e est BLEU, BLEU si e est ROUGE, VIDE sinon)
         */
        public static EtatCase opposite(EtatCase e){
            if ( e == VIDE){
                return VIDE;
            }
            return (e == BLEU)? ROUGE : BLEU;
        }

        @Override
        public String toString(){
            return name();
        }
    }

    /**
     * Enumération représentant les directions possibles dans une grille hexagonale.
     * Chaque direction est définie par des décalages relatifs en x et y.
     */
    public enum Direction {
        /** Direction vers le nord-ouest. */
        NORD_OUEST(-1, -1),
        /** Direction vers le nord-est. */
        NORD_EST(-1, 0),
        /** Direction vers le sud-ouest. */
        SUD_OUEST(1, 0),
        /** Direction vers le sud-est. */
        SUD_EST(1, 1),
        /** Direction vers la gauche. */
        GAUCHE(0, -1),
        /** Direction vers la droite. */
        DROITE(0, 1);

        private final int dx;
        private final int dy;

        /**
         * Constructeur pour une direction spécifique.
         *
         * @param dx le décalage horizontal
         * @param dy le décalage vertical
         */
        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        /**
         * Calcule la position voisine selon la direction actuelle.
         *
         * @param position la position de départ
         * @return une nouvelle position après application du décalage
         */
        public Position getPositionVoisine(Position position) {
            return new Position(position.getX() + dx, position.getY() + dy);
        }
    }

    /**
     * Constructeur pour créer une case avec une position spécifique.
     * Par défaut, la case est vide et n'a pas de voisins définis.
     *
     * @param position La position de la case
     */
    public Case(Position position) {
        this.position = position;
        this.occupe = EtatCase.VIDE;
        this.voisins = new HashMap<>();
    }

    /**
     * Retourne la position de la case.
     *
     * @return La position de la case
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Retourne l'état d'occupation de la case.
     *
     * @return L'état actuel de la case
     */
    public EtatCase getOccupe() {
        return occupe;
    }

    /**
     * Modifie l'état d'occupation de la case.
     *
     * @param occupe Le nouvel état de la case
     */
    public void setOccupe(EtatCase occupe) {
        this.occupe = occupe;
    }

    /**
     * Retourne les voisins de la case sous forme d'une map.
     *
     * @return Une map où les clés sont les directions et les valeurs sont les cases voisines
     */
    public Map<Direction, Case> getVoisins() {
        return voisins;
    }

    /**
     * Ajoute un voisin à la case dans une direction spécifique.
     *
     * @param direction La direction du voisin
     * @param voisin La case voisine
     */
    public void ajouterVoisin(Direction direction, Case voisin) {
        this.voisins.put(direction, voisin);
    }

    /**
     * Vérifie si la case est libre (non occupée).
     *
     * @return true si la case est vide, false sinon
     */
    public boolean estLibre() {
        return this.occupe == EtatCase.VIDE;
    }

    /**
     * Retourne une représentation textuelle de la case.
     * Utilisé pour l'affichage en console.
     *
     * @return Un hexagone vide, bleu ou rouge selon l'état de la case
     */
    @Override
    public String toString() {
        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[34m";
        final String RED = "\u001B[31m";
        final String HEX = "\u2B22"; 

        switch (occupe) {
            case BLEU:
                return BLUE + HEX + RESET; 
            case ROUGE:
                return RED + HEX + RESET; 
            default:
                return HEX; 
        }
    }

    @Override
    public boolean equals(Object o ){
        Case c = (Case) o;
        return this.occupe == c.getOccupe();
    }
}
