package gestionmap;

/**
 *	Specifie que l'exception est due a une demande illegale
 */
public class DemandeCaseInvalide extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6780515197634161099L;

	/**
	 * TODO je sais pas quoi mettre pour les exceptions
	 */
	public DemandeCaseInvalide() {
		super("Demande d'acces a une case invalide");
	}

}
