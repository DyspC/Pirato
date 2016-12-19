package gestionjoueur;

import gestionmap.*;


/**
 *	Factorisation de tous les personnages
 */
public abstract class Personnage {
	
	/**
	 * 	Pour gerer les inputs Joueur, présent ici pour utiliser sans cast dans le controleur
	 */
	protected Case destination;
	/**
	 * Texture du personnage
	 */
	public String texture;
	/**
	 * Position sur la map
	 */
	private Case position;
	/**
	 * Ancienne position sur la map, sert a empecher les demi tours
	 */
	private Case lastPosition;
	/**
	 * Identifiant servant a reconnaitre un joueur dans un output console
	 */
	protected int id;
	
	/**
	 * Getter de l'identifiant
	 * @return Identifiant du personnage
	 */
	public int getId() {
		return id;
	}


	/**
	 * Stocke le resultat de dé
	 * Utilisé pour {@link gestionmap.Case#atteignable(Moussaillon, int)}
	 */
	protected int resultatDe;

	/**
	 * Factorise l'appel des differents tours de jeu des personnages
	 */
	public abstract void jouer();
	
	/**
	 * Simulation du jet de dé
	 * @return  un entier aléatoire simulant un lancé de dé, dont la valeur max dépend du personnage
	 */
	protected abstract int jetDe();
	
	/**
	 * Initialisation d'un personnage
	 * @param nbMoussaillons qui sers uniquement a l'initialisation des moussaillons pour attribuer les cartes
	 */
	public abstract void init(int nbMoussaillons);
	
	
	/**
	 * Test de victoire d'un personnage 
	 * @return true si le personnage a gagné la partie
	 */
	public abstract boolean testVictoire();
	
	
	/**
	 * Getter du nombre de déplacements restants
	 * @return Nombre de déplacements restants
	 */
	public int getRestantDe(){
		return resultatDe;
	}
	
	/**
	 * Setter de la position du personnage 
	 * @param e Nouvelle position du personnage 
	 */
	protected void setPosition(Case e){		// Encapsule la maj de lastPosition // public uniquement pour les tests, protected sinon
		setLastPosition(this.position);
		if(this.position != null) this.position.retirerCol(this);
		this.position=e;
		if(e != null) this.position.ajouterCol(this);
	}
	/**
	 * Getter de position du personnage
	 * @return Position du personnage 
	 */
	public Case getPosition(){
		return this.position;
	}
	
	/**
	 * Getter de la derniere position du personnage 
	 * @return Derniere position du personnage 
	 */
	protected Case getLastPosition(){
		return this.lastPosition;
	}
	/**
	 * Setter de la derniere position du personnage 
	 * @param _p Derniere position à assigner au personnage 
	 */
	protected void setLastPosition(Case _p){
		this.lastPosition=_p;
	}
	
	/**
	 * Setter de la destination
	 * @param _destination Destination à assigner au personnage 
	 */
	public void setDestination(Case _destination){
		this.destination=_destination;
	}
	
	/**
	 * Getter de destination
	 * @return Destination du personnage 
	 */
	protected Case getDestination() {
		return this.destination;
	}
	
}
