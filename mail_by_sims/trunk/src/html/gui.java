package html;

/**
 * gui.java
 *
 * Created on 13-May-2010, 18:29:30
 */


import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.mail.Address;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class gui extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
// Table listing messages.
    private JTable table;
    String date;

    /** Creates new form gui */
    public gui() {

        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        recieve = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        inboxTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        recieve.setText("Receve");
        recieve.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recieveActionPerformed(evt);
            }
        });

        jButton1.setText("new");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

//        inboxTable.setModel(new javax.swing.table.DefaultTableModel(
//            new Object[][]{
//                {null, null, null},
//                {null, null, null},
//                {null, null, null},
//                {null, null, null}
//            },
//            new String[]{
//                "Date", "subject", "sender"
//            }) {
//
//            Class[] types = new Class[]{
//                java.lang.String.class, java.lang.String.class, java.lang.String.class}
//
//            public Class getColumnClass(int columnIndex) {
//                return types[columnIndex];
//            }
//        });
        jScrollPane1.setViewportView(inboxTable);
        inboxTable.getColumnModel().getColumn(0).setResizable(false);
        inboxTable.getColumnModel().getColumn(1).setResizable(false);
        inboxTable.getColumnModel().getColumn(2).setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(39, 39, 39).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(recieve).addGap(18, 18, 18).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(73, 73, 73)));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(31, 31, 31).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(recieve).addComponent(jButton1)).addGap(18, 18, 18).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(179, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>

    private void recieveActionPerformed(java.awt.event.ActionEvent evt) {
        fetchMail fetch = new fetchMail();
        fetch.fetch("email goes here, password goes here, search goes here");
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        createMail create = new createMail();
        centerW center = new centerW();
        //create.attVis();
        center.center(create);
        create.setVisible(true);
    }

    public void setDate(String Date, int pos) {

        //pos = pos + 1;

        String[] s = new String[5];
        s[pos] = Date;

        inboxTable.setValueAt(Date, pos, 0);
    }

    public String getDate() {
        return date;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new gui().setVisible(true);

            }
        });
    }
// Variables declaration - do not modify
    private javax.swing.JTable inboxTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton recieve;
// End of variables declaration
}

