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

	@Test
	public void testerSiCommandesIncorrectes() throws IOException {
		
		// S'assurer qu'il ne reste aucune donnée des autres tests.
		LireEnvoyerCommandes.listeClients.clear();
		LireEnvoyerCommandes.listePlats.clear();

		// Créer les données qui seront utilisées dans les tests.
		LireEnvoyerCommandes.listeClients.add(new Client("Roger"));
		LireEnvoyerCommandes.listeClients.add(new Client("Céline"));
		LireEnvoyerCommandes.listeClients.add(new Client("Steeve"));

		LireEnvoyerCommandes.listePlats.add(new Plat("Poutine", 10.5));
		LireEnvoyerCommandes.listePlats.add(new Plat("Frites", 2.5));
		LireEnvoyerCommandes.listePlats.add(new Plat("Repas_Poulet", 15.75));

		// Des tableaux sont utilisés au cas où le test comporte plusieurs commandes.
		
		// cas:1 les noms des plats n'existe pas.
		Commande[] test1 = { new Commande("Roger", "boisson", 4),
				 new Commande("Céline", "glace", 2),
				 new Commande("Steeve", "croquettes", 2)};
		
		// cas:2 les noms des clients n'existe pas.
		Commande[] test2 = { new Commande("bob", "Poutine", 4),
				 new Commande("bobette", "Poutine", 2),
				 new Commande("bobino", "Poutine", 2)};
		
		// cas:3 les noms des plats et les noms des clients n'existent pas.
		Commande[] test3 = { new Commande("bob", "boisson", 4),
				 new Commande("bobette", "glace", 2),
				 new Commande("bobino", "croquettes", 2)};
		
		// cas4: les chiffres sont invalides.
		Commande[] test4 = { new Commande("Roger", "Poutine", -2),
				 new Commande("Céline", "Frites", 0),
				 new Commande("Steeve", "Repas_Poulet", 0)};
		
		// cas5: les noms des plats et les noms des clients et les chiffres sont invalides.
		Commande[] test5 = { new Commande("bob", "boisson", 0),
				 new Commande("bobette", "glace", 0),
				 new Commande("bobino", "croquettes", -3)};
		
		// cas6: seulement une commande est erronee.
		Commande[] test6 = { new Commande("bob", "boisson", 0),
				 new Commande("Roger", "Poutine", 1),
				 new Commande("Céline", "Frites", 3)};

		
		assertFalse(creerErreurs(test1).isEmpty());
		assertFalse(creerErreurs(test2).isEmpty());
		assertFalse(creerErreurs(test3).isEmpty());
		assertFalse(creerErreurs(test4).isEmpty());
		assertFalse(creerErreurs(test5).isEmpty());
		assertFalse(creerErreurs(test6).isEmpty());
	}

	@Test
	public void testVerifierFormatFic() throws IOException {

		// Cas 1 : La ligne "Clients :" n'existe pas.
		String[] test1 = { "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5", "Repas_Poulet 15.75",
				"Commandes :", "Roger boisson 4", "Céline glace 2", "Steeve croquettes 2", "Fin" };

		// Cas 2 : Un plat a un prix non-numérique (Poutine a).
		String[] test2 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine a", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "Roger boisson 4", "Céline glace 2", "Steeve croquettes 2",
				"Fin" };

		// Cas 3 : Pas d'espace entre le nom du client et du plat.
		String[] test3 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine a", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "Rogerboisson 4", "Céline glace 2", "Steeve croquettes 2", "Fin" };

		// Cas 4 : Le fichier ne contient que des catégories, mais aucune donnée.
		String[] test4 = { "Clients :", "Plats :", "Commandes :", "Fin" };

		// Cas 5 : Le mot "Fin" n'est pas présent à la dernière ligne du fichier.
		String[] test5 = { "Clients :", "Plats :", "Commandes :" };

		assertTrue(LireEnvoyerCommandes.verifierFormatFic(inputOriginal)); // Cas contrôle
		assertFalse(LireEnvoyerCommandes.verifierFormatFic(test1));
		assertFalse(LireEnvoyerCommandes.verifierFormatFic(test2));
		assertFalse(LireEnvoyerCommandes.verifierFormatFic(test3));
		assertTrue(LireEnvoyerCommandes.verifierFormatFic(test4));
		assertFalse(LireEnvoyerCommandes.verifierFormatFic(test5));

	}

	@Test
	public void testPrix0() throws IOException {
		// S'assurer qu'il ne reste aucune donnée des autres tests.
		LireEnvoyerCommandes.listeClients.clear();
		LireEnvoyerCommandes.listePlats.clear();

		// Créer les données qui seront utilisées dans les tests.
		LireEnvoyerCommandes.listeClients.add(new Client("Roger"));

		LireEnvoyerCommandes.listePlats.add(new Plat("Poutine", 10.5));
		LireEnvoyerCommandes.listePlats.add(new Plat("Hamburger", 0));
		LireEnvoyerCommandes.listePlats.add(new Plat("Soupe", 0));

		// Des tableaux sont utilisés au cas où le test comporte plusieurs commandes.
		// Utilisation de la surcharge (nomClient, nomPlat, nbPlats, prixPlat) du
		// constructeur de Commande.
		Commande[] test1 = { new Commande("Roger", "Poutine", 0, 10.5) };
		Commande[] test2 = { new Commande("Roger", "Poutine", 1, 10.5) };
		Commande[] test3 = { new Commande("Roger", "Hamburger", 0, 0) };
		Commande[] test4 = { new Commande("Roger", "Hamburger", 3, 0) };
		Commande[] test5 = { new Commande("Roger", "Hamburger", 1, 0), new Commande("Roger", "Soupe", 1, 0) };
		Commande[] test6 = { new Commande("Roger", "Hamburger", 1, 0), new Commande("Roger", "Poutine", 1, 10.5) };

		// Cas 1 : Un plat ayant un prix > 0$ est commandé 0 fois.
		assertTrue(creerFacture(test1).isEmpty());

		// Cas 2 : Un plat ayant un prix > 0$ est commandé > 0 fois.
		assertFalse(creerFacture(test2).isEmpty());

		// Cas 3 : Un plat ayant un prix de 0$ est commandé 0 fois.
		assertTrue(creerFacture(test3).isEmpty());

		// Cas 4 : Un plat ayant un prix de 0$ est commandé > 0 fois.
		assertTrue(creerFacture(test4).isEmpty());

		// Cas 5 : Un seul client commande 2 plats à 0$.
		assertTrue(creerFacture(test5).isEmpty());

		// Cas 6 : Un seul client commande un plat à 0$ et un plat à plus que 0$.
		assertEquals("Roger 12.08$", creerFacture(test6).get(0));
	}

	// Popule la liste factures de LireEnvoyerCommandes avec le tableau de commandes
	// donné en paramètre.
	private ArrayList<String> creerFacture(Commande[] tabCommandes) throws IOException {

		// S'assurer qu'il ne reste rien des autres tests.
		LireEnvoyerCommandes.listeCommandes.clear();
		LireEnvoyerCommandes.factures.clear();

		for (Commande commande : tabCommandes) {
			LireEnvoyerCommandes.listeCommandes.add(commande);
		}

		LireEnvoyerCommandes.creerSortie();

		return LireEnvoyerCommandes.factures;
	}

	// Popule la liste erreurs de LireEnvoyerCommandes avec le tableau de commandes
	// donné en paramètre.
	private ArrayList<String> creerErreurs(Commande[] tabCommandes) throws IOException {

		// S'assurer qu'il ne reste rien des autres tests.
		LireEnvoyerCommandes.listeCommandes.clear();
		LireEnvoyerCommandes.erreurs.clear();

		for (Commande commande : tabCommandes) {
			LireEnvoyerCommandes.listeCommandes.add(commande);
		}

		LireEnvoyerCommandes.creerSortie();

		return LireEnvoyerCommandes.erreurs;
	}
	
	@Test
	public void testCalculerTaxes() {

		// Cas 1 : Taxe sur une commande de 1$
		assertEquals(1.15, LireEnvoyerCommandes.calculerTaxes(1), 0.00);

		// Cas 2 : Taxe sur une commande > 1$
		assertEquals(5.75, LireEnvoyerCommandes.calculerTaxes(5), 0.00);

		// Cas 3 : Taxe sur une commande ayant un prix élevé
		assertEquals(11497.50, LireEnvoyerCommandes.calculerTaxes(10000), 0.00);
	}

	@Test
	public void testChercherIndexClient() {
		// S'assurer qu'il ne reste aucune donnée des autres tests.
		LireEnvoyerCommandes.listeClients.clear();
		LireEnvoyerCommandes.listePlats.clear();

		// Créer les données qui seront utilisées dans les tests.
		LireEnvoyerCommandes.listeClients.add(new Client("Roger"));
		LireEnvoyerCommandes.listeClients.add(new Client("Nathan"));
		LireEnvoyerCommandes.listeClients.add(new Client("Chloe"));

		// Cas 1 : Chercher le client situé au début de la liste.
		assertEquals(0, LireEnvoyerCommandes.chercherIndexClient("Roger"));

		// Cas 2 : Chercher le client situé au milieu de la liste.
		assertEquals(1, LireEnvoyerCommandes.chercherIndexClient("Nathan"));

		// Cas 3 : Chercher le client situé à la fin de la liste.
		assertEquals(2, LireEnvoyerCommandes.chercherIndexClient("Chloe"));

		// Cas 4 : Chercher un client qui n'existe pas.
		assertEquals(-1, LireEnvoyerCommandes.chercherIndexClient("Elena"));
	}

	@Test
	public void testClientExiste() {
		// S'assurer qu'il ne reste aucune donnée des autres tests.
		LireEnvoyerCommandes.listeClients.clear();
		LireEnvoyerCommandes.listePlats.clear();

		// Créer les données qui seront utilisées dans les tests.
		LireEnvoyerCommandes.listeClients.add(new Client("Roger"));

		// Cas 1 : Le client est dans la liste.
		assertTrue(LireEnvoyerCommandes.clientExiste(new Commande("Roger", "Poutine", 1)));

		// Cas 2 : Le client n'est pas dans la liste.
		assertFalse(LireEnvoyerCommandes.clientExiste(new Commande("Nathan", "Poutine", 1)));
	}

	@Test
	public void testPlatExiste() {
		// S'assurer qu'il ne reste aucune donnée des autres tests.
		LireEnvoyerCommandes.listeClients.clear();
		LireEnvoyerCommandes.listePlats.clear();

		// Créer les données qui seront utilisées dans les tests.
		LireEnvoyerCommandes.listePlats.add(new Plat("Poutine", 0));

		// Cas 1 : Le plat est dans la liste.
		assertTrue(LireEnvoyerCommandes.platExiste(new Commande("Roger", "Poutine", 1)));

		// Cas 2 : Le plat n'est pas dans la liste.
		assertFalse(LireEnvoyerCommandes.platExiste(new Commande("Nathan", "Mochi", 1)));
	}

	/**
	 * test 2
	 */
	@Test
	public void testerDateEtHeure() {
		// trouver la date et l'heure
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH");
		TimeZone EST = TimeZone.getTimeZone("EST");
		Calendar maintenant = Calendar.getInstance(EST);
		Date date = maintenant.getTime();
		// trouver le fichier avec la date et l'heure du format suivant dd/MM/yyyy-HH,
		// s'il n'existe pas le test est faux
		System.out.println("\ntest 2\n");
		File file = new File("Facture-du-" + formatter.format(date) + ".txt");
		if (!(file.exists())) {
			fail();
		} else {
			System.out.println("\ntest 2 fonctionnel\n");
		}
	}

	private void modifierFichierSortie() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH");
		TimeZone EST = TimeZone.getTimeZone("EST");
		Calendar maintenant = Calendar.getInstance(EST);
		Date date = maintenant.getTime();
		nomFichierFacture = "Facture-du-" + formatter.format(date) + ".txt";
	}

	/**
	 * test 3
	 * 
	 * @throws IOException
	 */
	@Test
	public void testerAffichageCommandesIncorrectes() throws IOException {

		String[] input1 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "Roger boisson 4", "Céline glace 2", "Steeve croquettes 2",
				"Fin" }; // cas:1 les noms des plats n'existe pas
		String[] input2 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "bob Poutine 4", "bobbette Frites 2", "bobino Repas_Poulet 2",
				"Fin" }; // cas:2 les noms des clients n'existe pas
		String[] input3 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "bob boisson 4", "bobbette glace 2", "bobino croquettes 2",
				"Fin" }; // cas:3 les noms des plats et les noms des clients n'existent pas
		String[] input4 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "Roger Poutine 0", "Céline Frites -2", "Céline Repas_Poulet d>",
				"Fin" }; // cas4: les chiffres sont invalides
		String[] input5 = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "Roger Poutine 0", "Céline Frites -2", "Céline Repas_Poulet d>",
				"Fin" }; // cas5: les noms des plats et les noms des clients et les chiffres sont
							// invalides
		String[] factureAttendue = { "Commande incorrecte", "Commande incorrecte", "Commande incorrecte", }; // le
																												// output
																												// que
																												// l'on
																												// veut
																												// avoir;
		testerAffichageCommandeIncorrecte(input1, factureAttendue);
		testerAffichageCommandeIncorrecte(input2, factureAttendue);
		testerAffichageCommandeIncorrecte(input3, factureAttendue);
		testerAffichageCommandeIncorrecte(input4, factureAttendue);
		testerAffichageCommandeIncorrecte(input5, factureAttendue);

	}

	private void testerAffichageCommandeIncorrecte(String[] input, String[] factureAttendue) throws IOException {
		changerInput(input);// changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);// appeler main pour faire le traitement
		changerInput(inputOriginal);// reset le fichierEntree
		modifierFichierSortie();
		String[] factureRecue = lireSortieFacture();// recuperer le resultat

		assertEquals(factureAttendue[0], factureRecue[4]);// 3 est ligne ou on lit Commande Incorrecte la ligne qui suit
															// donne l'explication de l'erreur
		assertEquals(factureAttendue[1], factureRecue[8]);// 4 est ligne ou on lit Commande Incorrecte la ligne qui suit
															// donne l'explication de l'erreur, pour la facture 6 elle
															// lit le nom
		assertEquals(factureAttendue[2], factureRecue[12]);// 5 est ligne ou on lit Commande Incorrecte la ligne qui
															// suit donne l'explication de l'erreur, pour la facture 6
															// elle lit le nom
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

	// test supplementaires
	@Test
	public void siEspacesDansCommandes() throws IOException {
		String[] input = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "Roger Poutine 1", "Céline Frites 2", "Céline Repas_Poulet 1",
				"Fin" };
		String[] factureAttendu = { "Bienvenue chez Barette!", "", "Factures :", "Roger 12.08$", "Céline 23.86$" }; // le
																													// output
																													// que
																													// l'on
																													// veut
																													// avoir
		testInputOutputBasic(input, factureAttendu);

	}

	@Test
	public void siSigneDollarApparait() throws IOException {
		String[] input = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "Roger Poutine 1", "Céline Frites 2", "Céline Repas_Poulet 1",
				"Fin" };
		String[] factureAttendu = { "Bienvenue chez Barette!", "", "Factures :", "Roger 12.08$", "Céline 23.86$" }; // le
																													// output
																													// que
																													// l'on
																													// veut
																													// avoir
		testInputOutputBasic(input, factureAttendu);
	}

	@Test
	public void siNomsApparaissentEnOrdre() throws IOException {
		String[] input = { "Clients :", "Roger", "Céline", "Steeve", "Plats :", "Poutine 10.5", "Frites 2.5",
				"Repas_Poulet 15.75", "Commandes :", "Roger Poutine 1", "Céline Frites 2", "Céline Repas_Poulet 1",
				"Fin" };
		
		String[] factureAttendu = { "Bienvenue chez Barette!", "", "Factures :", "Roger 12.08$", "Céline 23.86$" }; // le
																													// output
																													// que
																													// l'on
																													// veut
																													// avoir
		testInputOutputBasic(input, factureAttendu);
	}

	private void testInputOutputBasic(String[] input, String[] factureAttendue) throws IOException {
		changerInput(input);// changer le fichier entree pour faire nos tests
		LireEnvoyerCommandes.main(null);// appeler main pour faire le traitement
		changerInput(inputOriginal);// reset le fichierEntree
		modifierFichierSortie();
		String[] factureRecue = lireSortieFacture();// recuperer le resultat

		assertArrayEquals(factureAttendue, factureRecue);

	}
}
