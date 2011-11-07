/**
 * 
 */
package factory;

import java.awt.Font;

/**
 * @author smardine
 */
public final class Fontfactory {

	private static Font TREE_FONT_GRAS;
	private static Font TREE_FONT_PLAIN;

	private Fontfactory() {

	}

	/**
	 * @return the tREE_FONT_GRAS
	 */
	public static Font getTREE_FONT_GRAS() {
		if (TREE_FONT_GRAS == null) {
			TREE_FONT_GRAS = new Font("Perpetua", Font.BOLD, 12);
		}
		return TREE_FONT_GRAS;
	}

	/**
	 * @return the tREE_FONT_PLAIN
	 */
	public static Font getTREE_FONT_PLAIN() {
		if (TREE_FONT_PLAIN == null) {
			TREE_FONT_PLAIN = new Font("Perpetua", Font.PLAIN, 12);
		}
		return TREE_FONT_PLAIN;
	}

}
