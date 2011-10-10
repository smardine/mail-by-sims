/**
 * 
 */
package bdd.helper;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * @author smardine
 */
public final class EncodeDecode {
	/**
	 * Constructeur privé pour classe utilitaire
	 */
	private EncodeDecode() {

	}

	/**
	 * Trasforme des caracteres Html pour pouvoir les passer dans une requette
	 * SQL sans risquer des erreurs d'interpretations du requetteur
	 * @param aText
	 * @return la chaine de caracteres transformée.
	 */
	public static String encodeHTMLforBase(String aText) {
		if (aText == null) {
			return "inconnu";
		}
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\'') {
				result.append("&#039;");
			} else if (character == '&') {
				result.append("&amp;");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	/**
	 * Operation inverse de {@link encodeHTMLforBase}, on prend la chaine de
	 * caractere en base et on la converti en caractere html
	 * @param p_input
	 * @return
	 */
	public static String decodeHTMLFromBase(String p_input) {
		return p_input//
				.replaceAll("&lt;", "<")//
				.replaceAll("&gt;", ">")//
				.replaceAll("&quot;", "\"")//
				.replaceAll("&#039;", "\'")//
				.replaceAll("&amp;", "&");
	}

}
