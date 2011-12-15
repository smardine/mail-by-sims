package bdd;

import java.util.ArrayList;
import java.util.List;

public class BDScripts {

	/**
	 * 
	 */
	private static final String AS = "AS ";
	/**
	 * 
	 */
	private static final String GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON = "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON ";
	/**
	 * 
	 */
	private static final String END2 = "END^ ";
	/**
	 * 
	 */
	private static final String SET_TERM2 = "SET TERM ; ^ ";
	/**
	 * 
	 */
	private static final String SET_TERM = "SET TERM ^ ; ";
	/**
	 * 
	 */
	private static final String END = "END ";
	/**
	 * 
	 */
	private static final String ACTIVE_BEFORE_INSERT_POSITION_0 = "ACTIVE BEFORE INSERT POSITION 0 ";
	/**
	 * 
	 */
	private static final String ID_COMPTE_INTEGER = "ID_COMPTE Integer, ";
	private List<String> allversion;
	private List<String> version1;
	private List<String> version2;
	private List<String> version3;
	private List<String> version4;
	private List<String> version5;
	private List<String> version6;
	private List<String> version7;
	private List<String> version8;

	public BDScripts() {

	}

	public List<String> getAll() {
		allversion = new ArrayList<String>();
		allversion.addAll(getVersion1());
		allversion.addAll(getVersion2());
		allversion.addAll(getVersion3());
		allversion.addAll(getVersion4());
		allversion.addAll(getVersion5());
		allversion.addAll(getVersion6());
		allversion.addAll(getVersion7());
		allversion.addAll(getVersion8());

		return allversion;
	}

