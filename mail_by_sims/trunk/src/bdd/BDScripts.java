package bdd;

import java.util.ArrayList;

public class BDScripts {

	private ArrayList<String> version1;
	private ArrayList<String> version2;
	private ArrayList<String> version3;
	private ArrayList<String> version4;
	private ArrayList<String> version5;

	public BDScripts() {

	}

	public ArrayList<String> getVersion1() {
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
				+ "ID_COMPTE Integer, " + "NOM_COMPTE Varchar(500), "
				+ "SERVEUR_POP Varchar(500), " + "PORT_POP Bigint, "
				+ "SERVEUR_SMTP Varchar(500), " + "PORT_SMTP Integer, "
				+ "USERNAME Varchar(500), " + "PWD Varchar(500), "
				+ "CONSTRAINT UNQ_COMPTES_1 UNIQUE (ID_COMPTE) );";
		version1.add(createTableComptes);

		String createTableDossier = "CREATE TABLE DOSSIER ( "
				+ "ID_COMPTE Integer, " + "ID_DOSSIER Integer, "
				+ "ID_DOSSIER_PARENT Integer, " + "NOM_DOSSIER Varchar(500) "
				+ ");";
		version1.add(createTableDossier);

		String createTableMailEnvoye = "CREATE TABLE MAIL_ENVOYE ( "
				+ "ID_COMPTE Integer, " + "ID_MESSAGE_ENVOYE Integer, "
				+ "UID_MESSAGE Varchar(1000), " + "EXPEDITEUR Varchar(500), "
				+ "DESTINATAIRE Blob sub_type 0, " + "SUJET Varchar(1000), "
				+ "CONTENU Blob sub_type 0, " + "DATE_ENVOI Timestamp );";
		version1.add(createTableMailEnvoye);
		String createTableMailRecu = "CREATE TABLE MAIL_RECU ( "
				+ "ID_COMPTE Integer, " + "ID_DOSSIER_STOCKAGE Integer, "
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
				+ "ACTIVE BEFORE INSERT POSITION 0 "
				+ "AS DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN "
				+ "IF (NEW.ID_BLACKLIST IS NULL) "
				+ "THEN NEW.ID_BLACKLIST = GEN_ID(GEN_BLACK_LISTE_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_BLACK_LISTE_ID, 0); "
				+ "if (tmp < new.ID_BLACKLIST) "
				+ "then tmp = GEN_ID(GEN_BLACK_LISTE_ID, new.ID_BLACKLIST-tmp); "
				+ "END " + "END ! " + "SET TERM ; ! ";
		version1.add(createTriggerBlackList);
		String createTriggerDossier = "SET TERM ^ ; "
				+ "CREATE TRIGGER DOSSIER_BI FOR DOSSIER "
				+ "ACTIVE BEFORE INSERT POSITION 0 "
				+ "AS DECLARE VARIABLE tmp DECIMAL(18,0); " + "BEGIN "
				+ "IF (NEW.ID_DOSSIER IS NULL) "
				+ "THEN NEW.ID_DOSSIER = GEN_ID(GEN_DOSSIER_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_DOSSIER_ID, 0); "
				+ "if (tmp < new.ID_DOSSIER) "
				+ "then tmp = GEN_ID(GEN_DOSSIER_ID, new.ID_DOSSIER-tmp); "
				+ "END " + "END^ " + "SET TERM ; ^ ";
		version1.add(createTriggerDossier);
		String createTriggerComptes = "SET TERM ^ ; "
				+ "CREATE TRIGGER ID_COMPTE FOR COMPTES "
				+ "ACTIVE BEFORE INSERT POSITION 0 " + "AS "
				+ "DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_COMPTE IS NULL) "
				+ "THEN NEW.ID_COMPTE = GEN_ID(GEN_COMPTES_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_COMPTES_ID, 0); "
				+ "if (tmp < new.ID_COMPTE) "
				+ "then tmp = GEN_ID(GEN_COMPTES_ID, new.ID_COMPTE-tmp); "
				+ "END " + "END^ " + "SET TERM ; ^ ";
		version1.add(createTriggerComptes);
		String createTriggerMailEnvoye = "SET TERM ^ ; "
				+ "CREATE TRIGGER MAIL_ENVOYE_BI FOR MAIL_ENVOYE "
				+ "ACTIVE BEFORE INSERT POSITION 0 "
				+ "AS DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_MESSAGE_ENVOYE IS NULL) "
				+ "THEN NEW.ID_MESSAGE_ENVOYE = GEN_ID(GEN_MAIL_ENVOYE_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_MAIL_ENVOYE_ID, 0); "
				+ "if (tmp < new.ID_MESSAGE_ENVOYE) "
				+ "then tmp = GEN_ID(GEN_MAIL_ENVOYE_ID, new.ID_MESSAGE_ENVOYE-tmp);"
				+ " END " + "END^ " + "SET TERM ; ^ ";
		version1.add(createTriggerMailEnvoye);
		String createTriggerMailRecu = "SET TERM ^ ; "
				+ "CREATE TRIGGER MAIL_RECU_BI FOR MAIL_RECU "
				+ "ACTIVE BEFORE INSERT POSITION 0 "
				+ "AS "
				+ "DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_MESSAGE_RECU IS NULL) "
				+ "THEN NEW.ID_MESSAGE_RECU = GEN_ID(GEN_MAIL_RECU_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_MAIL_RECU_ID, 0); "
				+ "if (tmp < new.ID_MESSAGE_RECU) "
				+ "then tmp = GEN_ID(GEN_MAIL_RECU_ID, new.ID_MESSAGE_RECU-tmp); "
				+ "END " + "END^ " + "SET TERM ; ^ ";
		version1.add(createTriggerMailRecu);

		String createTriggerRegles = "SET TERM ^ ; "
				+ "CREATE TRIGGER RELGES_BI FOR REGLES "
				+ "ACTIVE BEFORE INSERT POSITION 0 " + "AS "
				+ "DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_REGLE IS NULL) "
				+ "THEN NEW.ID_REGLE = GEN_ID(GEN_RELGES_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_RELGES_ID, 0); "
				+ "if (tmp < new.ID_REGLE) "
				+ "then tmp = GEN_ID(GEN_RELGES_ID, new.ID_REGLE-tmp); "
				+ "END " + "END^ " + "SET TERM ; ^";
		version1.add(createTriggerRegles);

		String createTriggerPieceJointe = "SET TERM ^ ; "
				+ "CREATE TRIGGER PIECE_JOINTE_BI FOR PIECE_JOINTE "
				+ "ACTIVE BEFORE INSERT POSITION 0 "
				+ "AS "
				+ "DECLARE VARIABLE tmp DECIMAL(18,0); "
				+ "BEGIN IF (NEW.ID_PIECE_JOINTE IS NULL) "
				+ "THEN NEW.ID_PIECE_JOINTE = GEN_ID(GEN_PIECE_JOINTE_ID, 1); "
				+ "ELSE BEGIN tmp = GEN_ID(GEN_PIECE_JOINTE_ID, 0); "
				+ "if (tmp < NEW.ID_PIECE_JOINTE) "
				+ "then tmp = GEN_ID(GEN_PIECE_JOINTE_ID, new.ID_PIECE_JOINTE-tmp); "
				+ "END " + "END^ " + "SET TERM ; ^";
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
				+ "CREATE INDEX IDX_PIECE_JOINTE_ID_PIECE_JOINTE ON PIECE_JOINTE (ID_PIECE_JOINTE);";
		version1.add(createIndex);

		String createGrant = "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON "
				+ "BLACK_LISTE TO  SYSDBA WITH GRANT OPTION; "
				+ "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON "
				+ "COMPTES TO  SYSDBA WITH GRANT OPTION; "
				+ "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON "
				+ "DOSSIER TO  SYSDBA WITH GRANT OPTION; "
				+ "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON "
				+ "MAIL_ENVOYE TO  SYSDBA WITH GRANT OPTION; "
				+ "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON "
				+ "MAIL_RECU TO  SYSDBA WITH GRANT OPTION; "
				+ "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON "
				+ "PARAM TO  SYSDBA WITH GRANT OPTION; "
				+ "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON "
				+ "REGLES TO  SYSDBA WITH GRANT OPTION; "
				+ "GRANT DELETE, INSERT, REFERENCES, SELECT, UPDATE ON "
				+ "PIECE_JOINTE TO  SYSDBA WITH GRANT OPTION;";
		version1.add(createGrant);

		version1.add("INSERT INTO PARAM (VERSION_BASE) VALUES ( 1);");

		return version1;
	}

