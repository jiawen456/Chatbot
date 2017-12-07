import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import org.apache.commons.io.FileExistsException;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.generics.LongPollingBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger("HelloWorld");

//    public void run() {
//        //Initialize Api Context
//        ApiContextInitializer.init();
//
//        // Instantiate Telegram Bots API
//        TelegramBotsApi botsApi = new TelegramBotsApi();
//        BotSession session = null;
//        // Register our bot
//        try {
//            session = botsApi.registerBot(new rehab50001bot());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Running");
//
//        try {
//            Thread.sleep(19000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        session.stop();

//    }

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
        DatabaseReference ref = database.getReference("server/saving-data/fireblog");

        DatabaseReference usersRef = ref.child("users");

        Map<String, User> users = new HashMap<String, User>();
        users.put("alanisawesome", new User("June 23, 1912", "Alan Turing"));
        users.put("gracehop", new User("December 9, 1906", "Grace Hopper"));
        users.put("grace", new User("December 25, 1906", "Grace"));

        usersRef.setValueAsync(users);

        // Get a reference to our posts
        DatabaseReference ref3 = database.getReference("server/saving-data/fireblog/users/grace");

        logger.info("print once");

        // Attach a listener to read the data at our posts reference
        ref3.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                logger.info("changed");
                String str = dataSnapshot.toString();
                logger.info("str = " + str);
                logger.info("The value changed is " + dataSnapshot.getValue());
                logger.info("The key is " + dataSnapshot.getKey());
            }

            public void onCancelled(DatabaseError databaseError) {
                logger.info("The read failed: " + databaseError.getCode());
            }
        });

        ref3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                logger.info("The updated post title is: " + dataSnapshot.getValue());
                logger.info("The key child is " + dataSnapshot.getKey());
                logger.info("prev child is " + prevChildKey);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

//        ScheduledExecutorService scheduler
//                = Executors.newSingleThreadScheduledExecutor();
//
//
//        Runnable task = new Main();
//        int initialDelay = 10;
//        int periodicDelay = 20;
//
//        scheduler.scheduleAtFixedRate(task, initialDelay, periodicDelay,
//                TimeUnit.SECONDS);

    }

}

