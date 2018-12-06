/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ShaerulH
 */
public class PaymentSCBCSVProcessing {

    private final PaymentParentFrame parentFrame;

    public PaymentSCBCSVProcessing(PaymentParentFrame parentFrame) {

        this.parentFrame = parentFrame;
    }

    private Vector<String> CSVSCBFieldPrcessing(Vector<String> row, String payerBankRouting) {

        Vector<String> newRow = row;

        // FIXING DATE FORMAT
        newRow.set(StaticIdentifiers.CSV_SCB_VALUE_DATE_INDEX,
                MyUtility.SCBDateFormat(newRow.get(StaticIdentifiers.CSV_SCB_VALUE_DATE_INDEX)));

        // ADDING CAPITAL LETTER X PREFIX
        newRow.set(StaticIdentifiers.CSV_SCB_PAYEE_ACCOUNT_NO_INDEX, StaticIdentifiers.SCB_NUMERIC_PREFIX
                + newRow.get(StaticIdentifiers.CSV_SCB_PAYEE_ACCOUNT_NO_INDEX));

        // ADDING CAPITAL LETTER X PREFIX
        newRow.set(StaticIdentifiers.CSV_SCB_COMPANY_ACCOUNT_NO_INDEX, StaticIdentifiers.SCB_NUMERIC_PREFIX
                + newRow.get(StaticIdentifiers.CSV_SCB_COMPANY_ACCOUNT_NO_INDEX));

        // Checking for Bank Transfer First
        String benRouting = newRow.get(StaticIdentifiers.CSV_SCB_ROUTING_NUMBER_INDEX).substring(0, 3);
        String epicRouting = payerBankRouting.substring(0, 3);

        // SETTING PROPER BEFTN, RTGS AND BT VALUES
        if (benRouting.equalsIgnoreCase(epicRouting)) { // This is unconditionally BT

            // setting the row with SWIFT CODE
            newRow.set(StaticIdentifiers.CSV_SCB_ROUTING_NUMBER_INDEX, StaticIdentifiers.SCB_SWIFT_CODE);
            // leaving the RTGS field blank
            newRow.set(StaticIdentifiers.CSV_SCB_RTGS_FLAG_INDEX, "");

        } else if (newRow.get(StaticIdentifiers.CSV_SCB_RTGS_FLAG_INDEX).toString().equalsIgnoreCase(StaticIdentifiers.RTGS)) { // This is for RTGS

            BigDecimal amount = new BigDecimal(newRow.get(StaticIdentifiers.CSV_SCB_AMOUNT_INDEX));

            // amount less than one lac so RTGS is not possible
            if (amount.compareTo(StaticIdentifiers.RTGS_MIN_AMOUNT) < 0) { // amount less than one lac so RTGS is not possible 

                newRow.set(StaticIdentifiers.CSV_SCB_RTGS_FLAG_INDEX, StaticIdentifiers.NO);

            } else {

                newRow.set(StaticIdentifiers.CSV_SCB_RTGS_FLAG_INDEX, StaticIdentifiers.YES);
            }

            // Prefixing Routing Number with Capital X
            newRow.set(StaticIdentifiers.CSV_SCB_ROUTING_NUMBER_INDEX, StaticIdentifiers.SCB_NUMERIC_PREFIX
                    + newRow.get(StaticIdentifiers.CSV_SCB_ROUTING_NUMBER_INDEX));

        } else {

            newRow.set(StaticIdentifiers.CSV_SCB_RTGS_FLAG_INDEX, StaticIdentifiers.NO);

            // Prefixing Routing Number with Capital X
            newRow.set(StaticIdentifiers.CSV_SCB_ROUTING_NUMBER_INDEX, StaticIdentifiers.SCB_NUMERIC_PREFIX
                    + newRow.get(StaticIdentifiers.CSV_SCB_ROUTING_NUMBER_INDEX));
        }

        return newRow;
    }

