
public class Client {
	private String nom;
	
	public Client(String nom) {
		this.setNom(nom);
	}
	
	/*Retourne le nom*/
	public String getNom(){
		return this.nom;
	}
	
	/*Change le nom par le nom recu*/
	public void setNom(String nom){
		this.nom = nom;
	}
}
