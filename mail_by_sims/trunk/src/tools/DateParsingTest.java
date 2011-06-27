package tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateParsingTest {
	public static void main(String[] a) {
		parseDate();
		parseDateSpecial();
	}

	public static void parseDate() {
		// SimpleDateFormat mf = new SimpleDateFormat("MMM-yyyy");
		// SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss.SSS");
		// SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat dtf = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss.SSS");

		// String ms = "Oct-2002";
		// String ts = "22:13:19.123";
		// String ds = "21-Oct-2002";
		String dts = "21.11.2002, 22:13:19.123";
		try {
			// System.out.println("Parsing " + ms + " with " + mf.toPattern());
			// System.out.println("   " + mf.parse(ms));
			// System.out.println("Parsing " + ts + " with " + tf.toPattern());
			// System.out.println("   " + tf.parse(ts));
			// System.out.println("Parsing " + ds + " with " + df.toPattern());
			// System.out.println("   " + df.parse(ds));
			System.out.println("Parsing " + dts + " with " + dtf.toPattern());
			System.out.println("   " + dtf.parse(dts));
			// System.out.println("Parsing " + dts + " with " + df.toPattern());
			// System.out.println("   " + df.parse(dts));
			// System.out.println("Parsing " + ds + " with " + dtf.toPattern());
			// System.out.println("   " + dtf.parse(ds));
		} catch (ParseException e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void parseDateSpecial() {
		SimpleDateFormat dtf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy");
		String str0 = "Mon Oct 21 20:54:05 EDT 2002"; // correct
		String str1 = "Mon Oct 20 20:54:05 EDT 2002"; // wrong d.o.w.
		String str2 = "Mon Oct 21 25:54:05 EDT 2002"; // wrong hour
		String str3 = "Mon Oct 21 20:54:05 PDT 2002"; // time zone
		String str4 = "Mon Oct 1 20:54:05 EDT 2002"; // missing a 'd'
		try {
			System.out.println("Parsing " + str0 + " with " + dtf.toPattern());
			System.out.println("   " + dtf.parse(str0));
			System.out.println("Parsing " + str1 + " with " + dtf.toPattern());
			System.out.println("   " + dtf.parse(str1));
			System.out.println("Parsing " + str2 + " with " + dtf.toPattern());
			System.out.println("   " + dtf.parse(str2));
			System.out.println("Parsing " + str3 + " with " + dtf.toPattern());
			System.out.println("   " + dtf.parse(str3));
			System.out.println("Parsing " + str4 + " with " + dtf.toPattern());
			System.out.println("   " + dtf.parse(str4));
		} catch (ParseException e) {
			System.out.println("Exception: " + e.toString());
		}
	}
}