    protected void CSVProcessingSCBStepOne() {

        // This is temporary vector for retaining SCB rows only
        Vector<Vector> rowTable = new Vector<Vector>();

        // Operate on All rows one after another
        // Then pick up all required fields from column position
        // Apply buisiness logic and meidate to final values
        // Write into three files for BEFTN RTGS and BT
        for (int i = 0; i < parentFrame.getjTablePayment().getRowCount(); i++) {

            // checking if its for SCB
            if (parentFrame.getjTablePayment().getValueAt(i, StaticIdentifiers.BANK_COLUMN_INDEX).toString().equalsIgnoreCase(StaticIdentifiers.SCB)) {

                // This is temporary container for the current row only
                Vector<String> row = new Vector<String>();

                // Column wise data collection by matching with SEQ_SCB[index]
                for (int j = 0; j < StaticIdentifiers.SEQ_SCB.length; j++) {

                    row.add(parentFrame.getjTablePayment().getValueAt(i, StaticIdentifiers.SEQ_SCB[j]).toString());
                }

                // Do the processing stage two
                row = CSVSCBFieldPrcessing(row, parentFrame.getjTablePayment().getValueAt(i, StaticIdentifiers.EPIC_ROUTING_INDEX).toString());
                rowTable.add(row);
            }

        }

        if (rowTable.size() > 0) {

            SCBCSVWriteToFile(rowTable);
        }
    }

    private void SCBCSVWriteToFile(Vector<Vector> rowTable) {

        // These are the final vectors to be sent for writing as a CSV file
        Vector<Vector> rowTableBEFTN = new Vector<Vector>();
        Vector<Vector> rowTableRTGS = new Vector<Vector>();
        Vector<Vector> rowTableBT = new Vector<Vector>();

        // Adding the Field Column headers to all three vectors
        Vector<String> headerRow = new Vector<>();
        Collections.addAll(headerRow, StaticIdentifiers.CSV_SCB);

        rowTableBEFTN.add(headerRow);
        rowTableRTGS.add(headerRow);
        rowTableBT.add(headerRow);

        for (Vector<String> row : rowTable) {

            // Check out if its BT
            if (row.get(StaticIdentifiers.CSV_SCB_ROUTING_NUMBER_INDEX).toString().equalsIgnoreCase(StaticIdentifiers.SCB_SWIFT_CODE)) {

                rowTableBT.add(row);

            } else if (row.get(StaticIdentifiers.CSV_SCB_RTGS_FLAG_INDEX).toString().equalsIgnoreCase(StaticIdentifiers.YES)) { // RTGS

                rowTableRTGS.add(row);

            } else { // BEFTN

                rowTableBEFTN.add(row);

            }
        }

        // FINALLY WRITING TO A FILE
        String dateNow = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        try {

            // Write to BEFTN File if the size is greater than 1
            // in 0 index the headers from the array string has all ready been inserted
            // At the begining of for loop
            if (rowTableBEFTN.size() > 1) {

                String filename = StaticIdentifiers.OUTPUT_FILE_PATH
                        + StaticIdentifiers.SCB + "-" + StaticIdentifiers.BEFTN
                        + "-" + dateNow + StaticIdentifiers.EXT;

                CSVJobs.writeDataToCSV(rowTableBEFTN, filename);

                // Write to Log Window
                parentFrame.textToLoggingTextArea(new Date().toString() + " " + filename
                        + " File created successfully" + " Total Record(s): " + (rowTableBEFTN.size() - 1) + "\n");

            }
            // Write to RTGS File if the size is not zero
            if (rowTableRTGS.size() > 1) {

                String filename = StaticIdentifiers.OUTPUT_FILE_PATH
                        + StaticIdentifiers.SCB + "-" + StaticIdentifiers.RTGS + "-" + dateNow + StaticIdentifiers.EXT;
                CSVJobs.writeDataToCSV(rowTableRTGS, filename);

                parentFrame.textToLoggingTextArea(new Date().toString() + " " + filename
                        + " File created successfully" + " Total Record(s): " + (rowTableRTGS.size() - 1) + "\n");
            }

            // Write to BT File if the size is not zero
            if (rowTableBT.size() > 1) {

                String filename = StaticIdentifiers.OUTPUT_FILE_PATH
                        + StaticIdentifiers.SCB + "-" + StaticIdentifiers.BT
                        + "-" + dateNow + StaticIdentifiers.EXT;
                CSVJobs.writeDataToCSV(rowTableBT, filename);
                
                parentFrame.textToLoggingTextArea(new Date().toString() + " " + filename
                        + " File created successfully" + " Total Record(s): " + (rowTableRTGS.size() - 1) + "\n");
            }

        } catch (Exception ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
