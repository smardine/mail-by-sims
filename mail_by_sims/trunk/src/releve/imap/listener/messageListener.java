/**
 * 
 */
package releve.imap.listener;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import mdl.MlCompteMail;

/**
 * @author smardine
 */
public class messageListener implements MessageChangedListener,
		MessageCountListener, FolderListener, ConnectionListener {

	private final MlCompteMail cptMail;
	private final Folder fldr;

	public messageListener(MlCompteMail p_cpt, Folder p_fldr) {
		this.cptMail = p_cpt;
		this.fldr = p_fldr;
	}

	/**
	 * @param p_comptePop
	 */
	public messageListener(MlCompteMail p_comptePop) {
		this(p_comptePop, null);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.mail.event.MessageChangedListener#messageChanged(javax.mail.event
	 * .MessageChangedEvent)
	 */
	@Override
	public void messageChanged(MessageChangedEvent p_arg0) {
		System.out.println("message event:" + p_arg0.toString());
		System.out.println("message_changed type: "
				+ p_arg0.getMessageChangeType());
		System.out.println("source: " + p_arg0.getSource());
		System.out.println("message converné" + p_arg0.getMessage());

		// try {
		// if (fldr instanceof IMAPFolder
		// && p_arg0.getMessage() instanceof IMAPMessage) {
		// System.out.println("uid du message: "
		// + ((IMAPFolder) fldr).getUID(p_arg0.getMessage()));
		// }

		try {
			System.out.println(p_arg0.getMessage().getFrom().toString());
			System.out.println(p_arg0.getMessage().getSubject());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.mail.event.MessageCountListener#messagesAdded(javax.mail.event.
	 * MessageCountEvent)
	 */
	@Override
	public void messagesAdded(MessageCountEvent p_arg0) {
		System.out.println("tabMessage=" + p_arg0.getMessages());
		System.out.println("source=" + p_arg0.getSource());
		System.out.println("type=" + p_arg0.getType());
		;
		System.out.println("isRemoved=" + p_arg0.isRemoved());

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.mail.event.MessageCountListener#messagesRemoved(javax.mail.event
	 * .MessageCountEvent)
	 */
	@Override
	public void messagesRemoved(MessageCountEvent p_arg0) {
		System.out.println("tabMessage=" + p_arg0.getMessages());
		System.out.println("source=" + p_arg0.getSource());
		System.out.println("type=" + p_arg0.getType());

		System.out.println("isRemoved=" + p_arg0.isRemoved());

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.mail.event.FolderListener#folderCreated(javax.mail.event.FolderEvent
	 * )
	 */
	@Override
	public void folderCreated(FolderEvent p_arg0) {
		p_arg0.getFolder();
		p_arg0.getNewFolder();
		p_arg0.getSource();
		p_arg0.getType();

	}

	/**
	 * javax.mail.event.FolderListener#folderDeleted(javax.mail.event.
	 * FolderEvent
	 */
	@Override
	public void folderDeleted(FolderEvent p_arg0) {
		p_arg0.getFolder();
		p_arg0.getNewFolder();
		p_arg0.getSource();
		p_arg0.getType();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.mail.event.FolderListener#folderRenamed(javax.mail.event.FolderEvent
	 * )
	 */
	@Override
	public void folderRenamed(FolderEvent p_arg0) {
		p_arg0.getFolder();
		p_arg0.getNewFolder();
		p_arg0.getSource();
		p_arg0.getType();

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.mail.event.ConnectionListener#closed(javax.mail.event.ConnectionEvent
	 * )
	 */
	@Override
	public void closed(ConnectionEvent p_arg0) {
		switch (p_arg0.getType()) {
			case ConnectionEvent.CLOSED:
				System.out.println("type event = fermeture");
				break;
			case ConnectionEvent.DISCONNECTED:
				System.out.println("type event = deconnexion");
				break;
			case ConnectionEvent.OPENED:
				System.out.println("type event =ouverture");
				break;
		}
		System.out.println(p_arg0.toString());
	}

	/*
	 * (non-Javadoc)
	 * @seejavax.mail.event.ConnectionListener#disconnected(javax.mail.event.
	 * ConnectionEvent)
	 */
	@Override
	public void disconnected(ConnectionEvent p_arg0) {
		switch (p_arg0.getType()) {
			case ConnectionEvent.CLOSED:
				System.out.println("type event = fermeture");
			case ConnectionEvent.DISCONNECTED:
				System.out.println("type event = deconnexion");
			case ConnectionEvent.OPENED:
				System.out.println("type event =ouverture");
				break;
		}
		System.out.println(p_arg0.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.mail.event.ConnectionListener#opened(javax.mail.event.ConnectionEvent
	 * )
	 */
	@Override
	public void opened(ConnectionEvent p_arg0) {
		switch (p_arg0.getType()) {
			case ConnectionEvent.CLOSED:
				System.out.println("type event = fermeture");
			case ConnectionEvent.DISCONNECTED:
				System.out.println("type event = deconnexion");
			case ConnectionEvent.OPENED:
				System.out.println("type event =ouverture");
				break;
		}
		System.out.println(p_arg0.toString());
	}

}
