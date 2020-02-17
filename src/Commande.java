
public class Commande {
	
	private String nomClient;
	private String nomPlat;
	private int nbPlats;
	private double prixPlat;
	
	/*Par défaut, toutes les commandes ont un prix de 0$*/
	public Commande(String nomClient, String nomPlat, int nbPlats) {
		this.setNomClient(nomClient);
		this.setNomPlat(nomPlat);
		this.setNbPlats(nbPlats);
		this.setPrixPlat(0);
	}

	/* Retourne le nom du client */
	public String getNomClient() {
		return this.nomClient;
	}

	/* Change le nom du client par le nom recu */
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	/* Retourne le nom du plat */
	public String getNomPlat() {
		return this.nomPlat;
	}

	/* Change le nom du plat par le nom du plat recu */
	public void setNomPlat(String nomPlat) {
		this.nomPlat = nomPlat;
	}
	
	/* Retourne le nombre de plat de la commande */
	public int getNbPlats() {
		return this.nbPlats;
	}

	/* Change le nombre de plat par le nombre du plat recu */
	public void setNbPlats(int nbPlats) {
		this.nbPlats = nbPlats;
	}
	
	/* Retourne le nombre de plat de la commande */
	public double getPrixPlat() {
		return this.prixPlat;
	}

	/* Change le nombre de plat par le nombre du plat recu */
	public void setPrixPlat(double prixPlat) {
		this.prixPlat = prixPlat;
	}
}
