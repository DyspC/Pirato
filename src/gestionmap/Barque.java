package gestionmap;
import gestionjoueur.Fantome;
import gestionjoueur.Moussaillon;
import gestionjoueur.Personnage;

/**
 *	Classe des cases de type Barque
 */
public class Barque extends Case {
	/**
	 * Constructeur de barque
	 * @param eRow la ligne de la barque
	 * @param eCol la colonne de la barque 
	 */
	public Barque(int eRow, int eCol) {
		super(eRow, eCol);
		texture="./assets/Barque.jpg";
	}

	/* (non-Javadoc)
	 * @see gestionmap.Case#atteignable(gestionjoueur.Personnage, int)
	 */
	public boolean atteignable(Personnage e, int restantDe){
		return e instanceof Fantome || (e instanceof Moussaillon && restantDe==1);
	}
}
