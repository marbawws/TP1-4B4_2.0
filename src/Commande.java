
public class Commande {
	
	private double nomClient;
	private String nomPlat;
	private double nbPlats;

	/* Retourne le Prix */
	public double getPrix() {
		return this.nomClient;
	}

	/* Change le prix par le prix recu */
	public void setPrix(double prix) {
		this.nomClient = prix;
	}

	/* Retourne le nom du plat */
	public String getNomPlat() {
		return this.nomPlat;
	}

	/* Change le nom du plat par le nom du plat recu */
	public void setNomPlats(String nomPlat) {
		this.nomPlat = nomPlat;
	}
	
	/* Retourne le nombre de plat de la commande */
	public double getnbPlats() {
		return this.nbPlats;
	}

	/* Change le nombre de plat par le nombre du plat recu */
	public void setnbPlats(double nbPlats) {
		this.nbPlats = nbPlats;
	}
}
