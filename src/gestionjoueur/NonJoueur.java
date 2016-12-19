package gestionjoueur;

/**
 *	Factorisation des personnages autonomes
 */
public abstract class NonJoueur extends Personnage {

	/**
	 * Tour de jeu des personnages men�s par une IA
	 */
	public abstract void tourIA();
	
	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#jouer()
	 */
	public void jouer(){
		this.tourIA();
	}
}
