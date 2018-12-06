/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author ShaerulH
 */
public final class PaymentTableOperation {

    private PaymentParentFrame parentFrame;
    private String[] detailColName;

    PaymentTableOperation(PaymentParentFrame parentFrame) {

        this.detailColName = StaticIdentifiers.detailColName;
        this.parentFrame = parentFrame;

        initializePaymentTable(parentFrame);
    }

    public void initializePaymentTable(PaymentParentFrame parentFrame) {

        // Initializing Righ Pane
        DefaultTableModel rightPanemodel = new DefaultTableModel();
        rightPanemodel.setColumnIdentifiers(detailColName);
        parentFrame.getjTableDetails().setModel(rightPanemodel);

        // Initializing Left Pane
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(StaticIdentifiers.PAYMENT_COL_NAME);
        parentFrame.getjTablePayment().setModel(model);

        // Setting first and second column with combobox
        //paymentFrame.getjTablePayment().getColumnModel().getColumn(StaticIdentifiers.BANK_COLUMN_INDEX).setCellEditor(new DefaultCellEditor(generateComboBoxForColumn(StaticIdentifiers.BANK)));
        //paymentFrame.getjTablePayment().getColumnModel().getColumn(StaticIdentifiers.TRANSFER_COLUMN_INDEX).setCellEditor(new DefaultCellEditor(generateComboBoxForColumn(StaticIdentifiers.TRANSFER)));
        // Setting the second column to checkbox
        TableColumn col = parentFrame.getjTablePayment().getColumnModel().getColumn(0);
        col.setCellEditor(parentFrame.getjTablePayment().getDefaultEditor(Boolean.class));
        col.setCellRenderer(parentFrame.getjTablePayment().getDefaultRenderer(Boolean.class));

        // Allowing Row selection in one click
        parentFrame.getjTablePayment().setRowSelectionAllowed(true);

        parentFrame.getjTablePayment().addKeyListener(new KeyAdapter() {

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

        parentFrame.getjTablePayment().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                int r = parentFrame.getjTablePayment().rowAtPoint(e.getPoint());

                if (r >= 0 && r < parentFrame.getjTablePayment().getRowCount()) {
                    parentFrame.getjTablePayment().setRowSelectionInterval(r, r);

                } else {
                    parentFrame.getjTablePayment().clearSelection();
                }

                final int rowindex = parentFrame.getjTablePayment().getSelectedRow();
                final int colindex = parentFrame.getjTablePayment().getSelectedColumn();

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

    public void populateDetailsPane(int rowindex) {

        DefaultTableModel detailsModel = (DefaultTableModel) parentFrame.getjTableDetails().getModel();
        detailsModel.setRowCount(0);

        for (int i = 0; i < parentFrame.getjTablePayment().getModel().getColumnCount(); i++) {

            String value = parentFrame.getjTablePayment().getModel().getValueAt(rowindex, i).toString();
            String key = parentFrame.getjTablePayment().getModel().getColumnName(i);

            String[] detailsRow = {key, value};
            DefaultTableModel myModel = (DefaultTableModel) parentFrame.getjTableDetails().getModel();
            myModel.addRow(detailsRow);

        }

        // RESIZING COLUMNS
        resizeColumnWidth(parentFrame.getjTableDetails());

    }

    /**
     *
     */
    public void openFileAndPopulateDataInTable() {

        File file = new FileChooser().GetFile();

        if (file != null) {

            Vector<Vector> tableRow = CSVJobs.readDataLineByLine(file.toString());

            if (tableRow != null) {

                DefaultTableModel paymentTableModel = (DefaultTableModel) parentFrame.getjTablePayment().getModel();

                if (paymentTableModel.getRowCount() > 0) {

                    // TODO
                    // Prompt user whether user want to append CSV
                    // If yes then check duplication and append to table
                    // If no then setNumRows(0)
                    paymentTableModel.setNumRows(0);

                }

                for (int i = 1; i < tableRow.size(); i++) {
                    tableRow.elementAt(i).add(0, false); // adding select at the begining
                    paymentTableModel.addRow(tableRow.elementAt(i));
                }

                // UPDATE COUNTERS
                rowAndColumnCounterUpdate();

                // AUTORESIZE COLUMNS
                //resizeColumnWidth(parentFrame.getjTablePayment());
                resizeHeaderAndColumnWidth(parentFrame.getjTablePayment());

                parentFrame.getjTablePayment().requestFocus(); // get the focus of the first row of the table
                parentFrame.getjTablePayment().changeSelection(0, 0, false, false); // select 
                populateDetailsPane(0); // populate the data of first row in the table

            }
        }
    }

    public void resizeHeaderAndColumnWidth(JTable table) {

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader tableHeader = table.getTableHeader();
        Font font = new Font("Verdana", Font.BOLD, 12);
        tableHeader.setFont(font);

        for (int column = 0; column < table.getColumnCount(); column++) {

            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = 0;
            TableCellRenderer rend = table.getTableHeader().getDefaultRenderer();
            TableCellRenderer rendCol = tableColumn.getHeaderRenderer();
            if (rendCol == null) {
                rendCol = rend;
            }

            Component header = rendCol.getTableCellRendererComponent(table, tableColumn.getHeaderValue(), false, false, 0, column);
            maxWidth = header.getPreferredSize().width;
            System.out.println("maxWidth :" + maxWidth);

            for (int row = 0; row < table.getRowCount(); row++) {

                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
                System.out.println("preferredWidth :" + preferredWidth);
                System.out.println("Width :" + width);

                //  We've exceeded the maximum width, no need to check other rows
                if (preferredWidth <= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);

        }

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
    }

    public void resizeColumnWidth(JTable table) {

        final TableColumnModel columnModel = table.getColumnModel();

        JTableHeader header = table.getTableHeader();
        Font font = new Font("Verdana", Font.BOLD, 12);
        header.setFont(font);

        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 300) {
                width = 300;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }

        // Added by Neel
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
    }

    public void selectAllRow() {

        for (int i = 0; i < parentFrame.getjTablePayment().getModel().getRowCount(); i++) {

            //if (validationCheckForRTGS(i, StaticIdentifiers.AMOUNT_COLUMN_INDEX)) {
            parentFrame.getjTablePayment().getModel().setValueAt(true, i, 0);

            //}
        }
        
        parentFrame.getjCheckBoxSelectAll().setSelected(true);
    }

    public void clearAllSelection() {

        for (int i = 0; i < parentFrame.getjTablePayment().getModel().getRowCount(); i++) {

            parentFrame.getjTablePayment().getModel().setValueAt(false, i, 0);
        }
        
        parentFrame.getjCheckBoxSelectAll().setSelected(false);
    }

    private void rowAndColumnCounterUpdate() {

        parentFrame.getjLabelRowCount().setText(parentFrame.getjTablePayment().getRowCount() + "");
        parentFrame.getjLabelColumnCount().setText(parentFrame.getjTablePayment().getColumnCount() + "");

        // For debugging purpose only
        //parentFrame.getjLabelRowCount().setText("1000000000");
        //parentFrame.getjLabelColumnCount().setText("1000000000");
    }
    
}
