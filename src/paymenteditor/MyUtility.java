/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ShaerulH
 */
public class MyUtility {

    public static String SCBDateFormat(String oldDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

        //String dateInString = "7-Jun-13";
        String newDateString = null;

        try {

            Date date = formatter.parse(oldDate);
            //System.out.println(date);

            //System.out.println(formatter1.format(date));
            newDateString = formatter1.format(date);

        } catch (ParseException e) {
        }

        return newDateString;
    }

    public static String HSBCDateFormat(String oldDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");

        //String dateInString = "7-Jun-13";
        String newDateString = null;

        try {

            Date date = formatter.parse(oldDate);
            //System.out.println(date);

            //System.out.println(formatter1.format(date));
            newDateString = formatter1.format(date);

        } catch (ParseException e) {
        }

        return newDateString;
    }

    /*
    public static void main(String args[]) {

        System.out.println(HSBCDateFormat("7-Jun-13"));
    }*/
}
