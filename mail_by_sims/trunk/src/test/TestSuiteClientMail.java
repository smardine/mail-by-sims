package test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 */

/**
 * @author smardine
 */
public class TestSuiteClientMail extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(CompteMailFactoryTest.class);
		return suite;

	}

}
