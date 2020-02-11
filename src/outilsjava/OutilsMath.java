/**
 * Auteure : Soti
 * Fichier : OutilsMath.java
 * Package : outilsjava
 * Date    : Automne 2019
 * Cours   : Programmation avec Java
 */

// La classe OutilsMath fait partie du package outilsjava.

package outilsjava;

/**
 * La classe OutilsMath contient des fonctions mathématiques que l'on ne trouvent pas dans la classe Math et qui
 * peuvent être très utiles dans les programmes Java.
 */

public class OutilsMath {

	/**
	 * La méthode publique statique factoriel() permet de calculer et de retourner le factoriel d'un nombre.
	 * 
	 * @param nb
	 *            Le nombre dont on désire le factoriel.
	 *            
	 * @return Le factoriel du nombre reçu en paramètre.
	 */

	public static int factoriel( int nb ) {
		int produit = 1;

		for ( int i = 1; i <= nb; ++i ) {
			produit *= i;
		}

		return produit;
	}

	/**
	 * La méthode publique statique hasard() permet de générer un nombre entier aléatoire entre un nombre minimum (>=0)
	 * et un nombre maximum inclus.
	 * 
	 * @param min
	 *            Le nombre minimum.
	 * @param max
	 *            Le nombre maximum
	 * 
	 * @return Le nombre entier aléatoire entre min (inclus) et max (inclus).
	 */

	public static int hasard( int min, int max ) {

		// Math.random() retourne un double entre 0 (inclus) et 1 (exclu).
		// Ici on veut un nombre entier entre min (inclus) et max (inclus).

		return (int) ( ( Math.random() * ( max - min + 1 ) ) + min );
	}
}