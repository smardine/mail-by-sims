/**
 * 
 */
package test;

import junit.framework.TestCase;
import mdl.MlCompteMail;
import factory.CompteMailFactory;
import fenetre.comptes.EnDefFournisseur;

/**
 * @author smardine
 */
public class CompteMailFactoryTest extends TestCase {
	private CompteMailFactory cptFact;
	private MlCompteMail cptMail;
	private EnDefFournisseur defFournisseur;
	private boolean result;
	private boolean exceptionLevee;

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cptFact = new CompteMailFactory();
		cptMail = new MlCompteMail("");
		defFournisseur = EnDefFournisseur.LA_POSTE;

		cptMail.setNomCompte(defFournisseur.getLib());
		cptMail.setServeurReception(defFournisseur.getServeurPop());
		cptMail.setPortPop(defFournisseur.getPortPop());
		cptMail.setServeurSMTP(defFournisseur.getServeurSMTP());
		cptMail.setPortSMTP(defFournisseur.getPortSMTP());
		cptMail.setUserName("mbs.test@laposte.net");
		cptMail.setPassword("Azerty123");
		cptMail.setTypeCompte(defFournisseur.getTypeCompte());
		result = false;
		exceptionLevee = false;
	}

	/**
	 * On test la creation d'un compte mail en base
	 * {@link factory.cptMailFactory#creationcptMail(mdl.MlcptMail)}.
	 */
	public final void testCreationCompteMail() {

		try {
			result = cptFact.creationCompteMail(cptMail);
		} catch (Exception e) {
			exceptionLevee = true;
		}
		assertTrue(cptMail.getIdCompte() != 0);
		assertTrue(result);
		assertFalse(exceptionLevee);
	}

	/**
	 * Test d'une connexion a la bal
	 * {@link factory.cptMailFactory#testBal(mdl.MlcptMail)}
	 */
	public final void testTestBalOk() {
		try {
			cptFact.creationCompteMail(cptMail);
			result = cptFact.testBal(cptMail);
			cptFact.suppressionCompteMail(cptMail, null, null);
		} catch (Exception e) {
			exceptionLevee = true;
		}
		assertTrue(result);
		assertFalse(exceptionLevee);
	}

	/**
	 * Test d'une connexion a la bal qui echoue
	 * {@link factory.cptMailFactory#testBal(mdl.MlcptMail)}
	 */
	public final void testTestBalNok() {
		try {
			cptMail.setPassword("123");
			cptFact.creationCompteMail(cptMail);
			result = cptFact.testBal(cptMail);
			cptFact.suppressionCompteMail(cptMail, null, null);
		} catch (Exception e) {
			exceptionLevee = true;
		}
		assertFalse(result);
		assertFalse(exceptionLevee);
	}

	/**
	 * Test d'une suppresion de compte mail qui echoue
	 * {@link factory.cptMailFactory#suppressioncptMail(mdl.MlcptMail)} .
	 */
	public final void testSuppressionCompteMailEchouee() {
		try {
			result = cptFact.suppressionCompteMail(cptMail, null, null);
		} catch (Exception e) {
			exceptionLevee = true;
		}
		assertFalse(result);
		assertTrue(exceptionLevee);

	}

	/**
	 * Test d'une suppresion de compte mail qui reussi
	 * {@link factory.cptMailFactory#suppressioncptMail(mdl.MlcptMail)}
	 */
	public final void testSuppressionCompteMailReussie() {
		try {
			cptFact.creationCompteMail(cptMail);
			result = cptFact.suppressionCompteMail(cptMail, null, null);
		} catch (Exception e) {
			exceptionLevee = true;
		}
		assertTrue(result);
		assertFalse(exceptionLevee);
	}

}
