package imap;

import javax.swing.JProgressBar;

public class ReleveAutreImap {

	private final String user;
	private final String password;
	private final String host;
	private final String idCompte;
	private final JProgressBar progressBar;

	public ReleveAutreImap(String p_idCompte, String p_user, String p_password,
			String p_host, JProgressBar progress) {

		this.user = p_user;
		this.password = p_password;
		this.host = p_host;
		this.idCompte = p_idCompte;
		this.progressBar = progress;
		this.main(idCompte, progressBar);

	}

	private void main(String p_idCompte, JProgressBar p_progressBar) {

	}

}
