package tools;

import java.util.Enumeration;

import javax.swing.UIManager;

public class SwingProperties {
	public static void main(String[] args) {
		Enumeration e = UIManager.getDefaults().keys();
		while (e.hasMoreElements()) {
			String key = e.nextElement().toString();
			if (key.matches(".*Tree.*Icon"))
				System.out.println(key + ": " + UIManager.get(key));
		}
	}
}