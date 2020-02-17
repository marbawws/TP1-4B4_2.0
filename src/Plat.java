
public class Plat {

	private double prix;
	private String nomPlat;
	
	public Plat(String nomPlat, double prix) {
		this.setPrix(prix);
		this.setNomPlat(nomPlat);
	}
	
	public Plat(String nomPlat) {
		this.setNomPlat(nomPlat);
	}

	/* Retourne le Prix */
	public double getPrix() {
		return this.prix;
	}

	/* Change le prix par le prix recu */
	public void setPrix(double prix) {
		this.prix = prix;
	}

	/* Retourne le nom du plat */
	public String getNomPlat() {
		return this.nomPlat;
	}

	/* Change le nom du plat par le nom du plat recu */
	public void setNomPlat(String nomPlat) {
		this.nomPlat = nomPlat;
	}
}
