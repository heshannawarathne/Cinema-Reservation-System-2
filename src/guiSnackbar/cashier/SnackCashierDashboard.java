/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package guiSnackbar.cashier;

import guiSnackbar.cashier.*;
import guiSnackbar.Login.SnackLogin;
import guiCashier.*;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import guiLogin.LogingOption;
import guiManager.MovieManageGRN;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import model.InvoiceItem;
import model.SnackInvoiceItem;
import model.mySQL;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Laky
 */
public class SnackCashierDashboard extends javax.swing.JFrame {

    HashMap<String, SnackInvoiceItem> invoicemap = new HashMap<>();
    HashMap<String, String> paymentMethodMap = new HashMap<>();

    /**
     * Creates new form SnackCashierDashboard
     */
    public SnackCashierDashboard(String email) {
        initComponents();
        hint();
        jLabel2.setText(email);
        jTextField1.setEditable(false);
        jTextField3.setEditable(false);
        jFormattedTextField3.setEditable(false);
        setLiveDT(jLabel8);
        genarateInvoiceId();
        loadPMethods();
    }

    private void hint() {

        if (jTextField3 != null) {
            jTextField3.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ID");
        }
        if (jTextField5 != null) {
            jTextField5.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Snack Name");
        }
        if (jTextField4 != null) {
            jTextField4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Avalible Qty");
        }
        if (jTextField6 != null) {
            jTextField6.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "customer Mobile");
        }
        if (jFormattedTextField2 != null) {
            jFormattedTextField2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "gty");
        }
        if (jFormattedTextField3 != null) {
            jFormattedTextField3.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "given amount");
        }
        if (jFormattedTextField4 != null) {
            jFormattedTextField4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "payed amount");
        }

    }

    //snack id
    public JTextField getjTextField2() {
        return jTextField2;
    }

    //sellingPrice
    public JFormattedTextField getjFormattedTextField1() {
        return jFormattedTextField1;
    }

    //qty
    public JFormattedTextField getjFormattedTextField2() {
        return jFormattedTextField2;
    }

    //Instock qty
    public JTextField getjTextField4() {
        return jTextField4;
    }

    //stockID
    public JTextField getjTextField3() {
        return jTextField3;
    }

    //stockID
    public JTextField getjTextField5() {
        return jTextField5;
    }

    //brand
    public JTextField getjTextField7() {
        return jTextField7;
    }

    private void genarateInvoiceId() {
        long id = System.currentTimeMillis();
        jTextField1.setText(String.valueOf(id));
        jTextField1.setEnabled(false);
    }

    private void loadPMethods() {

        try {

            ResultSet resultSet = mySQL.executeSearch("SELECT * FROM `payment_method`");

            Vector<String> vector = new Vector<>();

            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                paymentMethodMap.put(resultSet.getString("name"), resultSet.getString("id"));

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel<>(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadInvoiceItem() {

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        total = 0;

        for (SnackInvoiceItem invoiceItem : invoicemap.values()) {

            Vector<String> vector = new Vector<>();
            vector.add(invoiceItem.getStockID());
            vector.add(invoiceItem.getProductName());
            vector.add(invoiceItem.getSellingPrice());
            vector.add(invoiceItem.getProductQty());

            double itemTotal = Double.parseDouble(invoiceItem.getProductQty()) * Double.parseDouble(invoiceItem.getSellingPrice());
            total += itemTotal;
            vector.add(String.valueOf(itemTotal));

            model.addRow(vector);

        }

        jLabel10.setText(String.valueOf(total));

        //2
        calculate();

    }
    private double total = 0;
    private double payment = 0;
    private double balance = 0;
    private String paymentMethod = "Select";

    private void calculate() {

        if (jFormattedTextField4.getText().isEmpty()) {
            payment = 0;

        } else {
            payment = Double.parseDouble(jFormattedTextField4.getText());
        }

        total = Double.parseDouble(jLabel10.getText());

        paymentMethod = String.valueOf(jComboBox1.getSelectedItem());

        if (paymentMethod.equals("Cash")) { //Cash

            jFormattedTextField4.setEditable(true);

            balance = payment - total;

            if (balance < 0) {
                jButton7.setEnabled(false);

            } else {

                jButton7.setEnabled(true);
            }

        } else { //Card

            payment = total;
            balance = 0;
            jFormattedTextField4.setText(String.valueOf(payment));
            jFormattedTextField4.setEditable(false);
            jButton7.setEnabled(true);

        }

        jFormattedTextField3.setText(String.valueOf(balance));

    }

    private void saveinv() {
        try {

            String invID = jTextField1.getText();
            String userEmail = jLabel2.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String paid_Amount = jFormattedTextField4.getText();
            String total = jLabel10.getText();
            String AQTY = jFormattedTextField3.getText();
            String cusMobile = jTextField6.getText();
            String paymentMethodID = paymentMethodMap.get(String.valueOf(jComboBox1.getSelectedItem()));

            if (cusMobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter customer Mobile", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (!cusMobile.matches("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$")) {
                JOptionPane.showMessageDialog(this, "Please enter valid mobile number", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (AQTY.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Empty INVOICE,please add items", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {

                // Customer Register or not register
                ResultSet resultset = mySQL.executeSearch("SELECT * FROM `customer` WHERE `mobile` = '" + cusMobile + "'");
                if (!resultset.next()) {

                    // new customer register
                    mySQL.executeIUD("INSERT INTO `customer` (`mobile`, `customer_type_id`) VALUES('" + cusMobile + "', '1')");
                }

                //insert to invoice
                mySQL.executeIUD("INSERT INTO `snack_invoice` VALUES('" + invID + "','" + dateTime + "',"
                        + "'" + paid_Amount + "','" + paymentMethodID + "','" + cusMobile + "','" + userEmail + "')");
                //insert to invoice

                for (SnackInvoiceItem invoiceItem : invoicemap.values()) {

                    //insert to invoice item
                    mySQL.executeIUD("INSERT INTO `invoice_item` (`snack_stock_id`,`qty`,`snack_invoice_id`)"
                            + "VALUES ('" + invoiceItem.getStockID() + "','" + invoiceItem.getProductQty() + "','" + invID + "')");
                    //insert to invoice item

                    //stock update
                    mySQL.executeIUD("UPDATE `snack_stock` SET `qty` = `qty`-'" + invoiceItem.getProductQty() + "' WHERE `id` = '" + invoiceItem.getStockID() + "'");
                    //stock update
                    JOptionPane.showMessageDialog(this, "Successfull", "Success", JOptionPane.INFORMATION_MESSAGE);

                    genarateInvoiceId();
                    jButton2.setEnabled(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLiveDT(javax.swing.JLabel jLabel8) {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = sdf.format(now);
                jLabel8.setText(dateTime);
            }
        });
        timer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel71 = new javax.swing.JPanel();
        jPanel72 = new javax.swing.JPanel();
        jPanel73 = new javax.swing.JPanel();
        jPanel74 = new javax.swing.JPanel();
        jTextField7 = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jPanel40 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel30 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel48 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        jPanel54 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel55 = new javax.swing.JPanel();
        jPanel58 = new javax.swing.JPanel();
        jPanel59 = new javax.swing.JPanel();
        jPanel69 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jPanel62 = new javax.swing.JPanel();
        jPanel60 = new javax.swing.JPanel();
        jPanel63 = new javax.swing.JPanel();
        jPanel65 = new javax.swing.JPanel();
        jPanel66 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel64 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel67 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jPanel41 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Snack Cashier Dashboard");
        setPreferredSize(new java.awt.Dimension(1387, 834));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(31, 35, 51));
        jPanel4.setPreferredSize(new java.awt.Dimension(215, 100));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/cinema (2).png"))); // NOI18N
        jPanel4.add(jLabel1);

        jPanel1.add(jPanel4, java.awt.BorderLayout.LINE_START);

        jPanel5.setBackground(new java.awt.Color(31, 35, 51));
        jPanel5.setPreferredSize(new java.awt.Dimension(215, 100));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(31, 35, 51));
        jPanel11.setPreferredSize(new java.awt.Dimension(205, 100));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(31, 35, 51));
        jPanel12.setPreferredSize(new java.awt.Dimension(205, 60));

        jLabel5.setText("    ");

        jPanel7.setBackground(new java.awt.Color(31, 35, 51));
        jPanel7.setPreferredSize(new java.awt.Dimension(120, 44));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/contacts-32.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Welcome");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(31, 35, 51));
        jPanel8.setPreferredSize(new java.awt.Dimension(80, 44));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(31, 35, 51));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Lakshitha");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel2))
        );

        jPanel11.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel13.setBackground(new java.awt.Color(31, 35, 51));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("2024/05/09   10.00 PM");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clock.png"))); // NOI18N

        jTextField2.setText("jTextField2");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel11, java.awt.BorderLayout.LINE_START);

        jPanel1.add(jPanel5, java.awt.BorderLayout.LINE_END);

        jPanel6.setBackground(new java.awt.Color(31, 35, 51));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("SnackBar Cashier Dashboard");
        jPanel6.add(jLabel6, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel10.setPreferredSize(new java.awt.Dimension(200, 700));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logout-24.png"))); // NOI18N
        jButton2.setText("Logout");
        jButton2.setBorderPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(24, 119, 242));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Add Product");
        jButton4.setBorderPainted(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel10.add(jPanel25, java.awt.BorderLayout.PAGE_END);

        jPanel26.setLayout(new java.awt.BorderLayout());

        jPanel15.setPreferredSize(new java.awt.Dimension(200, 10));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel15, java.awt.BorderLayout.PAGE_START);

        jPanel16.setLayout(new java.awt.BorderLayout());

        jPanel17.setPreferredSize(new java.awt.Dimension(10, 531));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );

        jPanel16.add(jPanel17, java.awt.BorderLayout.LINE_START);

        jPanel18.setPreferredSize(new java.awt.Dimension(10, 531));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );

        jPanel16.add(jPanel18, java.awt.BorderLayout.LINE_END);

        jPanel19.setLayout(new java.awt.BorderLayout());

        jPanel20.setPreferredSize(new java.awt.Dimension(180, 90));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setPreferredSize(new java.awt.Dimension(180, 85));
        jPanel21.setLayout(new java.awt.GridLayout(2, 1, 5, 5));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel21.add(jTextField1);

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });
        jPanel21.add(jTextField6);

        jPanel20.add(jPanel21, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel20.add(jPanel22, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel20, java.awt.BorderLayout.PAGE_START);

        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel19.add(jPanel23, java.awt.BorderLayout.PAGE_END);

        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel27.setLayout(new java.awt.BorderLayout());
        jPanel24.add(jPanel27, java.awt.BorderLayout.PAGE_START);

        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel29.setPreferredSize(new java.awt.Dimension(180, 135));
        jPanel29.setLayout(new java.awt.BorderLayout());

        jPanel31.setPreferredSize(new java.awt.Dimension(180, 5));

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel29.add(jPanel31, java.awt.BorderLayout.PAGE_END);

        jPanel32.setLayout(new java.awt.BorderLayout());

        jPanel33.setPreferredSize(new java.awt.Dimension(180, 40));
        jPanel33.setLayout(new java.awt.BorderLayout());

        jPanel35.setPreferredSize(new java.awt.Dimension(40, 40));
        jPanel35.setLayout(new java.awt.GridLayout(1, 0));

        jButton3.setBackground(new java.awt.Color(24, 119, 242));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search-13-16.png"))); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton3);

        jPanel33.add(jPanel35, java.awt.BorderLayout.LINE_END);

        jPanel36.setLayout(new java.awt.BorderLayout());

        jPanel42.setLayout(new java.awt.BorderLayout());

        jPanel68.setPreferredSize(new java.awt.Dimension(40, 40));
        jPanel68.setLayout(new java.awt.BorderLayout());

        jTextField3.setPreferredSize(new java.awt.Dimension(30, 30));
        jPanel68.add(jTextField3, java.awt.BorderLayout.CENTER);

        jPanel42.add(jPanel68, java.awt.BorderLayout.LINE_START);

        jPanel71.setLayout(new java.awt.BorderLayout());

        jPanel72.setPreferredSize(new java.awt.Dimension(2, 531));

        javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
        jPanel72.setLayout(jPanel72Layout);
        jPanel72Layout.setHorizontalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel72Layout.setVerticalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel71.add(jPanel72, java.awt.BorderLayout.LINE_START);

        jPanel73.setLayout(new java.awt.BorderLayout());

        jPanel74.setPreferredSize(new java.awt.Dimension(5, 531));

        javax.swing.GroupLayout jPanel74Layout = new javax.swing.GroupLayout(jPanel74);
        jPanel74.setLayout(jPanel74Layout);
        jPanel74Layout.setHorizontalGroup(
            jPanel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel74Layout.setVerticalGroup(
            jPanel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel73.add(jPanel74, java.awt.BorderLayout.LINE_END);
        jPanel73.add(jTextField7, java.awt.BorderLayout.CENTER);

        jPanel71.add(jPanel73, java.awt.BorderLayout.CENTER);

        jPanel42.add(jPanel71, java.awt.BorderLayout.CENTER);

        jPanel36.add(jPanel42, java.awt.BorderLayout.CENTER);

        jPanel33.add(jPanel36, java.awt.BorderLayout.CENTER);

        jPanel32.add(jPanel33, java.awt.BorderLayout.PAGE_START);

        jPanel34.setLayout(new java.awt.BorderLayout());

        jPanel37.setPreferredSize(new java.awt.Dimension(180, 5));

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel34.add(jPanel37, java.awt.BorderLayout.PAGE_START);

        jPanel38.setLayout(new java.awt.BorderLayout());

        jPanel39.setPreferredSize(new java.awt.Dimension(180, 40));
        jPanel39.setLayout(new java.awt.BorderLayout());

        jTextField5.setText("Snack Name");
        jPanel39.add(jTextField5, java.awt.BorderLayout.CENTER);

        jPanel38.add(jPanel39, java.awt.BorderLayout.PAGE_START);

        jPanel40.setLayout(new java.awt.BorderLayout());

        jPanel43.setPreferredSize(new java.awt.Dimension(180, 5));

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel40.add(jPanel43, java.awt.BorderLayout.PAGE_START);

        jPanel44.setLayout(new java.awt.GridLayout(1, 0));
        jPanel44.add(jTextField4);

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField2.setText("0");
        jFormattedTextField2.setToolTipText("0");
        jFormattedTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField2ActionPerformed(evt);
            }
        });
        jFormattedTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField2KeyReleased(evt);
            }
        });
        jPanel44.add(jFormattedTextField2);

        jPanel40.add(jPanel44, java.awt.BorderLayout.CENTER);

        jPanel38.add(jPanel40, java.awt.BorderLayout.CENTER);

        jPanel34.add(jPanel38, java.awt.BorderLayout.CENTER);

        jPanel32.add(jPanel34, java.awt.BorderLayout.CENTER);

        jPanel29.add(jPanel32, java.awt.BorderLayout.CENTER);

        jPanel28.add(jPanel29, java.awt.BorderLayout.PAGE_START);

        jPanel30.setLayout(new java.awt.BorderLayout());

        jPanel45.setLayout(new java.awt.BorderLayout());
        jPanel30.add(jPanel45, java.awt.BorderLayout.PAGE_START);

        jPanel46.setLayout(new java.awt.BorderLayout());

        jPanel47.setPreferredSize(new java.awt.Dimension(180, 40));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jPanel50.setLayout(new java.awt.BorderLayout());

        jPanel51.setLayout(new java.awt.GridLayout(1, 0));

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField1.setToolTipText("0");
        jFormattedTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyReleased(evt);
            }
        });
        jPanel51.add(jFormattedTextField1);

        jPanel50.add(jPanel51, java.awt.BorderLayout.CENTER);

        jPanel47.add(jPanel50, java.awt.BorderLayout.CENTER);

        jPanel46.add(jPanel47, java.awt.BorderLayout.PAGE_START);

        jPanel48.setLayout(new java.awt.BorderLayout());

        jPanel52.setPreferredSize(new java.awt.Dimension(180, 5));

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel48.add(jPanel52, java.awt.BorderLayout.PAGE_START);

        jPanel53.setLayout(new java.awt.BorderLayout());

        jPanel54.setPreferredSize(new java.awt.Dimension(180, 40));
        jPanel54.setLayout(new java.awt.BorderLayout());

        jPanel57.setLayout(new java.awt.GridLayout(1, 0));

        jButton6.setBackground(new java.awt.Color(51, 51, 51));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Clear");
        jButton6.setBorderPainted(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel57.add(jButton6);

        jPanel54.add(jPanel57, java.awt.BorderLayout.CENTER);

        jPanel53.add(jPanel54, java.awt.BorderLayout.PAGE_START);

        jPanel55.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jPanel55.add(jPanel58, java.awt.BorderLayout.PAGE_START);

        jPanel59.setLayout(new java.awt.BorderLayout());

        jPanel69.setPreferredSize(new java.awt.Dimension(180, 5));

        javax.swing.GroupLayout jPanel69Layout = new javax.swing.GroupLayout(jPanel69);
        jPanel69.setLayout(jPanel69Layout);
        jPanel69Layout.setHorizontalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel69Layout.setVerticalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel59.add(jPanel69, java.awt.BorderLayout.PAGE_START);

        jPanel70.setLayout(new java.awt.BorderLayout());

        jPanel56.setPreferredSize(new java.awt.Dimension(180, 40));
        jPanel56.setLayout(new java.awt.BorderLayout());

        jPanel62.setLayout(new java.awt.GridLayout(1, 0));
        jPanel56.add(jPanel62, java.awt.BorderLayout.CENTER);

        jPanel70.add(jPanel56, java.awt.BorderLayout.PAGE_START);

        jPanel60.setLayout(new java.awt.BorderLayout());

        jPanel63.setPreferredSize(new java.awt.Dimension(180, 130));
        jPanel63.setLayout(new java.awt.BorderLayout());

        jPanel65.setPreferredSize(new java.awt.Dimension(180, 5));

        javax.swing.GroupLayout jPanel65Layout = new javax.swing.GroupLayout(jPanel65);
        jPanel65.setLayout(jPanel65Layout);
        jPanel65Layout.setHorizontalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel65Layout.setVerticalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel63.add(jPanel65, java.awt.BorderLayout.PAGE_START);

        jPanel66.setLayout(new java.awt.GridLayout(1, 2));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Total");
        jPanel66.add(jLabel7);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("00");
        jPanel66.add(jLabel10);

        jPanel63.add(jPanel66, java.awt.BorderLayout.CENTER);

        jPanel60.add(jPanel63, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout jPanel64Layout = new javax.swing.GroupLayout(jPanel64);
        jPanel64.setLayout(jPanel64Layout);
        jPanel64Layout.setHorizontalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel64Layout.setVerticalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
        );

        jPanel60.add(jPanel64, java.awt.BorderLayout.CENTER);

        jPanel70.add(jPanel60, java.awt.BorderLayout.CENTER);

        jPanel59.add(jPanel70, java.awt.BorderLayout.CENTER);

        jPanel55.add(jPanel59, java.awt.BorderLayout.CENTER);

        jPanel53.add(jPanel55, java.awt.BorderLayout.CENTER);

        jPanel48.add(jPanel53, java.awt.BorderLayout.CENTER);

        jPanel46.add(jPanel48, java.awt.BorderLayout.CENTER);

        jPanel30.add(jPanel46, java.awt.BorderLayout.CENTER);

        jPanel28.add(jPanel30, java.awt.BorderLayout.CENTER);

        jPanel24.add(jPanel28, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel24, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel26.add(jPanel16, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel26, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel10, java.awt.BorderLayout.LINE_START);

        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel67.setPreferredSize(new java.awt.Dimension(128, 100));

        jButton7.setBackground(new java.awt.Color(0, 153, 0));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/printt.png"))); // NOI18N
        jButton7.setText("Print Invoice");
        jButton7.setBorderPainted(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 0, 0));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/cancel-.png"))); // NOI18N
        jButton9.setText("Cancel Invoice");
        jButton9.setBorderPainted(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", " " }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField3.setToolTipText("0");
        jFormattedTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField4.setToolTipText("0");
        jFormattedTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField4KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel67Layout.createSequentialGroup()
                .addContainerGap(581, Short.MAX_VALUE)
                .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel67Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel67Layout.createSequentialGroup()
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel67Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jPanel14.add(jPanel67, java.awt.BorderLayout.PAGE_END);

        jPanel41.setPreferredSize(new java.awt.Dimension(200, 10));

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1187, Short.MAX_VALUE)
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel41, java.awt.BorderLayout.PAGE_START);

        jPanel49.setPreferredSize(new java.awt.Dimension(10, 590));

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 624, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel49, java.awt.BorderLayout.LINE_END);

        jPanel61.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Product Name", "Price", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel61.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel61, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new InvoiceStockview(this, true).show();


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        LogingOption loginoption = new LogingOption();
        loginoption.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jFormattedTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField2KeyReleased
        String tx = jFormattedTextField2.getText();

        if (!tx.matches("\\d*")) {

            jFormattedTextField2.setText(tx.replaceAll("[^\\\\d]", ""));

        }
    }//GEN-LAST:event_jFormattedTextField2KeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jButton2.setEnabled(false);
        String stockID = jTextField3.getText();
        String brand = jTextField7.getText();
        String customer_mobile = jTextField6.getText();
        String SnackName = jTextField5.getText();
        String qty = jFormattedTextField2.getText();
        String sellingPrice = jFormattedTextField1.getText();
        String currentStock = jTextField4.getText();

        if (stockID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Select a stock", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (qty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please type a quentity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                double quantity = Double.parseDouble(qty);
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Product quantity must be greater than zero.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    jFormattedTextField2.grabFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jFormattedTextField2.grabFocus();
                return;
            }
            try {
                double quantity = Double.parseDouble(qty);
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Product quantity must be greater than zero.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    jTextField2.grabFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jTextField2.grabFocus();
                return;
            }

            try {
                double availableStock = Double.parseDouble(currentStock);
                double requestedQuantity = Double.parseDouble(qty);

                if (requestedQuantity > availableStock) {
                    JOptionPane.showMessageDialog(this, "Quantity exceeds available stock.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    jTextField2.grabFocus();
                    return;
                }

                jTextField4.setText(String.valueOf(availableStock - requestedQuantity));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid stock value. Please check available stock.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SnackInvoiceItem invoiceItem = new SnackInvoiceItem();

            invoiceItem.setProductName(SnackName);
            invoiceItem.setStockID(stockID);

            invoiceItem.setProductQty(qty);
            invoiceItem.setSellingPrice(sellingPrice);

            if (invoicemap.get(stockID) == null) {
                invoicemap.put(stockID, invoiceItem);

            } else {

                SnackInvoiceItem found = invoicemap.get(stockID);

                int option = JOptionPane.showConfirmDialog(this, "Do you want to Update the Quantity of Product :" + SnackName, "Message",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {

                    found.setProductQty(String.valueOf(Double.parseDouble(found.getProductQty()) + Double.parseDouble(qty)));

                }

            }
            loadInvoiceItem();
            reset();
            jButton3.grabFocus();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed

    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        calculate();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jFormattedTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField4KeyReleased
        String tx = jFormattedTextField4.getText();

        if (!tx.matches("\\d*")) {

            jFormattedTextField4.setText(tx.replaceAll("[^\\\\d]", ""));

        } else {
            calculate();
        }
    }//GEN-LAST:event_jFormattedTextField4KeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        saveinv();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {

            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {

                String stockID = jTable1.getValueAt(selectedRow, 0).toString();

                invoicemap.remove(stockID);

                loadInvoiceItem();
            }

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        String tx = jTextField6.getText();

        if (!tx.matches("\\d*")) {

            jTextField6.setText(tx.replaceAll("[^\\\\d]", ""));

        }
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        reset();

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        int conform
                = JOptionPane.showConfirmDialog(this, "Are You Sure?", "Cancel Invoice", JOptionPane.YES_NO_OPTION);

        if (conform == JOptionPane.YES_OPTION) {

            invoicemap.clear();
            loadInvoiceItem();
            jFormattedTextField3.setText("");
            jFormattedTextField4.setText("");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jFormattedTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField2ActionPerformed

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased
        String tx = jFormattedTextField1.getText();

        if (!tx.matches("\\d*")) {

            jFormattedTextField1.setText(tx.replaceAll("[^\\\\d]", ""));

        }
    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        FlatMacDarkLaf.setup();
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new SnackCashierDashboard().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables

    private void reset() {
        jTextField3.setText("");
        jTextField3.setEditable(true);
        jTextField4.setText("");
        jTextField4.setEditable(true);
        jTextField5.setText("");
        jTextField5.setEditable(true);

        jTextField7.setText("");
        jTextField7.setEditable(true);

        jFormattedTextField1.setText("");
        jFormattedTextField1.setEditable(true);

        jFormattedTextField2.setText("");
        jFormattedTextField2.setEditable(true);

    }

}
