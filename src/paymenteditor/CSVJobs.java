/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/**
 *
 * @author ShaerulH
 */
public class CSVJobs {

    public static Vector<Vector> readDataLineByLine(String file) {

        Reader reader = null;
        Vector<Vector> rowTable = null;

        try {

            reader = Files.newBufferedReader(Paths.get(file));
            CSVReader csvReader = new CSVReader(reader);
            //List<String[]> records = csvReader.readAll();
            //csvReader.readAll();
            String[] nextRecord;

            rowTable = new Vector<Vector>();

            while ((nextRecord = csvReader.readNext()) != null) {

                Vector<String> row = new Vector<>();

                for (String nextRecord1 : nextRecord) {
                    System.out.println("Value: " + nextRecord1);
                    row.addElement(nextRecord1);
                }

                rowTable.addElement(row);
                /*
                System.out.println("Name : " + nextRecord[0]);
                System.out.println("Email : " + nextRecord[1]);
                System.out.println("Phone : " + nextRecord[2]);
                System.out.println("Country : " + nextRecord[3]);
                System.out.println("==========================");

                
                row.addElement(nextRecord[0]);
                row.addElement(nextRecord[1]);
                row.addElement(nextRecord[2]);
                row.addElement(nextRecord[3]);
                row.addElement(nextRecord[5]);
                row.addElement(nextRecord[6]);
                row.addElement(nextRecord[7]);
                row.addElement(nextRecord[8]);
                row.addElement(nextRecord[9]);
                row.addElement(nextRecord[10]);
                row.addElement(nextRecord[11]);
                row.addElement(nextRecord[12]);
                
                 */
            }

        } catch (IOException ex) {
            Logger.getLogger(CSVJobs.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(CSVJobs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rowTable;
    }

    public static void writeDataToCSV(Vector<Vector> rowTable, String bank) throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String filedate = dateFormat.format(date);

        String csv = "e:\\PaymentOutbox\\" + bank + "-" + filedate + ".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv));

        //Create record
        //String[] record = "4,David,Miller,Australia,30".split(",");
        List<String[]> entries = new ArrayList<>();

        // Choosing Header format for nominated Bank
        if (bank.equalsIgnoreCase(Payment.SCB)) {
            entries.add(Payment.CSV_SCB);
        } else {
            entries.add(Payment.CSV_HSBC);
        }

        for (Iterator<Vector> it = rowTable.iterator(); it.hasNext();) {

            Vector<String> element = it.next();
            String[] newString = element.toArray(new String[element.size()]);
            entries.add(newString);

            /*
            String newString = StringUtils.deleteWhitespace(element.toString().trim());
            newString = newString.substring(1, newString.length() - 1);*/
            //System.out.println(newString + "");
            //Write the record to file
            //writer.writeNext(newString,false); // false means writing without quotes
            //writer.write
        }

        //Write the record to file
        //writer.writeNext(record, false); // false means writing without quotes
        //writer.
        //close the writer
        // if false is not used then CSV columns vlaues shall be written within double quotes
        writer.writeAll(entries, false);
        writer.close();
    }
}
