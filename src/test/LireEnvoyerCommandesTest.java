package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import main.LireEnvoyerCommandes;

public class LireEnvoyerCommandesTest {

	@Test
	public void testfacture() {
		String[] tableauInformation = {"Clients :",
			"Roger",
			"C�line",
			"Steeve",
			"Plats :",
			"Poutine 10.5",
			"Frites 2.5",
			"Repas_Poulet 15.75",
			"Commandes :",
			"Roger Poutine 1",
			"C�line Frites 2",
			"C�line Repas_Poulet 1",
			"Fin"};
		if (LireEnvoyerCommandes.verifierFormatFic(tableauInformation)) {
			
			LireEnvoyerCommandes.creerListes(tableauInformation);
			
			if (LireEnvoyerCommandes.verifierIntegriteClients() && LireEnvoyerCommandes.verifierIntegritePlats()) {
				try {
					LireEnvoyerCommandes.creerFacture();
				} catch (IOException e) {
					//
				}
			} else {
				System.out.println("Le fichier ne respecte pas le format demand� !\nArr�t du programme.");
			}
			
		} else {
			System.out.println("Le fichier ne respecte pas le format demand� !\nArr�t du programme.");
		}
	}

}
