import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import org.apache.commons.io.FileExistsException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.generics.LongPollingBot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger("HelloWorld");

    public static void main(String[] args) {

        //Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        BotSession session = null;
        // Register our bot
        try {
            session = botsApi.registerBot(new rehab50001bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        try {

            // Fetch the service account key JSON file contents
            FileInputStream serviceAccount = new FileInputStream("D:/50.001-Introduction-to-Information-Systems-and-Programming/serviceAccountKey.json");

// Initialize the app with a custom auth variable, limiting the server's access
            Map<String, Object> auth = new HashMap<String, Object>();
//            auth.put("uid", "my-service-worker");
//
            FirebaseOptions options = new FirebaseOptions.Builder()
                    // .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl("https://hello-4b1e4.firebaseio.com")
//                    .setDatabaseAuthVariableOverride(null)
                    .build();
            FirebaseApp.initializeApp(options);

        } catch (FileExistsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference sessionStart = database.getReference("session");

        sessionStart.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                logger.info("changed");
                String str = dataSnapshot.toString();
                logger.info("str = " + str);
                logger.info("The value changed is " + dataSnapshot.getValue());
                logger.info("The key is " + dataSnapshot.getKey());

                if (dataSnapshot.getValue().toString().equals("started")) {
                    SendNotification start = new SendNotification(47627672, "Nice! We see that your session has started :)");
                    start.execute();
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                logger.info("The read failed: " + databaseError.getCode());
            }
        });


    }

}