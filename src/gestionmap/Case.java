package gestionmap;
import java.util.*;


import gestionjoueur.*;
import gestionnaires.PiratoControler;
import tresortoutseul.Tresor;

/**
 *La classe qui definit chaque case et ses comportements, notamment pour ensuite recuperer les caracteristiques de chaque case
 */
public abstract class Case {
	/**
	 * Ici on enregistre les textures de chaque case
	 */
	protected String texture;
	/**
	 * C'est ici qu'on enregistre les Personnages sur la case
	 */
	protected HashSet<Personnage> colPerso;
	/**
	 * C'est ici qu'on enregistre les Trésor sur la case
	 */
	protected HashSet<Tresor> colTresor;
	/**
	 * Ces deux entiers contiennent la position de la case
	 */
	protected int row,col;
	
	/**
	 * Constructeur de la case
	 * @param eRow la ligne de la case
	 * @param eCol la colonne de la case
	 */
	public Case(int eRow, int eCol) {
		colPerso = new HashSet<Personnage>();
		colTresor = new HashSet<Tresor>();
		row=eRow;
		col=eCol;
	}


	/**
	 * Getter sur la collection de personnages de la case
	 * @return la collection de personnages presents sur la case
	 */
	public HashSet<Personnage> getColPerso() {
		return colPerso;
	}

	/**
	 * Setter de la collection de personnages de la case
	 * @param colPerso la collection de personnages a assigner a la case
	 */
	public void setColPerso(HashSet<Personnage> colPerso) {
		this.colPerso = colPerso;
	}

	/**
	 * Getter sur la collection de tresors de la case
	 * @return la collection de tresors presents sur la cas
	 */
	public HashSet<Tresor> getColTresor() {
		return colTresor;
	}

	/**
	 * Setter de la collection de tresors de la case
	 * @param colTresor la collection de tresors a assigner a la case
	 */
	public void setColTresor(HashSet<Tresor> colTresor) {
		this.colTresor = colTresor;
	}
	
	/**
	 * Indique si le personnage peut rentrer sur la case avec son nb de deplacements restants
	 * @param e le personnage voulant entrer sur la case
	 * @param restantDe le nombre de deplacements du personnage
	 * @return vrai si la case est atteignable
	 */
	public boolean atteignable(Personnage e, int restantDe){
		//if(e.getClass() == Moussaillon.class) System.err.println("fdp de priorit� de methodes");
		return false;
	}
	
	/**
	 * Indique si le moussaillon peut rentrer sur la case avec son nb de d�placements restants
	 * @param e le moussaillon voulant entrer sur la case
	 * @param restantDe le nombre de deplacements du moussaillon
	 * @return vrai si la case est atteignable
	 */
	public boolean atteignable(Moussaillon e, int restantDe){
		return false;
	}
	/**
	 * Indique si le fantome peut rentrer sur la case avec son nb de deplacements restants
	 * @param e le fantome voulant entrer sur la case
	 * @param restantDe le nombre de deplacements du fantome
	 * @return vrai  tout le temps car le fantome peut aller partout
	 */
	public boolean atteignable(Fantome e, int restantDe){
		return true;
	}
	
	/**
	 * test si la case est un cocotier
	 * @return vrai si la case est effectivement un cocotier
	 */
	public boolean isCocotier(){
		return false;
	}
	/**
	 * test si la case est un sentier
	 * @return vrai si la case est effectivement un sentier
	 */
	public boolean isSentier(){
		return false;
	}
	
	/**
	 * getter sur la textuer
	 * @return le chemin d'acces a la texture
	 */
	public String getTexture(){
		return this.texture;
	}
	
	/**
	 * action effectuee a l'entree du pirate sur la case
	 * @param p pirate entrant sur la case
	 */
	public void action(Pirate p){
		// Tuer tous les moussaillons
		Iterator<Personnage> itPerso = colPerso.iterator();
		Personnage tmpPerso;
		while(itPerso.hasNext()){ //Parcours de la liste
			tmpPerso=itPerso.next();
			if(tmpPerso instanceof Moussaillon){
				((Moussaillon) tmpPerso).mourir();
				p.setStop(p.testVictoire());
			}
		}
		if(!colTresor.isEmpty() && this != Map.getInstance().getGrotteTresor()){
			// Déplacment des trésors à la grotte
			Iterator<Tresor> itTresor = colTresor.iterator();
			Tresor tmpTresor;
			while(itTresor.hasNext()){
				tmpTresor=itTresor.next();
				itTresor.remove();		// On delink le Tresor avant de le deplacer pour ne pas faire d'acces concurrent a la collection
				tmpTresor.setPosition((Sentier)Map.getInstance().getGrotteTresor());
				p.setStop(true);// on stoppe le pirate qui passe sur un coffre
			}
		}
		PiratoControler.majAffichage();
	}
	
