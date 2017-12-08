import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class rehab50001bot extends TelegramLongPollingBot {

    String state = "Initial";

    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage();

            if (message_text.equals("/start")) {
                state = "Name";
                message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("What is your name?");
            } else if (state.equals("Name")) {
                message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Thank you :) Would you like to start your rehab session now?");

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("patient_list");


                Map<String, Patient> users = new HashMap<String, Patient>();
                users.put(message_text, new Patient(chat_id, message_text));

                ref.setValueAsync(users);

                state = "Initial";

            } else if (message_text.contains("make appointment")) {
                message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("When would you like your next appointment?");

                state = "Appointment";
            } else if (state.equals("Appointment")) {
                message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Your appointment is approved :)");

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("appointment");

                Map<String, Patient> users = new HashMap<String, Patient>();
                users.put(message_text, new Patient(chat_id, message_text));

                ref.setValueAsync(users);

                state = "Initial";
            }

            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "rehab50001bot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "461023094:AAEsa7wLdUozAj8ya1wLZVrErinGnBmanCI";
    }

}
