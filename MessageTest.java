
package com.mycompany.programmingassignmentpart1;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    // TEST 1: Check that a message ID is valid.
    @Test
    public void testMessageIDIsValid() {
        Message message = new Message(1, "+27815780081", "Hello there");

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    // TEST 2: Check a valid recipient number.
    @Test
    public void testValidRecipientCell() {
        Message message = new Message(1, "+27815780081", "Hello there");

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    // TEST 3: Check an invalid recipient number.
    @Test
    public void testInvalidRecipientCell() {
        Message message = new Message(1, "0815780081", "Hello there");

        assertTrue(
                message.checkRecipientCell().contains("incorrectly formatted")
        );
    }

    // TEST 4: Check a valid message.
    @Test
    public void testValidMessage() {
        Message message = new Message(1, "+27815780081", "Hello there");

        assertEquals(
                "Message ready to send.",
                message.validateMessage()
        );
    }

    // TEST 5: Check that an empty message is rejected.
    @Test
    public void testEmptyMessage() {
        Message message = new Message(1, "+27815780081", "");

        assertEquals(
                "Please enter a message.",
                message.validateMessage()
        );
    }

    // TEST 6: Check that a message hash is created.
    @Test
    public void testMessageHashIsCreated() {
        Message message = new Message(1, "+27815780081", "Hello there");

        assertNotNull(message.getMessageHash());
        assertFalse(message.getMessageHash().isEmpty());
    }

    // TEST 7: Check that the longest message can be found.
    @Test
    public void testFindLongestMessage() {
        String messageText = "L".repeat(250);

        Message message = new Message(
                1, "+27815780081", messageText
        );

        message.sendMessage("1");

        String result = Message.findLongestMessage();

        assertTrue(result.contains(messageText));
    }

    // TEST 8: Search for a message using its ID.
    @Test
    public void testSearchMessageByID() {
        Message message = new Message(
                1, "+27815780081", "Unique ID search test"
        );

        message.sendMessage("1");

        String result = Message.searchMessageByID(
                message.getMessageID()
        );

        assertTrue(result.contains("Unique ID search test"));
        assertTrue(result.contains("+27815780081"));
    }

    // TEST 9: Search for messages using a recipient number.
    @Test
    public void testSearchMessagesByRecipient() {
        String recipient = "+27815780082";

        Message message = new Message(
                1, recipient, "Unique recipient search test"
        );

        message.sendMessage("1");

        String result = Message.searchMessagesByRecipient(recipient);

        assertTrue(result.contains("Unique recipient search test"));
        assertTrue(result.contains(recipient));
    }

    // TEST 10: Delete a stored message using its hash.
    @Test
    public void testDeleteMessageByHash() throws Exception {
        File testFile = File.createTempFile(
                "quickchat-test-", ".json"
        );
        testFile.deleteOnExit();

        Message message = new Message(
                1, "+27815780083", "Unique deletion test"
        );

        message.sendMessage("3");
        message.storeMessage(testFile.getAbsolutePath());

        String result = Message.deleteMessageByHash(
                message.getMessageHash(),
                testFile.getAbsolutePath()
        );

        assertEquals("Message successfully deleted.", result);

        assertEquals(
                "Message not found.",
                Message.searchMessageByID(message.getMessageID())
        );
    }

    // TEST 11: Check that an invalid message hash cannot be deleted.
    @Test
    public void testInvalidMessageHash() {
        String result = Message.deleteMessageByHash(
                "NOTAREALHASH", "messages.json"
        );

        assertEquals("Message hash not found.", result);
    }
}
