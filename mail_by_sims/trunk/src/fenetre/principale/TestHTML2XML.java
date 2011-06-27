package fenetre.principale;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.swing.JFrame;

import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

public class TestHTML2XML {
	private final String url;
	private final String outFileName;
	private final String errOutFileName;

	public TestHTML2XML(String url, String outFileName, String errOutFileName) {
		this.url = url;
		this.outFileName = outFileName;
		this.errOutFileName = errOutFileName;
	}

	public void convert() {
		URL u;
		BufferedInputStream in;
		FileOutputStream out;

		Tidy tidy = new Tidy();

		// Tell Tidy to convert HTML to XML
		tidy.setXmlOut(true);

		try {
			// Set file for error messages
			tidy
					.setErrout(new PrintWriter(new FileWriter(errOutFileName),
							true));
			u = new URL(url);

			// Create input and output streams
			in = new BufferedInputStream(u.openStream());
			out = new FileOutputStream(outFileName);

			// Convert files
			tidy.parse(in, out);

			// Clean up
			in.close();
			out.close();

		} catch (IOException e) {
			System.out.println(this.toString() + e.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		 * Parameters are: URL of HTML file Filename of output file Filename of
		 * error file
		 */
		String url = "file:///c:/contenu2.html";
		String outputFilename = "c:/contenu.xml";
		String errorfilename = "c:/error.log";

		TestHTML2XML t = new TestHTML2XML(url, outputFilename, errorfilename);
		t.convert();

		XHTMLPanel panel = new XHTMLPanel();

		// Set the XHTML document to render. We use the simplest form
		// of the API call, which uses a File reference. There
		// are a variety of overloads for setDocument().
		panel.setDocument(new File("c:/contenu.xml"));
		// String contenu = ReadFile.getContenuCaractere("C:/contenu.html");
		// panel.setDocumentFromString(contenu, null, null);
		// Put our panel in a scrolling pane. You can use
		// a regular JScrollPane here, or our FSScrollPane.
		// FSScrollPane is already set up to move the correct
		// amount when scrolling 1 line or 1 page
		FSScrollPane scroll = new FSScrollPane(panel);
		JFrame frame = new JFrame("Flying Saucer Single Page Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scroll);
		frame.pack();
		frame.setSize(1024, 768);
		frame.setVisible(true);
	}
}
