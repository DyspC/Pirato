package gestionnaires;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *C'est ici qu'on construit la Boussole qui sera ajoutée dans les boutons du GUI, qui affiche les 4 boutons de direction
 */
public class Boussole extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1658170540792185399L;
	static boolean shouldFill = true; //Ce sont des variables que j'ai gardé d'un exemple de GridBagLayout, qui notamment définissent l'utilisation des options de GridBagLayout

	/**
	 * Constructeur de Boussole, qui affiche les 4 boutons de direction
	 */
	public Boussole() {


		JButton nord = new JButton(); //Création des boutons de direction
		JButton sud = new JButton();
		JButton est = new JButton();
		JButton ouest = new JButton();

		nord.setOpaque(false);//On les rend transparent pour afficher l'image derrière
		nord.setContentAreaFilled(false);
		sud.setOpaque(false);
		sud.setContentAreaFilled(false);
		est.setOpaque(false);
		est.setContentAreaFilled(false);
		ouest.setOpaque(false);
		ouest.setContentAreaFilled(false);


		nord.addActionListener( //on attribue les bonnes actions à chaque bouton
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {GUI.getInstance().direction(0);}
				});
		est.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {GUI.getInstance().direction(1);}
				});
		sud.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {GUI.getInstance().direction(2);}
				});
		ouest.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {GUI.getInstance().direction(3);}
				});

		this.setLayout(new GridBagLayout()); //Mise en place du GridBagLayout
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			c.fill = GridBagConstraints.HORIZONTAL;
		}

		nord.setPreferredSize(new Dimension(50,50)); //Ajout des boutons selon la philosophie du BridBagLayout
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		this.add(nord, c);


		ouest.setPreferredSize(new Dimension(50,50));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		this.add(ouest, c);


		est.setPreferredSize(new Dimension(50,50));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		this.add(est, c);

		sud.setPreferredSize(new Dimension(50,50));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		this.add(sud, c);

	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g){

		int tailleX=this.getSize().width; //On dessine la boussole en fond, en centrant avec la même méthode que pour Plan
		int tailleY=this.getSize().height;
		Icon icon = new ImageIcon("./assets/boussole.png");
		int tailleIconeX=icon.getIconWidth();
		int tailleIconeY=icon.getIconHeight();

		tailleX = (tailleX>tailleIconeX) ? (tailleX-tailleIconeX)/2 : 0;
		tailleY = (tailleY>tailleIconeY) ? (tailleY-tailleIconeY)/2 : 0;

		g.setColor(Color.white); //Ici, on va ajouter les carrés blancs derrière chaque bouton car sinon ça ne supporte pas la transparance
		g.fillRect(50, 0, 50, 50);
		g.fillRect(100, 50, 50, 50);
		g.fillRect(0, 50, 50, 50);
		g.fillRect(50, 100, 50, 50);

		icon.paintIcon(this, g, tailleX, tailleY); //Puis on dessine la boussole
	}








}