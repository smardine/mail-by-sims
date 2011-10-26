package fenetre.comptes.creation;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingConstants;

import fenetre.comptes.EnDefFournisseur;
import fenetre.comptes.creation.MlActionCreation.MlChoixDuCompte;

public class choixFAI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButtonGMAIL = null;
	private JButton jButtonHOTMAIL = null;
	private JComboBox jComboBoxAutre = null;
	// private EnDefFournisseur choixComptePop;
	private final JTree tree;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;

	/**
	 * This is the default constructor
	 */
	public choixFAI(JTree p_tree) {

		super();
		this.tree = p_tree;
		initialize();
		List<EnDefFournisseur> lstComptePop = EnDefFournisseur.getComptePop();

		for (int i = 0; i < lstComptePop.size(); i++) {
			// String cptName = lstComptePop.get(i).getLib();
			jComboBoxAutre.addItem(lstComptePop.get(i).getLib());
		}

		jButtonGMAIL.addActionListener(new MlChoixDuCompte(this, tree));
		jButtonGMAIL.setActionCommand(EnDefFournisseur.GMAIL.getLib());
		jButtonHOTMAIL.addActionListener(new MlChoixDuCompte(this, tree));
		jButtonHOTMAIL.setActionCommand(EnDefFournisseur.HOTMAIL.getLib());

		jComboBoxAutre.addActionListener(new MlChoixDuCompte(this, tree));

	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(387, 180);
		this.setContentPane(getJContentPane());
		this.setTitle("Choix du fournisseur");
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(208, 42, 125, 20));
			jLabel3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel3.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel3.setText("Autres");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(121, 42, 51, 20));
			jLabel2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel2.setText("Hotmail");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(36, 42, 49, 20));
			jLabel1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel1.setText("Gmail");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(14, 10, 336, 27));
			jLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel
					.setText("Merci de choisir votre fournisseur de service mail");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButtonGMAIl(), null);
			jContentPane.add(getJButtonHOTMAIL(), null);
			jContentPane.add(getJComboBoxAutre(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(jLabel3, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButtonGMAIL
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonGMAIl() {
		if (jButtonGMAIL == null) {
			jButtonGMAIL = new JButton();
			jButtonGMAIL.setIcon(new ImageIcon(getClass().getResource(
					"/logo_gmail.jpg")));
			jButtonGMAIL.setLocation(new Point(36, 65));
			jButtonGMAIL.setSize(new Dimension(50, 50));
			jButtonGMAIL.setToolTipText("Choisir Gmail");
		}
		return jButtonGMAIL;
	}

	/**
	 * This method initializes jButtonHOTMAIL
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonHOTMAIL() {
		if (jButtonHOTMAIL == null) {
			jButtonHOTMAIL = new JButton();
			jButtonHOTMAIL.setIcon(new ImageIcon(getClass().getResource(
					"/logo_hotmail.jpg")));
			jButtonHOTMAIL.setHorizontalTextPosition(SwingConstants.CENTER);
			jButtonHOTMAIL.setLocation(new Point(122, 65));
			jButtonHOTMAIL.setSize(new Dimension(50, 50));
			jButtonHOTMAIL.setToolTipText("Choisir Hotmail");
		}
		return jButtonHOTMAIL;
	}

	/**
	 * This method initializes jComboBoxAutre
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxAutre() {
		if (jComboBoxAutre == null) {
			jComboBoxAutre = new JComboBox();
			jComboBoxAutre.setBounds(new Rectangle(208, 78, 125, 25));

			jComboBoxAutre
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							String selectedItem = (String) jComboBoxAutre
									.getSelectedItem();
							jComboBoxAutre.setActionCommand(selectedItem);
							// EnDefFournisseur enumChoisi = EnDefFournisseur
							// .getEnumFromLib(selectedItem);
							// choixComptePop = enumChoisi;
						}
					});
			jComboBoxAutre.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					String selectedItem = (String) e.getItem();
					jComboBoxAutre.setActionCommand(selectedItem);
					// EnDefFournisseur enumChoisi = EnDefFournisseur
					// .getEnumFromLib(selectedItem);
					// choixComptePop = enumChoisi;
				}
			});
		}
		return jComboBoxAutre;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
