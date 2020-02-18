import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import outilsjava.OutilsFichier;

public class LireEnvoyerCommandes {

	private static ArrayList<Client> listeClients = new ArrayList<Client>();
	private static ArrayList<Plat> listePlats = new ArrayList<Plat>();
	private static ArrayList<Commande> listeCommandes = new ArrayList<Commande>();
	
	public static void main(String[] args) throws IOException {
		String[] tableauInformation = lireFichierEtMettreDansTableau();
		if (verifier(tableauInformation)) {

			creerListes(tableauInformation);
			
			creerFacture();
			
		} else {
			System.out.println("Le fichier ne respecte pas le format demandé !\n\nArrêt du programme.");
		}
		
	}
	
	private static void creerFacture() throws IOException {
		
		// Vérifier si le client a des commandes à son nom.
		BufferedWriter informationOutput = OutilsFichier.ouvrirFicTexteEcriture("FichierSortie.txt");
		informationOutput.write("Bienvenue chez Barette!\nFactures :\n");
		
		for (int i = 0; i < listeClients.size(); i++) {
			ArrayList<Commande> commandesClientCourant = chercherCommandesClient(listeClients.get(i).getNom());
			double prix = 0;
			
			if (commandesClientCourant.isEmpty()) {
				//System.out.println(listeClients.get(i).getNom() + " " + prix + "$");
				
				informationOutput.write(listeClients.get(i).getNom() + " " + prix + "$");
				informationOutput.newLine();
				
			} else {
				
				for (int j = 0; j < commandesClientCourant.size(); j++) {
					//Additionner le prix de chaque commande faite par le client.
					prix += commandesClientCourant.get(j).getPrixPlat() * commandesClientCourant.get(j).getNbPlats();
					
				}
				//System.out.println(listeClients.get(i).getNom() + " " + prix + "$");
				
				informationOutput.write(listeClients.get(i).getNom() + " " + prix + "$");
				informationOutput.newLine();
			}
			
		}
		informationOutput.close();
	}
	
	private static ArrayList<Commande> chercherCommandesClient(String nomClient) {
		ArrayList<Commande> commandesClient = new ArrayList<Commande>();
		
		for (int i = 0; i < listeClients.size(); i++) {
			if (nomClient.equals(listeCommandes.get(i).getNomClient())) {
				commandesClient.add(listeCommandes.get(i));
			}
		}
		
		return commandesClient;
	}
	
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
	
	//Cette méthode popule les listes contenant les clients, les plats et les commandes.
	private static void creerListes(String[] tableauInformation) {
		String mode = "Clients"; //On sait que la première catégorie est clients.
		
		//On commence après «Clients :» et on ignore la dernière ligne du fichier.
		for (int i = 1; i < tableauInformation.length - 1; i++) { 
			if(tableauInformation[i].equals("Plats :")) {
				mode = "Plats";
			} else if (tableauInformation[i].equals("Commandes :")) {
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
					
					if (indexPlat < 0) {
						//Imprimer erreur
					} else {
						listeCommandes.get(listeCommandes.size() - 1).setPrixPlat(listePlats.get(indexPlat).getPrix());
					}
					break;
					
				default:
					break;
				}
			}
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
	
	//TODO Certains cas d'erreurs ne sont pas vérifiés pour la liste des commandes.
	//Je suggère également d'avoir un seul énoncé return à la fin de la méthode.
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
