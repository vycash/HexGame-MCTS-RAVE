
### INTRODUCTION :
Ce projet est l'implémentation du jeu HexGame en Java avec l'algorithme MCTS (Monte Carlo tree search) pour le joueur ia ainsi que l'optimisation RAVE pour le joueur ia MCTS

### DESCRIPTION DU PROJET:
le projet mets en place plusieurs fonctionnalitées :
- Execution du jeu en mode humain vs machine ou machine vs machine
- Visualisation du jeu en terminal et en mode graphique
- Implémentation de l'algorithme MCTS avec optimisation RAVE
- Tests unitaires 
- Expérimentations parametrables et modulaires
- Analyse des résultats de l'expérimentation


### CONFIGURATION :

- Vous pouvez modifier la classe Constants.java dans le package src/config/ pour modifier les constantes globales du projet.
- Vous pouvez modifier les configurations des expérimentations dans le fichier experiment_config.json.
- Les autres paramètres du jeu sont configurables lors du lancement du jeu avec le script run.sh.


### EXECUTION :

- executez le script run.sh pour le menu principal du jeu et choisir l'option que vous souhaitez (lancer le jeu, tests, expérimentations).
- executez le script runTest.sh pour lancer les tests unitaires.

**Attention**: les expérimentations peuvent prendre plusieurs jours pour compléter toutes les combinaisons de configuration possible, il est fortement conseillé de lancer plusieurs configurations en parallèle pour économiser le temps.  

### ANALYSE D'EXPERIMENTATION:
pour executez l'analyse et le tracage des graphes des résultats des éxpérimentations il est nécessaire d'avoir installé python et les packages suivant :
 - matplotlib, pour l'installer executez la commande suivante : pip install matplotlib
 - pandas , pour l'installer executez la commande suivante : pip install pandas
 - seaborn , pour l'installer executez la commande suivante : pip install seaborn

ensuite placez vous dans le package experimentation/ et executez la commande suivante:
- jupyter notebook

ensuite cliquez sur la flèche start pour lancer le script et generer les graphes
les résultats sont genérés dans experimentation/graphs/

## l'arborescence du projet :
.
├── experimentation/
├── javadoc/
├── lib/
├── rapport/
├── ressources/
├── src/
│   ├── config/
│   ├── controller/
│   ├── model/
│   │   ├── mcts/
│   │   └── player/
│   ├── utils/
│   │   └── strategyMessage/
│   └── vue/
└── testUnitaire/
    ├── controller/
    ├── model/
    │   ├── mcts/
    │   └── player/
    ├── simulation/
    ├── utils/
    └── vue/

# MCTS


## Explication

**MCTS : Monte Carlo Tree Search** est un algorithme de prise de décision utilisé pour explorer les choix/coups possibles dans un jeu ou un problème complexe.  
Il est particulièrement utile pour les jeux comme Hex, où il y a un grand nombre de coups possibles et où il est difficile de prédire quel coup est le meilleur.  

L'idée principale de **MCTS** est de simuler de nombreuses parties aléatoires à partir de l'état actuel du jeu, puis d'utiliser les résultats de ces simulations pour décider quel coup est le plus prometteur.  

---

## Fonctionnement

L'arbre de Recherche de l'algorithme est représenté par des Nœuds, où chaque nœud contient un ensemble d'informations (Le coup concerné, la grille représentant l'état du jeu après cette action, le nœud parent, le nombre de victoires, le nombre de pertes, et le nombre de visites).  

On désigne par un **nœud terminal/état final**, un nœud où il n'y a plus de coups possibles depuis son état (toutes les cases sont remplies) ou il existe un vainqueur (jeu terminé).  

On désigne par un **nœud complet**, un nœud où toute action possible depuis son état existe dans l'un de ses enfants, autrement dit : un nœud qui a le même nombre d’enfants que de coups possibles depuis son état.  

L'algorithme MCTS fonctionne en répétant 4 étapes majeures de manière itérative et renvoie à la fin des itérations le coup qui a le plus grand score Victoires/Défaites.  

---

### Étape 1 : Sélection

**Objectif** :  
Parcourir l'arbre de recherche pour trouver un nœud "intéressant" à explorer où chaque nœud représente un coup et la grille résultante de ce coup.  

