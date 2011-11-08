/**
 * 
 */
package fenetre.principale.jtree;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;

/**
 * @author smardine
 */
public class CustomTreeExpensionListener implements TreeExpansionListener,
		TreeWillExpandListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event
	 * .TreeExpansionEvent)
	 */
	@Override
	public void treeCollapsed(TreeExpansionEvent p_event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event
	 * .TreeExpansionEvent)
	 */
	@Override
	public void treeExpanded(TreeExpansionEvent p_event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.swing.event.TreeWillExpandListener#treeWillCollapse(javax.swing
	 * .event.TreeExpansionEvent)
	 */
	@Override
	public void treeWillCollapse(TreeExpansionEvent p_event)
			throws ExpandVetoException {
		System.out.println("Vous aller replier " + p_event.getPath());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.swing.event.TreeWillExpandListener#treeWillExpand(javax.swing.event
	 * .TreeExpansionEvent)
	 */
	@Override
	public void treeWillExpand(TreeExpansionEvent p_event)
			throws ExpandVetoException {
		System.out.println("Vous aller deplier " + p_event.getPath());
	}

}
