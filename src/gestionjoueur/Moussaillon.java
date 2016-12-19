package gestionjoueur;

import java.io.IOException;
import java.util.ArrayList;
import gestionmap.Barque;
import gestionmap.Case;
import gestionmap.Cocotier;
import gestionmap.Map;
import gestionmap.Sentier;
import gestionnaires.Message;
import tresortoutseul.Tresor;

/**
 *	Classe correspondant au profil de jeu Moussaillon 
 */
public class Moussaillon extends Joueur {
	
	/**
	 * Constante du jet maximal possible
	 */
	private static final int MAX_DE=3;

	/**
	 * Tresor porte par le moussaillon
	 */
	private Tresor tresor;
	/**
	 * Booleen traduisant l'etat du moussaillon
	 */
	private boolean isAlive;
	
	
	/**
	 * Nombre de cartes Perroquet que le moussaillon possede
	 */
	private int nbrePerroquet;
	/**
	 * Getter du nombre de cartes Perroquet restantes
	 * @return Nombre de cartes Perroquet restantes
	 */
	public int getNbrePerroquet() {
		return nbrePerroquet;
	}
	
	/**
	 * Booleen servant a stocker une utilisation de carte Perroquet
	 */
	private boolean doubleJet;

	
	/**
	 * Nombre de cartes Cocotier que le moussaillon possede
	 */
	private int nbreCocotier;
	/**
	 * Getter du nombre de cartes Cocotier restantes
	 * @return Nombre de cartes cocotier restantes
	 */
	public int getNbreCocotier() {
		return nbreCocotier;
	}
	
	/**
	 * Booleen stockant une utilisation de carte Cocotier
	 */
	private boolean isCache;

	// Memorisation des moussaillons
	/**
	 * Collection de Moussaillon 
	 * Approximation du singleton pour limiter a 3 le nombre de moussaillons
	 */
	private static ArrayList<Moussaillon> tabMoussaillon = null;

	/**
	 * Ajoute un moussaillon à la liste des moussaillons
	 * @param e Moussaillon à ajouter à la liste
	 * @throws Exception Si la collection est pleine
	 */
	static void addMoussaillon(Moussaillon e) throws Exception{
		if(tabMoussaillon == null) tabMoussaillon = new ArrayList<Moussaillon>();
		if(tabMoussaillon.size()>2) throw new Exception("Collection Moussaillon pleine");
		tabMoussaillon.add(e);
	}
	
	/**
	 * Enleve un moussaillon de la liste des moussaillons
	 * @param e Moussaillon à enlever de la liste
	 */
	static void deleteMoussaillon(Moussaillon e){
		tabMoussaillon.remove(e);
	}
	
	/**
	 * Methode pour obtenir le moussaillon à un indice precis de la liste
	 * Destine a etre appele dans {@link gestionmap.Case#action(Pirate)} pour tester la victoire
	 * @param n Indice desire
	 * @return Moussaillon présent à cet indice
	 * @throws IOException Si l'index demandé ne correspond a aucun moussaillon
	 */
	public static Moussaillon get(int n)throws IOException{		// Public pour faire des tests directs, l'enlever apres
		if(n<0 || n>tabMoussaillon.size()) throw new IOException("Index invalide");
		return tabMoussaillon.get(n);
	}
	/**
	 * Renvoie le nombre de moussaillons enregistrés
	 * @return Nombre de moussaillons présents dans la liste
	 */
	public static int size(){
		if(tabMoussaillon == null) return 0;
		return tabMoussaillon.size();
	}
	
	/**
	 * Remise à zéro de la liste des moussaillons
	 */
	public static void flush(){
		tabMoussaillon.clear();
	}
	// Fin Memorisation


