import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.commons.io.FileExistsException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Firebase {

    public Firebase() {
        try {

            // Fetch the service account key JSON file contents
            FileInputStream serviceAccount = new FileInputStream("D:/50.001-Introduction-to-Information-Systems-and-Programming/serviceAccountKey.json");

// Initialize the app with a custom auth variable, limiting the server's access
            Map<String, Object> auth = new HashMap<String, Object>();
//            auth.put("uid", "my-service-worker");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    // .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl("https://hello-4b1e4.firebaseio.com")
//                    .setDatabaseAuthVariableOverride(auth)
                    .build();
            FirebaseApp.initializeApp(options);

            // The app only has access as defined in the Security Rules
            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference("/some_resource");

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
        users.put("grace", new User("December 10, 1906", "Grace"));

        usersRef.setValueAsync(users);
    }
}
