import java.io.BufferedReader;
import java.io.IOException;

import outilsjava.OutilsFichier;

public class LireEnvoyerCommandes {

	public static void main(String[] args) {
		String[] tableauInformation = lireFichierEtMettreDansTableau();
	}

	private static String[] lireFichierEtMettreDansTableau() {
		
		int nblignes = 0;
		String[] tableauInformation = null;
		BufferedReader informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt");
		
		if (informations == null) {
			//le fichier n'existe pas 
		} else {
			
			try {
				while (informations.readLine() != null) nblignes++; // trouver le nombre de lignes
				informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt"); //reload le buffereader pour relire les lignes
				tableauInformation = new String[nblignes];
				
				for (int i = 0; i < nblignes; i++) {
					String ligneTexte = informations.readLine();
					tableauInformation[i] = ligneTexte; 
				}//extraire toutes les lignes dans un tableau
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Erreur pour lire la ligne");
			}
		}
		return tableauInformation;
	}
	
	private boolean verifier(String[] tableauInformation) {
		if (tableauInformation[0] == "Clients:" && tableauInformation[0] == "Clients:" ) {
		}

		return true;
	}
}
