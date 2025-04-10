package config;

public class Constants {
    
    public static final int ITERATION_BUDGET = 1000;
    public static int GRID_SIZE = 10;
    public static final double EPLOITATION_EXPLORATION_CONSTANT = Math.sqrt(2); // constante pour la formule UCT
    public static void setGridSize(int newGridSize){
        GRID_SIZE = newGridSize;
    }

}