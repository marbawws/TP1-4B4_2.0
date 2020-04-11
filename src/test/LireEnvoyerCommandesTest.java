package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import main.Client;
import main.Commande;
import main.LireEnvoyerCommandes;
import main.Plat;
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
	//obselete : testerAffichageCommandesIncorrectes() la remplace. taches 1 et 3 sont similaires donc le test 3 agit comme test 1 aussi
	/*@Test
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
		System.out.println("\ntest 1\n");
		testerSiCommandeIncorrecte(input1);
		testerSiCommandeIncorrecte(input2);
		testerSiCommandeIncorrecte(input3);
		testerSiCommandeIncorrecte(input4);
		testerSiCommandeIncorrecte(input5);
		testerSiCommandeIncorrecte(input6);
		System.out.println("\ntest 1 fonctionnel\n");
		
		
		
	}*/
	
	@Test
	public void testFormatFic() throws IOException {
		
		//Cas 1 : La ligne "Clients :" n'existe pas.
		String[] test1 = {"Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger boisson 4", "Céline glace 2",
				"Steeve croquettes 2", "Fin" };
		
		//Cas 2 : Un plat a un prix non-numérique (Poutine a).
		String[] test2 = {"Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine a",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Roger boisson 4", "Céline glace 2",
				"Steeve croquettes 2", "Fin" };
		
		//Cas 3 : Pas d'espace entre le nom du client et du plat.
		String[] test3 = {"Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine a",
				"Frites 2.5", "Repas_Poulet 15.75", "Commandes :", "Rogerboisson 4", "Céline glace 2",
				"Steeve croquettes 2", "Fin" };
		
		//Cas 4 : Le fichier ne contient que des catégories, mais aucune donnée.
		String[] test4 = {"Clients :", "Plats :" ,"Commandes :", "Fin"};
		
		//Cas 5 : Le mot "Fin" n'est pas présent à la dernière ligne du fichier.
		String[] test5 = {"Clients :", "Plats :" ,"Commandes :"};
		
		assertTrue(LireEnvoyerCommandes.verifierFormatFic(inputOriginal)); //Cas contrôle
		assertFalse(LireEnvoyerCommandes.verifierFormatFic(test1));
		assertFalse(LireEnvoyerCommandes.verifierFormatFic(test2));
		assertFalse(LireEnvoyerCommandes.verifierFormatFic(test3));
		assertTrue(LireEnvoyerCommandes.verifierFormatFic(test4));
		assertFalse(LireEnvoyerCommandes.verifierFormatFic(test5));
		
	}
	
	private void testerSiCommandeIncorrecte(String[] input) throws IOException {
		changerInput(input);//changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);//appeler main pour faire le traitement		
		changerInput(inputOriginal);// reset le fichierEntree 
		String[] factureRecue = lireSortieFacture();//recuperer le resultat		
		assertEquals("Commande Incorrecte", factureRecue[0]);
	}
	
	@Test
	public void testPrix0() throws IOException {
		//S'assurer qu'il ne reste aucune donnée des autres tests.
		LireEnvoyerCommandes.listeClients.clear();		
		LireEnvoyerCommandes.listePlats.clear();
		
		//Créer les données qui seront utilisées dans les tests.
		LireEnvoyerCommandes.listeClients.add(new Client("Roger")); 
		
		LireEnvoyerCommandes.listePlats.add(new Plat("Poutine", 10.5));
		LireEnvoyerCommandes.listePlats.add(new Plat("Hamburger", 0));
		LireEnvoyerCommandes.listePlats.add(new Plat("Soupe", 0));
		
		//Des tableaux sont utilisés au cas où le test comporte plusieurs commandes.
		//Utilisation de la surcharge (nomClient, nomPlat, nbPlats, prixPlat) du constructeur de Commande.
		Commande[] test1 = {new Commande("Roger", "Poutine", 0, 10.5)};
		Commande[] test2 = {new Commande("Roger", "Poutine", 1, 10.5)};
		Commande[] test3 = {new Commande("Roger", "Hamburger", 0, 0)};
		Commande[] test4 = {new Commande("Roger", "Hamburger", 3, 0)};
		Commande[] test5 = {new Commande("Roger", "Hamburger", 1, 0),
							new Commande("Roger", "Soupe", 1, 0)};
		Commande[] test6 = {new Commande("Roger", "Hamburger", 1, 0),
							new Commande("Roger", "Poutine", 1, 10.5)};
		
		//Cas 1 : Un plat ayant un prix > 0$ est commandé 0 fois.
		assertTrue(creerFacture(test1).isEmpty());
		
		//Cas 2 : Un plat ayant un prix > 0$ est commandé > 0 fois.
		assertFalse(creerFacture(test2).isEmpty());
		
		//Cas 3 : Un plat ayant un prix de 0$ est commandé 0 fois.
		assertTrue(creerFacture(test3).isEmpty());
		
		//Cas 4 : Un plat ayant un prix de 0$ est commandé > 0 fois.
		assertTrue(creerFacture(test4).isEmpty());
		
		//Cas 5 : Un seul client commande 2 plats à 0$.
		assertTrue(creerFacture(test5).isEmpty());
		
		//Cas 6 : Un seul client commande un plat à 0$ et un plat à plus que 0$.
		assertEquals("Roger 12.08$", creerFacture(test6).get(0));
	}
	
	//Popule la liste factures de LireEnvoyerCommandes avec le tableau de commandes donné en paramètre.
	public ArrayList<String> creerFacture(Commande[] tabCommandes) throws IOException {
		
		//S'assurer qu'il ne reste rien des autres tests.
		LireEnvoyerCommandes.listeCommandes.clear();
		LireEnvoyerCommandes.factures.clear();
		
		for (Commande commande : tabCommandes) {
			LireEnvoyerCommandes.listeCommandes.add(commande);
		}
		
		LireEnvoyerCommandes.creerSortie();
		
		return LireEnvoyerCommandes.factures;
	}
	
	@Test
	public void testCalculerTaxes() throws IOException {
		
		//Cas 1 : Taxe sur une commande de 1$
		assertEquals(1.15, LireEnvoyerCommandes.calculerTaxes(1), 0.00);
		
		//Cas 2 : Taxe sur une commande > 1$
		assertEquals(5.75, LireEnvoyerCommandes.calculerTaxes(5), 0.00);
		
		//Cas 3 : Taxe sur une commande ayant un prix élevé
		assertEquals(11497.50, LireEnvoyerCommandes.calculerTaxes(10000), 0.00);
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
		System.out.println("\ntest 2\n");
		File file = new File("Facture-du-" + formatter.format(date) +".txt");
		if (!(file.exists())) {
			fail();
		} else {
			System.out.println("\ntest 2 fonctionnel\n");
		}
	}
	
	private void modifierFichierSortie(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH");  
		TimeZone EST = TimeZone.getTimeZone("EST");
		Calendar maintenant = Calendar.getInstance(EST);
		Date date = maintenant.getTime();
		nomFichierFacture = "Facture-du-" + formatter.format(date) +".txt"; 
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
		String[] factureAttendue1 = { "Commande incorrecte", "Commande incorrecte", "Commande incorrecte", }; // le output que l'on veut avoir;
		String[] factureAttendue2 = { "Commande incorrecte", "Commande incorrecte", "Commande incorrecte", }; // le output que l'on veut avoir;
		String[] factureAttendue3 = { "Commande incorrecte", "Commande incorrecte", "Commande incorrecte", }; // le output que l'on veut avoir;
		String[] factureAttendue4 = { "Commande incorrecte", "Commande incorrecte", "Commande incorrecte", }; // le output que l'on veut avoir;
		String[] factureAttendue5 = { "Commande incorrecte", "Commande incorrecte", "Commande incorrecte", }; // le output que l'on veut avoir;
		System.out.println("\ntest 3\n");
		testerAffichageCommandeIncorrecte(input1, factureAttendue1);
		testerAffichageCommandeIncorrecte(input2, factureAttendue2);
		testerAffichageCommandeIncorrecte(input3, factureAttendue3);
		testerAffichageCommandeIncorrecte(input4, factureAttendue4);
		testerAffichageCommandeIncorrecte(input5, factureAttendue5);
		System.out.println("\ntest 3 fonctionnels\n");
		
	}
	
	private void testerAffichageCommandeIncorrecte(String[] input, String[] factureAttendue) throws IOException{
		changerInput(input);//changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);//appeler main pour faire le traitement		
		changerInput(inputOriginal);// reset le fichierEntree 
		modifierFichierSortie();
		String[] factureRecue = lireSortieFacture();//recuperer le resultat	
		
		assertEquals(factureAttendue[0], factureRecue[4]);// 3 est ligne ou on lit Commande Incorrecte la ligne qui suit donne l'explication de l'erreur
		assertEquals(factureAttendue[1], factureRecue[8]);// 4 est ligne ou on lit Commande Incorrecte la ligne qui suit donne l'explication de l'erreur, pour la facture 6 elle lit le nom
		assertEquals(factureAttendue[2], factureRecue[12]);// 5 est ligne ou on lit Commande Incorrecte la ligne qui suit donne l'explication de l'erreur, pour la facture 6 elle lit le nom	
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
