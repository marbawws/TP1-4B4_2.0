import java.io.BufferedReader;
import java.io.IOException;

import outilsjava.OutilsFichier;

public class LireEnvoyerCommandes {

	public static void main(String[] args) {
		String[] tableauInformation = lireFichierEtMettreDansTableau();
		if (verifier(tableauInformation)) {
			// go salifu! TODO
		}
		
	}

	private static String[] lireFichierEtMettreDansTableau() {

		int nblignes = 0;
		String[] tableauInformation = null;
		BufferedReader informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt");

		if (informations == null) {
			// le fichier n'existe pas, le message d'erreur est déjà envoyer par soti <3
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
	/*Cette méthode vérifie si chacune des lignes du tableau envoyer en paramètre est conforme au format*/
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
