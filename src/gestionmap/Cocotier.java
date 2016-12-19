package gestionmap;

import gestionjoueur.*;

/**
 *	Classe des cases de type Cocotier
 */
public class Cocotier extends Sentier {

	
	
	/**
	 * Constructeur de Sentier 
	 * @param eRow la ligne du sentier
	 * @param eCol le colonne du sentier
	 */
	public Cocotier(int eRow, int eCol) {
		super(eRow, eCol);
		texture="./assets/Cocotier.jpg";
	}

	/* (non-Javadoc)
	 * @see gestionmap.Case#atteignable(gestionjoueur.Moussaillon, int)
	 */
	public boolean atteignable(Moussaillon e, int restantDe){
		return true;
	}
	/* (non-Javadoc)
	 * @see gestionmap.Case#isCocotier()
	 */
	public boolean isCocotier(){
		return true;
	}
}
