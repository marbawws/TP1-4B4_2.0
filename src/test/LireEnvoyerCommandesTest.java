package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import main.LireEnvoyerCommandes;
import outilsjava.OutilsFichier;

public class LireEnvoyerCommandesTest {
	
	private String[] inputOriginal = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
			"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger Poutine 1", "Céline Frites 2",
			"Céline Repas_Poulet 1", "Fin" };
	private String nomFichierFacture = "FichierSortie.txt";
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
	public void testerSiCommandesIncorrectes() throws IOException {
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
		testerSiCommandeIncorrecte(input1);
		testerSiCommandeIncorrecte(input2);
		testerSiCommandeIncorrecte(input3);
		testerSiCommandeIncorrecte(input4);
		testerSiCommandeIncorrecte(input5);
		testerSiCommandeIncorrecte(input6);
		
		
		
	}
	
	private void testerSiCommandeIncorrecte(String[] input) throws IOException {
		changerInput(input);//changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);//appeler main pour faire le traitement		
		changerInput(inputOriginal);// reset le fichierEntree 
		String[] factureRecue = lireSortieFacture();//recuperer le resultat		
		assertEquals("Commande Incorrecte", factureRecue[0]);
	}
	
	/**
	 * test 2
	 */
	@Test
	public void testerDateEtHeure() {
		//trouver la date et l'heure
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH");  
		TimeZone EST = TimeZone.getTimeZone("EST");
		Calendar maintenant = Calendar.getInstance(EST);
		Date date = maintenant.getTime();
		//trouver le fichier avec la date et l'heure du format suivant dd/MM/yyyy-HH, s'il n'existe pas le test est faux
		//nomFichierFacture = "Fichier-du-" + formatter.format(date) +".txt"; dé-commentter cette ligne quand la tache 2 sera implementer pour que les tests est le bon nom du fichier
		File file = new File("Fichier-du-" + formatter.format(date) +".txt");
		if (!(file.exists())) {
			fail();
		} 	
	}
	
	/**
	 * test 3
	 * @throws IOException
	 */
	@Test
	public void testerAffichageCommandesIncorrectes() throws IOException {

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
		String[] factureAttendue1 = { "Bienvenue chez Barette!", "Factures :", "", "Commande Incorrecte", "Commande Incorrecte", "Commande Incorrecte" }; // le output que l'on veut avoir;
		String[] factureAttendue2 = { "Bienvenue chez Barette!", "Factures :", "", "Commande Incorrecte", "Commande Incorrecte", "Commande Incorrecte" }; // le output que l'on veut avoir;
		String[] factureAttendue3 = { "Bienvenue chez Barette!", "Factures :", "", "Commande Incorrecte", "Commande Incorrecte", "Commande Incorrecte" }; // le output que l'on veut avoir;
		String[] factureAttendue4 = { "Bienvenue chez Barette!", "Factures :", "", "Commande Incorrecte", "Commande Incorrecte", "Commande Incorrecte" }; // le output que l'on veut avoir;
		String[] factureAttendue5 = { "Bienvenue chez Barette!", "Factures :", "", "Commande Incorrecte", "Commande Incorrecte", "Commande Incorrecte" }; // le output que l'on veut avoir;
		String[] factureAttendue6 = { "Bienvenue chez Barette!", "Factures :", "", "Commande Incorrecte", "Céline 20.75$", "Steeve 0.00$"}; // le output que l'on veut avoir;
		testerAffichageCommandeIncorrecte(input1, factureAttendue1);
		testerAffichageCommandeIncorrecte(input2, factureAttendue2);
		testerAffichageCommandeIncorrecte(input3, factureAttendue3);
		testerAffichageCommandeIncorrecte(input4, factureAttendue4);
		testerAffichageCommandeIncorrecte(input5, factureAttendue5);
		testerAffichageCommandeIncorrecte(input6, factureAttendue6);
		
	}
	
	private void testerAffichageCommandeIncorrecte(String[] input, String[] factureAttendue) throws IOException{
		changerInput(input);//changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);//appeler main pour faire le traitement		
		changerInput(inputOriginal);// reset le fichierEntree 
		String[] factureRecue = lireSortieFacture();//recuperer le resultat		
		assertEquals(factureAttendue[3], factureRecue[3]);// 3 est ligne ou on lit Commande Incorrecte la ligne qui suit donne l'explication de l'erreur
		assertEquals(factureAttendue[4], factureRecue[5]);// 4 est ligne ou on lit Commande Incorrecte la ligne qui suit donne l'explication de l'erreur, pour la facture 6 elle lit le nom
		assertEquals(factureAttendue[5], factureRecue[6]);// 5 est ligne ou on lit Commande Incorrecte la ligne qui suit donne l'explication de l'erreur, pour la facture 6 elle lit le nom	
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
		BufferedReader informationsOutput = OutilsFichier.ouvrirFicTexteLecture(nomFichierFacture);

		if (informationsOutput == null) {
			// le fichier n'existe pas, le message d'erreur est déjà envoyer
		} else {
			while (informationsOutput.readLine() != null)
				nblignes++; // trouver le nombre de lignes
			informationsOutput = OutilsFichier.ouvrirFicTexteLecture(nomFichierFacture); // reload le buffereader pour
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
