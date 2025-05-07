package vue;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

import controller.Controller;

public class GrillePanel extends JPanel {

	/**
	 * Attribut utilisé pour la gestion de la version de la classe, nécessaire pour la sérialisation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Taille de la grille, représentant le nombre de lignes et de colonnes dans la grille hexagonale.
	 */
	private final int taille;  

	/**
	 * Taille des hexagones, utilisé pour calculer la largeur et la hauteur des hexagones.
	 */
	private static final int HEX_SIZE = 17;

	/**
	 * Largeur d'un hexagone, calculée à partir de la taille des hexagones.
	 */
	private static final int HEX_WIDTH = (int) (Math.sqrt(3) * HEX_SIZE);

	/**
	 * Hauteur d'un hexagone, calculée à partir de la taille des hexagones.
	 */
	private static final int HEX_HEIGHT = (int) (1.5 * HEX_SIZE);

	/**
	 * Le contrôleur qui gère les interactions entre la vue et la logique du jeu.
	 */
	private final Controller controller;

	/**
	 * Map contenant les hexagones de la grille, utilisés pour détecter les clics de l'utilisateur sur les cases hexagonales.
	 * La clé est une chaîne représentant les coordonnées (row,col), et la valeur est le Polygon de chaque hexagone.
	 */
	private final Map<String, Polygon> hexagonesMap;

	/**
	 * Tableau qui stocke l'état de chaque case hexagonale de la grille.
	 * Chaque case peut avoir un état comme "VIDE", "BLEU", "ROUGE", etc.
	 * Les indices [row][col] représentent les coordonnées de chaque case dans la grille.
	 */
	private final String[][] etatsCases;

	
	
	/**
	 * Constructeur de la classe GrillePanel qui initialise les attributs et crée la grille de jeu.
	 * Il configure également la taille de la grille et initialise les cases à "VIDE".
	 *
	 * @param controller Le contrôleur qui gère les interactions avec la vue.
	 */
    public GrillePanel(Controller controller) {
        
    	this.controller = controller;
        // On récupère la taille depuis le Controller
        this.taille = this.controller.getTaille();

        this.hexagonesMap = new HashMap<>();
        this.etatsCases = new String[taille][taille + taille - 1];
        
        // Initialiser tout à "VIDE"
        for (int row = 0; row < taille; row++) {
            for (int col = 0; col < taille; col++) { 
                etatsCases[row][col] = "VIDE";
            }
        }

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(800, 600));
    }

    
    
    /**
     * Cette méthode est appelée pour dessiner le contenu du panneau.
     * Elle appelle la méthode pour dessiner la grille hexagonale centrée.
     *
     * @param g Le contexte graphique dans lequel la grille est dessinée.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dessinerGrilleCentree(g);
    }

    /**
     * Dessine la grille en deux passes :
     *   - Calcul de la bounding-box brute pour positionner les hexagones.
     *   - Centrage de la grille sur le panneau en calculant les décalages nécessaires.
     *   
     * @param g Le contexte graphique dans lequel la grille est dessinée.
     */
    private void dessinerGrilleCentree(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        Map<String, Point> centres = new HashMap<>();
    
        final int hexWidth = HEX_WIDTH;
        final int hexHeight = HEX_HEIGHT;
    
        // First pass: Calculate positions
        for (int row = 0; row < taille; row++) {
            for (int col = 0; col < taille; col++) { // All rows have taille columns
                // Parallelogram offset: Each row is shifted right by (row * hexWidth/2)
                int posX = col * hexWidth + row * (hexWidth / 2);
                int posY = row * hexHeight;
    
                // Track min/max for centering
                minX = Math.min(minX, posX);
                maxX = Math.max(maxX, posX);
                minY = Math.min(minY, posY);
                maxY = Math.max(maxY, posY);
    
                centres.put(row + "," + col, new Point(posX, posY));
            }
        }
    
        // Center the grid
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int startX = (panelWidth - (maxX - minX + hexWidth)) / 2 - minX;
        int startY = (panelHeight - (maxY - minY + hexHeight)) / 2 - minY;
    
        hexagonesMap.clear();
    
        // Second pass: Draw hexagons
        for (int row = 0; row < taille; row++) {
            for (int col = 0; col < taille; col++) {
                Point center = centres.get(row + "," + col);
                int posX = center.x + startX;
                int posY = center.y + startY;
    
                Polygon hex = creerHexagone(posX, posY);
                hexagonesMap.put(row + "," + col, hex);
    
                g2.setColor(getColorForCase(etatsCases[row][col]));
                g2.fillPolygon(hex);
                g2.setColor(Color.BLACK);
                g2.drawPolygon(hex);
            }
        }
    }
    
    
    
    
    /**
     * Crée un Polygon représentant un hexagone autour du centre (cx, cy).
     * L'hexagone est construit avec les points x et y correspondant à un hexagone régulier.
     * 
     * @param cx La coordonnée x du centre de l'hexagone.
     * @param cy La coordonnée y du centre de l'hexagone.
     * @return Un objet Polygon représentant l'hexagone.
     */
    private Polygon creerHexagone(int cx, int cy) {
        int[] xPoints = {
            cx,
            cx + HEX_WIDTH/2,
            cx + HEX_WIDTH/2,
            cx,
            cx - HEX_WIDTH/2,
            cx - HEX_WIDTH/2
        };
        int[] yPoints = {
            cy - HEX_SIZE,
            cy - HEX_SIZE/2,
            cy + HEX_SIZE/2,
            cy + HEX_SIZE,
            cy + HEX_SIZE/2,
            cy - HEX_SIZE/2
        };
        return new Polygon(xPoints, yPoints, 6);
    }

    /**
     * Convertit l'état d'une case en une couleur Java.
     * L'état "BLEU" sera converti en bleu, "ROUGE" en rouge et "VIDE" en gris clair.
     *
     * @param etat L'état de la case ("BLEU", "ROUGE", "VIDE").
     * @return La couleur correspondant à l'état de la case.
     */
    private Color getColorForCase(String etat) {
        switch (etat) {
          case "BLEU":
           return Color.BLUE;
          case "ROUGE":
           return Color.RED;
          default:
           return Color.LIGHT_GRAY;
          
       }

    }

    /**
     * Change la couleur de la case spécifiée par ses indices (row, col) et redessine la grille.
     * 
     * @param row L'indice de la ligne de la case.
     * @param col L'indice de la colonne de la case.
     * @param etat L'état à assigner à la case ("BLEU", "ROUGE", etc.).
     */
    public void colorerCase(int row, int col, String etat) {
        if (row >= 0 && row < taille && col >= 0 && col < taille) { // Fix: 0 ≤ col < taille
            etatsCases[row][col] = etat;
            repaint();
        } else {
            System.err.println("[GrillePanel] Indices hors limites : ("+row+","+col+").");
        }
    }
}
