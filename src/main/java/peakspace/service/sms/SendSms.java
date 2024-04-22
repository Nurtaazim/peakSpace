package peakspace.service.sms;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;
import java.util.Random;

public class SendSms {

    public void btn_sendActionPerformed(String phoneNumber, String otp) {
        // This URL is used for sending messages
        String myURI = "https://api.bulksms.com/v1/messages";

        // change these values to match your own account
        String myUsername = "arstanbeekovvv";
        String myPassword = "mirlan001m.";

        // the details of the message we want to send
        String myData = "{to: \"" + phoneNumber + "\", body: \"Your OTP is:" + otp + "\"}";

        // if your message does not contain unicode, the "encoding" is not required:
        // String myData = "{to: \"1111111\", body: \"Hello Mr. Smith!\"}";

        // build the request based on the supplied settings
        URL url = null;
        try {
            url = new URL(myURI);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection request = null;
        try {
            request = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        request.setDoOutput(true);

        // supply the credentials
        String authStr = myUsername + ":" + myPassword;
        String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());
        request.setRequestProperty("Authorization", "Basic " + authEncoded);

        // we want to use HTTP POST
        try {
            request.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        request.setRequestProperty("Content-Type", "application/json");

        // write the data to the request
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(request.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.write(myData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // try ... catch to handle errors nicely
        try {
            // make the call to the API
            InputStream response = request.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(response));
            String replyText;
            while ((replyText = in.readLine()) != null) {
                System.out.println(replyText);
            }
            in.close();
        } catch (
                IOException ex) {
            System.out.println("An error occurred:" + ex.getMessage());
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getErrorStream()));
            // print the detail that comes with the error
            String replyText;
            while (true) {
                try {
                    if (!((replyText = in.readLine()) != null)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(replyText);
            }
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