	/**
	 * Constructeur de moussaillon
	 * @param _id numéro du moussaillon entre 1 et 3 
	 * @throws Exception Lorsque l'indice est hors limite
	 */
	public Moussaillon(int _id) throws Exception{	// Id doit etre entre 1 et 3 pour correspondre a un sprite
		if(_id>3 || _id<1) throw new Exception("Indice invalide");
		texture="./assets/ElGreco"+_id+".png";
		id=_id;
		setLastPosition(null);
		setPosition(null);
		try {
			Moussaillon.addMoussaillon(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#init(int)
	 */
	public void init(int nbMoussaillons){
		this.tresor=null;
		this.isAlive=true;
		this.nbreCocotier=6-nbMoussaillons;
		this.nbrePerroquet=4-nbMoussaillons;
		setPosition(Map.getInstance().getSpawnMoussaillon());
		//System.out.println("Moussaillon init : "+ this );
	}

	/* (non-Javadoc)
	 * @see gestionjoueur.Joueur#tourJeu()
	 * @see gestionnaires.Message#waitMessage(Message...)
	 * 
	 * Si il est vivant
		 * Initialiser lastPosition pour permettre un demi tour
		 * 0 -> resultatDe
		 * Si il est caché
			 * Proposer de rester caché ou de sortir via lancer de dé simple ou double
			 * Si il sort et tombe sur le pirate
			     * Mourir
		 * Si il n'est pas caché et ne s'est pas fait tuer en sortant d'un cocotier
			 * Attendre un message lancerDe ou cartePerroquet jusqu'a recevoir lancerDe
			 * Lancer le dé et l'ajouter pondéré a resultatDe
			 * Tant que resultatDe > 0 et il n'est pas caché
				 * Attendre lacherTresor ou carteCocotier ou nouvelleCase jusqu'a obtenir nouvelleCase ou carteCocotier en lancant les handlers a chaque evenement
				 * Si le message reçu est nouvelleCase et que la destination n'est pas la derniere position
					 * Se deplacer
					 * Faire son action
					 * resultatDe--
			 * Attendre un message carteCocotier ou finDeTour
			 * Si message recu est carteCocotier
				 * Se cacher
	 */
	public void tourJeu() {
		//System.out.println("Debut tour Moussaillon");
		if(this.isAlive){
			this.setLastPosition(null); // Il peut se retourner en debut de tour
			resultatDe=0;	// On l'init pour gerer correctement le cas où on sort d'un cocotier
			this.doubleJet=false;
			Message buffer=null;
			if(this.isCache){	// Le joueur est actuellement caché
				if(this.nbrePerroquet>0 ){
					buffer = Message.waitMessage(Message.resterCache, Message.lancerDe, Message.cartePerroquet);
				}else{
					buffer = Message.waitMessage(Message.resterCache, Message.lancerDe);
				}
				if(buffer ==  Message.resterCache){
					this.cacherCocotier();
				}else{
					if(buffer == Message.cartePerroquet){
						this.cartePerroquet();
					}
					isCache=false;
					resultatDe--;	// Il consomme deja un mouvement pour sortir
					if(Pirate.getInstance().getPosition() == this.getPosition()) mourir();
				}
			}
			if( !this.isCache && this.isAlive){
				
				while(buffer != Message.lancerDe){
					if(this.nbrePerroquet>0 || this.doubleJet){
						buffer = Message.waitMessage(Message.lancerDe, Message.cartePerroquet);
					}else{
						buffer = Message.waitMessage(Message.lancerDe);
					}
					if(buffer == Message.cartePerroquet) this.cartePerroquet();
				}

				resultatDe += ((doubleJet)?2:1)*this.jetDe();
				//System.out.println("ResultatDe : "+resultatDe);

				while( resultatDe > 0 && !this.isCache){
					// S'il porte un tresor, donner acces au bouton Drop // Tant qu'il est encore sur la case, il faut qu'il puisse encore ramasser
					do{
						if(this.tresor != null){
							if(this.getPosition().isCocotier() && this.nbreCocotier > 0){
								buffer = Message.waitMessage(Message.nouvelleCase, Message.lacherTresor, Message.carteCocotier);
							}else{
								buffer = Message.waitMessage(Message.nouvelleCase, Message.lacherTresor);
							}
						}else{
							if(this.getPosition().isCocotier() && this.nbreCocotier > 0){
								buffer = Message.waitMessage(Message.nouvelleCase, Message.carteCocotier);
							}else{
								buffer = Message.waitMessage(Message.nouvelleCase);
							}
						}
						switch(buffer){
							case carteCocotier:
								this.cacherCocotier();
								break;
							case lacherTresor:
								this.lacherTresor();
								break;
							default:
								break;
						}
						
					}while(buffer == Message.lacherTresor);
					
					if(buffer == Message.nouvelleCase && this.getDestination() != this.getLastPosition()){
						this.setPosition(this.getDestination());
						
						// Penser au fait que le joueur ne puisse monter sur la barque que si son nb de deplacements restant vaut 1
						this.getPosition().action(this);
						// S'il marche sur un trésor et qu'il n'en porte pas déjà un, il le prend
						resultatDe--;
					}
				}
				if(!this.isCache && this.getPosition() instanceof Cocotier && this.nbreCocotier>0){
					buffer = Message.waitMessage(Message.finDeTour, Message.carteCocotier);
				}else{
					buffer = Message.waitMessage(Message.finDeTour);
				}
				if(buffer == Message.carteCocotier) this.cacherCocotier();
			}
		}
	}

	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#jetDe()
	 */
	protected int jetDe() {
		return (int) Math.ceil(Moussaillon.MAX_DE*Math.random());
	}

	/**
	 * Action executée si on recois un input carte Cocotier
	 * @return vrai si le moussaillon se trouve bien sur une case cocotier et s'il lui reste des cartes
	 */
	private boolean cacherCocotier(){
		if( this.getPosition().isCocotier() && this.nbreCocotier > 0){
			this.nbreCocotier--;
			this.isCache=true;
			return true;//TODO verifier l'interet du return
		}else{
			this.isCache=false;		// Barriere de secours parce que de toute façon si on arrive ici ça devrait etre a faux deja
			return false;
		}
	}

	/**
	 * Action executée sur input carte Perroquet
	 */
	private void cartePerroquet(){	// Pensee pour etre lancee dans tous les cas quand on clique sur le bouton perroquet et gere le toggle d'etat
		if(this.doubleJet){					// Si on utilise deja une carte, on la reprend
			this.nbrePerroquet++;
			this.doubleJet=false;
		}else if(this.nbrePerroquet > 0){	// Si on utilise pas encore une carte et qu'il nous en reste, on double et on la consomme
			this.nbrePerroquet--;
			this.doubleJet=true;
		}
	}

	/**
	 * Possibilité de lacher un trésor pour gener le pirate
	 */
	public void lacherTresor(){
		if( this.tresor != null){
			Tresor t=this.tresor;
			this.tresor=null;
			t.tomber();
		}
	}

	/**
	 * Ramasse un trésor présent sur la case courante
	 * @param e Trésor à ramasser
	 */
	public void prendreTresor(Tresor e){
		if(!e.estPorte()){				// Les moussaillons ne peuvent aps se prendre les tresors
			if( this.tresor==null){
				this.tresor=e;
				e.setPorteur(this);
			}else{		// En cours de partie on ne devrait pas rentrer ici mais ça permet de faire les tests avec plus de flexibilité
				e.setPosition((Sentier)getPosition());
			}
		}else{
			System.err.println("dewa shikei !");
		}
	}

	/**
	 * Cherche les moussaillons à portée de la case argument, utilisée par le fantome qui cherche les moussaillons
	 * @param ePosition position à partir de laquelle on cherche les moussaillons à portée
	 * @param portee portee de déplacement 
	 * @return Tableau de sentiers 
	 * @see gestionjoueur.Fantome#tourIA()
	 */
	public static Sentier[] chercherMoussaillon(Case ePosition, int portee){
		int i;
		int j=0;
		Sentier[] res = new Sentier[3];	// Simulation de Bag pour permetre au fantome de ponderer son tirage
		for(i=0;i<tabMoussaillon.size();i++){
			try {
				if(Moussaillon.get(i).getPosition().distMin(ePosition) == 0 && Moussaillon.get(i).getPosition().distMax(ePosition) <= portee && Moussaillon.get(i).getPosition().distMax(ePosition) > 0) // On exclut le cas de d1 nulle, cette fonction a pour but de faire generer un deplacement
					res[j++] = (Sentier) Moussaillon.get(i).getPosition();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(i=j;i<3;i++) res[i]=null;

		return res;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Moussaillon"+this.id+": nbCoc: "+this.nbreCocotier+" isCache: "+this.isCache+" nbPerro: "+this.nbrePerroquet+" porteUnTresor: "+((this.tresor!=null)?"true":"false" );
	}

	/**
	 * Getter de l'état du moussaillon
	 * @return true si le moussaillon est encore en vie, false sinon
	 */
	public boolean isAlive(){
		return this.isAlive;
	}

	/**
	 * Fais mourir le moussaillon et lui fait lacher son éventuel trésor
	 */
	public void mourir() {
		if(!this.isCache){
			this.isAlive=false;
			this.lacherTresor();
		}
	}
	/* (non-Javadoc)
	 * @see gestionjoueur.Personnage#testVictoire()
	 */
	@Override
	public boolean testVictoire() {
		return (this.getPosition().getClass() == Barque.class) && this.tresor != null;	// Si on est sur la barque avec un tresor
	}

}