	public List<String> getVersion1() {
		version1 = new ArrayList<String>();
		String generator = "CREATE GENERATOR GEN_BLACK_LISTE_ID;"
				+ " CREATE GENERATOR GEN_COMPTES_ID;"
				+ " CREATE GENERATOR GEN_DOSSIER_ID;"
				+ " CREATE GENERATOR GEN_MAIL_ENVOYE_ID;"
				+ " CREATE GENERATOR GEN_MAIL_RECU_ID;"
				+ " CREATE GENERATOR GEN_RELGES_ID;"
				+ "CREATE GENERATOR GEN_PIECE_JOINTE_ID;";
		version1.add(generator);

		String createTablesBlackList = "CREATE TABLE BLACK_LISTE "
				+ "( ID_BLACKLIST Integer,"
				+ " ADRESSE_BLACKLIST Varchar(500)," + " ID_DOSSIER Integer );";
		version1.add(createTablesBlackList);

		String createTablePieceJointe = "CREATE TABLE PIECE_JOINTE "
				+ "( ID_PIECE_JOINTE Integer,"
				+ " CONTENU_PIECE_JOINTE Blob sub_type 0,"
				+ " NOM_PIECE_JOINTE Varchar(500),"// 
				+ " ID_MESSAGE Integer );";
		version1.add(createTablePieceJointe);

		String createTableComptes = "CREATE TABLE COMPTES ( "
				+ BDScripts.ID_COMPTE_INTEGER + "NOM_COMPTE Varchar(500), "
				+ "SERVEUR_POP Varchar(500), " + "PORT_POP Bigint, "
				+ "SERVEUR_SMTP Varchar(500), " + "PORT_SMTP Integer, "
				+ "USERNAME Varchar(500), " + "PWD Varchar(500), "
				+ "CONSTRAINT UNQ_COMPTES_1 UNIQUE (ID_COMPTE) );";
		version1.add(createTableComptes);

		String createTableDossier = "CREATE TABLE DOSSIER ( "
				+ BDScripts.ID_COMPTE_INTEGER + "ID_DOSSIER Integer, "
				+ "ID_DOSSIER_PARENT Integer, " + "NOM_DOSSIER Varchar(500) "
				+ ");";
		version1.add(createTableDossier);

		String createTableMailEnvoye = "CREATE TABLE MAIL_ENVOYE ( "
				+ BDScripts.ID_COMPTE_INTEGER + "ID_MESSAGE_ENVOYE Integer, "
				+ "UID_MESSAGE Varchar(1000), " + "EXPEDITEUR Varchar(500), "
				+ "DESTINATAIRE Blob sub_type 0, " + "SUJET Varchar(1000), "
				+ "CONTENU Blob sub_type 0, " + "DATE_ENVOI Timestamp );";
		version1.add(createTableMailEnvoye);
		String createTableMailRecu = "CREATE TABLE MAIL_RECU ( "
				+ BDScripts.ID_COMPTE_INTEGER + "ID_DOSSIER_STOCKAGE Integer, "
				+ "ID_MESSAGE_RECU Integer, " + "UID_MESSAGE Varchar(1000), "
				+ "EXPEDITEUR Varchar(500), "
				+ "DESTINATAIRE Blob sub_type 0, " + "SUJET Varchar(1000), "
				+ "CONTENU Blob sub_type 0, " + "DATE_RECEPTION Timestamp, "
				+ "CONSTRAINT UNQ_MAIL_RECU_1 UNIQUE (ID_MESSAGE_RECU) );";
		version1.add(createTableMailRecu);
		String createTableParam = "CREATE TABLE PARAM ( VERSION_BASE Bigint );";
		version1.add(createTableParam);
		String createTableRegles = "CREATE TABLE REGLES ( "
				+ "ID_REGLE Integer, " + "ADRESSE_REGLE Varchar(500), "
				+ "ID_DOSSIER_STOCKAGE Integer );";
		version1.add(createTableRegles);
		/********************* VIEWS **********************/

		/******************* EXCEPTIONS *******************/

		/******************** TRIGGERS ********************/

		String createTriggerBlackList = "SET TERM  !; "
				+ "CREATE TRIGGER BLACK_LISTE_BI FOR BLACK_LISTE "
				+ BDScripts.ACTIVE_BEFORE_INSERT_POSITION_0
				+ "AS DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN "
				+ "IF (NEW.ID_BLACKLIST IS NULL) "
				+ "THEN NEW.ID_BLACKLIST = GEN_ID(GEN_BLACK_LISTE_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_BLACK_LISTE_ID, 0); "
				+ "if (tmp < new.ID_BLACKLIST) "
				+ "then tmp = GEN_ID(GEN_BLACK_LISTE_ID, new.ID_BLACKLIST-tmp); "
				+ BDScripts.END + "END ! " + "SET TERM ; ! ";
		version1.add(createTriggerBlackList);
		String createTriggerDossier = BDScripts.SET_TERM
				+ "CREATE TRIGGER DOSSIER_BI FOR DOSSIER "
				+ BDScripts.ACTIVE_BEFORE_INSERT_POSITION_0
				+ "AS DECLARE VARIABLE tmp DECIMAL(18,0); " + "BEGIN "
				+ "IF (NEW.ID_DOSSIER IS NULL) "
				+ "THEN NEW.ID_DOSSIER = GEN_ID(GEN_DOSSIER_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_DOSSIER_ID, 0); "
				+ "if (tmp < new.ID_DOSSIER) "
				+ "then tmp = GEN_ID(GEN_DOSSIER_ID, new.ID_DOSSIER-tmp); "
				+ BDScripts.END + BDScripts.END2 + BDScripts.SET_TERM2;
		version1.add(createTriggerDossier);
		String createTriggerComptes = BDScripts.SET_TERM
				+ "CREATE TRIGGER ID_COMPTE FOR COMPTES "
				+ BDScripts.ACTIVE_BEFORE_INSERT_POSITION_0 + BDScripts.AS
				+ "DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_COMPTE IS NULL) "
				+ "THEN NEW.ID_COMPTE = GEN_ID(GEN_COMPTES_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_COMPTES_ID, 0); "
				+ "if (tmp < new.ID_COMPTE) "
				+ "then tmp = GEN_ID(GEN_COMPTES_ID, new.ID_COMPTE-tmp); "
				+ BDScripts.END + BDScripts.END2 + BDScripts.SET_TERM2;
		version1.add(createTriggerComptes);
		String createTriggerMailEnvoye = BDScripts.SET_TERM
				+ "CREATE TRIGGER MAIL_ENVOYE_BI FOR MAIL_ENVOYE "
				+ BDScripts.ACTIVE_BEFORE_INSERT_POSITION_0
				+ "AS DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_MESSAGE_ENVOYE IS NULL) "
				+ "THEN NEW.ID_MESSAGE_ENVOYE = GEN_ID(GEN_MAIL_ENVOYE_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_MAIL_ENVOYE_ID, 0); "
				+ "if (tmp < new.ID_MESSAGE_ENVOYE) "
				+ "then tmp = GEN_ID(GEN_MAIL_ENVOYE_ID, new.ID_MESSAGE_ENVOYE-tmp);"
				+ " END " + BDScripts.END2 + BDScripts.SET_TERM2;
		version1.add(createTriggerMailEnvoye);
		String createTriggerMailRecu = BDScripts.SET_TERM
				+ "CREATE TRIGGER MAIL_RECU_BI FOR MAIL_RECU "
				+ BDScripts.ACTIVE_BEFORE_INSERT_POSITION_0
				+ BDScripts.AS
				+ "DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_MESSAGE_RECU IS NULL) "
				+ "THEN NEW.ID_MESSAGE_RECU = GEN_ID(GEN_MAIL_RECU_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_MAIL_RECU_ID, 0); "
				+ "if (tmp < new.ID_MESSAGE_RECU) "
				+ "then tmp = GEN_ID(GEN_MAIL_RECU_ID, new.ID_MESSAGE_RECU-tmp); "
				+ BDScripts.END + BDScripts.END2 + BDScripts.SET_TERM2;
		version1.add(createTriggerMailRecu);

		String createTriggerRegles = BDScripts.SET_TERM
				+ "CREATE TRIGGER RELGES_BI FOR REGLES "
				+ BDScripts.ACTIVE_BEFORE_INSERT_POSITION_0 + BDScripts.AS
				+ "DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_REGLE IS NULL) "
				+ "THEN NEW.ID_REGLE = GEN_ID(GEN_RELGES_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_RELGES_ID, 0); "
				+ "if (tmp < new.ID_REGLE) "
				+ "then tmp = GEN_ID(GEN_RELGES_ID, new.ID_REGLE-tmp); "
				+ BDScripts.END + BDScripts.END2 + "SET TERM ; ^";
		version1.add(createTriggerRegles);

		String createTriggerPieceJointe = BDScripts.SET_TERM
				+ "CREATE TRIGGER PIECE_JOINTE_BI FOR PIECE_JOINTE "
				+ BDScripts.ACTIVE_BEFORE_INSERT_POSITION_0
				+ BDScripts.AS
				+ "DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_PIECE_JOINTE IS NULL) "
				+ "THEN NEW.ID_PIECE_JOINTE = GEN_ID(GEN_PIECE_JOINTE_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_PIECE_JOINTE_ID, 0); "
				+ "if (tmp < NEW.ID_PIECE_JOINTE) "
				+ "then tmp = GEN_ID(GEN_PIECE_JOINTE_ID, new.ID_PIECE_JOINTE-tmp); "
				+ BDScripts.END + BDScripts.END2 + "SET TERM ; ^";
		version1.add(createTriggerPieceJointe);

		String createIndex = "CREATE INDEX IDX_BLACK_LISTE1 ON BLACK_LISTE (ID_BLACKLIST) ;"
				+ "CREATE INDEX IDX_COMPTES_ID_COMPTE ON COMPTES (ID_COMPTE); "
				+ "CREATE INDEX IDX_DOSSIER_ID_DOSSIER ON DOSSIER (ID_DOSSIER); "
				+ "CREATE INDEX IDX_MAIL_ENVOYE_ID_MESS_ENVOYE ON MAIL_ENVOYE (ID_MESSAGE_ENVOYE); "
				+ "CREATE INDEX IDX_MAIL_ENVOYE_UID_MESSAGE ON MAIL_ENVOYE (UID_MESSAGE); "
				+ "CREATE INDEX IDX_MAIL_ENVOYE_DATE_ENVOI ON MAIL_ENVOYE (DATE_ENVOI); "
				+ "CREATE UNIQUE INDEX IDX_MAIL_RECU_ID_MESS_RECU ON MAIL_RECU (ID_MESSAGE_RECU); "
				+ "CREATE INDEX IDX_MAIL_RECU_UID_MESSAGE ON MAIL_RECU (UID_MESSAGE); "
				+ "CREATE INDEX IDX_RELGES_ID_REGLE ON REGLES (ID_REGLE);"
				+ "CREATE INDEX IDX_PIECE_JOINTE_ID_PJ ON PIECE_JOINTE (ID_PIECE_JOINTE);";
		version1.add(createIndex);

		String createGrant = BDScripts.GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON
				+ "BLACK_LISTE TO  SYSDBA WITH GRANT OPTION; "
				+ BDScripts.GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON
				+ "COMPTES TO  SYSDBA WITH GRANT OPTION; "
				+ BDScripts.GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON
				+ "DOSSIER TO  SYSDBA WITH GRANT OPTION; "
				+ BDScripts.GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON
				+ "MAIL_ENVOYE TO  SYSDBA WITH GRANT OPTION; "
				+ BDScripts.GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON
				+ "MAIL_RECU TO  SYSDBA WITH GRANT OPTION; "
				+ BDScripts.GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON
				+ "PARAM TO  SYSDBA WITH GRANT OPTION; "
				+ BDScripts.GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON
				+ "REGLES TO  SYSDBA WITH GRANT OPTION; "
				+ BDScripts.GRANT_DELETE_INSERT_REFERENCES_SELECT_UPDATE_ON
				+ "PIECE_JOINTE TO  SYSDBA WITH GRANT OPTION;";
		version1.add(createGrant);

		version1.add("INSERT INTO PARAM (VERSION_BASE) VALUES ( 1);");

		return version1;
	}

