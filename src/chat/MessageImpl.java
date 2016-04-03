package chat;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

@SuppressWarnings("serial")
public class MessageImpl  extends UnicastRemoteObject implements Message
{
	protected PropertyChangeSupport propertyChangeSupport;
	private String senderName;
	private String text;
	private String messageID;
	private Set<String> recipientsList;

	public MessageImpl() throws RemoteException
	{
	    propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public void sendFrom(String senderName, String msgText, String msgID,
			Set<String> recipientsList) throws RemoteException
	{
	    String oldText = this.text;
	    this.text = msgText;
	    this.messageID = msgID;
	    this.recipientsList = recipientsList;
	    this.senderName = senderName;
	    propertyChangeSupport.firePropertyChange("messageReceived",oldText, text);
	}
	
	public String getSender() throws RemoteException
	{
		return this.senderName;
	}
	
	public String getMessageText() throws RemoteException
	{
		return this.text;
	}
	
	public String getTextID() throws RemoteException
	{
		return this.messageID;
	}
	
	public Set<String> getRecipients() throws RemoteException
	{
		return this.recipientsList;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) throws RemoteException
	{
	    propertyChangeSupport.addPropertyChangeListener(listener);
	}
}
