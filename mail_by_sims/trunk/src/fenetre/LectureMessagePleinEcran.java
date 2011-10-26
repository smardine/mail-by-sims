package fenetre;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.text.Document;

import releve.imap.util.messageUtilisateur;
import bdd.BDRequette;
import fenetre.principale.EnNomComposant;
import fenetre.principale.MlActionHtmlPane;
import fenetre.principale.jList.MlActionjList;

public class LectureMessagePleinEcran extends JFrame {
	private final String TAG = this.getClass().getSimpleName();
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JDesktopPane jDesktopPaneVisualiser = null;
	private JDesktopPane jDesktopPaneBouton = null;
	private JScrollPane jScrollPane = null;
	private JTabbedPane jTabbedPane = null;
	private JPanel jPanel = null;
	private JEditorPane htmlPane = null;
	private JList jList = null;
	private JScrollPane jScrollPane1 = null;
	private DefaultListModel modelList = null;

	/**
	 * This is the default constructor
	 */
	public LectureMessagePleinEcran(JTable p_table, int p_idMessage) {
		super();
		initialize();
		modelList = new DefaultListModel();
		jList.setModel(modelList);
		jList.addMouseListener(new MlActionjList(p_table, jList));
		afficheContenuMail(p_idMessage);
		BDRequette bd = new BDRequette();
		this.setTitle(bd.getSujetFromId(p_idMessage));
		bd.closeConnexion();
	}

	private void afficheContenuMail(int p_idMessage) {
		BDRequette bd = new BDRequette();
		File contenu = bd.getContenuFromId(p_idMessage, true);

		// on RAZ le contenu du panelEditor
		Document doc = htmlPane.getDocument();
		doc.putProperty(Document.StreamDescriptionProperty, null);
		try {
			htmlPane.setPage("file:///" + contenu.getAbsolutePath());
		} catch (IOException e) {
			messageUtilisateur.affMessageException(TAG, e,
					"impossible d'afficher le mail");
		}

		// affichage des piece jointe dans la liste (si il y en a)
		List<String> lstPj = bd.getListeNomPieceJointe(p_idMessage);
		bd.closeConnexion();
		DefaultListModel model = (DefaultListModel) jList.getModel();
		int nbLigne = model.getSize();
		if (nbLigne > 0) {// si la liste est deja repli, on la vide
			model.removeAllElements();
		}
		if (lstPj.size() > 0) {
			for (String s : lstPj) {
				model.addElement(s);
			}

		}

	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 600));
		this.setPreferredSize(new Dimension(800, 600));
		this.setContentPane(getJContentPane());
		this.setTitle("Lecture message");
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJDesktopPaneBouton(),
					java.awt.BorderLayout.NORTH);
			jContentPane.add(getJDesktopPaneVisualiser(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jDesktopPaneVisualiser
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneVisualiser() {
		if (jDesktopPaneVisualiser == null) {
			jDesktopPaneVisualiser = new JDesktopPane();
			jDesktopPaneVisualiser.setLayout(new BorderLayout());
			jDesktopPaneVisualiser.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jDesktopPaneVisualiser;
	}

	/**
	 * This method initializes jDesktopPaneBouton
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneBouton() {
		if (jDesktopPaneBouton == null) {
			jDesktopPaneBouton = new JDesktopPane();
			jDesktopPaneBouton.setPreferredSize(new Dimension(
					EnNomComposant.PANEL_BOUTON.getLargeurInitiale(),
					EnNomComposant.PANEL_BOUTON.getHauteurInitiale()));
			jDesktopPaneBouton.setMinimumSize(new Dimension(
					EnNomComposant.PANEL_BOUTON.getLargeurInitiale(),
					EnNomComposant.PANEL_BOUTON.getHauteurInitiale()));
			jDesktopPaneBouton.setName(EnNomComposant.PANEL_BOUTON.getLib());
			jDesktopPaneBouton.add(getJTabbedPane(), null);
			jDesktopPaneBouton.add(getJScrollPane1(), null);
		}
		return jDesktopPaneBouton;
	}

	/**
	 * This method initializes jScrollPane
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getHtmlPane());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTabbedPane
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBounds(new Rectangle(0, 0, 5 * 50,
					EnNomComposant.PANEL_BOUTON.getHauteurInitiale()));
			jTabbedPane.addTab(null, null, getJPanel(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
		}
		return jPanel;
	}

	/**
	 * This method initializes htmlPane
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getHtmlPane() {
		if (htmlPane == null) {
			htmlPane = new JEditorPane();
			htmlPane.setComponentOrientation(ComponentOrientation.UNKNOWN);
			htmlPane.setFont(new Font("Dialog", Font.PLAIN, 12));
			htmlPane.setEditable(false);
			htmlPane.addHyperlinkListener(new MlActionHtmlPane());
		}
		return htmlPane;
	}

	/**
	 * This method initializes jList
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new JList();
		}
		return jList;
	}

	/**
	 * This method initializes jScrollPane1
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(new Rectangle(5 * 50, 0, 5 * 50,
					EnNomComposant.PANEL_BOUTON.getHauteurInitiale()));
			jScrollPane1.setViewportView(getJList());
		}
		return jScrollPane1;
	}

}
