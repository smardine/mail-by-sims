package imap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.googlecode.jdeltasync.AuthenticationException;
import com.googlecode.jdeltasync.DeltaSyncClient;
import com.googlecode.jdeltasync.DeltaSyncClientHelper;
import com.googlecode.jdeltasync.DeltaSyncException;
import com.googlecode.jdeltasync.Message;

public class TestReceive {
	public static void main(String[] args) {

		DeltaSyncClientHelper client = new DeltaSyncClientHelper(
				new DeltaSyncClient(), "username@hotmail.com", "password");
		try {
			client.login();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeltaSyncException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message[] messages = null;
		try {
			messages = client.getMessages(client.getInbox());

		} catch (DeltaSyncException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(messages.length + " messages in Inbox");
		for (Message message : messages) {
			File file = new File(message.getId() + ".msg");
			System.out.println("Downloading message from \""
					+ message.getFrom() + "\" with subject \""
					+ message.getSubject() + "\" received at "
					+ message.getDateReceived());
			OutputStream out = null;
			try {
				out = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				client.downloadMessageContent(message, out);
			} catch (DeltaSyncException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
