package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import outilsjava.OutilsFichier;

public class LireEnvoyerCommandes {

	public static ArrayList<Client> listeClients = new ArrayList<Client>();
	public static ArrayList<Plat> listePlats = new ArrayList<Plat>();
	public static ArrayList<Commande> listeCommandes = new ArrayList<Commande>();

	private static final String LIGNE_CLIENTS_ENTREE = "Clients :";
	private static final String LIGNE_PLATS_ENTREE = "Plats :";
	private static final String LIGNE_COMMANDES_ENTREE = "Commandes :";

	public static void main(String[] args) throws IOException {

		String[] tableauInformation = lireFichierEtMettreDansTableau();
		
		if (tableauInformation != null) {
			
			if (verifierFormatFic(tableauInformation)) {
				
				creerListes(tableauInformation);
				
				if (verifierIntegriteClients() && verifierIntegritePlats()) {
					String[] facture = creerFacture();
					ecrireFacture(facture);
				} else {
					System.out.println("Le fichier ne respecte pas le format demandé !\nArrêt du programme.");
				}
				
			} else {
				System.out.println("Le fichier ne respecte pas le format demandé !\nArrêt du programme.");
			}
		}

	}

	/*
	 * Vérifie si toutes les commandes sont faites par des clients qui existent dans
	 * la liste de clients. Retourne true si c'est le cas et false dans le cas
	 * contraire.
	 */
	public static boolean verifierIntegriteClients() {
		boolean valide = true;

		for (int i = 0; i < listeCommandes.size(); i++) {
			if (chercherIndexClient(listeCommandes.get(i).getNomClient()) < 0) {
				valide = false;
				break;
			}
		}

		return valide;
	}

	/*
	 * Vérifie si toutes les commandes comportent des plats qui existent dans la
	 * liste de plats. Retourne true si c'est le cas et false dans le cas contraire.
	 */
	public static boolean verifierIntegritePlats() {
		boolean valide = true;

		for (int i = 0; i < listeCommandes.size(); i++) {
			if (chercherIndexPlat(listeCommandes.get(i).getNomPlat()) < 0) {
				valide = false;
				break;
			}
		}

		return valide;
	}

	/*
	 * Crée un fichier de sortie appelé « FichierSortie.txt » contenant la facture.
	 */
	public static String[] creerFacture() throws IOException {

		//BufferedWriter informationOutput = OutilsFichier.ouvrirFicTexteEcriture("FichierSortie.txt");
		String[] facture = new String[listeClients.size() + 1];
		//if (informationOutput != null) {
			
			//informationOutput.write("Bienvenue chez Barette!\nFactures :\n");
			facture[0] = "Bienvenue chez Barette!\nFactures :\n";
			
			for (int i = 0; i < listeClients.size(); i++) {
				ArrayList<Commande> commandesClientCourant = chercherCommandesClient(listeClients.get(i).getNom());
				double prixAvantTaxes = 0;
				double prixApresTaxes;
				
				if (commandesClientCourant.isEmpty()) {
					
					/*informationOutput.write(listeClients.get(i).getNom() + " " + String.format("%.2f", prix) + "$");
					informationOutput.newLine();*/
					facture[i + 1] = listeClients.get(i).getNom() + " " + String.format("%.2f", prixAvantTaxes) + "$";
					
				} else {
					
					for (int j = 0; j < commandesClientCourant.size(); j++) {
						// Additionner le prix de chaque commande faite par le client.
						prixAvantTaxes +=
							commandesClientCourant.get(j).getPrixPlat() * commandesClientCourant.get(j).getNbPlats();
						
						//TODO Ajouter cette ligne après avoir complété calculerTaxes.
						//prixApresTaxes = calculerTaxes(prixAvantTaxes);
						
						
					}
					
					/*informationOutput.write(listeClients.get(i).getNom() + " " + String.format("%.2f", prix) + "$");
					informationOutput.newLine();*/
					
					//TODO Remplacer la dernière ligne par celle-ci après avoir complété calculerTaxes.
					//facture[i + 1] = listeClients.get(i).getNom() + " " + String.format("%.2f", prixApresTaxes) + "$";
					
					facture[i + 1] = listeClients.get(i).getNom() + " " + String.format("%.2f", prixAvantTaxes) + "$";
					
				}
				
			}
			
	/*		informationOutput.close();
			
			System.out.println("\nFacture écrite dans FichierSortie.txt. Fin du programme.");
		} else {
			System.out.println("\nOpération annulée. Fin du programme.");
		}
	*/	
		return facture;
	}
	
	public static double calculerTaxes(double montantAvantTaxes) {
		/*
		 * Voici une coquille de méthode qui calcule les taxes. Elle 
		 * Évidemment, je ne fais que suggérer une façon de gérer les taxes.
		 * J'ai également ajouté un squelette dans creerFacture.
		 * N'hésite pas à utiliser une structure différente si celle-ci ne fonctionne pas.
		 */
		return 0;
	}
	
	/*
	 * J'ai ajouter cette méthode pour facilité les tests :)
	 * 
	 */	
	public static void ecrireFacture(String[] facture) throws IOException {
		BufferedWriter informationOutput = OutilsFichier.ouvrirFicTexteEcriture("FichierSortie.txt");
		if (informationOutput != null) {
			
			for (int j = 0; j < facture.length; j++) {
				informationOutput.write(facture[j]);
				informationOutput.newLine();
			}
			
			informationOutput.close();
			System.out.println("\nFacture écrite dans FichierSortie.txt. Fin du programme.");
		} else {
			System.out.println("\nOpération annulée. Fin du programme.");
		}
		
	}
	/*
	 * Retourne une liste contenant les commandes du client indiqué en paramètre.
	 * Retourne une liste vide si le client n'a rien commandé.
	 */
	private static ArrayList<Commande> chercherCommandesClient(String nomClient) {
		ArrayList<Commande> commandesClient = new ArrayList<Commande>();

		for (int i = 0; i < listeClients.size(); i++) {
			if (nomClient.equals(listeCommandes.get(i).getNomClient())) {
				commandesClient.add(listeCommandes.get(i));
			}
		}

		return commandesClient;
	}

