package gestionnaires;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import gestionjoueur.Moussaillon;
import gestionjoueur.Personnage;


/**
 * Gestionnaire de l'IHM du BigPirate,
 * Charge de la partie affichage et reception des input du jeu
 */
public class GUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -215114960607100367L;
	private static GUI instance=null;
	
	
	/**
	 * Ici, la methode associee au patron singleton permettant de retourner l'instance de GUI
	 * @return GUI, l'unique instance associee
	 */
	public static GUI getInstance(){
		if(instance == null) instance = new GUI("BigPirato");
		return instance;
	}

	static private JPanel controles=new JPanel();//Panneau des controles situé en dessous de l'affichage de la carte
	static private JPanel boutons=new JPanel();//Panneaux des boutons de commande situé dans le panneau de controles
	static private Boussole boussole=new Boussole();//Instance de la classe Boussole attaché à ce GUI. Il se situera aussi dans le panneau de controle
	static private JPanel etat=new JPanel();//Panneau au dessus de la carte qui affiche les différents état des joueurs

	static private JButton de = new JButton("Lancer de"); //Bouton permettant de lancer le dé, affiché à part dans les controles
	static private JButton rester = new JButton("Rester cache");//Bouton pour rester caché, dans le pannneau des boutons
	static private JButton cocotier = new JButton("Utiliser une carte cocotier");//Bouton pour rentrer dans un cocotier, dans le pannneau des boutons
	static private JButton perroquet = new JButton("Utiliser une carte perroquet");//Bouton pour utiliser une carte perroquet, dans le pannneau des boutons
	static private JButton lacher = new JButton("Lacher le tresor");//Bouton pour lacher son trésor, dans le pannneau des boutons
	static private JButton finTour = new JButton("Finir le tour");//Bouton pour déclarer une fin de tour, dans le pannneau des boutons
	
	
	static private JLabel tour = new JLabel(); //JLabel pour afficher le joueur dont c'est le tour
	static private JLabel deplacements = new JLabel();//JLabel pour afficher le nombre de déplacements restants
	static private JLabel nbrCartes = new JLabel();//JLabel pour afficher le nombre de cartes restant



	/**
	 * constructeur de GUI, qui initialise aussi l'organisation de la fenetre, notamment des layout
	 * @param name nom de la GUI
	 */
	private GUI(String name) {
		super(name);

		Plan plan = new Plan();

		controles.setLayout(new FlowLayout(FlowLayout.CENTER,30,0)); //On fixe ici les différentes Layout
		boutons.setLayout(new BoxLayout(boutons,BoxLayout.PAGE_AXIS));
		etat.setLayout(new FlowLayout(FlowLayout.CENTER,30,0));
		setLayout(new BorderLayout(0,0));

		de.addActionListener( //ici on attribute les fonctions à chaque bouton
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {Message.notifier(Message.lancerDe);
					repaint();}
				});
		rester.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {Message.notifier(Message.resterCache);
					repaint();}
				});
		cocotier.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {Message.notifier(Message.carteCocotier);
					repaint();}
				});
		perroquet.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {Message.notifier(Message.cartePerroquet);
					repaint();}
				});
		lacher.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {Message.notifier(Message.lacherTresor);
					repaint();}
				});
		finTour.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						PiratoControler.rejouer();
						Message.notifier(Message.finDeTour);
						repaint();
					}
				});
		
		
		boutons.add(rester); //ajout des boutons dans leur ensembles respectifs
		boutons.add(cocotier);
		boutons.add(perroquet);
		boutons.add(lacher);
		boutons.add(finTour);

		controles.add(boussole);
		controles.add(de);
		controles.add(boutons);

		etat.add(tour);
		etat.add(deplacements);
		etat.add(nbrCartes);


		getContentPane().add(etat, BorderLayout.CENTER);
		getContentPane().add(controles, BorderLayout.SOUTH);
		getContentPane().add(plan, BorderLayout.NORTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setMinimumSize(new Dimension(64*15,32*23));
		controles.setPreferredSize(new Dimension(64,32*7));
		plan.setPreferredSize(new Dimension(64*14,32*14));
		etat.setPreferredSize(new Dimension(64*14,35));
		this.pack(); //ici c'est l'organisation de la taille de la fenetre; on demande notamment de respecter les prefferedSize
	}

	/**
	 * Choix de la direction, avec ici 0 = nord, 1 = est, 2 = sud, 3 = ouest. Les commandes hors limites seront traitées lors de l'appel au PiratoControlleur
	 * @param i Direction pour NESW = {1,2,3,4}
	 */
	protected void direction(int i) {
		PiratoControler.getInstance().inputDirection(i);
	}



	/**
	 *Genere une fenetre qui demande le nombre de joueurs à l'utilisateur
	 * @return le nombre de moussaillons choisi par l'utilisateur
	 */
	public static int getConfig(){
		String[] choix = { "1","2","3"};
		int input = Integer.parseInt(JOptionPane.showInputDialog(null, "Choisir le nombre de joueurs moussaillons",
				"MAXXXXIMUM PIRATO", JOptionPane.QUESTION_MESSAGE, null,
				choix,
				choix[0]).toString());
		return input;
	}


	/**
	 * Affichage du gagnant, puis mise du texte du bouton de fin de partie sur Recommencer Partie si le joueur veut recommencer.
	 * @param campGagnant classe du gagnant
	 */
	public static void victoire(Class<? extends Personnage> campGagnant) {
		JOptionPane.showMessageDialog(GUI.getInstance(), "Bravo ! Victoire du camp "+campGagnant.toString().substring(campGagnant.toString().indexOf(".")+1)+".\n");
		finTour.setText("Recommencer Partie");
		
	}

	/**
	 * C'est la fonction appelee par le Launcher pour rendre le gui visible d'une part. De plus, dans le cas eventuel ou c'est une partie recommencee, on remet le texte du bouton de fin de tour.
	 */
	public void launch() {
		finTour.setText("Finir Tour");
		this.setVisible(true);
	}

	/**
	 * Ici, mise à jour de l'affichage. On active d'abord les boutons en fonction des valeurs true et false dans Message attribué à chaque type de message, pour savoir ce qu'il est demandé;
	 * puis on met a jour l'affichage des infos en haut de la fenêtre, et enfin on demande à toutes les données graphiques de se redessiner
	 */
	public synchronized static void maj(){
		de.setEnabled(Message.lancerDe.isEnabled()); //Activation ou non des boutons
		rester.setEnabled(Message.resterCache.isEnabled());
		cocotier.setEnabled(Message.carteCocotier.isEnabled());
		perroquet.setEnabled(Message.cartePerroquet.isEnabled());
		lacher.setEnabled(Message.lacherTresor.isEnabled());
		finTour.setEnabled(Message.finDeTour.isEnabled());
		deplacements.setEnabled(Message.nouvelleCase.isEnabled());

		if(PiratoControler.getEnCours() != null){ //Affichage des infos
			deplacements.setText("Il vous reste "+PiratoControler.getEnCours().getRestantDe()+" mouvements.");
			tour.setText("C'est le tour du "+PiratoControler.getEnCours().getClass().toString().substring(PiratoControler.getEnCours().getClass().toString().indexOf(".")+1)+" "+((PiratoControler.getEnCours() instanceof Moussaillon)?""+PiratoControler.getEnCours().getId():""));//Une ligne bien dense pour prendre la classe sans prendre le gestionnaire.
			if(PiratoControler.getEnCours() instanceof Moussaillon){ //Infos spécifiques au Moussaillon
				nbrCartes.setEnabled(true); //On le grise si ce n'est pas un Moussaillon
				nbrCartes.setText("Il vous reste "+((Moussaillon)PiratoControler.getEnCours()).getNbrePerroquet()+" cartes Perroquet et "+((Moussaillon)PiratoControler.getEnCours()).getNbreCocotier()+" cartes Cocotier.");
			} else {nbrCartes.setEnabled(false);}
		}

		GUI.getInstance().repaint(); //On redessine
	}
}