	public ArrayList<String> getVersion2() {
		version2 = new ArrayList<String>();
		String script = "ALTER TABLE MAIL_RECU " + "ADD STATUT Char(1) "
				+ "CHARACTER SET ISO8859_1 NOT NULL COLLATE FR_FR; ";
		version2.add(script);
		script = "UPDATE MAIL_RECU SET STATUT = '1' WHERE STATUT IS NULL;";
		version2.add(script);
		version2.add("UPDATE PARAM SET VERSION_BASE=2;");
		return version2;
	}

	public ArrayList<String> getVersion3() {
		version3 = new ArrayList<String>();
		version3.add("ALTER TABLE COMPTES " + "ADD TYPE_COMPTE Varchar(7) "
				+ "CHARACTER SET ISO8859_1 NOT NULL " + "COLLATE FR_FR; ");
		version3.add("UPDATE COMPTES SET TYPE_COMPTE = 'pop' "
				+ "WHERE TYPE_COMPTE IS NULL;");
		version3.add("UPDATE PARAM SET VERSION_BASE=3;");
		return version3;
	}

	public ArrayList<String> getVersion4() {
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

	public ArrayList<String> getVersion5() {
		version5 = new ArrayList<String>();
		version5
				.add("ALTER TABLE DOSSIER ADD NOM_INTERNET Varchar(999) CHARACTER SET ISO8859_1;");
		version5.add("UPDATE PARAM SET VERSION_BASE=5;");
		return version5;
	}

}
