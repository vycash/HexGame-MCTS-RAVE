package config;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Classe représentant la configuration d'expérimentation du jeu.
 * 
 * Elle reflète la structure du fichier JSON de configuration, et fournit
 * une méthode pour charger ces données en mémoire via la bibliothèque Gson.
 * 
 */
public class ExperimentConfig {

    /**
     * Liste des tailles de grille .
     */
    private List<Integer> grid_sizes;
    
    /**
     * Liste des budgets possibles pour le Joueur 1.
     */
    private List<Integer> budget_j1;
    
    /**
     * Liste des budgets possibles pour le Joueur 2.
     */
    private List<Integer> budget_j2;
    
    /**
     * Liste des joueurs pouvant commencer la partie.
     */
    private List<String> starting_players;
    
    
    /**
     * Liste des Strategie (RAVE/MCTS).
     */
    private List<String> strategie;

    /**
     * Nombre de parties à jouer pour chaque configuration.
     */
    private int nb_games;

    /**
     * Méthode statique permettant de charger une configuration depuis un fichier JSON.
     * 
     * @param filename Chemin vers le fichier JSON à lire
     * @return Une instance de ExperimentConfig initialisée avec les données du fichier,
     *         ou null en cas d'erreur.
     */
    public static ExperimentConfig loadFromFile(String filename) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, ExperimentConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère la liste des tailles de grille à expérimenter.
     * 
     * @return La liste des tailles (grid_sizes).
     */
    public List<Integer> getGrid_sizes() {
        return grid_sizes;
    }

    /**
     * Définir la liste des tailles de grille.
     * 
     * @param grid_sizes Nouvelle liste de tailles de grille.
     */
    public void setGrid_sizes(List<Integer> grid_sizes) {
        this.grid_sizes = grid_sizes;
    }

    /**
     * Récupère la liste des budgets possibles pour le Joueur 1.
     * 
     * @return La liste des budgets pour J1.
     */
    public List<Integer> getBudget_j1() {
        return budget_j1;
    }

    /**
     * Définir la liste des budgets pour le Joueur 1.
     * 
     * @param budget_j1 Nouvelle liste de budgets pour J1.
     */
    public void setBudget_j1(List<Integer> budget_j1) {
        this.budget_j1 = budget_j1;
    }

    /**
     * Récupère la liste des budgets possibles pour le Joueur 2.
     * 
     * @return La liste des budgets pour J2.
     */
    public List<Integer> getBudget_j2() {
        return budget_j2;
    }

    /**
     * Définir la liste des budgets pour le Joueur 2.
     * 
     * @param budget_j2 Nouvelle liste de budgets pour J2.
     */
    public void setBudget_j2(List<Integer> budget_j2) {
        this.budget_j2 = budget_j2;
    }

    /**
     * Récupère la liste des joueurs qui peuvent commencer.
     * 
     * @return La liste des joueurs de départ (starting_players).
     */
    public List<String> getStarting_players() {
        return starting_players;
    }

    
    

       
    /**
     * Définir la liste des joueurs qui peuvent commencer la partie.
     * 
     * @param starting_players Nouvelle liste de joueurs de départ.
     */
    public void setStarting_players(List<String> starting_players) {
        this.starting_players = starting_players;
    }

    
    /**
     * Récupère la liste des strategie qui peuvent jouer.
     * 
     * @return La liste des strategie (Rave/MCTS).
     */
    public List<String> getStrategie() {
        return strategie;
    }
    
    
    
    /**
     * Définir la liste des strategie qui peuvent jouer.
     * 
     * @param strategie Nouvelle liste de strategie de jeu.
     */
    public void setStrategie(List<String> strategie) {
        this.strategie = strategie;
    }
    
    
    /**
     * Récupère le nombre de parties à jouer pour chaque configuration.
     * 
     * @return Le nombre de parties (nb_games).
     */
    public int getNb_games() {
        return nb_games;
    }

    /**
     * Définir le nombre de parties à jouer pour chaque configuration.
     * 
     * @param nb_games Nouveau nombre de parties.
     */
    public void setNb_games(int nb_games) {
        this.nb_games = nb_games;
    }


   
}
