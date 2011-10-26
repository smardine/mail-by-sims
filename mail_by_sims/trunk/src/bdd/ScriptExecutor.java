package bdd;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import releve.imap.util.messageUtilisateur;
import tools.Historique;

public class ScriptExecutor {
	private final String TAG = "ScriptExecutor";
	private int i;
	private final String CMDTERMINATOR = "SET TERM ";
	private final int SIZETERMINATOR = CMDTERMINATOR.length();
	private final char TERMINATOR = ';';
	private char terminator = TERMINATOR, newterminator = TERMINATOR;
	private boolean comment, hassql = false;
	private StringBuilder sql;
	private String onesql;
	private Statement stmt = null;

	public ScriptExecutor() {
	}

	boolean executeScriptSQL(Connection conn, String p_script) {

		try {
			comment = false;
			sql = new StringBuilder();
			stmt = conn.createStatement();
			for (i = 0; i < p_script.length(); i++) {
				if (p_script.charAt(i) == '\r' || p_script.charAt(i) == '\n') {
					// change ENTER (Windows/Linux/...) to white space
					sql.append(' ');
					continue;
				}
				if (!comment) {
					if (p_script.charAt(i) == terminator) {
						onesql = sql.toString().trim();
						if (onesql.length() > 0) {
							hassql = true;
							stmt.addBatch(onesql);
							// System.out.println(onesql);
						}
						sql = new StringBuilder();
						if (newterminator != terminator) {
							terminator = newterminator;
						}

					} else if (p_script.charAt(i) == '/'
							&& p_script.charAt(i + 1) == '*') { //
						// comment start
						comment = true;
						i++;
					} else {
						if (i + SIZETERMINATOR < p_script.length()
								&& p_script.substring(i, i + SIZETERMINATOR)
										.equalsIgnoreCase(CMDTERMINATOR)) {
							i += SIZETERMINATOR;
							while (i < p_script.length()) {
								if (p_script.charAt(i) == ' ') {
									// jump out white spaces between "SET TERM"
									// and new terminator
									i++;
								} else {
									break;
								}

							}
							newterminator = p_script.charAt(i);
						} else {
							sql.append(p_script.charAt(i));
						}
					}
				} else {
					if (p_script.charAt(i) == '*'
							&& p_script.charAt(i + 1) == '/') { //
						// comment end
						comment = false;
						i++;
					}
				}
			}
			if (hassql) {
				stmt.executeBatch();
			}
			return true;
		} catch (Exception e) {
			messageUtilisateur.affMessageException(TAG, e,
					"erreur à l'execution du script");
			Historique.ecrire(p_script);
			return false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				messageUtilisateur.affMessageException(TAG, e,
						"erreur à l'execution du script");
				Historique.ecrire(p_script);
			}
		}
	}
}
