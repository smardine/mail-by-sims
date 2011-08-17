package fenetre.principale;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.TreePath;

import mdl.MlListeMessage;
import Verif.Thread_Verif;
import fenetre.EnTitreFenetre;
import fenetre.comptes.EnDossierBase;
import fenetre.principale.MlAction.EnActionMain;
import fenetre.principale.MlAction.MlActionMain;
import fenetre.principale.jTable.DateTimeCellRenderer;
import fenetre.principale.jTable.MlActionJtable;
import fenetre.principale.jTable.MlActionPopupJTable;
import fenetre.principale.jTable.MyTableModel;
import fenetre.principale.jTable.XTableColumnModel;
import fenetre.principale.jtree.ArborescenceBoiteMail;
import fenetre.principale.jtree.MlActionJtree;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private static TreePath treePath;
	private static String nomCompte;
	private JPanel panelPrincipal = null;
	private JDesktopPane panelBouton = null;
	private JDesktopPane panelTree = null;
	private JDesktopPane panelTableEtListe = null;
	private JDesktopPane panelHTML = null;
	private JButton btNewMessage = null;
	private JButton btSupprMessage = null;
	private JButton btEnvoyerRecevoir = null;
	private JTree jTree = null;
	private JEditorPane htmlPane = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JList jList = null;
	private JMenuBar jJMenuBar;
	private JMenu jMenuFichier;
	private JMenu jMenuAide; // @jve:decl-index=0:
	private JMenuItem jMenuCompte;
	private JMenuItem jMenuQuitter;
	private JMenuItem jMenuExplorer;
	private JMenuItem jMenuHistorique;
	private JMenuItem jMenuContact;
	private static XTableColumnModel ColoneModel;
	private final MyTableModel tableModel;
	private JMenu jMenuImportExport;
	private JMenuItem jMenuItemImporter; // @jve:decl-index=0:
	private JScrollPane jScrollPane1 = null;
	private JMenu jMenuReleve = null;
	private JMenu jMenuEnvoiReception = null;
	private JMenuItem jMenuItemReleve = null;
	private DefaultListModel modelList = null;
	private JButton btRecevoir = null;
	private JButton btEnvoyer = null;
	private JScrollPane jScrollPane2 = null;
	private JTabbedPane jTabbedPane = null;
	private JPanel jPanel = null;
	private JProgressBar jProgressBarReleve = null;
	private JProgressBar jProgressBarPieceJointe = null;
	private JTextArea jTextArea = null;
	private JScrollPane jScrollPane3 = null;

	/**
	 * This method initializes panelBouton
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneHaut() {
		if (panelBouton == null) {
			panelBouton = new JDesktopPane();

			panelBouton
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			panelBouton.setPreferredSize(new Dimension(
					EnNomComposant.PANEL_BOUTON.getLargeurInitiale(),
					EnNomComposant.PANEL_BOUTON.getHauteurInitiale()));
			panelBouton.setMinimumSize(new Dimension(
					EnNomComposant.PANEL_BOUTON.getLargeurInitiale(),
					EnNomComposant.PANEL_BOUTON.getHauteurInitiale()));
			panelBouton.setName(EnNomComposant.PANEL_BOUTON.getLib());
			panelBouton.add(getJTabbedPane(), BorderLayout.CENTER);
			panelBouton.add(getJProgressBar(), null);
			panelBouton.add(getJProgressBarPieceJointe(), null);
			panelBouton.add(getJScrollPane3(), null);
		}
		return panelBouton;
	}

	/**
	 * This method initializes panelTree
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneGauche() {
		if (panelTree == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(1);
			borderLayout.setVgap(1);
			panelTree = new JDesktopPane();
			panelTree.setName(EnNomComposant.PANEL_TREE.getLib());
			panelTree.setLayout(borderLayout);
			panelTree.setPreferredSize(new Dimension(EnNomComposant.PANEL_TREE
					.getLargeurInitiale(), EnNomComposant.PANEL_TREE
					.getHauteurInitiale()));
			panelTree.setMinimumSize(new Dimension(EnNomComposant.PANEL_TREE
					.getLargeurInitiale(), EnNomComposant.PANEL_TREE
					.getHauteurInitiale()));
			panelTree.add(getJScrollPane2(), BorderLayout.CENTER);
		}
		return panelTree;
	}

	/**
	 * This method initializes panelTableEtListe
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneDroite() {
		if (panelTableEtListe == null) {
			panelTableEtListe = new JDesktopPane();
			panelTableEtListe.setPreferredSize(new Dimension(
					EnNomComposant.PANEL_TABLE_ET_LISTE.getLargeurInitiale(),
					EnNomComposant.PANEL_TABLE_ET_LISTE.getHauteurInitiale()));
			panelTableEtListe.setMinimumSize(new Dimension(
					EnNomComposant.PANEL_TABLE_ET_LISTE.getLargeurInitiale(),
					EnNomComposant.PANEL_TABLE_ET_LISTE.getHauteurInitiale()));
			panelTableEtListe.setLayout(new BorderLayout());
			panelTableEtListe.setName(EnNomComposant.PANEL_TABLE_ET_LISTE
					.getLib());
			panelTableEtListe.add(getJScrollPane(), BorderLayout.CENTER);
			panelTableEtListe.add(getJList(), BorderLayout.SOUTH);
		}
		return panelTableEtListe;
	}

	/**
	 * This method initializes panelHTML
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPaneBas() {
		if (panelHTML == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(1);
			borderLayout.setVgap(1);
			panelHTML = new JDesktopPane();
			panelHTML.setPreferredSize(new Dimension(EnNomComposant.PANEL_HTML
					.getLargeurInitiale(), EnNomComposant.PANEL_HTML
					.getHauteurInitiale()));
			panelHTML.setMinimumSize(new Dimension(EnNomComposant.PANEL_HTML
					.getLargeurInitiale(), EnNomComposant.PANEL_HTML
					.getHauteurInitiale()));
			panelHTML.setName(EnNomComposant.PANEL_HTML.getLib());
			panelHTML.setLayout(borderLayout);
			panelHTML.add(getJScrollPane1(), BorderLayout.CENTER);
		}
		return panelHTML;
	}

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonNewMessage() {
		if (btNewMessage == null) {
			btNewMessage = new JButton();
			btNewMessage.setText("");
			btNewMessage.setBounds(new Rectangle(150, 0, 50, 50));
			btNewMessage.setIcon(new ImageIcon(getClass().getResource(
					"/nouveau_message.png")));
		}
		return btNewMessage;
	}

	/**
	 * This method initializes btSupprMessage
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSupprMessage() {
		if (btSupprMessage == null) {
			btSupprMessage = new JButton();
			btSupprMessage.setText("");
			btSupprMessage.setActionCommand(EnActionMain.SUPPRIMER.getLib());
			btSupprMessage.setBounds(new Rectangle(200, 0, 50, 50));
			btSupprMessage.setIcon(new ImageIcon(getClass().getResource(
					"/supprimer.png")));
		}
		return btSupprMessage;
	}

	/**
	 * This method initializes btEnvoyerRecevoir
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonEnvoyer_Recevoir() {
		if (btEnvoyerRecevoir == null) {
			btEnvoyerRecevoir = new JButton();
			btEnvoyerRecevoir.setText("");
			btEnvoyerRecevoir.setIcon(new ImageIcon(getClass().getResource(
					"/envoyer_recevoir.png")));
			btEnvoyerRecevoir.setBounds(new Rectangle(0, 0, 50, 50));
			btEnvoyerRecevoir.setActionCommand(EnActionMain.ENVOYER_RECEVOIR
					.getLib());
		}
		return btEnvoyerRecevoir;
	}

	/**
	 * This method initializes jTree
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			// new BDAcces();
			ArborescenceBoiteMail arbo = new ArborescenceBoiteMail();
			jTree = new JTree(arbo);
			jTree.setShowsRootHandles(true);
			jTree.setRootVisible(false);
			jTree.setToggleClickCount(1);
			jTree.setFont(new Font("Perpetua", Font.BOLD, 12));
			jTree.setExpandsSelectedPaths(true);
		}
		return jTree;
	}

	/**
	 * This method initializes jEditorPane
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getJEditorPane() {
		htmlPane = new JEditorPane();
		htmlPane.setComponentOrientation(ComponentOrientation.UNKNOWN);
		htmlPane.setFont(new Font("Dialog", Font.PLAIN, 12));
		// htmlPane.setContentType("text/html");
		// htmlPane.setContentType("text/html");
		htmlPane.setEditable(false);
		htmlPane.addHyperlinkListener(new MlActionHtmlPane());

		return htmlPane;
	}

	/**
	 * This method initializes jScrollPane
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable
	 * @return javax.swing.JTable
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
			jTable.setAutoCreateColumnsFromModel(false);
			jTable.setColumnSelectionAllowed(true);
			jTable
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			jTable.setShowHorizontalLines(false);

			jTable.setShowVerticalLines(false);
			jTable.setAutoCreateRowSorter(true);
		}
		return jTable;
	}

	/**
	 * This method initializes jList
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new JList();
			jList.setBackground(Color.LIGHT_GRAY);
			jList.setPreferredSize(new Dimension(50, 50));
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
			jScrollPane1.setViewportView(getJEditorPane());
			jScrollPane1.setViewportBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jMenuReleve
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuReleve() {
		if (jMenuReleve == null) {
			jMenuReleve = new JMenu();
			jMenuReleve.setActionCommand("dfsdffdsf");
			jMenuReleve.setText("Outils");
			jMenuReleve.add(getJMenuEnvoiReception());
		}
		return jMenuReleve;
	}

	/**
	 * This method initializes jMenuEnvoiReception
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuEnvoiReception() {
		if (jMenuEnvoiReception == null) {
			jMenuEnvoiReception = new JMenu();
			jMenuEnvoiReception.setText("Envoyer / Recevoir");
			jMenuEnvoiReception.add(getJMenuItemReleve());
		}
		return jMenuEnvoiReception;
	}

	/**
	 * This method initializes jMenuItemReleve
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemReleve() {
		if (jMenuItemReleve == null) {
			jMenuItemReleve = new JMenuItem();
			jMenuItemReleve.setText(EnActionMain.ENVOYER_RECEVOIR.getLib());
			jMenuItemReleve.setActionCommand(EnActionMain.ENVOYER_RECEVOIR
					.getLib());
		}
		return jMenuItemReleve;
	}

	/**
	 * This method initializes btRecevoir
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonRecevoir() {
		if (btRecevoir == null) {
			btRecevoir = new JButton();
			btRecevoir.setIcon(new ImageIcon(getClass().getResource(
					"/recevoir.png")));
			btRecevoir.setBounds(new Rectangle(50, 0, 50, 50));
			btRecevoir.setActionCommand(EnActionMain.RECEVOIR.getLib());

		}
		return btRecevoir;
	}

	/**
	 * This method initializes btEnvoyer
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonEnvoyer() {
		if (btEnvoyer == null) {
			btEnvoyer = new JButton();
			btEnvoyer.setIcon(new ImageIcon(getClass().getResource(
					"/envoyer.png")));
			btEnvoyer.setBounds(new Rectangle(100, 0, 50, 50));
			btEnvoyer.setActionCommand(EnActionMain.ENVOYER.getLib());
		}
		return btEnvoyer;
	}

	/**
	 * This method initializes jScrollPane2
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getJTree());
		}
		return jScrollPane2;
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
			jTabbedPane
					.addTab("Gestion Messages", null, getJPanel(),
							"Acces aux fonction d'envoi, reception, synchronisation...");
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
			jPanel.setLayout(null);
			jPanel.add(getJButtonEnvoyer_Recevoir(), null);
			jPanel.add(getJButtonSupprMessage(), null);
			jPanel.add(getJButtonRecevoir(), null);
			jPanel.add(getJButtonEnvoyer(), null);
			jPanel.add(getJButtonNewMessage(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jProgressBarReleve
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBarReleve == null) {
			jProgressBarReleve = new JProgressBar();
			jProgressBarReleve.setBounds(new Rectangle(255, 8, 238, 23));
			jProgressBarReleve.setStringPainted(true);
			jProgressBarReleve.setBackground(new Color(238, 238, 238));
			jProgressBarReleve.setForeground(Color.blue);
			jProgressBarReleve.setVisible(false);
		}
		return jProgressBarReleve;
	}

	/**
	 * This method initializes jProgressBarPieceJointe
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBarPieceJointe() {
		if (jProgressBarPieceJointe == null) {
			jProgressBarPieceJointe = new JProgressBar();
			jProgressBarPieceJointe.setBounds(new Rectangle(255, 41, 239, 24));
			jProgressBarPieceJointe.setStringPainted(true);

			jProgressBarPieceJointe.setVisible(false);
			jProgressBarPieceJointe.setBackground(new Color(238, 238, 238));
			jProgressBarPieceJointe.setForeground(Color.blue);
		}
		return jProgressBarPieceJointe;
	}

	/**
	 * This method initializes jTextArea
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setWrapStyleWord(true);
			jTextArea.setLineWrap(true);
			jTextArea.setVisible(false);

		}
		return jTextArea;
	}

	/**
	 * This method initializes jScrollPane3
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setBounds(new Rectangle(496, 2, 283, 70));
			jScrollPane3.setVisible(false);
			jScrollPane3.setViewportView(getJTextArea());
		}
		return jScrollPane3;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main thisClass = new Main();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public Main() {
		super();
		// new BDAcces();
		initialize();
		this.addComponentListener(new MlComposantListener(panelPrincipal));
		Thread_Verif verif = new Thread_Verif(jTree);
		verif.start();
		modelList = new DefaultListModel();
		jList.setModel(modelList);
		ColoneModel = new XTableColumnModel();
		jTable.setColumnModel(ColoneModel);
		jTable.setDefaultRenderer(Date.class, new DateTimeCellRenderer());
		tableModel = new MyTableModel(new MlListeMessage(), ColoneModel);
		jTable.setModel(tableModel);
		jTable.addMouseListener(new MlActionJtable(jTable, htmlPane, jList,
				jProgressBarReleve, jTextArea, jScrollPane3));

		jMenuContact.addActionListener(new MlActionMain());
		jTree.addMouseListener(new MlActionJtree(jTree, jTable));
		jTree.addTreeSelectionListener(new MlActionJtree(jTree, jTable));
		jTree.addTreeExpansionListener(new MlActionJtree(jTree, jTable));
		jMenuItemImporter.addActionListener(new MlActionMain(jTree,
				jProgressBarReleve, jProgressBarPieceJointe, jTextArea,
				jScrollPane3));
		jMenuItemReleve.addActionListener(new MlActionMain(jTree,
				jProgressBarReleve, jProgressBarPieceJointe, jTextArea,
				jScrollPane3));
		btEnvoyerRecevoir.addActionListener(new MlActionMain(jTree,
				jProgressBarReleve, jProgressBarPieceJointe, jTextArea,
				jScrollPane3));
		btRecevoir.addActionListener(new MlActionMain(jTree,
				jProgressBarReleve, jProgressBarPieceJointe, jTextArea,
				jScrollPane3));
		btEnvoyer.addActionListener(new MlActionMain(jTree, jProgressBarReleve,
				jProgressBarPieceJointe, jTextArea, jScrollPane3));
		btSupprMessage.addActionListener(new MlActionPopupJTable(jTable, jList,
				jProgressBarReleve, jTextArea, jScrollPane3));
		jMenuCompte.addActionListener(new MlActionMain(this, jTree));
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		// Dimension screenSize = getToolkit().getScreenSize();
		// int width = screenSize.width * 8 / 10;
		// int height = screenSize.height * 8 / 10;
		// setBounds(width / 8, height / 8, width, height);
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 600));
		this.setPreferredSize(new Dimension(800, 600));

		this.setContentPane(getJContentPane());
		this.setJMenuBar(getJJMenuBar());
		this.setTitle(EnTitreFenetre.PRINCIPALE.getLib());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		this.setLocationRelativeTo(null);// on centre la fenetre

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/logo_appli.png")));

	}

	/**
	 * This method initializes panelPrincipal
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (panelPrincipal == null) {
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(3);
			borderLayout1.setVgap(3);
			panelPrincipal = new JPanel();
			panelPrincipal.setLayout(borderLayout1);
			panelPrincipal.add(getJDesktopPaneHaut(), BorderLayout.NORTH);
			panelPrincipal.add(getJDesktopPaneGauche(), BorderLayout.WEST);
			panelPrincipal.add(getJDesktopPaneDroite(), BorderLayout.CENTER);
			panelPrincipal.add(getJDesktopPaneBas(), BorderLayout.SOUTH);
		}
		return panelPrincipal;
	}

	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.setPreferredSize(new Dimension(0, 25));
			jJMenuBar.add(getJMenuFichier());
			jJMenuBar.add(getJMenuAide());
			jJMenuBar.add(getJMenuReleve());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenuFichier
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuFichier() {

		if (jMenuFichier == null) {
			jMenuFichier = new JMenu();
			jMenuFichier.setText("Fichier");
			jMenuFichier.add(getJMenuCompte());
			jMenuFichier.add(getJMenuImportExport());
			jMenuFichier.add(getJMenuQuitter());
		}
		return jMenuFichier;
	}

	/**
	 * This method initializes jMenuImportExport
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuImportExport() {
		if (jMenuImportExport == null) {
			jMenuImportExport = new JMenu();
			jMenuImportExport.add(getJMenuItemImporter());
			jMenuImportExport.setText(EnActionMain.IMPORT_EXPORT.getLib());
		}
		return jMenuImportExport;
	}

	/**
	 * This method initializes jMenuItemImporter
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemImporter() {
		if (jMenuItemImporter == null) {
			jMenuItemImporter = new JMenuItem();
			jMenuItemImporter.setText(EnActionMain.IMPORTER.getLib());
			jMenuItemImporter.setActionCommand(EnActionMain.IMPORTER.getLib());

		}
		return jMenuItemImporter;
	}

	/**
	 * This method initializes jMenuAide
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuAide() {
		if (jMenuAide == null) {
			jMenuAide = new JMenu();
			jMenuAide.setText("Aide");
			jMenuAide.add(getJMenuExplorer());
			jMenuAide.add(getJMenuHistorique());
			jMenuAide.add(getJMenuContact());
		}
		return jMenuAide;
	}

	/**
	 * This method initializes jMenuCompte
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuCompte() {
		if (jMenuCompte == null) {
			jMenuCompte = new JMenuItem();
			jMenuCompte.setText(EnActionMain.GESTION_COMPTE.getLib());
			jMenuCompte.setActionCommand(EnActionMain.GESTION_COMPTE.getLib());

		}
		return jMenuCompte;
	}

	/**
	 * This method initializes jMenuQuitter
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuQuitter() {
		if (jMenuQuitter == null) {
			jMenuQuitter = new JMenuItem();
			jMenuQuitter.setText(EnActionMain.QUITTER.getLib());
			jMenuQuitter.setActionCommand(EnActionMain.QUITTER.getLib());
			jMenuQuitter.addActionListener(new MlActionMain());
		}
		return jMenuQuitter;
	}

	/**
	 * This method initializes jMenuExplorer
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuExplorer() {
		if (jMenuExplorer == null) {
			jMenuExplorer = new JMenuItem();
			jMenuExplorer.setText(EnActionMain.EXPLORER.getLib());
			jMenuExplorer.setActionCommand(EnActionMain.EXPLORER.getLib());
			jMenuExplorer.addActionListener(new MlActionMain());
		}
		return jMenuExplorer;
	}

	/**
	 * This method initializes jMenuHistorique
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuHistorique() {
		if (jMenuHistorique == null) {
			jMenuHistorique = new JMenuItem();
			jMenuHistorique.setText(EnActionMain.HISTORIQUE.getLib());
			jMenuHistorique.setActionCommand(EnActionMain.HISTORIQUE.getLib());
			jMenuHistorique.addActionListener(new MlActionMain());
		}
		return jMenuHistorique;
	}

	/**
	 * This method initializes jMenuContact
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenuContact() {
		if (jMenuContact == null) {
			jMenuContact = new JMenuItem();
			jMenuContact.setText(EnActionMain.CONTACT.getLib());
			jMenuContact.setActionCommand(EnActionMain.CONTACT.getLib());

		}
		return jMenuContact;
	}

	/**
	 * @param treePath the treePath to set
	 */
	public static void setTreePath(TreePath treePath) {
		Main.treePath = treePath;
	}

	public static void setNomCompte(String p_compte) {
		Main.nomCompte = p_compte;
	}

	public static String getNomCompte() {
		return Main.nomCompte;
	}

	/**
	 * @return the treePath
	 */
	public static TreePath getTreePath() {
		if (treePath == null) {
			return new TreePath(EnDossierBase.ROOT.getLib());
		}
		return treePath;
	}

	public static XTableColumnModel getColumnModel() {
		return ColoneModel;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
