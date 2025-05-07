package vue;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.Controller;



/**
 * Cette classe gère les panneaux de l'interface utilisateur dans l'application.
 * Elle permet de créer plusieurs panneaux, y compris le panneau d'accueil, le panneau de configuration des joueurs, 
 * et d'autres panneaux fonctionnels comme ceux du menu et des options.
 */
public class Panels extends JPanel {
	/**
	 * Attribut utilisé pour la gestion de la version de la classe, nécessaire pour la sérialisation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Le contrôleur qui gère l'interaction entre la vue et la logique du jeu.
	 */
	private Controller controller;

	/**
	 * Contraintes de mise en page utilisées pour positionner les composants dans le layout GridBagLayout.
	 */
	private GridBagConstraints gbc;

	/**
	 * Contraintes de mise en page utilisées pour les boutons dans le panel.
	 */
	private GridBagConstraints boutonsGbc;

	/**
	 * Panel contenant les boutons pour les actions principales du jeu (par exemple "Jouer", "Quitter").
	 */
	private JPanel bouttonsPanel;

	/**
	 * Zone de texte utilisée pour afficher la description ou d'autres informations textuelles.
	 */
	private JTextPane descriptionPane;

	/**
	 * Label affichant le titre de la fenêtre ou de la section de l'interface.
	 */
	private JLabel titre;

	/**
	 * Label contenant le logo de l'application ou du jeu.
	 */
	private JLabel logo;

	/**
	 * Barre de menu en haut de la fenêtre, contenant des menus comme "Aide" et "À propos".
	 */
	private JMenuBar barreDeMenu;

	/**
	 * Menu "À propos" dans la barre de menu, donnant des informations sur l'application.
	 */
	private JMenu menuApropos;

	/**
	 * Menu "Aide" dans la barre de menu, offrant des informations d'aide.
	 */
	private JMenu menuAide;

	/**
	 * Icône utilisée pour le bouton "Jouer", représentant l'action de commencer une partie.
	 */
	private ImageIcon play;

	/**
	 * Icône utilisée pour le bouton "Quitter", représentant l'action de quitter l'application.
	 */
	private ImageIcon quitter;

	/**
	 * Bouton permettant au joueur 1 de réaliser une action ou d'effectuer un choix dans l'interface.
	 */
	private JButton boutonJoueur1;

	/**
	 * Bouton permettant au joueur 2 de réaliser une action ou d'effectuer un choix dans l'interface.
	 */
	private JButton boutonJoueur2;

	/**
	 * Bouton permettant de passer à la prochaine étape ou de valider une action dans l'interface.
	 */
	private JButton boutonSuivant;

	/**
	 * Panel contenant les options de configuration des joueurs (par exemple "Humain", "Aléatoire", "MCTS").
	 */
	private JPanel optionsPanel = new JPanel(new GridBagLayout());

	/**
	 * Référence au dernier bouton cliqué pour afficher ou masquer les options de joueur.
	 */
	private JButton dernierBoutonClique = null;

	/**
	 * Tableau contenant les choix des joueurs pour chaque joueur (par exemple "Humain", "Aléatoire", etc.).
	 */
	private String[] choixJoueurs = {"Humain", "Humain"};

	/**
	 * Label affichant le type de joueur (par exemple "Humain", "Aléatoire") pour chaque joueur.
	 */
	private JLabel labelJoueur;

	/**
	 * Couleur associée à chaque joueur, utilisée pour personnaliser l'interface graphique du jeu.
	 */
	private Color couleurJoueur;


