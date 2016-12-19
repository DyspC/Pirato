package gestionnaires;

import gestionjoueur.Fantome;
import gestionjoueur.Moussaillon;
import gestionjoueur.Personnage;
import gestionjoueur.Pirate;
import gestionmap.DemandeCaseInvalide;
import gestionmap.Map;
import gestionmap.Sentier;
import tresortoutseul.Tresor;


/**
 * Controleur du BigPirate,
 * Charg� de la partie logique du jeu
 *
 */
public class PiratoControler{

	/**
	 * Instance du PiratoControler (Pattern Singleton)
	 */
	private static PiratoControler instance = null;
	/**
	 * Personnage en cours d'action
	 */
	private static Personnage enCours;
	/**
	 * Stocke les appels a rejouer()
	 */
	private static boolean rejouer;
	
	/**
	 * Getter du personnage en cours
	 * @return Personnage dont c'est actuellement le tour
	 */
	public static Personnage getEnCours() {
		return enCours;
	}

	/**
	 * Receptionne depuis la GUI la demande de reinitialisation
	 */
	static void rejouer() {
		rejouer=true;
	}


	/**
	 * Getter d'instance
	 * @return Instance de PiratoControler
	 */
	public static synchronized PiratoControler getInstance() {
		if (instance==null){
			instance=new PiratoControler();
		}
		return instance;
	}

	/**
	 * Constructeur de PiratoControler
	 */
	private PiratoControler(){	
	}
	
	/**
	 * Demarrage de la partie
	 * Lancer {@link gestionnaires.PiratoControler#init(int)} Avant
	 * @return Desir de rejouer
	 */
	public boolean launch(){
		//System.out.println("Debut controler");
		Class<? extends Personnage> campGagnant;
		try {
			campGagnant = this.boucleJeu();
		} catch (Exception e) {
			e.printStackTrace();
			campGagnant = Fantome.class; // Joker
		}
		System.out.println("Jeu fini : "+campGagnant);
		GUI.victoire(campGagnant);
		rejouer=false;
		Message.waitMessage(Message.finDeTour); // fin de tour par convenance, pour eviter de faire un prompt utilisateur qui bloquerait les taches de fond
		System.out.println(rejouer);
		Moussaillon.flush();	// La partie est finie, on supprime les objets qui ne servent plus
		Pirate.flush();
		Fantome.flush();
		ListeJoueurs.flush();
		Map.flush();
		return rejouer;

	}

	/**
	 * Initialisation du controler de jeu
	 * @param nbMoussaillons le nombre de moussaillons inscrits dans la partie
	 */
	public void init(int nbMoussaillons){
		Moussaillon m;
		int i;
		Map.getInstance().setup();
		for(i=1;i<=nbMoussaillons; i++){
			try {
				m = new Moussaillon(i);
				m.init(nbMoussaillons);
				ListeJoueurs.ajouterPerso(m);
			} catch (Exception e) {	// Indice invalide
				e.printStackTrace();
			}
		}
		for(i=1;i<=nbMoussaillons; i++){
			new Tresor(i, (Sentier) Map.getInstance().getGrotteTresor());
		}
		Pirate.getInstance().init(0);
		ListeJoueurs.ajouterPerso(Pirate.getInstance());
		Fantome.getInstance().init(0);
		ListeJoueurs.ajouterPerso(Fantome.getInstance());
	}

	/**
	 * Boucle de jeu qui garantit l'enchainement des tours des personnages
	 * @return Classe du personnage ayant gagn� la partie (D'apres l'hypothese que les moussaillons jouent ensemble)
	 * @throws Exception Si le jeu n'est pas initialis� correctement
	 */
	private Class<? extends Personnage> boucleJeu() throws Exception{	// Exception si on a pas de joueurs, on est pas sens� la voir arriver
		try {
			do{
				enCours = ListeJoueurs.persoSuivant();
				
				enCours.jouer();
				GUI.maj();
			}while(!enCours.testVictoire());
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new Exception("Appel non reglementaire a boucleJeu()");
		}
		return enCours.getClass();
	}


	
	/**
	 * Relaie une demande de maj affichage � la GUI
	 */
	public static void majAffichage(){
		GUI.maj();
	}

	/**
	 * Receptionne une commande en direction depuis la GUI et si elle est valable, la relaie au joueur
	 * @param i Direction choisie par le joueur en suivant l'ordre NESW de 0 � 3
	 */
	void inputDirection(int i) { // NESW // Acces package
		try {
			//System.out.println("Commande de deplacement avec "+ PiratoControler.enCours.getRestantDe()+" restants et dep autoris� :"+Message.nouvelleCase.isEnabled());
			switch(i){
				case 0:
					if(Message.nouvelleCase.isEnabled() && PiratoControler.enCours.getPosition().north().atteignable(PiratoControler.enCours, PiratoControler.enCours.getRestantDe())){
						PiratoControler.enCours.setDestination(PiratoControler.enCours.getPosition().north());
						Message.notifier(Message.nouvelleCase);
					}
					break;
				case 1:
					if(Message.nouvelleCase.isEnabled() && PiratoControler.enCours.getPosition().east().atteignable(PiratoControler.enCours, PiratoControler.enCours.getRestantDe())){
						PiratoControler.enCours.setDestination(PiratoControler.enCours.getPosition().east());
						Message.notifier(Message.nouvelleCase);
					}
					break;
				case 2:
					if(Message.nouvelleCase.isEnabled() && PiratoControler.enCours.getPosition().south().atteignable(PiratoControler.enCours, PiratoControler.enCours.getRestantDe())){
						PiratoControler.enCours.setDestination(PiratoControler.enCours.getPosition().south());
						Message.notifier(Message.nouvelleCase);
					}
					break;
				case 3:
					if(Message.nouvelleCase.isEnabled() && PiratoControler.enCours.getPosition().west().atteignable(PiratoControler.enCours, PiratoControler.enCours.getRestantDe())){
						PiratoControler.enCours.setDestination(PiratoControler.enCours.getPosition().west());
						Message.notifier(Message.nouvelleCase);
					}
					break;
			}
		
		} catch (DemandeCaseInvalide e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}




