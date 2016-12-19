package gestionnaires;

import gestionmap.*;

import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.*;

import gestionjoueur.Moussaillon;
import gestionjoueur.Personnage;

public class Plan extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6149890968704868087L;

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g){	
		
		int tailleX=this.getSize().width; //Servira à mettre l'image au centre de son Panel
		int tailleY=this.getSize().height;
		int propX=this.getSize().width;//Resserera l'image si elle n'a pas assez d'espace
		int propY=this.getSize().height;
		
		tailleX = (tailleX>12*64) ? (tailleX-64*12)/2 : 0; //Opérations pour connaitre le centre de l'image
		tailleY = (tailleY>12*32) ? (tailleY-32*12)/2 : 0;
		
		propX = (propX<12*64) ? 64*propX/(64*12) : 64; //opération pour savoir s'il faut redimensionner l'image, et en quelle proportions
		propY = (propY<12*32) ? 32*propY/(32*12) : 32;
		


		int i,j;  //On va parcourir les 12*12 cases de la map
		for(i=0; i<12; i++){
			for(j=0; j<12; j++){

				Icon icon = new ImageIcon(Map.getInstance().getCase(i, j).getTexture()); //On récupère le String contenant le chemin vers l'image pour chaque case (nommé ici texture)
				icon.paintIcon(this, g, j*propX+tailleX, i*propY+tailleY); //On dessine avec l'aide des paramètres ficés plus haut

				Iterator<Personnage> itPerso = Map.getInstance().getCase(i, j).getColPerso().iterator(); //ici on va parcourir la liste des personnages pour chaque case
				Personnage tmpPerso;
				while(itPerso.hasNext()){ //tant qu'il en reste
					tmpPerso=itPerso.next();
					if(tmpPerso instanceof Moussaillon){ //Si c'est un Moussaillon, il faut tester s'il est vivant
						if(((Moussaillon) tmpPerso).isAlive()){
							icon = new ImageIcon(tmpPerso.texture);
							icon.paintIcon(this, g, j*propX+tailleX, i*propY+tailleY);
						}
					} else { //Sinon, on affiche systématiquement les persos, car seul les moussaillons peuvent disparaître
						icon = new ImageIcon(tmpPerso.texture);
						icon.paintIcon(this, g, j*propX+tailleX, i*propY+tailleY);
					}
					if(tmpPerso==PiratoControler.getEnCours()){ //Ici on met un pointeur sur le perso actuellement en trrain de jouer
						icon = new ImageIcon("./assets/Pointeur.png");
						icon.paintIcon(this, g, j*propX+tailleX, i*propY+tailleY);
					}
				}
				if(!Map.getInstance().getCase(i, j).getColTresor().isEmpty()){//Ici on imprime le trésor s'il y en a un
					icon = new ImageIcon("./assets/Coffre 32x.png");
					icon.paintIcon(this, g, j*propX+tailleX, i*propY+tailleY);
				}
				
				

			}
		}


	}

}