    /**
	 * Construit un panel principal avec une barre de menu personnalisée attachée à la fenêtre principale.
	 * Ce constructeur est typiquement utilisé pour initialiser l'interface avec les options de menu.
	 *
	 * @param frame La fenêtre principale à laquelle le panel sera attaché.
	 * @param controleur Le contrôleur pour gérer les interactions de l'utilisateur.
	 */
	public Panels(JFrame frame, Controller controleur) {
	    
		barreDeMenu = new JMenuBar();
	    barreDeMenu.setOpaque(true);
	    barreDeMenu.setBackground(Color.decode("#C0392B")); 
	    barreDeMenu.setForeground(Color.WHITE);
	    barreDeMenu.setFont(new Font("Arial", Font.BOLD, 14));

	    menuApropos = new JMenu("À propos");
	    menuApropos.setOpaque(true);
	    menuApropos.setBackground(Color.decode("#C0392B"));
	    menuApropos.setForeground(Color.BLACK);
	    menuApropos.setFont(new Font("Times New Roman", Font.PLAIN, 16));

	    menuAide = new JMenu("Aide");
	    menuAide.setOpaque(true);
	    menuAide.setBackground(Color.decode("#C0392B"));
	    menuAide.setForeground(Color.BLACK);
	    menuAide.setFont(new Font("Times New Roman", Font.PLAIN, 16));

	    JMenuItem aide = new JMenuItem("Aide");
	    stylerLeMenuItem(aide);

	    JMenuItem aPropos = new JMenuItem("À propos de l'application");
	    stylerLeMenuItem(aPropos);

	    String message = "<html>"
	            + "<body style='font-family: Arial, sans-serif; margin: 20px;'>"
	            + "<h2 style='color: #2980B9;'>À propos de l'Application</h2>"
	            + "<p style='line-height: 1.6;'>"
	            + "Ce projet est un jeu de Hex développé dans le cadre de notre dernière année de licence.<br>"
	            + "Le jeu de Hex est un jeu de plateau stratégique qui oppose deux joueurs : un joueur Rouge et un joueur Bleu.<br>"
	            + "L'objectif est de relier ses deux côtés opposés du plateau en plaçant ses pions sur les cellules hexagonales.<br>"
	            + "Développé en Java, ce projet met en avant la logique, la réflexion et la prise de décision."
	            + "</p>"
	            + "<h3 style='color: #2980B9;'>Créé par :</h3>"
	            + "<ul>"
	            + "   <li>ELOUARDI Salah Eddine</li>"
	            + "   <li>QACH Yahya</li>"
	            + "   <li>MORABET Ahmed</li>"
	            + "   <li>EL-AASMI Yassine</li>"
	            + "</ul>"
	            + "<h3 style='color: #2980B9;'>Encadrant :</h3>"
	            + "<p>Mr. BRUNO Zanuttini</p>"
	            + "<div style='text-align: center; width: 100%; margin-top: 10px; color: #808080;'>"
	            + "</div>"
	            + "</body>"
	            + "</html>";

	    aPropos.addActionListener(e -> controleur.afficherMessageAPropos(message));
	    aide.addActionListener(e -> System.out.println("en cours d'implementation"));

	    menuApropos.add(aPropos);
	    menuAide.add(aide);

	    barreDeMenu.add(menuApropos);
	    barreDeMenu.add(menuAide);

	    frame.setJMenuBar(barreDeMenu);
	}

    
    
	/**
	 * Construit un panel d'accueil contenant uniquement le logo et le titre.
	 * Ce constructeur est utilisé pour afficher une zone simplifiée de l'accueil.
	 *
	 * @param title Le titre de logo à afficher dans le panel.
	 */	
	public Panels(String title) {
		
		this.setLayout(new GridBagLayout()); 
		gbc = new GridBagConstraints();
		
		ImageIcon logoIcon = ImageLoader.loadImageIcon("unicaen.png");
		Image newlogo = logoIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
		logoIcon = new ImageIcon(newlogo);
		
		logo = new JLabel(logoIcon, JLabel.CENTER);
		
		
		gbc.gridx = 0; 
		gbc.gridy = 0; 
		gbc.anchor = GridBagConstraints.CENTER; 
		this.add(logo, gbc);
		
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html"); 
		textPane.setText("<html><body style='text-align: center;'>"
				+ "Université de Caen Normandie<br>"
				+ "UFR des Sciences<br>"
				+ "Département Informatique<br>"
				+ "3éme année de licence<br>"
				+ "d'informatique</body></html>");
		
		textPane.setOpaque(false);
		
		textPane.setEditable(false);
		textPane.setBorder(null);
		
		gbc.gridy = 1; 
		this.add(textPane, gbc);
	    this.setBackground(Color.decode("#D4E6F1"));
		
	}
    
    
    
    
    
