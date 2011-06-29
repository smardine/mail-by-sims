package tools;

import imap.util.messageUtilisateur;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecupDate {
	// * Choix de la langue francaise
	Locale locale = Locale.getDefault();
	static Date actuelle = new Date();

	// * Definition du format utilise pour les dates
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DateFormat dateEtHeure = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
	static DateFormat dateEtHeureArchive = new SimpleDateFormat(
			"yyyy_MM_dd_HH_mm_ss");
	static DateFormat dateSeulement = new SimpleDateFormat("dd-MM-yyyy");
	static SimpleDateFormat formatTimeStamp = new SimpleDateFormat(
			"yyyy-MM-dd, HH:mm:ss.S");
	static DateFormat formatTimeStampToDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.S");

	// * Donne la date au format "aaaa-mm-jj"

	/**
	 * Date systeme sous le format yyyy-MM-dd HH:mm:ss
	 * @return la date formatée -String
	 */
	public static String date() {
		final String dat = dateFormat.format(actuelle);
		return dat;
	}

	public static String dateSeulement() {
		final String dat = dateSeulement.format(actuelle);
		return dat;
	}

	/**
	 * Date systeme sous le format yyyy_MM_dd_HH_mm_ss
	 * @return la date formatée -String
	 */
	public static String dateEtHeure() {
		final String dat = dateEtHeure.format(actuelle);
		return dat;
	}

	public static String dateEtHeureArchive() {
		final String dat = dateEtHeureArchive.format(actuelle);
		return dat;
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
		return formatTimeStamp.format(p_date);
	}

	public static Date getdateFromTimeStamp(String timeStamp) {
		Date dateretour = null;
		try {
			dateretour = formatTimeStampToDate.parse(timeStamp);
		} catch (ParseException e) {
			messageUtilisateur.affMessageException(e,
					"impossible de recuperer la date au bon format");
		}
		return dateretour;

	}

}