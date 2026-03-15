#!/bin/bash

# Variables globales
SRC_DIR="src"
BUILD_DIR="build/"
TEST_DIR="testUnitaire/"
LIB_DIR="lib/"
DOC_DIR="javadoc"

# La classe principale (doit pointer sur votre nouveau "Main" unique)
MAIN_CLASS="model.Main"

# Fonctions pour afficher les options
afficher_titre() {
    echo "===================================================="
    echo "                      HEXGAME                       "
    echo "===================================================="
    echo ""
}

# Menu principal mis à jour avec l'option d'expérimentation
menu_principal() {
    afficher_titre
    echo "1. Lancer le jeu"
    echo "2. Exécuter les tests unitaires"
    echo "3. Générer et ouvrir la documentation Javadoc"
    echo "4. Nettoyer les fichiers compilés"
    echo "5. Re-Compiler le projet"
    echo "6. Lancer une expérimentation"
    echo "7. Générer le graphe à partir des résultats CSV"
    echo "8. Créer le JAR"
    echo "9. Quitter"
    echo -n "Choisissez une option : "
}

# Fonction pour choisir et exécuter le mode de jeu
choisir_mode_jeu() {
     
    echo "Choisissez le mode de jeu :"
    echo "1. Mode Console"
    echo "2. Mode Graphique"
    echo -n "Votre choix : "
    read mode

    # Variable LIBS pour charger les .jar dans lib
    LIBS=$(find "$LIB_DIR" -name "*.jar" | tr '\n' ':')

    case "$mode" in
    1)
         
        echo "Lancement du mode Console..."
        # On passe "CONSOLE" comme argument, que Main traitera comme "console"
        java -cp "$BUILD_DIR:$LIBS" "$MAIN_CLASS" CONSOLE
        ;;
    2)
         
        echo "Lancement du mode Graphique..."
        # On passe "GRAPHIQUE" comme argument, que Main traitera comme "graphique"
        java -cp "$BUILD_DIR:$LIBS" "$MAIN_CLASS" GRAPHIQUE
        ;;
    *)
         
        echo "Option invalide. Retour au menu principal."
        ;;
    esac
}

# Fonction pour compiler et exécuter le projet principal
compiler_projet() {
     
    echo "Compilation des sources Java..."
    mkdir -p "$BUILD_DIR"
    LIBS=$(find "$LIB_DIR" -name "*.jar" | tr '\n' ':')

    # Compilation des .java dans src/
    javac -d "$BUILD_DIR" -cp "$LIBS" $(find "$SRC_DIR" -name "*.java")

    if [ $? -eq 0 ]; then
         
        echo "Compilation réussie."
        #choisir_mode_jeu  # Appelle la fonction pour choisir le mode après compilation
    else
        
        echo "Erreur lors de la compilation."
    fi
}

make_jar() {
    compiler_projet
    jar cfm ../HexGame.jar MANIFEST.MF -C build .
}

# Fonction pour exécuter les tests unitaires en appelant RunTest
executer_tests() {
    echo "Exécution des tests unitaires via RunTest..."

    # Appeler le fichier RunTest.sh pour exécuter les tests
    ./RunTest.sh

    read -p "Appuyez sur une touche pour revenir au menu principal."
}


# Fonction pour générer et ouvrir la documentation Javadoc
generer_documentation() {
    echo "Génération de la documentation Javadoc..."
    mkdir -p "$DOC_DIR"
    
    # Construire la variable LIBS avec tous les JAR du dossier lib
    LIBS=$(find "$LIB_DIR" -name "*.jar" | tr '\n' ':')

    # Inclure le sous-package utils.strategyMessage dans la génération de la documentation
    javadoc -d "$DOC_DIR" -sourcepath "$SRC_DIR" -subpackages model:controller:config:utils.strategyMessage:vue -classpath "$LIBS" 2>/dev/null

    # Vérifier si la génération a réussi
    if [ $? -eq 0 ]; then
        echo "Documentation générée avec succès."

        # Vérifier si le fichier index.html existe et l'ouvrir
        FULL_PATH="$PWD/$DOC_DIR/index.html"
        if [ -f "$FULL_PATH" ]; then
            if command -v xdg-open &> /dev/null; then
                # Ouvrir le fichier dans le navigateur par défaut sous Linux
                xdg-open "$FULL_PATH" && echo "Documentation ouverte dans le navigateur (Linux)."
            elif command -v open &> /dev/null; then
                # Ouvrir le fichier dans le navigateur par défaut sous macOS
                open "$FULL_PATH" && echo "Documentation ouverte dans le navigateur (macOS)."
            else
                echo "Ouvrez manuellement : $FULL_PATH"
            fi
        else
            echo "Erreur : le fichier index.html n'a pas été généré."
        fi
    else
        echo "Erreur lors de la génération de la documentation."
    fi
    read -p "Appuyez sur une touche pour revenir au menu principal."
}



# Fonction pour nettoyer les fichiers compilés
nettoyer_fichiers() {
     
    echo "Nettoyage des fichiers compilés..."
    rm -rf "$BUILD_DIR"
    rm -rf "$DOC_DIR"
    echo "Fichiers nettoyés."
    read -p "Appuyez sur une touche pour revenir au menu principal."
}

lancer_experimentation() {
     
    echo "Lancement de l'expérimentation..."
    LIBS=$(find "$LIB_DIR" -name "*.jar" | tr '\n' ':')
    java -cp "$BUILD_DIR:$LIBS" "$MAIN_CLASS" EXPERIMENTATION
}



generer_graphe_python() {
    echo "📊 Génération des graphes"

    SCRIPT_PY="experimentation/analyse_experimentation.ipynb"

    if [ ! -f "$SCRIPT_PY" ]; then
        echo "❌ Le script Python $SCRIPT_PY est introuvable."
        return
    fi

    # Exécution du script Python directement
    jupyter nbconvert --execute $SCRIPT_PY

    if [ $? -eq 0 ]; then
        echo "✅ Graphes générés avec succès dans experimentation/graphs/."
    else
        echo "❌ Erreur lors de la génération du graphe."
    fi

    read -p "Appuyez sur une touche pour revenir au menu principal."
}




compiler_projet
# Boucle principale
while true; do
     
    menu_principal
    read choix
    case "$choix" in
    1) choisir_mode_jeu ;;
    2) executer_tests ;;
    3) generer_documentation ;;
    4) nettoyer_fichiers ;;
    5) compiler_projet ;;
    6) lancer_experimentation ;;
    7) generer_graphe_python ;;
    8) make_jar ;;
    9)   echo "Au revoir !"; exit 0 ;;
    *)   echo "Option invalide. Veuillez réessayer." ;;
    esac
done