	/**
	 * Action effectuee a l'arrivee d'un moussaillon sur la case
	 * @param e moussaillon entrant sur la case
	 */
	public void action(Moussaillon e){
		// Si il y  aun tresor sur la case, essayer de le prendre
		if(!this.colTresor.isEmpty()){
			e.prendreTresor(this.colTresor.iterator().next());
		}
		PiratoControler.majAffichage();
	}
	
	/**
	 * Action a effectuer a l'arrivee du fantome sur la case
	 * @param e le fantome entrant sur la case
	 */
	public void action(Fantome e){
		// Faire tomber les tresors de tous les moussaillons
		Iterator<Personnage> itPerso = colPerso.iterator();
		Personnage tmpPerso;
		while(itPerso.hasNext()){
			tmpPerso=itPerso.next();
			if(tmpPerso.getClass()==Moussaillon.class) ((Moussaillon) tmpPerso).lacherTresor();
		}
		// Tp tous les tresors
		Iterator<Tresor> itTresor = colTresor.iterator();
		Tresor tmpTresor;
		Sentier buffer;
		while(itTresor.hasNext()){
			tmpTresor=itTresor.next();
			itTresor.remove();		// On delink le Tresor avant de le deplacer pour ne pas faire d'acces concurrent a la collection
			do{
				buffer=Map.getInstance().getRandomSentier();
			}while(buffer == tmpTresor.getPosition());
			tmpTresor.setPosition(Map.getInstance().getRandomSentier());	
			
		}
		PiratoControler.majAffichage();
	}

	/**
	 * ajouter un personnage a la case
	 * @param e le personnage a enregistrer comme etant sur la case
	 */
	public void ajouterCol(Personnage e){
		colPerso.add(e);
	}
	
	/**
	 * ajouter un tresor a la case
	 * @param e le tresor a enregistrer comme �tant sur la case
	 */
	public void ajouterCol(Tresor e){
		colTresor.add(e);
	}
 
	/**
	 * retirer un personnage de la case
	 * @param e le personnage a retirer de la case
	 */
	public void retirerCol(Personnage e){
		colPerso.remove(e);
	}
	
	/**
	 * retirer un tresor de la case
	 * @param e Le tresor a retirer de la case
	 */
	public void retirerCol(Tresor e){
		colTresor.remove(e);
	}
	
	
	/**
	 * Calcul de portee necessaire pour le moussaillon
	 * @param e Seconde case de la comparaison
	 * @return Minimum des distances sur chaque axe
	 * @see gestionjoueur.Moussaillon#chercherMoussaillon(Case, int)
	 */
	public int distMin(Case e){
		return Math.min(Math.abs(this.getRow()-e.getRow()), Math.abs(this.getCol()-e.getCol()));
	}
	
	/**
	 * Norme infinie sur l'espace de la map. Necessaire pour le fantôme
	 * @param e Seconde case de la comparaison
	 * @return Maximum des distances sur chaque axe
	 * @see gestionjoueur.Moussaillon#chercherMoussaillon(Case, int)
	 */
	public int distMax(Case e){
		return Math.max(Math.abs(this.getRow()-e.getRow()), Math.abs(this.getCol()-e.getCol()));
	}

	/**
	 * Getter sur la ligne
	 * @return la ligne de la case
	 */
	public int getRow(){
		return this.row;
	}
	
	
	/**
	 * Getter sur la colonne
	 * @return la colone de la case
	 */
	public int getCol(){
		return this.col;
	}
		// Methodes d'acces de proche en proche entre cases
	/**
	 * Acces a la case au nord de la case courante
	 * @return la case au nord si elle existe
	 * @throws DemandeCaseInvalide Si on est au bord
	 */
	public Case north() throws DemandeCaseInvalide{
		if(this.row<=0) throw new DemandeCaseInvalide();
		return Map.getInstance().getCase(this.row-1, this.col);
	}
	/**
	 * Acces a la case au sud de la case courante
	 * @return la case au sud si elle existe
	 * @throws DemandeCaseInvalide Si on est au bord
	 */
	public Case south() throws DemandeCaseInvalide{
		if(this.row>=Map.getInstance().getHeight()-1) throw new DemandeCaseInvalide();
		return Map.getInstance().getCase(this.row+1, this.col);
	}
	/**
	 * Acces a la case a l'ouest de la case courante
	 * @return la case a l'ouest si elle existe
	 * @throws DemandeCaseInvalide Si on est au bord
	 */
	public Case west() throws DemandeCaseInvalide{
		if(this.col<=0) throw new DemandeCaseInvalide();
		return Map.getInstance().getCase(this.row, this.col-1);
	}
	/**
	 * Acces a la case a l'est de la case courante
	 * @return la case a l'est si elle existe
	 * @throws DemandeCaseInvalide Si on est au bord
	 */
	public Case east() throws DemandeCaseInvalide{
		if(this.col>=Map.getInstance().getWidth()-1) throw new DemandeCaseInvalide();
		return Map.getInstance().getCase(this.row, this.col+1);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "("+this.row+","+this.col+") contient "+colTresor.size()+" tresors et "+colPerso.size()+" persos";
	}
}
