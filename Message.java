/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.programmingassignmentpart1;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 * @author nyath
 */

public class Message {

    // Sets the maximum number of characters allowed in a message.
    private static final int MAX_MESSAGE_LENGTH = 250;
    private static int totalMessagesSent = 0;

    private String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private String messageHash;
    private String status;

    // Maximum number of messages the application can manage.
private static final int MAX_MESSAGES = 100;

    // Arrays to store messages according to their status.
private static Message[] sentMessages = new Message[MAX_MESSAGES];
private static Message[] disregardedMessages = new Message[MAX_MESSAGES];
private static Message[] storedMessages = new Message[MAX_MESSAGES];

    // Parallel arrays to store message hashes and message IDs.
private static String[] messageHashes = new String[MAX_MESSAGES];
private static String[] messageIDs = new String[MAX_MESSAGES];

    // Counters to track the number of messages in each array.
private static int sentCount = 0;
private static int disregardedCount = 0;
private static int storedCount = 0;
private static int messageCount = 0;

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    private String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L
                + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(number);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient == null
                || !recipient.matches("\\+[1-9][0-9]{7,14}")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

        return "Cell phone number successfully captured.";
    }

    public String validateMessage() {
        if (messageText == null || messageText.trim().isEmpty()) {
            return "Please enter a message.";
        }

        if (messageText.length() > MAX_MESSAGE_LENGTH) {
            int excess = messageText.length() - MAX_MESSAGE_LENGTH;
            return "Message exceeds 250 characters by " + excess
                    + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    public String createMessageHash() {
        String firstTwoDigits = messageID.substring(0, 2);

        String text = messageText == null ? "" : messageText.trim();
        String[] words = text.isEmpty() ? new String[0] : text.split("\\s+");

        String firstWord = words.length > 0
                ? words[0].replaceAll("[^a-zA-Z0-9]", "")
                : "";

        String lastWord = words.length > 0
                ? words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "")
                : "";

        return (firstTwoDigits + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    
public String sendMessage(String choice) {

    // Check whether the message is valid.
    if (!"Message ready to send.".equals(validateMessage())) {
        return validateMessage();
    }

    // Check whether the recipient number is valid.
    if (!"Cell phone number successfully captured."
            .equals(checkRecipientCell())) {
        return checkRecipientCell();
    }

    // Make sure there is space in the arrays.
    if (messageCount >= MAX_MESSAGES) {
        return "Message storage is full.";
    }

    // Send the message.
    if ("1".equals(choice)) {
        status = "Sent";

        sentMessages[sentCount] = this;
        sentCount++;

        totalMessagesSent++;

        messageHashes[messageCount] = messageHash;
        messageIDs[messageCount] = messageID;
        messageCount++;

        return "Message successfully sent.";
    }

    // Disregard the message.
    if ("2".equals(choice)) {
        status = "Disregarded";

        disregardedMessages[disregardedCount] = this;
        disregardedCount++;

        messageHashes[messageCount] = messageHash;
        messageIDs[messageCount] = messageID;
        messageCount++;

        return "Press 0 to delete the message.";
    }

    // Store the message.
    if ("3".equals(choice)) {
        status = "Stored";

        storedMessages[storedCount] = this;
        storedCount++;

        messageHashes[messageCount] = messageHash;
        messageIDs[messageCount] = messageID;
        messageCount++;

        return "Message successfully stored.";
    }

    return "Invalid option. Please select 1, 2 or 3.";
}


    
public static String printMessages() {

    StringBuilder result = new StringBuilder();

    // Loop through the sent messages array.
    for (int i = 0; i < sentCount; i++) {
        result.append(sentMessages[i].toString())
              .append("\n\n");
    }

    return result.toString();
}


    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    public String storeMessage(String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            String json = "{"
                    + "\"messageID\":\"" + escapeJson(messageID) + "\","
                    + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                    + "\"recipient\":\"" + escapeJson(recipient) + "\","
                    + "\"message\":\"" + escapeJson(messageText) + "\","
                    + "\"status\":\"" + escapeJson(status) + "\""
                    + "}";
            writer.write(json + System.lineSeparator());
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Message could not be stored: " + e.getMessage();
        }
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    public String toString() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText
                + "\nStatus: " + status;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getStatus() {
        return status;
    }

    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }
    
    public static String readStoredMessages(String fileName) {

    StringBuilder result = new StringBuilder();
    Gson gson = new Gson();

    // Reset the stored messages array before loading.
    storedCount = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

        String line;

        while ((line = reader.readLine()) != null) {

            JsonObject jsonObject = gson.fromJson(line, JsonObject.class);

            String messageID = jsonObject.get("messageID").getAsString();
            String messageHash = jsonObject.get("messageHash").getAsString();
            String recipient = jsonObject.get("recipient").getAsString();
            String messageText = jsonObject.get("message").getAsString();
            String status = jsonObject.get("status").getAsString();

            if ("Stored".equals(status) && storedCount < MAX_MESSAGES) {

                Message message = new Message(
        storedCount + 1,
        recipient,
        messageText
);

// Restore the original details from the JSON file.
message.messageID = messageID;
message.messageHash = messageHash;
message.status = status;

                storedMessages[storedCount] = message;
storedCount++;

// Check whether this message ID is already in the parallel arrays.
boolean alreadyExists = false;

for (int i = 0; i < messageCount; i++) {
    if (messageID.equals(messageIDs[i])) {
        alreadyExists = true;
        break;
    }
}

// Add the ID and hash only if they are not already recorded.
if (!alreadyExists && messageCount < MAX_MESSAGES) {
    messageHashes[messageCount] = messageHash;
    messageIDs[messageCount] = messageID;
    messageCount++;
}

                result.append(message.toString()).append("\n\n");
            }
        }

    } catch (IOException e) {
        return "Error reading stored messages: " + e.getMessage();
    }

    if (result.length() == 0) {
        return "No stored messages found.";
    }

    return result.toString();
}
    
    public static String findLongestMessage() {

    Message longestMessage = null;
    int longestLength = 0;

    // Check sent messages.
    for (int i = 0; i < sentCount; i++) {
        if (sentMessages[i].getMessageText().length() > longestLength) {
            longestLength = sentMessages[i].getMessageText().length();
            longestMessage = sentMessages[i];
        }
    }

    // Check stored messages.
    for (int i = 0; i < storedCount; i++) {
        if (storedMessages[i].getMessageText().length() > longestLength) {
            longestLength = storedMessages[i].getMessageText().length();
            longestMessage = storedMessages[i];
        }
    }

    if (longestMessage == null) {
        return "No messages available.";
    }

    return "Longest Message:\n" + longestMessage.toString()
            + "\nMessage Length: " + longestLength + " characters.";
}
    
    
public static String searchMessageByID(String searchID) {

    // Search sent messages.
    for (int i = 0; i < sentCount; i++) {
        if (sentMessages[i].getMessageID().equals(searchID)) {
            return "Message found!\n"
                    + "Recipient: " + sentMessages[i].getRecipient()
                    + "\nMessage: " + sentMessages[i].getMessageText();
        }
    }

    // Search disregarded messages.
    for (int i = 0; i < disregardedCount; i++) {
        if (disregardedMessages[i].getMessageID().equals(searchID)) {
            return "Message found!\n"
                    + "Recipient: " + disregardedMessages[i].getRecipient()
                    + "\nMessage: " + disregardedMessages[i].getMessageText();
        }
    }

    // Search stored messages.
    for (int i = 0; i < storedCount; i++) {
        if (storedMessages[i].getMessageID().equals(searchID)) {
            return "Message found!\n"
                    + "Recipient: " + storedMessages[i].getRecipient()
                    + "\nMessage: " + storedMessages[i].getMessageText();
        }
    }

    return "Message not found.";
}

    
public static String searchMessagesByRecipient(String searchRecipient) {

    StringBuilder result = new StringBuilder();

    // Search sent messages.
    for (int i = 0; i < sentCount; i++) {
        if (sentMessages[i].getRecipient().equals(searchRecipient)) {
            result.append(sentMessages[i].toString()).append("\n\n");
        }
    }

    // Search disregarded messages.
    for (int i = 0; i < disregardedCount; i++) {
        if (disregardedMessages[i].getRecipient().equals(searchRecipient)) {
            result.append(disregardedMessages[i].toString()).append("\n\n");
        }
    }

    // Search stored messages.
    for (int i = 0; i < storedCount; i++) {
        if (storedMessages[i].getRecipient().equals(searchRecipient)) {
            result.append(storedMessages[i].toString()).append("\n\n");
        }
    }

    if (result.length() == 0) {
        return "No messages found for this recipient.";
    }

    return "Messages for recipient " + searchRecipient + ":\n\n"
            + result.toString();
}


public static String deleteMessageByHash(String searchHash, String fileName) {

    // Search sent messages.
    for (int i = 0; i < sentCount; i++) {
        if (sentMessages[i].getMessageHash().equals(searchHash)) {

            for (int j = i; j < sentCount - 1; j++) {
                sentMessages[j] = sentMessages[j + 1];
            }

            sentMessages[--sentCount] = null;
            removeMessageFromRegistry(searchHash);

            return "Message successfully deleted.";
        }
    }

    // Search disregarded messages.
    for (int i = 0; i < disregardedCount; i++) {
        if (disregardedMessages[i].getMessageHash().equals(searchHash)) {

            for (int j = i; j < disregardedCount - 1; j++) {
                disregardedMessages[j] = disregardedMessages[j + 1];
            }

            disregardedMessages[--disregardedCount] = null;
            removeMessageFromRegistry(searchHash);

            return "Message successfully deleted.";
        }
    }

    // Search stored messages.
    for (int i = 0; i < storedCount; i++) {
        if (storedMessages[i].getMessageHash().equals(searchHash)) {

            for (int j = i; j < storedCount - 1; j++) {
                storedMessages[j] = storedMessages[j + 1];
            }

            storedMessages[--storedCount] = null;
            removeMessageFromRegistry(searchHash);

            // Update the JSON file after deleting a stored message.
            try (FileWriter writer = new FileWriter(fileName, false)) {

                Gson gson = new Gson();

                for (int j = 0; j < storedCount; j++) {
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("messageID", storedMessages[j].getMessageID());
                    jsonObject.addProperty("messageHash", storedMessages[j].getMessageHash());
                    jsonObject.addProperty("recipient", storedMessages[j].getRecipient());
                    jsonObject.addProperty("message", storedMessages[j].getMessageText());
                    jsonObject.addProperty("status", storedMessages[j].getStatus());

                    writer.write(gson.toJson(jsonObject));
                    writer.write(System.lineSeparator());
                }

            } catch (IOException e) {
                return "Message deleted from memory, but JSON file could not be updated: "
                        + e.getMessage();
            }

            return "Message successfully deleted.";
        }
    }

    return "Message hash not found.";
}


// Removes the deleted message's hash and ID from the parallel arrays.
private static void removeMessageFromRegistry(String searchHash) {

    for (int i = 0; i < messageCount; i++) {

        if (messageHashes[i].equals(searchHash)) {

            for (int j = i; j < messageCount - 1; j++) {
                messageHashes[j] = messageHashes[j + 1];
                messageIDs[j] = messageIDs[j + 1];
            }

            messageHashes[--messageCount] = null;
            messageIDs[messageCount] = null;

            return;
        }
    }
}

}