**Comment** :
- L'algorithme parcourt l'arbre en commençant à la racine de l'arbre (l'état actuel du jeu) et choisit l'enfant (coup possible) qui maximise la valeur obtenue en utilisant une formule appelée UCT (*Upper Confidence Bound for Trees*) à chaque fois jusqu'à atteindre **un nœud qui n'a pas été totalement exploré et qui n'est pas un état final**.  
  Cette approche garantit que les nœuds partiellement développés ont une chance de produire de nouveaux enfants.  

**Formule UCT** :
\[
\frac{w}{n} + C \cdot \sqrt{\frac{\ln(N)}{n}}
\]
- \(w\) : nombre de parties gagnées simulées depuis ce nœud  
- \(n\) : nombre de fois où le nœud a été visité  
- \(N\) : nombre de fois où le nœud père a été visité  
- \(C\) : paramètre d'exploration — en théorie égal à \(\sqrt{2}\), en pratique choisi expérimentalement.  

**Équilibre entre exploration et exploitation** :
- **Exploitation** : Choisir des coups qui ont déjà donné de bons résultats.  
- **Exploration** : Essayer des coups qui n'ont pas encore été beaucoup explorés.

---

### Étape 2 : Expansion

**Objectif** :  
Ajouter un nouveau nœud à l'arbre pour explorer un coup non encore essayé.  

**Comment** :
- Si **le nœud sélectionné dans la phase de sélection a déjà été visité et qu'il n'est pas un état terminal et qu'il n'est pas un nœud complet**, on le développe en lui ajoutant une action possible choisie au hasard depuis l'ensemble des coups possibles à partir de son état.  
  Cet ajout est représenté par un ajout d'un nouveau nœud contenant le coup joué et l'état résultant à l'arbre.  
- Sinon, si le nœud sélectionné n'a jamais été visité, on passe directement à la phase de simulation.  

---

### Étape 3 : Simulation

**Objectif** :  
Simuler une partie aléatoire à partir du meilleur enfant (inclut le nouveau nœud ajouté) pour estimer sa valeur.  

**Comment** :
- On joue une partie aléatoire à partir de l'état du jeu représenté par le nouveau nœud en choisissant un coup aléatoire pour chacun des joueurs à chaque tour jusqu'à atteindre un état final.  
- La simulation se termine lorsque la partie est finie (victoire, défaite ou match nul) où la grille est entièrement remplie.  
- On enregistre le résultat de la simulation (1 pour une victoire, -1 pour une défaite).  

---

### Étape 4 : Rétropropagation

**Objectif** :  
Mettre à jour les statistiques des nœuds visités pendant la sélection avec le résultat de la simulation.  

**Comment** :
- L'algorithme remonte l'arbre depuis le nœud simulé jusqu'à la racine.  
- Pour chaque nœud visité, on met à jour :  
  - Le nombre de visites.  
  - Le nombre de victoires.  
  - Le nombre de défaites.  

---

## Algorithme

L'illustration suivante montre le fonctionnement des 4 étapes de l'algorithme MCTS pour un état donné (figure à insérer ici en Markdown si nécessaire).  

---

## MCTS dans le jeu de Hex et Complexité

**MCTS est particulièrement efficace pour le jeu de Hex, car :**
- Pour chaque état, il y a un très grand nombre de coups possibles, et cet algorithme explore de manière intelligente en se concentrant sur les coups prometteurs.  
- Il n'y a pas besoin d'évaluation heuristique pour estimer la valeur d'un coup contrairement à d'autres algorithmes, car MCTS repose sur des simulations aléatoires pour estimer cette valeur.  

Dans notre implémentation :
- Chaque joueur a un arbre de recherche propre à lui pour séparer les responsabilités et avoir des estimations plus précises selon le joueur.  
- Pour des raisons d'efficacité, seul le sous-arbre enraciné au nœud sélectionné est conservé à la fin de chaque itération.  

---

## RAVE Optimisation

### Explication
RAVE (*Rapid Action Value Estimation*) est une extension de MCTS qui vise à accélérer la convergence des estimations en exploitant des informations supplémentaires provenant des simulations.  

- RAVE estime que :  
  - La valeur d'une action est similaire dans tous les sous-états du sous-arbre du nœud sélectionné.  
  - Chaque coup joué dans une simulation est traité comme s'il s'agissait de la première fois que ce coup a été joué.  

**Valeur RAVE** :
\[
\text{RAVE Value} = \frac{\text{RAVE Wins}}{\text{RAVE Visits}}
\]

**Valeur MCTS** :
\[
\text{MCTS Value} = \frac{\text{victoires}}{\text{défaites}}
\]