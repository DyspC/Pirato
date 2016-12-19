package tresortoutseul;

import gestionmap.*;
import gestionjoueur.*;

public class Tresor {
	
	/**Main contenant une routine de tests
	 * @param args Arguments de base
	 * @throws Exception Dans le vide
	 */
	public static void main(String[] args) throws Exception{ // Tests
		Moussaillon m1 = new Moussaillon(1);
		Moussaillon m2 = new Moussaillon(2);
		
		Sentier s1 = new Sentier(1,1);

		Tresor t1 = new Tresor(1, m1);
		Tresor t2 = new Tresor(2, s1);

		m2.prendreTresor(t1);	
		m1.lacherTresor();
		m1.prendreTresor(t2);
		m2.prendreTresor(t2);

		System.out.println(m1);
		System.out.println(m2);
		System.out.println(t1);
		System.out.println(t2);
		System.out.println();

	}
	
	
	/**
	 * Id pour output console
	 */
	private int id;
	/**
	 * Traduit l'etat du tresor
	 */
	private boolean estPorte;
	/**
	 * Sur quel case est le tresor quand estPorte = false
	 */
	private Sentier position;
	/**
	 * Quel Moussaillon porte le tresor quand estPorte = true
	 */
	private Moussaillon porteur;

	/**
	 * Crée un trésor porté par un moussaillon, seulement pour les tests
	 * @param id Identifiant
	 * @param e Porteur
	 */
	private Tresor(int id, Moussaillon e) {
		this.id=id;
		e.prendreTresor(this);
	}
	
	/**Crée un tresor posé sur une case
	 * @param id Identifiant
	 * @param e Sentier sur lequel le coffre est posé
	 */
	public Tresor(int id, Sentier e){
		this.id=id;
		this.setPosition(e);
	}

	/**
	 * Obtient le Sentier sur lequel est le tresor qu'il soit porté ou non
	 * @return Position du tresor sur la map
	 */
	public Sentier getPosition() {
		if( estPorte){
			return (Sentier) this.porteur.getPosition();
		}else{
			return position;
		}
	}

	/**
	 * Place le tresor sur une case
	 * @param position Sentier sur lequel on veut mettre le tresor
	 */
	public void setPosition(Sentier position) {
		if(this.estPorte) this.getPorteur().lacherTresor(); // ça provoque un double appel mais normalement ça bug pas vus que tomber est encapsulé dans moussaillon
		if(this.position != null) this.position.retirerCol(this);
		this.position = position;
		if(this.position != null)this.position.ajouterCol(this);	// Le if ne sert que pour les tests car ici on ne prend pas la peine de donner au moussaillon une position
		this.estPorte=false;
		this.porteur = null;
	}

	
	/**
	 * Renvoie le porteur du tresor
	 * @return le porteur s'il est porté, null sinon
	 */
	private Moussaillon getPorteur() {
		return porteur;
	}

	
	/**
	 * Passe le tresor dans l'état porté
	 * @param porteur Le nouveau porteur du tresor
	 */
	public void setPorteur(Moussaillon porteur) {
		if(this.position != null) this.position.retirerCol(this);
		this.porteur = porteur;
		this.estPorte=true;
		this.position=null;
	}
	
	/**
	 * @return true si le coffre à un porteur, false s'il est sur une case
	 */
	public boolean estPorte(){	// On en aura besoin quand on regardera parmi les tresors si un ets sur la case pour eviter que les moussaillons se volent les tresors
		return this.estPorte;
	}
	
	/**
	 * Place le tresor sur la case où il se trouve deja, 
	 * Permet d'etre appelé que le tresor soit au sol ou non
	 */
	public void tomber(){
		this.setPosition(this.getPosition());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Tresor"+this.id+": "+ ((this.estPorte)?("porté par " + this.porteur +" "):"") + "sur la case " + this.getPosition(); 
	}
}
