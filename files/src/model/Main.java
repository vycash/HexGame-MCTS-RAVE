package model;

import config.Constants;
import config.ExperimentConfig;
import controller.Controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Classe principale pour démarrer et exécuter le jeu Hex avec différents modes :
 *  Mode CONSOLE : interface console interactive
 *  Mode GRAPHIQUE : interface graphique via le Controller
 *  Mode EXPERIMENTATION : exécutions automatiques basées sur un fichier JSON de configuration
 */
public class Main {

    /**
     * Scanner global pour le mode console
     */
    public static Scanner scanner = new Scanner(System.in);

    /**
     * Logger utilisé uniquement en mode EXPERIMENTATION après appel de initializeLogger().
     */
    private static Logger experimentLogger = null;

    /**
     * Indique si l'entête du fichier CSV a déjà été écrit (pour éviter de le répéter).
     */
    private static boolean enteteCsvDejaEcrit = false;

    /**
     * Point d'entrée de l'application.
     *
     * @param args arguments de la ligne de commande (ex. "CONSOLE", "GRAPHIQUE", "EXPERIMENTATION")
     */
    public static void main(String[] args) {
        Mode modeChoisi = parseModeFromArgs(args);

        switch (modeChoisi) {
            case CONSOLE:
                lancerConsole();
                break;
            case EXPERIMENTATION:
                lancerExperimentation();
                break;
            default:
                lancerGraphique();
                break;
        }
    }

    /**
     * Détermine le mode à partir du tableau d'arguments.
     *
     * @param args arguments de la ligne de commande
     * @return le mode choisi (CONSOLE, GRAPHIQUE ou EXPERIMENTATION)
     */
    private static Mode parseModeFromArgs(String[] args) {
        if (args.length == 0) {
            return Mode.GRAPHIQUE;
        }
        String arg = args[0].toLowerCase();
        if ("console".equals(arg)) {
            return Mode.CONSOLE;
        } else if ("graphique".equals(arg) || "gui".equals(arg)) {
            return Mode.GRAPHIQUE;
        } else if ("experimentation".equals(arg)) {
            return Mode.EXPERIMENTATION;
        }
        return Mode.GRAPHIQUE;
    }

    /**
     * Lance la version console du jeu de Hex.
     * Cette méthode demande à l'utilisateur la taille de la grille,
     * puis initialise et démarre la partie en mode interactif.
     */
    private static void lancerConsole() {
        int taille = Constants.GRID_SIZE;
        try {
            System.out.println("Entrez la taille de la grille (par défaut " + taille + ") : ");
            taille = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide, la taille par défaut sera utilisée.");
        }
        Constants.setGridSize(taille);
        Orchestrateur orchestrateur = new Orchestrateur();
        orchestrateur.demarrerPartie();
    }

    /**
     * Lance la version graphique du jeu de Hex.
     * Cette méthode instancie simplement le Controller, 
     * qui prend en charge l'interface utilisateur en mode graphique.
     */
    private static void lancerGraphique() {
        new Controller();
    }

    /**
     * Lance le mode d'expérimentation.
     * Cette méthode lit le fichier JSON de configuration (experiment_config.json)
     * et exécute un plan d'expériences en boucle selon les paramètres spécifiés.
     * Les informations sont enregistrées dans un fichier de log et dans un fichier CSV.
     */
    private static void lancerExperimentation() {
        initializeLogger();

        ExperimentConfig config = ExperimentConfig.loadFromFile("./experimentation/experiment_config.json");
        if (config == null) {
            System.err.println("Impossible de charger la configuration d'expérimentation.");
            return;
        }

        for (int gridSize : config.getGrid_sizes()) {
            for (int b1 : config.getBudget_j1()) {
                for (int b2 : config.getBudget_j2()) {
                    for (String startingPlayer : config.getStarting_players()) {
                    	for (String strategieJ1 : config.getStrategie()) {
                            for (String strategieJ2 : config.getStrategie()) {
                            
                            	int nbGames = config.getNb_games();

                                System.out.println("Configuration => grid=" + gridSize
                                  + ", b1=" + b1 + ", b2=" + b2
                                  + ", strategieJ1=" + strategieJ1 + ", strategieJ2=" + strategieJ2
                                  + ", start=" + startingPlayer
                                  + ", nbGames=" + nbGames);

                               if (experimentLogger != null) {
                                   experimentLogger.info("[CONFIG] GridSize=" + gridSize
                                     + " | BudgetJ1=" + b1
                                     + " | BudgetJ2=" + b2
                                     + " | StrategieJ1=" + strategieJ1
                                     + " | StrategieJ2=" + strategieJ2
                                     + " | Starting=" + startingPlayer
                                     + " | nbGames=" + nbGames);
                                }

                                executerExperiences(gridSize, b1, b2, startingPlayer, strategieJ1 , strategieJ2 ,  nbGames);
                        
                           }
                        }
                     }
                }
            }
        }

        System.out.println("Toutes les expérimentations sont terminées.");
    }