	public List<String> getVersion2() {
		version2 = new ArrayList<String>();
		String script = "ALTER TABLE MAIL_RECU " + "ADD STATUT Char(1) "
				+ "CHARACTER SET ISO8859_1 NOT NULL COLLATE FR_FR; ";
		version2.add(script);
		script = "UPDATE MAIL_RECU SET STATUT = '1' WHERE STATUT IS NULL;";
		version2.add(script);
		version2.add("UPDATE PARAM SET VERSION_BASE=2;");
		return version2;
	}

	public List<String> getVersion3() {
		version3 = new ArrayList<String>();
		version3.add("ALTER TABLE COMPTES " + "ADD TYPE_COMPTE Varchar(7) "
				+ "CHARACTER SET ISO8859_1 NOT NULL " + "COLLATE FR_FR; ");
		version3.add("UPDATE COMPTES SET TYPE_COMPTE = 'pop' "
				+ "WHERE TYPE_COMPTE IS NULL;");
		version3.add("UPDATE PARAM SET VERSION_BASE=3;");
		return version3;
	}

	public List<String> getVersion4() {
		version4 = new ArrayList<String>();
		version4
				.add("CREATE INDEX IDX_MAIL_RECU_ID_COMPTE ON MAIL_RECU (ID_COMPTE);");
		version4
				.add("CREATE INDEX IDX_MAIL_RECU_ID_DOSSIER_STOCK ON MAIL_RECU (ID_DOSSIER_STOCKAGE);");
		version4
				.add("CREATE DESCENDING INDEX IDX_MAIL_RECU_DATE_REC_DESC ON MAIL_RECU (DATE_RECEPTION);");
		version4.add("UPDATE PARAM SET VERSION_BASE=4;");
		return version4;
	}

