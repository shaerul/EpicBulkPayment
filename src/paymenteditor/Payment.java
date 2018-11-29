/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author ShaerulH
 */
public final class Payment extends javax.swing.JPanel {

    private static final String BANK = "bank";
    private static final String TRANSFER = "transfer";
    private static final String BEFTN = "BEFTN";
    private static final String RTGS = "RTGS";
    private static final String BT = "BT";

    protected static final String HSBC = "HSBC";
    protected static final String SCB = "SCB";
    private static final String SCB_SWIFT_CODE = "SCBLBDD";
    private static final String HSBC_SWIFT_CODE = "HSBCBDDH";
    private static final String SCB_NUMERIC_PREFIX = "X";
    private static final String HSBC_NUMERIC_PREFIX = "'";
    private static final BigDecimal RTGS_MIN_AMOUNT = new BigDecimal(100000); // In BDT

    // All column indexex and relative to left pane payment table columns
    // 0 means the very first left column and it will increment along left to right order
    private static final int AMOUNT_COLUMN_INDEX = 9;
    private static final int BANK_COLUMN_INDEX = 2;
    private static final int TRANSFER_COLUMN_INDEX = 3;
    private static final int BENEFICIARY_ROUTING_INDEX = 8;
    private static final int BENEFICIARY_BANK_ACCOUNT_INDEX = 6;
    private static final int VALUE_DATE_INDEX = 11;
    private static final int EPIC_BANK_ACCOUNT_INDEX = 12;
    private static final int EPIC_ROUTING_INDEX = 22;

    String[] detailColName = {"Field", "Value"};

    // Unit name Array
    protected static final String[] UNITS = {
        "GTL",
        "CIPL",
        "PGCL",
        "EGMCL(AEPZ)",
        "EGMCL(DEPZ)"
    };

    // Bank Transfer Type Array
    protected static final String[] TRANSFER_ARRAY = {
        "BEFTN", // Hong Kong Shanghai Banking Corporation
        "RTGS", // Standard Chartered Bank
        "BT"
    };

    // Bank Name Array
    protected static final String[] BANKS_ARRAY = {
        "HSBC", // Hong Kong Shanghai Banking Corporation
        "SCB" // Standard Chartered Bank
    };

    // SCB CSV data Headers
    protected static final String[] CSV_SCB = {
        "Customer Reference",
        "Payee Name",
        "PayeeBankAccNo",
        "PayeeAccType (Add1)",
        "PayeeBankRouting (Add2)",
        "Amount",
        "Reason",
        "Payment Date (DD/MM/YYYY)",
        "Debit A/C No.",
        "Payee Email Address",
        "RTGS"
    };