    /**
     * Initialise le logger en mode EXPERIMENTATION.
     * Crée un répertoire "experimentation" si nécessaire et un fichier "experiment_main.log"
     * en mode append pour ne pas écraser d'anciens logs.
     */
    private static void initializeLogger() {
        try {
            java.io.File logDir = new java.io.File("experimentation");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            experimentLogger = Logger.getLogger("ExperimentsLogger");

            FileHandler fh = new FileHandler("experimentation/experiment_main.log", true);
            fh.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return record.getMessage() + "\n";
                }
            });

            experimentLogger.addHandler(fh);
            experimentLogger.setUseParentHandlers(false);
            experimentLogger.setLevel(Level.INFO);

        } catch (IOException e) {
            e.printStackTrace();
            experimentLogger = null;
        }
    }

    /**
     * Exécute plusieurs parties avec la configuration spécifiée,
     * et enregistre les résultats dans le logger et dans un fichier CSV.
     *
     * @param gridSize taille de la grille (ex. 5, 7, 9...)
     * @param budgetJ1 budget (nombre d'itérations) pour le Joueur 1 (MCTS)
     * @param budgetJ2 budget (nombre d'itérations) pour le Joueur 2 (MCTS)
     * @param startingPlayer joueur qui commence ("BLEU" ou "ROUGE")
     * @param nbGames nombre de parties à jouer
     */
    private static void executerExperiences(int gridSize, int budgetJ1, int budgetJ2,
                                            String startingPlayer,String strategieJ1 , String strategieJ2,  int nbGames) {
        int victoiresJ1 = 0;
        int victoiresJ2 = 0;

        for (int i = 0; i < nbGames; i++) {
            Orchestrateur orchestrateur = new Orchestrateur(
                    gridSize,
                    budgetJ1,
                    budgetJ2,
                    startingPlayer,
                    strategieJ1,
                    strategieJ2,
                    true
            );

            int gagnant = orchestrateur.jouerPartieAutomatique();
            if (gagnant == 1) {
                victoiresJ1++;
            } else if (gagnant == 2) {
                victoiresJ2++;
            }
        }

        String bilan = "Bilan => J1=" + victoiresJ1 + " WIN | J2=" + victoiresJ2 + " WIN"
                + " [Grid=" + gridSize + ", b1=" + budgetJ1 + ", b2=" + budgetJ2
                + ", StrategieJ1=" + strategieJ1 + ", StrategieJ2=" + strategieJ2 + ", start=" + startingPlayer + "]";


        System.out.println(bilan);

        if (experimentLogger != null) {
            experimentLogger.info(bilan + "\n");
        }

        ecrireResultatCSV(gridSize, budgetJ1, budgetJ2, nbGames, victoiresJ1, victoiresJ2, startingPlayer , strategieJ1 , strategieJ2);
    }

    /**
     * Ecrit le résultat d'une configuration dans un fichier CSV
     * (situé dans "experimentation/resultats.csv").
     *
     * @param gridSize taille de la grille
     * @param budgetJ1 budget du joueur 1
     * @param budgetJ2 budget du joueur 2
     * @param nbGames nombre de parties
     * @param victoiresJ1 nombre de victoires du joueur 1
     * @param victoiresJ2 nombre de victoires du joueur 2
     * @param startPlayer joueur qui commence
     */
    private static void ecrireResultatCSV(int gridSize, int budgetJ1, int budgetJ2,
                                          int nbGames, int victoiresJ1, int victoiresJ2,
                                          String startPlayer , String strategieJ1 , String strategieJ2) {
        String cheminCSV = "experimentation/results/resultats.csv";
        double ratioJ1 = (victoiresJ1 * 100.0) / nbGames;
        double ratioJ2 = (victoiresJ2 * 100.0) / nbGames;

        StringBuilder sb = new StringBuilder();

        if (!enteteCsvDejaEcrit) {
            sb.append("GridSize,BudgetJ1,BudgetJ2,StrategieJ1,StrategieJ2,NbGames,StartPlayer,VictoiresJ1,VictoiresJ2,PourcentageJ1,PourcentageJ2\n");
            enteteCsvDejaEcrit = true;
        }

        sb.append(gridSize).append(",")
          .append(budgetJ1).append(",")
          .append(budgetJ2).append(",")
          .append(strategieJ1).append(",")
          .append(strategieJ2).append(",")
          .append(nbGames).append(",")
          .append(startPlayer).append(",")
          .append(victoiresJ1).append(",")
          .append(victoiresJ2).append(",")
          .append(ratioJ1).append(",")
          .append(ratioJ2).append("\n");


        try (FileWriter fw = new FileWriter(cheminCSV, true)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
