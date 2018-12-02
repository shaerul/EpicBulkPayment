/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author ShaerulH
 */
public class PaymentAdviceGenerator {

    static final String HEADER = "PAYMENT ADVICE";
    static final Font FONT_HELVETICA_BOLD_12 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
    static final Font FONT_HELVETICA_BOLD_7 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, BaseColor.BLACK);
    static final Font FONT_HELVETICA_NORMAL_7 = FontFactory.getFont(FontFactory.HELVETICA, 7, BaseColor.BLACK);
    static final Font FONT_HELVETICA_BOLD_8 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
    static final Font FONT_HELVETICA_NORMAL_8 = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
    static final String FOOTER_1 = "EPIC GROUP, House #17, Road #15 (Rabindra Swarani), Sector #3, "
            + "Uttara, Dhaka 1206, Bangladesh";
    static final String FOOTER_2 = "Ph: +8801793593422, Email: epic@epicbd.com";

    public void GenerateImagePDF() {

        try {

            Image image = Image.getInstance("image/logo.png");
            image.scalePercent(60f);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("e:\\iTextImageExample.pdf"));
            document.open();

            Paragraph header = new Paragraph(HEADER + "\n\n", FONT_HELVETICA_BOLD_12);
            header.setAlignment(Element.ALIGN_CENTER);

            Paragraph title = new Paragraph(" " + new Date().toString(), FONT_HELVETICA_BOLD_8);

            title.setAlignment(Element.ALIGN_LEFT);
            title.add("\n Bangla Fabric Ltd.");
            title.add("\n A11 Bagerhat Road");
            title.add("\n Bagherhat, Khulna");
            title.add("\n Bangladesh");
            title.add("\n\n\n");

            // Generating the Table
            PdfPTable table = new PdfPTable(columnHeaders.length); // Number of Columns
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setWidthPercentage(100);
            //table.setLockedWidth(true);
            addTableHeader(table);

            addCustomRows(table, tableRow_1, FONT_HELVETICA_NORMAL_7);
            addCustomRows(table, tableRow_2, FONT_HELVETICA_NORMAL_7);
            addCustomRows(table, tableTotal, FONT_HELVETICA_BOLD_7);

            PdfContentByte cb = writer.getDirectContent();
            Barcode128 code128 = new Barcode128();
            code128.setCode("14785236987541");
            code128.setCodeType(Barcode128.CODE128);
            Image code128Image = code128.createImageWithBarcode(cb, null, null);

            //Footer
            document.add(image);
            document.add(header);
            document.add(title);
            document.add(table);
            document.add(new Paragraph("\n ", FONT_HELVETICA_BOLD_8));
            document.add(code128Image);
            onEndPageFooter(writer, document);
            document.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PaymentAdviceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PaymentAdviceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PaymentAdviceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(PaymentAdviceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static final String[] columnHeaders = {
        "Invoice/Bill",
        "Date",
        "Particulars",
        "Gross Amount",
        "VAT",
        "AIT",
        "Other Deduction",
        "Net Amount",
        "Voucher"
    };

    public static final String[] tableRow_1 = {
        "Bill-1",
        "01-07-18",
        "",
        "50,000.00",
        "6,521.74",
        "869.57",
        "10.00",
        "42,598.70",
        "1813000407"
    };

    public static final String[] tableRow_2 = {
        "Bill-2",
        "15-07-18",
        "",
        "70,000.00",
        "9,130.43",
        "1,217.39",
        "20.00",
        "59,632.17",
        "1813000407"
    };

    public static final String[] tableTotal = {
        "",
        "",
        "",
        "120,000.00",
        "15,652.17",
        "2,086.96",
        "30.00",
        "102,230.87",
        ""
    };

    private void addTableHeader(PdfPTable table) {
        Stream.of(columnHeaders).forEach((String columnTitle) -> {

            PdfPCell headerCell = new PdfPCell(new Phrase(columnTitle, FONT_HELVETICA_BOLD_7));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(headerCell);
        });
    }

    private void addCustomRows(PdfPTable table, String[] row, Font font)
            throws URISyntaxException, BadElementException, IOException {

        Stream.of(row).forEach((String columnTitle) -> {

            PdfPCell rowCell = new PdfPCell(new Phrase(columnTitle, font));
            rowCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            rowCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(rowCell);

        });
    }

    public void GenerateAdviceInPDF() {

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("e:\\iTextHelloWorld.pdf"));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk("Hello World", font);

            document.add(chunk);
            document.close();

        } catch (FileNotFoundException ex) {

            Logger.getLogger(PaymentAdviceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PaymentAdviceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onEndPageFooter(PdfWriter writer, Document document) {

        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase(FOOTER_2, FONT_HELVETICA_NORMAL_7);

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 20, 0);
        
        footer = new Phrase(FOOTER_1, FONT_HELVETICA_NORMAL_7);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

    public static void main(String args[]) {

        //new PaymentAdviceGenerator().GenerateAdviceInPDF();
        new PaymentAdviceGenerator().GenerateImagePDF();

    }
}
