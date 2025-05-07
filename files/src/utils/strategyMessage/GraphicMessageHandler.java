package utils.strategyMessage;

import java.util.List;
import javax.swing.JOptionPane;

/**
 * Affiche les messages et demandes via des boîtes de dialogue Swing.
 * 
 * Cette classe implémente l'interface {@code MessageHandler} en utilisant des boîtes de dialogue
 * pour afficher des messages d'information, des erreurs, ou pour demander une saisie utilisateur.
 * Elle peut également afficher les messages en fonction du mode (non expérimentation) si le joueur est humain.
 * 
 */
public class GraphicMessageHandler implements MessageHandler {

    /**
     * Indique si le joueur est humain.
     */
    private final boolean joueurHumain;
    
    /**
     * Indique si l'affichage des messages doit être réalisé en mode non expérimentation.
     * Si {@code nonExperimentation} est {@code true}, les messages seront affichés.
     */
    public boolean nonExperimentation;

    /**
     * Constructeur de GraphicMessageHandler.
     *
     * @param joueurHumain true si le joueur est humain, false sinon.
     */
    public GraphicMessageHandler(boolean joueurHumain) {
        this.joueurHumain = joueurHumain;
    }

    /**
     * Affiche un message d'information via une boîte de dialogue Swing.
     *
     * @param message Le message à afficher.
     * @param nonExperimentation true si le message doit être affiché en mode non expérimentation.
     */
    @Override
    public void afficherMessage(String message, boolean nonExperimentation) {
        if (joueurHumain && nonExperimentation) {
            JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Affiche un message d'erreur via une boîte de dialogue Swing.
     *
     * @param erreur Le message d'erreur à afficher.
     */
    @Override
    public void afficherErreur(String erreur) {
        if (joueurHumain) {
            JOptionPane.showMessageDialog(null, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Demande à l'utilisateur une saisie via une boîte de dialogue Swing.
     *
     * @param prompt Le message d'invite à afficher.
     * @return La chaîne de caractères saisie par l'utilisateur, ou {@code null} si le joueur n'est pas humain.
     */
    @Override
    public String demanderInput(String prompt) {
        if (!joueurHumain) {
            return null;
        }
        return JOptionPane.showInputDialog(null, prompt);
    }

    /**
     * Demande à l'utilisateur de choisir parmi une liste d'options via une boîte de dialogue Swing.
     *
     * @param message Le message d'invite à afficher.
     * @param options La liste des options proposées.
     * @return L'option choisie par l'utilisateur, ou {@code null} si aucune option n'est sélectionnée.
     */
    @Override
    public String demanderChoixParmiOptions(String message, List<String> options) {
        String[] opts = options.toArray(new String[0]);
        Object selected = JOptionPane.showInputDialog(
            null, message, "Sélection",
            JOptionPane.QUESTION_MESSAGE, null,
            opts, opts[0]
        );
        return (selected != null) ? selected.toString() : null;
    }
}
