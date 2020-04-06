package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import outilsjava.OutilsFichier;

public class LireEnvoyerCommandes {

	public static ArrayList<Client> listeClients = new ArrayList<Client>();
	public static ArrayList<Plat> listePlats = new ArrayList<Plat>();
	public static ArrayList<Commande> listeCommandes = new ArrayList<Commande>();

	private static final String LIGNE_CLIENTS_ENTREE = "Clients :";
	private static final String LIGNE_PLATS_ENTREE = "Plats :";
	private static final String LIGNE_COMMANDES_ENTREE = "Commandes :";
	
	public static ArrayList<String> erreurs = new ArrayList<String>();
	public static ArrayList<String> factures = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

		String[] tableauInformation = lireFichierEtMettreDansTableau();
		
		if (tableauInformation != null) {
			
			if (verifierFormatFic(tableauInformation)) {
				
				creerListes(tableauInformation);
				
				String[] facture = creerSortie();
				ecrireFicSortie(facture);
				afficherContenuTableau(facture);
				
			} else {
				System.out.println("Le fichier ne respecte pas le format demand� !\nArr�t du programme.");
			}
		}

	}

	/*
	 * Affiche toutes les lignes d'un tableau de caract�res dans la console.
	 */
	public static void afficherContenuTableau(String[] tabFacture) {
		System.out.println("Contenu de la sortie :\n");
		
		for (String ligneSortie : tabFacture) {
			System.out.println(ligneSortie);
		}
	}
	
	
	/*
	 * Cr�e la facture pour la commande entr�e en param�tre si elle est valide.
	 * Sinon, cr�e les erreurs pour la commande entr�e en param�tre.
	 */
	public static void traiterCommande(Commande commandeCourante) {
		double prixAvantTaxes = 0;
		double prixApresTaxes;
		boolean valide = true;
		String erreur = "\nCommande incorrecte\n" + 
				commandeCourante.getNomClient() + " " +
				commandeCourante.getNomPlat() +  " " +
				commandeCourante.getNbPlats() + "\n";
		
		if (clientExiste(commandeCourante) && platExiste(commandeCourante) && commandeCourante.getNbPlats() > 0) {
			
			prixAvantTaxes = commandeCourante.getPrixPlat() * commandeCourante.getNbPlats();
			
			factures.add(commandeCourante.getNomClient() + " " +
				String.format("%.2f", prixAvantTaxes) + "$");
			
		} else {
			
			if(!clientExiste(commandeCourante)) {
				valide = false;
				erreur += "Le client " + commandeCourante.getNomClient() + " n'existe pas. ";
			}
			
			if(!platExiste(commandeCourante)) {
				erreur += "Le plat " + commandeCourante.getNomPlat() + " n'existe pas. ";
			}
			
			if(commandeCourante.getNbPlats() == 0) {
				erreur += "Le client doit commander au moins un plat. ";
			}

			if(!valide) {
				erreurs.add(erreur);
			}
		}
			
	}
	
	public static double calculerTaxes(double montantAvantTaxes) {
		/*
		 * Voici une coquille de m�thode qui calcule les taxes.
		 * �videmment, je ne fais que sugg�rer une fa�on de g�rer les taxes.
		 * J'ai �galement ajout� un squelette dans creerFacture.
		 * N'h�site pas � utiliser une structure diff�rente si celle-ci ne fonctionne pas.
		 */
		return 0;
	}
	
	/*
	 * V�rifie si le client de la commande entr�e en param�tre existe dans
	 * la liste de clients. Retourne true si c'est le cas et false dans le cas contraire.
	 */
	public static boolean clientExiste(Commande commandeAVerifier) {
		boolean valide = true;
		
		if (chercherIndexClient(commandeAVerifier.getNomClient()) < 0) {
			valide = false;
		}
		
		return valide;
	}
	
	/*
	 * V�rifie si le plat de la commande entr�e en param�tre existe dans
	 * la liste de plats. Retourne true si c'est le cas et false dans le cas contraire.
	 */
	public static boolean platExiste(Commande commandeAVerifier) {
		boolean valide = true;
		
		if (chercherIndexPlat(commandeAVerifier.getNomPlat()) < 0) {
			valide = false;
		}
		
		return valide;
	}
	
	/*
	 * Cr�e un tableau de caract�res contenant la sortie.
	 */
	public static String[] creerSortie() throws IOException {
		ArrayList<String> sortie = new ArrayList<String>();
		
		sortie.add("Bienvenue chez Barette!");
		
		for (Commande commandeCourante : listeCommandes) {
			traiterCommande(commandeCourante);
		}

		if (!erreurs.isEmpty()) {
			sortie.add("\nErreurs :");
			sortie.addAll(erreurs);
		}
		
		sortie.add("\nFactures :");
		sortie.addAll(factures);
		
		return convertirVersTabChaines(sortie);
	}
	
	/*
	 * Convertit un ArrayList de caract�res en tableau de caract�res.
	 */
	public static String[] convertirVersTabChaines(ArrayList<String> tab) 
    { 
		// Convertir l'ArrayList en tableau d'objets.
        Object[] tabObj = tab.toArray(); 
  
        // Convertir le tableau d'objets en tableau de cha�nes.
        String[] str = Arrays .copyOf(tabObj, tabObj.length, String[].class); 
  
        return str; 
    } 
	
	
	/*
	 * �crit le contenu de la sortie dans un fichier nomm� Facture-du-date-heure.txt.
	 */	
	public static void ecrireFicSortie(String[] sortie) throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH");  
		TimeZone EST = TimeZone.getTimeZone("EST");
		Calendar maintenant = Calendar.getInstance(EST);
		Date date = maintenant.getTime();
		String nomFicSortie = "Facture-du-" + formatter.format(date) + ".txt";
		BufferedWriter informationOutput = OutilsFichier.ouvrirFicTexteEcriture(nomFicSortie);
		
		if (informationOutput != null) {
			
			for (int i = 0; i < sortie.length; i++) {
				informationOutput.write(sortie[i]);
				informationOutput.newLine();
			}
			
			informationOutput.close();
			System.out.println("Facture �crite dans " + nomFicSortie + ".\n");
		} else {
			System.out.println("\nOp�ration annul�e. Fin du programme.");
		}
		
	}

	/*
	 * Cherche dans la liste de plats et retourne l'index du plat indiqu� en
	 * param�tre. Retourne -1 si le plat n'existe pas.
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
	 * Cherche dans la liste de clients et retourne l'index du client indiqu� en
	 * param�tre. Retourne -1 si le client n'existe pas.
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

	// Cette m�thode popule les listes contenant les clients, les plats et les commandes.
	public static void creerListes(String[] tableauInformation) {
		String mode = "Clients"; // On sait que la premi�re cat�gorie est clients.

		// On commence apr�s �Clients :� et on ignore la derni�re ligne du tableau.
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
	 * Lis le fichier fichierEntree.txt et transf�re son contenu dans un tableau qui est retourn�.
	 */
	private static String[] lireFichierEtMettreDansTableau() {

		int nblignes = 0;
		String[] tableauInformation = null;
		BufferedReader informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt");

		if (informations != null) {
			try {
				while (informations.readLine() != null)
					// trouver le nombre de lignes
					nblignes++;
					// reload le buffereader pour relire les lignes
					informations = OutilsFichier.ouvrirFicTexteLecture("fichierEntree.txt"); 
					tableauInformation = new String[nblignes];
	
					// extraire toutes les lignes dans un tableau
					for (int i = 0; i < nblignes; i++) {
						String ligneTexte = informations.readLine();
						tableauInformation[i] = ligneTexte;
				} 

			} catch (IOException e) {

				System.out.println("Une erreur est survenue lors de la lecture du fichier.");
			}
		}
		return tableauInformation;
	}

	/*
	 * Cette m�thode v�rifie si chacune des lignes du tableau envoy� en param�tre sont conformes au format demand�.
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