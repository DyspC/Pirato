/**
 * Point d'entree de l'application 
 */
import gestionnaires.*;
/**
 * @author Clement
 *
 */
public class Launcher {

	/**
	 * Point d'entree du BigPirate
	 * Initialise la GUI et le Controleur avant de les lancer
	 * Inclut un parametre pour relancer le jeu a la fin d'une partie
	 * @param args Arguments habituels
	 */
	public static void main(String[] args) {
		boolean rejouer;
		do{
			int nbMoussaillons = GUI.getConfig();
			PiratoControler.getInstance().init(nbMoussaillons);
			GUI.getInstance().launch();
			rejouer = PiratoControler.getInstance().launch();
		}while(rejouer);
		
		System.exit(0);
		
	}

}
