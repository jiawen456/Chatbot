import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SendNotification {

    private static final Logger logger = LogManager.getLogger("SendNotification");

    public static final String BASEURL = "https://api.telegram.org/bot";
    public static final String TOKEN = "461023094:AAEsa7wLdUozAj8ya1wLZVrErinGnBmanCI";
    public static final String PATH = "sendmessage";

    public static final String CHATID_FIELD = "chat_id";
    private Integer chatId ; ///< Unique identifier for the message recepient — User or GroupChat id
    public static final String TEXT_FIELD = "text";
    private String text; ///< Text of the message to be sent

    public SendNotification(Integer chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }

    void execute() {

        String url = BASEURL + TOKEN + "/" + PATH;
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
        httppost.addHeader("charset", "UTF-8");

        HttpClient client = new DefaultHttpClient();

        /// Create list of parameters
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        /// Add chatid to the list
        nameValuePairs.add(new BasicNameValuePair(CHATID_FIELD, chatId + ""));
        /// Add text to the list
        nameValuePairs.add(new BasicNameValuePair(TEXT_FIELD, text));

        try {
            /// Set list of parameters as entity of the Http POST method
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = client.execute(httppost);

            logger.info("\nSending 'POST' request to URL : " + url);
            logger.info("Post parameters : " + httppost.getEntity());
            logger.info("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            logger.info(result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

