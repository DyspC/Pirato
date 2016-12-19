package gestionmap;

// Singleton

/**
 *C'est cette classe qui genere la map. C'est un singleton qui sera notamment régulierement appele par Plan pour le dessin des personnages et PiratoControler pour la gestion des objets
 */
public class Map {
		
	
	private Case tableau[][]; //contient le tableau de cases
	private int width, height;//contient la hauteur largeur du tableau (12)
	private Case spawnMoussaillon;//contient le spawn du moussaillon
	private Case spawnPirate;//contient le spawn du Pirate
	private Case spawnFantome;//contient le spawn du Fantome
	private Case grotteTresor;//contient le spawn du Tresor
	private static Map instance=null;//instance a appeler
	

	
	
	
	/**
	 * Constructeur de map
	 */
	private Map() {
		this.setup();	// Setup par defaut de la map
		
	}
	
	/**
	 * initialise la map 
	 */
	public void setup(){
		this.setWidth(12);
		this.setHeight(12);
		int[][] setupHardcodeInt={
				{0,0,0,0,0,0,0,0,0,0,0,0},	// 0 = Mer
				{0,1,1,1,1,2,1,1,1,2,1,0},	// 1 = Sentier
				{0,2,3,3,1,3,1,3,3,3,1,0},	// 2 = Cocotier
				{0,1,3,1,1,3,1,3,3,3,1,0},	// 3 = Arbre
				{0,1,3,1,3,1,1,1,2,3,1,0},	// 4 = Barque
				{0,1,1,1,1,1,3,3,1,1,1,0},
				{0,1,3,1,3,3,2,1,1,3,1,0},
				{0,2,3,1,3,3,1,3,1,3,1,0},
				{0,1,3,2,3,3,1,3,1,3,1,0},
				{0,1,3,1,3,3,1,3,1,3,1,0},
				{0,1,1,1,1,1,1,1,1,1,1,4},
				{0,0,0,0,0,0,0,0,0,0,4,4}
		};
		this.tableau = new Case[12][12];
		int i,j;
		for(i=0;i<12;i++){
			for(j=0;j<12;j++){
				switch(setupHardcodeInt[i][j]){
					case 0:
						this.tableau[i][j] = new Mer(i,j);
						break;
					case 1:
						this.tableau[i][j] = new Sentier(i,j);
						break;
					case 2:
						this.tableau[i][j] = new Cocotier(i,j);
						break;
					case 3:
						this.tableau[i][j] = new Arbre(i,j);
						break;
					case 4:
						this.tableau[i][j] = new Barque(i,j);
						break;
					default:
						System.out.println("ERR: Matrice de setup de la map mal hardcodee");
				}
			}
		}

		// Def des points de spawn
		
		this.setSpawnMoussaillon(this.tableau[10][10]);
		this.setSpawnPirate(this.tableau[3][4]);
		this.setSpawnFantome(this.tableau[1][10]);
		this.setGrotteTresor(this.tableau[3][4]);
	}

	/**
	 * tirage d'un sentier aleatoire 
	 * @return un sentier aleatoire
	 */
	public Sentier getRandomSentier(){
		int i,j;
		do{
			i=(int) Math.ceil(10*Math.random());
			j=(int) Math.ceil(10*Math.random());
		}while( !this.tableau[i][j].isSentier() );
		return (Sentier) this.tableau[i][j];
	}
	
	/**
	 * getter de case avec les coordonnees en parametre
	 * @param eRow la ligne de la case rechercee
	 * @param eCol la colonne de la case recherchee
	 * @return la case pr�sente aux coordonnees passees en parametres
	 */
	public Case getCase(int eRow, int eCol){
		return this.tableau[eRow][eCol];
	}
	
	/**
	 * getter sur le spawn pirate
	 * @return la case de spawn du pirate
	 */
	public Case getSpawnPirate() {
		return spawnPirate;
	}

	/**
	 * setter sur le spawn pirate 
	 * @param spawnPirate la case a assigner comme spawn du pirate
	 */
	public void setSpawnPirate(Case spawnPirate) {
		this.spawnPirate = spawnPirate;
	}
	/**
	 * getter sur le spawn moussaillon
	 * @return la case de spawn du moussaillon
	 */
	public Case getSpawnMoussaillon() {
		return spawnMoussaillon;
	}

	/**
	 * setter sur le spawn moussaillon
	 * @param spawnMoussaillon la case a assigner comme spawn du moussaillon
	 */
	public void setSpawnMoussaillon(Case spawnMoussaillon) {
		this.spawnMoussaillon = spawnMoussaillon;
	}
	/**
	 * getter sur le spawn fantome
	 * @return la case de spawn du fantome
	 */
	public Case getSpawnFantome() {
		return spawnFantome;
	}
	/**
	 * setter sur le spawn fantome
	 * @param spawnFantome la case a assigner comme spawn du fantome
	 */
	public void setSpawnFantome(Case spawnFantome) {
		this.spawnFantome = spawnFantome;
	}
	/**
	 * getter sur la grotte aux tresors
	 * @return la case grotte aux tresors
	 */
	public Case getGrotteTresor() {
		return grotteTresor;
	}
	/**
	 * setter sur la grotte aux tresors
	 * @param grotteTresor la case a assigner comme grotte aux tr�sors
	 */
	public void setGrotteTresor(Case grotteTresor) {
		this.grotteTresor = grotteTresor;
	}

	/**
	 * getter sur la hauteur de la map
	 * @return la hauteur de la map
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * setter sur la hauteur de la map
	 * @param height la hauteur de la map a enregistrer
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * getter sur la largeur de la map
	 * @return la largeur de la map
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * setter sur la largeur de la map
	 * @param width la largeur de la map a enregistrer
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * getter de l'instance de map (singleton)
	 * @return l'instance de map
	 */
	public static Map getInstance() {
		if (instance==null){
			instance=new Map();
		}
		return instance;
	}

	/**
	 * destruction de la map
	 */
	public static void flush() {
		Map.instance = null;	// A priori supprime ausis les cases car elle sont sens�es etre uniquement accessibles via la map

		
	}
}


