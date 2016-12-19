package gestionjoueur;

import java.io.IOException;
import gestionmap.Map;
import gestionnaires.Message;

/**
 *	Classe correspondant au profil de jeu pirate
 */
public class Pirate extends Joueur {
	/**
	 * Maximum du jet de dé
	 */
	private static final int MAX_DE=6;
	/**
	 * Pour que le pirate s'arrete en marchant sur un tresor laché
	 */
	private boolean stop=false;
	/**
	 * Instance du Pirate (pattern Singleton)
	 */
	private static Pirate instance = null;		

	/**
	 * Constructeur de Pirate
	 */
	private Pirate() {
		texture="./assets/ElPirato.png";
	}

	/**
	 * Renvoie l'instance de pirate 
	 * @return Instance de pirate
	 */
	public static synchronized Pirate getInstance()	{			
		if (instance == null){
		 	instance = new Pirate();	
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see gestionjoueur.Joueur#tourJeu()
	 * @see gestionnaires.Message#waitMessage(Message...)
	 * 
	 * Faire sauter son tour si tous les moussaillons sont morts
		 * Blocage sur lancerDe
		 * Lancer du dé -> restantDe
		 * Tant que restantDe > 0 et que action n'a pas dit au pirate de se stopper
			 * Attendre un input nouvelleCase qui ne soit un demi tour
			 * Se deplacer
			 * Faire son action
			 * restantDe--;
	     * Attendre une input finTour
	 */
	@Override
	public void tourJeu() {
		if(! testVictoire()){
			Message.waitMessage(Message.lancerDe);
			resultatDe = jetDe();
			do{
				//this.setDestination(this.getLastPosition());
				do{
					Message.waitMessage(Message.nouvelleCase);
				}while(this.getDestination()==this.getLastPosition());
	
				this.setPosition(this.getDestination());
				//  Action de marcher sur la case
				this.getPosition().action(this);
				resultatDe--;
	
			} while( resultatDe > 0 && !stop);
			Message.waitMessage(Message.finDeTour);
			this.setStop(false);
		}
	}


	/**
	 * Setter de stop
	 * @param b Nouvelle valeur de stop
	 */
	public void setStop(boolean b){
		stop=b;
	}

	/**
	 * getter de stop
	 * @return renvoie vrai si le pirate est stoppé
	 */
	public boolean getStop(){
		return stop;
	}
	
	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#jetDe()
	 */
	public int jetDe() {
		return (int) Math.ceil(MAX_DE*Math.random());
	}

	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#init(int)
	 */
	@Override
	public void init(int nbMoussaillons) {
		this.setPosition(Map.getInstance().getSpawnPirate());

	}

	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#testVictoire()
	 */
	@Override
	public boolean testVictoire() {
		int i;
		boolean notRes=false;
		for(i=0;i<Moussaillon.size();i++){ 
			try {
				notRes |= Moussaillon.get(i).isAlive();
			} catch (IOException e) {
				// On parcourt tous les moussaillons en accumulant leurs etats en vie afin de savoir s'ils sont tous morts
			}
		}
		return !notRes;
	}

	/**
	 * Détruit l'instance de pirate
	 */
	public static void flush() {
		Pirate.instance = null;
	}

	
}