    /* 
    Sequnece number for generating SCB CSV file from table columns
    This is required for iteration and pulling the right column value
    as per the index position arrange in an array.
     */
    protected static final int[] SEQ_SCB = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 3};

    // HSBC CSV data Headers
    protected static final String[] CSV_HSBC = {
        "Transaction Reference",
        "Amount",
        "Beneficiary Name",
        "Beneficiary A/C No",
        "Ben Bank Code/ Routing No",
        "Value Date(YYYYMMDD)",
        "Debit Account Number",
        "Payment Details1",
        "Payment Details2",
        "Payment Details3",
        "Email Address 1",
        "Email Address 2",
        "Account Type (Required for BEFTN)",
        "TransType (Required for BEFTN)",
        "RTGS (Y)",
        "First Party Name",
        "First Party Information Line 1",
        "First Party Information Line 2",
        "First Party Information Line 3"
    };

    /* 
    Sequnece number for generating HSBC CSV file from table columns which has been
    normalized as EPIC Sequence.
    
    This is required for iteration and pulling the right column value
    as per the index position arrange in an array.
     */
    private static final int[] SEQ_HSBC = {4, 9, 5, 6, 8, 11, 12, 10, 14, 15, 13, 16, 7, 17, 3, 18, 19, 20, 21};

    protected static final String[] PAYMENT_COL_NAME = {
        "Select",
        "Unit Name", // 1st Field    
        "EPIC Bank",
        "Bank Transfer",
        "Voucher No",
        "Beneficiary Account Name",
        "Beneficiary Account No",
        "Benficiary Account Type",
        "Beneficiary Routing No",
        "Amount",
        "Supplier Bill No",
        "Value Date",
        "EPIC Debit Account No",
        "Beneficiary Email", // 13th Field
        "Payment Details 2",
        "Payment Details 3",
        "Email Address 2",
        "TransType",
        "First Party Name",
        "First Party Information Line 1",
        "First Party Information Line 2",
        "First Party Information Line 3",
        "Epic Routing No" // 22nd Field
    };

    /*
    Object[] rowData = {
        "HSBC",
        false,
        "18130000437", // Max 15 Charascter
        "26/11/2018", // Value Date
        "G.S Chemical Suppliers", // Beneficiary Name
        "",// Beneficiary Bank"
        "0001554925602", //Benficiary Account No
        "Current", // Beneficiary Account Type
        "205260229", // Beneficiary Routing No
        "400000", // Amount
        "001174770015", // EPIC Debit Account
        "HSBC", // EPIC Bank Name
        "010234567", // EPIC Bank Routing No
        "CCD", // Trans Type
        "Cosmopolitan Industries pvt ltd", // First Party Name
        "H-17 R-15 SEC-03", // First Party Address 1
        "UTTARA DHAKA", // Frist Party Address 2
        "", // Frist Party Address 3
        "gschemicalsuppliers@gmail.com", // Email 1
        "mamuna@epicbd.com", // Email 2
        "" // Email 3
    };

    Object[] rowData1 = {
        "SCB",
        false,
        "18130000438", // Max 15 Charascter
        "26/11/2018", // Value Date
        "G.S Chemical Suppliers", // Beneficiary Name
        "",// Beneficiary Bank"
        "0001554925602", //Benficiary Account No
        "Current", // Beneficiary Account Type
        "205260229", // Beneficiary Routing No
        "400000", // Amount
        "001174770015", // EPIC Debit Account
        "HSBC", // EPIC Bank Name
        "010234567", // EPIC Bank Routing No
        "CCD", // Trans Type
        "Cosmopolitan Industries pvt ltd", // First Party Name
        "H-17 R-15 SEC-03", // First Party Address 1
        "UTTARA DHAKA", // Frist Party Address 2
        "", // Frist Party Address 3
        "gschemicalsuppliers@gmail.com", // Email 1
        "mamuna@epicbd.com", // Email 2
        "" // Email 3
    };
     */
    /**
     * Creates new form Payment
     */
    public Payment() {
        initComponents();

        // added by Neel
        this.initializePaymentTable();
    }

    /**
     * This method will initialize the payment table at start up So that all
     * columns are showed on the left pane and right pane. column Checkboxes and
     * Comboboxes will also be initialize with table.
     */
    public void initializePaymentTable() {

        // Initializing Righ Pane
        DefaultTableModel rightPanemodel = new DefaultTableModel();
        rightPanemodel.setColumnIdentifiers(detailColName);
        detailsTable.setModel(rightPanemodel);

        // Initializing Left Pane
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(PAYMENT_COL_NAME);
        paymentTable.setModel(model);

        // Setting first and second column with combobox
        paymentTable.getColumnModel().getColumn(BANK_COLUMN_INDEX).setCellEditor(new DefaultCellEditor(generateComboBoxForColumn(BANK)));
        paymentTable.getColumnModel().getColumn(TRANSFER_COLUMN_INDEX).setCellEditor(new DefaultCellEditor(generateComboBoxForColumn(TRANSFER)));

        // Setting the second column to checkbox
        TableColumn col = paymentTable.getColumnModel().getColumn(0);
        col.setCellEditor(paymentTable.getDefaultEditor(Boolean.class));
        col.setCellRenderer(paymentTable.getDefaultRenderer(Boolean.class));

        // Allowing Row selection in one click
        paymentTable.setRowSelectionAllowed(true);

        paymentTable.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key Pressed");
            }

            /*
            @Override
            public void keyTyped(KeyEvent ke) {
                char i = ke.getKeyChar();
                int ib = ((int) i);
                
                System.out.println("Key Typed");
                if ((ib == 8)) {
                    //if (jt1.isEditing()) {
                    //    jt1.getCellEditor().cancelCellEditing();
                    //}
                } else {
                    // my code to do
                }
            }*/
            //System.out.println("Key Pressed");
        });

        paymentTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                int r = paymentTable.rowAtPoint(e.getPoint());

                if (r >= 0 && r < paymentTable.getRowCount()) {
                    paymentTable.setRowSelectionInterval(r, r);

                } else {
                    paymentTable.clearSelection();
                }

                final int rowindex = paymentTable.getSelectedRow();
                final int colindex = paymentTable.getSelectedColumn();

                // Populate Right Window with Data
                populateDetailsPane(rowindex);

                if (rowindex < 0) {
                    return;
                }

                JPopupMenu popup = new JPopupMenu();
                JMenuItem menuItem = new JMenuItem("Payment Details...");
                //menuItem.addActionListener(this);

                popup.add(menuItem);
                menuItem = new JMenuItem("Select Row");
                menuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ev) {

                        /*                        
                        System.out.println("Row Index: " + rowindex + " Column Index: " + colindex);
                        javax.swing.JFrame frame = new javax.swing.JFrame("Payement Details");
                        //frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
                        frame.setResizable(false);
                        frame.setAlwaysOnTop(true);
                        frame.getContentPane().add(new PaymentDetailsWindow());
                        frame.pack();
                        frame.setVisible(true);
                        */
                    }

                });
                //menuItem.addActionListener(this);
                popup.add(menuItem);

                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    //JPopupMenu popup = createYourPopUp();
                    //JPopupMenu popup = new JPopupMenu();
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });
    }

    private void populateDetailsPane(int rowindex) {

        DefaultTableModel detailsModel = (DefaultTableModel) detailsTable.getModel();
        detailsModel.setRowCount(0);

        for (int i = 0; i < paymentTable.getModel().getColumnCount(); i++) {

            String value = paymentTable.getModel().getValueAt(rowindex, i).toString();
            String key = paymentTable.getModel().getColumnName(i);

            String[] detailsRow = {key, value};
            DefaultTableModel myModel = (DefaultTableModel) detailsTable.getModel();
            myModel.addRow(detailsRow);

        }
    }

    public void newRow() {

        DefaultTableModel yourModel = (DefaultTableModel) paymentTable.getModel();
        //yourModel.addRow(rowData);
        //yourModel.addRow(rowData1);

    }

    public boolean validationCheckForRTGS(int row, int col) {

        boolean result = true;
        String amount = paymentTable.getModel().getValueAt(row, col).toString();
        BigDecimal decAmount = new BigDecimal(amount);

        if (RTGS_MIN_AMOUNT.compareTo(decAmount) > 0) {

            result = false;
        }

        return result;
    }

    public void selectAllRow() {

        for (int i = 0; i < paymentTable.getModel().getRowCount(); i++) {

            if (validationCheckForRTGS(i, AMOUNT_COLUMN_INDEX)) {

                paymentTable.getModel().setValueAt(true, i, 0);

            }
        }
    }

    public void clearAllSelection() {

        for (int i = 0; i < paymentTable.getModel().getRowCount(); i++) {

            paymentTable.getModel().setValueAt(false, i, 0);
        }
    }

    public JTable getPaymentTable() {
        return paymentTable;
    }

    // JPOP menu start
    // JPOP menu end
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        paymentTable = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        detailsTable = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setAutoscrolls(true);
        setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jButton1.setText("Add Row");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Get Selected");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Select All");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Clear All Selection");
        jButton4.setToolTipText("Pressing this button will clear all selection for RTGS Payment");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerSize(2);

        paymentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        paymentTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        paymentTable.setRowSelectionAllowed(false);
        paymentTable.setShowHorizontalLines(false);
        paymentTable.setShowVerticalLines(false);
        jScrollPane2.setViewportView(paymentTable);
        paymentTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jSplitPane1.setLeftComponent(jScrollPane2);

        detailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        detailsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        detailsTable.setRowSelectionAllowed(false);
        detailsTable.setShowHorizontalLines(false);
        detailsTable.setShowVerticalLines(false);
        jScrollPane1.setViewportView(detailsTable);

        jSplitPane1.setRightComponent(jScrollPane1);

        jButton5.setText("File Upload...");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("SCB CSV Downlaod");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("HSBC CSV Download");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jButton4)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton6)
                            .addComponent(jButton7))))
                .addGap(18, 18, 18)
                .addComponent(jSplitPane1)
                .addGap(23, 23, 23))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //populatePaymentTable(paymentTable);
        newRow();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < paymentTable.getModel().getRowCount(); i++) {

            Boolean checked = (Boolean) paymentTable.getModel().getValueAt(i, 0);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        selectAllRow();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        clearAllSelection();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        File file = new FileChooser().GetFile();

        if (file != null) {

            Vector<Vector> tableRow = CSVJobs.readDataLineByLine(file.toString());

            if (tableRow != null) {

                DefaultTableModel yourModel = (DefaultTableModel) paymentTable.getModel();
                //yourModel.setDataVector((Object[][]) rowData, PAYMENT_COL_NAME);

                for (int i = 1; i < tableRow.size(); i++) {
                    tableRow.elementAt(i).add(0, false); // adding select at the begining
                    yourModel.addRow(tableRow.elementAt(i));
                }

                paymentTable.requestFocus(); // get the focus of the first row of the table
                paymentTable.changeSelection(0, 0, false, false); // select 
                populateDetailsPane(0); // populate the data of first row in the table

            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        CSVprocessingForSCB();

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        CSVprocessingForHSBC();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void CSVprocessingForSCB() {
        // This is CSV file generation process for SCB
        try {

            // This is the final vector to be sent for writing as a file
            Vector<Vector> rowTable = new Vector<Vector>();

            for (int i = 0; i < paymentTable.getRowCount(); i++) {

                // add row if the row is for SCB or ignore
                if (paymentTable.getValueAt(i, BANK_COLUMN_INDEX).toString().equalsIgnoreCase(SCB)) {

                    // This is container for the current row only
                    Vector<String> row = new Vector<>();

                    // Column wise data collection by matching with SEQ_SCB[index]
                    for (int j = 0; j < SEQ_SCB.length; j++) {

                        switch (SEQ_SCB[j]) {

                            case TRANSFER_COLUMN_INDEX: // the index of visual column in table for TRANSFER TYPE e.g. BEFTN RTGS etc.

                                if (paymentTable.getValueAt(i, SEQ_SCB[j]).toString().equalsIgnoreCase(RTGS)) {
                                    // payment will be through RTGS
                                    row.addElement("YES");

                                } else {
                                    // else through BEFTN
                                    row.addElement("NO"); // Leaving blank if BEFTN;

                                }

                                break;

                            case BENEFICIARY_ROUTING_INDEX:

                                String benRouting = paymentTable.getValueAt(i, SEQ_SCB[j]).toString().substring(0, 2);
                                String epicRouting = paymentTable.getValueAt(i, EPIC_ROUTING_INDEX).toString().substring(0, 2);

                                if (benRouting.equalsIgnoreCase(epicRouting)) {
                                    // if first three digits matches 
                                    // this means beneficiary and epic both has got bank in common
                                    row.addElement(SCB_SWIFT_CODE); // putting swift code
                                    //row.setElementAt("", 4); // set the RTGS value to blank

                                } else {

                                    //simply add an X prefix
                                    row.addElement(SCB_NUMERIC_PREFIX + paymentTable.getValueAt(i, SEQ_SCB[j]).toString());
                                }

                                break;

                            case BENEFICIARY_BANK_ACCOUNT_INDEX:

                                row.addElement(SCB_NUMERIC_PREFIX + paymentTable.getValueAt(i, SEQ_SCB[j]).toString());
                                break;

                            case EPIC_BANK_ACCOUNT_INDEX:

                                row.addElement(SCB_NUMERIC_PREFIX + paymentTable.getValueAt(i, SEQ_SCB[j]).toString());
                                break;

                            case VALUE_DATE_INDEX:
                                // Changing Date Format
                                row.addElement(MyUtility.SCBDateFormat(paymentTable.getValueAt(i, SEQ_SCB[j]).toString()));
                                break;

                            default:
                                row.addElement(paymentTable.getValueAt(i, SEQ_SCB[j]).toString());
                                break;
                        }
                    }

                    rowTable.addElement(row);

                }

            }

            //yourModel.getValueAt(ERROR, WIDTH)
            CSVJobs.writeDataToCSV(rowTable, SCB);

        } catch (Exception ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CSVprocessingForHSBC() {

        try {

            // This is the final vector to be sent for writing as a file
            Vector<Vector> rowTable = new Vector<Vector>();

            for (int i = 0; i < paymentTable.getRowCount(); i++) {

                // add row if the row is for HSBC or ignore
                // for a new bank define a new final String like SCB or HSBC and replace
                if (paymentTable.getValueAt(i, BANK_COLUMN_INDEX).toString().equalsIgnoreCase(HSBC)) {

                    // This is container for one row only
                    Vector<String> row = new Vector<>();

                    // Column wise data collection by matching with SEQ_SCB[index]
                    // Here total number of iteration is equal to SEQ_HSBC
                    for (int j = 0; j < SEQ_HSBC.length; j++) {

                        switch (SEQ_HSBC[j]) {

                            case TRANSFER_COLUMN_INDEX: // the index of visual column in table for TRANSFER TYPE e.g. BEFTN RTGS etc.

                                if (paymentTable.getValueAt(i, SEQ_HSBC[j]).toString().equalsIgnoreCase(RTGS)) {
                                    // payment will be through RTGS
                                    row.addElement("YES");

                                } else {
                                    // else through BEFTN
                                    row.addElement(""); // Leaving blank if BEFTN;
                                }

                                break;

                            case BENEFICIARY_ROUTING_INDEX:
                                
                                /* if first three digits of Benficiary Bank Routing 
                                   and EPIC Bank Routing are same then it would be BANK TRANSFER
                                   In case of Bank Transfer we have replace Beneficiary routing
                                   number with the Bank's SWIFT CODE. 
                                */    
                                
                                String beneficiaryRouting = paymentTable.getValueAt(i, SEQ_HSBC[j]).toString().substring(0, 2);
                                String epicRouting = paymentTable.getValueAt(i, EPIC_ROUTING_INDEX).toString().substring(0, 2);

                                if (beneficiaryRouting.equalsIgnoreCase(epicRouting)) {
                                    // if first three digits matches 
                                    // this means beneficiary and epic both has got bank in common
                                    row.addElement(HSBC_SWIFT_CODE); // putting swift code
                                    //row.setElementAt("", 4); // set the RTGS value to blank

                                } else { // So if its not BT then add the HSBC prefix

                                    //simply add an single quote prefix
                                    row.addElement(HSBC_NUMERIC_PREFIX + paymentTable.getValueAt(i, SEQ_HSBC[j]).toString());
                                }

                                break;

                            case BENEFICIARY_BANK_ACCOUNT_INDEX:

                                row.addElement(HSBC_NUMERIC_PREFIX + paymentTable.getValueAt(i, SEQ_HSBC[j]).toString());
                                break;

                            case EPIC_BANK_ACCOUNT_INDEX:

                                row.addElement(HSBC_NUMERIC_PREFIX + paymentTable.getValueAt(i, SEQ_HSBC[j]).toString());
                                break;

                            case VALUE_DATE_INDEX:
                                // Changing Date Format
                                row.addElement(MyUtility.HSBCDateFormat(paymentTable.getValueAt(i, SEQ_HSBC[j]).toString()));
                                break;

                            default:
                                row.addElement(paymentTable.getValueAt(i, SEQ_HSBC[j]).toString());
                                break;
                        }
                    }

                    rowTable.addElement(row);

                }
            }

            //yourModel.getValueAt(ERROR, WIDTH)
            CSVJobs.writeDataToCSV(rowTable,HSBC);

        } catch (Exception ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JComboBox generateComboBoxForColumn(String type) {

        JComboBox combox = new JComboBox();

        if (type.equalsIgnoreCase(BANK)) {

            for (String bank : BANKS_ARRAY) {
                combox.addItem(bank);
            }

        } else if (type.equalsIgnoreCase(TRANSFER)) {

            for (String trn : TRANSFER_ARRAY) {
                combox.addItem(trn);
            }

        }

        return combox;
    }

    private void handleRowClick(MouseEvent e) {

        ListSelectionModel selectionModel = paymentTable.getSelectionModel();
        Point contextMenuOpenedAt = e.getPoint();
        int clickedRow = paymentTable.rowAtPoint(contextMenuOpenedAt);

        if (clickedRow < 0) {

            // No row selected
            selectionModel.clearSelection();

        } else {
            // Some row selected
            if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
                int maxSelect = selectionModel.getMaxSelectionIndex();

                if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                    // Shift + CTRL
                    selectionModel.addSelectionInterval(maxSelect, clickedRow);
                } else {
                    // Shift
                    selectionModel.setSelectionInterval(maxSelect, clickedRow);
                }
            } else if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                // CTRL
                selectionModel.addSelectionInterval(clickedRow, clickedRow);
            } else {
                // No modifier key pressed
                selectionModel.setSelectionInterval(clickedRow, clickedRow);
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable detailsTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable paymentTable;
    // End of variables declaration//GEN-END:variables
}
