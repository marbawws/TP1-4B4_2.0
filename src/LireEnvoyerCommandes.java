import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import outilsjava.OutilsFichier;

public class LireEnvoyerCommandes {

	private static ArrayList<Commande> listeCommande = new ArrayList<Commande>();
	
	public static void main(String[] args) {
		String[] tableauInformation = lireFichierEtMettreDansTableau();
		if (verifier(tableauInformation)) {

			for (int i = 0; i < tableauInformation.length; i++) {
				System.out.println(tableauInformation[i]);
			}
			
		} else {
			System.out.println("Le fichier ne respecte pas le format demand� !\n\nArr�t du programme.");
		}
		
	}

	private static void creerObjets(String[] tableauInformation) {
		//Cr�ation des objets Client.
		
		/*
		 * Comme les commandes ont �t� valid�es dans l'�tape pr�c�dente, nous pouvons directement
		 * les placer dans des objets Commande.
		 */
		
	}
	
	private static String[] lireFichierEtMettreDansTableau() {

		int nblignes = 0;
		String[] tableauInformation = null;
		BufferedReader informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt");

		if (informations == null) {
			// le fichier n'existe pas, le message d'erreur est d�j� envoyer par soti <3
		} else {

			try {
				while (informations.readLine() != null) nblignes++; // trouver le nombre de lignes
				informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt"); // reload le buffereader pour
																							// relire les lignes
				tableauInformation = new String[nblignes];

				for (int i = 0; i < nblignes; i++) {
					String ligneTexte = informations.readLine();
					tableauInformation[i] = ligneTexte;
				} // extraire toutes les lignes dans un tableau

			} catch (IOException e) {
				
				System.out.println("Erreur survenue quand on lit la ligne");
			}
		}
		return tableauInformation;
	}
	/*Cette m�thode v�rifie si chacune des lignes du tableau envoyer en param�tre est conforme au format*/
	
	//TODO Certains cas d'erreurs ne sont pas v�rifi�s pour la liste des commandes.
	//Je sugg�re �galement d'avoir un seul �nonc� return � la fin de la m�thode.
	private static boolean verifier(String[] tableauInformation) {
		int lignePlats = 0;
		int ligneCommandes = 0;
		if (tableauInformation[0].equals("Clients :")) {

			for (int i = 0; i < tableauInformation.length && lignePlats == 0; i++) {
				if (tableauInformation[i].equals("Plats :")) {
					lignePlats = i;
				} // la ligne ou le mot Plats: se situe
			}

			for (int i = 1; i < lignePlats; i++) {
				if (tableauInformation[i].contains(" ")) {
					return false;
				} // si un nom contient un espace
			}

			for (int i = 0; i < tableauInformation.length && ligneCommandes == 0; i++) {
				if (tableauInformation[i].equals("Commandes :")) {
					ligneCommandes = i;
				} // la ligne ou le mot Commandes: se situe
			}

			for (int i = lignePlats + 1; i < ligneCommandes; i++) {
				if (!tableauInformation[i].matches("^.[a-zA-Z_]+ [0-9.]+$")) {
					return false;
				} // si un plat contient un espace avant le nombre
			}
			if (!tableauInformation[tableauInformation.length - 1].equals("Fin")) {
				return false;
			}
			
			for (int i = ligneCommandes + 1; i < tableauInformation.length - 1 ; i++) {
				if (!tableauInformation[i].matches("^.[a-zA-Z\u00C0-\u00FF]+ [a-zA-Z_]+ [0-9]+$")) {
					return false;
				} // si nom espace plat espace nombre
			}

		} else {

			return false;
		}

		return true;
	}
}
