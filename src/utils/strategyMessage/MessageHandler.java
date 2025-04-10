package utils.strategyMessage;

import java.util.List;

/**
 * Interface MessageHandler définissant une stratégie d'affichage de messages.
 * Elle permet de contrôler l'affichage des messages en fonction du mode (non expérimentation ou non).
 */
public interface MessageHandler {

    /**
     * Affiche un message d'information.
     *
     * @param message Le message à afficher.
     * @param nonExperimentation true si on est en mode non expérimentation (donc affichage souhaité), false sinon.
     */
    void afficherMessage(String message, boolean nonExperimentation);

    /**
     * Affiche un message d'erreur.
     *
     * @param erreur Le message d'erreur à afficher.
     */
    void afficherErreur(String erreur);

    /**
     * Demande une entrée utilisateur via une boîte de dialogue graphique.
     *
     * @param message Le message d'invite à afficher à l'utilisateur.
     * @return La chaîne de caractères saisie par l'utilisateur.
     */
    String demanderInput(String message);

    /**
     * Demande à l'utilisateur de choisir parmi une liste d'options.
     *
     * @param message Le message d'invite.
     * @param options La liste des options.
     * @return L'option choisie.
     */
    String demanderChoixParmiOptions(String message, List<String> options);
}