	/*
	 * Cherche dans la liste de plats et retourne l'index du plat indiqué en
	 * paramètre. Retourne -1 si le plat n'existe pas.
	 */
	private static int chercherIndexPlat(String nomPlat) {
		int index = -1;

		for (int i = 0; i < listePlats.size(); i++) {
			if (nomPlat.equals(listePlats.get(i).getNomPlat())) {
				index = i;
				break;
			}
		}

		return index;
	}

	/*
	 * Cherche dans la liste de clients et retourne l'index du client indiqué en
	 * paramètre. Retourne -1 si le client n'existe pas.
	 */
	private static int chercherIndexClient(String nomClient) {
		int index = -1;

		for (int i = 0; i < listeClients.size(); i++) {
			if (nomClient.equals(listeClients.get(i).getNom())) {
				index = i;
				break;
			}
		}

		return index;
	}

	// Cette méthode popule les listes contenant les clients, les plats et les
	// commandes.
	public static void creerListes(String[] tableauInformation) {
		String mode = "Clients"; // On sait que la première catégorie est clients.

		// On commence après «Clients :» et on ignore la dernière ligne du tableau.
		for (int i = 1; i < tableauInformation.length - 1; i++) {
			if (tableauInformation[i].equals(LIGNE_PLATS_ENTREE)) {
				mode = "Plats";
			} else if (tableauInformation[i].equals(LIGNE_COMMANDES_ENTREE)) {
				mode = "Commandes";
			} else {
				switch (mode) {
				case "Clients":
					listeClients.add(new Client(tableauInformation[i]));
					break;

				case "Plats":
					listePlats.add(new Plat(tableauInformation[i].trim().split(" ")[0],
							Double.parseDouble(tableauInformation[i].trim().split(" ")[1])));
					break;
				case "Commandes":
					listeCommandes.add(new Commande(tableauInformation[i].trim().split(" ")[0],
							tableauInformation[i].trim().split(" ")[1],
							Integer.parseInt(tableauInformation[i].trim().split(" ")[2])));

					int indexPlat = chercherIndexPlat(listeCommandes.get(listeCommandes.size() - 1).getNomPlat());

					if (indexPlat >= 0) {
						listeCommandes.get(listeCommandes.size() - 1).setPrixPlat(listePlats.get(indexPlat).getPrix());
					}
					break;

				default:
					break;
				}
			}
		}
	}

	/*
	 * Lis le fichier fichierEntree.txt et transfère son contenu dans un tableau qui
	 * est retourné.
	 */
	private static String[] lireFichierEtMettreDansTableau() {

		int nblignes = 0;
		String[] tableauInformation = null;
		BufferedReader informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt");

		if (informations == null) {
			// le fichier n'existe pas, le message d'erreur est déjà envoyer par soti <3
		} else {

			try {
				while (informations.readLine() != null)
					nblignes++; // trouver le nombre de lignes
					informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt"); // reload le buffereader pour
																								// relire les lignes
					tableauInformation = new String[nblignes];
	
					for (int i = 0; i < nblignes; i++) {
						String ligneTexte = informations.readLine();
						tableauInformation[i] = ligneTexte;
				} // extraire toutes les lignes dans un tableau

			} catch (IOException e) {

				System.out.println("Une erreur est survenue lors de la lecture du fichier.");
			}
		}
		return tableauInformation;
	}

	/*
	 * Cette méthode vérifie si chacune des lignes du tableau envoyé en paramètre
	 * sont conformes au format demandé.
	 */
	public static boolean verifierFormatFic(String[] tableauInformation) {
		int lignePlats = 0;
		int ligneCommandes = 0;
		boolean verifier = true;
		if (tableauInformation[0].equals(LIGNE_CLIENTS_ENTREE)) {
			
			if (verifier) {

				for (int i = 0; i < tableauInformation.length && lignePlats == 0; i++) {
					if (tableauInformation[i].equals(LIGNE_PLATS_ENTREE)) {
						lignePlats = i;
					} // la ligne ou le mot Plats: se situe
				}

				for (int i = 1; i < lignePlats; i++) {
					if (tableauInformation[i].contains(" ")) {
						verifier = false;
					} // si un nom contient un espace
				}
				
				if (verifier) {

					for (int i = 0; i < tableauInformation.length && ligneCommandes == 0; i++) {
						if (tableauInformation[i].equals(LIGNE_COMMANDES_ENTREE)) {
							ligneCommandes = i;
						} // la ligne ou le mot Commandes: se situe
					}

					for (int i = lignePlats + 1; i < ligneCommandes; i++) {
						if (!tableauInformation[i].matches("^.[a-zA-Z_]+ [0-9.]+$")) {
							verifier = false;
						} // si un plat contient un espace avant le nombre
					}
					if (verifier) {
						
						if (!tableauInformation[tableauInformation.length - 1].equals("Fin")) {
							verifier =  false;
						}
						if (verifier) {
							for (int i = ligneCommandes + 1; i < tableauInformation.length - 1; i++) {
								if (!tableauInformation[i].matches("^.[a-zA-Z\u00C0-\u00FF]+ [a-zA-Z_]+ [0-9]+$")) {
									return false;
								} // si nom espace plat espace nombre
							}
						}
					}
				}
			}

		} else {

			verifier = false;
		}

		return verifier;
	}

	
}