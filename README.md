<span style="color:red">Vous pouvez trouver une version en français du Readme dans le fichier README-French.md</span>

### INTRODUCTION:
This project is an implementation of the HexGame in Java using the MCTS (Monte Carlo Tree Search) algorithm for the AI player, along with RAVE optimization.

![Text](ressources/hexGame.png)
![Text](ressources/hexgame_graphique_end.png)


### PROJECT DESCRIPTION:
The project implements several features:
- Execution of the game in Human vs. Machine or Machine vs. Machine modes.
- Game visualization in both terminal and graphical modes.
- Implementation of the MCTS algorithm with RAVE optimization.
- Unit tests.
- Configurable and modular experiments.
- Analysis of experimental results.

### DOCUMENTATION:

You can find the project documentation by following [this link](https://vycash.github.io/HexGame-MCTS-RAVE/files/javadoc/index.html) or by pasting the URL below into your browser:
```
https://vycash.github.io/HexGame-MCTS-RAVE/files/javadoc/index.html
```

### CONFIGURATION:

- You can modify the `Constants.java` class in the `src/config/` package to change the project's global constants.
- You can modify experiment configurations in the `experiment_config.json` file.
- Other game parameters are configurable when launching the game using the `run.sh` script.

### EXECUTION:

To run the project on your machine, you must first clone the repository into a directory of your choice using the command:
```bash
git clone https://github.com/vycash/HexGame-MCTS-RAVE.git
```

Then, navigate to the cloned directory on your machine, named "HexGame-MCTS-RAVE/".  

If you are on a Windows machine, simply click on the following file:

```
HexGame.bat
```

If you are on a Linux machine, you have several options to run the program:

- Run the following command to play in graphical mode with the compiled program provided:
```
java -jar HexGame.jar GRAPHIQUE
```

- Run the following command to play in console mode with the compiled program provided:
```
java -jar HexGame.jar CONSOLE
```

- Or navigate into **files/** and run run the **run.sh** script to access the main menu and choose your desired option (compile, launch game, tests, experiments):
```
cd files/
./run.sh
```

**Warning** : Experiments can take several days to complete all possible configuration combinations; it is strongly recommended to run multiple configurations in parallel to save time.

### EXPERIMENT ANALYSIS:

To perform the analysis and plot the graphs of the experimental results, you must have Python installed along with the following packages:

- matplotlib: Install using pip install matplotlib.

- pandas: Install using pip install pandas.

- seaborn: Install using pip install seaborn.

Then, select the "Generate Python graphs" functionality from the main menu of the run.sh script.