	/**
     * Construit un panel d'accueil affichant le titre, la description et les boutons principaux.
     * Ce constructeur est utilisé pour créer la vue d'accueil de l'application.
     *
     * @param controller Le contrôleur pour gérer les actions de l'utilisateur.
     * @param y L'index de ligne pour le placement dans le GridBagLayout.
     */
	 public Panels(Controller controller, int y) {
	    
		 this.controller = controller;

	    // =========== 1) Configuration du layout ===========
	    this.setLayout(new GridBagLayout());
	    gbc = new GridBagConstraints();
	    boutonsGbc = new GridBagConstraints();

	    bouttonsPanel = new JPanel(new GridBagLayout());
	    bouttonsPanel.setBackground(Color.decode("#D4E6F1"));

	    // =========== 2) Titre (en haut) ===========
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.insets = new Insets(0, 0, 0, 0);

	    ajouterTitre(
	        "Accueil : Jeu de Hex",
	        gbc,
	        this,
	        Color.decode("#2980B9"), 
	        Color.WHITE
	    );

	    // =========== 3) Description (logo hex + texte) ===========
	    descriptionPane = new JTextPane();
	    descriptionPane.setOpaque(true);
	    descriptionPane.setEditable(false);

	    descriptionPane.setContentType("text/plain");

	    ImageIcon hexIcon = ImageLoader.loadImageIcon("hex.png");
	    if (hexIcon != null) {
	        Image img = hexIcon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
	        hexIcon = new ImageIcon(img);
	    }

	    StyledDocument doc = descriptionPane.getStyledDocument();
	    SimpleAttributeSet centerSet = new SimpleAttributeSet();
	    StyleConstants.setAlignment(centerSet, StyleConstants.ALIGN_CENTER);
	    doc.setParagraphAttributes(0, doc.getLength(), centerSet, false);

	    descriptionPane.setText(""); 

	    if (hexIcon != null) {
	        descriptionPane.insertIcon(hexIcon);
	    } else {
	        try {
	            doc.insertString(doc.getLength(), "[Logo introuvable]\n", null);
	        } catch (BadLocationException e) {
	            e.printStackTrace();
	        }
	    }

	    String texteDesc = "\n\nJeu de Hex\n\n"
	        + "Le jeu de Hex est un jeu de stratégie combinatoire abstrait\n"
	        + "opposant deux joueurs. L'objectif est de relier deux bords opposés\n"
	        + "du plateau avec ses pions. Ce projet, développé dans le cadre\n"
	        + "de notre formation universitaire, met l'accent sur la logique et\n"
	        + "la réflexion. Découvrez la richesse tactique du Hex, où chaque coup\n"
	        + "peut changer le cours de la partie.";
	    try {
	        doc.insertString(doc.getLength(), texteDesc, centerSet);
	    } catch (BadLocationException e) {
	        e.printStackTrace();
	    }


	    Border ligneDeBordure = BorderFactory.createLineBorder(Color.decode("#2E86C1"), 3); 
	    Border espaceEntreTexteEtBord = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	    Border bordure = BorderFactory.createCompoundBorder(ligneDeBordure, espaceEntreTexteEtBord);
	    descriptionPane.setBorder(bordure);

	    gbc.insets = new Insets(100, 0, 0, 0); 
	    gbc.gridy = 1;
	    this.add(descriptionPane, gbc);

	    // =========== 4) Boutons Quitter / Jouer ===========

	    
	    boutonsGbc.gridx = 0;
	    boutonsGbc.gridy = 0;
	    boutonsGbc.insets = new Insets(5, 5, 5, 5);

	    quitter = ImageLoader.loadImageIcon("quitter.png");
	    ajouterBouton(
	        bouttonsPanel,
	        "Quitter",
	        quitter,
	        this.controller::fermerApplication,
	        boutonsGbc,
	        Color.decode("#C0392B"), 
	        120,
	        30
	    );

	    boutonsGbc.gridx = 1;
	    play = ImageLoader.loadImageIcon("play.png");

	    if (play != null) {
	        Image playImg = play.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	        play = new ImageIcon(playImg);
	    }
	    ajouterBouton(
	        bouttonsPanel,
	        "Jouer",
	        play,  
	        () -> {
	            controller.naviguer(Page.JEU);
	        },
	        boutonsGbc,
	        Color.decode("#2E86C1"), 
	        120,
	        30
	    );

	    gbc.insets = new Insets(70, 0, 0, 0);
	    gbc.gridy = 2;
	    this.add(bouttonsPanel, gbc);

	    // =========== 5) Bordure globale / Fond du panel ===========
	    
	    Border ligneDeBrd2 = BorderFactory.createLineBorder(Color.decode("#2E86C1"), 3);
	    Border raisedBevel = BorderFactory.createRaisedBevelBorder();
	    Border loweredBevel = BorderFactory.createLoweredBevelBorder();
	    Border outerCompoundBorder= BorderFactory.createCompoundBorder(ligneDeBrd2, raisedBevel);
	    Border compoundBorder= BorderFactory.createCompoundBorder(outerCompoundBorder, loweredBevel);

	    this.setBorder(compoundBorder);

	    this.setBackground(Color.decode("#D4E6F1"));
	}



    
    /**
	 * Classe utilitaire pour le chargement des icônes d'images.
	 * Fournit une méthode pour charger une icône d'image à partir d'un chemin de fichier.
	 */
	 public static class ImageLoader {

