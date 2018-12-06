/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ShaerulH
 *
 * http://developer.muthofun.com/sms.php?username=shaerul&password=joarder123&mobiles=01793593411&sms=Test&uniccode=0
 *
 */
public class PaymentSendSMS {

    //private static final String LINE_BREAK = "%0D%0A";
    private static final String LINE_BREAK = "\n";
    private static final String URL = "http://developer.muthofun.com/sms.php?";
    private static final String PASSWORD = "password=joarder123";
    private static final String USERNAME = "username=shaerul";
    private static final String MOBILE = "mobiles=";
    private static final String SMS = "sms=";
    private static final String CODE = "uniccode=0";
    private static final String END_KEY_PAIR = "&";
    private static final String POST_PARAMS = "userName=Pankaj";

    private static final String USER_AGENT = "Mozilla/5.0";
    //private static final String GET_URL = "http://localhost:9090/SpringMVCExample";

    private static void sendPOST(String POST_URL) throws IOException {
        
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }

    private static void sendGET(String GET_URL) throws IOException {

        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                System.out.println(inputLine);
            }

            in.close();

            // print result
            System.out.println(response.toString());
        
        } else {
            
            System.out.println("GET request not worked");
        
        }

    }

    /**
     * Test sending e-mail with attachments
     */
    public static void main(String[] args) {

        try {

            String sms = "EPIC Payment Notification" + LINE_BREAK;
            sms += "Amt: 4,00,000.00" + LINE_BREAK;
            sms += "Date: 03/12/2018" + LINE_BREAK;
            sms += "Bill: Multiple(15)" + LINE_BREAK;
            sms += "Unit: GTL" + LINE_BREAK;
            sms += "Bank Transfer: DBBL" + LINE_BREAK;
            sms += "Thank you";
            
            sms = URLEncoder.encode(sms, "UTF-8");

            String msg = URL + USERNAME + END_KEY_PAIR;
            msg += PASSWORD + END_KEY_PAIR;
            //msg += MOBILE + "01676217242" + END_KEY_PAIR;
            //msg += MOBILE + "01754120790" + END_KEY_PAIR;
            msg += MOBILE + "01793593411" + END_KEY_PAIR;
            msg += SMS + sms + END_KEY_PAIR;
            msg += CODE;
            //System.out.println(msg);
            System.out.println("SMS Length (Character): " + sms.length());

            sendGET(msg);

        } catch (MalformedURLException ex) {
            Logger.getLogger(PaymentSendSMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PaymentSendSMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
