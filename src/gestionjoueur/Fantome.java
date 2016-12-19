package gestionjoueur;

import gestionmap.DemandeCaseInvalide;
import gestionmap.Map;
import gestionmap.Sentier;

/**
 *	Classe correspondant au profil de jeu Fantome
 */
public class Fantome extends NonJoueur {
	
	private static Fantome instance = null;
	
	/**
	 * Renvoie l'instance du singleton Fantome
	 * @return Instance de Fantome
	 */
	public static Fantome getInstance() {
		if (instance==null){
			instance=new Fantome();
		}
		return instance;
	}
	
	
	/**
	 * Constante du jet maximal possible
	 */
	private static final int MAX_DE=3; 
	
	/**
	 * Constructeur de Fantome
	 * Initialise sa texture et sa position
	 */
	private Fantome() { 
		texture="./assets/ElFlaqdo.png";
		this.setPosition(Map.getInstance().getSpawnFantome());
		this.setLastPosition(this.getPosition());
	}

	/* (non-Javadoc)
	 * @see gestionjoueur.NonJoueur#tourIA()
	 * 
	 * Lancer le de -> resultatDe
	 * Demander a Moussaillon la liste des cases a portée portant un moussaillon -> cibles
	 * S'il y a des cibles a proximité
	     * On en tire une au hasard et on la convertis en direction utilisable
	 * Sinon
	     * On tire une direction valide au hasard
	 * Tant que restantDe > 0
		 * Se deplacer dans la direction choisie
		 * Faire son action
		 * resultatDe--
	 */
	@Override
	public void tourIA() {
		resultatDe = jetDe();
		int direction = 0; // enum N E S W
		Sentier[] cibles=new Sentier[3];
		Sentier cibleChoisie = null;
		int nbCibles=0;
		// Chercher les joueurs a portée en ligne
		cibles = Moussaillon.chercherMoussaillon(this.getPosition(), resultatDe);
		
		if(cibles[0]!=null){// Si il y a des joueurs a portée
			// On en choisit un au hasard et on va dans sa direction
			while(cibles[nbCibles]!=null){
				nbCibles++;
			}
			cibleChoisie = cibles[(int)Math.floor(nbCibles * Math.random())];
			direction = (cibleChoisie.getRow()-this.getPosition().getRow() == 0)?((cibleChoisie.getCol()-this.getPosition().getCol() < 0)?1:3):((cibleChoisie.getRow()-this.getPosition().getRow() < 0)?0:2);
		}else{	// Sinon
			do{// On choisit une direction au hasard et on s'y deplace
				direction = this.tirerDirection();
			}while(	(direction==0 && this.getPosition().getRow()==0) || 
					(direction==2 && this.getPosition().getRow()==Map.getInstance().getHeight()-1) ||
					(direction==3 && this.getPosition().getCol()==0) || 
					(direction==1 && this.getPosition().getCol()==Map.getInstance().getWidth()-1)
				  );	// On verifie qu'on soit pas dans une direction où le fantome ne peut pas bouger
		}
		do{
			try{
				switch(direction){	// se deplacer dans la direction choisie
					case 0:
						this.setPosition(this.getPosition().north());
						break;
					case 1:
						this.setPosition(this.getPosition().east());
						break;
					case 2:
						this.setPosition(this.getPosition().south());
						break;
					case 3:
						this.setPosition(this.getPosition().west());
						break;
				}
			}catch(DemandeCaseInvalide e){
				//On ne print pas la sortie d'erreur parce qu'on se sert de la levée d'exception pour savoir qu'on va droit dans le mur
			}
			// Action de marcher sur la case
			this.getPosition().action(this);
			resultatDe--;
		}while( resultatDe > 0);

	}

	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#jetDe()
	 */
	@Override
	protected int jetDe() {
		return (int) Math.ceil(Fantome.MAX_DE*Math.random());
	}
	
	/**
	 * Methode utilisée par le fantome pour se déplacer aléatoirement
	 * @return Direction au hasard dans [[0,3]] (équiprobable)
	 */
	private int tirerDirection() {
		return (int) Math.floor(4*Math.random());
	}
	

	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#init(int)
	 */
	@Override
	public void init(int nbMoussaillons) {
		this.setPosition(Map.getInstance().getSpawnFantome());
		
	}

	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#testVictoire()
	 */
	@Override
	public boolean testVictoire() {
		return false;
	}

	/**
	 * Détruit l'instance de Fantome dans le but de revenir aux 
	 * conditions initiales dans le cas où on relance le jeu 
	 */
	public static void flush() {
		Fantome.instance=null;
	}
	
	

}
