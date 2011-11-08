package fenetre.principale.jtree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import mdl.ComposantVisuelCommun;
import mdl.MlCompteMail;
import mdl.MlDossier;
import mdl.MlListeMessage;
import tools.GestionRepertoire;
import factory.JTableFactory;

public class MlActionJtree implements MouseListener, TreeSelectionListener,
		TreeExpansionListener {

	private final JTree tree;
	// private final JTable table;
	private JPopupMenu popUpMenu;
	private JMenuItem Ajouter;
	private JMenuItem Supprimer;

	public MlActionJtree(JTree p_jTree, JTable p_jTable) {
		this.tree = p_jTree;
		// this.table = p_jTable;
		this.popUpMenu = getJPopupMenu();
	}

	/**
	 * This method initializes jPopupMenu
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJPopupMenu() {
		if (popUpMenu == null) {
			popUpMenu = new JPopupMenu();
			popUpMenu.add(getAjouter());
			popUpMenu.add(getSupprimer());

		}
		return popUpMenu;
	}

	/**
	 * This method initializes Ajouter
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAjouter() {
		if (Ajouter == null) {
			Ajouter = new JMenuItem();
			Ajouter.setText("Ajouter un dossier");
			Ajouter.setActionCommand("AJOUTER");
			Ajouter.addActionListener(new MlActionPopupJTree(tree));

		}
		return Ajouter;
	}

	/**
	 * This method initializes Supprimer
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSupprimer() {
		if (Supprimer == null) {
			Supprimer = new JMenuItem();
			Supprimer.setText("Supprimer ce dossier");
			Supprimer.setActionCommand("SUPPRIMER");
			Supprimer.addActionListener(new MlActionPopupJTree(tree));
		}
		return Supprimer;
	}

	@Override
	public void valueChanged(TreeSelectionEvent p_event) {
		// TreePath newPath = p_event.getNewLeadSelectionPath();
		// tree.setSelectionPath(newPath);
		// ComposantVisuelCommun.setTree(tree);

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent p_event) {
		// JTreeFactory treeFact = new JTreeFactory();
		// treeFact.refreshJTree();
		tree.setSelectionPath(p_event.getPath());
		ComposantVisuelCommun.setTree(tree);

	}

	@Override
	public void treeExpanded(TreeExpansionEvent p_event) {
		// tree = (JTree) p_event.getSource();

		TreePath newPath = p_event.getPath();
		if (null != newPath) {
			tree.setSelectionPath(newPath);
			// ComposantVisuelCommun.setTree(tree);
		}
		ComposantVisuelCommun.setTree((JTree) p_event.getSource());
		// JTreeFactory treeFact = new JTreeFactory();
		// treeFact.refreshJTree();

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		TreePath pathFromEvent = getPathFromEvent(e);
		if (e.getClickCount() == 1) {
			if (null != pathFromEvent)// on verifie si on a cliqué sur le nom
			// d'un compte
			{
				DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) pathFromEvent
						.getLastPathComponent();
				Object userObject = aNode.getUserObject();
				if (userObject instanceof MlDossier) {
					MlDossier dossier = (MlDossier) userObject;
					MlListeMessage lstMess = dossier.getListMessage();
					JTableFactory tableFact = new JTableFactory();
					tableFact.refreshJTable(lstMess);
					// MyTableModel modelDetable = (MyTableModel)
					// table.getModel();
					// modelDetable.valorisetable(lstMess);
					((DefaultListModel) ComposantVisuelCommun.getJListPJ()
							.getModel()).removeAllElements();
					Document doc = ComposantVisuelCommun.getHtmlPane()
							.getDocument();
					doc.putProperty(Document.StreamDescriptionProperty, null);
					try {
						ComposantVisuelCommun.getHtmlPane().setPage(
								"file:///"
										+ GestionRepertoire.RecupRepTemplate()
										+ "/vide.html");
						return;
					} catch (IOException e1) {
						return;
					}
				} else if (userObject instanceof MlCompteMail) {
					JTableFactory tableFact = new JTableFactory();
					tableFact.refreshJTable(new MlListeMessage());
					((DefaultListModel) ComposantVisuelCommun.getJListPJ()
							.getModel()).removeAllElements();
					Document doc = ComposantVisuelCommun.getHtmlPane()
							.getDocument();
					doc.putProperty(Document.StreamDescriptionProperty, null);
					try {
						ComposantVisuelCommun.getHtmlPane().setPage(
								"file:///"
										+ GestionRepertoire.RecupRepTemplate()
										+ "/vide.html");
						return;
					} catch (IOException e1) {
						return;
					}

				}
			}
		}

		int selRow = tree.getRowForLocation(e.getX(), e.getY());
		TreePath selPath = pathFromEvent;

		tree.setSelectionPath(selPath);
		tree.setSelectionRow(selRow);

		if (e.isPopupTrigger()) {
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) selPath
					.getLastPathComponent();
			Object userObject = aNode.getUserObject();
			if (userObject instanceof MlCompteMail) {
				Supprimer.setEnabled(false);
			} else if (userObject instanceof MlDossier) {
				MlDossier dossier = (MlDossier) userObject;
				if (dossier.getIdDossierParent() == 0) {
					Supprimer.setEnabled(false);
				} else {
					Supprimer.setEnabled(true);
				}
			}
			popUpMenu.show(e.getComponent(), e.getX(), e.getY());
		}

		// if (e.getClickCount() == 1) {
		// System.out.println("mySingleClick(selRow, selPath)");
		// } else if (e.getClickCount() == 2) {
		// System.out.println("myDoubleClick(selRow, selPath)");
		// }

	}

	/**
	 * @param e
	 * @return
	 */
	private TreePath getPathFromEvent(MouseEvent e) {
		return tree.getPathForLocation(e.getX(), e.getY());
	}

	// private List<String> getListeDossierdeBase() {
	// ArrayList<String> lstDossierBase = new ArrayList<String>(4);
	// EnDossierBase[] lstEnum = EnDossierBase.values();
	// for (EnDossierBase dossier : lstEnum) {
	// if (dossier != EnDossierBase.ROOT) {
	// lstDossierBase.add(dossier.getLib());
	// }
	//
	// }
	// return lstDossierBase;
	// }

}
