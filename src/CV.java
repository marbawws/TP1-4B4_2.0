
public class CV {

	private String nom;
	private String prenom;
	private String formation;
	private int anneesExperience;
	private String[] competences;
	private String attentes;

	public CV( String nom, String prenom, String formation, int anneesExperience, String[] competences,
			String attentes ) {

		this.nom = nom;
		this.prenom = prenom;
		this.formation = formation;
		this.anneesExperience = anneesExperience;
		this.competences = competences;
		this.attentes = attentes;

	}

	private void affiche() {
		String listeCompetences = "";

		for ( int i = 0; i < this.competences.length - 1; i++ ) {
			listeCompetences += this.competences[i] + ", ";
		}
		listeCompetences += this.competences[this.competences.length - 1];

		System.out.println( "\nVoici les informations de " + this.prenom + " " + this.nom + ":" );

		System.out.println( "\nNom : " + this.nom );
		System.out.println( "Prénom : " + this.prenom );
		System.out.println( "Formation : " + this.formation );
		System.out.println( "Années d'expérience : " + this.anneesExperience );
		System.out.println( "Liste des compétences : " + listeCompetences );
		System.out.println( "Attentes vis à vis le cours 4B4 : " + this.attentes );
	}

	public static void main( String[] args ) {
		CV cvSalif = new CV( "Diarra", "Salif", "Collège Montmorency", 2,
				new String[] { "Programmer", "Analyser", "Tester" }, "Créer des logiciels qui ne plantent pas !" );
		
		CV cvMarwane = new CV( "Rezgui", "Marwane", "Collège Montmorency", 1,
				new String[] { "Programmer", "Imaginer", "Conceptionner" }, "Avoir du plaisir !" );

		System.out.println( "Bienvenue chez Barette!" );

		cvSalif.affiche();
		cvMarwane.affiche();
	}

	
}
