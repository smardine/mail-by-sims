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
		defFournisseur = EnDefFournisseur.ALICE;

		cptMail.setNomCompte(defFournisseur.getLib());
		cptMail.setServeurReception(defFournisseur.getServeurPop());
		cptMail.setPortPop(defFournisseur.getPortPop());
		cptMail.setServeurSMTP(defFournisseur.getServeurSMTP());
		cptMail.setPortSMTP(defFournisseur.getPortSMTP());
		cptMail.setUserName("toto");
		cptMail.setPassword("titi");
		cptMail.setTypeCompte(defFournisseur.getTypeCompte());
		result = false;
		exceptionLevee = false;
	}

	/**
	 * Test method for
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
	 * Test method for
	 * {@link factory.cptMailFactory#suppressioncptMail(mdl.MlcptMail)} .
	 */
	public final void testSuppressionCompteMailEchouee() {
		try {
			result = cptFact.suppressionCompteMail(cptMail);
		} catch (Exception e) {
			exceptionLevee = true;
		}
		assertFalse(result);
		assertTrue(exceptionLevee);

	}

	public final void testSuppressionCompteMailReussie() {
		try {
			cptFact.creationCompteMail(cptMail);
			result = cptFact.suppressionCompteMail(cptMail);
		} catch (Exception e) {
			exceptionLevee = true;
		}
		assertTrue(result);
		assertFalse(exceptionLevee);
	}

}
