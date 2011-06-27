package JTree;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class creatingJTreeComponent {

	public static void main(String[] argv) throws Exception {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root Label");
		root.add(new DefaultMutableTreeNode("Node Label"));

		JTree tree = new JTree(root);

		JFrame f = new JFrame();
		f.add(new JScrollPane(tree));
		f.setSize(300, 300);
		f.setVisible(true);

	}
}
