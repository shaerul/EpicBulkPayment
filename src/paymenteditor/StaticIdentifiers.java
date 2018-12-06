/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import java.math.BigDecimal;

/**
 *
 * @author ShaerulH
 */

public class StaticIdentifiers {

    protected static final String BANK = "bank";
    protected static final String TRANSFER = "transfer";
    protected static final String BEFTN = "BEFTN";
    protected static final String RTGS = "RTGS";
    protected static final String BT = "BT";
    protected static final String YES = "YES";
    protected static final String Y = "Y";
    protected static final String N = "N";
    protected static final String NO = "NO";

    protected static final String OUTPUT_FILE_PATH = "e:\\PaymentOutbox\\";
    protected static final String EXT = ".csv";

    protected static final String HSBC = "HSBC";
    protected static final String SCB = "SCB";
    protected static final String SCB_SWIFT_CODE = "SCBLBDDX";
    protected static final String HSBC_SWIFT_CODE = "HSBCBDDH";
    protected static final String SCB_NUMERIC_PREFIX = "X";
    protected static final String HSBC_NUMERIC_PREFIX = "'";
    protected static final BigDecimal RTGS_MIN_AMOUNT = new BigDecimal("100000"); // In BDT

    // All column indexex and relative to left pane payment table columns
    // 0 means the very first left column and it will increment along left to right order
    protected static final int AMOUNT_COLUMN_INDEX = 9;
    protected static final int BANK_COLUMN_INDEX = 2;
    protected static final int TRANSFER_COLUMN_INDEX = 3;
    protected static final int BENEFICIARY_ROUTING_INDEX = 8;
    protected static final int BENEFICIARY_BANK_ACCOUNT_INDEX = 6;
    protected static final int VALUE_DATE_INDEX = 11;
    protected static final int EPIC_BANK_ACCOUNT_INDEX = 12;
    protected static final int EPIC_ROUTING_INDEX = 22;

    protected static final String[] detailColName = {"Field", "Value"};

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

    // To determine the field index of routng number and rtgs
    protected static final int CSV_SCB_PAYEE_ACCOUNT_NO_INDEX = 2;
    protected static final int CSV_SCB_COMPANY_ACCOUNT_NO_INDEX = 8;
    protected static final int CSV_SCB_ROUTING_NUMBER_INDEX = 4;
    protected static final int CSV_SCB_RTGS_FLAG_INDEX = 10;
    protected static final int CSV_SCB_AMOUNT_INDEX = 5;
    protected static final int CSV_SCB_VALUE_DATE_INDEX = 7;

    /* 
    Sequnece number for generating SCB CSV file from table columns
    This is required for iteration and pulling the right column value
    as per the index position arrange in an array.
    */
    
    protected static final int[] SEQ_SCB = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 3};

    // HSBC CSV data Headers
    protected static final String[] CSV_HSBC = {
        
        "Transaction Reference",
        "Amount", // 1
        "Beneficiary Name",
        "Beneficiary A/C No",       // 3
        "Ben Bank Code/ Routing No", // 4
        "Value Date(YYYYMMDD)", // 5
        "Debit Account Number", 
        "Payment Details1",
        "Payment Details2",
        "Payment Details3",
        "Email Address 1",
        "Email Address 2",
        "Account Type (Required for BEFTN)", // 12
        "TransType (Required for BEFTN)",
        "RTGS (Y)", // 14
        "First Party Name",
        "First Party Information Line 1",
        "First Party Information Line 2",
        "First Party Information Line 3"
            
    };
    
    // To determine the field index of routng number and rtgs
    protected static final int CSV_HSBC_PAYEE_ACCOUNT_NO_INDEX = 3;
    protected static final int CSV_HSBC_COMPANY_ACCOUNT_NO_INDEX = 6;
    protected static final int CSV_HSBC_ROUTING_NUMBER_INDEX = 4;
    protected static final int CSV_HSBC_RTGS_FLAG_INDEX = 14;
    protected static final int CSV_HSBC_AMOUNT_INDEX = 1;
    protected static final int CSV_HSBC_VALUE_DATE_INDEX = 5;

    /* 
    Sequnece number for generating HSBC CSV file from table columns which has been
    normalized as EPIC Sequence.
    
    This is required for iteration and pulling the right column value
    as per the index position arrange in an array.
     */
    protected static final int[] SEQ_HSBC = {4, 9, 5, 6, 8, 11, 12, 10, 14, 15, 13, 16, 7, 17, 3, 18, 19, 20, 21};

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

    static final Font FONT_HELVETICA_BOLD_12 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
    static final Font FONT_HELVETICA_BOLD_7 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, BaseColor.BLACK);
    static final Font FONT_HELVETICA_NORMAL_7 = FontFactory.getFont(FontFactory.HELVETICA, 7, BaseColor.BLACK);
    static final Font FONT_HELVETICA_BOLD_8 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
    static final Font FONT_HELVETICA_NORMAL_8 = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
    static final String FOOTER_1 = "EPIC GROUP, House #17, Road #15 (Rabindra Swarani), Sector #3, "
            + "Uttara, Dhaka 1206, Bangladesh";
    static final String FOOTER_2 = "Ph: +8801793593422, Email: epic@epicbd.com";

}
