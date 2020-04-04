package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Test;

import main.LireEnvoyerCommandes;
import outilsjava.OutilsFichier;

public class LireEnvoyerCommandesTest {
	
	private String[] inputOriginal = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
			"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger Poutine 1", "Céline Frites 2",
			"Céline Repas_Poulet 1", "Fin" };
	/* modele
	@Test
	public void comparerFactures() throws IOException {
		// test en blackbox
		String[] inputs = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger Poutine 1", "Céline Frites 2",
				"Céline Repas_Poulet 1", "Fin" }; // changer le input a guise
		String[] factureAttendu = { "Bienvenue chez Barette!", "Factures :", "", "Roger 10.50$", "Céline 20.75$",
				"Steeve 0.00$" } // le output que l'on veut avoir
		;
		changerInput(inputs);//changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);//appeler main pour faire le traitement
		String[] factureRecue = lireSortieFacture();//recuperer le resultat			
		assertArrayEquals(factureAttendu, factureRecue);// faire le test avec le output voulu
	}
	*/
	
	@Test
	public void testerCommandesIncorrectes() throws IOException {
		// test en blackbox
		String[] input1 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger boisson 4", "Céline glace 2",
				"Steeve croquettes 2", "Fin" }; // cas:1 les noms des plats n'existe pas
		String[] input2 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "bob Poutine 4", "bobbette Frites 2",
				"bobino Repas_Poulet 2", "Fin" }; // cas:2 les noms des clients n'existe pas
		String[] input3 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "bob boisson 4", "bobbette glace 2",
				"bobino croquettes 2", "Fin" }; // cas:3 les noms des plats et les noms des clients n'existent pas
		String[] input4 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger Poutine 0", "Céline Frites -2",
				"Céline Repas_Poulet d>", "Fin" }; // cas4: les chiffres sont invalides
		String[] input5 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger Poutine 0", "Céline Frites -2",
				"Céline Repas_Poulet d>", "Fin" }; // cas5: les noms des plats et les noms des clients et les chiffres sont invalides
		String[] input6 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "bob glace -1", "Céline Frites 2",
				"Céline Repas_Poulet 1", "Fin" }; // cas6: seulement une commande est erronee
		
		String[] factureAttendue1 = { "Bienvenue chez Barette!", "Factures :", "", "Commande invalide",
				"Commande invalide", "Commande invalide" }; // le output que l'on veut avoir;
		String[] factureAttendue2 = { "Bienvenue chez Barette!", "Factures :", "", "Commande invalide",
				"Commande invalide", "Commande invalide" }; // le output que l'on veut avoir;
		String[] factureAttendue3 = { "Bienvenue chez Barette!", "Factures :", "", "Commande invalide",
				"Commande invalide", "Commande invalide" }; // le output que l'on veut avoir;
		String[] factureAttendue4 = { "Bienvenue chez Barette!", "Factures :", "", "Commande invalide",
				"Commande invalide", "Commande invalide" }; // le output que l'on veut avoir;
		String[] factureAttendue5 = { "Bienvenue chez Barette!", "Factures :", "", "Commande invalide",
				"Commande invalide", "Commande invalide" }; // le output que l'on veut avoir;
		String[] factureAttendue6 = { "Bienvenue chez Barette!", "Factures :", "", "Commande invalide",
				"Céline 20.75$", "Steeve 0.00$"}; // le output que l'on veut avoir;
		
		testerCommandeIncorrecte(input1, factureAttendue1);
		testerCommandeIncorrecte(input2, factureAttendue2);
		testerCommandeIncorrecte(input3, factureAttendue3);
		testerCommandeIncorrecte(input4, factureAttendue4);
		testerCommandeIncorrecte(input5, factureAttendue5);
		testerCommandeIncorrecte(input6, factureAttendue6);
		
	}
	
	@Test
	public void testSiPrix0() throws IOException {
		//Cas 1 : Un plat ayant un prix > 0$ est commandé 0 fois.
		String[] input1 = { "Clients :", "Roger", 
							"Plats :", "Poutine 10.5",
							"Commandes :", "Roger Poutine 0", 
							"Fin" };
		//Cas 2 : Un plat ayant un prix de 0$ est commandé > 0 fois.
		String[] input2 = { "Clients :", "Roger", 
							"Plats :", "Poutine 0",
							"Commandes :", "Roger Poutine 3", 
							"Fin" };
		//Cas 3 : Un plat ayant un prix de 0$ est commandé 0 fois.
		String[] input3 = { "Clients :", "Roger", 
							"Plats :", "Poutine 0",
							"Commandes :", "Roger Poutine 0", 
							"Fin" };
		//Cas 4 : Un plat ayant un prix > 0$ est commandé > 0 fois.
		String[] input4 = { "Clients :", "Roger", 
							"Plats :", "Poutine 1",
							"Commandes :", "Roger Poutine 1", 
							"Fin" };
		
		String[] factureAttendue1 = { "Bienvenue chez Barette!", "Factures :", "", "" };
		String[] factureAttendue2 = { "Bienvenue chez Barette!", "Factures :", "", "" };
		String[] factureAttendue3 = { "Bienvenue chez Barette!", "Factures :", "", "" };
		String[] factureAttendue4 = { "Bienvenue chez Barette!", "Factures :", "", "Roger 1.00$" };
		
		testerCommandeIncorrecte(input1, factureAttendue1);
		testerCommandeIncorrecte(input2, factureAttendue2);
		testerCommandeIncorrecte(input3, factureAttendue3);
		testerCommandeIncorrecte(input4, factureAttendue4);
	}
	
	@Test
	public void testCalculerTaxes() throws IOException {
		
		/*
		//Cas 1 : Taxe sur une commande de 1$
		String[] input1 = { "Clients :", "Roger", 
				"Plats :", "Poutine 1",
				"Commandes :", "Roger Poutine 1", 
				"Fin" };
		
		//Cas 2 : Taxe sur une commande > 1$
		String[] input2 = { "Clients :", "Roger", 
				"Plats :", "Poutine 5",
				"Commandes :", "Roger Poutine 1", 
				"Fin" };
		
		//Cas 3 : Taxe sur une commande ayant un prix élevé
		String[] input3 = { "Clients :", "Roger", 
				"Plats :", "Poutine 10000",
				"Commandes :", "Roger Poutine 1", 
				"Fin" };
		 */
		
		//Cas 1 : Taxe sur une commande de 1$
		assertEquals(1.15, LireEnvoyerCommandes.calculerTaxes(1));
		
		//Cas 2 : Taxe sur une commande > 1$
		assertEquals(5.75, LireEnvoyerCommandes.calculerTaxes(5));
		
		//Cas 3 : Taxe sur une commande ayant un prix élevé
		assertEquals(11497.50, LireEnvoyerCommandes.calculerTaxes(10000));
	}
	
	private void testerCommandeIncorrecte(String[] input, String[] factureAttendue) throws IOException{
		changerInput(input);//changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);//appeler main pour faire le traitement		
		changerInput(inputOriginal);// reset le fichierEntree 
		String[] factureRecue = lireSortieFacture();//recuperer le resultat	
		
		// 3 est ligne ou on lit commande invalide la ligne qui suit donne l'explication de l'erreur
		assertEquals(factureAttendue[3], factureRecue[3]);
		// 4 est ligne ou on lit commande invalide la ligne qui suit donne l'explication de l'erreur, pour la facture 6 elle lit le nom
		assertEquals(factureAttendue[4], factureRecue[4]);
		// 5 est ligne ou on lit commande invalide la ligne qui suit donne l'explication de l'erreur, pour la facture 6 elle lit le nom
		assertEquals(factureAttendue[5], factureRecue[5]);	
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