    // On met un chemin relatif à 'livraison/ressources'
    private static final String BASE_PATH = "ressources" + File.separator;

    public static ImageIcon loadImageIcon(String imageName) {
        // Ex. imageName = "hex.png"
        String path = BASE_PATH + imageName; // => "ressources/hex.png"
        File f = new File(path);

        if (!f.exists()) {
            System.err.println("Image introuvable: " + f.getAbsolutePath());
            return null; 
        }
        return new ImageIcon(path);
    }
}



    
	  /**
	     * Ajoute un bouton au panneau spécifié.
	     * Ce bouton peut être utilisé pour lancer des actions comme démarrer la partie ou quitter.
	     *
	     * @param panelDeBoutons Le panel auquel le bouton sera ajouté.
	     * @param attribut Le texte ou l'attribut du bouton.
	     * @param icon L'icône à afficher sur le bouton, si applicable.
	     * @param action L'action à exécuter lorsque le bouton est pressé.
	     * @param boutonsGbc Les contraintes de positionnement du bouton.
	     * @param couleur La couleur de fond du bouton.
	     * @param x La largeur du bouton.
	     * @param y La hauteur du bouton.
	     */
	  private void ajouterBouton(JPanel panelDeBoutons, String attribut, ImageIcon icon, Runnable action, GridBagConstraints boutonsGbc, Color couleur, int x, int y) {
		
		JButton bouton = new JButton(attribut , icon);
		if (icon != null) {
			bouton.setIcon(icon);
			bouton.setOpaque(false);
			bouton.setContentAreaFilled(false);
			bouton.setBorderPainted(false);
		}
		
		bouton.setOpaque(true);
		bouton.setForeground(Color.BLACK);
		bouton.setBorder(BorderFactory.createRaisedBevelBorder());
		bouton.setFont(new Font("serif", Font.BOLD, 12));
		bouton.setPreferredSize(new Dimension(x, y));
		bouton.setBackground(couleur);
		bouton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				bouton.setBackground(bouton.getBackground().brighter());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				bouton.setBackground(couleur);
			}
		});
		
		bouton.addActionListener(e -> action.run());
		panelDeBoutons.add(bouton , boutonsGbc);
		
		
	}
    
    
    
    /**
	 * Applique un style personnalisé aux items du menu.
	 * Modifie l'apparence des items du menu pour les rendre visuellement cohérents avec l'interface de l'application.
	 *
	 * @param menuItem L'item du menu à styliser.
	 */
	private void stylerLeMenuItem(JMenuItem menuItem) {
		
		menuItem.setBackground(new Color(210, 224, 234)); 
		menuItem.setForeground(Color.BLACK); 
		menuItem.setFont(new Font("Arial", Font.ITALIC, 14)); 
		menuItem.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); 
		menuItem.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				menuItem.setBackground(menuItem.getBackground().brighter());
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				menuItem.setBackground(new Color(210, 224, 234));			
				
			}
		});	}
	
	
	
	/**
	 * Cette méthode gère l'interface de configuration des joueurs (Joueur 1 / Joueur 2).
	 * Elle propose des boutons pour sélectionner le type de joueur (Humain, Aléatoire, Robot/MCTS, Robot/RAVE) 
	 * et pour démarrer la partie.
	 * Utilise un GridBagLayout pour organiser l'interface.
	 *
	 * @param controller Le contrôleur pour gérer les actions de l'utilisateur.
	 * @param y L'index de la ligne dans le GridBagLayout où ce panel sera ajouté.
	 * @param k L'index de la colonne dans le GridBagLayout où ce panel sera ajouté.
	 * @param gamePanel Le panel de jeu à afficher lorsque le bouton "Jouer" est cliqué.
	 */
	public Panels(Controller controller , int y ,int k , GamePanel gamePanel) {
	    this.controller = controller;
	    setLayout(new GridBagLayout());

	    gbc = new GridBagConstraints();
	    boutonsGbc = new GridBagConstraints();

	    // ========== 1) Titre ==========
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.CENTER;
	    gbc.insets = new Insets(20, 0, 20, 0);

	    ajouterTitre("Configuration des Joueurs", gbc, this, Color.decode("#2980B9"), Color.WHITE);

	    // ========== 2) Message d’aide ==========
	    gbc.gridy = 1;
	    gbc.insets = new Insets(0, 10, 10, 10);

	    JLabel infoLabel = new JLabel("<html>"
	        + "<div style='text-align: center; font-family: Arial, sans-serif; font-size: 10px;'>"
	        + "<b style='color: #2E86C1;'>Choisissez le type <i>(Humain / Aléatoire / Robot/MCTS / Robot/RAVE)</i> pour chaque joueur,</b><br>"
	        + "puis cliquez sur <b style='color: #27AE60;'>\"Jouer\"</b> pour démarrer la partie.<br><br>"
	        + "<span style='color: #C0392B; font-weight: bold;'>"
	        + "Pour passer au tour suivant, cliquez sur le bouton \"Suivant\".</span>"
	        + "</div></html>"
	    );

	    infoLabel.setFont(new Font("Serif", Font.PLAIN, 10)); 
	    this.add(infoLabel, gbc);



	    // ========== 3) Panel central : Joueur 1 / Joueur 2 ==========
	    JPanel panelJoueurs = new JPanel(new GridBagLayout());
	    panelJoueurs.setBackground(Color.decode("#E0F2F1"));
	    GridBagConstraints joueursGbc = new GridBagConstraints();
	    joueursGbc.insets = new Insets(10, 10, 10, 10);

	    optionsPanel.setBackground(Color.decode("#E0F2F1"));

	    // --- Bouton Joueur 1 ---
	    joueursGbc.gridx = 0;
	    joueursGbc.gridy = 0;
	    ajouterBouton(
	        panelJoueurs, "Joueur 1", null, 
	        () -> {
	            if (optionsPanel.getParent() != null) {
	                panelJoueurs.remove(optionsPanel);
	            } else {
	                dernierBoutonClique = boutonJoueur1;
	                afficherOptionsSousLeBouton(panelJoueurs, 2, 0);
	            }
	            panelJoueurs.revalidate();
	            panelJoueurs.repaint();
	        },
	        joueursGbc, Color.decode("#5DADE2"), 120, 30
	    );
	    boutonJoueur1 = (JButton) panelJoueurs.getComponent(panelJoueurs.getComponentCount() - 1);

	    // --- Bouton Joueur 2 ---
	    joueursGbc.gridy = 1;
	    ajouterBouton(
	        panelJoueurs, "Joueur 2", null, 
	        () -> {
	            if (optionsPanel.getParent() != null) {
	                panelJoueurs.remove(optionsPanel);
	            } else {
	                dernierBoutonClique = boutonJoueur2;
	                afficherOptionsSousLeBouton(panelJoueurs, 2, 1);
	            }
	            panelJoueurs.revalidate();
	            panelJoueurs.repaint();
	        },
	        joueursGbc, Color.decode("#EC7063"), 120, 30
	    );
	    boutonJoueur2 = (JButton) panelJoueurs.getComponent(panelJoueurs.getComponentCount() - 1);

	    // Ajout du panelJoueurs
	    gbc.gridy = 2;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.insets = new Insets(10, 10, 10, 10);
	    this.add(panelJoueurs, gbc);

	    // ========== 4) Panel bas : Retour / Jouer ==========
	    JPanel panelBas = new JPanel(new GridBagLayout());
	    panelBas.setBackground(Color.decode("#E0F2F1"));
	    GridBagConstraints basGbc = new GridBagConstraints();
	    basGbc.insets = new Insets(5, 5, 5, 5);

	    basGbc.gridx = 0;
	    basGbc.gridy = 0;
	    ajouterBouton(
	        panelBas, "Retour", null, 
	        () -> controller.naviguer(Page.ACCEUIL),
	        basGbc, Color.decode("#BDC3C7"), 100, 30
	    );

	    basGbc.gridx = 1;
	    ajouterBouton(
	        panelBas, 
	        "Jouer", 
	        null,  
	        () -> {
	            gamePanel.afficherGrille();
	            afficherBoutonSuivant();  
	    	    controller.initialiserJoueurs(this);

	        },
	        basGbc, 
	        Color.decode("#27AE60"), 
	        100, 
	        30
	    );


	    gbc.gridy = 3;
	    gbc.insets = new Insets(30, 0, 20, 0);
	    this.add(panelBas, gbc);

	    // ========== 5) Panel plus bas : Suivant (Caché au début) ==========
	    JPanel plusbas = new JPanel(new GridBagLayout());
	    plusbas.setBackground(Color.decode("#E0F2F1"));
	    GridBagConstraints plusBasGbc = new GridBagConstraints();
	    plusBasGbc.insets = new Insets(5, 5, 5, 5);

	    boutonSuivant = new JButton("Suivant");
	    
	    boutonSuivant.setOpaque(true);
	    boutonSuivant.setForeground(Color.BLACK);
	    boutonSuivant.setBorder(BorderFactory.createRaisedBevelBorder());
	    boutonSuivant.setFont(new Font("serif", Font.BOLD, 12));
	    boutonSuivant.setPreferredSize(new Dimension(100, 30));
	    boutonSuivant.setBackground(Color.decode("#FFA726")); 
	    boutonSuivant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boutonSuivant.setBackground(boutonSuivant.getBackground().brighter());
			}
			@Override
			public void mouseExited(MouseEvent e) {
			    boutonSuivant.setBackground(Color.decode("#FFA726")); 
			}
		});
	    boutonSuivant.setVisible(false); 

	    boutonSuivant.addActionListener(e -> controller.demarrerPartie());
	    plusbas.add(boutonSuivant , plusBasGbc);
	

	    gbc.gridy = 4;
	    gbc.insets = new Insets(30, 0, 20, 0);
	    this.add(plusbas, gbc);

	        
	        // ========== 5) Couleur de fond globale ==========  
	        this.setBackground(Color.decode("#E0F2F1"));

	        // ========== 6) Bordure globale ==========  
	        Border lineBorder  = BorderFactory.createLineBorder(Color.decode("#2980B9"), 3);
	        Border raisedBevel = BorderFactory.createRaisedBevelBorder();
	        Border loweredBevel= BorderFactory.createLoweredBevelBorder();
	        Border outerCompound  = BorderFactory.createCompoundBorder(lineBorder, raisedBevel);
	        Border compoundBorder = BorderFactory.createCompoundBorder(outerCompound, loweredBevel);
	        this.setBorder(compoundBorder);
	    }
	

	/**
	 * Affiche le bouton "Suivant" après avoir cliqué sur "Jouer".
	 */
	private void afficherBoutonSuivant() {
	    boutonSuivant.setVisible(true);
	    this.revalidate();
	    this.repaint();
	}

	 /**
     * Cette méthode affiche les options du joueur sous le bouton spécifié.
     *
     * @param panelParent Le panel parent où afficher les options.
     * @param rowIndex L'index de la ligne où afficher les options.
     * @param joueurIndex L'index du joueur (1 ou 2).
     */
	private void afficherOptionsSousLeBouton(JPanel panelParent, int rowIndex, int joueurIndex) {
		    optionsPanel.removeAll();
		    GridBagConstraints c = new GridBagConstraints();
		    c.insets = new Insets(5, 5, 5, 5);

		    String[] noms = {"Humain", "Aléatoire", "Robot/MCTS", "Robot/RAVE"};
		    String[] fichiers = {"humain.png", "random.png", "mcts.png", "rave.png"};

		    for (int i = 0; i < noms.length; i++) {
		        JLabel lbl = creerLabelOption(noms[i], fichiers[i] , Color.decode("#E0F2F1"));
		        c.gridx = i;
		        c.gridy = 0;
		        optionsPanel.add(lbl, c);

		        final String texteOption = noms[i];
		        final int index = joueurIndex;
		        lbl.addMouseListener(new MouseAdapter() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                choixJoueurs[index] = texteOption; 
		                if (dernierBoutonClique != null) {
		                    dernierBoutonClique.setText(texteOption);
		                }
		                panelParent.remove(optionsPanel);
		                panelParent.revalidate();
		                panelParent.repaint();
		            }
		        });
		    }

		    GridBagConstraints parentGbc = new GridBagConstraints();
		    parentGbc.gridx = 0;
		    parentGbc.gridy = rowIndex;
		    parentGbc.anchor = GridBagConstraints.WEST;
		    parentGbc.insets = new Insets(10, 0, 0, 0);

		    panelParent.add(optionsPanel, parentGbc);
		    panelParent.revalidate();
		    panelParent.repaint();
		}

	
	 
	 
	 
	/**
     * Crée un label avec une icône pour afficher le type de joueur.
     * Utilisé pour créer des étiquettes pour les différents types de joueurs (Humain, Aléatoire, etc.).
     *
     * @param texteOption Le texte à afficher sur le label.
     * @param fichierImage Le nom du fichier d'image pour l'icône du label.
     * @param bgColor La couleur de fond du label.
     * @return Un JLabel configuré avec l'icône et le texte.
     */
	private JLabel creerLabelOption(String texteOption, String fichierImage, Color bgColor) {
	        ImageIcon icon = ImageLoader.loadImageIcon(fichierImage);
	        if (icon != null) {
	            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	            icon = new ImageIcon(img);
	        }
	        JLabel label = new JLabel(texteOption, icon, JLabel.CENTER);
	        label.setHorizontalTextPosition(JLabel.CENTER);
	        label.setVerticalTextPosition(JLabel.BOTTOM);
	        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        label.setOpaque(true);
	        label.setBackground(bgColor); 
	        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	        label.setFont(new Font("Arial", Font.BOLD, 14));
	        label.setForeground(Color.BLACK);
	        return label;
	    }
	    
	    
	    public String[] getChoixJoueurs() {
	        return choixJoueurs;
	    }
	 
	 
	 
	 
	    /**
	     * Panel pour Joueur 1 avec un hexagone bleu.
	     * Ce constructeur permet de créer un panel spécifique pour afficher le joueur 1 avec un hexagone bleu, 
	     * en fonction du choix du joueur (Humain, Aléatoire, Robot/MCTS, etc.).
	     *
	     * @param controller Le contrôleur pour gérer les actions de l'utilisateur.
	     * @param choixJoueur1 Le choix du joueur 1 (par exemple "Humain", "Aléatoire", "Robot/MCTS", etc.).
	     */
	    public Panels(Controller controller, String choixJoueur1) {
	        this.controller = controller;
	        setLayout(new GridBagLayout());
	        gbc = new GridBagConstraints();

	        // Définition des couleurs
	        this.setBackground(Color.decode("#D4E6F1")); // Bleu clair
	        this.setBorder(BorderFactory.createTitledBorder(
	            BorderFactory.createLineBorder(Color.decode("#154360"), 2), 
	            "🔵 Joueur 1", 2, 2, 
	            new Font("Arial", Font.BOLD, 14), Color.decode("#154360")
	        ));

	        couleurJoueur = Color.decode("#5DADE2"); // Bleu pour Joueur 1
	        String fichierImage = getIconForType(choixJoueur1);

	        labelJoueur = creerLabelOption("Joueur 1 : " + choixJoueur1, fichierImage, couleurJoueur);
	        

	        gbc.gridx = 1;
	        this.add(labelJoueur, gbc);
	    }

	    /**
	     * Panel pour Joueur 2 avec un hexagone rouge.
	     * Ce constructeur permet de créer un panel spécifique pour afficher le joueur 2 avec un hexagone rouge, 
	     * en fonction du choix du joueur (Humain, Aléatoire, Robot/MCTS, etc.).
	     *
	     * @param controller Le contrôleur pour gérer les actions de l'utilisateur.
	     * @param choixJoueur2 Le choix du joueur 2 (par exemple "Humain", "Aléatoire", "Robot/MCTS", etc.).
	     * @param isJoueur2 Un indicateur booléen pour spécifier si c'est bien le joueur 2 qui est concerné.
	     */
	    public Panels(Controller controller, String choixJoueur2, boolean isJoueur2) {
	        this.controller = controller;
	        setLayout(new GridBagLayout());
	        gbc = new GridBagConstraints();

	        // Définition des couleurs
	        this.setBackground(Color.decode("#FADBD8")); // Rouge clair
	        this.setBorder(BorderFactory.createTitledBorder(
	            BorderFactory.createLineBorder(Color.decode("#78281F"), 2), 
	            "🔴 Joueur 2", 2, 2, 
	            new Font("Arial", Font.BOLD, 14), Color.decode("#78281F")
	        ));

	        couleurJoueur = Color.decode("#EC7063"); // Rouge pour Joueur 2
	        String fichierImage = getIconForType(choixJoueur2);

	        // Création du label du joueur
	        labelJoueur = creerLabelOption("Joueur 2 : " + choixJoueur2, fichierImage, couleurJoueur);
	        
	       

	        gbc.gridx = 1;
	        this.add(labelJoueur, gbc);
	    }

	   
	    /**
	     * Associe une icône au type de joueur.
	     */
	    private String getIconForType(String typeJoueur) {
	        switch (typeJoueur) {
	            case "Aléatoire": return "random.png";
	            case "Robot/MCTS": return "mcts.png";
				case "Robot/RAVE": return "rave.png";  // Ajouté pour la version RAVE de Robot/Rave
	            default: return "humain.png";
	        }
	    }

	/**
	 * Ajoute un titre au panel spécifié.
	 * Utilisé pour les sections de l'interface utilisateur nécessitant un titre distinct.
	 * @param title Le titre à afficher.
	 * @param gbc Les contraintes de positionnement du titre.
	 * @param panel Le panel auquel le titre sera ajouté.
	 * @param couleurDeBackground La couleur de fond pour le titre.
	 * @param couleurDeForeground La couleur du texte pour le titre.
	 */
	private void ajouterTitre(String title, GridBagConstraints gbc, JPanel panel ,  Color couleurDeBackground , Color couleurDeForeground) {
		
		
		titre = new JLabel(title, SwingConstants.CENTER);
		titre.setFont(new Font("Serif", Font.BOLD, 20));
		titre.setOpaque(true);
		titre.setBackground(couleurDeBackground);  
		titre.setForeground(couleurDeForeground);  
		titre.setBorder(BorderFactory.createRaisedBevelBorder());
		
		panel.add(titre, gbc);
	}
	
}






