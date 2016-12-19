package gestionmap;

/**
 *	Classe des cases de type Mer
 */
public class Mer extends Case {

	
	/**
	 * Constructeur de case Mer
	 * @param eRow la ligne de la case
	 * @param eCol la colonne de la case
	 */
	public Mer(int eRow, int eCol) {
		super(eRow, eCol);
		texture="./assets/Mer.jpg";
	}
	
}
