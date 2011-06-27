package html.html;

import html.ExitListener;
import html.JIconButton;
import html.WindowUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class Browser extends JFrame implements HyperlinkListener,
		ActionListener {
	public static void main(String[] args) {
		if (args.length == 0)
			// new Browser("http://www.apl.jhu.edu/~hall/");
			new Browser("file:///C:/contenu.html");
		else
			new Browser(args[0]);
	}

	private final JIconButton homeButton;
	private final JTextField urlField;
	private JEditorPane htmlPane;
	private final String initialURL;

	public Browser(String initialURL) {
		super("Simple Swing Browser");
		this.initialURL = initialURL;
		addWindowListener(new ExitListener());
		WindowUtilities.setNativeLookAndFeel();

		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.lightGray);
		homeButton = new JIconButton("home.gif");
		homeButton.addActionListener(this);
		JLabel urlLabel = new JLabel("URL:");
		urlField = new JTextField(30);
		urlField.setText(initialURL);
		urlField.addActionListener(this);
		topPanel.add(homeButton);
		topPanel.add(urlLabel);
		topPanel.add(urlField);
		getContentPane().add(topPanel, BorderLayout.NORTH);

		try {
			htmlPane = new JEditorPane(initialURL);
			htmlPane.setEditable(false);
			htmlPane.addHyperlinkListener(this);
			JScrollPane scrollPane = new JScrollPane(htmlPane);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
		} catch (IOException ioe) {
			warnUser("Can't build HTML pane for " + initialURL + ": " + ioe);
		}

		Dimension screenSize = getToolkit().getScreenSize();
		int width = screenSize.width * 8 / 10;
		int height = screenSize.height * 8 / 10;
		setBounds(width / 8, height / 8, width, height);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		String url;
		if (event.getSource() == urlField)
			url = urlField.getText();
		else
			// Clicked "home" button instead of entering URL
			url = initialURL;
		try {
			htmlPane.setPage(new URL(url));
			urlField.setText(url);
		} catch (IOException ioe) {
			warnUser("Can't follow link to " + url + ": " + ioe);
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				htmlPane.setPage(event.getURL());
				urlField.setText(event.getURL().toExternalForm());
			} catch (IOException ioe) {
				warnUser("Can't follow link to "
						+ event.getURL().toExternalForm() + ": " + ioe);
			}
		}
	}

	private void warnUser(String message) {
		JOptionPane.showMessageDialog(this, message, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}