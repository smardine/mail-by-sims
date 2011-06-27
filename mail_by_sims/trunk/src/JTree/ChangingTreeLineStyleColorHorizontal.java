package JTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;

public class ChangingTreeLineStyleColorHorizontal {
	public static void main(final String args[]) {
		JFrame frame = new JFrame("JTreeSample");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Vector<String> v1 = new TreeNodeVector<String>("Two", new String[] {
				"Mercury", "Venus", "Mars" });
		Vector<Object> v2 = new TreeNodeVector<Object>("Three");
		v2.add(System.getProperties());
		v2.add(v1);
		Object rootNodes[] = { v1, v2 };
		Vector<Object> rootVector = new TreeNodeVector<Object>("Root",
				rootNodes);
		JTree tree = new JTree(rootVector);

		UIManager.put("Tree.line", Color.GREEN);
		tree.putClientProperty("JTree.lineStyle", "Horizontal");

		frame.add(new JScrollPane(tree), BorderLayout.CENTER);

		frame.setSize(300, 300);
		frame.setVisible(true);

	}
}
