package model;

import java.util.*;

import model.Case.EtatCase;
import model.player.Player;

/**
 * Représente une grille pour le jeu Hex, composée de cases hexagonales.
 */
public class Grille {

    private final int taille;
    private final Case[][] cases;
    private Player joueurActuel;
    private final List<Player> joueurs;

    /**
     * Constructeur pour initialiser une grille de taille donnée avec deux joueurs.
    *
    * @param taille  La taille de la grille.
    * @param joueur1 Le premier joueur.
    * @param joueur2 Le second joueur.
    */
   public Grille(int taille, Player joueur1, Player joueur2) {
        this.taille = taille;
        this.cases = new Case[taille][taille + taille - 1];
        this.joueurs = Arrays.asList(joueur1, joueur2);
        this.joueurActuel = joueur1;
        initialiserEtAttribuerVoisins();
    }

    /**
     * Surcharge pour créer une Grille par défaut (taille 14).
     */
    public Grille(Player joueur1, Player joueur2) {
        this(14, joueur1, joueur2);
    }

    /**
     * Retourne la taille de la grille.
     *
     * @return La taille de la grille.
     */
    public int getTaille() {
        return this.taille;
    }

    /**
     * Retourne le joueur actuellement en train de jouer.
     *
     * @return Le joueur actuel.
     */
    public Player getJoueurActuel() {
        return joueurActuel;
    }

    /**
     * Modifie le joueur actuellement en train de jouer, s'il fait partie de la liste.
     * 
     * @param p le joueur à modifier.
     */
    public void setJoueurActuel(Player p) {
        if (joueurs.contains(p)) {
            this.joueurActuel = p;
        }
    }

    /**
     * Retourne le tableau de Cases qui composent la grille.
     *
     * @return Le tableau de Cases.
     */
    public Case[][] getCases() {
        return this.cases;
    }

    /**
     * Retourne la liste des joueurs qui joue
     * 
     * @return Liste des joueurs.
     */
    public List<Player> getJoueurs() {
        return joueurs;
    }

    /**
     * Retourne une case spécifique à partir de ses coordonnées.
     *
     * @param position La position de la case.
     * @return La case à cette position, ou null si hors des limites.
     */
    public Case getCase(Position position) {
        if (!estDansGrille(position)) throw new IllegalArgumentException("la position donnée en parametre n'est pas valide "+position);
        return cases[position.getX()][position.getY()];
    }

    /**
     * Surcharge pour retourner une case via coordonnées entières (x, y).
     *
     * @param x La position x de la case.
     * @param y La position y de la case.
     * @return La case correspondant aux coordonnées données.
     */
    public Case getCase(int x, int y) {
        return getCase(new Position(x, y));
    }

    /**
     * Initialise la grille en instanciant toutes les Cases et attribue leurs voisins.
     */
    private void initialiserEtAttribuerVoisins() {
       
    	// Première passe : créer toutes les Cases
        for (int x = 0; x < taille; x++) {
            for (int y = x; y < taille + x; y++) {
                cases[x][y] = new Case(new Position(x, y));
            }
        }

        // Deuxième passe : lier les voisins pour chaque Case
        for (int x = 0; x < taille; x++) {
            for (int y = x; y < taille + x; y++) {
                Case current = cases[x][y];
                for (Case.Direction direction : Case.Direction.values()) {
                    Position voisinPos = direction.getPositionVoisine(current.getPosition());
                    if (estDansGrille(voisinPos)) {
                        Case voisin = getCase(voisinPos);
                        current.ajouterVoisin(direction, voisin);
                    }
                }
            }
        }
    }

    /**
     * Indique si la partie est terminée (victoire BLEU, victoire ROUGE, ou plus de coups disponibles).
     *
     * @return true si la partie est terminée, false sinon.
     */
    public boolean estTerminee() {
        return verifierVictoire(EtatCase.BLEU)
            || verifierVictoire(EtatCase.ROUGE)
            || this.genererCasesDisponibles().isEmpty();
    }

    /**
     * Vérifie si une position est dans les limites "hex" de la grille.
     *
     * @param position La position à vérifier.
     * @return true si la position est valide, false sinon.
     */
    public boolean estDansGrille(Position position) {
        if (position == null) throw new IllegalArgumentException("Position nulle donnée en parametres");
        int x = position.getX();
        int y = position.getY();
        return x >= 0 && y >= x && x < taille && y < taille + taille - 1;
    }

   
   /**
 * Potential Issues and Debugging Steps:
 * 1. Make sure the BFS logic for ROUGE is seeded with the correct starting row (x=0).
 * 2. Verify that the adjacency (voisins) is set up correctly for each Case, so the search can traverse all connected ROUGE cells.
 * 3. Ensure that ROUGE cells actually occupy the top row in your simulation so they can be discovered by the BFS.
 * 4. Confirm that the final win check (x == taille - 1 for ROUGE) matches how your board and neighbors are laid out.
 * 
 * Below is one approach to verify BFS for ROUGE:
 */ 
    