	public List<String> getVersion5() {
		version5 = new ArrayList<String>();
		version5
				.add("ALTER TABLE DOSSIER ADD NOM_INTERNET Varchar(999) CHARACTER SET ISO8859_1;");
		version5.add("UPDATE PARAM SET VERSION_BASE=5;");
		return version5;
	}

	public List<String> getVersion6() {
		version6 = new ArrayList<String>();
		version6.add("CREATE INDEX IDX_NOM_DOSSIER ON DOSSIER (NOM_DOSSIER);");
		version6
				.add("CREATE INDEX IDX_DOSSIER_CPT ON DOSSIER (ID_COMPTE, ID_DOSSIER_PARENT);");
		version6.add("CREATE INDEX IDX_NOM_COMPTE ON COMPTES (NOM_COMPTE);");
		version6
				.add("CREATE INDEX IDX_PIECE_JOINTE1 ON PIECE_JOINTE (ID_MESSAGE);");
		version6
				.add("CREATE INDEX IDX_PIECE_JOINTE2 ON PIECE_JOINTE (NOM_PIECE_JOINTE);");
		version6.add("UPDATE PARAM SET VERSION_BASE=6;");
		return version6;
	}

	/**
	 * @return
	 */
	public List<String> getVersion7() {
		version7 = new ArrayList<String>();
		version7.add("ALTER TABLE MAIL_RECU ADD DESTINATAIRE_COPY Blob;");
		version7.add("ALTER TABLE MAIL_RECU ADD DESTINATAIRE_CACHE Blob;");
		version7.add("ALTER TABLE MAIL_RECU ALTER ID_COMPTE POSITION 1;");

		version7
				.add("ALTER TABLE MAIL_RECU ALTER ID_DOSSIER_STOCKAGE POSITION 2;");
		version7.add("ALTER TABLE MAIL_RECU ALTER ID_MESSAGE_RECU POSITION 3;");
		version7.add("ALTER TABLE MAIL_RECU ALTER UID_MESSAGE POSITION 4;");
		version7.add("ALTER TABLE MAIL_RECU ALTER EXPEDITEUR POSITION 5;");
		version7.add("ALTER TABLE MAIL_RECU ALTER DESTINATAIRE POSITION 6;");
		version7
				.add("ALTER TABLE MAIL_RECU ALTER DESTINATAIRE_COPY POSITION 7;");
		version7
				.add("ALTER TABLE MAIL_RECU ALTER DESTINATAIRE_CACHE POSITION 8;");
		version7.add("ALTER TABLE MAIL_RECU ALTER SUJET POSITION 9;");
		version7.add("ALTER TABLE MAIL_RECU ALTER CONTENU POSITION 10;");
		version7.add("ALTER TABLE MAIL_RECU ALTER DATE_RECEPTION POSITION 11;");
		version7.add("ALTER TABLE MAIL_RECU ALTER STATUT POSITION 12;");
		version7.add("UPDATE PARAM SET VERSION_BASE=7;");

		return version7;
	}

	/**
	 * @return
	 */
	public List<String> getVersion8() {
		version8 = new ArrayList<String>();
		version8.add("DROP TABLE MAIL_ENVOYE;");
		version8.add("ALTER TABLE MAIL_RECU ADD FLAG Char(1);");
		version8.add("UPDATE PARAM SET VERSION_BASE=8;");
		return version8;
	}

}
