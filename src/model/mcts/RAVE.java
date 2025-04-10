package model.mcts;

import model.*;
import model.Case.EtatCase;
import config.*;
import java.util.*;


/**
 * Implémentation de l'algorithme RAVE (Rapid Action Value Estimation) dérivé de MCTS.
 * 
 * Cette version optimise la sélection des coups en exploitant des statistiques partagées
 * entre les différentes branches de l'arbre pour accélérer la convergence vers les bons choix.
 */
public class RAVE extends MCTS {

	
	/**
     * Constructeur de RAVE avec budget d'itérations et mode expérimental spécifiés.
     * 
     * @param iterationBudget le nombre d'itérations à effectuer
     * @param experimentation true si on active le mode expérimental (désactive certains affichages)
     */
    public RAVE(int iterationBudget,boolean experimentation) {
        super(iterationBudget,experimentation);
    }

    
    /**
     * Constructeur de RAVE avec budget d'itérations, sans mode expérimental.
     * 
     * @param iterationBudget le nombre d'itérations à effectuer
     */
    public RAVE(int iterationBudget) {
        super(iterationBudget,false);
    }

    
    /**
     * Constructeur de RAVE avec mode expérimental spécifié et budget par défaut.
     * 
     * @param experimentation true si on active le mode expérimental
     */
    public RAVE(boolean experimentation) {
        super(experimentation);
    }
    
    
    /**
     * Constructeur par défaut avec les paramètres standards.
     */
    public RAVE() {
        super();
    }

    /**
     * Exécute une simulation complète à partir du nœud donné, en appliquant la stratégie RAVE.
     * Enregistre les actions jouées par le joueur original pour ajuster les statistiques RAVE.
     * 
     * @param noeud le nœud à partir duquel démarrer la simulation
     * @param joueurOriginal le joueur pour lequel la simulation est évaluée
     * @return 1.0 si victoire, -1.0 sinon
     */
    @Override
    protected double simuler(Noeud noeud, EtatCase joueurOriginal) {
        Grille simulationGrille = noeud.getGrille().copy();
        Case.EtatCase joueurActuel = noeud.getJoueurActuel();
        Set<Position> actionsJouees = new HashSet<>();

        if (noeud.isTerminal()) {
            return noeud.getGrille().verifierVictoire(joueurOriginal) ? 1.0 : -1.0;
        }

        while (!simulationGrille.estTerminee()) {
            Map<Integer, Position> coupsDisponibles = simulationGrille.genererCasesDisponibles();
            int randomIndex = new Random().nextInt(coupsDisponibles.size());
            Position coup = coupsDisponibles.get(randomIndex);
            simulationGrille.getCase(coup).setOccupe(joueurActuel);

            if (joueurActuel == joueurOriginal)  actionsJouees.add(coup);

            joueurActuel = EtatCase.opposite(joueurActuel);
        }

        double resultat = simulationGrille.verifierVictoire(joueurOriginal) ? 1.0 : -1.0;
        updateRaveValues(noeud.getParent(), actionsJouees, resultat);

        return resultat;
    }

    
    
    /**
     * Sélectionne le meilleur enfant d’un nœud en combinant les valeurs classiques et RAVE.
     * 
     * @param noeud le nœud dont on veut déterminer le meilleur enfant
     * @return le nœud enfant avec la meilleure valeur combinée
     */
    @Override
    protected Noeud bestChild(Noeud noeud) {
        if (noeud.isLeaf()) {
            return noeud;
        }

        Noeud meilleurEnfant = null;
        double meilleurScore = -Double.MAX_VALUE;

        for (Noeud enfant : noeud.getEnfants()) {
            double explorationTerm = Constants.EPLOITATION_EXPLORATION_CONSTANT * Math.sqrt(Math.log(noeud.getVisits()) / (enfant.getVisits() + 1));
            double score = enfant.getCombinedValue() + explorationTerm;

            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurEnfant = enfant;
            }
        }
        return meilleurEnfant;
    }

    
    
    /**
     * Détermine le meilleur coup à jouer à partir d’un nœud donné.
     * Affiche également tous les coups possibles si le mode expérimental est désactivé.
     * 
     * @param noeud le nœud racine dont les enfants sont évalués
     * @return la position correspondant au meilleur coup
     */
    protected Position bestMove(Noeud noeud) {
        List<Noeud> enfantsTries = new ArrayList<>(noeud.getEnfants());
        
        Noeud meilleur = enfantsTries.get(0);
        for (Noeud child : enfantsTries) {
            if (child.getCombinedValue() > meilleur.getCombinedValue()) {
                meilleur = child;
            }
        }
        
        if ( this.isExperimentation() == false ){
            afficherTousLesCoups(noeud);
            System.out.println("\nBest move : " + meilleur.toStringRave() + " avec " + this.getIterationBudget() + " itérations");
        }
        return meilleur.getMove();
    }

    
    
    /**
     * Met à jour récursivement les statistiques RAVE à partir des actions jouées durant la simulation.
     * 
     * @param node le nœud courant
     * @param actionsJouees les positions jouées par le joueur original
     * @param resultat résultat de la simulation (1.0 pour victoire, -1.0 pour défaite)
     */
    private void updateRaveValues(Noeud node, Set<Position> actionsJouees, double resultat) {
        if (node == null) return;

        if (actionsJouees.contains(node.getMove())) {
            if (resultat > 0) {
                //node.addWins(resultat);
                node.setRaveWins(node.getRaveWins() + resultat);
            } 
            node.addRaveVisits();
            //node.incrementVisits();
        }

        for (Noeud child : node.getEnfants()) {
            updateRaveValues(child, actionsJouees, resultat);
        }
    }

    
    
    /**
     * Affiche dans la console la liste de tous les coups possibles à partir d’un nœud donné,
     * avec leurs statistiques RAVE.
     * 
     * @param noeud le nœud dont les enfants sont à afficher
     */
    @Override
    protected void afficherTousLesCoups(Noeud noeud) {
        List<Noeud> enfants = noeud.getEnfants();
        System.out.println("Liste des moves possibles:");
        for (Noeud n : enfants) {
            System.out.println(n.toStringRave());
        }
    }
}