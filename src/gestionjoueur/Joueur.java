package gestionjoueur;

/**
 * Classe abstraite rassemblant les personnages necessitant des actions utilisateur pour fonctionner
 */
public abstract class Joueur extends Personnage{
	
	/**
	 * Tour de jeu du Joueur
	 * Contient des blocages sur reception de message
	 */
	public abstract void tourJeu();

	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#jouer()
	 */
	public void jouer(){
		this.tourJeu();
	}


}