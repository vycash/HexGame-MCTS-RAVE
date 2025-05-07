package model.mcts;

import java.util.*;
import model.*;
import model.Case.EtatCase;
import config.Constants;

/**
 * Implémente l'algorithme Monte Carlo Tree Search.
 */
public class MCTS {

    private Noeud root;
    protected static final Random RANDOM = new Random();
    private int iterationBudget;
    private boolean experimentation;


    // Constructor with custom iteration budget
    public MCTS(int iterationBudget,boolean experimentation) {
        this.iterationBudget = iterationBudget;
        this.experimentation = experimentation;
    }
    public MCTS(int iterationBudget){
        this(iterationBudget,false);
    }
    public MCTS(boolean experimentation){
        this(Constants.ITERATION_BUDGET,experimentation);
    }
    // Default constructor using the iteration budget from Constants
    public MCTS() {
        this(Constants.ITERATION_BUDGET,false);
    }


    /**
     * Retourne le nombre d'itérations alloué pour l'exécution de l'algorithme MCTS.
     *
     * @return le budget d'itération
     */
    public int getIterationBudget() { return this.iterationBudget; }
    
    
    
    /**
     * Indique si l'algorithme est en mode expérimentation.
     *
     * @return true si en expérimentation, false sinon.
     */
    public boolean isExperimentation() { return this.experimentation; }

    
    
    
    /**
     * Trouve le meilleur coup à partir d'un état donné.
     *
     * @param grille  L'état actuel du jeu.
     * @param couleur La couleur du joueur qui doit jouer.
     * @return Le meilleur coup trouvé.
     */
    public Position trouverMeilleurCoup(Grille grille, Case.EtatCase couleur) {
        // Start the timer
        long startTime = System.currentTimeMillis();
    
        // Vérifie si l'état actuel existe déjà dans l'arbre
        Noeud searchResult = findChild(root, grille);
        Noeud racine = (searchResult == null) ? new Noeud(grille, null, null, couleur) : searchResult;
    
        // Lance les itérations MCTS
        for (int i = 0; i < iterationBudget; i++) {
            // 1. Sélection
            Noeud noeud = selectionner(racine);
            
            // 2. Développement
            if (noeud.getVisits() > 0) {
                developper(noeud);
                // Récupère le nouvel enfant (un seul, si créé)
                Noeud unvisitedChild = bestChild(noeud);
                if (unvisitedChild != null) {
                    noeud = unvisitedChild;
                }
            }
            
            // 3. Simulation
            double resultat = simuler(noeud, couleur);
            
            // 4. Rétropropagation
            retropropager(noeud, resultat);
        }
    
        // Détermine le meilleur coup et met à jour la racine
        Position bestMove = bestMove(racine);
        updateRootAfterMove(racine, bestMove);
    
        // Stop the timer and calculate elapsed time
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
    
        // Print the time taken
        if ( experimentation == false ) System.out.println("Meilleur coup trouvé après " + elapsedTime + " millisecondes.");
    
        return bestMove;
    }

    // methode pour trouver un noeud avec un état précis à partir d'un noeud précis
    protected Noeud findChild(Noeud currentRoot, Grille targetState) {
        if (currentRoot == null) return null;
        for (Noeud child : currentRoot.getEnfants()) {
            if (child.getGrille().equals(targetState)) {
                return child;
            }
            Noeud deeperMatch = findChild(child, targetState);
            if (deeperMatch != null) return deeperMatch;
        }
        return null;
    }
    
    // methode qui affiche tous les coups à partir d'un noeud càd affiche tous les enfants d'un noeud
    protected void afficherTousLesCoups(Noeud noeud) {
        // Afficher tous les coups possibles
        List<Noeud> enfants = noeud.getEnfants();
        System.out.println("Liste des moves possibles:");
        for (int i = 0; i < enfants.size(); i++) {
            System.out.println(enfants.get(i));
        }
    }
    
    // methode qui renvoie le meilleur enfant, l'enfant qui a le plus haut win rate ratio ( victoirs/défaites )
    protected Position bestMove(Noeud noeud) {
        List<Noeud> enfantsTries = new ArrayList<>(noeud.getEnfants());
        
        // Afficher tous les coups possibles
        
        // Obtenir le meilleur coup
        Noeud meilleur = enfantsTries.get(0);
        for ( Noeud child : enfantsTries ){
            if (child.getRatio() > meilleur.getRatio()){
                meilleur = child;
            }
        }
        
        if ( experimentation == false ){
            afficherTousLesCoups(noeud);
            System.out.println("\nBest move : " + meilleur + " avec "+iterationBudget+" itérations");
        }
        return meilleur.getMove();
    }


    // methode qui met à jour la racine après avoir choisi le coup à jouer
    protected void updateRootAfterMove(Noeud currentRoot, Position move) {
        for (Noeud child : currentRoot.getEnfants()) {
            if (child.getMove().equals(move)) {
                this.root = child;
                this.root.setParent(null);
                break;
            }
        }
    }

    /**
     * Sélectionne un nœud en descendant dans l'arbre jusqu'à rencontrer un nœud terminal ou non complètement développé.
     *
     * @param noeud Le nœud de départ pour la sélection.
     * @return Le nœud sélectionné.
     */
    protected Noeud selectionner(Noeud noeud) {
        while (!noeud.isTerminal() && noeud.isFullyExpanded()) {
            noeud = bestChild(noeud);
        }
        return noeud;
    }

