package jseparator;

import javax.swing.JFrame;
import javax.swing.JSeparator;

public class JSeparatorVERTICALHORI {

  public static void main(String[] a){
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    frame.add(new JSeparator(JSeparator.VERTICAL));

    frame.setSize(300, 200);
    frame.setVisible(true);
  }


}
