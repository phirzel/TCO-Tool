package ch.softenvironment.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import junit.framework.TestCase;

/**
 * Test Tracer.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class TracerTestCase extends TestCase {

	public void testLogFile() {
		try {
			FileOutputStream stream = new FileOutputStream(System.getProperty("java.io.tmpdir") + "TracerTestCase.log", false);
			// PrintWriter printer = new PrintWriter(new
			// OutputStreamWriter(stream, "UTF-8");

			Tracer.start(new PrintStream(stream), Tracer.ALL);
			Tracer.getInstance().developerError("do you see a developerError entry?");
			Tracer.getInstance().stop();
		} catch (FileNotFoundException e) {
			fail("testLogFile: " + e.getLocalizedMessage());
		}
	}
}