    // renvoie l'enfant qui maximise sa valeur UCT d'un noeud
    protected Noeud bestChild(Noeud noeud) {
        if (noeud.isLeaf()) {
            return noeud;
        }

        Noeud meilleurEnfant = null;
        double meilleurScore = -1;

        for (Noeud enfant : noeud.getEnfants()) {
            double score = enfant.UCT();
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurEnfant = enfant;
            }
        }
        return meilleurEnfant;
    }


    /**
     * Développe un nœud en ajoutant un seul nouvel enfant non exploré, s'il existe des coups disponibles.
     *
     * @param noeud Le nœud à développer.
     */
    protected void developper(Noeud noeud) {
        if (!noeud.isTerminal() && !noeud.isFullyExpanded()) {
            Map<Integer, Position> coupsDisponibles = noeud.getGrille().genererCasesDisponibles();
            
            // Si des coups non explorés sont disponibles, en créer un
            if (!coupsDisponibles.isEmpty()) {
                List<Position> coupsNonExplores = new ArrayList<>(coupsDisponibles.values());
                
                // Filtrer les coups déjà explorés
                for (Noeud enfant : noeud.getEnfants()) {
                    coupsNonExplores.remove(enfant.getMove());
                }
                
                // Si des coups non explorés restent, en choisir un au hasard
                if (!coupsNonExplores.isEmpty()) {
                    int randomIndex = RANDOM.nextInt(coupsNonExplores.size());
                    Position coup = coupsNonExplores.get(randomIndex);
    
                    // Créer une nouvelle grille en copiant l'état actuel
                    Grille nouvelleGrille = noeud.getGrille().copy();
                    nouvelleGrille.getCase(coup).setOccupe(noeud.getJoueurActuel());
    
                    // Créer le nœud enfant
                    Noeud enfant = new Noeud(
                        nouvelleGrille,
                        coup,
                        noeud,
                        EtatCase.opposite(noeud.getJoueurActuel())
                    );
    
                    noeud.addEnfant(enfant);
                    
                }
            }
        }
    }


    /**
     * Simule une partie aléatoire à partir d'un état donné.
     *
     * @param noeud Le nœud à partir duquel la simulation démarre.
     * @param joueurOriginal La couleur du joueur d'origine pour la simulation.
     * @return Un score (1.0 pour victoire, -1.0 pour défaite).
     */
    protected double simuler(Noeud noeud, EtatCase joueurOriginal) {

        // Crée une copie de la grille pour la simulation
        Grille simulationGrille = noeud.getGrille().copy();
        Case.EtatCase joueurActuel = noeud.getJoueurActuel();

        /* System.out.println("!!!!!!!!!!! Grille avant simulation ");
        simulationGrille.afficherGrille(); */

        if (noeud.isTerminal()) {
            return noeud.getGrille().verifierVictoire(joueurOriginal) ? 1.0 : -1.0;
        }
        //System.out.println("================= debut de la simulation  ================= c'est le tour de "+joueurOriginal+"Il va jouer"+noeud.getMove()+"\n");

        while (!simulationGrille.estTerminee()) {
            Map<Integer, Position> coupsDisponibles = simulationGrille.genererCasesDisponibles();
            int randomIndex = RANDOM.nextInt(coupsDisponibles.size());
            Position coup = coupsDisponibles.get(randomIndex);
            simulationGrille.getCase(coup).setOccupe(joueurActuel);

            /* System.out.println("iteration "+joueurActuel+"============");
            simulationGrille.afficherGrille();
            System.out.println("est ce que "+joueurActuel+" a gagné? "+ simulationGrille.verifierVictoire(joueurActuel));
            System.out.println("est ce que "+EtatCase.opposite(joueurActuel)+" a gagné? "+ simulationGrille.verifierVictoire(EtatCase.opposite(joueurActuel)));
             */
            joueurActuel = EtatCase.opposite(joueurActuel);
        }

        /* System.out.println("================= fin de la simulation  =================\n");
        System.out.println("est ce que "+joueurOriginal+" a gagné? "+ simulationGrille.verifierVictoire(joueurOriginal));
        System.out.println("est ce que opponent "+EtatCase.opposite(joueurOriginal)+" a gagné? "+ simulationGrille.verifierVictoire(EtatCase.opposite(joueurOriginal)));
        simulationGrille.afficherGrille(); */
       

        // Détermine le résultat de la simulation
        if (simulationGrille.verifierVictoire(joueurOriginal)) {
            //System.out.println("==== Return +1");
            return 1.0;  // Victoire
        }else {
            //System.out.println("==== Return -1");
            return -1.0;  // Défaite
        }
    }

    /**
     * Rétropropagation du résultat de la simulation à travers l'arbre.
     *
     * @param noeud Le nœud à partir duquel commencer la rétropropagation.
     * @param resultat Le résultat de la simulation (score positif pour victoire, négatif pour défaite).
     */
    protected void retropropager(Noeud noeud, double resultat) {
        while (noeud != null) {
            if ( resultat > 0){
                noeud.addWins(resultat);
            }
            else{
                noeud.addLosses(-resultat);
            }
            noeud.incrementVisits();
            noeud = noeud.getParent();
        }
    }
    
  

    
    
    
}