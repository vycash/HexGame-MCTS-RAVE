package utils.strategyMessage;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implémentation de l'interface MessageHandler pour l'affichage et la saisie de messages via la console.
 * 
 * Cette classe permet :
 * - d'afficher des messages d'information ou d'erreur,
 * - de demander des entrées utilisateur sous forme de texte ou de choix parmi une liste,
 * - de gérer les interactions utilisateur en mode console, notamment lors de l'exécution non expérimentale.
 */

public class ConsoleMessageHandler implements MessageHandler {

    private final Scanner scanner;
    @SuppressWarnings("unused")
	private boolean nonExperimentation;

    /**
     * Constructeur de la classe.
     * 
     * @param nonExperimentation indique si l'affichage est en mode non expérimental
     */
    public ConsoleMessageHandler(boolean nonExperimentation) {
        this.scanner = new Scanner(System.in);
        this.nonExperimentation = nonExperimentation;
    }

    
    /**
     * Affiche un message d'information dans la console, si le mode non expérimental est activé.
     * 
     * @param message le message à afficher
     * @param nonExperimentation booléen indiquant si le message doit être affiché
     */
    @Override
    public void afficherMessage(String message, boolean nonExperimentation) {
        if(nonExperimentation) {
            System.out.println("[INFO] " + message);
        }
    }

    
    /**
     * Affiche un message d'erreur dans la console.
     * 
     * @param erreur le message d'erreur à afficher
     */
    @Override
    public void afficherErreur(String erreur) {
        System.err.println("[ERREUR] " + erreur);
    }

    /**
     * Demande une saisie à l'utilisateur avec un message d'invite.
     * 
     * @param prompt le message d'invite
     * @return la ligne saisie par l'utilisateur
     */
    @Override
    public String demanderInput(String prompt) {
        System.out.println(prompt);
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }

    
    /**
     * Affiche une liste d'options à l'utilisateur et récupère son choix.
     * 
     * @param message le message d'instruction
     * @param options la liste des options disponibles
     * @return l'option choisie par l'utilisateur
     */
    @Override
    public String demanderChoixParmiOptions(String message, List<String> options) {
        afficherMessage(message, true);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i+1) + " - " + options.get(i));
        }
        while(true) {
            System.out.print("Votre choix : ");
            String input = scanner.nextLine();
            try {
                int choix = Integer.parseInt(input);
                if (choix >= 1 && choix <= options.size()) {
                    return options.get(choix - 1);
                }
            } catch (NumberFormatException e) {
                // Ignorer
            }
            afficherErreur("Choix invalide. Réessayez.");
        }
    }

    
    /**
     * Affiche un ensemble d'options numérotées et demande à l'utilisateur de faire un choix.
     * 
     * @param message le message d'instruction
     * @param choices une map contenant les numéros et les descriptions des choix
     * @return le numéro du choix sélectionné par l'utilisateur
     */
    public int getChoice(String message, Map<Integer, String> choices) {
        int choice = -1;
        while (true) {
            System.out.println(message);
            for (Map.Entry<Integer, String> entry : choices.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.print("Votre choix : ");
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choices.containsKey(choice)) {
                    break;
                } else {
                    System.out.println("Choix invalide. Veuillez choisir une option valide.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez saisir un nombre.");
            }
        }
        return choice;
    }
}
