package tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import releve.imap.util.messageUtilisateur;

public class RecupDate {
	private static final String TAG = "RecupDate";
	// * Choix de la langue francaise
	Locale locale = Locale.getDefault();
	private static Date actuelle = new Date();

	// * Definition du format utilise pour les dates
	private static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final DateFormat dateEtHeure = new SimpleDateFormat(
			"yyyy_MM_dd_HH_mm");
	private static final DateFormat dateEtHeureArchive = new SimpleDateFormat(
			"yyyy_MM_dd_HH_mm_ss");
	private static final DateFormat dateSeulement = new SimpleDateFormat(
			"dd-MM-yyyy");
	private static final SimpleDateFormat formatTimeStamp = new SimpleDateFormat(
			"yyyy-MM-dd, HH:mm:ss.S");
	private static final DateFormat formatTimeStampToDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.S");

	private static final DateFormat formatPourTable = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm:ss");

	// * Donne la date au format "aaaa-mm-jj"

	/**
	 * Date systeme sous le format yyyy-MM-dd HH:mm:ss
	 * @return la date formatée -String
	 */
	public static String date() {
		return dateFormat.format(actuelle);
	}

	public static String dateSeulement() {
		return dateSeulement.format(actuelle);
	}

	/**
	 * Date systeme sous le format yyyy_MM_dd_HH_mm_ss
	 * @return la date formatée -String
	 */
	public static String dateEtHeure() {
		return dateEtHeure.format(actuelle);
	}

	public static String dateEtHeureArchive() {
		return dateEtHeureArchive.format(actuelle);
	}

	public static String LongToDate(final long dateAConvertir) {
		// Date date = new Date();
		final DateFormat dataformat = DateFormat
				.getDateInstance(DateFormat.LONG);
		final String s4 = dataformat.format(dateAConvertir);
		// System.out.println(dataformat.format(dateAConvertir));
		return s4;

	}

	/**
	 * Obtenir la date au format timeStamp de firebird
	 * @param p_date - Date
	 * @return date formattée "yyyy-MM-dd, HH:mm:ss.S" ex: '2011-6-21,
	 *         10:47:29.318'
	 */
	public static String getTimeStamp(Date p_date) {
		Date aDate = p_date;
		if (aDate == null) {
			aDate = new Date();
		}
		return formatTimeStamp.format(aDate);
	}

	public static Date getdateFromTimeStamp(String timeStamp) {
		Date dateretour = null;
		try {
			dateretour = formatTimeStampToDate.parse(timeStamp);
		} catch (ParseException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"impossible de recuperer la date au bon format");
		}
		return dateretour;

	}

	public static Date getDatepourTable(Date p_date) {
		String dateretour = formatPourTable.format(p_date);
		Date date = null;
		try {
			date = formatPourTable.parse(dateretour);
		} catch (ParseException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"impossible de recuperer la date au bon format");
		}
		return date;
	}

	/**
	 * @return the formatPourTable
	 */
	public static DateFormat getFormatPourTable() {
		return formatPourTable;
	}
}