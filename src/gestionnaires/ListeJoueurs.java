package gestionnaires;

import java.util.ArrayList;
import gestionjoueur.Personnage;

/**
 * Classe collection etant lisible en boucle
 */
public class ListeJoueurs {
	
	/**
	 * Collection des joueurs
	 */
	private static ArrayList<Personnage> liste;
	/**
	 * Index où prendre le eprsonnage suivant
	 */
	private static int index=0;
	/**
	 * Instance de ListeJoueurs (Pattern Singleton)
	 */
	private static ListeJoueurs instance = null;	

	/**
	 * Reinitialise la liste
	 */
	public static void flush(){
		liste.clear();
		index = 0;
		instance=null;
	}

	/**
	 * Constructeur et initialisation
	 */
	private ListeJoueurs(){
		ListeJoueurs.index=0;
		ListeJoueurs.liste = new ArrayList<Personnage>();
	}
	
	/**
	 * Renvoie l'instance de la liste des joueurs
	 * @return Liste des joueurs 
	 */
	public static synchronized ListeJoueurs getInstance(){			
		if (instance == null){
		 	instance = new ListeJoueurs();	
		}
		return instance;
	}

	/**
	 * Ajoute d'un personnage à la liste des personnages
	 * @param e Personnage à ajouter à la liste
	 */
	public static void ajouterPerso(Personnage e){
		if(liste==null) liste = new ArrayList<Personnage>();
		liste.add(e);
	}
	
	
	/**
	 * Enleve un personnage de la liste
	 * @param e Personnage à retirer de la liste
	 */
	public static void enleverPerso(Personnage e){
		if(liste.indexOf(e)<index) index--;
		liste.remove(e);
	}
	
	/**
	 * Passage au personnage suivant 
	 * @return Personnage suivant dans la liste ( renvoie le premier si on cherche le suivant du dernier)
	 * @throws Exception Si on cherche a acceder a la liste quand elle est vide
	 */
	public static Personnage persoSuivant()throws Exception{
		if(liste.isEmpty()) throw new Exception("Liste vide");
		return liste.get((index++)%liste.size());
	}


}
