package test;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import bdd.BDParam;

/**
 * 
 */

/**
 * @author smardine
 */
public class TestSuiteClientMail extends TestCase {

	public static Test suite() {

		TestResult result = new TestResult();
		Test testSuite = createSuite();
		testSuite.run(result);

		assertTrue(result.wasSuccessful());
		return testSuite;
	}

	/**
	 * @return
	 */
	private static Test createSuite() {
		BDParam params = new BDParam();
		File fichierDatabase = new File(params.getEmplacementBase());
		if (fichierDatabase.exists()) {
			fichierDatabase.delete();
		}
		TestSuite suite = new TestSuite();
		suite.addTestSuite(CompteMailFactoryTest.class);
		return suite;
	}
}