    /**
     * Vérifie la victoire pour un joueur (par exemple, BLEU connecte gauche/droite, ROUGE connecte haut/bas).
     *
     * @param joueur Le joueur dont on veut vérifier la victoire.
     * @return true si ce joueur a gagné, sinon false.
     */
    public boolean verifierVictoire(Player joueur) {    
    	
    	Set<Case> visites = new HashSet<>();
        Queue<Case> aExplorer = new LinkedList<>();

       // Cases de départ pour BLEU: colonne gauche (x, x)
       // Cases de départ pour ROUGE: ligne du haut (0, y) pour y = 0..(taille-1)
       if (joueur.getCouleur() == Case.EtatCase.BLEU) {
         for (int x = 0; x < taille; x++) {
            Case caseDepart = cases[x][x];
            if (caseDepart.getOccupe() == joueur.getCouleur()) {
                aExplorer.add(caseDepart);
            }
        }
    } else if (joueur.getCouleur() == Case.EtatCase.ROUGE) {
        for (int y = 0; y < taille; y++) {
            Case caseDepart = cases[0][y];
            if (caseDepart.getOccupe() == joueur.getCouleur()) {
                aExplorer.add(caseDepart);
            }
        }
    }

    // Parcours en largeur (BFS)
    while (!aExplorer.isEmpty()) {
        Case current = aExplorer.poll();
        if (visites.contains(current)) {
            continue;
        }
        visites.add(current);

        // Condition de victoire BLEU: atteindre (x, x + (taille - 1))
        if (joueur.getCouleur() == Case.EtatCase.BLEU
            && current.getPosition().getY() == current.getPosition().getX() + (taille - 1)) {
            return true;
        }
        // Condition de victoire ROUGE: atteindre la "dernière" ligne x = taille - 1
        else if (joueur.getCouleur() == Case.EtatCase.ROUGE
                 && current.getPosition().getX() == (taille - 1)) {
            return true;
        }

        // Ajouter les voisins de la même couleur
        for (Case voisin : current.getVoisins().values()) {
            if (voisin != null
                && voisin.getOccupe() == joueur.getCouleur()
                && !visites.contains(voisin)) {
                aExplorer.add(voisin);
            }
        }
    }
    return false;
}

    /**
     * Vérifie la victoire pour une couleur directement (cherche le joueur qui a cette couleur).
     */
    public boolean verifierVictoire(EtatCase couleur) {
        for (Player j : joueurs) {
            if (j.getCouleur() == couleur) {
                return verifierVictoire(j);
            }
        }
        throw new IllegalArgumentException("joueur inexistant");
    }

    /**
     * Génère une map des cases disponibles (vides) avec un identifiant numérique.
     *
     * @return Une map associant un numéro à la position de chaque case vide.
     */
    public Map<Integer, Position> genererCasesDisponibles() {
        Map<Integer, Position> casesDisponibles = new HashMap<>();
        int numero = 0;

        for (int x = 0; x < taille; x++) {
            for (int y = x; y < taille + x; y++) {
                Case current = cases[x][y];
                if (current.estLibre()) {
                    casesDisponibles.put(numero, current.getPosition());
                    numero++;
                }
            }
        }
        return casesDisponibles;
    }

    /**
     * Convertit un identifiant de case disponible en sa position.
     *
     * @param numero Le numéro identifiant la case.
     * @param casesDisponibles La map des cases disponibles.
     * @return La position correspondante au numéro fourni.
     */
    public Position convertirNumeroEnPosition(int numero, Map<Integer, Position> casesDisponibles) {
        return casesDisponibles.get(numero);
    }

    /**
     * Affiche la grille en console. (Sert de debug/visualisation)
     */
    public void afficherGrille() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < taille; x++) {
            // Espaces pour garder la forme hex
            sb.append(" ".repeat(x));
            for (int y = x; y < taille + x; y++) {
                sb.append(cases[x][y]).append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    /**
     * Crée une copie de la grille actuelle, incluant l'occupation des cases et le joueur courant.
     *
     * @return Une nouvelle instance de Grille identique à l'originale.
     */
    public Grille copy() {
        // Créer une nouvelle grille (ce constructeur appelle initialiserEtAttribuerVoisins())
        Grille copie = new Grille(this.getTaille(), this.joueurs.get(0), this.joueurs.get(1));

        // Copier l'occupation de chaque case
        for (int x = 0; x < taille; x++) {
            for (int y = x; y < taille + x; y++) {
                Case originalCase = this.getCase(x, y);
                Case copiedCase = copie.getCase(x, y);
                copiedCase.setOccupe(originalCase.getOccupe());
            }
        }

        // Appliquer le même joueurActuel
        copie.setJoueurActuel(this.getJoueurActuel());

        return copie;
    }

    /**
     * Réinitialise l'occupation de toutes les cases de la grille à VIDE.
     */
    public void clear(){
    	for (int x = 0; x < taille; x++) {
            for (int y = x; y < taille + x; y++) {
                this.getCase(x, y).setOccupe(Case.EtatCase.VIDE);
            }
        }
    }

    /**
     * Vérifie si deux grilles sont identiques (même occupation case par case).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grille g = (Grille) o;
        Case[][] otherCases = g.getCases();
        for (int x = 0; x < taille; x++) {
            for (int y = x; y < taille + x; y++) {
                if (!otherCases[x][y].equals(this.cases[x][y])) {
                    return false;
                }
            }
        }
        return true;
    }

    
}