package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Test;

import main.LireEnvoyerCommandes;
import outilsjava.OutilsFichier;

public class LireEnvoyerCommandesTest {

	@Test
	public void testfacture() throws IOException {
		// test en blackbox
		String[] input = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger Poutine 1", "Céline Frites 2",
				"Céline Repas_Poulet 1", "Fin" }; // changer le input a guise
		String[] factureAttendu = { "Bienvenue chez Barette!", "Factures :", "", "Roger 10.50$", "Céline 20.75$",
				"Steeve 0.00$" } // le output que l'on veut avoir
		;
		changerInput(input);//changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);//appeler main pour faire le traitement
		String[] factureRecue = lireSortieFacture();//recuperer le resultat
		assertArrayEquals(factureAttendu, factureRecue);// faire le test avec le output voulu

		}
	
	private void changerInput(String[] input) throws IOException {
		BufferedWriter informationInput = OutilsFichier.ouvrirFicTexteEcriture("FichierEntree.txt");

		for (int j = 0; j < input.length; j++) {
			informationInput.write(input[j]);
			informationInput.newLine();
		}
		
		informationInput.close();
	}
	private String[] lireSortieFacture() throws IOException {
		int nblignes = 0;
		String[] factureRecue = null;
		BufferedReader informationsOutput = OutilsFichier.ouvrirFicTexteLecture("fichierSortie.txt");

		if (informationsOutput == null) {
			// le fichier n'existe pas, le message d'erreur est déjà envoyer 
		} else {
			while (informationsOutput.readLine() != null)
				nblignes++; // trouver le nombre de lignes
			informationsOutput = OutilsFichier.ouvrirFicTexteLecture("fichierSortie.txt"); // reload le buffereader pour
																							// relire les lignes
			factureRecue = new String[nblignes];

			for (int i = 0; i < nblignes; i++) {
				String ligneTexte = informationsOutput.readLine();
				factureRecue[i] = ligneTexte;
			} // extraire toutes les lignes dans un tableau

		}
		return factureRecue;
	}
}
