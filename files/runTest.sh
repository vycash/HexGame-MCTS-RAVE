#!/bin/bash

# Définition des variables
BUILD_DIR="build"
LIB_JUNIT="lib/junit-platform-console-standalone-1.9.3.jar:build"
TESTS_DIR="testUnitaire/"
TEST_CLASSES=(
    "livraison.testUnitaire.model.CaseTest"
    "livraison.testUnitaire.model.GrilleTest"
    "livraison.testUnitaire.model.OrchestrateurTest"
    "livraison.testUnitaire.model.PositionTest"
    "livraison.testUnitaire.model.player.PlayerTest"
    "livraison.testUnitaire.model.mcts.MCTSTest"
    "livraison.testUnitaire.model.mcts.RAVETest"
    "livraison.testUnitaire.model.mcts.NoeudTest"
    "livraison.testUnitaire.model.player.PlayerFactoryTest"
    "livraison.testUnitaire.model.player.RandomStrategyTest"
    "livraison.testUnitaire.model.player.MonteCarloStrategyTest"
    "livraison.testUnitaire.controller.ControllerTest"
    
)

# Fonction pour compiler tous les tests
compiler_tests() {
    echo "🔹 Compilation des tests..."
    javac -d "$BUILD_DIR" -cp "$LIB_JUNIT:$BUILD_DIR" $(find "$TESTS_DIR" -name "*.java")
    
    if [ $? -eq 0 ]; then
        echo "✅ Compilation réussie !"
    else
        echo "❌ Échec de la compilation. Vérifiez vos erreurs."
        exit 1
    fi
}

# Fonction pour exécuter un test spécifique
executer_test() {
    local test_class="$1"
    echo "🔹 Exécution du test : $test_class..."
    java -jar "lib/junit-platform-console-standalone-1.9.3.jar" --class-path "$BUILD_DIR" --select-class "$test_class"
}

# Menu principal
while true; do
    echo ""
    echo "=============================="
    echo "  🛠️  MENU D'EXÉCUTION DES TESTS  "
    echo "=============================="
    echo "1️⃣  Exécuter TOUS les tests"
    echo "2️⃣  Exécuter un test spécifique"
    echo "3️⃣  Quitter"
    echo  "➡️  Choisissez une option : "
    read choix

    case "$choix" in
        1)
            compiler_tests
            echo "🚀 Exécution de TOUS les tests..."
            java -jar lib/junit-platform-console-standalone-1.9.3.jar --class-path build --scan-class-path
            if [ $? -eq 0 ]; then
                echo ""
                echo "✅ TOUS LES TESTS ONT ÉTÉ VALIDÉS AVEC SUCCÈS ! 🎉"
                echo "📦 Tous les modules testés sont fonctionnels."
            else
                echo ""
                echo "❌ Certains tests ont échoué. Consulte les logs ci-dessus pour corriger les erreurs."
            fi
            ;;
            
        2)
            echo "📌 Tests disponibles :"
            for i in "${!TEST_CLASSES[@]}"; do
                echo "$(($i + 1))) ${TEST_CLASSES[$i]}"
            done
            echo  "➡️  Entrez le numéro du test à exécuter : "
            read test_num
            
            if [[ "$test_num" -ge 1 && "$test_num" -le "${#TEST_CLASSES[@]}" ]]; then
                compiler_tests
                executer_test "${TEST_CLASSES[$((test_num - 1))]}"
            else
                echo "❌ Numéro invalide, veuillez réessayer."
            fi
            ;;
        q|Q|3)
            echo "👋 Au revoir !"
            exit 0
            ;;
        *)
            echo "❌ Option invalide, veuillez réessayer."
            ;;
    esac
done