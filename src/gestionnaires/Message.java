package gestionnaires;

import java.util.concurrent.LinkedBlockingQueue;

/**
 *	Classe permettant de formaliser la gestion des messages
 *	et de bloquer en attente d'une action
 */
public enum Message {
	resterCache (false),
	carteCocotier (false),
	cartePerroquet (false),
	nouvelleCase (false),
	lacherTresor (false),
	finDeTour (false),
	rejouer (false),
	lancerDe (false);

	/**
	 * Attribut enabled d'un message
	 */
	private boolean enabled=false; 

	/**
	 * Getter d'enable
	 * @return Valeur de enable
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Setter d'enable
	 * @param value Valeur à donner enable
	 */
	private void setEnabled(boolean value) {
		this.enabled = value;
	}

	/**
	 * Constructeur de Message
	 */
	private Message() {
	}

	/**
	 * Constructeur de message avec initialisation
	 * @param _value valeur a donner au message
	 */
	private Message(boolean _value) {
		this.enabled=_value;
	}
	
	/**
	 * BlockingQueue permettant de bloquer le fil d'execution sans utiliser d'attente active
	 */
	private static LinkedBlockingQueue<Object> wait = new LinkedBlockingQueue<Object>(1);

	/**
	 * Methode a appeler pour debloquer via message
	 * @param type Type du message envoyé pour debloquer
	 */
	public static void notifier(gestionnaires.Message type){
		if(type.isEnabled()){
			type.setEnabled(false);
			//
			//
			if(!wait.offer(new Object())){
				try {
					throw new Exception("Notification recue alors que le blocage n'est pas pret");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//
		}
	}


	/**
	 * Setter generalisé des messages
	 * @param e Valeur voulue
	 */
	private static void setAll(boolean e){
		resterCache.setEnabled(e);
		carteCocotier.setEnabled(e);
		cartePerroquet.setEnabled(e);
		nouvelleCase.setEnabled(e);
		lacherTresor.setEnabled(e);
		lancerDe.setEnabled(e);
		finDeTour.setEnabled(e);
		rejouer.setEnabled(e);
	}
	
	/**
	 * 	Attente d'un message 
	 * @param args Ensemble des messages attendus 
	 * @return Message ayant permi le deblocage
	 */
	public static Message waitMessage(Message... args){	
		Message.setAll(false);
		wait.poll();	// On poll le bloqueur pour s'assurer qu'il soit vide avant de faire le take
		for (Message type : args) {
	        type.setEnabled(true); 
	    }
		PiratoControler.majAffichage();	// On dit au controler de dire a la gui de demander a message de lui donner ses etats
		try {
			wait.take();	// on prend un object dans la file pour se bloquer dessus
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int i;
		Message m=null;
		for(i=0;i<args.length;i++){
			if(!args[i].isEnabled()) m=args[i];
		}
		Message.setAll(false);
		return m;					// On renvoie l'arg sortant
	}
	

	// Fin systeme de blocage
}