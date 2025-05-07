package model.player;

import config.*;
import model.Case;
import utils.strategyMessage.MessageHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory pour créer des joueurs avec des stratégies spécifiques.
 */
public class PlayerFactory {

    // Stocke le budget d'itération pour chaque joueur MCTS indexé par le nom du joueur.
    private static Map<String, Integer> iterationBudgets = new HashMap<>();

    /**
     * Retourne le budget d'itération associé à un joueur donné.
     *
     * @param playerName Le nom du joueur.
     * @return Le budget d'itération pour ce joueur ou la valeur par défaut si non défini.
     */
    public static int getIterationBudget(String playerName) {
        return iterationBudgets.getOrDefault(playerName, Constants.ITERATION_BUDGET);
    }

    /**
     * Crée un joueur en associant son nom, sa couleur et sa stratégie.
     * Pour les stratégies MCTS/RAVE, le budget est demandé à l'utilisateur.
     *
     * @param nom Le nom du joueur.
     * @param couleur La couleur du joueur.
     * @param typeStrategy Le type de stratégie (random, mcts, rave, human).
     * @param messageHandler Le gestionnaire de messages pour l'interaction avec l'utilisateur.
     * @return Le joueur créé.
     * @throws IllegalArgumentException si la stratégie est inconnue.
     */
    public static Player creerPlayer(String nom, Case.EtatCase couleur,
                                     String typeStrategy,
                                     MessageHandler messageHandler) {
        PlayerStrategy strategy;
        int budget;
        switch (typeStrategy.toLowerCase()) {
            case "random":
                strategy = new RandomStrategy();
                break;
            case "mcts":
                budget = demandInput(nom, messageHandler);
                strategy = new MonteCarloStrategy(budget);
                iterationBudgets.put(nom, budget);
                break;
            case "rave":
                budget = demandInput(nom, messageHandler);
                strategy = new RAVEStrategy(budget);
                iterationBudgets.put(nom, budget);
                break;
            case "human":
                strategy = new HumanStrategy();
                break;
            default:
                throw new IllegalArgumentException("Stratégie inconnue : " + typeStrategy);
        }
        return new Player(nom, couleur, strategy, messageHandler);
    }

    
    /**
     * Crée un joueur MCTS/RAVE sans demande de saisie, en utilisant un budget prédéfini.
     * 
     * @param nom Le nom du joueur.
     * @param couleur La couleur du joueur.
     * @param typeStrategy Le type de stratégie (mcts ou rave).
     * @param budget Le budget d'itération à utiliser.
     * @param messageHandler Le gestionnaire de messages.
     * @return Le joueur créé.
     * @throws IllegalArgumentException si le type de stratégie n'est pas mcts ou rave.
     */
    public static Player creerPlayerAutomatique(String nom, Case.EtatCase couleur, String typeStrategy, int budget, MessageHandler messageHandler) {

    		PlayerStrategy strategy;
    	    if ("mcts".equalsIgnoreCase(typeStrategy)) {
    	    	strategy = new MonteCarloStrategy(budget);
    	    } else if ("rave".equalsIgnoreCase(typeStrategy)) {
    	    	strategy = new RAVEStrategy(budget);
    	    } else {
    	    	throw new IllegalArgumentException("Stratégie inconnue (seul MCTS ou RAVE attendu) : " + typeStrategy);
    	    }

    	    iterationBudgets.put(nom, budget);
    	    return new Player(nom, couleur, strategy, messageHandler);
    }



    /**
     * Demande à l'utilisateur de saisir le budget d'itération pour un joueur MCTS ou RAVE.
     *
     * @param nom Le nom du joueur.
     * @param messageHandler Le gestionnaire de messages pour demander une saisie.
     * @return Le budget saisi par l'utilisateur, ou la valeur par défaut en cas d'erreur ou d'absence de saisie.
     */
    private static int demandInput(String nom, MessageHandler messageHandler){
        String input = messageHandler.demanderInput("Choisissez un nombre d'itérations ou appuyez sur Entrée pour la valeur par défaut (" 
                + Constants.ITERATION_BUDGET + "): ");
        int budget;
        try {
            if (!input.isEmpty()) {
                budget = Integer.parseInt(input);
            } else {
                budget = Constants.ITERATION_BUDGET;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide, la valeur par défaut sera utilisée.");
            budget = Constants.ITERATION_BUDGET;
        }
        return budget;
    }
}
