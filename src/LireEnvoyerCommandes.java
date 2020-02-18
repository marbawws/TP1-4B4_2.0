import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import outilsjava.OutilsFichier;

public class LireEnvoyerCommandes {

	private static ArrayList<Client> listeClients = new ArrayList<Client>();
	private static ArrayList<Plat> listePlats = new ArrayList<Plat>();
	private static ArrayList<Commande> listeCommandes = new ArrayList<Commande>();
	
	private static final String LIGNE_CLIENTS_ENTREE = "Clients :";
	private static final String LIGNE_PLATS_ENTREE = "Plats :";
	private static final String LIGNE_COMMANDES_ENTREE = "Commandes :";
	
	public static void main(String[] args) {
		String[] tableauInformation = lireFichierEtMettreDansTableau();
		if (verifier(tableauInformation)) {

			creerListes(tableauInformation);
			
			System.out.println("Bienvenue chez Barette!\nFactures :");
			
			creerFacture();
			
		} else {
			System.out.println("Le fichier ne respecte pas le format demand� !\n\nArr�t du programme.");
		}
		
	}
	
	/*
	 * TODO Bien qu'il fonctionne, le code de l'affichage des factures (creerFacture(), chercherCommandeClient()
	 * et chercherIndexPlat() est un brouillon et devra �tre optimis�.
	 */
	
	private static void creerFacture() {
		
		// V�rifier si le client a des commandes � son nom.
		for (int i = 0; i < listeClients.size(); i++) {
			ArrayList<Commande> commandesClientCourant = chercherCommandesClient(listeClients.get(i).getNom());
			double prix = 0;
			
			if (commandesClientCourant.isEmpty()) {
				System.out.println(listeClients.get(i).getNom() + " " + String.format("%.2f", prix) + "$");
			} else {
				for (int j = 0; j < commandesClientCourant.size(); j++) {
					//Additionner le prix de chaque commande faite par le client.
					prix += commandesClientCourant.get(j).getPrixPlat() * commandesClientCourant.get(j).getNbPlats();
				}
				System.out.println(listeClients.get(i).getNom() + " " + String.format("%.2f", prix) + "$");
			}
			
		}
	}
	
	/*
	Retourne une liste contenant les commandes du client indiqu� en param�tre. Retourne une liste vide
	si le client n'a rien command�.
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
	
	//Retourne l'index du plat indiqu� en param�tre ou -1 si le plat n'existe pas.
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
	
	//Cette m�thode popule les listes contenant les clients, les plats et les commandes.
	private static void creerListes(String[] tableauInformation) {
		String mode = "Clients"; //On sait que la premi�re cat�gorie est clients.
		
		//On commence apr�s �Clients :� et on ignore la derni�re ligne du fichier.
		for (int i = 1; i < tableauInformation.length - 1; i++) { 
			if(tableauInformation[i].equals(LIGNE_PLATS_ENTREE)) {
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
			// le fichier n'existe pas, le message d'erreur est d�j� envoyer par soti <3
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
				
				System.out.println("Une erreur est survenue lors de la lecture du fichier.");
			}
		}
		return tableauInformation;
	}
	
	/*Cette m�thode v�rifie si chacune des lignes du tableau envoyer en param�tre est conforme au format*/
	
	//TODO Certains cas d'erreurs ne sont pas v�rifi�s pour la liste des commandes.
	//Je sugg�re �galement d'avoir un seul �nonc� return � la fin de la m�thode.
	private static boolean verifier(String[] tableauInformation) {
		int lignePlats = 0;
		int ligneCommandes = 0;
		if (tableauInformation[0].equals(LIGNE_CLIENTS_ENTREE)) {

			for (int i = 0; i < tableauInformation.length && lignePlats == 0; i++) {
				if (tableauInformation[i].equals(LIGNE_PLATS_ENTREE)) {
					lignePlats = i;
				} // la ligne ou le mot Plats: se situe
			}

			for (int i = 1; i < lignePlats; i++) {
				if (tableauInformation[i].contains(" ")) {
					return false;
				} // si un nom contient un espace
			}

			for (int i = 0; i < tableauInformation.length && ligneCommandes == 0; i++) {
				if (tableauInformation[i].equals(LIGNE_COMMANDES_ENTREE)) {
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
