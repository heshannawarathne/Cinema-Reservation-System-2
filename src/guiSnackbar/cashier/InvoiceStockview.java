package guiSnackbar.cashier;


import guiSnackbar.cashier.*;
import guiSnackbar.Login.SnackLogin;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import model.mySQL;

/**
 *
 * @author Heshan Nawarathna
 */
public class InvoiceStockview extends javax.swing.JDialog {

    SnackCashierDashboard snk;

    public InvoiceStockview(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadGRNItem("");
        snk = (SnackCashierDashboard) parent;

    }

    private void loadGRNItem(String name) {

        try {
            ResultSet resultSet = mySQL.executeSearch("SELECT * FROM `snack_product` "
                    + "INNER JOIN `brand`ON `snack_product`.`brand_id`= `brand`.`id` "
                    + "INNER JOIN `snack_stock` ON `snack_product`.`id`=`snack_stock`.`snack_product_id`"
                    + "INNER JOIN `grn_item` ON `grn_item`.`snack_stock_id` =`snack_stock`.`id`WHERE `name` LIKE '" + name + "%'");

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("snack_stock.id"));
                vector.add(resultSet.getString("snack_product.id"));
                vector.add(resultSet.getString("brand.name"));
                vector.add(resultSet.getString("snack_product.product"));
                vector.add(resultSet.getString("snack_stock.selling_price"));
                vector.add(resultSet.getString("snack_stock.qty"));
                vector.add(resultSet.getString("snack_stock.mfd"));
                vector.add(resultSet.getString("snack_stock.exp"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(949, 10));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1073, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(949, 10));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1073, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 546));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.LINE_START);

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 546));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5, java.awt.BorderLayout.LINE_END);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(929, 50));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel17.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Stock View");
        jLabel1.setPreferredSize(new java.awt.Dimension(250, 16));
        jPanel17.add(jLabel1);

        jPanel25.setLayout(new java.awt.BorderLayout());

        jPanel26.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel30.setLayout(new java.awt.BorderLayout());

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel30.add(jTextField1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel30.add(jPanel11, java.awt.BorderLayout.LINE_END);

        jPanel28.add(jPanel30, java.awt.BorderLayout.CENTER);

        jPanel26.add(jPanel28, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel26, java.awt.BorderLayout.CENTER);

        jPanel17.add(jPanel25);

        jPanel7.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel9.setPreferredSize(new java.awt.Dimension(929, 5));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1053, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel13.setPreferredSize(new java.awt.Dimension(929, 5));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1053, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel13, java.awt.BorderLayout.PAGE_START);

        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setPreferredSize(new java.awt.Dimension(973, 5));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1053, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel20.add(jPanel21, java.awt.BorderLayout.PAGE_END);

        jPanel23.setLayout(new java.awt.GridLayout(1, 0));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Product id", "Brand", "Name", "Selling Price", "Quentity", "Mfd", "Exp"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel23.add(jScrollPane2);

        jPanel20.add(jPanel23, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        loadGRNItem(jTextField1.getText());
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int row = jTable2.getSelectedRow();

        if (evt.getClickCount() == 2) {

            String snack_id = String.valueOf(jTable2.getValueAt(row, 1));
            String qty = String.valueOf(jTable2.getValueAt(row, 5));
            String selling_price = String.valueOf(jTable2.getValueAt(row, 4));
            String snack_name = String.valueOf(jTable2.getValueAt(row, 3));
            String brand = String.valueOf(jTable2.getValueAt(row, 2));
            String stock_id = String.valueOf(jTable2.getValueAt(row, 0));

            snk.getjTextField3().setText(stock_id);
            snk.getjTextField2().setText(snack_id);
            snk.getjTextField4().setText(qty);
            snk.getjTextField4().setEnabled(false);
            snk.getjFormattedTextField1().setText(selling_price);
            snk.getjFormattedTextField1().setEditable(false);
            snk.getjTextField5().setText(snack_name);
            snk.getjTextField5().setEditable(false);
            snk.getjFormattedTextField2().grabFocus();
            snk.getjFormattedTextField2().setText("");
            snk.getjTextField7().setText(brand);
            snk.getjTextField7().setEditable(false);
            

            this.dispose();

        }
    }//GEN-LAST:event_jTable2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InvoiceStockview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InvoiceStockview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InvoiceStockview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InvoiceStockview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InvoiceStockview dialog = new InvoiceStockview(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
