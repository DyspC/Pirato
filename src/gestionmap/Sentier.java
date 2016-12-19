package gestionmap;
import gestionjoueur.Personnage;

/**
 *	Classe des cases de type Sentier
 */
public class Sentier extends Case {
	/**
	 * constructeur de sentier
	 * @param eRow la ligne du sentier
	 * @param eCol la colonne du sentier
	 */
	public Sentier(int eRow, int eCol) {
		super(eRow, eCol);
		texture="./assets/Sentier.jpg";
	}

	/* (non-Javadoc)
	 * @see gestionmap.Case#atteignable(gestionjoueur.Personnage, int)
	 */
	public boolean atteignable(Personnage e, int restantDe){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see gestionmap.Case#isSentier()
	 */
	public boolean isSentier(){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see gestionmap.Case#toString()
	 */
	public String toString(){
		return "Sentier"+super.toString();
	}
}
