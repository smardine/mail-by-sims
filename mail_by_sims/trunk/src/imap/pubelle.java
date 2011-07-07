package imap;

public class pubelle {

	// Object content = m.getContent();
	// Date date = m.getSentDate();
	// Address[] from = m.getFrom();
	// Address[] receiver = m.getAllRecipients();
	// String subj = m.getSubject();
	// String mimeType = m.getContentType();
	//	
	// String d = df.format(date);
	//
	// Enumeration<Header> allHeaders = m.getAllHeaders();
	// StringBuilder sb = new StringBuilder();
	//
	// while (allHeaders.hasMoreElements()) {
	// Header e = allHeaders.nextElement();
	// sb.append(e.getName() + ": " + e.getValue() + "\r\n");
	//
	// }
	// boolean complet = true;
	// // long dateActu = new Date().getTime();
	// String contenuComplet = recupContenuMail(m, complet, uid);
	// complet = false;
	// String contenu = recupContenuMail(m, complet, uid);
	// sb.append("\r\n" + contenuComplet);
	//
	// // creation du message:
	// if (sb != null) {
	// WriteFile.WriteFullFile(sb.toString(), GestionRepertoire
	// .RecupRepTravail()
	// + "/eml/" + uid + ".eml");
	// }
	// if (contenu != null) {
	// WriteFile.WriteFullFile(contenu, GestionRepertoire
	// .RecupRepTravail()
	// + "/html/" + uid + ".html");
	// // editor.setText(contenu);
	// editor.setPage(new URL("file:///"
	// + GestionRepertoire.RecupRepTravail() + "/html/"
	// + uid + ".html"));
	// }
	// StringBuilder sbRequette = new StringBuilder();
	// sbRequette
	// .append("INSERT INTO MAIL_RECU (ID_COMPTE, ID_DOSSIER_STOCKAGE, UID_MESSAGE, EXPEDITEUR, DESTINATAIRE, SUJET, CONTENU, DATE_RECEPTION)  VALUES (");
	// sbRequette.append("'" + p_idCompte + "','1'," + uid + ",'"
	// + from.toString() + "','" + receiver.toString() + "','"
	// + subj + "','" + contenu + "'," + date + ")");
	// BDRequette.executeRequete(sbRequette.toString());

}
